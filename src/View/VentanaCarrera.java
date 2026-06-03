package View;

import Controlador.ControllerJugador;
import Controlador.controllerCarrera;
import Modelo1.Caballo;
import repositorio.carrerarepositorio;
import repositorio.jugadorrepositorio;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VentanaCarrera — Vista MVC
 * Muestra la animación de la carrera usando javax.swing.Timer.
 * Se comunica SOLO con controllerCarrera y ControllerJugador (nunca con el Modelo directamente).
 */
public class VentanaCarrera extends JFrame {

    // ── controladores ────────────────────────────────────────
    private controllerCarrera  ctrlCarrera;
    private ControllerJugador  ctrlJugador;

    // ── repositorios (para persistir al finalizar) ───────────
    private carrerarepositorio repoCarrera;
    private jugadorrepositorio repoJugador;

    // ── datos de carrera ─────────────────────────────────────
    private double distanciaTotal;
    private List<Caballo> caballos;

    // ── animación ────────────────────────────────────────────
    private Timer timer;
    private static final int DELAY_MS = 90;  

    // ── UI ───────────────────────────────────────────────────
    private PistaPanel pistaPanel;
    private JLabel     lblEstado;
    private JLabel     lblGanador;
    private JLabel     lblPuntaje;
    private JButton    btnVolver;
    private JButton    btnReiniciar;

    // ── colores ──────────────────────────────────────────────
    private static final Color C_BG      = new Color(10, 20, 10);
    private static final Color C_PANEL   = new Color(18, 32, 18);
    private static final Color C_CARD    = new Color(25, 42, 25);
    private static final Color C_ACCENT  = new Color(212, 175, 55);
    private static final Color C_ACCENT2 = new Color(140, 100, 20);
    private static final Color C_TEXT    = new Color(230, 225, 210);
    private static final Color C_MUTED   = new Color(120, 130, 110);
    private static final Color C_GREEN   = new Color(80, 200, 120);
    private static final Color C_RED     = new Color(220, 80, 80);

    // Colores para cada carril de caballo
    private static final Color[] HORSE_COLORS = {
        new Color(220, 160, 50),
        new Color(80,  160, 220),
        new Color(200, 80,  80),
        new Color(80,  200, 120),
        new Color(180, 80,  200)
    };

    // ════════════════════════════════════════════════════════
    //  Constructor
    // ════════════════════════════════════════════════════════
    public VentanaCarrera(
            controllerCarrera ctrlCarrera,
            ControllerJugador ctrlJugador,
            double distanciaTotal,
            List<Caballo> caballos) {

        this.ctrlCarrera    = ctrlCarrera;
        this.ctrlJugador    = ctrlJugador;
        this.distanciaTotal = distanciaTotal;
        this.caballos       = caballos;

        repoCarrera = new carrerarepositorio();
        repoJugador = new jugadorrepositorio();

        configurarVentana();
        construirUI();
        iniciarAnimacion();
    }

