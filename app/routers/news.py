from fastapi import APIRouter
import httpx
import os
from typing import List, Optional

router = APIRouter(prefix="/news", tags=["news"])

NEWS_API_KEY = os.getenv("NEWS_API_KEY")
NEWS_URL = "https://newsapi.org/v2/everything"

@router.get('/')
def get_agricultural_news():
    if not NEWS_API_KEY:
        return {
            "warning": "Falta configurar NEWS_API_KEY en el servidor.",
            "data": [
                {
                    "titulo": "Noticia de Ejemplo (Sin API Key)",
                    "contenido": "Configura tu API Key para ver noticias reales.",
                    "fuente": "Sistema",
                    "fecha": "2023-01-01"
                }
            ]
        }

    # Parámetros de búsqueda
    params = {
        "q": "(agricultura OR agro OR campesinos OR cultivos) AND colombia",
        "language": "es",
        "sortBy": "publishedAt", # Las más recientes primero
        "apiKey": NEWS_API_KEY,
        "pageSize": 10 # Traer solo 10 para no saturar
    }

    try:
        # Hacemos la petición síncrona (igual que en climate.py)
        response = httpx.get(NEWS_URL, params=params, timeout=10.0)
        
        if response.status_code != 200:
            return {"error": "Error al consultar proveedor de noticias", "details": response.json()}
            
        data = response.json()
        articles = data.get("articles", [])
        
        # Formateamos la respuesta para que sea limpia
        formatted_news = []
        for art in articles:
            formatted_news.append({
                "titulo": art.get("title"),
                "contenido": art.get("description"),
                "fuente": art.get("source", {}).get("name"),
                "url": art.get("url"),
                "imagen": art.get("urlToImage"),
                "fecha": art.get("publishedAt")
            })
            
        return formatted_news

    except Exception as e:
        return {"error": f"Error de conexión: {str(e)}"}
