#  API Gateway (Derrick & Franckline)

##  Configuration du Routage (application.yml)

L'API Gateway doit rediriger les appels vers les bons ports :

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: identity-service
          uri: lb://identity-service
          predicates:
            - Path=/api/auth/**, /api/users/**
        - id: catalog-service
          uri: lb://catalog-service
          predicates:
            - Path=/api/gigs/**, /api/categories/**
        - id: request-service
          uri: lb://request-service
          predicates:
            - Path=/api/requests/**, /api/proposals/**
        - id: business-service
          uri: lb://business-service
          predicates:
            - Path=/api/orders/**, /api/reviews/**
        - id: payment-service
          uri: lb://payment-service
          predicates:
            - Path=/api/payments/**
        - id: support-service
          uri: lb://support-service
          predicates:
            - Path=/api/chat/**, /api/notifications/**
```

---

##  Début du projet (Guide)
1. **Discovery :** Connecter la Gateway à Eureka.
2. **CORS :** Autoriser `http://localhost:5173` (le port par défaut de Vite/React).
3. **Filtre JWT :** Créer un filtre qui vérifie le jeton avant de passer la requête aux autres services.
