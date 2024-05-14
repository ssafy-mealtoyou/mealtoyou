from datetime import date
import pandas as pd

import redis
from concurrent.futures import ThreadPoolExecutor, as_completed
from apscheduler.schedulers.background import BackgroundScheduler
from fastapi import FastAPI, Depends
from sqlalchemy import func, and_
from sqlalchemy.orm import Session
from database import SessionLocal, Exercise, UserHealth, FoodCombination, User, \
  Diet
from food_recommendation import get_food_recommendations, NutrientInfo
from other_foods_recommendation import get_otherFoods, Info
import py_eureka_client.eureka_client as eureka_client
import os
import socket

app = FastAPI()

eureka_server = os.getenv("EUREKA_SERVER",
                          "http://localhost:8761/eureka/")
instance_host = os.getenv("INSTANCE_HOST", "localhost")
environment = os.getenv("ENVIRONMENT", "local")  # 기본값을 'local'로 설정
redis_ip = os.getenv("REDIS", "localhost")


def get_container_ip():
  # 도커 컨테이너의 IP 주소를 가져오기
  hostname = socket.gethostname()
  ip = socket.gethostbyname(hostname)
  return ip


async def init_eureka_client():
  if environment == "docker":
    instance_ip = get_container_ip()
  else:
    instance_ip = "localhost"

  await eureka_client.init_async(
      eureka_server=eureka_server,
      app_name="ai-server",
      instance_port=8000,
      instance_ip=instance_ip,
      instance_host=instance_host
  )


@app.on_event("startup")
async def startup_event():
  await init_eureka_client()


def get_db():
  db = SessionLocal()
  try:
    yield db
  finally:
    db.close()


# Redis 클라이언트 생성
redis_client = redis.StrictRedis(host=redis_ip, port=6379, db=0)

# 식단의 만료 기간을 3일로 설정
PERIOD = 60


# 테스트용 나중에 지우기
@app.get("/")
async def root():
  return {"message": "Hello World"}


# 음식 추천 api
@app.post("/recommendations")
async def get_recommendations(nutrient_info: NutrientInfo,
    db: Session = Depends(get_db)):
  # nutrient_info = NutrientInfo(calories=600, carbs=82.5, protein=45, fat=10)

  sum_nutrient = nutrient_info.calories + nutrient_info.carbs + nutrient_info.protein + nutrient_info.fat
  # 먼저 RDB에서 내가 원하는 목표값과 비슷한 조합이 있는지 확인
  combinations = find_combination_in_db(
      db, nutrient_info.calories / sum_nutrient,
          nutrient_info.carbs / sum_nutrient,
          nutrient_info.protein / sum_nutrient, nutrient_info.fat / sum_nutrient
  )

  for combination in combinations:
    # 모든 조합이 이미 추천된 경우 새로운 조합 생성
    if not is_diet_recommended(user_id=1, diet_id=combination.id):
      store_diet(user_id=1, diet_id=combination.id)
      total = combination.carbs_ratio * sum_nutrient + combination.protein_ratio * sum_nutrient + combination.fat_ratio * sum_nutrient
      print(combination.food_names)
      dietFoods = []
      food_list = pd.read_excel('food_nutrition.xlsx')
      for food in combination.food_names:
        food_info = food_list[food_list['식품명'] == food].iloc[0]
        dietFood = {
          'name': food_info['식품명'],
          'imageUrl': '',  # 실제 이미지 URL이 필요
          'calories': float(food_info['에너지(㎉)']),
          'carbohydrate': float(food_info['탄수화물(g)']),
          'protein': float(food_info['단백질(g)']),
          'fat': float(food_info['지방(g)'])
        }
        dietFoods.append(dietFood)
      # dietFoods 리스트에서 칼로리의 합과 각 영양소의 합 계산
      # totalCalories = sum(food_info['에너지(㎉)'] for dietFood in dietFoods)
      totalCalories = 0
      for dietFood in dietFoods:
        print(dietFood)
        totalCalories += float(dietFood['calories'])
      print(222222222222)
      print(totalCalories)
      calorieRatio = nutrient_info.calories / totalCalories if totalCalories != 0 else 500
      print(nutrient_info)
      print(calorieRatio)
      # 각 음식의 칼로리 및 영양소를 목표 칼로리 비율에 맞게 조정
      for food in dietFoods:
        food['calories'] *= calorieRatio
        food['carbohydrate'] *= calorieRatio
        food['protein'] *= calorieRatio
        food['fat'] *= calorieRatio
        print(food)
      return {
        "dietId": combination.id,
        "totalCalories": int(combination.total_calories_ratio * sum_nutrient),
        "carbohydratePer": int(
            combination.carbs_ratio * sum_nutrient / total * 100),
        "proteinPer": int(
            combination.protein_ratio * sum_nutrient / total * 100),
        "fatPer": int(combination.fat_ratio * sum_nutrient / total * 100),
        'dietFoods': dietFoods
      }
  else:
    # 기존 조합이 없는 경우 새로운 조합 생성
    return get_food_recommendations(nutrient_info)


