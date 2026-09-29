package com.kauam.apitest.accounts;

public class Account {
    private int id;
    private String nome;
    private String numero;
    private double saldo;

    public Account() {
    }

    public Account(int id, String nome, String numero, double saldo) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.saldo = saldo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
