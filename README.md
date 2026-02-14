# SWI1_vlasak_vjacka
Projekt do SWI1

Požadavky pro spuštění
Před prvním spuštěním se ujisti, že máš nainstalováno:

Java JDK 21

Node.js (verze LTS)

MariaDB Server

IntelliJ IDEA 

Jak zprovoznit projekt
1. Databáze (MariaDB)
   Spusť MariaDB server.

Vytvoř novou prázdnou databázi:

SQL
CREATE DATABASE music_shop_db;
heslo k root = musicshop
backend/src/main/resources/application.properties

2. Backend (Spring Boot)
   V IntelliJ otevři složku backend.

Počkej, až Maven stáhne všechny závislosti (uvidíš v pravém dolním rohu).

Spusť aplikaci pomocí třídy BackendApplication.

Backend poběží na: http://localhost:8080

3. Frontend (React)
   Otevři terminál v adresáři frontend.

Nainstaluj potřebné knihovny (udělej jen poprvé):

Bash
npm install
Spusť frontend:

Bash
npm start
Aplikace se otevře na: http://localhost:3000

📂 Struktura projektu
/backend - Java Spring Boot API, JPA, MariaDB.

/frontend - React.js UI, komunikace přes REST API.