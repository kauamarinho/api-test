package com.kauam.apitest.accounts;

import java.util.List;

/**
 * Camada de regras de negócio: valida os dados e delega a persistência ao Dao.
 * Regras inválidas viram IllegalArgumentException, que o Controller traduz em HTTP 400.
 */
public class AccountService {
    private final AccountDao dao;

    public AccountService(AccountDao dao) {
        this.dao = dao;
    }

    public List<Account> getAll() {
        return dao.getAll();
    }

    // Retorna null quando o id não existe (o Controller responde 404).
    public Account getById(int id) {
        return dao.getById(id);
    }

    // Valida antes de salvar; o Dao é quem gera o id.
    public Account create(Account nova) {
        validar(nova);
        return dao.save(nova);
    }

    // Mesma validação do create; retorna null se o id não existe.
    public Account update(int id, Account atualizada) {
        validar(atualizada);
        return dao.update(id, atualizada);
    }

    public Account remove(int id) {
        return dao.remove(id);
    }

    // Regras: nome obrigatório e saldo não negativo.
    private void validar(Account conta) {
        if (conta == null || conta.getNome() == null || conta.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (conta.getSaldo() < 0) {
            throw new IllegalArgumentException("Saldo não pode ser negativo");
        }
    }
}
