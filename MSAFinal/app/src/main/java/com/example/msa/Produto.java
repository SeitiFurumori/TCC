package com.example.msa;

public class Produto {

    private String nome_produto;
    private double preco_produto;
    private int quantidade_produto;
    private String descricao_produto;
    private String profileUrl;
    private int comprado;
    private String uid;
    private String puid;


    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public double getPreco_produto() {
        return preco_produto;
    }

    public void setPreco_produto(double preco_produto) {
        this.preco_produto = preco_produto;
    }

    public int getQuantidade_produto() {
        return quantidade_produto;
    }

    public void setQuantidade_produto(int quantidade_produto) {
        this.quantidade_produto = quantidade_produto;
    }

    public String getDescricao_produto() {
        return descricao_produto;
    }

    public void setDescricao_produto(String descricao_produto) {
        this.descricao_produto = descricao_produto;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public int getComprado() {
        return comprado;
    }

    public void setComprado(int comprado) {
        this.comprado = comprado;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public Produto(String nome_produto, double preco_produto, int quantidade_produto, String descricao_produto, String profileUrl, String uid, String puid) {
        this.nome_produto = nome_produto;
        this.preco_produto = preco_produto;
        this.quantidade_produto = quantidade_produto;
        this.descricao_produto = descricao_produto;
        this.profileUrl = profileUrl;
        this.uid = uid;
        this.puid = puid;
    }


    public Produto(String nome_produto, double preco_produto, int quantidade_produto, String descricao_produto, String profileUrl, int comprado, String uid, String puid) {
        this.nome_produto = nome_produto;
        this.preco_produto = preco_produto;
        this.quantidade_produto = quantidade_produto;
        this.descricao_produto = descricao_produto;
        this.profileUrl = profileUrl;
        this.comprado = comprado;
        this.uid = uid;
        this.puid = puid;
    }

    public Produto(String nome_produto, double preco_produto, int quantidade_produto, String descricao_produto , int comprado) {
        this.nome_produto = nome_produto;
        this.preco_produto = preco_produto;
        this.quantidade_produto = quantidade_produto;
        this.descricao_produto = descricao_produto;
        this.comprado = comprado;
    }
    public String valores(){
        return "\nNome: "+nome_produto+"\nPreço: "+preco_produto+"\nQuantidade: "+quantidade_produto+"\nDescrição: "+descricao_produto+"\nVendeu: "+comprado;
    }
}



