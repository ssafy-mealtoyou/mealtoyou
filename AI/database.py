from sqlalchemy import Column, Integer, Float, BigInteger, Double, JSON, Index, \
  Date, ForeignKey, create_engine, String, Boolean, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from config import Config

Base = declarative_base()

# exercise 테이블 모델
class Exercise(Base):
  __tablename__ = 'exercise'
  exercise_id = Column(BigInteger, primary_key=True, autoincrement=True)
  calories_burned = Column(Double)
  steps = Column(BigInteger)
  step_start_date = Column(Date)
  calories_start_date = Column(Date)
  user_id = Column(BigInteger, ForeignKey('user_health.user_id'))

# user_health 테이블 모델
class UserHealth(Base):
  __tablename__ = 'user_health'
  user_health_id = Column(BigInteger, primary_key=True, autoincrement=True)
  user_id = Column(BigInteger, index=True)
  measured_date = Column(Date)
  weight = Column(Double)
  body_fat = Column(Double)
  skeletal_muscle = Column(Double)
  bmr = Column(Double)
  bmi = Column(Double)


# 음식 조합 테이블 모델
class FoodCombination(Base):
  __tablename__ = "food_combinations"

  id = Column(Integer, primary_key=True, autoincrement=True)
  food_names = Column(JSON, nullable=False)  # 음식 이름의 리스트
  total_calories_ratio = Column(Float, nullable=False)  # 총 칼로리 비율
  carbs_ratio = Column(Float, nullable=False)  # 총 탄수화물 비율
  protein_ratio = Column(Float, nullable=False)  # 총 단백질 비율
  fat_ratio = Column(Float, nullable=False)  # 총 지방 비율

  # 인덱스 설정 (총 칼로리 및 영양소 기준)
  # 다중 컬럼 인덱스 설정 (총 칼로리, 탄수화물, 단백질 순서)
  __table_args__ = (
    Index('idx_calories_carbs_protein', 'total_calories_ratio', 'carbs_ratio',
          'protein_ratio', 'fat_ratio'),
  )

class User(Base):
  __tablename__ = 'users'

  user_id = Column(Integer, primary_key=True, autoincrement=True)
  email = Column(String(50))
  nickname = Column(String(100))
  social_key = Column(String(100))
  gender = Column(Integer)
  age = Column(Integer)
  height = Column(Float)
  weight = Column(Float)
  intermittent_fasting_yn = Column(Boolean)
  user_image_url = Column(String(300))
  goal_weight = Column(Float)
  goal_end_date = Column(DateTime)
  withdraw_yn = Column(Boolean)
  role = Column(String(20))
  fcm_token = Column(String(500))

class Diet(Base):
  __tablename__ = 'diet'
  diet_id = Column(BigInteger, primary_key=True, autoincrement=True)
  user_id = Column(BigInteger, nullable=False)
  create_datetime = Column(DateTime, nullable=False)
  total_calories = Column(Float, nullable=False)
  total_carbohydrate = Column(Float, nullable=False)
  total_protein = Column(Float, nullable=False)
  total_fat = Column(Float, nullable=False)



# 데이터베이스 URL 및 엔진 설정 (여기에 실제 MySQL 정보 입력)
DATABASE_URL = Config.DATABASE_URL
engine = create_engine(DATABASE_URL,pool_size=10, max_overflow=20)

# 세션 생성
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# 테이블 생성 (필요할 경우)
Base.metadata.create_all(bind=engine)
