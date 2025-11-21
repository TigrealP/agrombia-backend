from fastapi import APIRouter
import httpx

router = APIRouter(prefix="/climate", tags=["climate"])
WEATHER_API = "https://api.open-meteo.com/v1/forecast"

@router.get('/')
def get_climate(lat: float, lon: float):
    url = f"{WEATHER_API}?latitude={lat}&longitude={lon}&current_weather=true"
    r = httpx.get(url, timeout=10.0)
    if r.status_code != 200:
        return {"error": "no data"}
    return r.json()
