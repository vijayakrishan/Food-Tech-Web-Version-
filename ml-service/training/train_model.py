import pandas as pd
import numpy as np
import os
import json
import joblib
from datetime import datetime
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score, accuracy_score, precision_score, recall_score, f1_score
import xgboost as xgb
import shap
import matplotlib.pyplot as plt

def train():
    current_dir = os.path.dirname(os.path.abspath(__file__))
    data_path = os.path.join(os.path.dirname(current_dir), "data", "training_dataset.csv")
    models_dir = os.path.join(os.path.dirname(current_dir), "models")
    os.makedirs(models_dir, exist_ok=True)

    print(f"Loading data from {data_path}...")
    df = pd.read_csv(data_path)
    
    print("Exploratory Stats:")
    print(df.describe())

    X = df.drop(columns=['suitability_score'])
    y = df['suitability_score']
    feature_names = X.columns.tolist()

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    print("Training XGBoost model...")
    model = xgb.XGBRegressor(
        n_estimators=200,
        max_depth=6,
        learning_rate=0.1,
        subsample=0.8,
        colsample_bytree=0.8,
        random_state=42
    )
    model.fit(X_train_scaled, y_train)

    print("Evaluating model...")
    y_pred = model.predict(X_test_scaled)
    mae = mean_absolute_error(y_test, y_pred)
    rmse = np.sqrt(mean_squared_error(y_test, y_pred))
    r2 = r2_score(y_test, y_pred)

    # Classification metrics by binning scores
    # Threshold for 'suitable' could be 0.7
    threshold = 0.7
    y_test_bin = (y_test >= threshold).astype(int)
    y_pred_bin = (y_pred >= threshold).astype(int)
    
    acc = accuracy_score(y_test_bin, y_pred_bin)
    prec = precision_score(y_test_bin, y_pred_bin, zero_division=0)
    rec = recall_score(y_test_bin, y_pred_bin, zero_division=0)
    f1 = f1_score(y_test_bin, y_pred_bin, zero_division=0)

    print(f"MAE: {mae:.4f}")
    print(f"RMSE: {rmse:.4f}")
    print(f"R²: {r2:.4f}")
    print(f"Classification (Threshold {threshold}) - Acc: {acc:.4f}, Prec: {prec:.4f}, Rec: {rec:.4f}, F1: {f1:.4f}")

    print("Generating SHAP analysis...")
    explainer = shap.TreeExplainer(model)
    shap_values = explainer.shap_values(X_train_scaled[:1000]) # Sample for speed

    plt.figure(figsize=(10, 8))
    shap.summary_plot(shap_values, X_train[:1000], feature_names=feature_names, show=False)
    plt.tight_layout()
    plt.savefig(os.path.join(models_dir, "shap_summary.png"))
    plt.close()

    # Calculate global feature importance
    mean_abs_shap = np.abs(shap_values).mean(axis=0)
    feature_importance = {feat: float(val) for feat, val in zip(feature_names, mean_abs_shap)}

    print("Saving artifacts...")
    joblib.dump(model, os.path.join(models_dir, "xgb_model.joblib"))
    joblib.dump(scaler, os.path.join(models_dir, "scaler.joblib"))
    joblib.dump(feature_names, os.path.join(models_dir, "feature_names.joblib"))

    metrics = {
        "mae": float(mae),
        "rmse": float(rmse),
        "r2": float(r2),
        "classification_accuracy": float(acc),
        "classification_precision": float(prec),
        "classification_recall": float(rec),
        "classification_f1": float(f1),
        "training_date": datetime.now().isoformat(),
        "dataset_size": len(df),
        "model_version": "1.0.0"
    }

    with open(os.path.join(models_dir, "metrics.json"), "w") as f:
        json.dump(metrics, f, indent=4)
        
    # Save a separate file with feature importance
    with open(os.path.join(models_dir, "feature_importance.json"), "w") as f:
        json.dump(feature_importance, f, indent=4)

    print("Training complete.")

if __name__ == "__main__":
    train()
