# Informační systém pro evidenci hudebníků a vybavení

## Přehled projektu
Tato aplikace slouží jako komplexní informační systém určený pro evidenci hudebníků, správu jejich hudebního vybavení a organizaci jejich působení v různých hudebních uskupeních.

### Hlavní cíle
* **Centralizace dat:** Jednotné místo pro správu informací o členech a kapelách.
* **Evidence vybavení:** Přehled o tom, jakými nástroji a technikou jednotliví členové disponují.
* **Organizační přehled:** Snadné zjištění, v jakých formacích a seskupeních hudebníci aktuálně působí.

---

## Použité technologie

### Databáze
* **MariaDB**: Relační databáze pro bezpečné a strukturované ukládání dat.

### Backend (Serverová část)
* **Java**: Hlavní programovací jazyk pro robustní logiku aplikace.
* **Spring Boot**: Framework pro rychlý vývoj backendových služeb.
* **Spring Security**: Zajištění bezpečnosti a autorizace přístupu.
* **Spring Data JPA / Hibernate**: Mapování objektů na databázové tabulky (ORM).
* **JWT (JSON Web Tokens)**: Bezpečný přenos informací mezi klientem a serverem v rámci autentizace.

### Frontend (Klientská část)
* **React**: Knihovna pro tvorbu moderního uživatelského rozhraní.
* **TypeScript**: Typová kontrola pro zvýšení stability a čitelnosti kódu.
* **React Router**: Správa navigace v rámci jednostránkové aplikace (SPA).
* **Axios**: Knihovna pro asynchronní HTTP požadavky na backend.
* **Vite**: Moderní a rychlý nástroj pro sestavení (build) frontendu.
* **Čisté CSS (CSS Variables)**: Stylování pomocí nativních CSS proměnných pro snadnou údržbu a konzistenci.
* **Lokální správa stavu (React Hooks)**: Efektivní řízení dat přímo v komponentách.

### Infrastruktura & Deployment
* **Docker & Docker Compose**: Kontejnerizace aplikace pro snadné nasazení a konzistentní běh v různých prostředích.
* **Nginx**: Výkonný webový server a reverzní proxy pro směrování provozu.