# 음식 수정 api
@app.post("/asd")
def find_other_foods(info: Info):
  return get_otherFoods(info)


def read_user_health(user_id: int, db: Session = Depends(get_db)):
  user_health_data = db.query(UserHealth).filter(
      UserHealth.user_id == user_id).first()
  print(user_health_data.bmr)
  print(user_health_data)


# 사용자의 칼로리 계산
def calculate_calories(user_id: int, db: Session):
  user_health_data = db.query(UserHealth).filter(
      UserHealth.user_id == user_id).first()
  if (not user_health_data):
    user = db.query(User).filter(User.user_id == user_id).one_or_none()
    bmr = 66.47 + (13.75 * user.weight) + (5 * user.height) - (6.76 * user.age)
  else:
    bmr = user_health_data.bmr

  today = date.today()  # 오늘 날짜 가져오기
  calories_burned = db.query(
      func.sum(Exercise.calories_burned)
  ).filter(
      and_(
          Exercise.user_id == user_id,
          func.date(Diet.create_datetime) == today
      )
  ).scalar() or 0
  # 먹은 칼로리

  # 해당 사용자의 오늘 날짜에 해당하는 섭취 칼로리 총합을 계산
  ate_calories = db.query(
      func.sum(Diet.total_calories)
  ).filter(
      and_(
          Diet.user_id == user_id,
          func.date(Diet.create_datetime) == today
      )
  ).scalar() or 0  # 쿼리 결과가 None이면 0을 반환
  print('=========================================')
  print(bmr, calories_burned, ate_calories)
  # 기초대사량 + 소모 칼로리 + 섭취 칼로리
  return bmr + calories_burned - ate_calories


# 사용자 목록 가져오기
def get_all_users(db: Session):
  return db.query(UserHealth.user_id).distinct().all()


# 목표에 따른 영양소 비율 설정
def get_nutrient_ratios(current_weight, target_weight):
  print('-------------------------')
  print(current_weight)
  print(target_weight)
  if target_weight < current_weight:  # 살 뺄 때
    return 0.40, 0.35, 0.25
  elif target_weight > current_weight:  # 증량할 때
    return 0.55, 0.30, 0.15
  else:  # 체중을 유지할 때
    return 0.50, 0.25, 0.25


# 함수 호출을 위한 스케줄러 작업
def scheduled_recommendation(split_factor):
  print(split_factor)
  # 데이터베이스 세션 관리
  with SessionLocal() as db:
    # 모든 사용자를 가져와 각 사용자의 칼로리 계산 및 추천 실행
    all_users = db.query(User.user_id, User.weight).distinct().all()
    print(all_users)
    # ThreadPoolExecutor 사용
    with ThreadPoolExecutor(max_workers=10) as executor:
      futures = []
      for user in all_users:
        print(user)
        futures.append(executor.submit(process_user, user, split_factor))
      print(futures)
      for future in as_completed(futures):
        user_id, response = future.result()
        print(f"User {user_id} - Recommendation: {response}")


