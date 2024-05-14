import os

import numpy as np
import pandas as pd
import openai
import redis
from pydantic import BaseModel
from scipy.optimize import differential_evolution
from database import SessionLocal, FoodCombination
from config import Config
import os


# openai API 키 인증
openai.api_key = Config.OPENAI_API_KEY
model = "gpt-3.5-turbo"
redis_ip = os.getenv("REDIS", "localhost")
# Redis 클라이언트 인스턴스 생성
redis_client = redis.StrictRedis(host=redis_ip, port=6379, db=0)
PERIOD = 60 * 60 * 24 * 3  # 3일간 캐싱

# 기본 역할 설정
messages = [{"role": "system", "content": "영양사가 되었습니다."}]

global df_nutrients
df_nutrients = None
global weights
weights = None


def load_data():
  global df_nutrients
  if df_nutrients is None:
    df_nutrients = pd.read_excel('food_nutrition.xlsx')
    # 데이터 유형 변환
    df_nutrients['에너지(㎉)'] = pd.to_numeric(df_nutrients['에너지(㎉)'], errors='coerce')
    df_nutrients['탄수화물(g)'] = pd.to_numeric(df_nutrients['탄수화물(g)'], errors='coerce')
    df_nutrients['단백질(g)'] = pd.to_numeric(df_nutrients['단백질(g)'], errors='coerce')
    df_nutrients['지방(g)'] = pd.to_numeric(df_nutrients['지방(g)'], errors='coerce')
  return df_nutrients

def calculate_weights(target):
  global weights
  if weights is None:
    # target 배열에서 0이 아닌 값에 대해 역수를 취함
    # 0 값을 방지하기 위해 작은 상수를 더함
    weights = 1 / (target + 0.1)
    # 가중치 정규화 (합이 1이 되도록)
    weights /= weights.sum()
  return weights

def check_food_combinations(foods):
  #음식 조합이 어울리는지 확인
  query = f"음식 조합: {', '.join(foods)}. 이 조합이 사람들이 일반적으로 먹는 식사 조합인가? 어울리면 O 안 어울리면 X로만 대답해줘"
  print(query)
  try:
    response = openai.ChatCompletion.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "system", "content": query}]
    )
    return response['choices'][0]['message']['content'].strip()
  except Exception as e:
    print("API 호출 중 에러 발생:", str(e))
    return "모델 호출에 실패했습니다."


# 입력 받을 기본적인 모델 생성
class Input(BaseModel):
  user_type: str
  partner_type: str
  situation_type: str
  Relationship: str
class QuestionInput(BaseModel):
  question: str

class NutrientInfo(BaseModel):
  calories: float
  carbs: float
  protein: float
  fat: float
class FoodRecommendations(BaseModel):
  selected_foods: list
  total_nutrients: list


def objective(selections, category_data, target,total, active_categories):
  total_nutrients = np.zeros(4)  # 탄, 단, 지, 칼로리
  # active_categories = categories[:4]  # 동적 카테고리 리스트

  # '국' 선택 확인
  guk_idx = active_categories.index('국')
  selected_guk = category_data['국'].iloc[int(selections[guk_idx])]
  common_categories = None  # 공통 카테고리 집합

  if '국밥' in {selected_guk['카테고리1'], selected_guk['카테고리2']}:
    active_categories[0] = '김치'  # '밥' 대신 '김치' 선택
  else:
    active_categories[0] = '밥'  # '밥' 선택
  for i, cat in enumerate(active_categories):
    # print(selections)
    idx = int(selections[i])
    if idx >= len(category_data[cat]):  # 인덱스 유효성 검사
      return float('inf')  # 유효하지 않은 인덱스 접근 시 높은 비용 반환

    selected_food = category_data[cat].iloc[idx]
    nutrients = np.array([selected_food['에너지(㎉)'], selected_food['탄수화물(g)'], selected_food['단백질(g)'], selected_food['지방(g)']])
    total_nutrients += nutrients

    food_categories = {selected_food['카테고리1'], selected_food['카테고리2']}

    if common_categories is None:
      common_categories = food_categories
    else:
      common_categories &= food_categories  # 교집합 업데이트

  if not common_categories:
    # 공통 카테고리가 없으면 높은 비용을 반환
    return 10000
  # 모든 영양소의 총합
  total_sum = np.sum(total_nutrients)
  # 각 영양소의 비율 계산
  nutrien = total_nutrients / total_sum * total

  global weights
  # 가중치 적용된 차이(로그를 사용하여 민감도 증가시킴)
  difference = weights * np.log(1 + np.abs(nutrien - target))


  return np.sum(difference)

