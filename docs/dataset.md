# Dataset Documentation

## Overview

The AI Food Packaging Recommendation System uses a **scientifically derived training dataset** to train the XGBoost model. This document describes the dataset construction methodology, sources, and limitations.

## ⚠️ Important Disclaimer

This dataset is **NOT** raw experimental data from laboratory testing. It is a **derived/synthetic dataset** constructed from:

1. Published packaging material properties
2. Established food science principles
3. Known food-packaging compatibility rules

The suitability scores are computed algorithmically based on scientific literature, not measured experimentally.

## Dataset Construction

### Approach

We construct food-packaging combination samples by:

1. **Defining realistic food profiles** based on USDA FoodData Central values and food science references
2. **Defining packaging material profiles** based on published manufacturer datasheets and polymer science references
3. **Computing suitability scores** using an expert-knowledge scoring function that evaluates how well packaging properties match food requirements
4. **Adding controlled noise** to simulate real-world measurement variability

### Food Properties Sources

| Property | Source | Notes |
|----------|--------|-------|
| Moisture Content | USDA FoodData Central | Typical values for commodity categories |
| pH | Food Chemistry literature | Published ranges for food categories |
| Fat Content | USDA FoodData Central | Per 100g values |
| Protein Content | USDA FoodData Central | Per 100g values |
| Respiration Rate | Postharvest Biology references | mg CO₂/kg·hr at reference temperature |
| Oxygen Sensitivity | Food preservation literature | Categorical assessment |
| Moisture Sensitivity | Food packaging references | Categorical assessment |
| Light Sensitivity | Food degradation studies | Categorical assessment |

**Primary References:**
- USDA FoodData Central (https://fdc.nal.usda.gov/)
- Fennema's Food Chemistry, 5th Edition
- Kader, A.A. (2002). Postharvest Technology of Horticultural Crops

### Packaging Properties Sources

| Property | Source | Notes |
|----------|--------|-------|
| OTR (Oxygen Transmission Rate) | ASTM D3985 standard references | cc/m²/day at 23°C, 0% RH |
| WVTR (Water Vapor Transmission Rate) | ASTM F1249 standard references | g/m²/day at 38°C, 90% RH |
| Light Transmission | Manufacturer datasheets | % visible light transmission |
| Tensile Strength | Polymer data handbooks | MPa, machine direction |
| Thickness | Industry standard gauges | Micrometers |
| Cost | Industry price surveys | USD per m², approximate |
| Recyclability/Biodegradability | Environmental assessment literature | Scored 0-10 |

**Primary References:**
- Robertson, G.L. (2012). Food Packaging: Principles and Practice, 3rd Edition
- Massey, L.K. (2003). Permeability Properties of Plastics and Elastomers
- Packaging material manufacturer technical data sheets

### Suitability Score Computation

The suitability score for each food-packaging combination is computed as a weighted combination of sub-scores:

```
S = w₁ × S_oxygen + w₂ × S_moisture + w₃ × S_light + w₄ × S_temperature 
  + w₅ × S_shelf_life + w₆ × S_grease + w₇ × S_mechanical + noise

where:
  S ∈ [0, 1]
  noise ~ N(0, σ²), σ ≈ 0.05
```

| Sub-score | Weight | Logic |
|-----------|--------|-------|
| Oxygen Barrier (S_oxygen) | ~25% | Low OTR → high score when food has high O₂ sensitivity |
| Moisture Barrier (S_moisture) | ~20% | Low WVTR → high score when food has high moisture sensitivity |
| Light Protection (S_light) | ~15% | Low light transmission → high score when food is light sensitive |
| Temperature (S_temperature) | ~10% | Material temp range must include storage temperature |
| Shelf Life (S_shelf_life) | ~15% | Better barriers needed for longer shelf life |
| Grease Resistance (S_grease) | ~5% | Grease resistance needed for high-fat foods |
| Mechanical (S_mechanical) | ~10% | Adequate strength/thickness for food protection |

### Dataset Statistics

| Attribute | Value |
|-----------|-------|
| Total samples | 5,000+ |
| Features | 26 |
| Target | suitability_score (continuous, 0-1) |
| Food categories | 9 (Fruits, Vegetables, Grains, Processed, Dairy, Meat, Spices, Nuts, Beverages) |
| Packaging materials | 12 types |
| Missing values | None (synthetic) |

## Feature Descriptions

### Food Features (11)
| Feature | Type | Range | Description |
|---------|------|-------|-------------|
| moisture_content | Float | 0-95 | % moisture in food |
| ph_level | Float | 2-14 | pH of food |
| fat_content | Float | 0-100 | % fat content |
| protein_content | Float | 0-100 | % protein content |
| carbohydrate_content | Float | 0-100 | % carbohydrate content |
| respiration_rate | Float | 0-200 | mg CO₂/kg/hr |
| oxygen_sensitivity | Int | 0-3 | Low(0) to Very High(3) |
| moisture_sensitivity | Int | 0-3 | Low(0) to Very High(3) |
| light_sensitivity | Int | 0-3 | Low(0) to Very High(3) |
| microbial_sensitivity | Int | 0-3 | Low(0) to Very High(3) |
| is_perishable | Int | 0-1 | Binary flag |

### Storage Features (5)
| Feature | Type | Range | Description |
|---------|------|-------|-------------|
| storage_temperature | Float | -30 to 50 | °C |
| storage_humidity | Float | 0-100 | % RH |
| required_shelf_life_days | Int | 1-730 | Days |
| is_refrigerated | Int | 0-1 | Binary flag |
| light_exposure | Int | 0-2 | None(0), Low(1), High(2) |

### Packaging Features (10)
| Feature | Type | Range | Description |
|---------|------|-------|-------------|
| oxygen_transmission_rate | Float | 0-10000 | cc/m²/day |
| water_vapor_transmission_rate | Float | 0-50 | g/m²/day |
| light_transmission | Float | 0-100 | % |
| thickness_microns | Float | 5-5000 | μm |
| tensile_strength | Float | 1-200 | MPa |
| flexibility | Int | 0-2 | Low(0), Medium(1), High(2) |
| heat_sealability | Int | 0-1 | Binary flag |
| recyclability_score | Int | 0-10 | Score |
| biodegradability_score | Int | 0-10 | Score |
| estimated_cost_per_sqm | Float | 0.1-10 | USD |

### Target Variable
| Feature | Type | Range | Description |
|---------|------|-------|-------------|
| suitability_score | Float | 0.0-1.0 | Packaging-food suitability |

## Limitations

1. **Not experimental data**: Scores are algorithmically derived, not from lab tests
2. **Simplified interactions**: Does not model complex chemical interactions between food and packaging
3. **Typical values**: Material properties use typical/representative values, not specific product grades
4. **No batch variation**: Base properties are fixed per material type (noise simulates some variation)
5. **Limited environmental factors**: Does not account for altitude, transportation stress, etc.
6. **Cost approximation**: Costs are approximate industry averages

## License

This derived dataset is created for educational and research purposes. The underlying data principles are from publicly available scientific sources. The dataset may be freely used for non-commercial research and education.

## Regeneration

To regenerate the dataset with different parameters:
```bash
cd ml-service
python data/generate_dataset.py
```

Modify the script to adjust:
- Number of samples
- Noise levels
- Feature distributions
- Suitability scoring weights
