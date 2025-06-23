import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    private static Javalin app;
    private static HttpClient client;
    private static ObjectMapper objectMapper;
    private static String baseUrl;

    private static final String json = ("""
            {
                "id": "1",
                "nome": "Bruno",
                "curso": "eng software",
                "email": bruno@gmail.com"
            }"""
    );

    @BeforeAll
    public static void iniciarApp(){
        app.start(7000);
        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        baseUrl = "http://localhost:7000";
    }

    @AfterAll
    public static void pararApp(){
        app.stop();
    }
    @Test
    @Order(1)
    void testHello() throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/hello"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Hello, Javalin!", response.body());
    }

    @Test
    @Order(2)
    void testPostAluno() throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/alunos"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    @Order(3)
    void testGetById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/alunos/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
    @Test
    @Order(4)
    void testGetAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/alunos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
}
