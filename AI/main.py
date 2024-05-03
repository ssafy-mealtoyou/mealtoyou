import numpy as np
import pandas as pd
from fastapi import FastAPI
from pydantic import BaseModel
from scipy.optimize import differential_evolution
from food_recommendation import get_food_recommendations, NutrientInfo
from other_foods_recommendation import get_otherFoods,Info

app = FastAPI()


@app.get("/")
async def root():
    return {"message": "Hello World"}

# 음식 추천 api
@app.post("/recommendations")
def get_recommendations(nutrient_info: NutrientInfo):
    return get_food_recommendations(nutrient_info)

# 음식 수정 api
@app.post("/asd")
def find_other_foods(info: Info):
    return get_otherFoods(info)