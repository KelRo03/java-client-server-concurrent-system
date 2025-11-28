/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;


import com.mycompany.proyectoc.s.Dao.UsuarioDAO;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Esta clase representa la ventana de inicio de sesión de la aplicación usando Swing.
 * Tiene campos para que el usuario ingrese su nombre y contraseña, y un botón para iniciar sesión.
 * Cuando el usuario presiona "Ingresar", se valida la información con UsuarioDAO. 
 * Si el login es correcto, muestra un mensaje de bienvenida y abre la ventana correspondiente:
 * VentanaEmpleado si es un empleado, o VentanaTecnico si es un técnico.
 * Si los datos son incorrectos o faltan campos, muestra un mensaje de error.
 * Así, toda la lógica de inicio de sesión queda centralizada y la interfaz es sencilla de usar.
 */

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public Login() {
        setTitle("Inicio de sesión");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        construirGUI();
        setVisible(true);
    }

    private void construirGUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        btnLogin = new JButton("Ingresar");
        panel.add(new JLabel());
        panel.add(btnLogin);

        add(panel);
    }

    public JTextField getTxtUsuario() { return txtUsuario; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JButton getBtnLogin() { return btnLogin; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login v = new Login();
            new controlador.ControladorLogin(v);
        });
    }
}