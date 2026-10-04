from pydantic import BaseModel
from typing import List, Dict

class PredictionRequest(BaseModel):
    # All food features
    moisture_content: float  # 0-95
    ph_level: float  # 2-14
    fat_content: float  # 0-100
    protein_content: float  # 0-100
    carbohydrate_content: float  # 0-100
    respiration_rate: float  # 0-200
    oxygen_sensitivity: int  # 0-3
    moisture_sensitivity: int  # 0-3
    light_sensitivity: int  # 0-3
    microbial_sensitivity: int  # 0-3
    is_perishable: int  # 0 or 1
    
    # Storage features
    storage_temperature: float
    storage_humidity: float
    required_shelf_life_days: int
    is_refrigerated: int
    light_exposure: int  # 0-2
    
    # Packaging features
    oxygen_transmission_rate: float
    water_vapor_transmission_rate: float
    light_transmission: float
    thickness_microns: float
    tensile_strength: float
    flexibility: int  # 0-2
    heat_sealability: int
    recyclability_score: float
    biodegradability_score: float
    estimated_cost_per_sqm: float

class BatchPredictionRequest(BaseModel):
    predictions: List[PredictionRequest]
    
class PredictionResponse(BaseModel):
    suitability_score: float
    shap_values: Dict[str, float]  # feature_name -> SHAP value
    top_factors: List[Dict]  # sorted by absolute SHAP value

class BatchPredictionResponse(BaseModel):
    results: List[PredictionResponse]

class ModelInfo(BaseModel):
    model_name: str
    algorithm: str
    version: str
    training_date: str
    dataset_size: int
    metrics: Dict[str, float]
    feature_names: List[str]
    feature_importance: Dict[str, float]
