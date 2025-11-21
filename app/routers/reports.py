from fastapi import APIRouter, Depends
from sqlmodel import Session, select
from app.deps import get_db, get_current_user
from app.models import Report, User

router = APIRouter(prefix="/reports", tags=["reports"])

@router.post('/')
def create_report(payload: dict, session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    payload['user_id'] = current_user.id
    r = Report(**payload)
    session.add(r)
    session.commit()
    session.refresh(r)
    return r

@router.get('/')
def list_reports(session: Session = Depends(get_db)):
    return session.exec(select(Report)).all()