def get_food_recommendations(nutrient_info,user_id=1, max_attempts=15):
  # 데이터 불러오기
  df_nutrients = load_data()
  print(1231321)
  #카테고리를 크게 5가지로 나눔
  categories = ['밥', '국', '메인반찬', '사이드반찬','김치']
  # 대분류별로 데이터 분할
  category_data = {cat: df_nutrients[df_nutrients['종류'] == cat] for cat in categories}

  # 목표 칼로리와, 탄단지
  targets = {'calories': nutrient_info.calories, 'carbs': nutrient_info.carbs, 'protein': nutrient_info.protein, 'fat': nutrient_info.fat}
  target = np.array([nutrient_info.calories,nutrient_info.carbs,nutrient_info.protein,nutrient_info.fat])
  global weights
  weights = calculate_weights(target)
  total = sum(targets.values())

  active_categories=categories[:4]

  bounds = [(0, len(category_data[cat]) - 1) for cat in active_categories]

  attempt = 0
  while attempt < max_attempts:
    result = differential_evolution(objective, bounds, args=(category_data, target, total, active_categories), strategy='best1bin', maxiter=2000, popsize=15, tol=0.1, mutation=(0.5, 1), recombination=0.7)
    if result.success:
      selected_foods = [category_data[cat].iloc[int(result.x[i])]['식품명'] for i, cat in enumerate(active_categories)]
      # Redis를 통해 최근 3일안에 추천된 식단인지 확인
      key = f"user:{user_id}:foods:{'_'.join(selected_foods)}"
      if redis_client.exists(key):
        print(f"User {user_id}: This combination is already recommended.")
        continue
      combination_check = check_food_combinations(selected_foods)
      print(combination_check)
      if combination_check == 'O':
        total_nutrients_summary = np.zeros(4)  # Initialize nutrient summary array
        for i, cat in enumerate(active_categories):
          selected_food_idx = int(result.x[i])
          selected_food = category_data[cat].iloc[selected_food_idx]
          nutrients = selected_food[['에너지(㎉)', '탄수화물(g)', '단백질(g)', '지방(g)']].apply(pd.to_numeric, errors='coerce').fillna(0).values
          total_nutrients_summary += nutrients

        print("Selected foods:", selected_foods)
        print("Total nutrients (Calories, Carbs, Protein, Fat):", total_nutrients_summary)

        to = sum(total_nutrients_summary)
        scaled_total_nutrients = (total_nutrients_summary / to * total).tolist()
        for i, cat in enumerate(active_categories):
          selected_food_idx = int(result.x[i])
          selected_food = category_data[cat].iloc[selected_food_idx]
          print(f"Selected {cat}: {selected_food['식품명']}, Quantity: {selected_food['1회제공량']/to*total}(g/mg)")

        dietFoods = []
        for i, cat in enumerate(active_categories):
          selected_food_idx = int(result.x[i])
          selected_food = category_data[cat].iloc[selected_food_idx]
          dietFood = {
            'name': selected_food['식품명'],
            'imageUrl': '',  # 실제 이미지 URL이 필요
            'calories': float(selected_food['에너지(㎉)']),
            'carbohydrate': float(selected_food['탄수화물(g)']),
            'protein': float(selected_food['단백질(g)']),
            'fat': float(selected_food['지방(g)'])
          }
          dietFoods.append(dietFood)


      # Redis에 새로운 식단 조합을 저장
        redis_client.setex(key, PERIOD, "recommended")
        diet_id = store_food_data(selected_foods, scaled_total_nutrients[0], scaled_total_nutrients[1], scaled_total_nutrients[2], scaled_total_nutrients[3])
        # store_food_data(selected_foods,scaled_total_nutrients[0],scaled_total_nutrients[1],scaled_total_nutrients[2],scaled_total_nutrients[3])

        return {
          "dietId": diet_id,
          "totalCalories": int(scaled_total_nutrients[0]),
          "carbohydratePer": int(scaled_total_nutrients[1] / scaled_total_nutrients[0] * 100),
          "proteinPer": int(scaled_total_nutrients[2] / scaled_total_nutrients[0] * 100),
          "fatPer": int(scaled_total_nutrients[3] / scaled_total_nutrients[0] * 100),
          'dietFoods': dietFoods
        }
      #FoodRecommendations(selected_foods=selected_foods, total_nutrients=scaled_total_nutrients)
      else:
        print("Combination not suitable, retrying...")
    else:
      print("Optimization failed:", result.message)
    attempt += 1
  return None  # Re


def store_food_data(food_names, total_calories, carbs, protein, fat):

  # 각 영양소의 비율 계산 (총 영양소 칼로리 대비)
  calories_ratio = round(total_calories / (total_calories+carbs+protein+fat),6)
  carb_ratio = round(carbs / (total_calories+carbs+protein+fat),6)
  protein_ratio = round(protein / (total_calories+carbs+protein+fat), 6)
  fat_ratio = round(fat / (total_calories+carbs+protein+fat), 6)

  # # DB에 저장할 음식 데이터
  food_combination = FoodCombination(
      food_names=food_names,
      total_calories_ratio=calories_ratio,
      carbs_ratio=carb_ratio,
      protein_ratio=protein_ratio,
      fat_ratio=fat_ratio
  )
  # 데이터베이스 세션을 생성하여 데이터 저장
  session = SessionLocal()
  try:
    session.add(food_combination)
    session.commit()
    return food_combination.id
  except Exception as e:
    print("데이터 저장 중 오류 발생:", str(e))
    session.rollback()
  finally:
    session.close()

def is_diet_recommended(user_id, diet_id):
  key = f"user:{user_id}:diet:{diet_id}"
  return redis_client.exists(key)