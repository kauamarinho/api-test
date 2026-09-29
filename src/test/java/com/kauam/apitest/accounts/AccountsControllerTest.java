package com.kauam.apitest.accounts;

import io.javalin.http.Context;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class AccountsControllerTest {

    @Test
    public void deve_retornar_a_conta_quando_id_existe() {
        AccountService serviceMock = mock(AccountService.class);
        Context ctxMock = mock(Context.class);
        Account conta = new Account(1, "João", "123", 100.0);
        when(ctxMock.pathParam("id")).thenReturn("1");
        when(serviceMock.getById(1)).thenReturn(conta);

        new AccountsController(serviceMock).getById(ctxMock);

        verify(ctxMock).json(conta);
        verify(ctxMock, never()).status(404);
    }

    @Test
    public void deve_retornar_404_quando_id_nao_existe() {
        AccountService serviceMock = mock(AccountService.class);
        Context ctxMock = mock(Context.class);
        when(ctxMock.pathParam("id")).thenReturn("999");
        when(serviceMock.getById(999)).thenReturn(null);

        new AccountsController(serviceMock).getById(ctxMock);

        verify(ctxMock).status(404);
    }
}
