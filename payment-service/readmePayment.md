# Payment Service (Luc & Dilane)

## Description
Gère les transactions de paiement pour Kam'Etud (escrow, wallet, intégration mobile money).

L'architecture est conçue pour être **agnostique du fournisseur de paiement** : une interface `PaymentProvider` permet de basculer entre FedaPay et d'autres sandbox sans modifier le service ni le contrôleur.

> ⚠️ **Statut provisoire (25/06/2026) :** FedaPay est actuellement utilisé pour les tests de développement, car le sandbox Campay limite les transactions à 25 FCFA, ce qui empêche de tester des montants réalistes (5000–7000 FCFA).

## Architecture

```
com.kametude.payment/
├── PaymentServiceApplication.java
├── controller/
│   └── PaymentController.java       → endpoints REST
├── service/
│   └── PaymentService.java          → logique métier (escrow, wallet)
├── provider/
│   ├── PaymentProvider.java         → interface commune aux fournisseurs
│   └── FedapayProvider.java         → implémentation FedaPay (active par défaut)
├── repository/
│   └── PaymentTransactionRepository.java
├── entity/
│   └── PaymentTransaction.java
└── dto/
    ├── PaymentRequestDTO.java
    └── PaymentResponseDTO.java
```

## Schéma de Base de Données (PostgreSQL)

### Table `payment_transactions`

| Champ | Type | Description |
|---|---|---|
| `id` | UUID (PK) | Identifiant interne de la transaction |
| `order_id` | UUID | Référence à la commande concernée |
| `amount` | Double | Montant en FCFA |
| `status` | String | `PENDING`, `SUCCESS`, `FAILED` |
| `provider` | String | Fournisseur utilisé (`FedaPay`) |
| `external_reference` | String | Référence/ID renvoyé par le fournisseur de paiement |
| `phone` | String | Numéro Mobile Money du payeur |

**Nomenclature :** la table conserve le nom `payment_transactions` pour rester compatible avec le schéma initial validé par l'équipe.

## Endpoints

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/payments` | Initie un paiement auprès du fournisseur actif et enregistre la transaction en base |

### Exemple de requête
```json
POST http://localhost:8085/api/payments
Content-Type: application/json

{
  "orderId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 6000,
  "phone": "237600000000"
}
```

### Exemple de réponse
```json
{
  "transactionId": "dc5fab57-ebc1-40d3-8b29-ddbbff50ae3f",
  "status": "PENDING",
  "externalReference": "461328",
  "message": "Paiement initié (FedaPay sandbox) pour la commande ..."
}
```

## Configuration requise

Le service nécessite les clés API sandbox du fournisseur actif, fournies via variables d'environnement (jamais commitées, voir `.gitignore`) :

```
FEDAPAY_SECRET_KEY=sk_sandbox_xxxxx
FEDAPAY_PUBLIC_KEY=pk_sandbox_xxxxx
```

## Lancer le service

1. **Démarrer la base de données isolée**
```bash
docker compose up -d payment-db
```

2. **Définir les variables d'environnement** (dans le terminal utilisé pour lancer l'application)
```powershell
$env:FEDAPAY_SECRET_KEY="sk_sandbox_xxxxx"
$env:FEDAPAY_PUBLIC_KEY="pk_sandbox_xxxxx"
```

3. **Lancer l'application**
```bash
mvn spring-boot:run
```

Le service écoute sur le port **8085** et se connecte à `payment_db` sur le port **5435**.

## Instructions générales

1. **Nomenclature :** utilisez `payment_transactions` comme nom de table pour rester proche du schéma initial.
2. **Point d'entrée unique :** ce microservice est le seul autorisé à appeler l'API externes FedaPay ; les autres services doivent passer par ses endpoints REST.

## Notes techniques

- Devise utilisée en sandbox FedaPay : **XOF** (le code `XAF`, devise officielle du Cameroun, n'est pas reconnu par leur environnement sandbox). À revérifier impérativement avant tout passage en production.
- La réponse de l'API FedaPay encapsule les données de transaction dans une clé `v1/transaction` ; ce détail est déjà pris en compte dans `FedapayProvider`.
