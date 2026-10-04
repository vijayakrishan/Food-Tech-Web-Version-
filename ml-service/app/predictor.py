import os
import joblib
import json
import shap
import numpy as np
import pandas as pd
from typing import List, Dict
from app.models import PredictionRequest, PredictionResponse, ModelInfo

class Predictor:
    def __init__(self):
        self.models_dir = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "models")
        self.model = None
        self.scaler = None
        self.feature_names = None
        self.metrics = None
        self.feature_importance = None
        self.explainer = None
        self._load_assets()

    def _load_assets(self):
        try:
            self.model = joblib.load(os.path.join(self.models_dir, "xgb_model.joblib"))
            self.scaler = joblib.load(os.path.join(self.models_dir, "scaler.joblib"))
            self.feature_names = joblib.load(os.path.join(self.models_dir, "feature_names.joblib"))
            
            with open(os.path.join(self.models_dir, "metrics.json"), "r") as f:
                self.metrics = json.load(f)
            
            with open(os.path.join(self.models_dir, "feature_importance.json"), "r") as f:
                self.feature_importance = json.load(f)
                
        except Exception as e:
            print(f"Error loading models: {e}. Model might not be trained yet.")
            
    def _get_explainer(self):
        if self.explainer is None and self.model is not None:
            self.explainer = shap.TreeExplainer(self.model)
        return self.explainer

    def _prepare_features(self, request: PredictionRequest) -> np.ndarray:
        req_dict = request.model_dump()
        # Ensure features are in the exact order as training
        features = [req_dict[f] for f in self.feature_names]
        # Scale features
        scaled = self.scaler.transform([features])
        return scaled

    def predict(self, request: PredictionRequest) -> PredictionResponse:
        if self.model is None:
            raise RuntimeError("Model is not loaded.")
            
        X_scaled = self._prepare_features(request)
        score = float(self.model.predict(X_scaled)[0])
        
        # Ensure score is within bounds due to model approximations
        score = max(0.0, min(1.0, score))
        
        explanation = self.explain(request)
        
        return PredictionResponse(
            suitability_score=score,
            shap_values=explanation["shap_values"],
            top_factors=explanation["top_factors"]
        )

    def batch_predict(self, requests: List[PredictionRequest]) -> List[PredictionResponse]:
        if self.model is None:
            raise RuntimeError("Model is not loaded.")
            
        results = []
        for req in requests:
            results.append(self.predict(req))
            
        return results

    def explain(self, request: PredictionRequest) -> Dict:
        if self.model is None:
            raise RuntimeError("Model is not loaded.")
            
        explainer = self._get_explainer()
        X_scaled = self._prepare_features(request)
        
        shap_values = explainer.shap_values(X_scaled)[0]
        
        shap_dict = {feat: float(val) for feat, val in zip(self.feature_names, shap_values)}
        
        # Sort factors by absolute magnitude
        top_factors = []
        for feat, val in sorted(shap_dict.items(), key=lambda item: abs(item[1]), reverse=True):
            top_factors.append({
                "feature": feat,
                "impact": val,
                "direction": "positive" if val > 0 else "negative"
            })
            
        return {
            "shap_values": shap_dict,
            "top_factors": top_factors
        }

    def get_model_info(self) -> ModelInfo:
        if self.model is None:
            raise RuntimeError("Model is not loaded.")
            
        return ModelInfo(
            model_name="XGBoost Packaging Suitability Predictor",
            algorithm="XGBoost Regressor",
            version=self.metrics.get("model_version", "1.0"),
            training_date=self.metrics.get("training_date", "Unknown"),
            dataset_size=self.metrics.get("dataset_size", 0),
            metrics={k: v for k, v in self.metrics.items() if k not in ["training_date", "model_version", "dataset_size"]},
            feature_names=self.feature_names,
            feature_importance=self.feature_importance
        )

# Global singleton
predictor = Predictor()
