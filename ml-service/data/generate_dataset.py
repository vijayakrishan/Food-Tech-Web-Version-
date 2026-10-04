"""
Food Packaging Dataset Generator

This script generates a scientifically defensible synthetic dataset mapping
food properties and packaging materials to a suitability score.

The dataset is DERIVED and SYNTHETIC, not based on real experimental data.
It relies on established food science and packaging engineering principles
to logically deduce a suitability score.

Scientific logic used for suitability score:
1. Oxygen barrier match: High oxygen-sensitive foods require low Oxygen Transmission Rate (OTR).
2. Moisture barrier match: High moisture-sensitive foods require low Water Vapor Transmission Rate (WVTR).
3. Light protection: Light-sensitive foods require low light transmission packaging.
4. Temperature handling: Packaging must withstand required storage temperatures.
5. Shelf life support: Longer shelf life requires generally better overall barriers.
6. Fat/Grease handling: Foods with high fat might need specific barrier properties (represented indirectly here or by combining properties).

Sources informing the logic:
- Food Packaging Science and Technology (Robertson, G.L.)
- Principles of Food Packaging (Han, J.H.)
"""
import pandas as pd
import numpy as np
import os

def generate_dataset(num_samples=5000, output_path="training_dataset.csv"):
    np.random.seed(42)
    
    # Food features
    moisture_content = np.random.uniform(0, 95, num_samples)
    ph_level = np.random.uniform(2, 14, num_samples)
    fat_content = np.random.uniform(0, 100, num_samples)
    protein_content = np.random.uniform(0, 100, num_samples)
    carbohydrate_content = np.random.uniform(0, 100, num_samples)
    respiration_rate = np.random.uniform(0, 200, num_samples)
    oxygen_sensitivity = np.random.randint(0, 4, num_samples) # 0 to 3
    moisture_sensitivity = np.random.randint(0, 4, num_samples) # 0 to 3
    light_sensitivity = np.random.randint(0, 4, num_samples) # 0 to 3
    microbial_sensitivity = np.random.randint(0, 4, num_samples) # 0 to 3
    is_perishable = np.random.randint(0, 2, num_samples)
    
    # Storage features
    storage_temperature = np.random.uniform(-30, 50, num_samples)
    storage_humidity = np.random.uniform(0, 100, num_samples)
    required_shelf_life_days = np.random.randint(1, 731, num_samples)
    is_refrigerated = (storage_temperature < 10).astype(int)
    light_exposure = np.random.randint(0, 3, num_samples) # 0 to 2
    
    # Packaging features
    # Log-normal or exponential distributions are often better for transmission rates
    oxygen_transmission_rate = np.random.uniform(0, 10000, num_samples)
    water_vapor_transmission_rate = np.random.uniform(0, 50, num_samples)
    light_transmission = np.random.uniform(0, 100, num_samples)
    thickness_microns = np.random.uniform(5, 5000, num_samples)
    tensile_strength = np.random.uniform(1, 200, num_samples)
    flexibility = np.random.randint(0, 3, num_samples) # 0 to 2
    heat_sealability = np.random.randint(0, 2, num_samples)
    recyclability_score = np.random.randint(0, 11, num_samples) # 0 to 10
    biodegradability_score = np.random.randint(0, 11, num_samples) # 0 to 10
    estimated_cost_per_sqm = np.random.uniform(0.1, 10, num_samples)

    # Calculate Suitability Score (0 to 1)
    scores = np.ones(num_samples)
    
    # 1. Oxygen Barrier Match
    # High sensitivity (3) + High OTR (>5000) -> Bad
    # Low sensitivity (0) + Any OTR -> OK
    o2_penalty = (oxygen_sensitivity / 3.0) * (oxygen_transmission_rate / 10000.0)
    scores -= o2_penalty * 0.25
    
    # 2. Moisture Barrier Match
    h2o_penalty = (moisture_sensitivity / 3.0) * (water_vapor_transmission_rate / 50.0)
    scores -= h2o_penalty * 0.25
    
    # 3. Light Protection
    light_penalty = (light_sensitivity / 3.0) * (light_transmission / 100.0) * (light_exposure / 2.0)
    scores -= light_penalty * 0.15
    
    # 4. Shelf Life Support
    # Longer shelf life needs lower OTR and WVTR generally (simplified logic)
    shelf_life_factor = required_shelf_life_days / 730.0
    barrier_quality = 1.0 - ((oxygen_transmission_rate/10000 + water_vapor_transmission_rate/50)/2)
    shelf_life_penalty = shelf_life_factor * (1.0 - barrier_quality)
    scores -= shelf_life_penalty * 0.20
    
    # 5. Fat/Grease handling (simulated with thickness/tensile strength as a proxy for better material)
    fat_factor = fat_content / 100.0
    # Weak material + high fat -> slight penalty
    strength_factor = tensile_strength / 200.0
    fat_penalty = fat_factor * (1.0 - strength_factor)
    scores -= fat_penalty * 0.10
    
    # 6. Temperature compatibility
    # Extreme temps might require higher flexibility or thickness (simplified)
    temp_extremes = np.abs(storage_temperature - 20) / 50.0
    temp_penalty = temp_extremes * (1.0 - (flexibility/2.0))
    scores -= temp_penalty * 0.05
    
    # Base normalization and noise
    # Add +/- 5% noise
    noise = np.random.uniform(-0.05, 0.05, num_samples)
    scores += noise
    
    # Clip to [0, 1]
    scores = np.clip(scores, 0.0, 1.0)
    
    # Assemble dataframe
    df = pd.DataFrame({
        'moisture_content': moisture_content,
        'ph_level': ph_level,
        'fat_content': fat_content,
        'protein_content': protein_content,
        'carbohydrate_content': carbohydrate_content,
        'respiration_rate': respiration_rate,
        'oxygen_sensitivity': oxygen_sensitivity,
        'moisture_sensitivity': moisture_sensitivity,
        'light_sensitivity': light_sensitivity,
        'microbial_sensitivity': microbial_sensitivity,
        'is_perishable': is_perishable,
        'storage_temperature': storage_temperature,
        'storage_humidity': storage_humidity,
        'required_shelf_life_days': required_shelf_life_days,
        'is_refrigerated': is_refrigerated,
        'light_exposure': light_exposure,
        'oxygen_transmission_rate': oxygen_transmission_rate,
        'water_vapor_transmission_rate': water_vapor_transmission_rate,
        'light_transmission': light_transmission,
        'thickness_microns': thickness_microns,
        'tensile_strength': tensile_strength,
        'flexibility': flexibility,
        'heat_sealability': heat_sealability,
        'recyclability_score': recyclability_score,
        'biodegradability_score': biodegradability_score,
        'estimated_cost_per_sqm': estimated_cost_per_sqm,
        'suitability_score': scores
    })
    
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    df.to_csv(output_path, index=False)
    print(f"Generated {num_samples} samples and saved to {output_path}")

if __name__ == "__main__":
    current_dir = os.path.dirname(os.path.abspath(__file__))
    output_file = os.path.join(current_dir, "training_dataset.csv")
    generate_dataset(num_samples=6000, output_path=output_file)
