package View;
 
import Controlador.ControllerJugador;
import Controlador.controllerCarrera;
import Dto.JugadorDTO;
import Modelo1.Caballo;
import repositorio.caballorepositorio;
 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
 
public class VentanaPrincipal extends JFrame {
 
    // ── controladores y repositorio ──────────────────────────
    private ControllerJugador controllerJugador;
    private controllerCarrera  ctrlCarrera;
    private caballorepositorio repoCaballo;
 
    // ── campos de la UI ──────────────────────────────────────
    private JTextField txtNombre;
    private JTextField txtMail;
    private JList<String> listaCaballos;
    private DefaultListModel<String> modeloCaballos;
    private JTextArea areaInfoCaballo;
    private JLabel lblPuntaje;
    private JButton btnCrearJugador;
    private JButton btnIniciarCarrera;
    private JButton btnAgregarCaballo;
 
    private List<Caballo> caballosDisponibles;
 
    // ── paleta ───────────────────────────────────────────────
    private static final Color C_BG      = new Color(15,  15,  20);
    private static final Color C_PANEL   = new Color(25,  25,  35);
    private static final Color C_CARD    = new Color(35,  35,  50);
    private static final Color C_ACCENT  = new Color(212, 175, 55);
    private static final Color C_ACCENT2 = new Color(180, 100, 20);
    private static final Color C_TEXT    = new Color(230, 225, 210);
    private static final Color C_MUTED   = new Color(130, 125, 110);
    private static final Color C_GREEN   = new Color(80,  200, 120);
 
    public VentanaPrincipal() {
        controllerJugador = new ControllerJugador();
        ctrlCarrera       = new controllerCarrera();
        repoCaballo       = new caballorepositorio();
        configurarVentana();
        construirUI();
        cargarCaballos();
    }