def process_user(user, split_factor):
  user_id, current_weight = user
  with SessionLocal() as db:
    user_data = db.query(User).filter(User.user_id == user_id).first()
    print(f"Processing {user_id}, {current_weight}, user data: {user_data}")

    goal_weight = user_data.goal_weight
    if (goal_weight is None):
      goal_weight = current_weight
    carb_ratio, protein_ratio, fat_ratio = get_nutrient_ratios(current_weight,
                                                               goal_weight)

    calories = calculate_calories(user_id, db)
    # 한끼 최소 500칼로리
    if (calories < 500):
      calories = 500
    print("calories: ", calories)
    if calories is None:
      return user_id, "Error: Could not calculate calories"

    if split_factor:
      calories = round(calories / split_factor, 2)
    else:
      calories = round(calories, 2)

    carbs_calories = calories * carb_ratio
    protein_calories = calories * protein_ratio
    fat_calories = calories * fat_ratio

    carbs = round(carbs_calories / 4, 2)
    proteins = round(protein_calories / 4, 2)
    fats = round(fat_calories / 9, 2)

    nutrients = NutrientInfo(calories=calories, carbs=carbs, protein=proteins,
                             fat=fats)
    response = get_food_recommendations(nutrients)

  return user_id, response


# 스케줄러 인스턴스 생성 및 작업 추가
scheduler = BackgroundScheduler()
scheduler.add_job(scheduled_recommendation, 'cron', hour=13, minute=12,
                  args=[3])  # 아침에 3으로 나눔
scheduler.add_job(scheduled_recommendation, 'cron', hour=10, minute=37,
                  args=[3])  # 아침에 3으로 나눔
scheduler.add_job(scheduled_recommendation, 'cron', hour=10, minute=38,
                  args=[3])  # 아침에 3으로 나눔
scheduler.add_job(scheduled_recommendation, 'cron', hour=10, minute=39,
                  args=[3])  # 아침에 3으로 나눔

scheduler.add_job(scheduled_recommendation, 'cron', hour=6, minute=30,
                  args=[3])  # 아침에 3으로 나눔
scheduler.add_job(scheduled_recommendation, 'cron', hour=11, minute=30,
                  args=[2])  # 점심에 2로 나눔
scheduler.add_job(scheduled_recommendation, 'cron', hour=17, minute=30,
                  args=[1])  # 저녁에 나누지 않음

# 스케줄러 시작
scheduler.start()


# RDB에서 특정 비율의 음식 조합을 찾는 함수
def find_combination_in_db(db, target_calories, target_carbs, target_protein,
    target_fat, tolerance=0.1):
  results = db.query(FoodCombination).filter(
      (FoodCombination.total_calories_ratio.between(
          target_calories * (1 - tolerance),
          target_calories * (1 + tolerance))) &
      (FoodCombination.carbs_ratio.between(target_carbs * (1 - tolerance),
                                           target_carbs * (1 + tolerance))) &
      (FoodCombination.protein_ratio.between(target_protein * (1 - tolerance),
                                             target_protein * (
                                                 1 + tolerance))) &
      (FoodCombination.fat_ratio.between(target_fat * (1 - tolerance),
                                         target_fat * (1 + tolerance)))
  ).all()

  return results


# 사용자 식단 추천 저장
def store_diet(user_id, diet_id):
  # 사용자별로 식단 조합 키 생성
  key = f"user:{user_id}:diet:{diet_id}"

  # 해당 식단을 3일 동안 유지하는 키 생성
  redis_client.setex(key, PERIOD, "recommended")


# 사용자에게 이미 동일한 식단을 추천했는지 확인
def is_diet_recommended(user_id, diet_id):
  key = f"user:{user_id}:diet:{diet_id}"
  return redis_client.exists(key)