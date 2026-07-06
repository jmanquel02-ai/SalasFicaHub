package launcher;

import controlador.AppController;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada de SalasFICA.
 * Crea {@link AppController} en el hilo de Swing (EDT).
 *
 * @author Jonathan Manquel
 * @version 2.0
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AppController app = new AppController();
                app.iniciar();
            } catch (Exception e) {
                System.err.println("Error al iniciar SalasFICA: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
