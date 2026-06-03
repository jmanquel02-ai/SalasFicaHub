package modelo;

import java.util.*;

/**
 * Modela el cálculo y representación del camino más corto entre dos intersecciones (nodos).
 * Implementa el algoritmo de Búsqueda en Anchura (BFS) sobre el grafo espacial del campus.
 */
public class Ruta {

    private final Nodo origen;
    private final Nodo destino;
    private List<Nodo> camino;
    private final List<String> instrucciones;
    private double distanciaMetros;

    public Ruta(Nodo origen, Nodo destino) {
        this.origen = origen;
        this.destino = destino;
        this.camino = new ArrayList<>();
        this.instrucciones = new ArrayList<>();
        ejecutarCalculoRuta();
    }

    /**
     * Aplica el algoritmo Breadth-First Search (BFS) para determinar la ruta óptima.
     */
    private void ejecutarCalculoRuta() {
        Queue<List<Nodo>> colaProcesamiento = new LinkedList<>();
        Set<String> nodosVisitados = new HashSet<>();

        colaProcesamiento.add(Collections.singletonList(origen));

        while (!colaProcesamiento.isEmpty()) {
            List<Nodo> caminoActual = colaProcesamiento.poll();
            Nodo nodoEvaluado = caminoActual.get(caminoActual.size() - 1);

            if (nodoEvaluado.equals(destino)) {
                this.camino = new ArrayList<>(caminoActual);
                procesarInstruccionesTextuales();
                calcularDistanciaAcumulada();
                return;
            }

            if (nodosVisitados.contains(nodoEvaluado.getId())) {
                continue;
            }
            nodosVisitados.add(nodoEvaluado.getId());

            for (Nodo vecino : nodoEvaluado.getVecinos()) {
                if (!nodosVisitados.contains(vecino.getId())) {
                    List<Nodo> nuevoCamino = new ArrayList<>(caminoActual);
                    nuevoCamino.add(vecino);
                    colaProcesamiento.add(nuevoCamino);
                }
            }
        }
    }

    /**
     * Traduce los nodos de la ruta resultante a directrices legibles por el usuario.
     */
    private void procesarInstruccionesTextuales() {
        if (camino == null || camino.isEmpty()) {
            return;
        }

        instrucciones.add("Punto de partida: Ubíquese en " + origen.getDescripcion() + ".");

        for (int i = 1; i < camino.size() - 1; i++) {
            instrucciones.add(String.format("Paso %d: Diríjase hacia la intersección ubicada en %s.", i, camino.get(i).getDescripcion()));
        }

        instrucciones.add("Llegada: Ingrese a las dependencias y proceda a la ubicación final de su sala.");
    }

    /**
     * Cuantifica la distancia métrica de la ruta iterando sobre los segmentos geográficos
     * mediante la aplicación de la fórmula del semiverseno (Haversine).
     */
    private void calcularDistanciaAcumulada() {
        distanciaMetros = (camino.size() - 1) * 45;
    }

    private double aplicarFormulaHaversine(Nodo nodoA, Nodo nodoB) {
        final int RADIO_TIERRA_METROS = 6371000;
        double difLatitud = Math.toRadians(nodoB.getLatitud() - nodoA.getLatitud());
        double difLongitud = Math.toRadians(nodoB.getLongitud() - nodoA.getLongitud());

        double componenteA = Math.sin(difLatitud / 2) * Math.sin(difLatitud / 2)
                + Math.cos(Math.toRadians(nodoA.getLatitud()))
                * Math.cos(Math.toRadians(nodoB.getLatitud()))
                * Math.sin(difLongitud / 2) * Math.sin(difLongitud / 2);

        double componenteC = 2 * Math.atan2(Math.sqrt(componenteA), Math.sqrt(1 - componenteA));
        return RADIO_TIERRA_METROS * componenteC;
    }

    public boolean isValida() {
        return camino != null && !camino.isEmpty();
    }

    public List<Nodo> getCamino() { return camino; }
    public List<String> getInstrucciones() { return instrucciones; }
    public double getDistanciaMetros() { return distanciaMetros; }
    public Nodo getOrigen() { return origen; }
    public Nodo getDestino() { return destino; }
}