package vista;

import controlador.AppController;
import modelo.Ruta;
import modelo.Sala;

import javax.swing.*;
import java.awt.*;

/**
 * Vista lateral con instrucciones de navegación paso a paso.
 * Muestra los pasos de {@link Ruta#getInstrucciones()} e info de la sala.
 *
 * @author Bastián Escobar
 * @version 2.0
 */
public class InstruccionesView extends JFrame {

    private AppController controller;
    private JTextArea     areaInstrucciones;
    private JLabel        labelSala;
    private JLabel        labelDistancia;

    /**
     * @param controller controlador principal
     */
    public InstruccionesView(AppController controller) {
        this.controller = controller;
        configurarVentana();
        construirComponentes();
    }

    private void configurarVentana() {
        setTitle("Instrucciones");
        setSize(280, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                controller.salir();
            }
        });
        setLayout(new BorderLayout());
    }

    private void construirComponentes() {
        labelSala         = new JLabel("Sala: —");
        labelDistancia    = new JLabel("Distancia: —");
        areaInstrucciones = new JTextArea();
        areaInstrucciones.setEditable(false);
        areaInstrucciones.setLineWrap(true);
        areaInstrucciones.setWrapStyleWord(true);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        header.add(labelSala);
        header.add(labelDistancia);

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(areaInstrucciones), BorderLayout.CENTER);
    }

    /**
     * Muestra instrucciones y datos de la sala destino.
     *
     * @param ruta ruta calculada
     * @param sala sala destino
     */
    public void mostrarInstrucciones(Ruta ruta, Sala sala) {
        labelSala.setText("Sala: " + sala.getCodigo() + " — Piso " + sala.getPiso());
        labelDistancia.setText(String.format("Distancia: %.0f m aprox.",
                ruta.getDistanciaMetros()));

        StringBuilder sb = new StringBuilder();
        for (String paso : ruta.getInstrucciones())
            sb.append(paso).append("\n\n");

        areaInstrucciones.setText(sb.toString());
        areaInstrucciones.setCaretPosition(0);
    }

    public void mostrar()  { setVisible(true); }
    public void ocultar()  { setVisible(false); }
}
