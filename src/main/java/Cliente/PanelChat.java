/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

/**
 *
 * @author sheyr
 */
public class PanelChat extends JPanel {

    private JTextArea chatArea;
    private JTextField campoMensaje;
    private JButton btnEnviarMensaje;
    private java.util.function.Consumer<String> sender;

    public PanelChat() {
        setLayout(new BorderLayout(5,5));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel panelMensaje = new JPanel(new BorderLayout(5,5));
        campoMensaje = new JTextField();
        btnEnviarMensaje = new JButton("Enviar");
        btnEnviarMensaje.addActionListener(e -> enviarMensaje());

        panelMensaje.add(campoMensaje, BorderLayout.CENTER);
        panelMensaje.add(btnEnviarMensaje, BorderLayout.EAST);
        add(panelMensaje, BorderLayout.SOUTH);
    }

    public void mostrarMensaje(String msg) {
        chatArea.append(msg + "\n");
    }

    private void enviarMensaje() {
        String msg = campoMensaje.getText().trim();
        
        if (msg.isEmpty()) return;

        try {
            if (sender == null) {
                mostrarMensaje("Error: no hay canal de envío configurado.");
                return;
            }

            sender.accept(msg);
            mostrarMensaje("Tú: " + msg);
            campoMensaje.setText("");

            if (msg.equalsIgnoreCase("fin")) {
                campoMensaje.setEnabled(false);
                btnEnviarMensaje.setEnabled(false);
            }
        } catch (Exception e) {
            mostrarMensaje("Error enviando mensaje: " + e.getMessage());
        }
    }

    public void setSender(java.util.function.Consumer<String> sender) {
        this.sender = sender;
    }
}

