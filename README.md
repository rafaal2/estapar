# Estapar Parking Backend

Mini‑backend (Spring Boot + MySQL) que gerencia setores, vagas, sessões de estacionamento e faturamento, consumindo o **garage simulator** da Estapar.

## Sumário rápido

| Recurso                | Descrição                                                         |
| ---------------------- | ----------------------------------------------------------------- |
| **Importar garagem**   | `POST /import-garage` – grava setores e vagas vindos do simulador |
| **Webhook de eventos** | `POST /webhook` – recebe `ENTRY`, `PARKED`, `EXIT`                |
| **Status por placa**   | `POST /plate-status` – último estado da placa                     |
| **Status da vaga**     | `POST /spot-status` – situação da vaga (lat/lng)                  |
| **Faturamento diário** | `GET /revenue?date=YYYY-MM-DD&sector=A`                           |

---

## Como rodar localmente

### Pré‑requisitos

- Java 17+
- Maven 3.9+
- MySQL 8 
- Docker Desktop (para o *garage‑sim*) e importação de garagem

### 1 — Subir o simulador

```bash
docker run -d -p 3003:3000 --name garage-sim \
  cfontes0estapar/garage-sim:1.0.0

curl http://localhost:3003/garage   # sanity‑check
```

### 2 — Criar o banco

```sql
CREATE DATABASE estapar
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

### 3 — Ajustar `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/estapar
spring.datasource.username=<usuario>
spring.datasource.password=<senha>
```

### 4 — Build & Run

```bash
./mvnw spring-boot:run
# ou:
# ./mvnw clean package && java -jar target/estapar-0.0.1-SNAPSHOT.jar
```

A API inicia em [**http://localhost:8080**](http://localhost:8080).\
Se usar *springdoc*, Swagger UI estará em **/swagger‑ui.html**.

---

## Fluxo de uso

1. **Importar configuração da garagem**

   ```http
   POST /import-garage
   Content-Type: application/json
   <payload retornado de /garage>
   ```

2. O simulador começa a enviar eventos para **POST /webhook**.

3. Consultas rápidas:

| Endpoint                            | Método | Body                               | Exemplo                                                                                                               |
| ----------------------------------- | ------ | ---------------------------------- | --------------------------------------------------------------------------------------------------------------------- |
| `/plate-status`                     | POST   | `{ "license_plate":"ZUL0001" }`    | `curl -X POST -H "Content-Type:application/json" -d '{"license_plate":"ZUL0001"}' http://localhost:8080/plate-status` |
| `/spot-status`                      | POST   | `{ "lat": -23.56, "lng": -46.65 }` | idem                                                                                                                  |
| `/revenue?date=2025-01-01&sector=A` | GET    | —                                  | `curl "http://localhost:8080/revenue?date=2025-01-01&sector=A"`                                                       |

### Exemplo de resposta `/plate-status`

```jsonc
{
  "license_plate": "ZUL0001",
  "price_until_now": 12.34,
  "entry_time": "2025-01-01T12:00",
  "time_parked": "01:15",
  "lat": -23.561684,
  "lng": -46.655981
}
```

---

## Regras de preço dinâmico

| Ocupação do setor | Ajuste no preço/hora |
| ----------------- | -------------------- |
| < 25 %            | –10 %                |
| 25 % – 50 %       | 0 % (base)           |
| 50 % – 75 %       | +10 %                |
| 75 % – 100 %      | +25 %                |

*Capacidade 100 % ⇒ novas entradas bloqueadas até alguém sair.*

---

## Tecnologias usadas

- Spring Boot 3
- Spring Data JPA + Hibernate
- MySQL 8
- Lombok

## extras
- crud para gerenciar as entidades criadas
