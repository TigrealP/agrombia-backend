from fastapi import APIRouter, Depends, HTTPException
from sqlmodel import Session, select
from app.deps import get_db, get_current_user
from app.models import Task, Crop, User
from app.schemas import TareaCreate

router = APIRouter(prefix="/tareas", tags=["tareas"])

@router.post('/')
def create_tarea(tarea_in: TareaCreate, session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    crop = session.get(Crop, tarea_in.crop_id)
    if not crop or crop.user_id != current_user.id:
        raise HTTPException(status_code=400, detail='Cultivo inválido o no pertenece al usuario')
    
    tarea = Task(**tarea_in.dict())
    session.add(tarea)
    session.commit()
    session.refresh(tarea)
    return tarea

@router.get('/by-cultivo/{cultivo_id}')
def tareas_by_cultivo(cultivo_id: int, session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    return session.exec(select(Task).where(Task.crop_id == cultivo_id)).all()
