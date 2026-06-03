package View;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class main {
	public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
 
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
	}
}

