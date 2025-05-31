public List<Produto> listarProdutosPorCategoria(int categoriaId) {
    List<Produto> produtos = new ArrayList<>();

    String sql = "SELECT * FROM produtos WHERE categoria_id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, categoriaId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Produto produto = new Produto();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
            produto.setQuantidadeMinEstoque(rs.getInt("quantidade_min_estoque"));
            produto.setQuantidadeMaxEstoque(rs.getInt("quantidade_max_estoque"));
            produto.setPreco(rs.getBigDecimal("preco"));
            produto.setUnidade(rs.getString("unidade"));
            produtos.add(produto);
        }

    } catch (SQLException e) {
        System.err.println("Erro ao listar produtos: " + e.getMessage());
    }

    return produtos;
}
