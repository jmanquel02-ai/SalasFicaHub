package modelo;

import java.util.*;

/**
 * Modela el cálculo y representación del camino más corto entre dos intersecciones (nodos).
 * Implementa el algoritmo de Dijkstra sobre el grafo espacial del campus, usando la
 * distancia real (fórmula de Haversine) entre nodos como peso de cada arista.
 *
 * Nota histórica: antes se usaba BFS (menos saltos), pero al crecer el grafo con
 * enlaces entre cruces duplicados de distintas ramas, BFS tomaba "atajos" de pocos
 * saltos que en la realidad eran rodeos largos, generando rutas en zigzag.
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
     * Aplica el algoritmo de Dijkstra para determinar la ruta más corta en distancia real,
     * usando aplicarFormulaHaversine() como peso de cada arista del grafo.
     */
    private void ejecutarCalculoRuta() {
        Map<String, Double> distancia   = new HashMap<>();
        Map<String, Nodo>   anterior    = new HashMap<>();
        Set<String>         visitados   = new HashSet<>();
        PriorityQueue<Nodo> cola = new PriorityQueue<>(
                Comparator.comparingDouble(n -> distancia.getOrDefault(n.getId(), Double.MAX_VALUE)));

        distancia.put(origen.getId(), 0.0);
        cola.add(origen);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();

            if (visitados.contains(actual.getId())) continue;
            visitados.add(actual.getId());

            if (actual.equals(destino)) break;

            double distActual = distancia.getOrDefault(actual.getId(), Double.MAX_VALUE);

            for (Nodo vecino : actual.getVecinos()) {
                if (visitados.contains(vecino.getId())) continue;

                double peso = aplicarFormulaHaversine(actual, vecino);
                double nuevaDist = distActual + peso;

                if (nuevaDist < distancia.getOrDefault(vecino.getId(), Double.MAX_VALUE)) {
                    distancia.put(vecino.getId(), nuevaDist);
                    anterior.put(vecino.getId(), actual);
                    cola.add(vecino);
                }
            }
        }

        if (!distancia.containsKey(destino.getId())) {
            return; // no hay ruta: camino queda vacío, isValida() devolverá false
        }

        // Reconstruir camino desde destino hacia origen usando el mapa "anterior"
        LinkedList<Nodo> caminoReconstruido = new LinkedList<>();
        Nodo cursor = destino;
        while (cursor != null) {
            caminoReconstruido.addFirst(cursor);
            if (cursor.equals(origen)) break;
            cursor = anterior.get(cursor.getId());
        }

        this.camino = new ArrayList<>(caminoReconstruido);
        this.distanciaMetros = distancia.get(destino.getId());
        procesarInstruccionesTextuales();
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
     * Calcula la distancia real en metros entre dos nodos adyacentes mediante
     * la fórmula del semiverseno (Haversine). Se usa como peso de cada arista en Dijkstra.
     */
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