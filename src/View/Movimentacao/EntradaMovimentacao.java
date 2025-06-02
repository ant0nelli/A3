package View.Movimentacao;
import DAO.ProdutoDAO;
import Model.Produto;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;


/**
 *
 * @author danie
 */


public class EntradaMovimentacao extends javax.swing.JFrame {
    
    private void carregarProdutosNoComboBox() {
    ProdutoDAO produtoDAO = new ProdutoDAO();
    try {
        List<Produto> produtos = produtoDAO.listarTodos();
        jComboBox1.removeAllItems(); // Limpa o combo
        
        
        for (Produto p : produtos) {
            jComboBox1.addItem(p.getNome());
         
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Erro ao carregar produtos: " + ex.getMessage());
    }
}






    /**
     * Creates new form EntradaMovimentacao
     */
    public EntradaMovimentacao() {
        initComponents();
        carregarProdutosNoComboBox();

    // Adiciona a ação para o botão Salvar (jButton1)
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarMovimentacaoEntrada(); 
        }
    });
}
    // Dentro da classe View.Movimentacao.EntradaMovimentacao

private void salvarMovimentacaoEntrada() {
    // 1. Obter dados da interface gráfica
    String nomeProdutoSelecionado = (String) jComboBox1.getSelectedItem();
    String quantidadeStr = jTextField1.getText().trim();

    // 2. Validar os dados de entrada
    if (nomeProdutoSelecionado == null) {
        JOptionPane.showMessageDialog(this, "Por favor, selecione um produto.", "Validação Falhou", JOptionPane.WARNING_MESSAGE);
        jComboBox1.requestFocus();
        return;
    }

    if (quantidadeStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, digite a quantidade.", "Validação Falhou", JOptionPane.WARNING_MESSAGE);
        jTextField1.requestFocus();
        return;
    }

    int quantidadeEntrada;
    try {
        quantidadeEntrada = Integer.parseInt(quantidadeStr);
        if (quantidadeEntrada <= 0) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser um número positivo.", "Validação Falhou", JOptionPane.ERROR_MESSAGE);
            jTextField1.requestFocus();
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "A quantidade deve ser um número válido (inteiro).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        jTextField1.requestFocus();
        return;
    }

    // 3. Processar e salvar os dados (interação com DAO)
    try {
        ProdutoDAO produtoDAO = new ProdutoDAO(); // Instancia seu ProdutoDAO

        // Como o jComboBox1 armazena Nomes, precisamos buscar o objeto Produto.
        // Idealmente, seu ProdutoDAO teria um método buscarPorNome(String nome).
        // Por agora, vamos iterar pela lista de todos os produtos retornada por listarTodos().
        List<Produto> todosOsProdutos = produtoDAO.listarTodos();
        Produto produtoSelecionadoObj = null;
        for (Produto p : todosOsProdutos) {
            if (p.getNome().equals(nomeProdutoSelecionado)) {
                produtoSelecionadoObj = p;
                break;
            }
        }

        if (produtoSelecionadoObj == null) {
            JOptionPane.showMessageDialog(this, "Produto selecionado ('" + nomeProdutoSelecionado + "') não encontrado nos registros.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- LÓGICA DE ATUALIZAÇÃO DE ESTOQUE E REGISTRO DE MOVIMENTAÇÃO ---

        // a. Atualizar o estoque do produto
        int estoqueAtual = produtoSelecionadoObj.getQuantidadeEstoque();
        int novoEstoque = estoqueAtual + quantidadeEntrada;
        produtoSelecionadoObj.setQuantidadeEstoque(novoEstoque); // Atualiza o objeto Produto

        // Chama o método atualizar do ProdutoDAO.
        // Seu método atualizar(Produto) já persiste todas as informações do produto, incluindo o estoque.
        boolean atualizadoComSucesso = produtoDAO.atualizar(produtoSelecionadoObj);

        if (atualizadoComSucesso) {
            // b. Registrar a movimentação (IMPORTANTE: Exige MovimentacaoDAO e Movimentacao Model)
            // Esta parte é crucial para um bom controle de estoque e rastreabilidade.
            // Você precisará criar a classe Model.Movimentacao e DAO.MovimentacaoDAO.
            /*
            // Exemplo de como seria com MovimentacaoDAO:
            Model.Movimentacao novaEntradaMov = new Model.Movimentacao(); // Supondo que você criou essa classe
            novaEntradaMov.setProdutoId(produtoSelecionadoObj.getIdProduto()); // Usa getIdProduto() do seu Produto model
            novaEntradaMov.setQuantidade(quantidadeEntrada);
            novaEntradaMov.setTipo("ENTRADA"); // Define o tipo da movimentação
            novaEntradaMov.setDataMovimentacao(new java.sql.Timestamp(System.currentTimeMillis())); // Data atual

            DAO.MovimentacaoDAO movimentacaoDAO = new DAO.MovimentacaoDAO(); // Você precisará criar esta classe DAO
            movimentacaoDAO.salvar(novaEntradaMov); // Método para inserir na tabela de movimentações
            */

            String mensagemSucesso = "Entrada de " + quantidadeEntrada + " unidade(s) do produto '" + nomeProdutoSelecionado + "' registrada.\n" +
                                     "Novo estoque: " + novoEstoque + ".";
            
            // Se você tiver a classe MensagemCheck configurada e quiser usá-la:
            // DAO.ProdutoDAO.MensagemCheck.mostrar(mensagemSucesso); 
            // Caso contrário, use JOptionPane padrão:
            JOptionPane.showMessageDialog(this, mensagemSucesso, "Sucesso na Entrada", JOptionPane.INFORMATION_MESSAGE);

            // Limpar campos após salvar
            jTextField1.setText("");
            jComboBox1.setSelectedIndex(-1); // Desseleciona o item ou defina para um índice padrão (ex: 0)
            
            // Opcional: Recarregar os produtos no ComboBox se quiser refletir alguma mudança (improvável neste caso)
            // carregarProdutosNoComboBox(); 

        } else {
            // Teoricamente, seu método atualizar lança RuntimeException em caso de falha SQL,
            // então este 'else' pode não ser alcançado se uma exceção ocorrer.
            JOptionPane.showMessageDialog(this, "Falha ao atualizar o estoque do produto no banco de dados (o DAO não retornou erro, mas indicou falha).", "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erro de SQL ao processar a entrada: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); // Importante para depuração
    } catch (RuntimeException ex) {
        // Seu ProdutoDAO.atualizar pode lançar RuntimeException em caso de SQLException
        JOptionPane.showMessageDialog(this, "Erro (RuntimeException) durante a operação com o banco de dados: " + ex.getMessage(), "Erro Interno do DAO", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } catch (Exception ex) { // Captura genérica para outros erros inesperados
        JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
    



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Salvar");

        jLabel1.setText("Selecione o Item:");

        jLabel2.setText("Digite a Quantidade:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Movimentação de Entrada");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(jButton1)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EntradaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EntradaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EntradaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EntradaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EntradaMovimentacao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
