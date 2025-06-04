package DAO;

import Model.Produto;
import View.Mensagens;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

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
    public boolean deletar(int id_produto) {
        String sql = "DELETE FROM tb_produtos WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id_produto);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao deletar produto com ID " + id_produto + ": " + e.getMessage());
            e.printStackTrace();
            return false;
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
                    mensagem += String.format("Nome: %s \n Estoque atual: %d \n Estoque mínimo: %d\n\n", p.getNome(), p.getQuantidadeEstoque(), p.getQuantidadeMinEstoque());
                    encontrouProdutoAbaixo = true;
                }
            }

            if (encontrouProdutoAbaixo) {
                //JOptionPane.showMessageDialog(null, "Os seguintes produtos estão com estoque abaixo do mínimo:\n\n" + mensagem);
                Mensagens.mostrarAviso("Os seguintes produitos estão com o estoque abaixo do mínimo\n\n" + mensagem);
            } else {
                Mensagens.mostrarCheck("Todos os produtos estão com o estoque acima do mínimo");
            }

            return encontrouProdutoAbaixo;

        } catch (SQLException ex) {
            Mensagens.mostrarError("Erro ao gerar o relátorio de quantidade mínima" + ex.getMessage());
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
                Mensagens.mostrarAviso("Os seguintes produtos estão com o estoque acima do máximo" + mensagem);
            } else {

                Mensagens.mostrarCheck("Tudo ótimo, nenhum produto em excesso no estoque");
            }

            return encontrouProdutoAcima;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório de quantidade mínima: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }// NOVO MÉTODO: Relatório Lista de Preços

    public void relatorioListaDePrecos() {
        StringBuilder mensagem = new StringBuilder("== LISTA DE PREÇOS ==\n\n");
        boolean produtosEncontrados = false;

        try {
            List<Produto> produtos = listarTodos();

            if (produtos.isEmpty()) {
                Mensagens.mostrarAviso("Nenhum produto cadastrado para exibir na lista de preços.");
                return;
            }

            for (Produto p : produtos) {
                mensagem.append(String.format("Nome: %s - Preço: R$ %.2f\n", p.getNome(), p.getPreco()));
                produtosEncontrados = true;
            }

            if (produtosEncontrados) {
                JTextArea textArea = new JTextArea(mensagem.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Lista de Preços", JOptionPane.INFORMATION_MESSAGE);

            } else {
                Mensagens.mostrarAviso("Nenhum produto encontrado para exibir.");
            }

        } catch (SQLException ex) {
            Mensagens.mostrarError("Erro ao gerar a lista de preços: " + ex.getMessage());
        }
    }

    // NOVO MÉTODO: Balanço Físico/Financeiro
    public void relatorioBalancoFisicoFinanceiro() {
        int totalItens = 0;
        double valorTotalEstoque = 0.0;
        StringBuilder detalhes = new StringBuilder("== BALANÇO FÍSICO E FINANCEIRO DO ESTOQUE ==\n\n");
        detalhes.append(String.format("%-30s | %10s | %10s | %15s\n", "Produto", "Qtd.", "Preço Unit.", "Valor em Estoque"));
        

        try {
            List<Produto> produtos = listarTodos(); // Reutiliza o método existente que já ordena por nome

            if (produtos.isEmpty()) {
                Mensagens.mostrarAviso("Nenhum produto cadastrado para gerar o balanço.");
                return;
            }

            for (Produto p : produtos) {
                totalItens += p.getQuantidadeEstoque();
                double valorProdutoEmEstoque = p.getQuantidadeEstoque() * p.getPreco();
                valorTotalEstoque += valorProdutoEmEstoque;
                detalhes.append(String.format("%-30s | %10d | R$ %8.2f | R$ %13.2f\n",
                        p.getNome(),
                        p.getQuantidadeEstoque(),
                        p.getPreco(),
                        valorProdutoEmEstoque));
            }

            detalhes.append("--------------------------------------------------------------------------\n");
            detalhes.append(String.format("\nQuantidade total de itens em estoque: %d unidades\n", totalItens));
            detalhes.append(String.format("Valor financeiro total do estoque: R$ %.2f\n", valorTotalEstoque));

            JOptionPane.showMessageDialog(null, detalhes);

        } catch (SQLException ex) {
            Mensagens.mostrarError("Erro ao gerar o balanço físico/financeiro: " + ex.getMessage());
        }
    }

    // NOVO MÉTODO: Produtos por Categoria
    public void relatorioProdutosPorCategoria() {
        StringBuilder mensagemFinal = new StringBuilder("== PRODUTOS POR CATEGORIA ==\n\n");

        try {
            List<Produto> todosProdutos = listarTodos(); // Já vem com nome da categoria e ordenado por nome do produto

            if (todosProdutos.isEmpty()) {
                Mensagens.mostrarAviso("Nenhum produto cadastrado para exibir.");
                return;
            }

            // Agrupa os produtos por categoria
            Map<String, List<Produto>> produtosAgrupados = new HashMap<>();
            for (Produto p : todosProdutos) {
                produtosAgrupados.computeIfAbsent(p.getNomeCategoria(), k -> new ArrayList<>()).add(p);
            }

            // Ordena as categorias por nome para exibição
            List<String> nomesCategoriaOrdenados = new ArrayList<>(produtosAgrupados.keySet());
            nomesCategoriaOrdenados.sort(Comparator.naturalOrder());

            if (produtosAgrupados.isEmpty()) { // Verificação adicional, embora coberta pela primeira
                Mensagens.mostrarAviso("Nenhuma categoria com produtos encontrada.");
                return;
            }

            for (String nomeCategoria : nomesCategoriaOrdenados) {
                mensagemFinal.append("--- Categoria: ").append(nomeCategoria).append(" ---\n");
                List<Produto> produtosDaCategoria = produtosAgrupados.get(nomeCategoria);
                // produtosDaCategoria já estão ordenados pelo nome do produto (devido ao listarTodos)
                for (Produto p : produtosDaCategoria) {
                    mensagemFinal.append(String.format("  - Nome: %s (ID: %d)\n", p.getNome(), p.getIdProduto()));
                    mensagemFinal.append(String.format("    Preço: R$ %.2f | Estoque: %d %s\n", p.getPreco(), p.getQuantidadeEstoque(), p.getUnidade()));
                }
                mensagemFinal.append("\n");
            }

            JTextArea textArea = new JTextArea(mensagemFinal.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));
            JOptionPane.showMessageDialog(null, scrollPane, "Produtos por Categoria", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            Mensagens.mostrarError("Erro ao gerar o relatório de produtos por categoria: " + ex.getMessage());
        }
    }
}
