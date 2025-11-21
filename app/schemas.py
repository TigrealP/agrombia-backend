from datetime import datetime
from sqlmodel import SQLModel
from typing import Optional, List
from datetime import date

class Token(SQLModel):
    access_token: str
    token_type: str = "bearer"

class UserCreate(SQLModel):
    nombre: str
    email: str
    contraseña: str
    rol: Optional[str] = "Agricultor"
    ubicacion: Optional[str] = None

class UserRead(SQLModel):
    id: int
    nombre: str
    email: str
    rol: str
    ubicacion: Optional[str]

class CultivoCreate(SQLModel):
    nombre: str
    tipo: Optional[str] = None
    latitud: Optional[float] = None
    longitud: Optional[float] = None
    fecha_registro: Optional[date] = None

class TareaCreate(SQLModel):
    titulo: str
    descripcion: Optional[str] = None
    fecha: Optional[datetime] = None
    crop_id: int

class ReportCreate(SQLModel):
    tipo: str
    descripcion: Optional[str] = None
    fecha: Optional[datetime] = None
