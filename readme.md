# Song Manager
API для хранения файлов песен и подробной информации о песнях.

## Стек технологий
- Java 17;
- Spring (Core, Boot, MVC, Data MongoDB, Security, OAuth2);
- Spring Cloud (Eureka, API Gateway, Config Server, OpenFeign, Resilience4J);
- AWS (S3, SQS);
- Apache Camel (HTTP, AWS SQS).

## Инструкция по сборке
- Клонировать репозиторий на свой компьютер.
- Сборка выполняется на Java 17.
- Выполнить команду `./gradlew build`.

## Инструкция по запуску

### Запуск через Docker compose
- Выполнить комманду `docker-compose up`.
- Дождаться запуска всех сервисов.
