# 
FROM ultralytics/ultralytics:8.2.5-cpu

# 
WORKDIR /code

# 
COPY ./requirements.txt /code/requirements.txt

# 
RUN pip install --upgrade -r /code/requirements.txt

# 
COPY app /code/app
COPY .env /code/app/.env
COPY yolov9-foods.pt /code/app/food_detector/data/yolov9-foods.pt

# 
CMD ["python", "app/main.py"]
