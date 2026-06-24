# Support & Interaction Service — Guide complet

# 1️⃣ Présentation

Le support-service gère 3 fonctionnalités indépendantes :

- Le chat en temps réel entre client et étudiant sur une commande
- Les notifications (alertes utilisateur)
- Le stockage de fichiers (CV, livrables, photos)

Port d'écoute : 8086
Base de données : PostgreSQL (support_db, port 5436)

<aside>
⚠️

La sécurité JWT n'est **pas** encore implémentée. N'importe qui peut actuellement se faire passer pour un autre utilisateur. À corriger dès que identity-service fournit un secret JWT commun.

</aside>

# 2️⃣ Prérequis

- Java 21 installé
- Maven (fourni via le wrapper `./mvnw`, pas d'installation séparée nécessaire)
- Docker Desktop installé et lancé
- Un IDE (VS Code avec Extension Pack for Java + Spring Boot Extension Pack, ou IntelliJ)

# 3️⃣ Créer le projet depuis zéro

Si vous devez recréer ce service depuis Spring Initializr ([https://start.spring.io](https://start.spring.io)) :

| Champ | Valeur |
| --- | --- |
| Project | Maven |
| Language | Java |
| Spring Boot | 3.3.x (dernière version stable) |
| Group | com.kametude |
| Artifact | support-service |
| Packaging | Jar |
| Java | 21 |

**Dependencies à ajouter :**

- Spring Web
- WebSocket
- Spring Data JPA
- PostgreSQL Driver
- Validation
- Eureka Discovery Client
- Lombok

<aside>
💡

Le nom du package généré sera `com.kametude.support_service` (avec underscore) — c'est normal, Spring Initializr convertit automatiquement les tirets de l'Artifact ID.

</aside>

# 4️⃣ Configuration

Renommer `application.properties` en `application.yml` dans `src/main/resources`, puis y mettre :

```yaml
spring:
  application:
    name: support-service
  datasource:
    url: jdbc:postgresql://localhost:5436/support_db
    username: user
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8086

eureka:
  client:
    enabled: false

app:
  storage:
    location: storage
```

<aside>
⚠️

`eureka.client.enabled: false` est temporaire — à retirer / passer à `true` dès qu'un serveur Eureka existe dans l'équipe, sinon ce service restera invisible pour l'api-gateway.

</aside>

# 5️⃣ Lancer la base de données

Depuis la racine du repo Backend-Kametude (là où se trouve `docker-compose.yml`) :

```bash
docker compose up -d support-db
```

Vérifier que ça tourne :

```bash
docker ps
```

Vous devez voir une ligne `support-db` avec le port `5436->5432` et le statut "Up".

# 6️⃣ Lancer l'application

Depuis le dossier `support-service` :

```bash
./mvnw spring-boot:run
```

Si tout fonctionne, le terminal affiche en dernière ligne :

```
Started SupportServiceApplication in X seconds
```

<aside>
🪟

Sur Windows, `curl` est un alias PowerShell qui casse la syntaxe classique. Utilisez toujours `curl.exe` (pas juste `curl`) pour tester les API en ligne de commande.

</aside>

# 7️⃣ Architecture du code

Le projet suit le pattern en couches classique de Spring Boot :

| Couche | Rôle | Dossier |
| --- | --- | --- |
| Entity | Représente une table SQL | `entity/` |
| Repository | Accès aux données (SQL généré automatiquement) | `repository/` |
| Service | Logique métier | `service/` |
| Controller | Expose les endpoints HTTP/WebSocket | `controller/` |
| DTO | Objets d'échange pour les requêtes (pas les entités directement) | `dto/` |
| Config | Configuration technique (WebSocket, etc.) | `config/` |

Flux d'une requête typique :
Client → Controller → Service → Repository → Base de données → retour JSON

# 8️⃣ API — Notifications

- POST /api/notifications — Créer une notification
    
    ```json
    {
      "userId": "uuid-de-l-utilisateur",
      "message": "Ta commande a ete mise a jour",
      "type": "ORDER_UPDATE"
    }
    ```
    
    `type` accepte uniquement : `ORDER_UPDATE`, `NEW_MESSAGE`, `PAYMENT_CONFIRMED`
    
    Exemple curl (Windows PowerShell) :
    
    ```bash
    curl.exe -X POST http://localhost:8086/api/notifications -H "Content-Type: application/json" --data "@notification.json"
    ```
    
- GET /api/notifications/users/{userId} — Liste des notifications
    
    ```
    GET http://localhost:8086/api/notifications/users/3fa85f64-5717-4562-b3fc-2c963f66afa6
    ```
    
    Réponse exemple :
    
    ```json
    [
      {
        "id": "c886ad4d-d02b-403c-829b-f138f4d8bb0f",
        "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "message": "Ta commande a ete mise a jour",
        "type": "ORDER_UPDATE",
        "createdAt": "2026-06-23T...",
        "read": false
      }
    ]
    ```
    
- PATCH /api/notifications/{notificationId}/read — Marquer comme lue

# 9️⃣ API — Chat

<aside>
🔌

Le chat fonctionne différemment du REST classique : le client ouvre une connexion persistante, s'abonne à un "topic", et reçoit les messages en temps réel sans avoir à redemander.

</aside>

## Connexion

```
ws://localhost:8086/ws  (via SockJS)
```

## Envoyer un message

| Élément | Valeur |
| --- | --- |
| Destination STOMP | /app/chat.send |

```json
{
  "orderId": "uuid-de-la-commande",
  "senderId": "uuid-de-l-expediteur",
  "content": "Bonjour !"
}
```

## Recevoir les messages en temps réel

S'abonner à : `/topic/order.{orderId}`
Chaque message envoyé sur cette commande sera reçu automatiquement par tous les abonnés.

## Historique (REST classique)

```
GET /api/chat/orders/{orderId}/messages
```

# 🔟 API — Storage

- POST /api/storage/upload — Upload d'un fichier
    
    ```bash
    curl.exe -X POST http://localhost:8086/api/storage/upload -F "file=@monfichier.pdf"
    ```
    
    Réponse :
    
    ```json
    {
      "filename": "24069ab4-bc17-48da-8a1a-18785bb05962.pdf",
      "downloadUrl": "/api/storage/files/24069ab4-bc17-48da-8a1a-18785bb05962.pdf"
    }
    ```
    
    <aside>
    💡
    
    Le fichier est renommé avec un UUID pour éviter les collisions de noms. C'est le `downloadUrl` retourné qu'il faut conserver pour récupérer le fichier plus tard.
    
    </aside>
    
- GET /api/storage/files/{filename} — Télécharger un fichier

# 🧪 Comment tester soi-même

**Pour le REST (notifications, storage) :**
Utiliser `curl.exe` (PowerShell) ou Postman.

**Pour le WebSocket (chat) :**
`curl` ne fonctionne pas bien avec WebSocket. Utiliser une page HTML de test avec SockJS + STOMP.js (voir code complet en annexe ci-dessous), ou un outil dédié comme Postman (qui supporte aussi STOMP).

- Code de la page de test HTML (chat)
    
    Colle ici le code complet du `test-chat.html` qu'on a utilisé.
    

# ⚠️ Problèmes rencontrés et solutions

| Problème | Cause | Solution |
| --- | --- | --- |
| `curl` renvoie des erreurs "Could not resolve host" | PowerShell interprète `curl` comme `Invoke-WebRequest`, casse les guillemets | Utiliser `curl.exe` explicitement |
| `--data "@fichier"` échoue | Mauvais dossier courant dans le terminal | Vérifier avec `pwd` avant de lancer la commande |
| Erreur "Syntax error on token }" | Méthode collée en dehors des accolades de la classe | Toujours vérifier que le code est entre `{ }` de la classe |
| Eureka spam des erreurs en boucle au démarrage | Pas de serveur Eureka disponible encore | `eureka.client.enabled: false` dans `application.yml` |
| Unknown property 'app' (warning VS Code) | Propriété personnalisée non reconnue par l'extension | Pas une erreur réelle, ignorer ce warning |

# 🧭 Ce qu'il reste à faire

- [ ]  Implémenter la vérification JWT (extraction userId depuis le token)
- [ ]  Sécuriser ou retirer le POST /api/notifications manuel (passer par RabbitMQ)
- [ ]  Migrer le stockage local vers MinIO/S3
- [ ]  Réactiver Eureka une fois disponible dans l'équipe