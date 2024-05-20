import os

from dotenv import load_dotenv

load_dotenv()


class Config:
  MODEL_PATH = os.getenv("MODEL_PATH")
  OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
  DATABASE_URL = os.getenv("DATABASE_URL")
