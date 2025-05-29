package DAO;

import Model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ProdutoDAO {

    private final Connection connection;

    //Construtor para conexão ñao ficar null
    public ProdutoDAO() {
        this.connection = conectar();
    }

    public static Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_estoque", "root", "root123"
            );
            return con;

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //Update
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE tb_produtos SET nome = ?, preco = ?, unidade = ?, quantidade_estoque = ?, quantidade_min_estoque = ?, quantidade_max_estoque = ?, id_categoria = ? WHERE id_produto = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getQuantidadeMinEstoque());
            stmt.setInt(6, produto.getQuantidadeMaxEstoque());
            stmt.setInt(7, produto.getIdCategoria());
            stmt.setInt(8, produto.getIdProduto());
            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    //Deletar
    public void deletar(int id_produto) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_produto);
            stmt.executeUpdate();
        }
    }
    //Buscar por id
    public Produto buscarPorId(int id_produto) throws SQLException {
        //join para buscar o nome + id para poder usar lá no combobox em editar produto
        String sql = "SELECT p.*, c.nome AS nome_categoria "
                + "FROM tb_produtos p "
                + "JOIN tb_categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_produto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }
        return null;
    }
    //Listar todos
    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        //JOIN com a mesma finalidade do buscarPOrId
        String sql = "SELECT p.*, c.nome AS nome_categoria "
                + "FROM tb_produtos p "
                + "JOIN tb_categorias c ON p.id_categoria = c.id_categoria";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }
        return produtos;
    }
    //Mapear os produtos
    private Produto mapearProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setIdProduto(rs.getInt("id_produto"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setUnidade(rs.getString("unidade"));
        produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
        produto.setQuantidadeMinEstoque(rs.getInt("quantidade_min_estoque"));
        produto.setQuantidadeMaxEstoque(rs.getInt("quantidade_max_estoque"));

        produto.setIdCategoria(rs.getInt("id_categoria"));
        produto.setNomeCategoria(rs.getString("nome_categoria"));
        return produto;
    }
    //Reajuste Percentual
    public boolean reajustarPrecoProduto(Produto produto, double percentual) {
        String sql = "UPDATE tb_produtos SET preco = preco * (1 + ? / 100) WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDouble(1, percentual);
            stmt.setInt(2, produto.getIdProduto());

            stmt.execute();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Insert
    public boolean insertProduto(Produto produto) {
        String sql = "INSERT INTO tb_produtos (nome, preco, unidade, quantidade_estoque, quantidade_min_estoque, quantidade_max_estoque, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getQuantidadeMinEstoque());
            stmt.setInt(6, produto.getQuantidadeMaxEstoque());
            stmt.setInt(7, produto.getIdCategoria());

            stmt.executeUpdate();
            stmt.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Erro:" + e);
            throw new RuntimeException(e);
        }
    }

//RELATÓRIOS



    //Relatorio com produtos que estão abaixo da quantidade mínima
    public boolean relatorioQntMinima() {
        ProdutoDAO listarprodutos = new ProdutoDAO();
        boolean encontrouProdutoAbaixo = false;
        String mensagem = "";

        try {
            List<Produto> produtos = listarprodutos.listarTodos();

            for (Produto p : produtos) {
                if (p.getQuantidadeEstoque() < p.getQuantidadeMinEstoque()) {
                    mensagem += String.format("Nome: %s \n Estoque atual: %d \n Estoque mínimo: %d", p.getNome(), p.getQuantidadeEstoque(), p.getQuantidadeMinEstoque());
                    encontrouProdutoAbaixo = true;
                }
            }

            if (encontrouProdutoAbaixo) {
                JOptionPane.showMessageDialog(null, "Os seguintes produtos estão com estoque abaixo do mínimo:\n\n" + mensagem);
            } else {
                JOptionPane.showMessageDialog(null, "Todos os produtos estão com estoque acima do mínimo.");
            }

            return encontrouProdutoAbaixo;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório de quantidade mínima: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    //Relatório com produtos acima da qnt máxima

    public boolean relatorioQntMax() {
        ProdutoDAO daoRelatorioMax = new ProdutoDAO();
        boolean encontrouProdutoAcima = false;
        String mensagem = "";

        try {
            List<Produto> produtos = daoRelatorioMax.listarTodos();
            for (Produto p : produtos) {
                if (p.getQuantidadeMaxEstoque() < p.getQuantidadeEstoque()) {
                    mensagem += String.format("\nNome: %s \n Estoque máximo: %d \n Estoque atual: %d \n", p.getNome(), p.getQuantidadeMaxEstoque(), p.getQuantidadeEstoque());
                    encontrouProdutoAcima = true;
                }
            }
            if (encontrouProdutoAcima) {
                JOptionPane.showMessageDialog(null, "Os seguintes produtos estão com o estoque acima do máximo" + mensagem);
            } else {
                JOptionPane.showMessageDialog(null, "Tudo ótimo, nenhum produto em excesso no estoque");
            }

            return encontrouProdutoAcima;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório de quantidade mínima: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

}
