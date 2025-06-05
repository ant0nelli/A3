/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View.Movimentacao;
import Model.Produto;
import DAO.ProdutoDAO;
import View.Mensagens;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author danie
 */
public class SaidaMovimentacao extends javax.swing.JFrame {
    private void carregarProdutosNoComboBox() {
    ProdutoDAO produtoDAO = new ProdutoDAO();
    try {
        List<Produto> produtos = produtoDAO.listarTodos();
        jComboBox1.removeAllItems();
        
        
        for (Produto p : produtos) {
            jComboBox1.addItem(p.getNome());
         
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Erro ao carregar produtos: " + ex.getMessage());
    }
}

    /**
     * Creates new form SaidaMovimentacao
     */
    public SaidaMovimentacao() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        carregarProdutosNoComboBox();

           
        }
    

private void salvarMovimentacaoSaida() {
    
    String nomeProdutoSelecionado = (String) jComboBox1.getSelectedItem();
    String quantidadeStr = jTextField1.getText().trim();

 
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

    int quantidadeSaida;
    try {
        quantidadeSaida = Integer.parseInt(quantidadeStr);
        if (quantidadeSaida <= 0) {
            JOptionPane.showMessageDialog(this, "A quantidade de saída deve ser um número positivo.", "Validação Falhou", JOptionPane.ERROR_MESSAGE);
            jTextField1.requestFocus();
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "A quantidade deve ser um número válido (inteiro).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        jTextField1.requestFocus();
        return;
    }


    try {
        ProdutoDAO produtoDAO = new ProdutoDAO();

       
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

        
        int estoqueAtual = produtoSelecionadoObj.getQuantidadeEstoque();
        
        if (estoqueAtual == 0) {
            JOptionPane.showMessageDialog(this,
                "O produto '" + nomeProdutoSelecionado + "' já está com ESTOQUE ZERADO.\nNão é possível realizar a saída.",
                "Estoque Zerado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (quantidadeSaida > estoqueAtual) {
            JOptionPane.showMessageDialog(this,
                "A quantidade de saída solicitada (" + quantidadeSaida + ") é MAIOR que o estoque atual (" + estoqueAtual + ") do produto '" + nomeProdutoSelecionado + "'.\nSaída não permitida.",
                "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
            jTextField1.setText(String.valueOf(estoqueAtual)); 
            jTextField1.requestFocus();
            return; 
        }
        
        if (estoqueAtual - quantidadeSaida < produtoSelecionadoObj.getQuantidadeMinEstoque()) {
            Mensagens.mostrarAviso("O Estoque está menor que a quantidade mínima, Reabasteça seu Estoque!");
        }
        

        int novoEstoque = estoqueAtual - quantidadeSaida;
        produtoSelecionadoObj.setQuantidadeEstoque(novoEstoque);

        
        boolean atualizadoComSucesso = produtoDAO.atualizar(produtoSelecionadoObj);

        if (atualizadoComSucesso) {

            String mensagemSucesso = "Saída de " + quantidadeSaida + " unidade(s) do produto '" + nomeProdutoSelecionado + "' registrada.\n" +
                                     "Novo estoque: " + novoEstoque + ".";
            
            JOptionPane.showMessageDialog(this, mensagemSucesso, "Sucesso na Movimentação de Saída", JOptionPane.INFORMATION_MESSAGE);

            // Limpar campos após salvar
            jTextField1.setText("");
            jComboBox1.setSelectedIndex(-1); // Ou defina para um item padrão, como o primeiro

        } else {
            JOptionPane.showMessageDialog(this, "Falha ao atualizar o estoque do produto no banco de dados.", "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erro de SQL ao processar a saída: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } catch (RuntimeException ex) {
        JOptionPane.showMessageDialog(this, "Erro (RuntimeException) durante a operação com o banco de dados: " + ex.getMessage(), "Erro Interno do DAO", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Movimentação de Saída");

        jButton1.setText("Salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Selecione o Item:");

        jLabel4.setText("Digite a Quantidade:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 15, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(62, 62, 62))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        salvarMovimentacaoSaida();
        
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(SaidaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SaidaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SaidaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SaidaMovimentacao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SaidaMovimentacao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
