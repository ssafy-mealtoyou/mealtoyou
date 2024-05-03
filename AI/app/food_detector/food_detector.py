from pydantic import BaseModel
from ultralytics import YOLO
import cv2
import numpy as np


class FoodDetectionResponse(BaseModel):
    className: int
    confidence: float


# 모델 weight 로딩
model = YOLO('app/food_detector/data/yolov9-foods.pt')


def predict(image_bytes, conf):

    # 이미지 변환
    image = np.frombuffer(image_bytes, dtype=np.uint8)
    image = cv2.imdecode(image, cv2.IMREAD_COLOR)

    # 예측
    result = model.predict(image, conf=conf)[0]

    response = [FoodDetectionResponse(className=cls_name, confidence=conf) for (cls_name, conf) in get_classes(result)]

    return response


def get_classes(result):
    for box in result.boxes:
        cls = int(box.cls.item())
        cls_name = result.names[cls]
        cls_name = '00000000' if cls_name == '0' else cls_name
        conf = box.conf.item()
        yield cls_name, conf

