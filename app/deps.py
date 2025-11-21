from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from sqlmodel import Session
from jose import jwt, JWTError
from app.database import get_session
from app.models import User
import os

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/auth/token")

SECRET_KEY = os.getenv("SECRET_KEY")
ALGORITHM = "HS256"

def get_db():
    yield from get_session()

def get_current_user(token: str = Depends(oauth2_scheme), session: Session = Depends(get_db)):
    if SECRET_KEY is None:
        raise RuntimeError("SECRET_KEY no está configurada en variables de entorno.")

    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Token inválido",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        user_id = payload.get("sub")
        if user_id is None:
            raise credentials_exception
    except JWTError:
        raise credentials_exception

    user = session.get(User, user_id)
    if not user:
        raise credentials_exception
    return user

def require_admin(current_user: User = Depends(get_current_user)):
    if getattr(current_user, "is_admin", False) is not True:
        raise HTTPException(status_code=403, detail="No tienes permisos de administrador")
    return current_user
