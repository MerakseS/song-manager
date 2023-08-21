# Song Manager

Сервис для хранения файлов песен и подробной информации о песнях.

## Backend

### Стек технологий

- Java 17;
- Spring (Core, Boot, MVC, Data MongoDB, Security, OAuth2);
- Spring Cloud (Eureka, API Gateway, Config Server, OpenFeign, Resilience4J);
- AWS (S3, SQS);
- Apache Camel (HTTP, AWS SQS).

### Инструкция по сборке

- Сборка выполняется на Java 17.
- Выполнить команду `./gradlew build`.

## Frontend

### Стек технологий

- TypeScript;
- Angular 16, angular-oauth2-oidc;
- Bootstrap, HTML5, CSS;

### Инструкция по сборке

- Сборка выполняется на Node.js 18;
- Выполнить команду `npm install`;
- Выполнить команду `npm run build`.

## Инструкция по запуску

### Запуск через Docker compose

- Выполнить команду `docker-compose up`;
    - Переменные окружения можно изменить в файле `.env`
- Дождаться запуска всех сервисов.

### Запуск локально

1. Запустить ConfigServiceApplication;
2. Запустить DiscoveryServiceApplication;
3. Запустить AuthApiApplication:
    - Предварительно запустить MongoDB через docker-compose в папке `./auth-api` или локально;
        - Данные для подключения можно изменить с помощью переменных окружения `MONGODB_USER`, `MONGODB_PASSWORD`, `MONGODB_HOST`
          и `MONGODB_PORT`;
    - В переменных окружения указать `CLIENT_ID` и `CLIENT_SECRET`;
4. Запустить FileApiApplication:
    - Предварительно запустить Localstack и MongoDB через docker-compose в папке `./file-api` или локально;
        - Данные для подключения к AWS можно изменить с помощью переменных
          окружения `AWS_ENDPOINT`, `AWS_REGION`, `AWS_ACCESS_KEY` и `AWS_SECRET_KEY`;
        - Данные для подключения к MongoDB можно изменить с помощью переменных
          окружения `MONGODB_USER`, `MONGODB_PASSWORD`, `MONGODB_HOST` и `MONGODB_PORT`;
5. Запустить EnricherServiceApplication:
    - В переменных окружения указать `SPOTIFY_CLIENT_ID` и `SPOTIFY_CLIENT_SECRET` для получения токена Spotify;
6. Запустить SongApiApplication:
    - Предварительно запустить MongoDB через docker-compose в папке `./song-api` или локально;
        - Данные для подключения к MongoDB можно изменить с помощью переменных
          окружения `MONGODB_USER`, `MONGODB_PASSWORD`, `MONGODB_HOST` и `MONGODB_PORT`;
7. Запустить ApiGatewayApplication;
8. Запустить Frontend с помощью команды `npm run start`
    - В переменных окружения указать те же `CLIENT_ID` и `CLIENT_SECRET`, которые указывались в `auth-api`;