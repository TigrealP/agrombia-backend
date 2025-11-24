from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.database import init_db
from app.routers import auth as auth_router, users as users_router, crops as crops_router, tasks as tasks_router, climate as climate_router, reports as reports_router, news as news_router

def create_app():
    app = FastAPI(title="Agrombia API")
    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    app.include_router(auth_router.router)
    app.include_router(users_router.router)
    app.include_router(crops_router.router)
    app.include_router(tasks_router.router)
    app.include_router(climate_router.router)
    # app.include_router(alerts_router.router) -> Eliminado
    app.include_router(reports_router.router)
    app.include_router(news_router.router)

    return app

app = create_app()

@app.on_event("startup")
def on_startup():
    init_db()
