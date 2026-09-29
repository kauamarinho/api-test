package com.kauam.apitest.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GoogleBooksClient {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Título do livro: ");
        String titulo = sc.nextLine().trim();

        String url = "https://www.googleapis.com/books/v1/volumes?maxResults=1&q=intitle:"
                + URLEncoder.encode(titulo, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Erro na requisição. Status: " + response.statusCode());
            return;
        }

        JsonNode items = new ObjectMapper().readTree(response.body()).path("items");
        if (items.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        JsonNode info = items.get(0).path("volumeInfo");
        System.out.println("\n=== Informações do livro ===");
        System.out.println("Título: " + info.path("title").asText("N/D"));
        if (info.has("subtitle")) System.out.println("Subtítulo: " + info.get("subtitle").asText());
        System.out.println("Autores: " + join(info.path("authors")));
        System.out.println("Editora: " + info.path("publisher").asText("N/D"));
        System.out.println("Data de publicação: " + info.path("publishedDate").asText("N/D"));
        System.out.println("Páginas: " + info.path("pageCount").asText("N/D"));
        System.out.println("Categorias: " + join(info.path("categories")));
        System.out.println("Idioma: " + info.path("language").asText("N/D"));
        System.out.println("Nota média: " + info.path("averageRating").asText("N/D"));
        System.out.println("Descrição: " + info.path("description").asText("N/D"));
        System.out.println("Link: " + info.path("infoLink").asText("N/D"));
    }

    private static String join(JsonNode array) {
        if (!array.isArray() || array.isEmpty()) return "N/D";
        StringBuilder sb = new StringBuilder();
        for (JsonNode n : array) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(n.asText());
        }
        return sb.toString();
    }
}
