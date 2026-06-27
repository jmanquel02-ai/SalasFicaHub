package vista;

import controlador.AppController;
import modelo.Nodo;
import modelo.Ruta;
import modelo.Sala;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.cache.FileBasedLocalCache;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Vista del mapa del campus UFRO con la ruta dibujada sobre OpenStreetMap.
 *
 * <p>Usa JXMapViewer2 (Maven Central) para mostrar tiles reales de
 * OpenStreetMap — los mismos que se ven en openstreetmap.org —
 * y dibuja encima:</p>
 * <ul>
 *   <li>Marcador verde en el punto de origen.</li>
 *   <li>Marcador rojo en la sala destino.</li>
 *   <li>Polilínea azul siguiendo los nodos del camino calculado.</li>
 * </ul>
 *
 * <p>Los tiles se cachean localmente en ~/.jxmapviewer2/ para que
 * el mapa cargue más rápido en usos sucesivos.</p>
 *
 * <p>Esta clase NO calcula rutas. Recibe una {@link Ruta} ya calculada
 * por {@link controlador.BusquedaController} y la visualiza.</p>
 *
 * @author Bastián Escobar
 * @version 4.0 — JXMapViewer2 (Maven Central) + caché local
 */
public class MapaView extends JFrame {

    // ── Constantes del campus UFRO ───────────────────────────────────────────
    /** Latitud central del campus Andrés Bello. */
    private static final double LAT_UFRO = -38.7474;

    /** Longitud central del campus Andrés Bello. */
    private static final double LON_UFRO = -72.6161;

    /**
     * Zoom inicial.
     * En JXMapViewer2: 1 = más acercado, 17 = más alejado.
     * Valor 1 muestra edificios individuales (equivale a zoom 18 en OSM).
     */
    private static final int ZOOM_INIT = 1;

    // ── Componentes ──────────────────────────────────────────────────────────
    /** Panel principal del mapa (JXMapViewer2 con tiles OSM reales). */
    private JXMapViewer mapa;

    /** Etiqueta con código y datos de la sala destino. */
    private JLabel lblSalaDestino;

    /** Etiqueta con la distancia estimada. */
    private JLabel lblDistancia;

    /** Lista con los pasos de navegación. */
    private JList<String> listaPasos;

    /** Controlador principal de la aplicación. */
    private final AppController controller;

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Crea la ventana del mapa centrada en el campus UFRO.
     *
     * @param controller controlador principal que coordina las vistas
     */
    public MapaView(AppController controller) {
        this.controller = controller;
        configurarVentana();
        construirComponentes();
    }

    // ── Configuración ─────────────────────────────────────────────────────────

