# AI-Based Food Packaging Suitability Dataset

This directory contains datasets used for training the machine learning models that recommend packaging materials for food products.

## Description
This is a **DERIVED and SYNTHETIC** dataset. It does not contain direct experimental laboratory results. Instead, it was algorithmically generated based on established principles from food science and packaging engineering literature.

## Methodology
The dataset maps a combination of **Food Properties**, **Storage Conditions**, and **Packaging Material Properties** to a target `suitability_score` (ranging from 0.0 to 1.0).

The suitability score is computed using the following scientifically-informed logic:
1. **Oxygen Barrier Match**: Foods with high `oxygen_sensitivity` (e.g., fresh meats, roasted coffee) require packaging with a low Oxygen Transmission Rate (OTR). A high OTR for such foods heavily penalizes the score.
2. **Moisture Barrier Match**: Foods with high `moisture_sensitivity` (e.g., crackers, dehydrated powders) require packaging with a low Water Vapor Transmission Rate (WVTR). A high WVTR penalizes the score.
3. **Light Protection**: Light-sensitive foods degrade under light exposure. Packaging materials with high `light_transmission` penalize the score, factored by the environmental `light_exposure`.
4. **Shelf Life Support**: Extended `required_shelf_life_days` necessitate proportionally better overall barrier properties (OTR, WVTR).
5. **Physical Integrity & Fat Resistance**: High `fat_content` requires grease resistance, approximated here through physical integrity (`tensile_strength`).
6. **Temperature Handling**: Storage temperature extremes demand packaging with appropriate `flexibility` and robustness to prevent cracking or melting.

Controlled random noise (±5%) is introduced to simulate real-world variability and measurement inaccuracies.

## Source References
The relationships and sensitivity weights embedded in the generation script are derived from:
- Robertson, G. L. (2012). *Food Packaging: Principles and Practice* (3rd ed.). CRC Press.
- Han, J. H. (2014). *Innovations in Food Packaging* (2nd ed.). Academic Press.
- Coles, R., McDowell, D., & Kirwan, M. J. (2003). *Food Packaging Technology*. Blackwell Publishing.

## Feature Overview
- **Food Features**: `moisture_content`, `ph_level`, `fat_content`, `protein_content`, `carbohydrate_content`, `respiration_rate`, `oxygen_sensitivity`, `moisture_sensitivity`, `light_sensitivity`, `microbial_sensitivity`, `is_perishable`.
- **Storage Features**: `storage_temperature`, `storage_humidity`, `required_shelf_life_days`, `is_refrigerated`, `light_exposure`.
- **Packaging Features**: `oxygen_transmission_rate`, `water_vapor_transmission_rate`, `light_transmission`, `thickness_microns`, `tensile_strength`, `flexibility`, `heat_sealability`, `recyclability_score`, `biodegradability_score`, `estimated_cost_per_sqm`.
- **Target**: `suitability_score` (Continuous: 0.0 - 1.0)

## Limitations
- **Synthetic Nature**: This dataset is synthesized for demonstration and initial ML pipeline training. It should be validated against or replaced by real empirical data for production deployment in critical food safety scenarios.
- **Simplifications**: Complex interactions like permeation of specific aroma compounds or migration of packaging monomers into food are not fully captured.

## License
This derived dataset is freely usable for educational, research, and non-commercial development purposes.
