package vista;

import controlador.AppController;
import modelo.PuntoReferencia;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interfaz gráfica inicial de la aplicación.
 * Gestiona la captura de parámetros de búsqueda por parte del usuario.
 */
public class BienvenidaView extends JFrame {

    private final AppController controller;
    private JComboBox<String> comboPuntoOrigen;
    private JTextField campoSalaDestino;
    private JButton btnBuscarRuta;
    private JLabel labelEstadoSistema;
    private BufferedImage imagenFondo;

    public BienvenidaView(AppController controller) {
        this.controller = controller;
        inicializarRecursosVisuales();
        configurarVentana();
        construirInterfaz();
    }

    private void inicializarRecursosVisuales() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("img/mapa_ufro.png")) {
            if (is != null) {
                BufferedImage original = ImageIO.read(is);
                int width = original.getWidth();
                int height = (int) (original.getHeight() * 0.58);
                imagenFondo = original.getSubimage(0, 0, width, height);
            }
        } catch (IOException e) {
            System.err.println("Fallo al inicializar recurso de fondo: " + e.getMessage());
        }
    }

    private void configurarVentana() {
        setTitle("SalasFICA - Sistema de Orientación");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                controller.salir();
            }
        });
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void construirInterfaz() {
        JPanel panelContenedor = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                if (imagenFondo != null) {
                    int pw = getWidth();
                    int ph = getHeight();
                    double ratioImg = (double) imagenFondo.getWidth() / imagenFondo.getHeight();
                    double ratioPan = (double) pw / ph;

                    int drawW, drawH, drawX, drawY;
                    if (ratioImg > ratioPan) {
                        drawW = pw;
                        drawH = (int) (pw / ratioImg);
                        drawX = 0;
                        drawY = (ph - drawH) / 2;
                    } else {
                        drawH = ph;
                        drawW = (int) (ph * ratioImg);
                        drawX = (pw - drawW) / 2;
                        drawY = 0;
                    }
                    g2.drawImage(imagenFondo, drawX, drawY, drawW, drawH, null);
                    g2.setColor(new Color(0, 0, 0, 90));
                    g2.fillRect(0, 0, pw, ph);
                } else {
                    g2.setColor(new Color(30, 60, 100));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        panelContenedor.add(construirEncabezado(), BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        panelCentral.add(construirFormularioBusqueda());
        panelContenedor.add(panelCentral, BorderLayout.CENTER);

        panelContenedor.add(construirPieDePagina(), BorderLayout.SOUTH);
        add(panelContenedor, BorderLayout.CENTER);
    }

    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panel.setOpaque(false);

        JLabel titulo = new JLabel("SalasFICA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("- Orientación Espacial Campus Andrés Bello");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(220, 230, 240));

        panel.add(titulo);
        panel.add(subtitulo);
        return panel;
    }

    private JPanel construirFormularioBusqueda() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 190, 210), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));
        panel.setPreferredSize(new Dimension(400, 280));

        JLabel lblTitulo = new JLabel("Parámetros de Búsqueda");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(30, 60, 100));

        JLabel lblOrigen = new JLabel("Punto de partida:");
        lblOrigen.setFont(new Font("SansSerif", Font.PLAIN, 12));

        comboPuntoOrigen = new JComboBox<>();
        comboPuntoOrigen.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboPuntoOrigen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        for (PuntoReferencia p : controller.getRepositorio().getPuntos()) {
            comboPuntoOrigen.addItem(p.getId() + " - " + p.getNombre());
        }

        JLabel lblDestino = new JLabel("Código de dependencia (Sala):");
        lblDestino.setFont(new Font("SansSerif", Font.PLAIN, 12));

        campoSalaDestino = new JTextField();
        campoSalaDestino.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoSalaDestino.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campoSalaDestino.setForeground(Color.GRAY);
        campoSalaDestino.setText("Ejemplo: R101, E201");

        campoSalaDestino.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoSalaDestino.getForeground() == Color.GRAY) {
                    campoSalaDestino.setText("");
                    campoSalaDestino.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campoSalaDestino.getText().trim().isEmpty()) {
                    campoSalaDestino.setForeground(Color.GRAY);
                    campoSalaDestino.setText("Ejemplo: R101, E201");
                }
            }
        });
        campoSalaDestino.addActionListener(e -> procesarSolicitudBusqueda());

        btnBuscarRuta = new JButton("Consultar Ruta");
        btnBuscarRuta.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBuscarRuta.setBackground(new Color(25, 80, 160));
        btnBuscarRuta.setForeground(Color.WHITE);
        btnBuscarRuta.setFocusPainted(false);
        btnBuscarRuta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBuscarRuta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscarRuta.addActionListener(e -> procesarSolicitudBusqueda());

        labelEstadoSistema = new JLabel(" ");
        labelEstadoSistema.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelEstadoSistema.setForeground(new Color(180, 40, 40));

        // Alineación a la izquierda para todos los componentes
        for (Component c : new Component[]{lblTitulo, lblOrigen, comboPuntoOrigen, lblDestino, campoSalaDestino, btnBuscarRuta, labelEstadoSistema}) {
            ((JComponent) c).setAlignmentX(LEFT_ALIGNMENT);
        }

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblOrigen);
        panel.add(Box.createVerticalStrut(5));
        panel.add(comboPuntoOrigen);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblDestino);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campoSalaDestino);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnBuscarRuta);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelEstadoSistema);

        return panel;
    }

    private JPanel construirPieDePagina() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        JLabel lblPie = new JLabel("Universidad de La Frontera | Facultad de Ingeniería y Ciencias | POO 2026");
        lblPie.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblPie.setForeground(new Color(220, 220, 220));
        panel.add(lblPie);
        return panel;
    }

    private void procesarSolicitudBusqueda() {
        String codigoSala = campoSalaDestino.getText().trim().toUpperCase();

        if (codigoSala.isEmpty() || campoSalaDestino.getForeground() == Color.GRAY) {
            labelEstadoSistema.setText("Error: El campo de destino es obligatorio.");
            return;
        }

        Object seleccionMapeada = comboPuntoOrigen.getSelectedItem();
        if (seleccionMapeada == null) {
            labelEstadoSistema.setText("Error: Seleccione un punto de partida válido.");
            return;
        }

        labelEstadoSistema.setText("Procesando solicitud de ruta...");
        btnBuscarRuta.setEnabled(false);

        String idOrigen = seleccionMapeada.toString().split(" - ")[0].trim();

        controller.iniciarBusqueda(idOrigen, codigoSala);

        btnBuscarRuta.setEnabled(true);
        labelEstadoSistema.setText(" ");
    }

    public void mostrar() { setVisible(true); }
    public void ocultar() { setVisible(false); }
}