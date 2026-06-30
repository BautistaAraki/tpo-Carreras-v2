package View;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import Controlador.*;
import Dto.*;

public class VentanaPrincipal extends JFrame {
    private final ControllerJugador jugadorController = ControllerJugador.getInstancia();
    private final ControllerCarrera carreraController = ControllerCarrera.getInstancia();

    private JTextField txtNombre;
    private JTextField txtMail;
    private JLabel lblPuntaje;
    private JLabel lblUsuario;
    private JButton btnAcceder;
    private JButton btnCambiarUsuario;
    private JButton btnAgregarCaballo;
    private JButton btnIniciarCarrera;
    private JSpinner spinnerDistancia;
    private JList<String> listaCaballos;
    private DefaultListModel<String> modeloCaballos;
    private JTextArea areaInfoCaballo;
    private List<CaballoDTO> caballosDisponibles = List.of();

    private static final Color C_BG = new Color(15, 15, 20);
    private static final Color C_PANEL = new Color(25, 25, 35);
    private static final Color C_CARD = new Color(35, 35, 50);
    private static final Color C_ACCENT = new Color(212, 175, 55);
    private static final Color C_ACCENT2 = new Color(180, 100, 20);
    private static final Color C_TEXT = new Color(230, 225, 210);
    private static final Color C_MUTED = new Color(130, 125, 110);
    private static final Color C_GREEN = new Color(80, 200, 120);

    public VentanaPrincipal() {
        configurarVentana();
        construirUI();
        cargarCaballos();
        if (jugadorController.haySesionActiva()) autenticar(jugadorController.obtenerJugadorActual());
    }

