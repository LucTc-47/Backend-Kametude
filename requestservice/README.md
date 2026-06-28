
# 📋 Request Service — Kam'Etud

## Description
Microservice de gestion des appels d'offres et propositions 
de la plateforme Kam'Etud.

- **Équipe** : Melista (Chef de groupe) + Prisca (Binôme 4)
- **Port** : 8085
- **Base de données** : kametud_requests (PostgreSQL)

## Stack technique
- Java 21
- Spring Boot 3.4.1
- PostgreSQL
- Maven
- Lombok
- Spring Data JPA

## Endpoints principaux
| Méthode | URL | Description |
|---|---|---|
| POST | /api/v1/requests | Créer une demande |
| GET | /api/v1/requests | Lister les demandes |
| POST | /api/v1/proposals | Créer une proposition |
| PUT | /api/v1/proposals/{id}/accept | Accepter une proposition |
| PUT | /api/v1/proposals/{id}/reject | Rejeter une proposition |

## Lancer le service
```bash
mvn spring-boot:run
```

## Documentation complète
https://app.notion.com/p/Request-Service-Kam-Etud-38dad8ec5da480b4b754c01206039ab0

## Auteur
ASSONFACK Melista Elouanda — CM-UDS-23SCI0939  
Université de Dschang — L3 Informatique 2025/2026
