from app.database import engine, init_db
from sqlmodel import Session, text
from app import models, auth

def seed():
    init_db()
    with Session(engine) as session:
        # admin
        admin = session.exec(text("SELECT * FROM users WHERE email='admin@local'")).first()
        if not admin:
            a = models.User(nombre="Admin", email="admin@local", hashed_password=auth.get_password_hash("admin123"), is_admin=True)
            session.add(a)
        # farmer
        farmer = session.exec(text("SELECT * FROM users WHERE email='farmer@local'")).first()
        if not farmer:
            f = models.User(nombre="Farmer", email="farmer@local", hashed_password=auth.get_password_hash("farmer123"), is_admin=False)
            session.add(f)
        session.commit()
        print("Seed finished")

if __name__ == "__main__":
    seed()
