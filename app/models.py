from typing import Optional, List
from datetime import datetime
from sqlmodel import SQLModel, Field, Relationship


class User(SQLModel, table=True):
    __tablename__ = "users"

    id: Optional[int] = Field(default=None, primary_key=True)
    nombre: str
    email: str
    hashed_password: str
    is_admin: bool = Field(default=False)

    # Relaciones
    crops: List["Crop"] = Relationship(back_populates="user")


class Crop(SQLModel, table=True):
    __tablename__ = "crops"

    id: Optional[int] = Field(default=None, primary_key=True)
    nombre: str
    tipo: Optional[str] = None
    fecha_registro: Optional[datetime] = None
    user_id: int = Field(foreign_key="users.id")

    user: Optional[User] = Relationship(back_populates="crops")
    tasks: List["Task"] = Relationship(back_populates="crop")
    alerts: List["Alert"] = Relationship(back_populates="crop")


class Task(SQLModel, table=True):
    __tablename__ = "tasks"

    id: Optional[int] = Field(default=None, primary_key=True)
    titulo: str
    descripcion: Optional[str] = None
    fecha: Optional[datetime] = None
    crop_id: int = Field(foreign_key="crops.id")

    crop: Optional[Crop] = Relationship(back_populates="tasks")


class Climate(SQLModel, table=True):
    __tablename__ = "climate"

    id: Optional[int] = Field(default=None, primary_key=True)
    fecha: Optional[datetime] = None
    temperatura: Optional[float] = None
    humedad: Optional[float] = None
    precipitacion: Optional[float] = None
    region: Optional[str] = None
    user_id: Optional[int] = Field(default=None, foreign_key="users.id")


class Alert(SQLModel, table=True):
    __tablename__ = "alerts"

    id: Optional[int] = Field(default=None, primary_key=True)
    titulo: Optional[str] = None
    mensaje: str
    severidad: Optional[str] = None
    fecha: Optional[datetime] = None
    crop_id: Optional[int] = Field(default=None, foreign_key="crops.id")
    user_id: Optional[int] = Field(default=None, foreign_key="users.id")

    crop: Optional[Crop] = Relationship(back_populates="alerts")


class Report(SQLModel, table=True):
    __tablename__ = "reports"

    id: Optional[int] = Field(default=None, primary_key=True)
    tipo: Optional[str] = None
    descripcion: Optional[str] = None
    fecha: Optional[datetime] = None
    user_id: Optional[int] = Field(default=None, foreign_key="users.id")


class News(SQLModel, table=True):
    __tablename__ = "news"

    id: Optional[int] = Field(default=None, primary_key=True)
    titulo: str
    contenido: Optional[str] = None
    fuente: Optional[str] = None
    fecha: Optional[datetime] = None
