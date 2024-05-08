from apscheduler.schedulers.background import BackgroundScheduler
from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from database import SessionLocal, Exercise, UserHealth, FoodCombination
from food_recommendation import get_food_recommendations, NutrientInfo
from other_foods_recommendation import get_otherFoods, Info

app = FastAPI()


def get_db():
  db = SessionLocal()
  try:
    yield db
  finally:
    db.close()


#테스트용 나중에 지우기
@app.get("/")
async def root():
  return {"message": "Hello World"}


# 음식 추천 api
@app.post("/recommendations")
def get_recommendations(nutrient_info: NutrientInfo, db: Session = Depends(get_db)):
  sum = nutrient_info.calories+ nutrient_info.carbs+ nutrient_info.protein+ nutrient_info.fat
  # 먼저 RDB에서 내가 원하는 목표값과 비슷한 조합이 있는지 확인
  existing_combination = find_combination_in_db(
      db, nutrient_info.calories/sum, nutrient_info.carbs/sum, nutrient_info.protein/sum, nutrient_info.fat/sum
  )
  # 기존 조합이 있을 경우
  if existing_combination :
    return {
      "selected_foods": existing_combination.food_names,
      "total_nutrients": [
        existing_combination.total_calories_ratio,
        existing_combination.carbs_ratio,
        existing_combination.protein_ratio,
        existing_combination.fat_ratio
      ],
      "nutrients": [
        nutrient_info.calories,
        nutrient_info.carbs,
        nutrient_info.protein,
        nutrient_info.fat
      ],
      "total": [
        existing_combination.total_calories_ratio*sum,
        existing_combination.carbs_ratio*sum,
        existing_combination.protein_ratio*sum,
        existing_combination.fat_ratio*sum
      ],
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
  user_health_data = db.query(UserHealth).filter(UserHealth.user_id == user_id).first()
  exercise_data = db.query(Exercise).filter(Exercise.user_id == user_id).first()
  if not user_health_data or not exercise_data:
    return None
  # 먹은 칼로리
  ate_calories = 0
  # 기초대사량 + 소모 칼로리 + 섭취 칼로리
  return user_health_data.bmr + exercise_data.calories_burned - ate_calories

# 사용자 목록 가져오기
def get_all_users(db: Session):
  return db.query(UserHealth.user_id).distinct().all()


# 목표에 따른 영양소 비율 설정
def get_nutrient_ratios(current_weight, target_weight):
  if target_weight < current_weight:  # 살 뺄 때
    return 0.40, 0.35, 0.25
  elif target_weight > current_weight:  # 증량할 때
    return 0.55, 0.30, 0.15
  else:  # 체중을 유지할 때
    return 0.50, 0.25, 0.25

# 함수 호출을 위한 스케줄러 작업
def scheduled_recommendation(split_factor=None):
  print(split_factor)
  # 데이터베이스 세션 관리
  with SessionLocal() as db:
    # 모든 사용자를 가져와 각 사용자의 칼로리 계산 및 추천 실행
    all_users = db.query(UserHealth.user_id, UserHealth.weight).distinct().all()
    print(all_users)
    for user in all_users:
      print(user)
      user_id, current_weight = user
      user_health_data = db.query(UserHealth).filter(UserHealth.user_id == user_id).first()

      target_weight = user_health_data.weight
      # 목표에 따른 영양소 비율 계산
      carb_ratio, protein_ratio, fat_ratio = get_nutrient_ratios(current_weight, target_weight)

      # 총 섭취 칼로리 계산
      calories = calculate_calories(user_id, db)
      if calories is None:
        print(f"Error: Could not calculate calories for user {user_id}")
        continue

      # 아침, 점심, 저녁 시간대에 따라 칼로리를 나누기 위해 `split_factor` 적용
      if split_factor:
        calories = round(calories / split_factor, 2)
      else:
        calories = round(calories, 2)


      # 영양소 비율에 따라 칼로리 계산 및 그램 단위 변환
      carbs_calories = calories * carb_ratio
      protein_calories = calories * protein_ratio
      fat_calories = calories * fat_ratio

      carbs = round(carbs_calories / 4, 2)
      proteins = round(protein_calories / 4, 2)
      fats = round(fat_calories / 9, 2)

      nutrients = NutrientInfo(calories=calories,
                               carbs=carbs,
                               protein=proteins,
                               fat=fats)
      response = get_food_recommendations(nutrients)
      print(f"User {user_id} - Recommendation: {response}")

# 스케줄러 인스턴스 생성 및 작업 추가
scheduler = BackgroundScheduler()
scheduler.add_job(scheduled_recommendation, 'cron', hour=6, minute=50, args=[3])  # 아침에 3으로 나눔
scheduler.add_job(scheduled_recommendation, 'cron', hour=11, minute=50, args=[2])  # 점심에 2로 나눔
scheduler.add_job(scheduled_recommendation, 'cron', hour=17, minute=50, args=[None])  # 저녁에 나누지 않음


# 스케줄러 시작
scheduler.start()


# RDB에서 특정 비율의 음식 조합을 찾는 함수
def find_combination_in_db(db, target_calories, target_carbs, target_protein, target_fat, tolerance=0.5):
  results = db.query(FoodCombination).filter(
      (FoodCombination.total_calories_ratio.between(target_calories * (1 - tolerance), target_calories * (1 + tolerance))) &
      (FoodCombination.carbs_ratio.between(target_carbs * (1 - tolerance), target_carbs * (1 + tolerance))) &
      (FoodCombination.protein_ratio.between(target_protein * (1 - tolerance), target_protein * (1 + tolerance))) &
      (FoodCombination.fat_ratio.between(target_fat * (1 - tolerance), target_fat * (1 + tolerance)))
  ).first()

  return results