    private void configurarVentana() {
        setTitle("SalasFICA - Mapa del Campus");
        setSize(1100, 720);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                controller.salir();
            }
        });
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
    }

    private void construirComponentes() {

        // ── Mapa JXMapViewer2 con tiles OSM reales ───────────────────────────
        TileFactoryInfo info      = new OSMTileFactoryInfo();
        DefaultTileFactory factory = new DefaultTileFactory(info);
        factory.setThreadPoolSize(4);

        // Caché local: evita descargar el mismo tile dos veces
        File cacheDir = new File(
            System.getProperty("user.home") + File.separator + ".jxmapviewer2"
        );
        factory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        mapa = new JXMapViewer();
        mapa.setTileFactory(factory);
        mapa.setAddressLocation(new GeoPosition(LAT_UFRO, LON_UFRO));
        mapa.setZoom(ZOOM_INIT);

        // ── Panel lateral ────────────────────────────────────────────────────
        JPanel panelLateral = new JPanel(new BorderLayout(0, 8));
        panelLateral.setPreferredSize(new Dimension(250, 0));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(14, 12, 12, 12));
        panelLateral.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Destino");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTitulo.setForeground(new Color(100, 100, 100));

        lblSalaDestino = new JLabel("—");
        lblSalaDestino.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblSalaDestino.setForeground(new Color(20, 20, 20));

        lblDistancia = new JLabel(" ");
        lblDistancia.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDistancia.setForeground(new Color(110, 110, 110));

        JPanel encabezado = new JPanel(new GridLayout(3, 1, 0, 3));
        encabezado.setBackground(Color.WHITE);
        encabezado.add(lblTitulo);
        encabezado.add(lblSalaDestino);
        encabezado.add(lblDistancia);

        JLabel lblPasos = new JLabel("Instrucciones");
        lblPasos.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPasos.setForeground(new Color(100, 100, 100));
        lblPasos.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));

        listaPasos = new JList<>(new DefaultListModel<>());
        listaPasos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        listaPasos.setCellRenderer(new PasoRenderer());
        listaPasos.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(listaPasos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel centroPasos = new JPanel(new BorderLayout(0, 4));
        centroPasos.setBackground(Color.WHITE);
        centroPasos.add(lblPasos, BorderLayout.NORTH);
        centroPasos.add(scroll,   BorderLayout.CENTER);

        JButton btnVolver = new JButton("← Nueva búsqueda");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> controller.mostrarBienvenida());

        panelLateral.add(encabezado,  BorderLayout.NORTH);
        panelLateral.add(centroPasos, BorderLayout.CENTER);
        panelLateral.add(btnVolver,   BorderLayout.SOUTH);

        // ── Barra inferior de atribución ─────────────────────────────────────
        JLabel lblCredito = new JLabel(
            "  Mapa: © OpenStreetMap contributors (tile.openstreetmap.org)  |  JXMapViewer2"
        );
        lblCredito.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCredito.setForeground(new Color(120, 120, 120));
        lblCredito.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));

        JPanel barraEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        barraEstado.setBackground(new Color(242, 242, 242));
        barraEstado.setBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210))
        );
        barraEstado.add(lblCredito);

        add(mapa,         BorderLayout.CENTER);
        add(panelLateral, BorderLayout.EAST);
        add(barraEstado,  BorderLayout.SOUTH);
    }

    // ── Método principal público ──────────────────────────────────────────────

    /**
     * Dibuja la ruta calculada sobre el mapa OpenStreetMap.
     *
     * <ol>
     *   <li>Construye la polilínea azul con todos los nodos del camino.</li>
     *   <li>Coloca marcador verde en el origen y rojo en el destino.</li>
     *   <li>Centra el mapa en el punto medio de la ruta con zoom 1.</li>
     *   <li>Actualiza el panel lateral con sala, distancia e instrucciones.</li>
     * </ol>
     *
     * @param ruta     ruta calculada con la lista de nodos
     * @param sala     sala destino
     * @param origenId ID del punto de partida (para la etiqueta)
     */
    public void mostrarRutaImagen(Ruta ruta, Sala sala, String origenId) {
        List<Nodo> camino = ruta.getCamino();
        if (camino == null || camino.isEmpty()) {
            mostrarError("La ruta calculada está vacía.");
            return;
        }

        // ── Polilínea de la ruta ─────────────────────────────────────────────
        List<GeoPosition> puntosRuta = new ArrayList<>();
        for (Nodo n : camino)
            puntosRuta.add(new GeoPosition(n.getLatitud(), n.getLongitud()));

        RoutePainter rutaPainter = new RoutePainter(puntosRuta);

        // ── Marcadores origen (verde) y destino (rojo) ───────────────────────
        // Se usa MarcadorPainter directo (implementa Painter<JXMapViewer>)
        // para evitar el doble indicador que genera WaypointPainter
        // al superponer su ícono por defecto sobre el renderer personalizado.
        Nodo nOrigen  = camino.get(0);
        Nodo nDestino = camino.get(camino.size() - 1);

        GeoPosition posOrigen  = new GeoPosition(
            nOrigen.getLatitud(),  nOrigen.getLongitud()
        );
        GeoPosition posDestino = new GeoPosition(
            nDestino.getLatitud(), nDestino.getLongitud()
        );

        MarcadorPainter marcadorPainter = new MarcadorPainter(posOrigen, posDestino);

        // ── Combinar painters y aplicar ──────────────────────────────────────
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(rutaPainter);
        painters.add(marcadorPainter);
        mapa.setOverlayPainter(new CompoundPainter<>(painters));

        // ── Centrar en punto medio de la ruta ────────────────────────────────
        double latMedia = (nOrigen.getLatitud()  + nDestino.getLatitud())  / 2.0;
        double lonMedia = (nOrigen.getLongitud() + nDestino.getLongitud()) / 2.0;
        mapa.setAddressLocation(new GeoPosition(latMedia, lonMedia));
        mapa.setZoom(ZOOM_INIT);

        // ── Panel lateral ────────────────────────────────────────────────────
        if (sala != null) {
            lblSalaDestino.setText(
                sala.getCodigo() + "  —  Piso " + sala.getPiso()
                + "  (cap. " + sala.getCapacidad() + ")"
            );
        }
        double metros = calcularDistanciaTotal(camino);
        lblDistancia.setText(String.format(
            "%.0f m estimados  ·  %d segmentos", metros, camino.size() - 1
        ));
        actualizarInstrucciones(ruta, camino);

        mapa.repaint();
    }

    /**
     * Muestra un mensaje de error en un cuadro de diálogo.
     *
     * @param mensaje texto descriptivo del error
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this, mensaje, "Error de Consulta", JOptionPane.ERROR_MESSAGE
        );
    }

    /** Hace visible esta ventana. */
    public void mostrar()  { setVisible(true); }

    /** Oculta esta ventana sin destruirla. */
    public void ocultar()  { setVisible(false); }

    // ── Privados ──────────────────────────────────────────────────────────────

    private void actualizarInstrucciones(Ruta ruta, List<Nodo> camino) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        List<String> instrucciones = ruta.getInstrucciones();

        if (instrucciones != null && !instrucciones.isEmpty()) {
            for (int i = 0; i < instrucciones.size(); i++)
                modelo.addElement((i + 1) + ".  " + instrucciones.get(i));
        } else {
            for (int i = 0; i < camino.size() - 1; i++) {
                Nodo desde = camino.get(i), hasta = camino.get(i + 1);
                double m   = haversine(
                    desde.getLatitud(), desde.getLongitud(),
                    hasta.getLatitud(), hasta.getLongitud()
                );
                String dir = calcularDireccion(
                    desde.getLatitud(), desde.getLongitud(),
                    hasta.getLatitud(), hasta.getLongitud()
                );
                modelo.addElement(String.format(
                    "%d.  Camina %.0f m hacia el %s", i + 1, m, dir
                ));
            }
            if (!camino.isEmpty())
                modelo.addElement(camino.size() + ".  Has llegado a tu destino.");
        }
        listaPasos.setModel(modelo);
    }

    private double calcularDistanciaTotal(List<Nodo> camino) {
        double total = 0.0;
        for (int i = 0; i < camino.size() - 1; i++)
            total += haversine(
                camino.get(i).getLatitud(),   camino.get(i).getLongitud(),
                camino.get(i+1).getLatitud(), camino.get(i+1).getLongitud()
            );
        return total;
    }

    private String calcularDireccion(double lat1, double lon1,
                                      double lat2, double lon2) {
        double dLat = lat2 - lat1, dLon = lon2 - lon1;
        if (Math.abs(dLat) >= Math.abs(dLon))
            return dLat >= 0 ? "Norte" : "Sur";
        return dLon >= 0 ? "Este" : "Oeste";
    }

    private double haversine(double lat1, double lon1,
                              double lat2, double lon2) {
        final double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // ── Clases internas ───────────────────────────────────────────────────────

    /**
     * Painter que dibuja la polilínea azul de la ruta sobre el mapa OSM.
     * Basado en el RoutePainter oficial del repositorio de JXMapViewer2.
     * Fuente: github.com/msteiger/jxmapviewer2/blob/master/examples/src/
     *         sample2_waypoints/RoutePainter.java
     */
    private static class RoutePainter implements Painter<JXMapViewer> {

        private final List<GeoPosition> puntos;

        RoutePainter(List<GeoPosition> puntos) {
            this.puntos = new ArrayList<>(puntos);
        }

        @Override
        public void paint(Graphics2D g, JXMapViewer mapa, int w, int h) {
            if (puntos.size() < 2) return;
            g = (Graphics2D) g.create();
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            );

            // Traducir coordenadas al viewport actual
            java.awt.Rectangle rect = mapa.getViewportBounds();
            g.translate(-rect.x, -rect.y);

            // Convertir GeoPosition a píxeles
            int[] xs = new int[puntos.size()];
            int[] ys = new int[puntos.size()];
            for (int i = 0; i < puntos.size(); i++) {
                Point2D pt = mapa.getTileFactory()
                    .geoToPixel(puntos.get(i), mapa.getZoom());
                xs[i] = (int) pt.getX();
                ys[i] = (int) pt.getY();
            }

            // Halo blanco para contraste sobre el mapa
            g.setColor(new Color(255, 255, 255, 200));
            g.setStroke(new BasicStroke(
                9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
            ));
            for (int i = 0; i < xs.length - 1; i++)
                g.drawLine(xs[i], ys[i], xs[i+1], ys[i+1]);

            // Línea azul principal
            g.setColor(new Color(25, 118, 210));
            g.setStroke(new BasicStroke(
                4.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
            ));
            for (int i = 0; i < xs.length - 1; i++)
                g.drawLine(xs[i], ys[i], xs[i+1], ys[i+1]);

            g.dispose();
        }
    }

    /**
     * Painter directo que dibuja los dos marcadores sobre el mapa:
     * círculo verde en el origen y círculo rojo en el destino.
     *
     * <p>Implementa {@code Painter<JXMapViewer>} directamente en vez de
     * usar {@code WaypointPainter} para evitar el doble indicador que
     * produce ese componente al superponer su ícono por defecto.</p>
     */
    private static class MarcadorPainter implements Painter<JXMapViewer> {

        private final GeoPosition posOrigen;
        private final GeoPosition posDestino;

        MarcadorPainter(GeoPosition origen, GeoPosition destino) {
            this.posOrigen  = origen;
            this.posDestino = destino;
        }

        @Override
        public void paint(Graphics2D g, JXMapViewer mapa, int w, int h) {
            g = (Graphics2D) g.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
            java.awt.Rectangle rect = mapa.getViewportBounds();
            g.translate(-rect.x, -rect.y);

            dibujarMarcador(g, mapa, posOrigen,  new Color(46, 125, 50));
            dibujarMarcador(g, mapa, posDestino, new Color(198, 40,  40));

            g.dispose();
        }

        /**
         * Dibuja un círculo coloreado con borde blanco y punto central
         * en la posición GPS indicada, convertida a píxeles del viewport.
         */
        private void dibujarMarcador(Graphics2D g, JXMapViewer mapa,
                                      GeoPosition pos, Color color) {
            Point2D pt = mapa.getTileFactory().geoToPixel(pos, mapa.getZoom());
            int cx = (int) pt.getX();
            int cy = (int) pt.getY();
            int r  = 10;

            // Sombra
            g.setColor(new Color(0, 0, 0, 55));
            g.fillOval(cx - r + 1, cy - r + 1, r * 2, r * 2);
            // Relleno
            g.setColor(color);
            g.fillOval(cx - r, cy - r, r * 2, r * 2);
            // Borde blanco
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2f));
            g.drawOval(cx - r, cy - r, r * 2, r * 2);
            // Punto central
            g.fillOval(cx - 3, cy - 3, 6, 6);
        }
    }

    /**
     * Renderer de celdas para la lista de pasos de navegación.
     *
     * <p>Usa JTextArea en vez de JLabel para que el texto haga salto de
     * línea automático (word wrap) cuando supera el ancho del panel,
     * mostrando cada paso completo sin cortes.</p>
     */
    private static class PasoRenderer
            implements javax.swing.ListCellRenderer<String> {

        private static final Color BG_PAR      = new Color(248, 248, 248);
        private static final Color BG_IMPAR    = Color.WHITE;
        private static final Color BG_SELECTED = new Color(210, 225, 245);

        @Override
        public Component getListCellRendererComponent(
                JList<? extends String> list, String value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JTextArea area = new JTextArea(value);
            area.setFont(new Font("SansSerif", Font.PLAIN, 12));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setOpaque(true);
            area.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            area.setSize(new Dimension(230, Short.MAX_VALUE));

            if (isSelected) {
                area.setBackground(BG_SELECTED);
                area.setForeground(list.getSelectionForeground());
            } else {
                area.setBackground(index % 2 == 0 ? BG_PAR : BG_IMPAR);
                area.setForeground(list.getForeground());
            }
            return area;
        }
    }
}
