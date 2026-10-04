SET NAMES utf8mb4;

-- 1. Roles
INSERT IGNORE INTO roles (name, description) VALUES 
('ROLE_ADMIN', 'System Administrator with full access'),
('ROLE_TECHNOLOGIST', 'Food Technologist / Packaging Expert'),
('ROLE_PRODUCER', 'Food Producer / End User');

-- 2. Demo Users ('password123' hashed)
INSERT IGNORE INTO users (username, email, password, first_name, last_name, phone, organization, role_id, is_active) VALUES 
('admin', 'admin@foodpack.ai', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', '1234567890', 'FoodPack AI', (SELECT id FROM roles WHERE name='ROLE_ADMIN'), TRUE),
('tech', 'tech@foodpack.ai', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Tech', 'Expert', '1234567891', 'FoodPack AI', (SELECT id FROM roles WHERE name='ROLE_TECHNOLOGIST'), TRUE),
('producer', 'producer@foodpack.ai', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Food', 'Producer', '1234567892', 'Farms Inc.', (SELECT id FROM roles WHERE name='ROLE_PRODUCER'), TRUE);

-- 3. Food Categories
INSERT IGNORE INTO food_categories (name, description) VALUES 
('Fruits', 'Fresh fruits'),
('Vegetables', 'Fresh vegetables'),
('Grains', 'Cereals and grains'),
('Processed Foods', 'Manufactured and processed food items'),
('Dairy', 'Milk and milk products'),
('Meat & Poultry', 'Meat and poultry products'),
('Spices & Herbs', 'Spices, herbs, and condiments'),
('Nuts & Seeds', 'Nuts, seeds, and dry fruits'),
('Beverages', 'Drinks and liquid refreshments');

-- 4. Food Commodities
INSERT IGNORE INTO food_commodities (name, category_id, description) VALUES 
('Apple', (SELECT id FROM food_categories WHERE name='Fruits'), 'Fresh whole apples'),
('Banana', (SELECT id FROM food_categories WHERE name='Fruits'), 'Fresh bananas'),
('Mango', (SELECT id FROM food_categories WHERE name='Fruits'), 'Fresh mangoes'),
('Tomato', (SELECT id FROM food_categories WHERE name='Vegetables'), 'Fresh tomatoes'),
('Potato', (SELECT id FROM food_categories WHERE name='Vegetables'), 'Fresh potatoes'),
('Onion', (SELECT id FROM food_categories WHERE name='Vegetables'), 'Fresh onions'),
('Carrot', (SELECT id FROM food_categories WHERE name='Vegetables'), 'Fresh carrots'),
('Spinach', (SELECT id FROM food_categories WHERE name='Vegetables'), 'Fresh spinach leaves'),
('Rice', (SELECT id FROM food_categories WHERE name='Grains'), 'White rice grains'),
('Wheat', (SELECT id FROM food_categories WHERE name='Grains'), 'Whole wheat grains'),
('Potato Chips', (SELECT id FROM food_categories WHERE name='Processed Foods'), 'Fried potato slices'),
('Biscuits', (SELECT id FROM food_categories WHERE name='Processed Foods'), 'Baked sweet or savory biscuits'),
('Milk', (SELECT id FROM food_categories WHERE name='Dairy'), 'Liquid cow milk'),
('Cheese', (SELECT id FROM food_categories WHERE name='Dairy'), 'Processed cheese blocks'),
('Chicken', (SELECT id FROM food_categories WHERE name='Meat & Poultry'), 'Raw chicken meat'),
('Turmeric', (SELECT id FROM food_categories WHERE name='Spices & Herbs'), 'Turmeric powder'),
('Black Pepper', (SELECT id FROM food_categories WHERE name='Spices & Herbs'), 'Black pepper corns'),
('Almonds', (SELECT id FROM food_categories WHERE name='Nuts & Seeds'), 'Raw almonds'),
('Cashews', (SELECT id FROM food_categories WHERE name='Nuts & Seeds'), 'Raw cashew nuts'),
('Orange Juice', (SELECT id FROM food_categories WHERE name='Beverages'), 'Fresh orange juice');

-- 5. Food Properties
INSERT IGNORE INTO food_properties (food_commodity_id, moisture_content, ph_level, fat_content, protein_content, carbohydrate_content, respiration_rate, oxygen_sensitivity, moisture_sensitivity, light_sensitivity, microbial_sensitivity, is_perishable) VALUES 
((SELECT id FROM food_commodities WHERE name='Apple'), 85.00, 3.50, 0.20, 0.30, 14.00, 15.00, 'Medium', 'High', 'Low', 'Medium', TRUE),
((SELECT id FROM food_commodities WHERE name='Banana'), 75.00, 5.00, 0.30, 1.10, 23.00, 40.00, 'High', 'High', 'Low', 'High', TRUE),
((SELECT id FROM food_commodities WHERE name='Mango'), 82.00, 4.20, 0.40, 0.80, 15.00, 30.00, 'High', 'High', 'Medium', 'High', TRUE),
((SELECT id FROM food_commodities WHERE name='Tomato'), 94.00, 4.30, 0.20, 0.90, 3.90, 25.00, 'Medium', 'High', 'Medium', 'High', TRUE),
((SELECT id FROM food_commodities WHERE name='Potato'), 79.00, 6.00, 0.10, 2.00, 17.00, 8.00, 'Low', 'Medium', 'High', 'Medium', TRUE),
((SELECT id FROM food_commodities WHERE name='Onion'), 89.00, 5.50, 0.10, 1.10, 9.00, 5.00, 'Low', 'Medium', 'Low', 'Low', TRUE),
((SELECT id FROM food_commodities WHERE name='Carrot'), 88.00, 6.00, 0.20, 0.90, 10.00, 15.00, 'Medium', 'High', 'Low', 'Medium', TRUE),
((SELECT id FROM food_commodities WHERE name='Spinach'), 91.00, 6.50, 0.40, 2.90, 3.60, 60.00, 'High', 'High', 'Medium', 'High', TRUE),
((SELECT id FROM food_commodities WHERE name='Rice'), 12.00, 6.50, 0.60, 7.00, 80.00, 0.00, 'Low', 'Medium', 'Low', 'Low', FALSE),
((SELECT id FROM food_commodities WHERE name='Wheat'), 11.00, 6.30, 1.50, 13.00, 71.00, 0.00, 'Low', 'Medium', 'Low', 'Low', FALSE),
((SELECT id FROM food_commodities WHERE name='Potato Chips'), 2.00, 6.20, 35.00, 6.00, 50.00, 0.00, 'High', 'High', 'High', 'Low', FALSE),
((SELECT id FROM food_commodities WHERE name='Biscuits'), 3.00, 6.50, 15.00, 6.00, 65.00, 0.00, 'Medium', 'High', 'Low', 'Low', FALSE),
((SELECT id FROM food_commodities WHERE name='Milk'), 88.00, 6.70, 3.30, 3.20, 4.80, 0.00, 'High', 'Medium', 'High', 'Very High', TRUE),
((SELECT id FROM food_commodities WHERE name='Cheese'), 37.00, 5.30, 33.00, 25.00, 1.30, 0.00, 'High', 'Low', 'Medium', 'High', TRUE),
((SELECT id FROM food_commodities WHERE name='Chicken'), 75.00, 6.20, 3.60, 21.00, 0.00, 0.00, 'High', 'Medium', 'Low', 'Very High', TRUE),
((SELECT id FROM food_commodities WHERE name='Turmeric'), 11.00, 6.00, 3.00, 8.00, 65.00, 0.00, 'Medium', 'High', 'High', 'Low', FALSE),
((SELECT id FROM food_commodities WHERE name='Black Pepper'), 10.00, 5.50, 3.30, 10.00, 64.00, 0.00, 'Medium', 'High', 'Low', 'Low', FALSE),
((SELECT id FROM food_commodities WHERE name='Almonds'), 4.00, 6.00, 50.00, 21.00, 22.00, 0.00, 'High', 'High', 'Medium', 'Low', FALSE),
((SELECT id FROM food_commodities WHERE name='Cashews'), 5.00, 5.80, 44.00, 18.00, 30.00, 0.00, 'High', 'High', 'Medium', 'Low', FALSE),
((SELECT id FROM food_commodities WHERE name='Orange Juice'), 88.00, 3.80, 0.20, 0.70, 10.00, 0.00, 'High', 'Low', 'High', 'High', TRUE);

-- 6. Packaging Materials
INSERT IGNORE INTO packaging_materials (name, material_code, category, description) VALUES 
('Low-Density Polyethylene', 'LDPE', 'Plastics', 'Flexible, transparent, good moisture barrier'),
('High-Density Polyethylene', 'HDPE', 'Plastics', 'Rigid, good moisture barrier, high chemical resistance'),
('Polypropylene', 'PP', 'Plastics', 'High melting point, good moisture barrier, low clarity'),
('Polyethylene Terephthalate', 'PET', 'Plastics', 'Excellent clarity, strong, good gas and moisture barrier'),
('Glass', 'GL', 'Glass', 'Inert, absolute barrier, heavy, fragile'),
('Aluminium Foil', 'ALF', 'Metals', 'Absolute barrier to moisture, gases, and light'),
('Paperboard', 'PB', 'Paper', 'Rigid, printable, poor barrier without coating'),
('Polylactic Acid', 'PLA', 'Bioplastics', 'Biodegradable, clear, moderate barrier properties'),
('Biaxially Oriented PP', 'BOPP', 'Plastics', 'Excellent clarity, good printability, good moisture barrier'),
('Multilayer Laminate (Al/PET/PE)', 'MLAM', 'Composite', 'High barrier performance for long shelf life'),
('Biodegradable Film (Starch)', 'BDF', 'Bioplastics', 'Eco-friendly, poor moisture barrier'),
('Cellophane', 'CELL', 'Bioplastics', 'Derived from cellulose, transparent, breathable');

-- 7. Packaging Properties
INSERT IGNORE INTO packaging_properties (material_id, oxygen_transmission_rate, water_vapor_transmission_rate, light_transmission, thickness_microns, tensile_strength, flexibility, heat_sealability, transparency, recyclability_score, biodegradability_score, estimated_cost_per_sqm, sustainability_score, max_temperature, min_temperature, grease_resistance) VALUES 
((SELECT id FROM packaging_materials WHERE material_code='LDPE'), 6500.00, 15.00, 92.00, 50.00, 15.00, 'High', TRUE, TRUE, 6, 1, 0.50, 4, 80.0, -50.0, 'Low'),
((SELECT id FROM packaging_materials WHERE material_code='HDPE'), 2600.00, 6.00, 92.00, 50.00, 30.00, 'Medium', TRUE, TRUE, 7, 1, 0.55, 4, 110.0, -50.0, 'Medium'),
((SELECT id FROM packaging_materials WHERE material_code='PET'), 50.00, 15.00, 90.00, 25.00, 180.00, 'Low', TRUE, TRUE, 8, 1, 0.80, 5, 70.0, -60.0, 'High'),
((SELECT id FROM packaging_materials WHERE material_code='GL'), 0.00, 0.00, 90.00, 3000.00, 50.00, 'Low', FALSE, TRUE, 9, 0, 2.50, 6, 500.0, -100.0, 'High'),
((SELECT id FROM packaging_materials WHERE material_code='ALF'), 0.10, 0.10, 0.00, 9.00, 70.00, 'High', TRUE, FALSE, 7, 0, 1.20, 4, 300.0, -100.0, 'High'),
((SELECT id FROM packaging_materials WHERE material_code='PP'), 1800.00, 5.00, 92.00, 30.00, 35.00, 'Medium', TRUE, TRUE, 6, 1, 0.60, 4, 130.0, -20.0, 'High'),
((SELECT id FROM packaging_materials WHERE material_code='PB'), 10000.00, 2000.00, 0.00, 400.00, 40.00, 'Low', FALSE, FALSE, 8, 8, 0.40, 7, 100.0, -40.0, 'Low'),
((SELECT id FROM packaging_materials WHERE material_code='PLA'), 300.00, 250.00, 92.00, 30.00, 45.00, 'Medium', TRUE, TRUE, 4, 9, 1.10, 8, 50.0, -20.0, 'High'),
((SELECT id FROM packaging_materials WHERE material_code='BOPP'), 1500.00, 4.00, 95.00, 20.00, 140.00, 'Medium', TRUE, TRUE, 6, 1, 0.65, 4, 120.0, -30.0, 'High'),
((SELECT id FROM packaging_materials WHERE material_code='MLAM'), 0.05, 0.05, 0.00, 80.00, 90.00, 'Medium', TRUE, FALSE, 2, 0, 1.80, 2, 120.0, -40.0, 'High'),
((SELECT id FROM packaging_materials WHERE material_code='BDF'), 8000.00, 1500.00, 85.00, 40.00, 20.00, 'High', TRUE, TRUE, 5, 10, 0.90, 9, 60.0, -20.0, 'Low'),
((SELECT id FROM packaging_materials WHERE material_code='CELL'), 400.00, 1200.00, 95.00, 25.00, 80.00, 'High', TRUE, TRUE, 3, 9, 1.00, 8, 130.0, -20.0, 'High');

-- 8. Initial Model Version
INSERT IGNORE INTO model_versions (model_name, algorithm, version, training_date, dataset_size, accuracy, precision_score, recall_score, f1_score, rmse, r2_score, mae, is_active, model_path) VALUES 
('Packaging Recommender Model', 'XGBoost', 'v1.0', NOW(), 5000, 0.8900, 0.8800, 0.8600, 0.8700, 0.0850, 0.9100, 0.0550, TRUE, '/models/xgboost_v1_0.pkl');