    // Constructor con jugador existente — para cuando vuelve del menu
    public VentanaPrincipal(ControllerJugador jugadorExistente) {
        controllerJugador = jugadorExistente;
        ctrlCarrera       = new controllerCarrera();
        repoCaballo       = new caballorepositorio();
        configurarVentana();
        construirUI();
        cargarCaballos();
        txtNombre.setText(controllerJugador.obtenerNombreJugador());
        txtNombre.setEnabled(false);
        txtMail.setEnabled(false);
        btnCrearJugador.setText("✔ JUGADOR REGISTRADO");
        btnCrearJugador.setEnabled(false);
        lblPuntaje.setText("PUNTAJE: " + controllerJugador.obtenerPuntaje() + "  ");
    }
    
        
    
    
    
 
    // ════════════════════════════════════════════════════════
    //  Configuración base
    // ════════════════════════════════════════════════════════
    private void configurarVentana() {
        setTitle("🏇 Sistema de Carreras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_BG);
    }
 
    // ════════════════════════════════════════════════════════
    //  Construcción de la interfaz
    // ════════════════════════════════════════════════════════
    private void construirUI() {
        setLayout(new BorderLayout(0, 0));
        add(crearHeader(), BorderLayout.NORTH);
        add(crearCuerpo(), BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);
    }
 
    // ── Header ───────────────────────────────────────────────
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(40, 30, 5),
                    getWidth(), 0, new Color(20, 15, 5));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 75));
        header.setBorder(new MatteBorder(0, 0, 2, 0, C_ACCENT));
 
        JLabel titulo = new JLabel("  🏇  CARRERA DE CABALLOS");
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(C_ACCENT);
 
        lblPuntaje = new JLabel("PUNTAJE: 0  ");
        lblPuntaje.setFont(new Font("Monospaced", Font.BOLD, 16));
        lblPuntaje.setForeground(C_GREEN);
 
        header.add(titulo,     BorderLayout.WEST);
        header.add(lblPuntaje, BorderLayout.EAST);
        return header;
    }
 
    // ── Cuerpo principal ─────────────────────────────────────
    private JPanel crearCuerpo() {
        JPanel cuerpo = new JPanel(new GridLayout(1, 2, 12, 0));
        cuerpo.setBackground(C_BG);
        cuerpo.setBorder(new EmptyBorder(14, 14, 14, 14));
 
        cuerpo.add(crearPanelJugador());
        cuerpo.add(crearPanelCaballos());
        return cuerpo;
    }
 
    // ── Panel izquierdo: datos del jugador ───────────────────
    private JPanel crearPanelJugador() {
        JPanel panel = crearCard("JUGADOR");
 
        JLabel lNombre = label("Nombre");
        txtNombre = campo();
 
        JLabel lMail = label("Mail");
        txtMail = campo();
 
        btnCrearJugador = boton("REGISTRAR JUGADOR", C_ACCENT, C_BG);
        btnCrearJugador.addActionListener(e -> accionCrearJugador());
 
        panel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(6, 10, 6, 10);
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
 
        g.gridx=0; g.gridy=0; g.gridwidth=2;
        panel.add(tituloSeccion("DATOS DEL JUGADOR"), g);
 
        g.gridy=1; g.gridwidth=1; g.weightx=0.35;
        panel.add(lNombre, g);
        g.gridx=1; g.weightx=0.65;
        panel.add(txtNombre, g);
 
        g.gridx=0; g.gridy=2; g.weightx=0.35;
        panel.add(lMail, g);
        g.gridx=1; g.weightx=0.65;
        panel.add(txtMail, g);
 
        g.gridx=0; g.gridy=3; g.gridwidth=2;
        g.insets = new Insets(14,10,6,10);
        panel.add(btnCrearJugador, g);
 
        g.gridy=4; g.insets = new Insets(18,10,6,10);
        panel.add(separador(), g);
 
        g.gridy=5; g.insets = new Insets(6,10,4,10);
        panel.add(tituloSeccion("CABALLO SELECCIONADO"), g);
 
        areaInfoCaballo = new JTextArea(6, 20);
        areaInfoCaballo.setEditable(false);
        areaInfoCaballo.setBackground(new Color(20,20,30));
        areaInfoCaballo.setForeground(C_TEXT);
        areaInfoCaballo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaInfoCaballo.setBorder(new EmptyBorder(8,8,8,8));
        areaInfoCaballo.setText("  Ninguno seleccionado");
        JScrollPane scroll = new JScrollPane(areaInfoCaballo);
        scroll.setBorder(new LineBorder(C_ACCENT2, 1));
        g.gridy=6; g.weighty=1; g.fill=GridBagConstraints.BOTH;
        panel.add(scroll, g);
 
        return panel;
    }
 
    // ── Panel derecho: lista de caballos ─────────────────────
    private JPanel crearPanelCaballos() {
        JPanel panel = crearCard("CABALLOS");
        panel.setLayout(new BorderLayout(0, 8));
        panel.setBorder(new CompoundBorder(
            panel.getBorder(),
            new EmptyBorder(10,10,10,10)));
 
        panel.add(tituloSeccion("CABALLOS DISPONIBLES"), BorderLayout.NORTH);
 
        modeloCaballos = new DefaultListModel<>();
        listaCaballos  = new JList<>(modeloCaballos);
        listaCaballos.setBackground(new Color(20,20,30));
        listaCaballos.setForeground(C_TEXT);
        listaCaballos.setFont(new Font("Monospaced", Font.PLAIN, 13));
        listaCaballos.setSelectionBackground(C_ACCENT2);
        listaCaballos.setSelectionForeground(Color.WHITE);
        listaCaballos.setFixedCellHeight(36);
        listaCaballos.setBorder(new EmptyBorder(4,8,4,8));
        listaCaballos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) accionSeleccionarCaballo();
        });
 
        JScrollPane scroll = new JScrollPane(listaCaballos);
        scroll.setBorder(new LineBorder(C_ACCENT2, 1));
        panel.add(scroll, BorderLayout.CENTER);
 
        // ── Botones ──────────────────────────────────────────
        btnAgregarCaballo = boton("➕  AGREGAR CABALLO", C_ACCENT, C_BG);
        btnAgregarCaballo.addActionListener(e -> accionAgregarCaballo());
 
        btnIniciarCarrera = boton("🏁  INICIAR CARRERA", C_GREEN, C_BG);
        btnIniciarCarrera.setEnabled(false);
        btnIniciarCarrera.addActionListener(e -> accionIniciarCarrera());
 
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 0, 6));
        panelBotones.setBackground(C_CARD);
        panelBotones.add(btnAgregarCaballo);
        panelBotones.add(btnIniciarCarrera);
        panel.add(panelBotones, BorderLayout.SOUTH);
 
        return panel;
    }
 
    // ── Footer ───────────────────────────────────────────────
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(20,15,5));
        footer.setBorder(new MatteBorder(1,0,0,0, C_ACCENT2));
        JLabel lbl = new JLabel("Sistema de Carreras — MVC + DTO + Singleton");
        lbl.setForeground(C_MUTED);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        footer.add(lbl);
        return footer;
    }
 
    // ════════════════════════════════════════════════════════
    //  Acciones
    // ════════════════════════════════════════════════════════
    private void accionCrearJugador() {
        String nombre = txtNombre.getText().trim();
        String mail   = txtMail.getText().trim();
 
        if (nombre.isEmpty() || mail.isEmpty()) {
            mostrarError("Completá nombre y mail para continuar.");
            return;
        }
 
        JugadorDTO dto = new JugadorDTO(nombre, mail, 0);
        controllerJugador.crearJugador(dto);
 
        lblPuntaje.setText("PUNTAJE: 0  ");
        btnCrearJugador.setText("✔ JUGADOR REGISTRADO");
        btnCrearJugador.setEnabled(false);
        txtNombre.setEnabled(false);
        txtMail.setEnabled(false);
 
        verificarHabilitacionInicio();
    }
 
    private void accionAgregarCaballo() {
        JTextField txtNombreC     = new JTextField();
        JTextField txtVelocidad   = new JTextField();
        JTextField txtResistencia = new JTextField();
        String[]   perfiles       = {"Veloz", "Resistente", "Equilibrado"};
        JComboBox<String> comboPerfil = new JComboBox<>(perfiles);
 
        JPanel form = new JPanel(new GridLayout(4, 2, 6, 8));
        form.setBackground(C_CARD);
        form.add(new JLabel("Nombre:"));      form.add(txtNombreC);
        form.add(new JLabel("Velocidad:"));   form.add(txtVelocidad);
        form.add(new JLabel("Resistencia:")); form.add(txtResistencia);
        form.add(new JLabel("Perfil:"));      form.add(comboPerfil);
 
        for (Component comp : form.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(C_TEXT);
            }
        }
 
        int resultado = JOptionPane.showConfirmDialog(
            this, form, "Registrar nuevo caballo",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
 
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String nombre      = txtNombreC.getText().trim();
                double velocidad   = Double.parseDouble(txtVelocidad.getText().trim());
                double resistencia = Double.parseDouble(txtResistencia.getText().trim());
                String perfil      = (String) comboPerfil.getSelectedItem();
 
                if (nombre.isEmpty()) {
                    mostrarError("El nombre no puede estar vacío.");
                    return;
                }
 
                Caballo nuevo = new Caballo(
                    nombre, velocidad, resistencia, resistencia, 0.0, perfil
                );
 
                repoCaballo.guardar(nuevo);
 
                caballosDisponibles.add(nuevo);
                modeloCaballos.addElement(
                    String.format("%-14s  [%s]", nuevo.getNombre(), nuevo.getperfil())
                );
 
                JOptionPane.showMessageDialog(this,
                    "✔ Caballo \"" + nombre + "\" registrado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
 
            } catch (NumberFormatException ex) {
                mostrarError("Velocidad y resistencia deben ser números. Ej: 8.5");
            }
        }
    }
 
    private void accionSeleccionarCaballo() {
        int idx = listaCaballos.getSelectedIndex();
        if (idx < 0 || caballosDisponibles == null) return;
 
        Caballo c = caballosDisponibles.get(idx);
        controllerJugador.seleccionarCaballo(c);
 
        areaInfoCaballo.setText(
            "  Nombre    : " + c.getNombre()        + "\n" +
            "  Perfil     : " + c.getperfil()        + "\n" +
            "  Velocidad  : " + c.getVelocidadBase() + "\n" +
            "  Resistencia: " + c.getResistencia()   + "\n"
        );
 
        verificarHabilitacionInicio();
    }
 
    private void accionIniciarCarrera() {
        Caballo caballoJugador = controllerJugador.obtenerCaballoSeleccionado();
        if (caballoJugador == null) {
            mostrarError("Seleccioná un caballo primero.");
            return;
        }
 
        // ── Clonar caballos para que cada carrera arranque fresca ──
        List<Caballo> copias = new ArrayList<>();
        for (Caballo c : caballosDisponibles) {
            copias.add(c.clonar());
        }
 
        // El caballo del jugador debe ser la copia, no el original
        Caballo caballoCopia = null;
        for (Caballo c : copias) {
            if (c.getNombre().equals(caballoJugador.getNombre())) {
                caballoCopia = c;
                break;
            }
        }
        controllerJugador.seleccionarCaballo(caballoCopia);
 
        double distancia = 100.0;
 
        ctrlCarrera.crearCarrera(
            distancia,
            copias,
            controllerJugador.getJugador()
        );
        ctrlCarrera.iniciarCarrera();
 
        VentanaCarrera ventanaCarrera = new VentanaCarrera(
            ctrlCarrera,
            controllerJugador,
            distancia,
            copias
        );
        ventanaCarrera.setVisible(true);
        setVisible(false);
    }
 
    // ════════════════════════════════════════════════════════
    //  Helpers UI
    // ════════════════════════════════════════════════════════
    private void verificarHabilitacionInicio() {
        boolean jugadorCreado  = controllerJugador.obtenerNombreJugador() != null
                                 && !controllerJugador.obtenerNombreJugador().isEmpty();
        boolean caballoElegido = controllerJugador.obtenerCaballoSeleccionado() != null;
        btnIniciarCarrera.setEnabled(jugadorCreado && caballoElegido);
    }
 
    private void cargarCaballos() {
        caballosDisponibles = repoCaballo.listarTodos();
 
        if (caballosDisponibles == null || caballosDisponibles.isEmpty()) {
            caballosDisponibles = new ArrayList<>(java.util.Arrays.asList(
                new Caballo("Relámpago", 10.0,  80.0,  80.0, 0.0, "Veloz"),
                new Caballo("Tormenta",   7.0, 100.0, 100.0, 0.0, "Resistente"),
                new Caballo("Centella",   9.0,  90.0,  90.0, 0.0, "Equilibrado"),
                new Caballo("Trueno",     8.5,  85.0,  85.0, 0.0, "Equilibrado"),
                new Caballo("Vendaval",  11.0,  60.0,  60.0, 0.0, "Veloz")
            ));
        }
 
        modeloCaballos.clear();
        for (Caballo c : caballosDisponibles) {
            modeloCaballos.addElement(
                String.format("%-14s  [%s]", c.getNombre(), c.getperfil())
            );
        }
    }
 
    private JPanel crearCard(String id) {
        JPanel p = new JPanel();
        p.setBackground(C_CARD);
        p.setBorder(new CompoundBorder(
            new LineBorder(C_ACCENT2, 1),
            new EmptyBorder(6,6,6,6)));
        return p;
    }
 
    private JLabel label(String texto) {
        JLabel l = new JLabel(texto + ":");
        l.setForeground(C_MUTED);
        l.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return l;
    }
 
    private JTextField campo() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(20,20,30));
        tf.setForeground(C_TEXT);
        tf.setCaretColor(C_ACCENT);
        tf.setFont(new Font("Monospaced", Font.PLAIN, 13));
        tf.setBorder(new CompoundBorder(
            new LineBorder(C_ACCENT2, 1),
            new EmptyBorder(4,6,4,6)));
        return tf;
    }
 
    private JButton boton(String texto, Color bg, Color fg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Monospaced", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(new CompoundBorder(
            new LineBorder(bg.darker(), 1),
            new EmptyBorder(8,14,8,14)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(bg.brighter()); }
            @Override public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        return b;
    }
 
    private JLabel tituloSeccion(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(C_ACCENT);
        l.setFont(new Font("Monospaced", Font.BOLD, 12));
        return l;
    }
 
    private JSeparator separador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(C_ACCENT2);
        return sep;
    }
 
    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atención",
            JOptionPane.WARNING_MESSAGE);
    }
 
    // ════════════════════════════════════════════════════════
    //  main
    // ════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
 
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}