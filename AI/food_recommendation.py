import numpy as np
import pandas as pd
import openai
from pydantic import BaseModel
from scipy.optimize import differential_evolution
from config import Config


# openai API 키 인증
openai.api_key = Config.OPENAI_API_KEY
model = "gpt-3.5-turbo"

# 기본 역할 설정
messages = [{"role": "system", "content": "영양사가 되었습니다."}]

def check_food_combinations(foods):
  """
  주어진 음식 조합이 어울리는지를 GPT-3.5 Turbo 모델에 물어봅니다.
  """
  query = f"음식 조합: {', '.join(foods)}. 이 조합이 사람들이 일반적으로 먹는 식사 조합인가? 어울리면 O 안 어울리면 X로 대답해줘"
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
  difference = np.log(1 + np.abs(nutrien - target))  # 로그를 적용하여 민감도 증가

  return np.sum(difference)

def get_food_recommendations(nutrient_info, max_attempts=10):
  # 데이터 불러오기
  print(1)
  df_nutrients = pd.read_excel('food_nutrition.xlsx')

  # 데이터 유형 변환
  df_nutrients['에너지(㎉)'] = pd.to_numeric(df_nutrients['에너지(㎉)'], errors='coerce')
  df_nutrients['탄수화물(g)'] = pd.to_numeric(df_nutrients['탄수화물(g)'], errors='coerce')
  df_nutrients['단백질(g)'] = pd.to_numeric(df_nutrients['단백질(g)'], errors='coerce')
  df_nutrients['지방(g)'] = pd.to_numeric(df_nutrients['지방(g)'], errors='coerce')

  categories = ['밥', '국', '메인반찬', '사이드반찬','김치']
  # 대분류별로 데이터 분할
  category_data = {cat: df_nutrients[df_nutrients['종류'] == cat] for cat in categories}


  # 목표 영양소 설정
  # targets = {'에너지(㎉)': nutrient_info.calories, '탄수화물(g)': nutrient_info.carbs, '단백질(g)': nutrient_info.protein, '지방(g)': nutrient_info.fat}
  targets = {'calories': nutrient_info.calories, 'carbs': nutrient_info.carbs, 'protein': nutrient_info.protein, 'fat': nutrient_info.fat}
  target = np.array([nutrient_info.calories,nutrient_info.carbs,nutrient_info.protein,nutrient_info.fat])
  # target = np.array([1500,120,53,15])
  total = sum(targets.values())
  # 목표 비율
  target_ratio = np.array([value / total for value in targets.values()])
  # 최적화 목적 함수
  active_categories=categories[:4]

  bounds = [(0, len(category_data[cat]) - 1) for cat in active_categories]

  attempt = 0
  while attempt < max_attempts:
    result = differential_evolution(objective, bounds, args=(category_data, target, total, active_categories), strategy='best1bin', maxiter=10000, popsize=15, tol=0.1, mutation=(0.5, 1), recombination=0.7)
    if result.success:
      selected_foods = [category_data[cat].iloc[int(result.x[i])]['식품명'] for i, cat in enumerate(active_categories)]
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
        return FoodRecommendations(selected_foods=selected_foods, total_nutrients=scaled_total_nutrients)
      else:
        print("Combination not suitable, retrying...")
    else:
      print("Optimization failed:", result.message)
    attempt += 1
  return None  # Re


    # 최적화 실행
    # result = differential_evolution(objective, bounds, args=(category_data, target, total, active_categories),strategy='best1bin', maxiter=10000, popsize=15, tol=0.1, mutation=(0.5, 1), recombination=0.7)
    # # 결과 출력
    # if result.success:
    #   selected_foods = []
    #   total_nutrients_summary = np.zeros(4)  # 에너지(㎉), 탄수화물(g), 단백질(g), 지방(g)
    #   for i, cat in enumerate(active_categories):
    #     idx = int(result.x[i])
    #     selected_food = category_data[cat].iloc[idx]
    #     selected_foods.append(selected_food['식품명'])
    #     # 각 선택된 음식의 영양소를 더함
    #     nutrients = selected_food[['에너지(㎉)', '탄수화물(g)', '단백질(g)', '지방(g)']].apply(pd.to_numeric, errors='coerce').fillna(0).values
    #     total_nutrients_summary += nutrients
    #
    #   combination_check = check_food_combinations(selected_foods)
    #   if combination_check == 'O':
    #   print("음식 조합 검증 결과:", combination_check)
    #   print(selected_foods)
    #
    #   # 선택된 음식들과 총 영양소 출력
    #   print("Selected foods:", selected_foods)
    #   print("Total nutrients (Calories, Carbs, Protein, Fat):", total_nutrients_summary)
    #
    #   to = sum(total_nutrients_summary)
    #   print(target_ratio)
    #   print(total_nutrients_summary/to)
    #   print(targets)
    #   print(total_nutrients_summary/to * total)
    #
    #   for i, cat in enumerate(active_categories):
    #     selected_food_idx = int(result.x[i])
    #     selected_food = category_data[cat].iloc[selected_food_idx]
    #     print(f"Selected {cat}: {selected_food['식품명']}, Quantity: {selected_food['1회제공량']/to*total}(g/mg)")
    #
    #   return FoodRecommendations(selected_foods= selected_foods,total_nutrients=(total_nutrients_summary/to * total).tolist())
    # else:
    #   print("Optimization failed:", result.message)
    # pass