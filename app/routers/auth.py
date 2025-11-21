from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordRequestForm
from sqlmodel import Session, select
from app.deps import get_db
from app.auth import get_password_hash, verify_password, create_access_token
from app.models import User

router = APIRouter(prefix="/auth", tags=["auth"])

@router.post('/register')
def register(payload: dict, session: Session = Depends(get_db)):
    q = session.exec(select(User).where(User.email == payload.get("email"))).first()
    if q:
        raise HTTPException(status_code=400, detail='Email ya registrado')
    user = User(nombre=payload.get("nombre"), email=payload.get("email"), hashed_password=get_password_hash(payload.get("contraseña")))
    session.add(user)
    session.commit()
    session.refresh(user)
    return {"msg": "usuario creado", "user_id": user.id}

@router.post('/token')
def token(form_data: OAuth2PasswordRequestForm = Depends(), session: Session = Depends(get_db)):
    user = session.exec(select(User).where(User.email == form_data.username)).first()
    if not user:
        raise HTTPException(status_code=401, detail='Credenciales inválidas')
    if not verify_password(form_data.password, user.hashed_password):
        raise HTTPException(status_code=401, detail='Credenciales inválidas')
    access_token = create_access_token(subject=user.id)
    return {"access_token": access_token, "token_type": "bearer"}
