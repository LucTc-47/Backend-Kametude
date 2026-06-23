#  Catalog Service (Noel & Steve)

##  Description
Gère les Gigs. **Important :** Le frontend s'attend à ce que les paliers (Tiers) soient inclus dans l'objet Gig sous forme de JSON.

## ️ Schéma de Base de Données (PostgreSQL)

### Table `gigs`
- `id`: UUID (PK)
- `title`: String
- `description`: Text
- `category`: String (Le nom de la catégorie)
- `location`: String (La ville)
- `student_id`: UUID
- `rating`: Float
- `images`: List<String> (Array d'URLs)
- `tier_basique`: JSON (Détails du pack basique)
- `tier_standard`: JSON (Détails du pack standard)
- `tier_premium`: JSON (Détails du pack premium)
- `published`: Boolean

---

##  Instructions
1. **Paliers (Tiers) :** Au lieu d'une table séparée complexe, stockez les 3 tiers dans des colonnes JSONB (`tier_basique`, `tier_standard`, `tier_premium`) comme dans Supabase. Cela simplifie énormément le code Frontend.
2. **Recherche :** Implémentez la recherche par `location` et `category`.
