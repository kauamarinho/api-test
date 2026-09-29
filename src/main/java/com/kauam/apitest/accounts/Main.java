package com.kauam.apitest.accounts;

/**
 * Ponto de entrada da API.
 * Fluxo geral de uma requisição:
 * cliente HTTP -> Javalin (rota em MyApp) -> AccountsController -> AccountService -> AccountDao -> resposta JSON.
 */
public class Main {
    public static void main(String[] args) {
        // Monta o app (rotas + dependências) e só aqui abre a porta 8080.
        MyApp.configurarJavalin().start(8080);
    }
}
