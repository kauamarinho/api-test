package com.kauam.apitest.accounts;

import io.javalin.http.Context;

/**
 * Camada HTTP: lê a requisição (path param, body), chama o Service e decide o status da resposta.
 * Não contém regra de negócio.
 */
public class AccountsController {
    private final AccountService service;

    public AccountsController(AccountService service) {
        this.service = service;
    }

    // GET /accounts -> 200 com a lista em JSON.
    public void getAll(Context ctx) {
        ctx.json(service.getAll());
    }

    // GET /accounts/{id} -> 200 com a conta, ou 404 se não existir.
    public void getById(Context ctx) {
        Account conta = service.getById(Integer.parseInt(ctx.pathParam("id")));
        if (conta == null) ctx.status(404);
        else ctx.json(conta);
    }

    // POST /accounts -> converte o body JSON em Account; 201 se criou, 400 se o Service rejeitou os dados.
    public void create(Context ctx) {
        try {
            Account nova = ctx.bodyAsClass(Account.class);
            ctx.status(201).json(service.create(nova));
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    // PUT /accounts/{id} -> 200 com a conta atualizada, 404 se o id não existe, 400 se os dados são inválidos.
    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            Account atualizada = service.update(id, ctx.bodyAsClass(Account.class));
            if (atualizada == null) ctx.status(404);
            else ctx.json(atualizada);
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    // DELETE /accounts/{id} -> 200 com a conta removida, ou 404 se não existir.
    public void remove(Context ctx) {
        Account removida = service.remove(Integer.parseInt(ctx.pathParam("id")));
        if (removida == null) ctx.status(404);
        else ctx.json(removida);
    }
}
