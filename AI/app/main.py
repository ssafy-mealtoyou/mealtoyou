from typing import Union

import uvicorn
from fastapi import FastAPI, UploadFile, File

from config import Config
from food_detector import food_detector
from food_recommendation.food_recommendation import get_food_recommendations, NutrientInfo

app = FastAPI()


@app.post("/detect")
async def detect_objects(file: UploadFile = File(...), conf: float = 0.5):
    # Process the uploaded image for object detection
    image_bytes = await file.read()
    detections = food_detector.predict(image_bytes, conf)

    return {"results": detections}


@app.post("/recommendations")
def get_recommendations(nutrient_info: NutrientInfo):
    return get_food_recommendations(nutrient_info)


if __name__ == "__main__":
    uvicorn.run(app, host='0.0.0.0', port=int(Config.PORT))
