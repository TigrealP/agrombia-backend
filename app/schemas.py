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
    fecha_registro: Optional[date] = None

class TareaCreate(SQLModel):
    nombre: str
    tipo_tarea: str
    fecha_programada: Optional[date] = None
    id_cultivo: int
