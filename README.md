# rest

Учебный Maven-проект для работы с `RestTemplate` и REST API.

## Что делает программа

За один запуск программа последовательно:

1. Выполняет `GET http://94.198.50.185:7081/api/users`.
2. Берёт `JSESSIONID` из заголовка ответа `Set-Cookie`.
3. Выполняет `POST` и создаёт пользователя:
   - `id = 3`
   - `name = James`
   - `lastName = Brown`
   - `age = 30`
4. Выполняет `PUT` и меняет пользователя на:
   - `id = 3`
   - `name = Thomas`
   - `lastName = Shelby`
   - `age = 30`
5. Выполняет `DELETE /api/users/3`.
6. Склеивает части кода из ответов POST, PUT и DELETE.

Все POST/PUT/DELETE-запросы отправляются с той же cookie, которую сервер выдал после первого GET-запроса.

## Требования

- JDK 17+
- Maven

## Запуск в IntelliJ IDEA

1. Открыть проект как Maven-проект.
2. Дождаться загрузки зависимостей.
3. Открыть `src/main/java/org/example/Main.java`.
4. Запустить метод `main()`.

В конце в консоли появится:

```text
FINAL CODE: ...
CODE LENGTH: 18
```

Именно `FINAL CODE` нужно отправить на проверку.
