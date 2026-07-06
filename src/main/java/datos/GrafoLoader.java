package datos;

import modelo.Nodo;

import java.io.*;
import java.util.*;

/**
 * Carga el grafo de nodos desde {@code nodos.csv} en resources.
 * Construye los objetos {@link Nodo} y enlaza sus vecinos.
 *
 * Formato CSV esperado:
 * id,latitud,longitud,descripcion,vecinos
 * N001,-38.746,-72.617,Entrada norte,N002|N003
 *
 * @author Jonathan Manquel
 * @version 2.0
 */
public class GrafoLoader {

    /**
     * Lee nodos.csv desde el classpath y construye el grafo.
     *
     * @return mapa id → Nodo con vecinos enlazados
     * @throws IOException si el archivo no se encuentra
     */
    public Map<String, Nodo> cargarGrafo() throws IOException {
        Map<String, Nodo>         nodos   = new LinkedHashMap<>();
        Map<String, List<String>> vecinos = new HashMap<>();

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("data/nodos.csv");

        if (is == null)
            throw new IOException("No se encontró data/nodos.csv en resources");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String linea;
            boolean primera = true;

            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.trim().isEmpty()) continue;

                String[] c = linea.split(",", 5);
                if (c.length < 5) continue;

                String id   = c[0].trim();
                double lat  = Double.parseDouble(c[1].trim());
                double lon  = Double.parseDouble(c[2].trim());
                String desc = c[3].trim();
                String vecs = c[4].trim();

                nodos.put(id, new Nodo(id, lat, lon, desc));
                vecinos.put(id, Arrays.asList(vecs.split("\\|")));
            }
        }

        // Segunda pasada: enlazar vecinos
        for (Map.Entry<String, List<String>> e : vecinos.entrySet()) {
            Nodo nodo = nodos.get(e.getKey());
            for (String vid : e.getValue()) {
                Nodo v = nodos.get(vid.trim());
                if (v != null) nodo.agregarVecino(v);
            }
        }

        return nodos;
    }
}
