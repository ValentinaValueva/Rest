package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Main {

    private static final String USERS_URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. GET: получаем список пользователей и cookie с идентификатором сессии.
        ResponseEntity<String> getResponse =
                restTemplate.getForEntity(USERS_URL, String.class);

        System.out.println("GET status: " + getResponse.getStatusCode());
        System.out.println("Users: " + getResponse.getBody());

        String setCookie = getResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (setCookie == null || setCookie.isBlank()) {
            throw new IllegalStateException("Сервер не вернул заголовок Set-Cookie");
        }

        // Сервер может вернуть, например:
        // JSESSIONID=ABC123; Path=/; HttpOnly
        // В следующие запросы передаём только JSESSIONID=ABC123.
        String sessionCookie = setCookie.split(";", 2)[0];

        System.out.println("Session cookie: " + sessionCookie);

        // Заголовки для всех следующих запросов.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionCookie);

        // 2. POST: создаём пользователя id=3, James Brown.
        User jamesBrown = new User(3L, "James", "Brown", (byte) 30);

        HttpEntity<User> postRequest = new HttpEntity<>(jamesBrown, headers);
        ResponseEntity<String> postResponse = restTemplate.exchange(
                USERS_URL,
                HttpMethod.POST,
                postRequest,
                String.class
        );

        String part1 = safeBody(postResponse);
        System.out.println("POST status: " + postResponse.getStatusCode());
        System.out.println("Part 1: " + part1);

        // 3. PUT: изменяем того же пользователя на Thomas Shelby.
        User thomasShelby = new User(3L, "Thomas", "Shelby", (byte) 30);

        HttpEntity<User> putRequest = new HttpEntity<>(thomasShelby, headers);
        ResponseEntity<String> putResponse = restTemplate.exchange(
                USERS_URL,
                HttpMethod.PUT,
                putRequest,
                String.class
        );

        String part2 = safeBody(putResponse);
        System.out.println("PUT status: " + putResponse.getStatusCode());
        System.out.println("Part 2: " + part2);

        // 4. DELETE: удаляем пользователя id=3.
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                USERS_URL + "/3",
                HttpMethod.DELETE,
                deleteRequest,
                String.class
        );

        String part3 = safeBody(deleteResponse);
        System.out.println("DELETE status: " + deleteResponse.getStatusCode());
        System.out.println("Part 3: " + part3);

        // 5. Склеиваем три части кода.
        String result = part1 + part2 + part3;

        System.out.println();
        System.out.println("============================");
        System.out.println("FINAL CODE: " + result);
        System.out.println("CODE LENGTH: " + result.length());
        System.out.println("============================");

        if (result.length() != 18) {
            System.out.println("Внимание: по условию итоговый код должен содержать 18 символов.");
        }
    }

    private static String safeBody(ResponseEntity<String> response) {
        return response.getBody() == null ? "" : response.getBody().trim();
    }
}
