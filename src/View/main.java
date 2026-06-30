package View;

import javax.swing.*;

public final class main {
    private main() {}
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}

