from fastapi import APIRouter, Depends, HTTPException
from sqlmodel import Session, select
from app.deps import get_db, get_current_user
from app.models import Crop, User

router = APIRouter(prefix="/cultivos", tags=["cultivos"])

@router.post('/')
def create_cultivo(payload: dict, session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    payload['user_id'] = current_user.id
    cultivo = Crop(**payload)
    session.add(cultivo)
    session.commit()
    session.refresh(cultivo)
    return cultivo

@router.get('/')
def list_my_cultivos(session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    return session.exec(select(Crop).where(Crop.user_id == current_user.id)).all()
