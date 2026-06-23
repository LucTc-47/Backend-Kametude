# 🤝 Request Service (Prisca & Melista)

## 📝 Description
Gère les appels d'offres.

## ️ Schéma de Base de Données (PostgreSQL)

### Table `gig_requests`
- `id`: UUID (PK)
- `title`: String
- `description`: Text
- `budget`: Double (Anciennement price)
- `category`: String
- `location`: String
- `deadline`: Timestamp
- `status`: String (OPEN, CLOSED)
- `client_id`: UUID
- `client_name`: String

### Table `request_proposals`
- `id`: UUID (PK)
- `request_id`: UUID
- `student_id`: UUID
- `student_name`: String
- `message`: Text
- `price`: Double
- `delivery_days`: Integer
- `status`: String

---

##  Instructions
1. **Budget :** Utilisez le champ `budget` pour correspondre au type Supabase.
2. **Affichage :** Assurez-vous de renvoyer le `client_name` et le `student_name` pour éviter de faire des jointures côté frontend.
