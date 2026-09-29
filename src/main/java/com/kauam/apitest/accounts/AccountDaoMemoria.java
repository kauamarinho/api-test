package com.kauam.apitest.accounts;

import java.util.ArrayList;
import java.util.List;

/**
 * Camada de persistência: guarda as contas em uma lista na memória (os dados somem ao reiniciar).
 * Os métodos são synchronized porque o servidor atende requisições em várias threads.
 */
public class AccountDaoMemoria implements AccountDao {
    private final List<Account> contas = new ArrayList<>();
    private int proximoId = 1;

    // Devolve uma cópia da lista para ninguém alterar o estado interno por fora.
    @Override
    public synchronized List<Account> getAll() {
        return new ArrayList<>(contas);
    }

    // Busca por id; null se não achar.
    @Override
    public synchronized Account getById(int id) {
        return contas.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    // Gera o id (1, 2, 3...) e adiciona à lista.
    @Override
    public synchronized Account save(Account account) {
        account.setId(proximoId++);
        contas.add(account);
        return account;
    }

    // Copia os campos novos para a conta existente (o id não muda); null se não existir.
    @Override
    public synchronized Account update(int id, Account account) {
        Account existente = getById(id);
        if (existente == null) return null;
        existente.setNome(account.getNome());
        existente.setNumero(account.getNumero());
        existente.setSaldo(account.getSaldo());
        return existente;
    }

    // Remove e devolve a conta removida; null se não existir.
    @Override
    public synchronized Account remove(int id) {
        Account existente = getById(id);
        if (existente != null) contas.remove(existente);
        return existente;
    }
}