    private void configurarVentana() {
        setTitle("Sistema de Carreras");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(880, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_BG);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                jugadorController.cerrarAplicacion();
                dispose();
                System.exit(0);
            }
        });
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        add(crearHeader(), BorderLayout.NORTH);
        add(crearCuerpo(), BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(40, 30, 5), getWidth(), 0, new Color(20, 15, 5)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 75));
        header.setBorder(new MatteBorder(0, 0, 2, 0, C_ACCENT));
        JLabel titulo = new JLabel("  CARRERA DE CABALLOS");
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(C_ACCENT);
        JPanel estado = new JPanel(new GridLayout(2, 1));
        estado.setOpaque(false);
        lblUsuario = new JLabel("SIN USUARIO  ", SwingConstants.RIGHT);
        lblUsuario.setForeground(C_TEXT);
        lblPuntaje = new JLabel("PUNTAJE: 0  ", SwingConstants.RIGHT);
        lblPuntaje.setFont(new Font("Monospaced", Font.BOLD, 15));
        lblPuntaje.setForeground(C_GREEN);
        estado.add(lblUsuario);
        estado.add(lblPuntaje);
        header.add(titulo, BorderLayout.WEST);
        header.add(estado, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        JPanel cuerpo = new JPanel(new GridLayout(1, 2, 12, 0));
        cuerpo.setBackground(C_BG);
        cuerpo.setBorder(new EmptyBorder(14, 14, 14, 14));
        cuerpo.add(crearPanelJugador());
        cuerpo.add(crearPanelCaballos());
        return cuerpo;
    }

    private JPanel crearPanelJugador() {
        JPanel panel = crearCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(tituloSeccion("ACCESO DEL JUGADOR"), g);

        txtNombre = campo();
        txtMail = campo();
        g.gridy = 1; g.gridwidth = 1; g.weightx = .35; panel.add(label("Nombre"), g);
        g.gridx = 1; g.weightx = .65; panel.add(txtNombre, g);
        g.gridx = 0; g.gridy = 2; g.weightx = .35; panel.add(label("Mail"), g);
        g.gridx = 1; g.weightx = .65; panel.add(txtMail, g);

        btnAcceder = boton("INGRESAR / CREAR CUENTA", C_ACCENT, C_BG);
        btnAcceder.addActionListener(e -> accionAcceder());
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2; g.insets = new Insets(12, 10, 4, 10);
        panel.add(btnAcceder, g);

        btnCambiarUsuario = boton("CAMBIAR USUARIO", C_MUTED, C_BG);
        btnCambiarUsuario.setEnabled(false);
        btnCambiarUsuario.addActionListener(e -> accionCambiarUsuario());
        g.gridy = 4; g.insets = new Insets(4, 10, 10, 10); panel.add(btnCambiarUsuario, g);

        g.gridy = 5; panel.add(new JSeparator(), g);
        g.gridy = 6; panel.add(tituloSeccion("CONFIGURACIÓN DE PISTA"), g);
        spinnerDistancia = new JSpinner(new SpinnerNumberModel(100, 50, 500, 50));
        g.gridy = 7; g.gridwidth = 1; g.weightx = .55; panel.add(label("Distancia (metros)"), g);
        g.gridx = 1; g.weightx = .45; panel.add(spinnerDistancia, g);

        areaInfoCaballo = new JTextArea(6, 20);
        areaInfoCaballo.setEditable(false);
        areaInfoCaballo.setBackground(new Color(20, 20, 30));
        areaInfoCaballo.setForeground(C_TEXT);
        areaInfoCaballo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaInfoCaballo.setBorder(new EmptyBorder(8, 8, 8, 8));
        areaInfoCaballo.setText("Ningún caballo seleccionado");
        g.gridx = 0; g.gridy = 8; g.gridwidth = 2; g.weighty = 1; g.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(areaInfoCaballo), g);
        return panel;
    }

    private JPanel crearPanelCaballos() {
        JPanel panel = crearCard();
        panel.setLayout(new BorderLayout(0, 8));
        panel.add(tituloSeccion("CABALLOS DISPONIBLES"), BorderLayout.NORTH);
        modeloCaballos = new DefaultListModel<>();
        listaCaballos = new JList<>(modeloCaballos);
        listaCaballos.setBackground(new Color(20, 20, 30));
        listaCaballos.setForeground(C_TEXT);
        listaCaballos.setFont(new Font("Monospaced", Font.PLAIN, 13));
        listaCaballos.setSelectionBackground(C_ACCENT2);
        listaCaballos.setSelectionForeground(Color.WHITE);
        listaCaballos.setFixedCellHeight(36);
        listaCaballos.setEnabled(false);
        listaCaballos.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) accionSeleccionarCaballo(); });
        panel.add(new JScrollPane(listaCaballos), BorderLayout.CENTER);

        btnAgregarCaballo = boton("AGREGAR CABALLO", C_ACCENT, C_BG);
        btnAgregarCaballo.setEnabled(false);
        btnAgregarCaballo.addActionListener(e -> accionAgregarCaballo());
        btnIniciarCarrera = boton("INICIAR CARRERA", C_GREEN, C_BG);
        btnIniciarCarrera.setEnabled(false);
        btnIniciarCarrera.addActionListener(e -> accionIniciarCarrera());
        JPanel botones = new JPanel(new GridLayout(2, 1, 0, 6));
        botones.setBackground(C_CARD);
        botones.add(btnAgregarCaballo);
        botones.add(btnIniciarCarrera);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(20, 15, 5));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, C_ACCENT2));
        JLabel texto = new JLabel("MVC + DTO + Singleton + JPA/Hibernate");
        texto.setForeground(C_MUTED);
        footer.add(texto);
        return footer;
    }

    private void accionAcceder() {
        ejecutar(() -> {
            JugadorDTO jugador = jugadorController.ingresarORegistrar(txtNombre.getText(), txtMail.getText());
            autenticar(jugador);
        });
    }

    private void autenticar(JugadorDTO jugador) {
        txtNombre.setText(jugador.nombre());
        txtMail.setText(jugador.mail());
        txtNombre.setEnabled(false);
        txtMail.setEnabled(false);
        btnAcceder.setEnabled(false);
        btnCambiarUsuario.setEnabled(true);
        btnAgregarCaballo.setEnabled(true);
        listaCaballos.setEnabled(true);
        lblUsuario.setText(jugador.nombre().toUpperCase() + "  ");
        lblPuntaje.setText("PUNTAJE: " + jugador.puntaje() + "  ");
        cargarCaballos();
    }

    private void accionCambiarUsuario() {
        jugadorController.cerrarSesion();
        txtNombre.setText("");
        txtMail.setText("");
        txtNombre.setEnabled(true);
        txtMail.setEnabled(true);
        btnAcceder.setEnabled(true);
        btnCambiarUsuario.setEnabled(false);
        btnAgregarCaballo.setEnabled(false);
        btnIniciarCarrera.setEnabled(false);
        listaCaballos.clearSelection();
        listaCaballos.setEnabled(false);
        areaInfoCaballo.setText("Ningún caballo seleccionado");
        lblUsuario.setText("SIN USUARIO  ");
        lblPuntaje.setText("PUNTAJE: 0  ");
        txtNombre.requestFocusInWindow();
    }

    private void accionSeleccionarCaballo() {
        int indice = listaCaballos.getSelectedIndex();
        if (indice < 0 || !listaCaballos.isEnabled()) return;
        ejecutar(() -> {
            CaballoDTO caballo = caballosDisponibles.get(indice);
            jugadorController.seleccionarCaballo(caballo.nombre());
            areaInfoCaballo.setText("Nombre      : " + caballo.nombre() + "\nPerfil      : " + caballo.perfil()
                    + "\nVelocidad   : " + caballo.velocidadBase() + "\nResistencia : " + caballo.resistencia());
            btnIniciarCarrera.setEnabled(true);
        });
    }

    private void accionAgregarCaballo() {
        JTextField nombre = new JTextField();
        JTextField velocidad = new JTextField();
        JTextField resistencia = new JTextField();
        JComboBox<String> perfil = new JComboBox<>(new String[]{"Veloz", "Resistente", "Equilibrado"});
        Object[] campos = {"Nombre", nombre, "Velocidad", velocidad, "Resistencia", resistencia, "Perfil", perfil};
        if (JOptionPane.showConfirmDialog(this, campos, "Registrar caballo", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        ejecutar(() -> {
            carreraController.registrarCaballo(new CaballoDTO(nombre.getText(), Double.parseDouble(velocidad.getText()),
                    Double.parseDouble(resistencia.getText()), (String) perfil.getSelectedItem()));
            cargarCaballos();
        });
    }

    private void accionIniciarCarrera() {
        ejecutar(() -> {
            double distancia = ((Number) spinnerDistancia.getValue()).doubleValue();
            carreraController.crearCarrera(distancia, caballosDisponibles.stream().map(CaballoDTO::nombre).toList());
            new VentanaCarrera().setVisible(true);
            setVisible(false);
        });
    }

    private void cargarCaballos() {
        ejecutar(() -> {
            caballosDisponibles = carreraController.listarCaballos();
            modeloCaballos.clear();
            caballosDisponibles.forEach(c -> modeloCaballos.addElement(String.format("%-14s [%s]", c.nombre(), c.perfil())));
        });
    }

    private JPanel crearCard() {
        JPanel panel = new JPanel();
        panel.setBackground(C_CARD);
        panel.setBorder(new CompoundBorder(new LineBorder(C_ACCENT2, 1), new EmptyBorder(10, 10, 10, 10)));
        return panel;
    }
    private JLabel label(String texto) { JLabel l = new JLabel(texto); l.setForeground(C_TEXT); return l; }
    private JTextField campo() { JTextField c = new JTextField(); c.setBackground(new Color(20,20,30)); c.setForeground(C_TEXT); c.setCaretColor(C_TEXT); return c; }
    private JLabel tituloSeccion(String texto) { JLabel l = new JLabel(texto); l.setForeground(C_ACCENT); l.setFont(new Font("Monospaced", Font.BOLD, 12)); return l; }
    private JButton boton(String texto, Color fondo, Color frente) {
        JButton b = new JButton(texto); b.setBackground(fondo); b.setForeground(frente); b.setFont(new Font("Monospaced", Font.BOLD, 12));
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return b;
    }
    private void ejecutar(Runnable accion) {
        try { accion.run(); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Atención", JOptionPane.WARNING_MESSAGE); }
    }
}

