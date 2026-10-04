# System Architecture

## Overview

The AI-Based Food Packaging Material Recommendation System is a microservices-based application with four main components communicating through REST APIs.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                         USER (Browser)                               │
└───────────────────────────┬─────────────────────────────────────────┘
                            │ HTTPS
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    FRONTEND (React + Vite)                            │
│  ┌──────────┐ ┌───────────┐ ┌──────────┐ ┌─────────────┐           │
│  │ Landing  │ │   Auth    │ │Dashboard │ │Recommendation│           │
│  │  Page    │ │  Pages    │ │  Pages   │ │    Flow      │           │
│  └──────────┘ └───────────┘ └──────────┘ └─────────────┘           │
│  ┌──────────────────────────────────────────────────────┐           │
│  │         Axios API Client + JWT Interceptor            │           │
│  └────────────────────────┬─────────────────────────────┘           │
└───────────────────────────┼─────────────────────────────────────────┘
                            │ REST API (JSON)
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                 BACKEND (Spring Boot 3.2)                             │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                     Security Layer                              │ │
│  │  JWT Token Provider │ Auth Filter │ CORS │ RBAC                │ │
│  └────────────────────────────────────────────────────────────────┘ │
│  ┌──────────┐ ┌───────────┐ ┌──────────┐ ┌─────────────┐         │
│  │   Auth   │ │   Food    │ │Packaging │ │Recommendation│         │
│  │Controller│ │Controller │ │Controller│ │ Controller   │         │
│  └────┬─────┘ └─────┬─────┘ └────┬─────┘ └──────┬──────┘         │
│       ▼              ▼            ▼               ▼                │
│  ┌──────────┐ ┌───────────┐ ┌──────────┐ ┌─────────────┐         │
│  │   Auth   │ │   Food    │ │Packaging │ │Recommendation│         │
│  │ Service  │ │ Service   │ │ Service  │ │   Service    │         │
│  └────┬─────┘ └─────┬─────┘ └────┬─────┘ └──────┬──────┘         │
│       ▼              ▼            ▼               │                │
│  ┌──────────────────────────────────────┐         │                │
│  │      JPA Repositories (MySQL)        │         │                │
│  └──────────────────┬───────────────────┘         │                │
│                     │                              │                │
│                     │                    ┌─────────▼──────┐        │
│                     │                    │ ML Service      │        │
│                     │                    │   Client        │        │
│                     │                    └─────────┬──────┘        │
└─────────────────────┼──────────────────────────────┼───────────────┘
                      │                              │ REST (Internal)
                      ▼                              ▼
┌─────────────────────────────┐  ┌───────────────────────────────────┐
│       MySQL 8.0              │  │     ML SERVICE (FastAPI)           │
│                              │  │                                   │
│  ┌────────┐ ┌─────────────┐ │  │  ┌───────────┐ ┌──────────────┐ │
│  │ users  │ │food_commodit│ │  │  │  XGBoost   │ │    SHAP      │ │
│  │ roles  │ │food_properti│ │  │  │  Model     │ │ TreeExplainer│ │
│  └────────┘ │packaging_mat│ │  │  └───────────┘ └──────────────┘ │
│             │packaging_pro│ │  │  ┌───────────┐ ┌──────────────┐ │
│  ┌────────┐ │recommendation│ │  │  │  Scaler   │ │   Metrics    │ │
│  │ audit  │ │saved_recomme│ │  │  │  Pipeline  │ │   Store      │ │
│  │ logs   │ │model_version│ │  │  └───────────┘ └──────────────┘ │
│  └────────┘ └─────────────┘ │  └───────────────────────────────────┘
└─────────────────────────────┘
```

## Data Flow: Recommendation Request

```
1. User fills recommendation form (food + storage + preferences)
         │
2. Frontend sends POST /api/recommendations
         │
3. Backend validates input
         │
4. Backend loads ALL packaging materials from MySQL
         │
5. For EACH material:
   ├── Combines food features + storage features + material properties
   └── Creates prediction payload
         │
6. Backend sends batch to ML Service (POST /batch-predict)
         │
7. ML Service:
   ├── Scales features using saved StandardScaler
   ├── XGBoost predicts suitability score (0.0-1.0)
   └── Returns scores for all materials
         │
8. Backend applies user preference weights:
   finalScore = perfWeight × suitability
              + costWeight × (1 - normalizedCost)
              + sustainWeight × (sustainScore/10)
              + shelfWeight × suitability
         │
9. Backend ranks materials by finalScore
         │
10. Backend takes top 3 and requests SHAP explanations
          │
11. ML Service generates SHAP values using TreeExplainer
          │
12. Backend stores results + factors in MySQL
          │
13. Backend returns ranked results with explanations
          │
14. Frontend displays recommendation with SHAP visualization
```

## Security Architecture

```
┌────────────────────────────────────────────────┐
│                  Security Flow                   │
│                                                  │
│  1. User logs in with credentials                │
│  2. Backend validates + generates JWT            │
│  3. Frontend stores JWT in localStorage          │
│  4. Every API request includes:                  │
│     Authorization: Bearer <JWT>                  │
│  5. JwtAuthenticationFilter validates token       │
│  6. SecurityConfig enforces role-based access:   │
│     /api/admin/**       → ROLE_ADMIN             │
│     /api/technologist/**→ ROLE_TECHNOLOGIST      │
│     /api/recommendations→ Authenticated          │
│     /api/auth/**        → Public                 │
│  7. ML Service is internal-only (not exposed)    │
└────────────────────────────────────────────────┘
```

## Technology Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Frontend | React 18 + Vite | UI framework |
| Styling | Tailwind CSS | Utility-first CSS |
| Charts | Recharts | Data visualization |
| Icons | Lucide React | Icon library |
| Backend | Spring Boot 3.2 | REST API framework |
| Security | Spring Security + JWT | Authentication/Authorization |
| ORM | Spring Data JPA | Database access |
| ML Framework | XGBoost | Prediction model |
| ML Explainability | SHAP | Model interpretation |
| ML API | FastAPI | ML service API |
| Database | MySQL 8.0 | Data persistence |
| Containerization | Docker + Compose | Deployment |

## Database Schema

See [database/schema.sql](../database/schema.sql) for the complete schema with 13 normalized tables.

## ML Model

See [ml-model.md](ml-model.md) for detailed ML documentation.
