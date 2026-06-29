## Description profonde 
 **guide de démarrage**, le **workflow Git sans conflit**, et surtout une **répartition chirurgicale des tâches** 

```markdown
# 📦 Kam'Etud — Catalog Service (Microservice Gigs)

Ce microservice gère le catalogue de prestations de services (Gigs) proposés par les étudiants. Il s'exécute de manière autonome sur le port **8083**, possède sa propre base de données PostgreSQL, et expose des endpoints REST consommés par la Gateway.

---

## 🛠️ Fiche Technique & Technologies

- **Langage & Framework :** Java 21, Spring Boot 3.4.0
- **Base de Données :** PostgreSQL 16 (Base locale : `catalog_db`)
- **Port de l'application :** `8083`
- **Outil de Build :** Maven 3.x
- **ORM :** Spring Data JPA (Hibernate 6)

---

## 📁 Architecture des Packages

Pour que notre code soit propre, nous respectons l'architecture en couches suivante :

```text
src/main/java/com/example/kametud_catalog/
├── client/      # Appels REST vers d'autres microservices (ex: Identity Service)
├── config/      # Configurations globales de l'application (CORS, Jackson, etc.)
├── controller/  # Les points d'entrée de l'API (Endpoints REST avec @RestController)
├── dto/         # Objets de transfert de données (Requêtes entrantes et Réponses sortantes)
├── entity/      # Entités JPA représentant les tables de notre base de données PostgreSQL
├── repository/  # Interfaces d'accès à la base de données (Spring Data JPA)
└── service/     # La logique métier (Là où s'appliquent nos règles de gestion)
```

---

## 💾 Schéma de la Table `gigs` (PostgreSQL)

Les trois paliers (Tiers) sont stockés sous forme d'objets `JSONB` directement dans la table principale pour simplifier le code et accélérer les requêtes de lecture du Frontend.

```sql
CREATE TABLE gigs (
    id UUID PRIMARY KEY,
    student_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    rating NUMERIC(3, 2) DEFAULT 0.0,
    tier_basique JSONB NOT NULL,
    tier_standard JSONB NOT NULL,
    tier_premium JSONB NOT NULL,
    published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_gigs_category ON gigs(category);
CREATE INDEX idx_gigs_location ON gigs(location);
CREATE INDEX idx_gigs_student ON gigs(student_id);
```

---

## 👥 Répartition des Tâches (Feuille de Route)

Pour avancer vite et éviter les conflits, nous avons séparé le travail de manière logique. Steve s'occupe de la base de données et du moteur interne (Service/Repository), et Noel s'occupe de l'exposition API (Controller/DTO/Validation) et de la communication avec l'extérieur.

### 💻 Steve : Moteur Interne, Base de données & Recherche
Ta mission est de t'assurer que les données sont bien structurées, enregistrées et que les requêtes de recherche sont ultra-rapides.

- [ ] **Tâche S1 : Entité de données**
  - Créer la classe `Gig.java` dans le package `entity/`.
  - Configurer les annotations JPA (`@Entity`, `@Table`, `@Id`, etc.).
  - Utiliser `@JdbcTypeCode(SqlTypes.JSON)` pour mapper les trois colonnes JSONB (`tierBasique`, `tierStandard`, `tierPremium`).
- [ ] **Tâche S2 : Accès aux données (Repository)**
  - Créer l'interface `GigRepository.java` dans le package `repository/`.
  - Écrire les méthodes de recherche personnalisées, par exemple :
    `List<Gig> findByCategoryAndLocationAndPublishedTrue(String category, String location);`
- [ ] **Tâche S3 : Logique Métier (Service)**
  - Créer la classe `GigService.java` dans le package `service/`.
  - Implémenter les méthodes de création d'un Gig, de recherche, et de récupération d'un Gig par son ID.

---

### 💻 Noel : API, DTOs, Validation & Communication Externe
Ta mission est d'exposer nos fonctionnalités proprement au monde extérieur et de vérifier que tout ce qui entre dans notre application est valide et sécurisé.

- [ ] **Tâche N1 : Objets d'Échange (DTOs)**
  - Créer la classe `GigTierDto.java` dans `dto/` (structure d'un palier : titre, description, prix, délais).
  - Créer `GigCreateRequest.java` (ce que le client envoie pour créer un service).
  - Créer `GigResponse.java` (ce que nous renvoyons au format JSON, propre et sans données sensibles).
- [ ] **Tâche N2 : Points d'Entrée API (Controller)**
  - Créer `GigController.java` dans le package `controller/`.
  - Exposer les routes :
    - `POST /api/gigs` (Créer un nouveau service)
    - `GET /api/gigs` (Rechercher des services par catégorie et/ou localisation)
    - `GET /api/gigs/{id}` (Récupérer un service précis par son identifiant)
- [ ] **Tâche N3 : Sécurité & Validation**
  - Configurer la validation sur les requêtes entrantes (ex: prix toujours positif, titre obligatoire non vide).
- [ ] **Tâche N4 : Appel Inter-Service (Client)**
  - Préparer l'appel REST vers le microservice **Identity Service** pour vérifier si un étudiant est banni ou non-vérifié avant de valider la publication de son Gig.

---

## 🔄 Workflow Git & GitHub (Zéro Conflit)

Pour travailler sereinement sans jamais casser le code de l'autre, suivez scrupuleusement ces 5 étapes :

### 1. Synchroniser sa machine locale (Chaque matin)
On commence toujours sa journée en récupérant le travail propre de la branche principale.
```bash
git checkout main
git pull origin main
```

### 2. Travailler sur sa propre branche
On ne code **jamais** directement sur la branche `main`.
*   Steve travaille sur : `git checkout -b feature/steve-backend-core`
*   Noel travaille sur : `git checkout -b feature/noel-api-endpoints`

### 3. Commiter avec style et rigueur
On utilise la convention des *Conventional Commits* pour que notre historique de projet soit digne d'un niveau pro :
```bash
git add .
git commit -m "feat: creation de l'entite de base Gig"
```
*Prefixes autorisés :*
- `feat:` (Nouvelle fonctionnalité)
- `fix:` (Correction de bug)
- `chore:` (Changement de config, pom.xml, ou README)

### 4. Mettre à jour sa branche avant de l'envoyer
Avant d'envoyer ton travail sur GitHub, intègre les nouveautés que ton binôme a pu ajouter sur `main` entre-temps :
```bash
git fetch origin
git merge origin/main
```
*(S'il y a des conflits de code, résolvez-les ensemble devant le même écran, puis validez le commit de merge).*

### 5. Envoyer sur GitHub et demander une Pull Request (PR)
Une fois le code prêt et testé localement :
```bash
git push origin TA_BRANCHE_ACTUELLE
```
Allez sur GitHub, ouvrez une **Pull Request** (PR), et désignez votre binôme comme **Reviewer**. Une fois la relecture validée, fusionnez dans `main`.

---

## 🚀 Démarrage Rapide

1. Assurez-vous d'avoir créé la base locale `catalog_db` dans PostgreSQL.
2. Configurez vos accès dans `src/main/resources/application.yaml`.
3. Lancez la compilation :
   ```bash
   mvn clean compile
   ```
4. Démarrez l'application depuis votre IDE. Le service écoute sur le port **`8083`**.
```
```
