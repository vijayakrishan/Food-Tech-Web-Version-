from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from typing import List, Dict, Union

from app.models import (
    PredictionRequest, 
    PredictionResponse, 
    BatchPredictionRequest, 
    BatchPredictionResponse,
    ModelInfo
)
from app.predictor import predictor

app = FastAPI(
    title="Food Packaging Suitability ML API",
    description="API for predicting food packaging suitability using XGBoost",
    version="1.0.0"
)

# Allow CORS for Spring Boot backend and frontends
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.on_event("startup")
async def startup_event():
    if predictor.model is None:
        print("WARNING: Model not found. Loading or training required.")
    else:
        print("Model loaded successfully.")

@app.get("/health")
async def health_check():
    status = "healthy" if predictor.model is not None else "degraded"
    return {"status": status, "model_loaded": predictor.model is not None}

@app.get("/model-info", response_model=ModelInfo)
async def get_model_info():
    try:
        return predictor.get_model_info()
    except RuntimeError as e:
        raise HTTPException(status_code=503, detail=str(e))

@app.post("/predict", response_model=PredictionResponse)
async def predict(request: PredictionRequest):
    try:
        return predictor.predict(request)
    except RuntimeError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@app.post("/batch-predict")
async def batch_predict(request: Union[BatchPredictionRequest, List[PredictionRequest]]):
    try:
        items = request.predictions if isinstance(request, BatchPredictionRequest) else request
        results = predictor.batch_predict(items)
        return {"results": results}
    except RuntimeError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@app.post("/explain")
async def explain(request: PredictionRequest):
    try:
        return predictor.explain(request)
    except RuntimeError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))
