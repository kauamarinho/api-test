package com.kauam.apitest.accounts;

import io.javalin.http.ContentType;
import io.javalin.http.Context;

import java.util.List;

/**
 * Página inicial (GET /): HTML simples com a lista de endpoints e as contas cadastradas no momento.
 */
public class HomeController {
    private final AccountService service;

    public HomeController(AccountService service) {
        this.service = service;
    }

    public void home(Context ctx) {
        StringBuilder linhas = new StringBuilder();
        List<Account> contas = service.getAll();
        for (Account c : contas) {
            linhas.append("<tr><td>").append(c.getId())
                    .append("</td><td>").append(escapar(c.getNome()))
                    .append("</td><td>").append(escapar(c.getNumero()))
                    .append("</td><td>").append(String.format("%.2f", c.getSaldo()))
                    .append("</td></tr>");
        }
        if (contas.isEmpty()) {
            linhas.append("<tr><td colspan=\"4\">Nenhuma conta cadastrada. Faça um POST em /accounts.</td></tr>");
        }

        String html = """
                <!doctype html>
                <html lang="pt-BR">
                <head>
                  <meta charset="utf-8">
                  <title>API de Contas</title>
                  <style>
                    body { font-family: sans-serif; max-width: 720px; margin: 2rem auto; padding: 0 1rem; }
                    table { border-collapse: collapse; width: 100%%; margin-bottom: 2rem; }
                    th, td { border: 1px solid #ccc; padding: 6px 10px; text-align: left; }
                    th { background: #f0f0f0; }
                    code { background: #f0f0f0; padding: 1px 4px; }
                  </style>
                </head>
                <body>
                  <h1>API de Contas (Javalin)</h1>
                  <h2>Endpoints</h2>
                  <table>
                    <tr><th>Verbo</th><th>Caminho</th><th>Ação</th></tr>
                    <tr><td>GET</td><td><a href="/accounts"><code>/accounts</code></a></td><td>Listar todas</td></tr>
                    <tr><td>POST</td><td><code>/accounts</code></td><td>Criar (body JSON)</td></tr>
                    <tr><td>GET</td><td><code>/accounts/{id}</code></td><td>Buscar por id</td></tr>
                    <tr><td>PUT</td><td><code>/accounts/{id}</code></td><td>Atualizar</td></tr>
                    <tr><td>DELETE</td><td><code>/accounts/{id}</code></td><td>Remover</td></tr>
                  </table>
                  <h2>Contas cadastradas (%d)</h2>
                  <table>
                    <tr><th>ID</th><th>Nome</th><th>Número</th><th>Saldo</th></tr>
                    %s
                  </table>
                </body>
                </html>
                """.formatted(contas.size(), linhas);

        ctx.contentType(ContentType.TEXT_HTML).result(html);
    }

    // Evita que texto digitado pelo usuário seja interpretado como HTML.
    private String escapar(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
