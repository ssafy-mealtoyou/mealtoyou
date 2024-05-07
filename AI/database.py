from sqlalchemy import Column, BigInteger, Double, Date, ForeignKey, create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship, sessionmaker
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


# 데이터베이스 URL 및 엔진 설정 (여기에 실제 MySQL 정보 입력)
DATABASE_URL = Config.DATABASE_URL
engine = create_engine(DATABASE_URL)

# 세션 생성
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# 테이블 생성 (필요할 경우)
Base.metadata.create_all(bind=engine)