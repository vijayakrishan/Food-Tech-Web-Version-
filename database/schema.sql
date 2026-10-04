SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. roles
CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name ENUM('ROLE_PRODUCER', 'ROLE_TECHNOLOGIST', 'ROLE_ADMIN') NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    organization VARCHAR(100),
    role_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_created_at ON users(created_at);

-- 3. food_categories
CREATE TABLE IF NOT EXISTS food_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. food_commodities
CREATE TABLE IF NOT EXISTS food_commodities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category_id INT NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES food_categories(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_food_commodities_category_id ON food_commodities(category_id);

-- 5. food_properties
CREATE TABLE IF NOT EXISTS food_properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    food_commodity_id BIGINT NOT NULL UNIQUE,
    moisture_content DECIMAL(5,2),
    ph_level DECIMAL(4,2),
    fat_content DECIMAL(5,2),
    protein_content DECIMAL(5,2),
    carbohydrate_content DECIMAL(5,2),
    respiration_rate DECIMAL(8,2) COMMENT 'mg CO2/kg/hr',
    oxygen_sensitivity ENUM('Low','Medium','High','Very High'),
    moisture_sensitivity ENUM('Low','Medium','High','Very High'),
    light_sensitivity ENUM('Low','Medium','High','Very High'),
    microbial_sensitivity ENUM('Low','Medium','High','Very High'),
    is_perishable BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (food_commodity_id) REFERENCES food_commodities(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. packaging_materials
CREATE TABLE IF NOT EXISTS packaging_materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    material_code VARCHAR(20) NOT NULL UNIQUE,
    category VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. packaging_properties
CREATE TABLE IF NOT EXISTS packaging_properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL UNIQUE,
    oxygen_transmission_rate DECIMAL(10,2) COMMENT 'cc/m2/day',
    water_vapor_transmission_rate DECIMAL(10,2) COMMENT 'g/m2/day',
    light_transmission DECIMAL(5,2) COMMENT '%',
    thickness_microns DECIMAL(8,2),
    tensile_strength DECIMAL(8,2) COMMENT 'MPa',
    flexibility ENUM('Low','Medium','High'),
    heat_sealability BOOLEAN,
    transparency BOOLEAN,
    recyclability_score INT CHECK (recyclability_score BETWEEN 1 AND 10),
    biodegradability_score INT CHECK (biodegradability_score BETWEEN 1 AND 10),
    estimated_cost_per_sqm DECIMAL(8,2) COMMENT 'USD',
    sustainability_score INT CHECK (sustainability_score BETWEEN 1 AND 10),
    max_temperature DECIMAL(5,1) COMMENT '°C',
    min_temperature DECIMAL(5,1) COMMENT '°C',
    grease_resistance ENUM('Low','Medium','High'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (material_id) REFERENCES packaging_materials(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. recommendation_requests
CREATE TABLE IF NOT EXISTS recommendation_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    food_commodity_id BIGINT,
    moisture_content DECIMAL(5,2),
    ph_level DECIMAL(4,2),
    fat_content DECIMAL(5,2),
    protein_content DECIMAL(5,2),
    carbohydrate_content DECIMAL(5,2),
    respiration_rate DECIMAL(8,2),
    oxygen_sensitivity VARCHAR(20),
    moisture_sensitivity VARCHAR(20),
    light_sensitivity VARCHAR(20),
    storage_temperature DECIMAL(5,2),
    storage_humidity DECIMAL(5,2),
    required_shelf_life_days INT,
    is_refrigerated BOOLEAN,
    light_exposure VARCHAR(50),
    cost_preference DECIMAL(3,2) COMMENT '0-1 weight',
    sustainability_preference DECIMAL(3,2),
    performance_preference DECIMAL(3,2),
    shelf_life_preference DECIMAL(3,2),
    status ENUM('PENDING','PROCESSING','COMPLETED','FAILED') DEFAULT 'PENDING',
    model_version VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (food_commodity_id) REFERENCES food_commodities(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_recommendation_requests_user_id ON recommendation_requests(user_id);
CREATE INDEX idx_recommendation_requests_food_commodity_id ON recommendation_requests(food_commodity_id);
CREATE INDEX idx_recommendation_requests_status ON recommendation_requests(status);
CREATE INDEX idx_recommendation_requests_created_at ON recommendation_requests(created_at);

-- 9. recommendation_results
CREATE TABLE IF NOT EXISTS recommendation_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    rank INT,
    suitability_score DECIMAL(5,2),
    performance_score DECIMAL(5,2),
    cost_score DECIMAL(5,2),
    sustainability_score DECIMAL(5,2),
    overall_score DECIMAL(5,2),
    explanation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES recommendation_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES packaging_materials(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_recommendation_results_request_id ON recommendation_results(request_id);
CREATE INDEX idx_recommendation_results_material_id ON recommendation_results(material_id);

-- 10. recommendation_factors
CREATE TABLE IF NOT EXISTS recommendation_factors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    result_id BIGINT NOT NULL,
    factor_name VARCHAR(100),
    factor_value DECIMAL(8,4),
    contribution_percentage DECIMAL(5,2),
    direction ENUM('POSITIVE','NEGATIVE'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (result_id) REFERENCES recommendation_results(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_recommendation_factors_result_id ON recommendation_factors(result_id);

-- 11. saved_recommendations
CREATE TABLE IF NOT EXISTS saved_recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    request_id BIGINT NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_request (user_id, request_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES recommendation_requests(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. model_versions
CREATE TABLE IF NOT EXISTS model_versions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model_name VARCHAR(100) NOT NULL,
    algorithm VARCHAR(100),
    version VARCHAR(50) NOT NULL,
    training_date DATETIME,
    dataset_size INT,
    accuracy DECIMAL(5,4),
    precision_score DECIMAL(5,4),
    recall_score DECIMAL(5,4),
    f1_score DECIMAL(5,4),
    rmse DECIMAL(8,4),
    r2_score DECIMAL(5,4),
    mae DECIMAL(8,4),
    is_active BOOLEAN DEFAULT FALSE,
    model_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_model_versions_is_active ON model_versions(is_active);

-- 13. audit_logs
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100),
    entity_type VARCHAR(100),
    entity_id BIGINT,
    details TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);

SET FOREIGN_KEY_CHECKS = 1;
