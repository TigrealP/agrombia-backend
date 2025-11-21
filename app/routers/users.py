from fastapi import APIRouter, Depends
from sqlmodel import Session, select
from app.deps import get_db, get_current_user
from app.models import User

router = APIRouter(prefix="/users", tags=["users"])

@router.get('/me')
def me(current_user: User = Depends(get_current_user)):
    return current_user

@router.get('/')
def list_users(session: Session = Depends(get_db)):
    return session.exec(select(User)).all()
