from fastapi import APIRouter, Depends, HTTPException
from sqlmodel import Session, select
from app.deps import get_db, get_current_user
from app.models import Crop, User
from app.schemas import CultivoCreate

router = APIRouter(prefix="/cultivos", tags=["cultivos"])

@router.post('/')
def create_cultivo(crop_in: CultivoCreate, session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    crop_data = crop_in.dict(exclude_unset=True)
    crop_data['user_id'] = current_user.id
    
    cultivo = Crop(**crop_data)
    
    session.add(cultivo)
    session.commit()
    session.refresh(cultivo)
    return cultivo

@router.get('/')
def list_my_cultivos(session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    return session.exec(select(Crop).where(Crop.user_id == current_user.id)).all()
