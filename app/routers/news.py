from fastapi import APIRouter, Depends
from sqlmodel import Session, select
from app.deps import get_db
from app.models import News

router = APIRouter(prefix="/news", tags=["news"])

@router.post('/')
def create_news(payload: dict, session: Session = Depends(get_db)):
    n = News(**payload)
    session.add(n)
    session.commit()
    session.refresh(n)
    return n

@router.get('/')
def list_news(session: Session = Depends(get_db)):
    return session.exec(select(News)).all()
