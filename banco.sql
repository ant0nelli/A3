create database if not exists db_estoque;
use db_estoque;
create table tb_categorias(
    nome varchar(100),
    tamanho varchar(20),
    embalagem varchar(50)
);
create table tb_produtos(
    nome varchar(100),
    preco decimal(10,2),
    unidade int,
    quantidade_estoque int,
    quantidade_min_estoque int,
    quantidade_max_estoque int,
    categoria varchar(100)
);