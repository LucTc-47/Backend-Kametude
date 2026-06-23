#  Support & Interaction Service (Derrick & Franckline)

##  Description
C'est le **centre de communication et de ressources**. Il gère les échanges en temps réel (Chat), les alertes (Notifications) et le stockage de tous les fichiers (Photos de Gigs, CV, Livrables). Il rend la plateforme vivante et interactive.

##  Schéma de Base de Données (PostgreSQL ou NoSQL)

### Table `chat_messages`
- `id`: UUID
- `order_id`: UUID (Pour lier la discussion à une mission)
- `sender_id`: UUID
- `content`: Text
- `timestamp`: Timestamp

### Table `notifications`
- `id`: UUID
- `user_id`: UUID (Destinataire)
- `message`: String
- `type`: Enum (ORDER_UPDATE, NEW_MESSAGE, PAYMENT_CONFIRMED)
- `is_read`: Boolean

---

##  Instructions pour bien avancer
1. **WebSockets (STOMP) :** Utilisez Spring WebSocket pour que les messages s'affichent instantanément sans rafraîchir la page.
2. **Stockage (MinIO ou Local) :** Créez un service `StorageService` capable de sauvegarder des fichiers. Au début, sauvegardez-les dans un dossier local du serveur, puis passez à S3/MinIO plus tard.
3. **Async :** L'envoi de notifications ne doit jamais ralentir le reste du site. Utilisez `@Async` de Spring.

##  Checklist de démarrage
- [ ] Configurer les WebSockets dans Spring Boot.
- [ ] Implémenter l'envoi et la réception de messages de chat.
- [ ] Créer l'endpoint d'Upload de fichiers (`/api/storage/upload`).
- [ ] Mettre en place le système de notifications (Stockage + Endpoint de lecture).
