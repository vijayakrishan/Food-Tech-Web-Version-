# ML Model Documentation

## Overview

The recommendation system uses an **XGBoost Regressor** to predict the suitability of a packaging material for a given food commodity under specified storage conditions.

## Problem Formulation

### Input
A feature vector combining:
- **11 food features**: moisture content, pH, fat, protein, carbohydrates, respiration rate, oxygen/moisture/light/microbial sensitivity, perishability
- **5 storage features**: temperature, humidity, shelf life, refrigeration, light exposure
- **10 packaging features**: OTR, WVTR, light transmission, thickness, tensile strength, flexibility, heat sealability, recyclability, biodegradability, cost

**Total: 26 features**

### Output
- **Suitability score**: Continuous value in [0.0, 1.0]
- 1.0 = perfect match between food requirements and packaging properties
- 0.0 = completely unsuitable combination

## Dataset

### Nature
The training dataset is a **scientifically derived/synthetic dataset** — NOT raw experimental data.

### Construction Logic
Suitability scores are computed using established food packaging science principles:

1. **Oxygen Barrier Match** (weight ~25%)
   - High oxygen-sensitive foods need low OTR packaging
   - Score = f(food.oxygen_sensitivity, packaging.OTR)

2. **Moisture Barrier Match** (weight ~20%)
   - High moisture-sensitive foods need low WVTR packaging
   - Score = f(food.moisture_sensitivity, packaging.WVTR)

3. **Light Protection** (weight ~15%)
   - Light-sensitive foods need low light transmission
   - Score = f(food.light_sensitivity, packaging.light_transmission)

4. **Temperature Compatibility** (weight ~10%)
   - Packaging must handle the storage temperature range

5. **Shelf Life Support** (weight ~15%)
   - Longer shelf life requires better overall barriers

6. **Fat/Grease Resistance** (weight ~5%)
   - High-fat foods need grease-resistant materials

7. **Mechanical Protection** (weight ~10%)
   - Adequate tensile strength and thickness

### Noise
Gaussian noise (σ ≈ 5-10%) is added to simulate real-world variation in:
- Material batch consistency
- Environmental factors
- Measurement uncertainty

### Size
- **5,000+ samples** covering diverse food-packaging combinations
- Balanced across different food categories and packaging materials

### Source References
- Material properties: Published polymer datasheets, packaging engineering references
- Food properties: USDA FoodData Central, food science literature
- Barrier requirements: Robertson (2012) "Food Packaging: Principles and Practice"
- OTR/WVTR values: ASTM standard test references

## Model Training

### Algorithm
XGBoost Regressor (gradient boosting decision trees)

### Hyperparameters
```
n_estimators: 200
max_depth: 6
learning_rate: 0.1
subsample: 0.8
colsample_bytree: 0.8
objective: reg:squarederror
eval_metric: rmse
```

### Training/Test Split
- 80% training, 20% testing
- Random seed for reproducibility

### Evaluation Metrics
| Metric | Description |
|--------|-------------|
| MAE | Mean Absolute Error |
| RMSE | Root Mean Squared Error |
| R² | Coefficient of Determination |

> **Note**: Exact metrics depend on the generated dataset. The system displays actually computed metrics, not fabricated numbers.

## Explainability (SHAP)

### Method
SHAP TreeExplainer — exact Shapley values for tree ensembles.

### What SHAP Shows
For each prediction, SHAP decomposes the score into individual feature contributions:
- **Positive SHAP value**: Feature pushes score UP (better suitability)
- **Negative SHAP value**: Feature pushes score DOWN (worse suitability)

### Example
```
Oxygen Sensitivity (High)     → +0.15  (needs good barrier → Al foil excels)
Required Shelf Life (180 days)→ +0.12  (long shelf → needs excellent barriers)
Light Sensitivity (High)      → +0.10  (needs opacity → Al foil blocks light)
Material Cost ($1.20/m²)      → -0.03  (higher cost slightly negative)
WVTR (0.1 g/m²/day)          → +0.08  (excellent moisture barrier)
```

### Visual Output
- Horizontal bar chart with positive (green) and negative (red) bars
- Top 8-10 most influential features
- Readable feature names and values

## Ranking Engine

The final ranking combines:

```
finalScore = w_perf × suitabilityScore 
           + w_cost × costScore 
           + w_sust × sustainabilityScore 
           + w_shelf × shelfLifeScore

where:
  costScore = 1 - (material_cost / max_cost)
  sustainabilityScore = material.sustainability_score / 10
  shelfLifeScore = suitabilityScore  (proxy)
  w_perf + w_cost + w_sust + w_shelf = 1.0
```

Users control weights through preference sliders.

## Limitations

1. **Synthetic dataset**: Predictions are based on derived data, not controlled experiments
2. **Simplified model**: Real-world packaging selection involves additional factors (regulatory, supply chain, branding)
3. **Material properties are typical values**: Actual properties vary by manufacturer and grade
4. **No interaction effects**: Model may not capture complex material-food chemical interactions
5. **Temperature simplification**: Uses single storage temperature rather than temperature profiles

## Retraining

```bash
cd ml-service
python data/generate_dataset.py    # Regenerate dataset (or use new data)
python training/train_model.py     # Retrain model
# Restart the ML service to load new model
```

## File Artifacts

| File | Description |
|------|-------------|
| `models/xgb_model.joblib` | Trained XGBoost model |
| `models/scaler.joblib` | StandardScaler for feature normalization |
| `models/feature_names.joblib` | Ordered list of feature names |
| `models/metrics.json` | Training evaluation metrics |
| `models/shap_summary.png` | Global SHAP feature importance plot |
| `data/training_dataset.csv` | Generated training dataset |
