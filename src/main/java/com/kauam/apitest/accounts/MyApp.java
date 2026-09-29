package com.kauam.apitest.accounts;

import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Configuração da API: liga as dependências e mapeia verbo HTTP + caminho -> método do controller.
 * Não chama .start(), assim os testes (JavalinTest) podem subir o app em porta aleatória.
 */

public class MyApp {

    // Versão padrão: usa o Dao em memória.
    public static Javalin configurarJavalin() {
        return configurarJavalin(new AccountService(new AccountDaoMemoria()));
    }

    // Recebe o Service pronto, o que facilita trocar por um dublê em testes.
    public static Javalin configurarJavalin(AccountService service) {
        AccountsController controller = new AccountsController(service);
        HomeController home = new HomeController(service);
        return Javalin.create(config -> config.router.apiBuilder(() -> {
            get("/", home::home);                    // GET    /  (página HTML com endpoints e contas)
            path("/accounts", () -> {                // prefixo comum: /accounts
                get(controller::getAll);             // GET    /accounts
                post(controller::create);            // POST   /accounts
                path("/{id}", () -> {                // {id} vira ctx.pathParam("id")
                    get(controller::getById);        // GET    /accounts/{id}
                    put(controller::update);         // PUT    /accounts/{id}
                    delete(controller::remove);      // DELETE /accounts/{id}
                });
            });
        }));
    }
}
