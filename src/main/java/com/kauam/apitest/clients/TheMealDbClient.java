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

public class TheMealDbClient {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nome da receita (em inglês, ex: Arrabiata): ");
        String nome = sc.nextLine().trim();

        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s="
                + URLEncoder.encode(nome, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Erro na requisição. Status: " + response.statusCode());
            return;
        }

        JsonNode meals = new ObjectMapper().readTree(response.body()).path("meals");
        if (!meals.isArray() || meals.isEmpty()) {
            System.out.println("Nenhuma receita encontrada.");
            return;
        }

        JsonNode meal = meals.get(0);
        System.out.println("\n=== " + meal.path("strMeal").asText() + " ===");
        System.out.println("Categoria: " + meal.path("strCategory").asText("N/D"));
        System.out.println("Origem: " + meal.path("strArea").asText("N/D"));
        System.out.println("Tags: " + meal.path("strTags").asText("N/D"));
        System.out.println("Imagem: " + meal.path("strMealThumb").asText("N/D"));
        System.out.println("Vídeo: " + meal.path("strYoutube").asText("N/D"));

        System.out.println("\nIngredientes:");
        for (int i = 1; i <= 20; i++) {
            String ing = meal.path("strIngredient" + i).asText("").trim();
            if (ing.isEmpty()) continue;
            System.out.println(" - " + ing + " (" + meal.path("strMeasure" + i).asText("").trim() + ")");
        }

        System.out.println("\nModo de preparo:\n" + meal.path("strInstructions").asText("N/D"));
    }
}
