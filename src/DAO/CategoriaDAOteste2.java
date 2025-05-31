public ArrayList<RelatorioCategoriaDTO> getRelatorioQuantidadePorCategoria() {
    ArrayList<RelatorioCategoriaDTO> relatorio = new ArrayList<>();

    String sql = "SELECT c.nome AS nome_categoria, COUNT(p.id_produto) AS quantidade " +
                 "FROM tb_categorias c " +
                 "LEFT JOIN tb_produtos p ON c.id_categoria = p.id_categoria " +
                 "GROUP BY c.id_categoria, c.nome";

    try (Connection conn = conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String nomeCategoria = rs.getString("nome_categoria");
            int quantidade = rs.getInt("quantidade");

            RelatorioCategoriaDTO item = new RelatorioCategoriaDTO(nomeCategoria, quantidade);
            relatorio.add(item);
        }

    } catch (SQLException e) {
        System.out.println("Erro ao gerar relatório: " + e);
        throw new RuntimeException(e);
    }

    return relatorio;
}
