/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import Cliente.Login;
import com.mycompany.proyectoc.s.Dao.UsuarioDAO;
import javax.swing.JOptionPane;
/**
 *
 * @author seblo
 */
public class ControladorLogin {

    private final Login vista;
    private final UsuarioDAO dao = new UsuarioDAO();

    public ControladorLogin(Login vista) {
        this.vista = vista;
        this.vista.getBtnLogin().addActionListener(e -> validarLogin());
    }

    private void validarLogin() {
        String nombre = vista.getTxtUsuario().getText().trim();
        String pass = new String(vista.getTxtPassword().getPassword());

        if (nombre.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debe llenar todos los campos.");
            return;
        }

        if (dao.validarLogin(nombre, pass)) {
            String tipo = dao.obtenerTipo(nombre);
            JOptionPane.showMessageDialog(vista, "Bienvenido " + nombre + " (" + tipo + ")");
            vista.dispose();

            // Abrir ventana según tipo de usuario
            if ("empleado".equalsIgnoreCase(tipo)) {
                new Cliente.VentanaEmpleado(nombre);
            } else if ("tecnico".equalsIgnoreCase(tipo)) {
                new Cliente.VentanaTecnico(nombre);
            } else {
                JOptionPane.showMessageDialog(vista, "Tipo de usuario no reconocido.");
            }

        } else {
            JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos.");
        }
    }
}
