create database if not exists db_estoque;
use db_estoque;
create table tb_categorias(
    id_categoria int not null,
    nome varchar(100),
    tamanho varchar(20),
    embalagem varchar(50),
    primary key(id_categoria)
);
create table tb_produtos(
    id_produto int not null,
    nome varchar(100),
    preco decimal(10,2),
    unidade varchar(20),
    quantidade_estoque int,
    quantidade_min_estoque int,
    quantidade_max_estoque int,
    primary key(id_produto),
    id_categoria int not null,
    constraint fk_id_categoria foreign key (id_categoria) references tb_categorias(id_categoria)
);