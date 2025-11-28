/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import com.mycompany.proyectoc.s.Modelo.Ticket;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author sheyr
 */
public class PanelTicket extends JPanel {

    private JLabel lblId;
    private JLabel lblDescripcion;
    private JLabel lblUrgencia;
    private JLabel lblDepartamento;
    private JLabel lblEmpleado;

    private Ticket ticket;

    public PanelTicket(Ticket ticket) {
        this.ticket = ticket;
        setLayout(new GridLayout(5, 1, 5, 5));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        lblId = new JLabel("ID: " + ticket.getId());
        lblDescripcion = new JLabel("Descripción: " + ticket.getDescripcion());
        lblUrgencia = new JLabel("Urgencia: " + ticket.getUrgencia());
        lblDepartamento = new JLabel("Departamento: " + ticket.getDepartamento());
        lblEmpleado = new JLabel("Empleado: " + ticket.getNombreEmpleado());

        add(lblId);
        add(lblDescripcion);
        add(lblUrgencia);
        add(lblDepartamento);
        add(lblEmpleado);
    }

    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Permite actualizar los datos del ticket en el panel
     */
    public void actualizarTicket(Ticket ticket) {
        this.ticket = ticket;
        lblId.setText("ID: " + ticket.getId());
        lblDescripcion.setText("Descripción: " + ticket.getDescripcion());
        lblUrgencia.setText("Urgencia: " + ticket.getUrgencia());
        lblDepartamento.setText("Departamento: " + ticket.getDepartamento());
        lblEmpleado.setText("Empleado: " + ticket.getNombreEmpleado());
        repaint();
    }
}