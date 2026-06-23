# Project Compass - Backend Architecture (Team of 12)

Cette architecture est optimisée pour votre équipe de 12 étudiants, répartis en binômes stratégiques.

## Répartition des Binômes

| Binôme | Microservices | Responsables | Responsabilités Clés |
| :--- | :--- | :--- | :--- |
| **Binôme 1** | `gateway-service` | **Derrick & Franckline** | Sécurité JWT, Routage, Discovery, Config. |
| **Binôme 2** | `identity-service` | **Darwin & Ericka** | Auth, Profils Étudiants/Clients, Vérification KYC. |
| **Binôme 3** | `catalog-service` | **Noel & Steve** | Gigs (paliers), Catégories, Recherche, Villes. |
| **Binôme 4** | `request-service` | **Prisca & Melista** | Appels d'offres clients, Propositions étudiants. |
| **Binôme 5** | `business-service` | **Elvira & Veronique** | Workflow Commandes (Orders), Délais, Livrables. |
| **Binôme 6** | `payment-service` | **Luc & Dilane** | Intégration Campay, Escrow, Portefeuille. |
| **Binôme 7** | `support-service` | **Derrick & Franckline** | Chat, Notifications, Dashboards Admin, Storage. |

*(Note: Derrick et Franckline assurent la Gateway et le Support Service pour garantir la cohérence technique transverse)*

## Infrastructure
- **Port Gateway :** 8080
- **Service Discovery :** Eureka
- **Base de données :** PostgreSQL (1 instance par service via Docker Compose)
- **Communication :** OpenFeign (Synchrone) / RabbitMQ (Asynchrone)
