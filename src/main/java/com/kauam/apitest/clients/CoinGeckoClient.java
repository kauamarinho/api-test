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

public class CoinGeckoClient {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID da criptomoeda (ex: bitcoin, ethereum, solana): ");
        String id = sc.nextLine().trim().toLowerCase();

        String url = "https://api.coingecko.com/api/v3/simple/price?ids="
                + URLEncoder.encode(id, StandardCharsets.UTF_8)
                + "&vs_currencies=usd,brl&include_24hr_change=true";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Erro na requisição. Status: " + response.statusCode());
            return;
        }

        JsonNode coin = new ObjectMapper().readTree(response.body()).path(id);
        if (coin.isMissingNode()) {
            System.out.println("Criptomoeda não encontrada: " + id);
            return;
        }

        System.out.println("\n=== Cotação de " + id + " ===");
        System.out.printf("USD: $ %,.2f%n", coin.path("usd").asDouble());
        System.out.printf("BRL: R$ %,.2f%n", coin.path("brl").asDouble());
        System.out.printf("Variação 24h (USD): %.2f%%%n", coin.path("usd_24h_change").asDouble());
    }
}
