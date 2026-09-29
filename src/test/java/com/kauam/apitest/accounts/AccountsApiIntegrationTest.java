package com.kauam.apitest.accounts;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountsApiIntegrationTest {
    private static final String JOAO = "{\"nome\":\"João\",\"numero\":\"123\",\"saldo\":100.0}";

    @Test
    public void deve_retornar_lista_vazia_no_GET_accounts() {
        Javalin app = MyApp.configurarJavalin();

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/accounts");

            assertEquals(200, response.code());
            assertEquals("[]", response.body().string());
        });
    }

    @Test
    public void deve_criar_e_buscar_conta() {
        Javalin app = MyApp.configurarJavalin();

        JavalinTest.test(app, (server, client) -> {
            assertEquals(201, client.post("/accounts", JOAO).code());

            var response = client.get("/accounts/1");
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("João"));
        });
    }

    @Test
    public void deve_retornar_pagina_html_no_GET_raiz() {
        Javalin app = MyApp.configurarJavalin();

        JavalinTest.test(app, (server, client) -> {
            client.post("/accounts", JOAO);

            var response = client.get("/");

            assertEquals(200, response.code());
            String body = response.body().string();
            assertTrue(body.contains("API de Contas"));
            assertTrue(body.contains("João"));
        });
    }

    @Test
    public void deve_retornar_404_para_conta_inexistente() {
        Javalin app = MyApp.configurarJavalin();

        JavalinTest.test(app, (server, client) ->
                assertEquals(404, client.get("/accounts/999").code()));
    }

    @Test
    public void deve_retornar_400_para_saldo_negativo() {
        Javalin app = MyApp.configurarJavalin();

        JavalinTest.test(app, (server, client) ->
                assertEquals(400, client.post("/accounts",
                        "{\"nome\":\"João\",\"numero\":\"1\",\"saldo\":-5}").code()));
    }

    @Test
    public void deve_atualizar_e_remover_conta() {
        Javalin app = MyApp.configurarJavalin();

        JavalinTest.test(app, (server, client) -> {
            client.post("/accounts", JOAO);

            assertEquals(200, client.put("/accounts/1",
                    "{\"nome\":\"Maria\",\"numero\":\"123\",\"saldo\":50.0}").code());
            assertEquals(200, client.delete("/accounts/1").code());
            assertEquals(404, client.get("/accounts/1").code());
        });
    }
}
