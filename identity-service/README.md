#  Identity & Auth Service (Darwin & Ericka)

##  Description
Gère l'authentification et les profils. **Important :** Pour rester compatible avec le frontend, les noms de champs doivent correspondre à la table `profiles` de Supabase.

##  Schéma de Base de Données (PostgreSQL)

### Table `users` (Interne)
- `id`: UUID (PK)
- `email`: String (Unique)
- `password`: String (Haché)
- `role`: Enum (admin, moderator, user, student, client)

### Table `profiles` (Compatible Frontend)
- `id`: UUID (PK)
- `user_id`: UUID (FK)
- `first_name`: String
- `last_name`: String
- `email`: String
- `phone`: String
- `avatar_url`: String
- `bio`: Text
- `city`: String
- `skills`: List<String>
- `rating`: Float
- `role`: String (admin, moderator, user, student, client)
- `verified`: Boolean (Anciennement is_verified)
- `created_at`: Timestamp

---

##  Instructions
1. **Compatibilité :** Utilisez `verified` (au lieu de `is_verified`) pour que le badge de vérification s'affiche sans changer le React.
2. **DTO :** Le JSON de réponse du profil doit être identique à celui de Supabase.
