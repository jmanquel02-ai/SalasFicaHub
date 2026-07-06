package test;

import datos.RepositorioDatos;
import modelo.Nodo;
import modelo.PuntoReferencia;
import modelo.Sala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la capa de datos.
 * Valida la correcta extracción de información desde archivos externos (JSON/CSV)
 * y el manejo de excepciones o datos nulos.
 */
class RepositorioDatosTest {

    private RepositorioDatos repositorio;

    // Se ejecuta ANTES de cada @Test para asegurar un entorno limpio
    @BeforeEach
    void setUp() {
        try {
            repositorio = new RepositorioDatos();
        } catch (Exception e) {
            fail("Fallo crítico: El repositorio no pudo cargar los archivos JSON/CSV. " + e.getMessage());
        }
    }

    @Test
    void testCargaMasivaDeSalas() {
        // Verifica que el JSON de salas se leyó por completo
        assertEquals(24, repositorio.getSalas().size(), "El sistema debe cargar exactamente 24 salas del archivo");
    }

    @Test
    void testBuscarSalaExistenteSinSensibilidadMayusculas() {
        // Verifica que la búsqueda funciona incluso si el usuario escribe en minúsculas
        Sala sala = repositorio.buscarSala("r101");

        assertNotNull(sala, "La sala R101 debe existir en la memoria");
        assertEquals("R101", sala.getCodigo(), "El código normalizado debe ser R101 en mayúsculas");
        assertEquals("R", sala.getEdificioId(), "La sala R101 debe pertenecer al edificio R");
    }

    @Test
    void testIntegridadDelGrafoEspacial() {
        // Verifica que el CSV de nodos se leyó correctamente
        assertEquals(14, repositorio.getGrafo().size(), "El grafo debe contener exactamente 14 nodos de intersección");

        Nodo nodoEntrada = repositorio.getNodo("N001");
        assertNotNull(nodoEntrada, "El nodo N001 (Entrada Francisco Salazar) debe existir");
        assertEquals(2, nodoEntrada.getVecinos().size(), "El nodo N001 debe tener exactamente 2 conexiones en el CSV");
    }

    @Test
    void testMapeoDePuntosDeReferencia() {
        // Verifica que los puntos de inicio (P1 al P5) existen
        assertEquals(5, repositorio.getPuntos().size(), "Deben existir 5 puntos de referencia base");

        PuntoReferencia p5 = null;
        for (PuntoReferencia p : repositorio.getPuntos()) {
            if (p.getId().equals("P5")) {
                p5 = p;
                break;
            }
        }

        assertNotNull(p5, "El punto P5 debe estar registrado");
        assertEquals("N005", p5.getNodoId(), "El punto P5 debe estar conectado topológicamente al nodo N005");
    }

    @Test
    void testManejoDeErroresEntradasInvalidas() {
        // Verifica que el sistema no explote (NullPointerException) si recibe basura
        assertNull(repositorio.buscarSala(null), "Buscar un nulo debe retornar null limpiamente");
        assertNull(repositorio.buscarSala(""), "Buscar un texto vacío debe retornar null limpiamente");
        assertNull(repositorio.buscarSala("SALA_FALSA"), "Buscar una sala inexistente debe retornar null");
    }
}