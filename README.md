# AI-Based Intelligent Food Packaging Material Recommendation System

> **Smarter Packaging. Longer Shelf Life. Less Waste.**

An AI-powered web application that recommends optimal packaging materials for food commodities using XGBoost machine learning, SHAP explainability, and multi-objective optimization.

![Architecture](docs/architecture-diagram.png)

---

## 🏗️ Architecture

```
┌──────────────┐     ┌──────────────────┐     ┌──────────────────┐     ┌─────────┐
│   React UI   │────▶│  Spring Boot API  │────▶│  FastAPI ML Svc  │────▶│ XGBoost │
│  (Vite +     │◀────│  (Java 17)        │◀────│  (Python 3.11)   │◀────│  Model  │
│  Tailwind)   │     │  + JWT + JPA      │     │  + SHAP          │     └─────────┘
└──────────────┘     └────────┬─────────┘     └──────────────────┘
                              │
                     ┌────────▼─────────┐
                     │     MySQL 8.0     │
                     │   (13 tables)     │
                     └──────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose (recommended)
- OR: Node.js 20+, Java 17+, Python 3.11+, MySQL 8.0+

### One-Command Start (Docker)
```bash
# Clone and start
cp .env.example .env
docker compose up --build
```

Services will be available at:
| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| ML Service | http://localhost:8000 |
| MySQL | localhost:3306 |

### Manual Development Setup

#### 1. Database
```bash
# Start MySQL and run schema
mysql -u root -p < database/schema.sql
mysql -u root -p food_packaging_db < database/seed.sql
```

#### 2. ML Service
```bash
cd ml-service
pip install -r requirements.txt
python data/generate_dataset.py      # Generate training data
python training/train_model.py       # Train XGBoost model
python main.py                       # Start FastAPI server (port 8000)
```

#### 3. Backend
```bash
cd backend
mvn spring-boot:run                  # Start Spring Boot (port 8080)
```

#### 4. Frontend
```bash
cd frontend
npm install
npm run dev                          # Start Vite dev server (port 5173)
```

## 👥 Demo Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@foodpack.ai | password123 |
| Technologist | tech@foodpack.ai | password123 |
| Producer | producer@foodpack.ai | password123 |

> ⚠️ Change these passwords in production!

## 🎯 Features

### For Food Producers
- 🔍 Select food commodity and enter properties
- 📊 Get AI-powered packaging recommendations (Top 3)
- 🧠 SHAP-driven explanations for each recommendation
- ⚖️ Multi-objective optimization (performance, cost, sustainability, shelf life)
- 📋 Compare packaging materials side-by-side
- 💾 Save and track recommendation history
- 📄 Generate downloadable reports

### For Food Technologists
- 🍎 Manage food commodity database
- 📦 Manage packaging material database
- 🤖 View AI model performance and metrics
- 📈 Feature importance and SHAP analysis
- ✅ Validate AI recommendations

### For Administrators
- 👤 User management with role-based access
- 📊 System-wide statistics and dashboards
- 📝 Audit logging
- ⚙️ System configuration

## 🤖 AI/ML Details

### Model
- **Algorithm**: XGBoost Regressor
- **Task**: Predict packaging suitability score (0.0 - 1.0)
- **Features**: 26 input features (food, storage, packaging properties)
- **Explainability**: SHAP TreeExplainer

### Input Features
| Category | Features |
|----------|----------|
| Food | moisture, pH, fat, protein, carbs, respiration rate, oxygen/moisture/light/microbial sensitivity |
| Storage | temperature, humidity, shelf life, refrigeration, light exposure |
| Packaging | OTR, WVTR, light transmission, thickness, tensile strength, flexibility, cost, recyclability, biodegradability |

### Multi-Objective Ranking
The final ranking combines:
1. **AI Suitability Score** — XGBoost prediction
2. **Cost Score** — Inverse normalized cost
3. **Sustainability Score** — Material sustainability rating
4. **User Preference Weights** — Adjustable sliders

### Dataset
The training dataset is a scientifically defensible derived dataset based on:
- Published packaging material properties (OTR, WVTR, etc.)
- Food science principles for barrier requirements
- Established packaging-food compatibility rules

See [datasets/README.md](datasets/README.md) for full documentation.

## 📁 Project Structure

```
ai-food-packaging/
├── frontend/                 # React + Vite + Tailwind CSS
│   ├── src/
│   │   ├── api/             # Axios API clients
│   │   ├── components/      # React components
│   │   ├── contexts/        # Auth & Theme contexts
│   │   ├── hooks/           # Custom hooks
│   │   ├── pages/           # Page components
│   │   └── utils/           # Utilities
│   ├── package.json
│   └── vite.config.js
│
├── backend/                  # Java Spring Boot
│   ├── src/main/java/com/foodpackaging/
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Data transfer objects
│   │   ├── entity/          # JPA entities
│   │   ├── exception/       # Exception handlers
│   │   ├── repository/      # JPA repositories
│   │   ├── security/        # JWT + Spring Security
│   │   └── service/         # Business logic
│   └── pom.xml
│
├── ml-service/               # Python FastAPI + XGBoost
│   ├── app/                 # FastAPI application
│   ├── data/                # Dataset generation
│   ├── models/              # Trained model artifacts
│   ├── training/            # Training pipeline
│   ├── main.py              # FastAPI entry point
│   └── requirements.txt
│
├── database/                 # MySQL schema & seed data
│   ├── schema.sql
│   └── seed.sql
│
├── datasets/                 # Dataset documentation
├── docs/                     # Architecture & API docs
├── docker-compose.yml
├── .env.example
└── README.md
```

## 🔒 Security

- Password hashing with BCrypt
- JWT token authentication
- Role-based access control (RBAC)
- CORS configuration
- Input validation
- SQL injection protection via JPA
- No hardcoded secrets

## 📊 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login |
| GET | /api/auth/me | Get current user |

### Food Commodities
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/foods | List foods |
| GET | /api/foods/{id} | Get food details |
| POST | /api/foods | Create food |
| PUT | /api/foods/{id} | Update food |
| DELETE | /api/foods/{id} | Delete food |

### Packaging Materials
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/packaging-materials | List materials |
| GET | /api/packaging-materials/{id} | Get material details |
| POST | /api/packaging-materials | Create material |
| PUT | /api/packaging-materials/{id} | Update material |

### Recommendations
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/recommendations | Create recommendation |
| GET | /api/recommendations | List user recommendations |
| GET | /api/recommendations/{id} | Get recommendation details |
| POST | /api/recommendations/{id}/save | Save recommendation |

### ML Service (Internal)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /predict | Single prediction |
| POST | /batch-predict | Batch predictions |
| POST | /explain | SHAP explanation |
| GET | /model-info | Model metadata |
| GET | /health | Health check |

## 🧪 Demo Scenario

**Food**: Potato Chips  
**Properties**: Moisture 2%, Fat 35%, pH 6.2, O₂ sensitivity High, Light sensitivity High  
**Storage**: 25°C, 6 months shelf life  

The system runs the actual trained XGBoost model and returns ranked packaging materials with SHAP explanations.

## 📝 License

This project is for educational and research purposes. See dataset documentation for data source licenses.

## 🙏 Acknowledgments

- Food packaging science literature for material properties
- XGBoost by Tianqi Chen
- SHAP by Scott Lundberg
