/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import com.mycompany.proyectoc.s.Dao.TicketDAO;
import com.mycompany.proyectoc.s.Dao.MensajeChatDAO;
import com.mycompany.proyectoc.s.Modelo.Ticket;
import com.mycompany.proyectoc.s.Modelo.MensajeChat;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * @author sheyr
 */
public class VentanaEmpleado extends JFrame {

    private JTextArea chatArea;
    private JTextField txtMensaje;
    private JButton btnEnviar;
    private JButton btnCrearTicket;
    private JButton btnActualizar;
    private JList<String> listaTickets;
    private DefaultListModel<String> modeloTickets;

    private int ticketSeleccionadoId = -1;
    private String nombreEmpleado;
    private TicketDAO ticketDAO;
    private MensajeChatDAO mensajeDAO;

    public VentanaEmpleado(String nombre) {
        this.nombreEmpleado = nombre;
        this.ticketDAO = new TicketDAO();
        this.mensajeDAO = new MensajeChatDAO();

        setTitle("Empleado: " + nombre + " - Mis Tickets");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---- Panel de Tickets ----
        modeloTickets = new DefaultListModel<>();
        listaTickets = new JList<>(modeloTickets);
        listaTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTickets = new JScrollPane(listaTickets);
        scrollTickets.setBorder(BorderFactory.createTitledBorder("Mis Tickets"));
        scrollTickets.setPreferredSize(new Dimension(300, 200));

        // ---- Panel de Chat ----
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Chat del Ticket Seleccionado"));

        txtMensaje = new JTextField();
        txtMensaje.setEnabled(false);
        btnEnviar = new JButton("Enviar");
        btnEnviar.setEnabled(false);
        btnCrearTicket = new JButton("Crear Nuevo Ticket");
        btnActualizar = new JButton("Actualizar");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(txtMensaje, BorderLayout.CENTER);
        bottomPanel.add(btnEnviar, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(btnCrearTicket);
        topPanel.add(btnActualizar);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(scrollTickets, BorderLayout.CENTER);
        leftPanel.add(topPanel, BorderLayout.NORTH);

        add(leftPanel, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // ---- Cargar tickets iniciales ----
        cargarMisTickets();

        // ---- Event Listeners ----
        setupEventListeners();

        setVisible(true);
    }

    private void setupEventListeners() {
        // Acción de enviar mensaje
        btnEnviar.addActionListener(e -> enviarMensaje());
        txtMensaje.addActionListener(e -> enviarMensaje());

        // Acción de crear ticket
        btnCrearTicket.addActionListener(e -> crearTicket());

        // Acción de actualizar
        btnActualizar.addActionListener(e -> cargarMisTickets());

        // Acción al seleccionar ticket
        listaTickets.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarTicket();
            }
        });
    }

    private void cargarMisTickets() {
        modeloTickets.clear();
        List<Ticket> todosTickets = ticketDAO.obtenerTickets();
        
        // Filtrar solo los tickets del empleado actual
        for (Ticket ticket : todosTickets) {
            if (ticket.getNombreEmpleado().equals(nombreEmpleado)) {
                String descripcion = "ID:" + ticket.getId() + " - " + ticket.getDescripcion() + 
                                   " [Urgencia:" + ticket.getUrgencia() + "] [" + ticket.getEstado() + "]";
                
                if (ticket.getTecnicoAsignado() != null) {
                    descripcion += " [Técnico: " + ticket.getTecnicoAsignado() + "]";
                }
                
                modeloTickets.addElement(descripcion);
            }
        }
        
        // Limpiar selección
        ticketSeleccionadoId = -1;
        chatArea.setText("");
        txtMensaje.setEnabled(false);
        btnEnviar.setEnabled(false);
    }

    private void seleccionarTicket() {
        String ticketSeleccionado = listaTickets.getSelectedValue();
        if (ticketSeleccionado != null) {
            // Extraer ID del ticket del string
            String idStr = ticketSeleccionado.substring(ticketSeleccionado.indexOf("ID:") + 3, 
                                                        ticketSeleccionado.indexOf(" - "));
            ticketSeleccionadoId = Integer.parseInt(idStr);
            
            // Cargar mensajes del chat
            cargarMensajesChat(ticketSeleccionadoId);
            
            // Habilitar chat si el ticket tiene técnico asignado
            List<Ticket> tickets = ticketDAO.obtenerTickets();
            for (Ticket t : tickets) {
                if (t.getId() == ticketSeleccionadoId) {
                    boolean tieneTecnico = (t.getTecnicoAsignado() != null && !"COMPLETADO".equals(t.getEstado()));
                    txtMensaje.setEnabled(tieneTecnico);
                    btnEnviar.setEnabled(tieneTecnico);
                    break;
                }
            }
        }
    }

    private void cargarMensajesChat(int ticketId) {
        chatArea.setText("");
        List<MensajeChat> mensajes = mensajeDAO.obtenerMensajesPorTicket(ticketId);
        
        for (MensajeChat mensaje : mensajes) {
            chatArea.append("[" + mensaje.getFechaEnvio() + "] " + 
                           mensaje.getRemitente() + " (" + mensaje.getTipoRemitente() + "): " + 
                           mensaje.getMensaje() + "\n");
        }
        
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void enviarMensaje() {
        if (ticketSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un ticket primero");
            return;
        }
        
        String texto = txtMensaje.getText().trim();
        if (!texto.isEmpty()) {
            // Guardar mensaje en BD
            MensajeChat mensaje = new MensajeChat(ticketSeleccionadoId, nombreEmpleado, "EMPLEADO", texto);
            mensajeDAO.insertarMensaje(mensaje);
            
            // Recargar chat
            cargarMensajesChat(ticketSeleccionadoId);
            txtMensaje.setText("");
        }
    }

    private void crearTicket() {
        JTextField descripcion = new JTextField();
        JTextField urgencia = new JTextField("1");
        JTextField departamento = new JTextField("General");

        Object[] message = {
                "Descripción:", descripcion,
                "Urgencia (1-5):", urgencia,
                "Departamento:", departamento
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Crear Ticket", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String desc = descripcion.getText().trim();
                int urg = Integer.parseInt(urgencia.getText().trim());
                String dept = departamento.getText().trim();
                
                if (desc.isEmpty() || dept.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor completa todos los campos");
                    return;
                }
                
                if (urg < 1 || urg > 5) {
                    JOptionPane.showMessageDialog(this, "La urgencia debe ser un número entre 1 y 5");
                    return;
                }

                Ticket ticket = new Ticket(desc, urg, dept, nombreEmpleado);
                
                // Guardar en base de datos
                ticketDAO.insertarTicket(ticket);
                
                JOptionPane.showMessageDialog(this, "Ticket creado exitosamente");
                
                // Recargar la lista de tickets
                cargarMisTickets();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La urgencia debe ser un número válido");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al crear ticket: " + e.getMessage());
            }
        }
    }
}