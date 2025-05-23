/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author arthu
 */

public class Produto {
    private int id_produto;
    private String nome;
    private double preco;
    private String unidade;
    private int quantidade_estoque;
    private int quantidade_min_estoque;
    private int quantidade_max_estoque;
    private String categoria;

    //Construtor vazio
    public Produto() {

    }
    //Construtor com prarametro
    public Produto(int id_produto, String nome, double preco, String unidade,int quantidade_estoque, int quantidade_min_estoque, int quantidade_max_estoque, String categoria) {
        this.id_produto = id_produto;
        this.nome = nome;
        this.preco = preco;
        this.unidade = unidade;
        this.quantidade_estoque = quantidade_estoque;
        this.quantidade_min_estoque = quantidade_min_estoque;
        this.quantidade_max_estoque = quantidade_max_estoque;
        this.categoria = categoria;
    }

    public int getIdProduto() {
        return id_produto;
    }

    public void setIdProduto(int id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public int getQuantidadeEstoque() {
        return quantidade_estoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidade_estoque = quantidadeEstoque;
    }

    public int getQuantidadeMinEstoque() {
        return quantidade_min_estoque;
    }

    public void setQuantidadeMinEstoque(int quantidadeMinEstoque) {
        this.quantidade_min_estoque = quantidadeMinEstoque;
    }

    public int getQuantidadeMaxEstoque() {
        return quantidade_max_estoque;
    }

    public void setQuantidadeMaxEstoque(int quantidadeMaxEstoque) {
        this.quantidade_max_estoque = quantidadeMaxEstoque;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    @Override
    public String toString(){
        return this.nome;
    }
}

