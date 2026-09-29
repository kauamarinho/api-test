package com.kauam.apitest.accounts;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Cliente da própria API: monta a requisição (HttpRequest), envia (HttpClient)
 * e converte o JSON da resposta (HttpResponse) em Account com Gson.
 */

public class AccountsApiClient {
    private final String url;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public AccountsApiClient(String url) {
        this.url = url;
    }

    // GET /accounts/{id}: sem body; o JSON de resposta vira Account.
    public Account getById(int id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url + "/accounts/" + id)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(res.body(), Account.class);
    }

    // POST /accounts: Account -> JSON no body, com Content-Type para o Javalin conseguir ler.
    public Account create(Account nova) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/accounts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(nova)))
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(res.body(), Account.class);
    }

    // Demonstração: exige o servidor (Main) rodando. Cria uma conta e a busca de volta pelo id gerado.
    public static void main(String[] args) throws Exception {
        AccountsApiClient api = new AccountsApiClient("http://localhost:8080");
        Account criada = api.create(new Account(0, "João", "123", 100.0));
        System.out.println("Criada: id=" + criada.getId() + ", nome=" + criada.getNome());
        System.out.println("Buscada: " + api.getById(criada.getId()).getNome());
    }
}
