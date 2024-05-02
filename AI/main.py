import numpy as np
import pandas as pd
from fastapi import FastAPI
from pydantic import BaseModel
from scipy.optimize import differential_evolution
from food_recommendation import get_food_recommendations, NutrientInfo

app = FastAPI()


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.post("/recommendations")
def get_recommendations(nutrient_info: NutrientInfo):
    return get_food_recommendations(nutrient_info)


