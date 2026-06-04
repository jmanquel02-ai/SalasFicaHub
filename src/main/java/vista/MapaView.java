package vista;

import controlador.AppController;
import modelo.Ruta;
import modelo.Sala;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Vista principal del mapa del campus.
 * Renderiza dinámicamente la imagen de la ruta solicitada sobre el plano base.
 */
public class MapaView extends JFrame {

    private final AppController controller;
    private JLabel labelImagen;
    private JLabel labelRuta;
    private BufferedImage imagenActual;

    private static final String IMG_DEFAULT = "img/mapa_ufro.png";
    private static final String RUTAS_DIR = "img/rutas/";

    public MapaView(AppController controller) {
        this.controller = controller;
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setTitle("SalasFICA - Mapa del Campus");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                controller.salir();
            }
        });
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void construirUI() {
        labelImagen = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenActual != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    int pw = getWidth();
                    int ph = getHeight();
                    double ratio = Math.min((double) pw / imagenActual.getWidth(), (double) ph / imagenActual.getHeight());

                    int dw = (int) (imagenActual.getWidth() * ratio);
                    int dh = (int) (imagenActual.getHeight() * ratio);
                    int dx = (pw - dw) / 2;
                    int dy = (ph - dh) / 2;

                    g2.drawImage(imagenActual, dx, dy, dw, dh, null);
                } else {
                    g.setColor(new Color(200, 220, 200));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(new Color(100, 130, 100));
                    g.setFont(new Font("SansSerif", Font.PLAIN, 14));

                    String msg = "Imagen de ruta no disponible";
                    FontMetrics fm = g.getFontMetrics();
                    g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
                }
            }
        };
        labelImagen.setPreferredSize(new Dimension(900, 580));

        labelRuta = new JLabel("Seleccione un punto de inicio y una sala de destino", SwingConstants.CENTER);
        labelRuta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelRuta.setForeground(new Color(80, 100, 120));
        labelRuta.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JButton btnVolver = new JButton("Volver al Inicio");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> controller.mostrarBienvenida());

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(0, 8, 4, 8));
        footer.add(btnVolver, BorderLayout.WEST);
        footer.add(labelRuta, BorderLayout.CENTER);

        add(labelImagen, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        cargarImagen(IMG_DEFAULT);
    }

    /**
     * Resuelve y carga la imagen correspondiente a la ruta calculada.
     * Implementa un mecanismo de fallback: busca la imagen específica de la sala,
     * y si no existe, busca la imagen general del edificio.
     *
     * @param ruta La ruta calculada por el modelo.
     * @param sala La sala de destino.
     * @param origenId Identificador del punto de inicio.
     */
    public void mostrarRutaImagen(Ruta ruta, Sala sala, String origenId) {
        String edificioId = sala.getEdificioId();
        String salaCodigo = sala.getCodigo();

        String rutaImagenExacta = RUTAS_DIR + origenId + "-" + salaCodigo + ".png";
        boolean imagenCargada = cargarImagen(rutaImagenExacta);

        if (!imagenCargada) {
            String rutaImagenGeneral = RUTAS_DIR + origenId + "_" + edificioId + ".png";
            imagenCargada = cargarImagen(rutaImagenGeneral);
        }

        if (imagenCargada) {
            labelRuta.setText(String.format("Ruta: %s hacia Pabellón %s (Sala %s) | Distancia estimada: %.0f m.",
                    origenId, edificioId, salaCodigo, ruta.getDistanciaMetros()));
        } else {
            labelRuta.setText("Advertencia: Imagen cartográfica no encontrada para la ruta solicitada.");
            imagenActual = null;
            labelImagen.repaint();
        }
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Consulta", JOptionPane.ERROR_MESSAGE);
    }

    private boolean cargarImagen(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                return false;
            }
            imagenActual = ImageIO.read(is);
            labelImagen.repaint();
            return true;
        } catch (IOException e) {
            System.err.println("Excepción al cargar la imagen de ruta: " + e.getMessage());
            return false;
        }
    }

    public void mostrar() { setVisible(true); }
    public void ocultar() { setVisible(false); }
}