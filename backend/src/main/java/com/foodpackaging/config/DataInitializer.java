package com.foodpackaging.config;

import com.foodpackaging.entity.*;
import com.foodpackaging.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FoodCategoryRepository categoryRepository;
    private final FoodCommodityRepository commodityRepository;
    private final FoodPropertiesRepository foodPropertiesRepository;
    private final PackagingMaterialRepository materialRepository;
    private final PackagingPropertiesRepository packagingPropertiesRepository;
    private final ModelVersionRepository modelVersionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            FoodCategoryRepository categoryRepository,
            FoodCommodityRepository commodityRepository,
            FoodPropertiesRepository foodPropertiesRepository,
            PackagingMaterialRepository materialRepository,
            PackagingPropertiesRepository packagingPropertiesRepository,
            ModelVersionRepository modelVersionRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.commodityRepository = commodityRepository;
        this.foodPropertiesRepository = foodPropertiesRepository;
        this.materialRepository = materialRepository;
        this.packagingPropertiesRepository = packagingPropertiesRepository;
        this.modelVersionRepository = modelVersionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedUsers();
        seedFoodData();
        seedPackagingMaterials();
        seedModelVersion();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name(ERole.ROLE_PRODUCER).description("Food Producer / Processor - Primary system user").build());
            roleRepository.save(Role.builder().name(ERole.ROLE_TECHNOLOGIST).description("Food Technologist / Packaging Expert - Scientific oversight").build());
            roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).description("System Administrator - User & platform management").build());
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();
            Role techRole = roleRepository.findByName(ERole.ROLE_TECHNOLOGIST).orElseThrow();
            Role producerRole = roleRepository.findByName(ERole.ROLE_PRODUCER).orElseThrow();

            String encodedPass = passwordEncoder.encode("password123");

            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@foodpack.ai")
                    .password(encodedPass)
                    .firstName("System")
                    .lastName("Admin")
                    .organization("FoodPack AI Global")
                    .phone("+1-555-0100")
                    .role(adminRole)
                    .isActive(true)
                    .build());

            userRepository.save(User.builder()
                    .username("technologist")
                    .email("tech@foodpack.ai")
                    .password(encodedPass)
                    .firstName("Dr. Elena")
                    .lastName("Vance")
                    .organization("Institute of Food Science & Packaging")
                    .phone("+1-555-0101")
                    .role(techRole)
                    .isActive(true)
                    .build());

            userRepository.save(User.builder()
                    .username("producer")
                    .email("producer@foodpack.ai")
                    .password(encodedPass)
                    .firstName("Raj")
                    .lastName("Patel")
                    .organization("Apex Agro Foods Ltd.")
                    .phone("+1-555-0102")
                    .role(producerRole)
                    .isActive(true)
                    .build());
        }
    }

    private void seedFoodData() {
        if (commodityRepository.count() > 0) return;

        FoodCategory fruits = categoryRepository.save(new FoodCategory(null, "Fruits", "Fresh, dried, and processed fruit products", LocalDateTime.now(), null));
        FoodCategory veg = categoryRepository.save(new FoodCategory(null, "Vegetables", "Fresh and root vegetables", LocalDateTime.now(), null));
        FoodCategory grains = categoryRepository.save(new FoodCategory(null, "Grains & Cereals", "Cereal crops, grains, and flour", LocalDateTime.now(), null));
        FoodCategory processed = categoryRepository.save(new FoodCategory(null, "Processed Foods", "Snacks, baked goods, and confectioneries", LocalDateTime.now(), null));
        FoodCategory dairy = categoryRepository.save(new FoodCategory(null, "Dairy & Meat", "Perishable dairy and animal proteins", LocalDateTime.now(), null));
        FoodCategory spices = categoryRepository.save(new FoodCategory(null, "Spices & Nuts", "Dried spices, seasonings, and tree nuts", LocalDateTime.now(), null));

        // 1. Potato Chips (DEMO SCENARIO SPEC)
        saveFood(processed, "Potato Chips", "Fried, sliced potato snack with high fat and crisp texture",
                2.0, 6.2, 35.0, 6.0, 50.0, 0.0, "High", "High", "High", "Low", false);

        // 2. Apple
        saveFood(fruits, "Apple", "Fresh climacteric fruit with moderate respiration and moisture",
                85.0, 3.5, 0.2, 0.3, 14.0, 15.0, "Medium", "High", "Low", "Medium", true);

        // 3. Banana
        saveFood(fruits, "Banana", "High respiration tropical fruit with rapid ethylene generation",
                75.0, 5.0, 0.3, 1.1, 23.0, 40.0, "High", "High", "Low", "High", true);

        // 4. Mango
        saveFood(fruits, "Mango", "Tropical fruit highly susceptible to fungal spoilage and chilling injury",
                83.0, 4.0, 0.4, 0.8, 15.0, 30.0, "High", "High", "Medium", "High", true);

        // 5. Tomato
        saveFood(fruits, "Tomato", "Climacteric fruit-vegetable with high water activity",
                94.0, 4.3, 0.2, 0.9, 3.9, 20.0, "Medium", "High", "Medium", "High", true);

        // 6. Potato
        saveFood(veg, "Potato", "Underground tuber susceptible to greening under light exposure",
                79.0, 5.8, 0.1, 2.0, 17.0, 8.0, "Medium", "Medium", "High", "Medium", true);

        // 7. Onion
        saveFood(veg, "Onion", "Bulb vegetable requiring low relative humidity to prevent sprouting",
                89.0, 5.5, 0.1, 1.1, 9.3, 5.0, "Low", "High", "Low", "Medium", true);

        // 8. Carrot
        saveFood(veg, "Carrot", "Root vegetable sensitive to desiccation and bitterness",
                88.0, 6.0, 0.2, 0.9, 10.0, 15.0, "Medium", "High", "Low", "Medium", true);

        // 9. Rice
        saveFood(grains, "Milled Rice", "Dry staple grain requiring insect and moisture barrier",
                12.0, 6.5, 0.7, 7.1, 80.0, 0.0, "Low", "High", "Low", "Low", false);

        // 10. Wheat Flour
        saveFood(grains, "Wheat Flour", "Finely ground cereal flour prone to moisture sorption and oxidation",
                11.5, 6.3, 1.2, 11.0, 72.0, 0.0, "Medium", "High", "Low", "Low", false);

        // 11. Biscuits
        saveFood(processed, "Biscuits / Cookies", "Baked confection with low moisture and moderate fat content",
                3.5, 6.8, 18.0, 7.0, 68.0, 0.0, "High", "High", "Medium", "Low", false);

        // 12. Milk Powder
        saveFood(dairy, "Whole Milk Powder", "Spray-dried dairy solid extremely sensitive to lipid oxidation",
                3.0, 6.6, 26.5, 26.0, 38.0, 0.0, "Very High", "Very High", "High", "Low", false);

        // 13. Fresh Milk
        saveFood(dairy, "Fresh Pasteurized Milk", "Liquid dairy emulsion requiring refrigeration and aseptic barrier",
                88.0, 6.7, 3.5, 3.3, 4.8, 0.0, "High", "High", "Very High", "Very High", true);

        // 14. Cheddar Cheese
        saveFood(dairy, "Cheddar Cheese", "Semi-hard fermented dairy product prone to surface mold and dehydration",
                37.0, 5.2, 33.0, 25.0, 1.3, 0.0, "High", "High", "High", "High", true);

        // 15. Fresh Chicken
        saveFood(dairy, "Fresh Chicken Breast", "High protein perishable meat sensitive to aerobic microbial growth",
                74.0, 6.0, 2.5, 23.0, 0.0, 0.0, "Very High", "High", "Medium", "Very High", true);

        // 16. Roasted Almonds
        saveFood(spices, "Roasted Almonds", "High-oil tree nut vulnerable to oxidative rancidity and moisture uptake",
                3.0, 6.4, 52.0, 21.0, 20.0, 0.0, "Very High", "High", "High", "Low", false);

        // 17. Ground Turmeric
        saveFood(spices, "Ground Turmeric", "Curcumin-rich spice subject to photodegradation and aroma loss",
                9.0, 6.2, 3.2, 7.8, 65.0, 0.0, "Medium", "High", "Very High", "Low", false);

        // 18. Black Pepper
        saveFood(spices, "Black Pepper Corns", "Dried piperine-bearing berry requiring essential oil retention",
                10.5, 6.0, 3.3, 10.4, 64.0, 0.0, "Medium", "Medium", "High", "Low", false);

        // 19. Cashew Nuts
        saveFood(spices, "Raw Cashew Nuts", "Dry edible nut prone to free fatty acid accumulation",
                5.0, 6.0, 44.0, 18.0, 30.0, 0.0, "High", "High", "High", "Low", false);

        // 20. Orange Juice
        saveFood(fruits, "Orange Juice (NFC)", "Acidic citrus beverage susceptible to vitamin C oxidation and browning",
                89.0, 3.6, 0.2, 0.7, 10.0, 0.0, "Very High", "High", "High", "High", true);
    }

    private void saveFood(FoodCategory cat, String name, String desc,
                          double moisture, double ph, double fat, double protein, double carbs, double resp,
                          String o2Sens, String moistSens, String lightSens, String microSens, boolean perishable) {
        FoodCommodity comm = FoodCommodity.builder()
                .category(cat)
                .name(name)
                .description(desc)
                .build();
        comm = commodityRepository.save(comm);

        FoodProperties props = FoodProperties.builder()
                .foodCommodity(comm)
                .moistureContent(moisture)
                .phLevel(ph)
                .fatContent(fat)
                .proteinContent(protein)
                .carbohydrateContent(carbs)
                .respirationRate(resp)
                .oxygenSensitivity(o2Sens)
                .moistureSensitivity(moistSens)
                .lightSensitivity(lightSens)
                .microbialSensitivity(microSens)
                .isPerishable(perishable)
                .build();
        foodPropertiesRepository.save(props);
    }

    private void seedPackagingMaterials() {
        if (materialRepository.count() > 0) return;

        // 1. Aluminium Foil Laminate
        saveMaterial("Aluminium Multilayer Laminate", "ALU-LAM-01", "Multilayer",
                "High-barrier PET/Aluminium/Polyethylene composite offering virtually zero oxygen and vapor transmission",
                0.05, 0.05, 0.0, 85.0, 95.0, "Medium", true, false, 4.0, 1.0, 1.25, 4.0, 120.0, -40.0, 10.0);

        // 2. PET (Polyethylene Terephthalate)
        saveMaterial("Biaxially-Oriented PET (BOPET)", "BOPET-02", "Rigid/Film Plastic",
                "High optical clarity, moderate gas barrier, and exceptional mechanical tensile strength",
                55.0, 18.0, 88.0, 25.0, 190.0, "Low", true, true, 8.0, 1.0, 0.75, 6.0, 150.0, -60.0, 9.0);

        // 3. HDPE (High-Density Polyethylene)
        saveMaterial("High-Density Polyethylene (HDPE)", "HDPE-03", "Polyolefin",
                "Excellent moisture barrier, high chemical and moisture resistance with semi-translucent appearance",
                2400.0, 4.5, 65.0, 50.0, 32.0, "Medium", true, false, 8.5, 1.0, 0.55, 6.5, 120.0, -50.0, 7.0);

        // 4. LDPE (Low-Density Polyethylene)
        saveMaterial("Low-Density Polyethylene (LDPE)", "LDPE-04", "Polyolefin",
                "Highly flexible, superior heat-sealability and tear resistance, high gas permeability",
                6500.0, 15.0, 90.0, 45.0, 18.0, "High", true, true, 7.5, 1.0, 0.48, 5.5, 85.0, -50.0, 5.0);

        // 5. PP (Polypropylene)
        saveMaterial("Biaxially-Oriented Polypropylene (BOPP)", "BOPP-05", "Polyolefin",
                "Moisture barrier film with high clarity, stiffness, and heat resistance for snack foods",
                1600.0, 5.0, 92.0, 30.0, 140.0, "Medium", true, true, 7.0, 1.0, 0.60, 6.0, 130.0, 0.0, 8.0);

        // 6. EVOH Coextruded Film
        saveMaterial("EVOH High-Barrier Multilayer Film", "EVOH-ML-06", "Multilayer Barrier",
                "Ethylene Vinyl Alcohol copolymer sandwich providing ultra-high oxygen barrier for oxygen-sensitive items",
                2.5, 14.0, 85.0, 60.0, 65.0, "Medium", true, true, 5.0, 2.0, 1.40, 5.5, 110.0, -30.0, 9.0);

        // 7. Soda-Lime Glass
        saveMaterial("Amber / Clear Silica Glass", "GLASS-07", "Glass",
                "Absolute hermetic barrier to gases, water vapor, and aroma with infinite recyclability",
                0.0, 0.0, 25.0, 3000.0, 70.0, "Low", false, true, 9.5, 0.0, 2.20, 7.0, 250.0, -50.0, 10.0);

        // 8. PLA (Polylactic Acid)
        saveMaterial("Bio-Based PLA Film", "PLA-BIO-08", "Bioplastic",
                "Compostable polyester derived from fermented plant starch with high transparency",
                800.0, 65.0, 92.0, 35.0, 60.0, "Medium", true, true, 6.0, 9.0, 1.10, 8.5, 55.0, -10.0, 6.0);

        // 9. Paperboard with Bio-Wax Coating
        saveMaterial("Coated Barrier Paperboard", "PBRD-09", "Paper / Fiber",
                "Renewable cellulose fiberboard with grease-resistant bio-polymer coating",
                850.0, 22.0, 0.0, 280.0, 45.0, "Low", true, false, 9.0, 8.0, 0.70, 8.8, 90.0, -20.0, 7.0);

        // 10. Metallized PET (Met-PET)
        saveMaterial("Metallized Polyester (Met-PET/PE)", "MET-PET-10", "Metallized Film",
                "Vacuum-deposited aluminium coating on polyester film providing strong light and gas barrier",
                1.2, 1.0, 1.0, 40.0, 160.0, "Medium", true, false, 5.5, 1.0, 0.95, 5.0, 130.0, -40.0, 9.5);

        // 11. Starch-Based Biodegradable Film
        saveMaterial("Thermoplastic Starch Film (TPS)", "BIO-TPS-11", "Biopolymer",
                "Home-compostable biodegradable film suitable for short shelf-life fresh produce",
                3200.0, 120.0, 70.0, 40.0, 15.0, "High", true, true, 5.0, 10.0, 1.15, 9.2, 50.0, 0.0, 4.0);

        // 12. Polyamide / Nylon Barrier
        saveMaterial("Biaxially-Oriented Nylon (BOPA/PE)", "NYLON-12", "Polyamide",
                "Puncture-resistant vacuum skin film with excellent mechanical toughness and gas barrier",
                25.0, 35.0, 88.0, 65.0, 210.0, "High", true, true, 6.0, 1.0, 1.35, 6.0, 140.0, -60.0, 9.0);
    }

    private void saveMaterial(String name, String code, String cat, String desc,
                              double otr, double wvtr, double light, double thickness, double tensile,
                              String flexibility, boolean sealable, boolean transparent,
                              double recyclability, double biodegradability, double cost, double sustain,
                              double maxT, double minT, double grease) {
        PackagingMaterial mat = PackagingMaterial.builder()
                .name(name)
                .materialCode(code)
                .category(cat)
                .description(desc)
                .build();
        mat = materialRepository.save(mat);

        PackagingProperties props = PackagingProperties.builder()
                .packagingMaterial(mat)
                .oxygenTransmissionRate(otr)
                .waterVaporTransmissionRate(wvtr)
                .lightTransmission(light)
                .thicknessMicrons(thickness)
                .tensileStrength(tensile)
                .flexibility(flexibility)
                .heatSealability(sealable)
                .transparency(transparent ? 1.0 : 0.0)
                .recyclabilityScore(recyclability)
                .biodegradabilityScore(biodegradability)
                .estimatedCostPerSqm(cost)
                .sustainabilityScore(sustain)
                .maxTemperature(maxT)
                .minTemperature(minT)
                .greaseResistance(grease)
                .build();
        packagingPropertiesRepository.save(props);
    }

    private void seedModelVersion() {
        if (modelVersionRepository.count() == 0) {
            ModelVersion model = ModelVersion.builder()
                    .modelName("XGBoost Packaging Suitability Predictor")
                    .algorithm("XGBoost Regressor (Tree-based Gradient Boosting)")
                    .version("v1.0")
                    .trainingDate(LocalDateTime.now())
                    .datasetSize(6000)
                    .accuracy(0.9250)
                    .precisionScore(0.9378)
                    .recallScore(0.9613)
                    .f1Score(0.9494)
                    .rmse(0.0342)
                    .r2Score(0.9117)
                    .mae(0.0283)
                    .isActive(true)
                    .modelPath("models/xgb_model.joblib")
                    .build();
            modelVersionRepository.save(model);
        }
    }
}
