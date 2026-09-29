package com.kauam.apitest.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {
    private AccountService service;

    @BeforeEach
    public void setUp() {
        service = new AccountService(new AccountDaoMemoria());
    }

    @Test
    public void deve_criar_conta_com_id_gerado_para_dados_validos() {
        Account criada = service.create(new Account(0, "João", "123", 100.0));

        assertEquals(1, criada.getId());
    }

    @Test
    public void deve_lancar_IllegalArgumentException_para_saldo_negativo() {
        Account invalida = new Account(0, "João", "123", -1.0);

        assertThrows(IllegalArgumentException.class, () -> service.create(invalida));
    }

    @Test
    public void deve_lancar_IllegalArgumentException_para_nome_vazio() {
        Account invalida = new Account(0, " ", "123", 10.0);

        assertThrows(IllegalArgumentException.class, () -> service.create(invalida));
    }

    @Test
    public void deve_retornar_null_ao_buscar_id_inexistente() {
        assertNull(service.getById(999));
    }
}
