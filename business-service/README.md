# ⚙️ Business Service (Elvira & Veronique)

## 📝 Description
Gère les commandes et les avis.

## ️ Schéma de Base de Données (PostgreSQL)

### Table `orders`
- `id`: UUID (PK)
- `gig_id`: UUID
- `gig_title`: String
- `client_id`: UUID
- `client_name`: String
- `student_id`: UUID
- `student_name`: String
- `status`: String
- `budget`: Double
- `delivery_date`: Timestamp
- `deliverable_url`: String
- `tier`: String (basique, standard, premium)

### Table `reviews`
- `id`: UUID
- `order_id`: UUID
- `reviewer_id`: UUID
- `reviewer_name`: String
- `rating`: Integer
- `text`: Text (Anciennement comment)

---

##  Instructions
1. **Champs Redondants :** Stockez le `gig_title`, `client_name` et `student_name` directement dans la table `orders`. C'est une pratique courante en microservices pour éviter les appels entre services lors de l'affichage d'une liste de commandes.
2. **Reviews :** Utilisez le champ `text` au lieu de `comment` pour matcher avec Supabase.
