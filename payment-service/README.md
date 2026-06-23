#  Payment Service (Luc & Dilane)

##  Description
Gère les transactions Campay.

## ️ Schéma de Base de Données (PostgreSQL)

### Table `payment_transactions`
- `id`: UUID (PK)
- `order_id`: UUID
- `amount`: Double
- `status`: String (PENDING, SUCCESS, FAILED)
- `provider`: String (Campay)
- `external_reference`: String
- `phone`: String

---

##  Instructions
1. **Nomenclature :** Utilisez `payment_transactions` comme nom de table pour rester proche du schéma initial.
2. **Campay :** Ce service est le seul à appeler l'API Campay.
