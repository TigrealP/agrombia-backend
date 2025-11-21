from fastapi import APIRouter, Depends
from sqlmodel import Session, select
from app.deps import get_db, get_current_user
from app.models import Report, User
from app.schemas import ReportCreate

router = APIRouter(prefix="/reports", tags=["reports"])

@router.post('/')
def create_report(report_in: ReportCreate, session: Session = Depends(get_db), current_user: User = Depends(get_current_user)):
    report_data = report_in.dict()
    report_data['user_id'] = current_user.id
    
    r = Report(**report_data)
    session.add(r)
    session.commit()
    session.refresh(r)
    return r

@router.get('/')
def list_reports(session: Session = Depends(get_db)):
    return session.exec(select(Report)).all()
