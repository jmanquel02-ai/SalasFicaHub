package controlador;

import datos.RepositorioDatos;
import vista.BienvenidaView;
import vista.InstruccionesView;
import vista.MapaView;

/**
 * Controlador principal de SalasFICA.
 * Inicia la app, carga los datos y navega entre vistas.
 * NO calcula rutas — eso es responsabilidad de {@link BusquedaController}.
 *
 * @author Vicente Flores
 * @version 2.0
 */
public class AppController {

    private RepositorioDatos  repositorio;
    private BienvenidaView    bienvenidaView;
    private MapaView          mapaView;
    private InstruccionesView instruccionesView;

    /**
     * Crea el controlador y carga todos los datos al iniciar.
     *
     * @throws Exception si los archivos de datos no pueden leerse
     */
    public AppController() throws Exception {
        this.repositorio = new RepositorioDatos();
    }

    /**
     * Inicia la aplicación mostrando la pantalla de bienvenida.
     */
    public void iniciar() {
        bienvenidaView    = new BienvenidaView(this);
        mapaView          = new MapaView(this);
        instruccionesView = new InstruccionesView(this);
        mostrarBienvenida();
    }

    /**
     * Muestra la pantalla de bienvenida y oculta el mapa.
     */
    public void mostrarBienvenida() {
        mapaView.ocultar();
        instruccionesView.ocultar();
        bienvenidaView.mostrar();
    }

    /**
     * Muestra el mapa e instrucciones, oculta bienvenida.
     */
    public void mostrarMapa() {
        bienvenidaView.ocultar();
        mapaView.mostrar();
        instruccionesView.mostrar();
    }

    /**
     * Cierra toda la aplicación limpiamente.
     * Llamado cuando el usuario cierra cualquier ventana.
     */
    public void salir() {
        System.exit(0);
    }

    /**
     * Delega la búsqueda al {@link BusquedaController}.
     *
     * @param origenId   id del punto de referencia
     * @param codigoSala código de la sala destino
     */
    public void iniciarBusqueda(String origenId, String codigoSala) {
        BusquedaController busqueda =
                new BusquedaController(repositorio, mapaView, instruccionesView);
        busqueda.buscarYMostrarRuta(origenId, codigoSala);
        mostrarMapa();
    }

    public RepositorioDatos getRepositorio() { return repositorio; }
}