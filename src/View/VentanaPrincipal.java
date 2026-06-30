package View;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import Controlador.*;
import Dto.*;

public class VentanaCarrera extends JFrame {
    private final ControllerCarrera carreraController = ControllerCarrera.getInstancia();
    private final ControllerJugador jugadorController = ControllerJugador.getInstancia();
    private final JLabel estado = new JLabel("EN CURSO");
    private final JLabel resultado = new JLabel(" ");
    private final PistaPanel pista = new PistaPanel();
    private final Timer timer;
    private CarreraDTO carrera;

    public VentanaCarrera() {
        super("Carrera en curso");
        carrera = carreraController.obtenerEstado();
        setSize(960,620); setLocationRelativeTo(null); setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        construirUI(); timer = new Timer(90, e -> avanzar());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                timer.stop(); jugadorController.cerrarAplicacion(); dispose(); System.exit(0);
            }
        });
        timer.start();
    }

    private void construirUI() {
        JPanel raiz = new JPanel(new BorderLayout(10,10)); raiz.setBackground(new Color(10,24,14));
        raiz.setBorder(BorderFactory.createEmptyBorder(12,12,12,12)); setContentPane(raiz);
        JPanel superior = new JPanel(new BorderLayout()); superior.setOpaque(false);
        JLabel titulo = new JLabel("CARRERA EN VIVO"); titulo.setForeground(new Color(225,184,72));
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD,20f)); estado.setForeground(new Color(90,220,130));
        superior.add(titulo, BorderLayout.WEST); superior.add(estado, BorderLayout.EAST); raiz.add(superior, BorderLayout.NORTH);
        raiz.add(pista, BorderLayout.CENTER);
        JPanel inferior = new JPanel(new BorderLayout()); inferior.setOpaque(false); resultado.setForeground(Color.WHITE);
        JButton volver = new JButton("VOLVER AL MENÚ"); volver.addActionListener(e -> volver());
        inferior.add(resultado, BorderLayout.WEST); inferior.add(volver, BorderLayout.EAST); raiz.add(inferior, BorderLayout.SOUTH);
    }

    private void avanzar() {
        carrera = carreraController.simularTurno(); pista.repaint(); if (!carrera.finalizada()) return;
        timer.stop(); estado.setText("FINALIZADA"); estado.setForeground(new Color(225,184,72));
        resultado.setText("Ganador: " + carrera.ganador() + " | +" + carrera.puntosObtenidos() + " puntos | Total: " + carrera.puntajeTotal());
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                "Ganador: " + carrera.ganador() + "\nPuntos obtenidos: " + carrera.puntosObtenidos()
                + "\nPuntaje acumulado: " + carrera.puntajeTotal(), "Resultado", JOptionPane.INFORMATION_MESSAGE));
    }
    private void volver() {
        timer.stop(); jugadorController.persistirSesion(); dispose(); new VentanaPrincipal().setVisible(true);
    }

    private final class PistaPanel extends JPanel {
        private final Image caballoImagen;
        private PistaPanel() {
            setBackground(new Color(24,74,36)); setBorder(BorderFactory.createLineBorder(new Color(155,113,38),2));
            URL recurso = VentanaCarrera.class.getResource("/images/caballo.png");
            caballoImagen = recurso == null ? null : new ImageIcon(recurso).getImage();
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cantidad = carrera.caballos().size(); int altoCarril = Math.max(65,getHeight()/Math.max(1,cantidad));
            int inicio = 120, meta = getWidth()-80;
            g2.setColor(Color.WHITE); g2.setStroke(new BasicStroke(3,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1,new float[]{8,6},0));
            g2.drawLine(meta,0,meta,getHeight());
            for (int i=0; i<cantidad; i++) {
                CaballoDTO c = carrera.caballos().get(i); int y=i*altoCarril;
                g2.setColor(i%2==0 ? new Color(35,92,45) : new Color(29,82,40)); g2.fillRect(0,y,getWidth(),altoCarril);
                double progreso=Math.min(1,c.distanciaRecorrida()/carrera.distanciaTotal()); int x=inicio+(int)((meta-inicio-58)*progreso); int cy=y+altoCarril/2;
                g2.setColor(c.seleccionado()?new Color(255,212,74):Color.WHITE);
                g2.drawString((c.seleccionado()?"★ ":"")+c.nombre(),8,cy+5);
                if (caballoImagen!=null) g2.drawImage(caballoImagen,x,cy-25,72,50,null);
                else { g2.setColor(new Color(176,100,40)); g2.fillOval(x,cy-14,55,28); }
            }
            g2.dispose();
        }
    }
}