    // ════════════════════════════════════════════════════════
    //  Configuración
    // ════════════════════════════════════════════════════════
    private void configurarVentana() {
        setTitle("🏁 Carrera en curso");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_BG);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (timer != null) timer.stop();
                System.exit(0);
            }
        });
    }

    // ════════════════════════════════════════════════════════
    //  UI
    // ════════════════════════════════════════════════════════
    private void construirUI() {
        setLayout(new BorderLayout(0, 0));
        add(crearHeader(),  BorderLayout.NORTH);
        add(crearPista(),   BorderLayout.CENTER);
        add(crearFooter(),  BorderLayout.SOUTH);
    }

    private JPanel crearHeader() {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(10, 30, 10));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        h.setPreferredSize(new Dimension(0, 60));
        h.setBorder(new MatteBorder(0, 0, 2, 0, C_ACCENT));
        h.setOpaque(false);

        JLabel titulo = new JLabel("  🏇  CARRERA EN VIVO");
        titulo.setFont(new Font("Serif", Font.BOLD, 22));
        titulo.setForeground(C_ACCENT);

        lblEstado = new JLabel("EN CURSO...  ");
        lblEstado.setFont(new Font("Monospaced", Font.BOLD, 14));
        lblEstado.setForeground(C_GREEN);

        h.add(titulo,    BorderLayout.WEST);
        h.add(lblEstado, BorderLayout.EAST);
        return h;
    }

    private JPanel crearPista() {
        JPanel contenedor = new JPanel(new BorderLayout(0, 10));
        contenedor.setBackground(C_BG);
        contenedor.setBorder(new EmptyBorder(12, 12, 8, 12));

        pistaPanel = new PistaPanel();
        contenedor.add(pistaPanel, BorderLayout.CENTER);

        // Panel lateral con info de posiciones
        contenedor.add(crearPanelPosiciones(), BorderLayout.EAST);

        return contenedor;
    }

    private JPanel crearPanelPosiciones() {
        JPanel p = new JPanel();
        p.setBackground(C_CARD);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new CompoundBorder(
            new LineBorder(C_ACCENT2, 1),
            new EmptyBorder(10, 10, 10, 10)));
        p.setPreferredSize(new Dimension(190, 0));

        JLabel titulo = new JLabel("POSICIONES");
        titulo.setForeground(C_ACCENT);
        titulo.setFont(new Font("Monospaced", Font.BOLD, 12));
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        p.add(titulo);
        p.add(Box.createVerticalStrut(8));

        for (int i = 0; i < caballos.size(); i++) {
            Caballo c = caballos.get(i);
            boolean esJugador = c.equals(ctrlJugador.obtenerCaballoSeleccionado());

            JLabel lbl = new JLabel(labelCaballo(c, esJugador));
            lbl.setForeground(HORSE_COLORS[i % HORSE_COLORS.length]);
            lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            lbl.setName("pos_" + i);
            p.add(lbl);
            p.add(Box.createVerticalStrut(4));
        }

        p.add(Box.createVerticalGlue());

        // Labels de resultado (ocultos hasta el final)
        lblGanador = new JLabel(" ");
        lblGanador.setForeground(C_ACCENT);
        lblGanador.setFont(new Font("Monospaced", Font.BOLD, 11));
        lblGanador.setAlignmentX(LEFT_ALIGNMENT);

        lblPuntaje = new JLabel(" ");
        lblPuntaje.setForeground(C_GREEN);
        lblPuntaje.setFont(new Font("Monospaced", Font.BOLD, 12));
        lblPuntaje.setAlignmentX(LEFT_ALIGNMENT);

        p.add(new JSeparator() {{ setForeground(C_ACCENT2); setMaximumSize(new Dimension(170, 2)); }});
        p.add(Box.createVerticalStrut(6));
        p.add(lblGanador);
        p.add(Box.createVerticalStrut(4));
        p.add(lblPuntaje);

        return p;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        footer.setBackground(new Color(10, 25, 10));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, C_ACCENT2));

        btnReiniciar = crearBoton("↺  NUEVA CARRERA", C_ACCENT2, C_TEXT);
        btnReiniciar.setEnabled(false);
        btnReiniciar.addActionListener(e -> accionNuevaCarrera());

        btnVolver = crearBoton("← VOLVER AL MENÚ", C_MUTED, C_BG);
        btnVolver.addActionListener(e -> accionVolver());

        footer.add(btnReiniciar);
        footer.add(btnVolver);
        return footer;
    }

    // ════════════════════════════════════════════════════════
    //  Animación con Timer
    // ════════════════════════════════════════════════════════
    private void iniciarAnimacion() {
        timer = new Timer(DELAY_MS, e -> tick());
        timer.start();
    }

    private void tick() {
        // 1. Pedir al controlador que simule un turno
        ctrlCarrera.simularTurno();
        
        for (Caballo c : ctrlCarrera.obtenerCaballos()) {
            System.out.println(c.getNombre() + 
                " | dist: " + c.getDistanciaRecorrida() + 
                " | energia: " + c.getEnergiaActual());
        }
        System.out.println("Finalizada: " + ctrlCarrera.carreraFinalizada());
        System.out.println("---");
        pistaPanel.repaint();

        // 3. Actualizar labels de posición en el panel lateral
        actualizarPosiciones();

        // 4. Verificar si terminó
        if (ctrlCarrera.carreraFinalizada()) {
            timer.stop();
            finalizarCarrera();
        }
    }

    private void actualizarPosiciones() {
        // Actualizar cada label de posición
        Container panelPos = (Container) ((BorderLayout) ((JPanel) getContentPane()
            .getComponent(1)).getLayout())
            .getLayoutComponent(BorderLayout.EAST);

        if (panelPos == null) return;

        int idx = 0;
        for (Component comp : panelPos.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (lbl.getName() != null && lbl.getName().startsWith("pos_")) {
                    int i = Integer.parseInt(lbl.getName().substring(4));
                    if (i < caballos.size()) {
                        Caballo c = caballos.get(i);
                        boolean esJugador = c.equals(ctrlJugador.obtenerCaballoSeleccionado());
                        lbl.setText(labelCaballo(c, esJugador));
                    }
                }
            }
        }
    }

    private void finalizarCarrera() {
        Caballo ganador = ctrlCarrera.obtenerGanador();
        int puntaje     = ctrlCarrera.calcularPuntajeJugador();

        // Sumar puntaje al jugador
        ctrlJugador.getJugador().sumarPuntos(puntaje);

        // Persistir
        try {
            repoCarrera.guardar(ctrlCarrera.getCarrera());
            repoJugador.guardarJugador(ctrlJugador.getJugador());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Actualizar UI
        lblEstado.setText("FINALIZADA  ");
        lblEstado.setForeground(C_ACCENT);

        lblGanador.setText("🏆 " + (ganador != null ? ganador.getNombre() : "?"));
        lblPuntaje.setText("+" + puntaje + " pts  →  "
            + ctrlJugador.obtenerPuntaje() + " total");

        btnReiniciar.setEnabled(true);

        // Diálogo de resultado
        String jugadorCaballo = ctrlJugador.obtenerCaballoSeleccionado() != null
            ? ctrlJugador.obtenerCaballoSeleccionado().getNombre() : "?";

        boolean gano = ganador != null &&
            ganador.equals(ctrlJugador.obtenerCaballoSeleccionado());

        String mensaje = (gano ? "🏆 ¡GANASTE!\n" : "😔 Tu caballo no ganó esta vez.\n")
            + "\nGanador: " + (ganador != null ? ganador.getNombre() : "?")
            + "\nTu caballo: " + jugadorCaballo
            + "\nPuntos obtenidos: +" + puntaje
            + "\nPuntaje total: " + ctrlJugador.obtenerPuntaje();

        JOptionPane.showMessageDialog(this, mensaje,
            gano ? "¡Victoria!" : "Resultado final",
            gano ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.PLAIN_MESSAGE);

        pistaPanel.repaint();
    }

    // ════════════════════════════════════════════════════════
    //  Acciones de botones
    // ════════════════════════════════════════════════════════
    private void accionNuevaCarrera() {
        // Reiniciar carrera con los mismos caballos
        ctrlCarrera.crearCarrera(
            distanciaTotal,
            caballos,
            ctrlJugador.getJugador()
        );
        ctrlCarrera.iniciarCarrera();

        lblEstado.setText("EN CURSO...  ");
        lblEstado.setForeground(C_GREEN);
        lblGanador.setText(" ");
        lblPuntaje.setText(" ");
        btnReiniciar.setEnabled(false);

        iniciarAnimacion();
    }

    private void accionVolver() {
        if (timer != null) timer.stop();
        dispose();
        new VentanaPrincipal(ctrlJugador).setVisible(true);
    }

    // ════════════════════════════════════════════════════════
    //  Helpers
    // ════════════════════════════════════════════════════════
    private String labelCaballo(Caballo c, boolean esJugador) {
        double pct = Math.min(100.0,
            (c.getDistanciaRecorrida() / distanciaTotal) * 100);
        return String.format("%s%-10s %4.0f%%",
            esJugador ? "★ " : "  ",
            c.getNombre().length() > 9
                ? c.getNombre().substring(0, 9) : c.getNombre(),
            pct);
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Monospaced", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(new CompoundBorder(
            new LineBorder(bg.darker(), 1),
            new EmptyBorder(7, 14, 7, 14)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ════════════════════════════════════════════════════════
    //  Panel de la pista (pintado manual)
    // ════════════════════════════════════════════════════════
    private class PistaPanel extends JPanel {

        private static final int MARGEN_IZQ  = 100;
        private static final int MARGEN_DER  = 30;
        private static final int ALTO_CARRIL = 60;
        private static final int PAD_VERT    = 10;

        public PistaPanel() {
            setBackground(new Color(20, 50, 20));
            setBorder(new LineBorder(C_ACCENT2, 1));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int n = caballos.size();

            // Fondo de hierba con líneas
            dibujarFondo(g2, w, h, n);

            // Línea de meta
            int xMeta = w - MARGEN_DER;
            g2.setColor(C_RED);
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{8, 5}, 0));
            g2.drawLine(xMeta, 0, xMeta, h);
            g2.setStroke(new BasicStroke(1));

            // Etiqueta META
            g2.setColor(C_RED);
            g2.setFont(new Font("Monospaced", Font.BOLD, 11));
            g2.drawString("META", xMeta - 18, 14);

            // Dibujar cada caballo
            int pistasAncho = w - MARGEN_IZQ - MARGEN_DER;
            for (int i = 0; i < n; i++) {
                Caballo c = caballos.get(i);
                int cy = PAD_VERT + i * ALTO_CARRIL + ALTO_CARRIL / 2;

                double progreso = Math.min(1.0,
                    c.getDistanciaRecorrida() / distanciaTotal);
                int cx = MARGEN_IZQ + (int)(progreso * pistasAncho);

                Color color = HORSE_COLORS[i % HORSE_COLORS.length];
                boolean esJugador = c.equals(ctrlJugador.obtenerCaballoSeleccionado());

                dibujarCaballo(g2, cx, cy, color, c.getNombre(), esJugador);
            }

            // Nombre de jugador a la izquierda
            g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
            for (int i = 0; i < n; i++) {
                Caballo c = caballos.get(i);
                int cy = PAD_VERT + i * ALTO_CARRIL + ALTO_CARRIL / 2;
                boolean esJugador = c.equals(ctrlJugador.obtenerCaballoSeleccionado());

                g2.setColor(esJugador ? C_ACCENT : C_MUTED);
                String tag = (esJugador ? "★ " : "  ") + c.getNombre();
                g2.drawString(tag, 4, cy + 4);
            }
        }

        private void dibujarFondo(Graphics2D g2, int w, int h, int n) {
            for (int i = 0; i < n; i++) {
                Color base = i % 2 == 0
                    ? new Color(25, 60, 25)
                    : new Color(20, 50, 20);
                g2.setColor(base);
                int y = PAD_VERT + i * ALTO_CARRIL;
                g2.fillRect(0, y, w, ALTO_CARRIL);

                // Línea divisoria de carril
                g2.setColor(new Color(50, 80, 50));
                g2.drawLine(0, y, w, y);
            }

            // Línea de largada
            g2.setColor(C_ACCENT2);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(MARGEN_IZQ, 0, MARGEN_IZQ, h);
            g2.setStroke(new BasicStroke(1));
        }

        private void dibujarCaballo(Graphics2D g2, int cx, int cy,
                                     Color color, String nombre,
                                     boolean esJugador) {
         
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillOval(cx - 16, cy + 12, 36, 8);

            
            g2.setColor(color);
            // Cuerpo
            g2.fillOval(cx - 18, cy - 10, 36, 20);
            // Cabeza
            g2.fillOval(cx + 14, cy - 14, 16, 14);
            // Cuello
            g2.fillRect(cx + 12, cy - 10, 6, 10);
            // Patas (4 rectángulos)
            g2.fillRect(cx - 12, cy + 8, 5, 10);
            g2.fillRect(cx - 2,  cy + 8, 5, 10);
            g2.fillRect(cx + 8,  cy + 8, 5, 10);
            g2.fillRect(cx + 17, cy + 6, 5, 10);
            // Cola
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(cx - 26, cy - 8, 14, 16, 270, 180);
            g2.setStroke(new BasicStroke(1));

            // Contorno si es el caballo del jugador
            if (esJugador) {
                g2.setColor(C_ACCENT);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(cx - 19, cy - 11, 38, 22);
                g2.setStroke(new BasicStroke(1));

                // Estrella encima
                g2.setFont(new Font("Dialog", Font.PLAIN, 14));
                g2.drawString("★", cx - 6, cy - 16);
            }

            // Energía (barra pequeña)
            // (se puede leer de c, pero por simplicidad omitimos getter de energía aquí)
        }
    }
}
