package DAO;

import Model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProdutoDAO {
    private Connection connection;

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


    public boolean atualizar(Produto produto){
        String sql = "UPDATE tb_produtos SET nome = ?, preco = ?, unidade = ?, quantidade_estoque = ?, quantidade_min_estoque = ?, quantidade_max_estoque = ? WHERE id_produto = ?";
        try (Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getQuantidadeMinEstoque());
            stmt.setInt(6, produto.getQuantidadeMaxEstoque());
            stmt.setString(7, produto.getCategoria());
            stmt.executeUpdate();
            stmt.close();
            return true;
        }   catch (SQLException e) {
        System.out.println("Erro: " + e.getMessage());
        throw new RuntimeException(e);
    }
}


    public void deletar(int id_produto) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_produto);
            stmt.executeUpdate();
        }
    }

    public Produto buscarPorId(int id_produto) throws SQLException {
        String sql = "SELECT * FROM produtos WHERE id_produto = ?";
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

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }
        return produtos;
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setIdProduto(rs.getInt("id_produto"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setUnidade(rs.getString("unidade"));
        produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
        produto.setQuantidadeMinEstoque(rs.getInt("quantidade_min_estoque"));
        produto.setQuantidadeMaxEstoque(rs.getInt("quantidade_max_estoque"));
        produto.setCategoria(rs.getString("categoria"));
        return produto;
    }

    
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
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getQuantidadeMinEstoque());
            stmt.setInt(6, produto.getQuantidadeMaxEstoque());
            stmt.setString(7, produto.getCategoria());

            stmt.executeUpdate();
            stmt.close();

        return true;

    } catch (SQLException e) {
        System.out.println("Erro:" + e);
        throw new RuntimeException(e);
    }
}

}