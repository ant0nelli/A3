/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author arthu
 */
public class Mensagens {

    public static void mostrarCheck(String mensagem) {
        // Carrega e redimensiona o ícone
        ImageIcon originalIcon = new ImageIcon(Mensagens.class.getResource("check.png"));
        Image imagemReduzida = originalIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        Icon checkIcon = new ImageIcon(imagemReduzida);

        JOptionPane.showMessageDialog(
                null,
                mensagem,
                "Sucesso",
                JOptionPane.PLAIN_MESSAGE,
                checkIcon
        );
    }

    public static void mostrarError(String mensagem){
        JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarAviso(String mensagem){
        JOptionPane.showMessageDialog(null, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }


}


