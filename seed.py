from app.database import engine, init_db
from sqlmodel import Session, text
from app import models, auth

def seed():
    init_db()
    with Session(engine) as session:
        # admin
        admin = session.exec(text("SELECT * FROM usuario WHERE email='admin@local'")).first()
        if not admin:
            a = models.Usuario(nombre="Admin", email="admin@local", contraseña=auth.get_password_hash("admin123"), rol=models.Role.Administrador)
            session.add(a)
        # farmer
        farmer = session.exec(text("SELECT * FROM usuario WHERE email='farmer@local'")).first()
        if not farmer:
            f = models.Usuario(nombre="Farmer", email="farmer@local", contraseña=auth.get_password_hash("farmer123"), rol=models.Role.Agricultor, ubicacion="Valle")
            session.add(f)
        session.commit()
        print("Seed finished")

if __name__ == "__main__":
    seed()
