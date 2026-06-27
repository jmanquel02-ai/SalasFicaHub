package vista;

import controlador.AppController;
import modelo.Edificio;
import modelo.PuntoReferencia;
import modelo.Sala;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Pantalla de bienvenida con formulario de búsqueda en cascada.
 *
 * <p>Flujo de selección:</p>
 * <ol>
 *   <li>El usuario elige el <b>Punto de partida</b> (P1–P5).</li>
 *   <li>Elige el <b>Pabellón destino</b> (R, R1, R2, D, E, RA).</li>
 *   <li>La lista de <b>Salas</b> se filtra automáticamente según el pabellón.</li>
 *   <li>Presiona <b>Consultar Ruta</b>.</li>
 * </ol>
 *
 * @author Bastián Escobar
 * @version 3.0 — listas en cascada
 */
public class BienvenidaView extends JFrame {

    // ── Componentes ──────────────────────────────────────────────────────────
    private final AppController controller;

    /** Lista 1: punto de partida. */
    private JComboBox<String> comboPuntoOrigen;

    /** Lista 2: pabellón destino. */
    private JComboBox<String> comboPabellon;

    /** Lista 3: sala destino — se filtra al cambiar pabellón. */
    private JComboBox<String> comboSala;

    /** Botón principal de búsqueda. */
    private JButton btnBuscarRuta;

    /** Etiqueta de estado / error. */
    private JLabel labelEstado;

    /** Imagen de fondo del campus. */
    private BufferedImage imagenFondo;

    // ── Constructor ──────────────────────────────────────────────────────────

    public BienvenidaView(AppController controller) {
        this.controller = controller;
        inicializarRecursosVisuales();
        configurarVentana();
        construirInterfaz();
    }

    // ── Inicialización ────────────────────────────────────────────────────────

