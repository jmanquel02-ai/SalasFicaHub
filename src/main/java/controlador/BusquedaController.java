package controlador;

import datos.RepositorioDatos;
import modelo.*;
import vista.InstruccionesView;
import vista.MapaView;

/**
 * Controlador de búsqueda y navegación de SalasFICA.
 * Recibe origen y destino, pide la ruta al modelo
 * y actualiza las vistas con el resultado.
 * NO dibuja ni calcula — solo coordina.
 *
 * @author Vicente Flores
 * @version 2.0
 */
public class BusquedaController {

    private RepositorioDatos  repositorio;
    private MapaView          mapaView;
    private InstruccionesView instruccionesView;

    public BusquedaController(RepositorioDatos repositorio,
                              MapaView mapaView,
                              InstruccionesView instruccionesView) {
        this.repositorio       = repositorio;
        this.mapaView          = mapaView;
        this.instruccionesView = instruccionesView;
    }

    /**
     * Busca sala, calcula ruta y actualiza ambas vistas.
     *
     * @param origenId   id del punto de referencia (ej: "P1")
     * @param codigoSala código de sala destino (ej: "R101")
     */
    public void buscarYMostrarRuta(String origenId, String codigoSala) {
        // 1. Buscar sala
        Sala sala = repositorio.buscarSala(codigoSala);
        if (sala == null) {
            mapaView.mostrarError("Sala '" + codigoSala + "' no encontrada.");
            return;
        }

        // 2. Buscar punto de referencia
        PuntoReferencia punto = repositorio.getPuntos().stream()
                .filter(p -> p.getId().equals(origenId))
                .findFirst().orElse(null);

        if (punto == null) {
            mapaView.mostrarError("Punto de origen no válido.");
            return;
        }

        // 3. Obtener nodos del grafo
        Nodo nodoOrigen  = repositorio.getNodo(punto.getNodoId());
        Nodo nodoDestino = repositorio.getNodo(sala.getNodoId());

        if (nodoOrigen == null || nodoDestino == null) {
            mapaView.mostrarError("No se pudo conectar al grafo de rutas.");
            return;
        }

        // 4. Calcular ruta (el modelo hace el trabajo)
        Ruta ruta = new Ruta(nodoOrigen, nodoDestino);

        if (!ruta.isValida()) {
            mapaView.mostrarError("No se encontró ruta hacia " + codigoSala + ".");
            return;
        }

        // 5. Actualizar vistas — el controlador no dibuja, solo entrega datos
        mapaView.mostrarRutaImagen(ruta, sala, origenId);
        instruccionesView.mostrarInstrucciones(ruta, sala);
    }
}