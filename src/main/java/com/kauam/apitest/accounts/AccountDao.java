package com.kauam.apitest.accounts;

import java.util.List;

public interface AccountDao {
    List<Account> getAll();

    Account getById(int id);

    Account save(Account account);

    Account update(int id, Account account);

    Account remove(int id);
}
