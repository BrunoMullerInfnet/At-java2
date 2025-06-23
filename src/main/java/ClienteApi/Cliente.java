package ClienteApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Cliente {

    private static final String baseUrl = "http://localhost:7000";

    public static void main(String[] args) {
        try {
            getStatus();

            String novoAlunoJson = "{\"id\":\"1\",\"nome\":\"Bruno\",\"curso\":\"eng software\",\"email\":\"bruno@email.com\"}";
            Post(novoAlunoJson);

            listAll();

            getById("1");

        } catch (Exception e) {
            System.out.println(e);;
        }
    }

    public static void Post(String alunoJson) throws Exception {
        URL url = new URI(baseUrl + "/alunos").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = alunoJson.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        System.out.println("Status: " + connection.getResponseCode());
        readResponse(connection);
    }

    public static void listAll() throws Exception {
        URL url = new URI(baseUrl + "/alunos").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        System.out.println("Status: " + connection.getResponseCode());
        readResponse(connection);
    }

    public static void getById(String id) throws Exception {
        URL url = new URI(baseUrl + "/alunos/" + id).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        System.out.println("Status: " + connection.getResponseCode());
        readResponse(connection);
    }

    public static void getStatus() throws Exception {
        URL url = new URI(baseUrl + "/status").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        System.out.println("Status: " + connection.getResponseCode());
        readResponse(connection);
    }
    private static void readResponse(HttpURLConnection connection) throws Exception {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
        } catch (Exception e) {
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    response.append(line.trim());
                }
            }
        }
        System.out.println("Resposta: " + response.toString());
    }
}