/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import com.mycompany.proyectoc.s.Dao.TicketDAO;
import com.mycompany.proyectoc.s.Dao.MensajeChatDAO;
import com.mycompany.proyectoc.s.Modelo.Ticket;
import com.mycompany.proyectoc.s.Modelo.MensajeChat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author sheyr
 */
public class VentanaTecnico extends JFrame {

    private JTextArea areaChat;
    private JTextField campoMensaje;
    private JButton btnEnviar;
    private JButton btnTomarTicket;
    private JButton btnCompletarTicket;
    private JButton btnActualizar;
    private JList<String> listaTickets;
    private DefaultListModel<String> modeloTickets;
    private JTextArea detalleTicket;
    
    private int ticketSeleccionadoId = -1;
    private String nombreTecnico;
    private TicketDAO ticketDAO;
    private MensajeChatDAO mensajeDAO;

    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;

    public VentanaTecnico(String nombreUsuario) {
        this.nombreTecnico = nombreUsuario;
        this.ticketDAO = new TicketDAO();
        this.mensajeDAO = new MensajeChatDAO();
        
        setTitle("Técnico - " + nombreUsuario + " - Sistema de Tickets");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---- Panel de Tickets ----
        modeloTickets = new DefaultListModel<>();
        listaTickets = new JList<>(modeloTickets);
        listaTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTickets = new JScrollPane(listaTickets);
        scrollTickets.setBorder(BorderFactory.createTitledBorder("Tickets Disponibles"));

        detalleTicket = new JTextArea(6, 20);
        detalleTicket.setEditable(false);
        detalleTicket.setBorder(BorderFactory.createTitledBorder("Detalle del Ticket"));
        JScrollPane scrollDetalle = new JScrollPane(detalleTicket);

        // ---- Botones de acción ----
        btnTomarTicket = new JButton("Tomar/Transferir Ticket");
        btnCompletarTicket = new JButton("Marcar Completado");
        btnActualizar = new JButton("Actualizar Lista");
        
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        panelBotones.add(btnTomarTicket);
        panelBotones.add(btnCompletarTicket);
        panelBotones.add(btnActualizar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(scrollTickets, BorderLayout.CENTER);
        panelIzquierdo.add(scrollDetalle, BorderLayout.SOUTH);
        panelIzquierdo.add(panelBotones, BorderLayout.EAST);
        panelIzquierdo.setPreferredSize(new Dimension(400, 600));

        // ---- Panel de Chat ----
        areaChat = new JTextArea();
        areaChat.setEditable(false);
        areaChat.setLineWrap(true);
        areaChat.setWrapStyleWord(true);
        JScrollPane scrollChat = new JScrollPane(areaChat);
        scrollChat.setBorder(BorderFactory.createTitledBorder("Chat del Ticket Seleccionado"));

        campoMensaje = new JTextField();
        campoMensaje.setEnabled(false);
        btnEnviar = new JButton("Enviar");
        btnEnviar.setEnabled(false);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(campoMensaje, BorderLayout.CENTER);
        panelInferior.add(btnEnviar, BorderLayout.EAST);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.add(scrollChat, BorderLayout.CENTER);
        panelDerecho.add(panelInferior, BorderLayout.SOUTH);

        // ---- Agregar a la ventana ----
        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        // ---- Cargar tickets iniciales ----
        cargarTicketsDisponibles();

        // ---- Event Listeners ----
        setupEventListeners();

        setVisible(true);
    }

    private void setupEventListeners() {
        // ---- Acción de enviar mensaje ----
        btnEnviar.addActionListener(e -> enviarMensaje());
        campoMensaje.addActionListener(e -> enviarMensaje());

        // ---- Acción al seleccionar ticket ----
        listaTickets.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarTicket();
            }
        });

        // ---- Acción de tomar/transferir ticket ----
        btnTomarTicket.addActionListener(e -> tomarOTransferirTicket());

        // ---- Acción de completar ticket ----
        btnCompletarTicket.addActionListener(e -> completarTicket());

        // ---- Acción de actualizar ----
        btnActualizar.addActionListener(e -> cargarTicketsDisponibles());
    }

    private void cargarTicketsDisponibles() {
        modeloTickets.clear();
        List<Ticket> tickets = ticketDAO.obtenerTicketsDisponibles();
        
        for (Ticket ticket : tickets) {
            String estado = ticket.getEstado();
            String tecnico = ticket.getTecnicoAsignado();
            String descripcion = "ID:" + ticket.getId() + " - " + ticket.getDescripcion() + 
                               " [Urgencia:" + ticket.getUrgencia() + "] [" + estado + "]";
            
            if (tecnico != null) {
                descripcion += " [Asignado a: " + tecnico + "]";
            }
            
            modeloTickets.addElement(descripcion);
        }
        
        // Limpiar selección
        ticketSeleccionadoId = -1;
        detalleTicket.setText("");
        areaChat.setText("");
        campoMensaje.setEnabled(false);
        btnEnviar.setEnabled(false);
        btnCompletarTicket.setEnabled(false);
    }

    private void seleccionarTicket() {
        String ticketSeleccionado = listaTickets.getSelectedValue();
        if (ticketSeleccionado != null) {
            // Extraer ID del ticket del string
            String idStr = ticketSeleccionado.substring(ticketSeleccionado.indexOf("ID:") + 3, 
                                                        ticketSeleccionado.indexOf(" - "));
            ticketSeleccionadoId = Integer.parseInt(idStr);
            
            // Cargar detalles del ticket
            cargarDetalleTicket(ticketSeleccionadoId);
            
            // Cargar mensajes del chat
            cargarMensajesChat(ticketSeleccionadoId);
            
            // Habilitar chat si el ticket está asignado al técnico actual
            List<Ticket> tickets = ticketDAO.obtenerTicketsDisponibles();
            for (Ticket t : tickets) {
                if (t.getId() == ticketSeleccionadoId) {
                    boolean esDelTecnicoActual = nombreTecnico.equals(t.getTecnicoAsignado());
                    campoMensaje.setEnabled(esDelTecnicoActual);
                    btnEnviar.setEnabled(esDelTecnicoActual);
                    btnCompletarTicket.setEnabled(esDelTecnicoActual && !"COMPLETADO".equals(t.getEstado()));
                    break;
                }
            }
        }
    }

    private void cargarDetalleTicket(int ticketId) {
        List<Ticket> tickets = ticketDAO.obtenerTicketsDisponibles();
        for (Ticket t : tickets) {
            if (t.getId() == ticketId) {
                StringBuilder detalle = new StringBuilder();
                detalle.append("ID: ").append(t.getId()).append("\n");
                detalle.append("Descripción: ").append(t.getDescripcion()).append("\n");
                detalle.append("Urgencia: ").append(t.getUrgencia()).append("\n");
                detalle.append("Departamento: ").append(t.getDepartamento()).append("\n");
                detalle.append("Empleado: ").append(t.getNombreEmpleado()).append("\n");
                detalle.append("Estado: ").append(t.getEstado()).append("\n");
                if (t.getTecnicoAsignado() != null) {
                    detalle.append("Técnico Asignado: ").append(t.getTecnicoAsignado()).append("\n");
                }
                detalleTicket.setText(detalle.toString());
                break;
            }
        }
    }

    private void cargarMensajesChat(int ticketId) {
        areaChat.setText("");
        List<MensajeChat> mensajes = mensajeDAO.obtenerMensajesPorTicket(ticketId);
        
        for (MensajeChat mensaje : mensajes) {
            areaChat.append("[" + mensaje.getFechaEnvio() + "] " + 
                           mensaje.getRemitente() + " (" + mensaje.getTipoRemitente() + "): " + 
                           mensaje.getMensaje() + "\n");
        }
        
        areaChat.setCaretPosition(areaChat.getDocument().getLength());
    }

    private void enviarMensaje() {
        if (ticketSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un ticket primero");
            return;
        }
        
        String texto = campoMensaje.getText().trim();
        if (!texto.isEmpty()) {
            // Guardar mensaje en BD
            MensajeChat mensaje = new MensajeChat(ticketSeleccionadoId, nombreTecnico, "TECNICO", texto);
            mensajeDAO.insertarMensaje(mensaje);
            
            // Recargar chat
            cargarMensajesChat(ticketSeleccionadoId);
            campoMensaje.setText("");
        }
    }

    private void tomarOTransferirTicket() {
        if (ticketSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un ticket primero");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Deseas tomar/transferir este ticket a tu nombre?",
                "Confirmar Acción",
                JOptionPane.YES_NO_OPTION);
                
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (ticketSeleccionadoId != -1) {
                ticketDAO.asignarTecnico(ticketSeleccionadoId, nombreTecnico);
                JOptionPane.showMessageDialog(this, "Ticket asignado correctamente");
                cargarTicketsDisponibles();
            }
        }
    }

    private void completarTicket() {
        if (ticketSeleccionadoId == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un ticket primero");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de marcar este ticket como completado?",
                "Confirmar Completado",
                JOptionPane.YES_NO_OPTION);
                
        if (confirmacion == JOptionPane.YES_OPTION) {
            ticketDAO.completarTicket(ticketSeleccionadoId);
            
            // Agregar mensaje final al chat
            MensajeChat mensajeFinal = new MensajeChat(ticketSeleccionadoId, nombreTecnico, "SISTEMA", 
                                                      "Ticket marcado como COMPLETADO por " + nombreTecnico);
            mensajeDAO.insertarMensaje(mensajeFinal);
            
            JOptionPane.showMessageDialog(this, "Ticket completado correctamente");
            cargarTicketsDisponibles();
        }
    }
}