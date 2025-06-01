/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author arthu
 */
public class MensagemCheck {

    public static void mostrar(String mensagem) {
        // Carrega e redimensiona o ícone
        ImageIcon originalIcon = new ImageIcon(MensagemCheck.class.getResource("check.png"));
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
}
