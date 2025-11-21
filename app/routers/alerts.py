from fastapi import APIRouter, Depends, HTTPException
from sqlmodel import Session, select
from app.deps import get_db, get_current_user
from app.models import Alert, User

router = APIRouter(prefix="/alerts", tags=["alerts"])

@router.post('/')
def create_alert(payload: dict, session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    payload['user_id'] = current_user.id
    alert = Alert(**payload)
    session.add(alert)
    session.commit()
    session.refresh(alert)
    return alert

@router.get('/')
def list_alerts(session: Session = Depends(get_db)):
    return session.exec(select(Alert)).all()
