package vista;

import controlador.AppController;
import modelo.Ruta;
import modelo.Sala;

/**
 * Vista de instrucciones — desactivada en esta versión.
 *
 * <p>Las instrucciones y datos de la sala se muestran directamente
 * en el panel lateral de {@link MapaView}, por lo que esta ventana
 * ya no es necesaria. Se mantiene la clase para no romper la firma
 * de {@link controlador.AppController} ni {@link controlador.BusquedaController}.</p>
 *
 * @author Bastián Escobar
 * @version 3.0
 */
public class InstruccionesView {

    public InstruccionesView(AppController controller) {
        // Sin UI — todo está en el panel lateral del MapaView
    }

    /** No hace nada — las instrucciones están en MapaView. */
    public void mostrarInstrucciones(Ruta ruta, Sala sala) { }

    /** No hace nada. */
    public void mostrar()  { }

    /** No hace nada. */
    public void ocultar()  { }
}