    private void inicializarRecursosVisuales() {
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("img/mapa_ufro.png")) {
            if (is != null) {
                BufferedImage original = ImageIO.read(is);
                int w = original.getWidth();
                int h = (int) (original.getHeight() * 0.58);
                imagenFondo = original.getSubimage(0, 0, w, h);
            }
        } catch (IOException e) {
            System.err.println("Fallo al cargar imagen de fondo: " + e.getMessage());
        }
    }

    private void configurarVentana() {
        setTitle("SalasFICA - Sistema de Orientación");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                controller.salir();
            }
        });
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    // ── Construcción de la interfaz ───────────────────────────────────────────

    private void construirInterfaz() {
        JPanel contenedor = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                if (imagenFondo != null) {
                    int pw = getWidth(), ph = getHeight();
                    double ri = (double) imagenFondo.getWidth() / imagenFondo.getHeight();
                    double rp = (double) pw / ph;
                    int dw, dh, dx, dy;
                    if (ri > rp) { dw=pw; dh=(int)(pw/ri); dx=0; dy=(ph-dh)/2; }
                    else         { dh=ph; dw=(int)(ph*ri); dx=(pw-dw)/2; dy=0; }
                    g2.drawImage(imagenFondo, dx, dy, dw, dh, null);
                    g2.setColor(new Color(0, 0, 0, 90));
                    g2.fillRect(0, 0, pw, ph);
                } else {
                    g2.setColor(new Color(30, 60, 100));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        contenedor.add(construirEncabezado(),  BorderLayout.NORTH);
        JPanel central = new JPanel(new GridBagLayout());
        central.setOpaque(false);
        central.add(construirFormulario());
        contenedor.add(central, BorderLayout.CENTER);
        contenedor.add(construirPie(), BorderLayout.SOUTH);
        add(contenedor, BorderLayout.CENTER);
    }

    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panel.setOpaque(false);
        JLabel titulo = new JLabel("SalasFICA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        JLabel sub = new JLabel("- Orientación Espacial Campus Andrés Bello");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sub.setForeground(new Color(220, 230, 240));
        panel.add(titulo);
        panel.add(sub);
        return panel;
    }

    /**
     * Construye el formulario con las tres listas en cascada.
     *
     * <p>La lista de salas se actualiza automáticamente cada vez que
     * el usuario cambia el pabellón seleccionado.</p>
     */
    private JPanel construirFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 190, 210), 1),
                new EmptyBorder(28, 40, 28, 40)
        ));
        panel.setPreferredSize(new Dimension(420, 360));

        // ── Título ───────────────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("Parámetros de Búsqueda");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(30, 60, 100));

        // ── Lista 1: Punto de partida ─────────────────────────────────────────
        JLabel lblOrigen = new JLabel("Punto de partida:");
        lblOrigen.setFont(new Font("SansSerif", Font.PLAIN, 12));

        comboPuntoOrigen = new JComboBox<>();
        comboPuntoOrigen.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboPuntoOrigen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        for (PuntoReferencia p : controller.getRepositorio().getPuntos()) {
            comboPuntoOrigen.addItem(p.getId() + " - " + p.getNombre());
        }

        // ── Lista 2: Pabellón ─────────────────────────────────────────────────
        JLabel lblPabellon = new JLabel("Pabellón destino:");
        lblPabellon.setFont(new Font("SansSerif", Font.PLAIN, 12));

        comboPabellon = new JComboBox<>();
        comboPabellon.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboPabellon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        for (Edificio e : controller.getRepositorio().getEdificios()) {
            comboPabellon.addItem(e.getId() + " — " + e.getNombre());
        }

        // ── Lista 3: Sala (filtrada por pabellón) ─────────────────────────────
        JLabel lblSala = new JLabel("Sala destino:");
        lblSala.setFont(new Font("SansSerif", Font.PLAIN, 12));

        comboSala = new JComboBox<>();
        comboSala.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboSala.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Carga inicial con el primer pabellón
        actualizarComboSalas();

        // Cuando cambia el pabellón → actualizar salas
        comboPabellon.addActionListener(e -> actualizarComboSalas());

        // ── Botón ─────────────────────────────────────────────────────────────
        btnBuscarRuta = new JButton("Consultar Ruta");
        btnBuscarRuta.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBuscarRuta.setBackground(new Color(25, 80, 160));
        btnBuscarRuta.setForeground(Color.WHITE);
        btnBuscarRuta.setFocusPainted(false);
        btnBuscarRuta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBuscarRuta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscarRuta.addActionListener(e -> procesarBusqueda());

        // ── Estado / error ────────────────────────────────────────────────────
        labelEstado = new JLabel(" ");
        labelEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelEstado.setForeground(new Color(180, 40, 40));

        // ── Alineación izquierda ──────────────────────────────────────────────
        for (JComponent c : new JComponent[]{
                lblTitulo, lblOrigen, comboPuntoOrigen,
                lblPabellon, comboPabellon,
                lblSala, comboSala,
                btnBuscarRuta, labelEstado}) {
            c.setAlignmentX(LEFT_ALIGNMENT);
        }

        // ── Ensamblado ────────────────────────────────────────────────────────
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(18));
        panel.add(lblOrigen);
        panel.add(Box.createVerticalStrut(4));
        panel.add(comboPuntoOrigen);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblPabellon);
        panel.add(Box.createVerticalStrut(4));
        panel.add(comboPabellon);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblSala);
        panel.add(Box.createVerticalStrut(4));
        panel.add(comboSala);
        panel.add(Box.createVerticalStrut(18));
        panel.add(btnBuscarRuta);
        panel.add(Box.createVerticalStrut(8));
        panel.add(labelEstado);

        return panel;
    }

    private JPanel construirPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(
            "Universidad de La Frontera | Facultad de Ingeniería y Ciencias | POO 2026"
        );
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(new Color(220, 220, 220));
        panel.add(lbl);
        return panel;
    }

    // ── Lógica de cascada ─────────────────────────────────────────────────────

    /**
     * Actualiza el combo de salas según el pabellón seleccionado.
     * Se llama al inicio y cada vez que el usuario cambia el pabellón.
     */
    private void actualizarComboSalas() {
        comboSala.removeAllItems();

        String seleccion = (String) comboPabellon.getSelectedItem();
        if (seleccion == null) return;

        // El id del pabellón está antes del " — "
        String idPabellon = seleccion.split(" — ")[0].trim();

        List<modelo.Sala> salas = controller.getRepositorio().getSalasPorEdificio(idPabellon);
        if (salas == null || salas.isEmpty()) {
            comboSala.addItem("— Sin salas disponibles —");
            return;
        }

        for (modelo.Sala s : salas) {
            comboSala.addItem(s.getCodigo() + "  (Piso " + s.getPiso() + ")");
        }
    }

    // ── Procesamiento de la búsqueda ──────────────────────────────────────────

    /**
     * Valida la selección y delega la búsqueda al controlador.
     */
    private void procesarBusqueda() {
        // Punto de origen
        String selOrigen = (String) comboPuntoOrigen.getSelectedItem();
        if (selOrigen == null) {
            labelEstado.setText("Error: selecciona un punto de partida.");
            return;
        }
        String idOrigen = selOrigen.split(" - ")[0].trim();

        // Sala destino
        String selSala = (String) comboSala.getSelectedItem();
        if (selSala == null || selSala.startsWith("—")) {
            labelEstado.setText("Error: selecciona una sala destino.");
            return;
        }
        // El código de la sala está antes del "  (Piso"
        String codigoSala = selSala.split("\\s+\\(")[0].trim();

        labelEstado.setText("Procesando ruta...");
        btnBuscarRuta.setEnabled(false);

        controller.iniciarBusqueda(idOrigen, codigoSala);

        btnBuscarRuta.setEnabled(true);
        labelEstado.setText(" ");
    }

    // ── Visibilidad ───────────────────────────────────────────────────────────

    public void mostrar() { setVisible(true); }
    public void ocultar() { setVisible(false); }
}
