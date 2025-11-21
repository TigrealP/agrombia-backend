from fastapi import APIRouter, Depends, HTTPException
import httpx
from sqlmodel import Session
from app.deps import get_db
from app.models import Crop

router = APIRouter(prefix="/climate", tags=["climate"])
WEATHER_API = "https://api.open-meteo.com/v1/forecast"

@router.get('/')
def get_climate(lat: float, lon: float):
    url = f"{WEATHER_API}?latitude={lat}&longitude={lon}&current_weather=true"
    r = httpx.get(url, timeout=10.0)
    if r.status_code != 200:
        return {"error": "no data"}
    return r.json()

@router.get('/crop/{crop_id}')
def get_crop_weather(crop_id: int, session: Session = Depends(get_db)):
    crop = session.get(Crop, crop_id)
    if not crop:
        raise HTTPException(status_code=404, detail="Cultivo no encontrado")
    
    if crop.latitud is None or crop.longitud is None:
         raise HTTPException(status_code=400, detail="El cultivo no tiene ubicación registrada (lat/lon)")

    url = f"{WEATHER_API}?latitude={crop.latitud}&longitude={crop.longitud}&current_weather=true"
    r = httpx.get(url, timeout=10.0)
    
    if r.status_code != 200:
        return {"error": "No se pudieron obtener datos del clima"}
    
    return r.json()
