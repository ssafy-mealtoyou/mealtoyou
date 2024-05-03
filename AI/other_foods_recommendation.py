from pydantic import BaseModel
import pandas as pd

class Info(BaseModel):
  kind : str
  target_calories: float
  target_carbs: float
  target_protein: float
  target_fat: float
  now_calories: float
  now_carbs: float
  now_protein: float
  now_fat: float


def calculate_score(row, diff):
  """
  음식과 영양소 차이의 절대값의 합을 계산합니다.
  """
  score = (abs(row['에너지(㎉)'] - diff['calories']) +
           abs(row['탄수화물(g)'] - diff['carbs']) +
           abs(row['단백질(g)'] - diff['protein']) +
           abs(row['지방(g)'] - diff['fat']))
  return score


def get_otherFoods(info):
  df = pd.read_excel('updated_data.xlsx')

  # 선택된 칼럼들로 새로운 DataFrame 생성
  df_nutrients = df[
    ['종류','카테고리2','카테고리', '식품명', '1회제공량', '내용량_단위', '총내용량(g)', '총내용량(mL)', '식품대분류',
     '에너지(㎉)', '탄수화물(g)', '단백질(g)', '지방(g)', '총당류(g)', '나트륨(㎎)']].copy()
  # 데이터 유형 변환
  df_nutrients['에너지(㎉)'] = pd.to_numeric(df_nutrients['에너지(㎉)'], errors='coerce')
  df_nutrients['탄수화물(g)'] = pd.to_numeric(df_nutrients['탄수화물(g)'], errors='coerce')
  df_nutrients['단백질(g)'] = pd.to_numeric(df_nutrients['단백질(g)'], errors='coerce')
  df_nutrients['지방(g)'] = pd.to_numeric(df_nutrients['지방(g)'], errors='coerce')


  # 영양소 차이 계산
  nutrient_diff = {
    'calories': info.target_calories - info.now_calories,
    'carbs': info.target_carbs - info.now_carbs,
    'protein': info.target_protein - info.now_protein,
    'fat': info.target_fat - info.now_fat
  }

  # '종류' 필터링
  filtered_df = df_nutrients[df_nutrients['종류'] == info.kind]

  # 각 음식에 대한 점수 및 차이 계산
  def calculate_score_and_diff(row):
    score = (abs(row['에너지(㎉)'] - nutrient_diff['calories']) +
             abs(row['탄수화물(g)'] - nutrient_diff['carbs']) +
             abs(row['단백질(g)'] - nutrient_diff['protein']) +
             abs(row['지방(g)'] - nutrient_diff['fat']))
    return pd.Series([score,
                      row['에너지(㎉)'] - nutrient_diff['calories'],
                      row['탄수화물(g)'] - nutrient_diff['carbs'],
                      row['단백질(g)'] - nutrient_diff['protein'],
                      row['지방(g)'] - nutrient_diff['fat']],
                     index=['score', 'calories_diff', 'carbs_diff', 'protein_diff', 'fat_diff'])

  filtered_df[['score', 'calories_diff', 'carbs_diff', 'protein_diff', 'fat_diff']] = filtered_df.apply(calculate_score_and_diff, axis=1)

  # 점수가 낮은 순으로 음식 정렬
  optimal_foods = filtered_df.sort_values('score')

  # 결과 반환
  return optimal_foods[['식품명', '에너지(㎉)', '탄수화물(g)', '단백질(g)', '지방(g)', 'score', 'calories_diff', 'carbs_diff', 'protein_diff', 'fat_diff']].head(10)

