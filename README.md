# SalasFicaHub

**Programación Orientada a Objetos — Universidad de La Frontera**
Facultad de Ingeniería y Ciencias

## Descripción
Sistema de orientación académica para la FICA-UFRO.
Permite consultar la ubicación de salas dentro del campus
y obtener una ruta desde un punto de inicio hasta la sala destino.

## Integrantes
- Jonathan Manquel
- Bastián Escobar
- Vicente Flores

**Profesor:** Samuel Sepúlveda

## Tecnologías

| Tecnología    | Versión | Para qué sirve             |
|---------------|---------|-----------------------------|
| Java          | 17      | Lenguaje base                |
| Maven         | -       | Gestión de dependencias      |
| json-simple   | 1.1.1   | Leer archivos JSON            |
| jxmapviewer2  | 2.8.1   | Mapa OpenStreetMap/Swing      |
| junit-jupiter | 5.10.0  | Pruebas unitarias              |

Todas las dependencias vienen de **Maven Central**, no hay que descargar nada manualmente.

### ¿Por qué JXMapViewer2 y no JMapViewer?
JXMapViewer2 está publicado directamente en Maven Central por su autor
(Martin Steiger, github.com/msteiger/jxmapviewer2), así que Maven lo
descarga desde `repo1.maven.org`, el servidor oficial de la Apache
Software Foundation. JMapViewer (el original de JOSM) tenía su propio
repositorio, que resultó inestable y no disponible desde todas las redes
universitarias.

## Estructura del proyecto
```
src/main/java/
├── controlador/    AppController, BusquedaController
├── datos/          RepositorioDatos, GrafoLoader
├── launcher/       Main
├── modelo/         Ubicacion, Edificio, Sala, PuntoReferencia, Nodo, Ruta
└── vista/          BienvenidaView, MapaView, InstruccionesView

src/main/resources/data/
├── edificios.json
├── salas.json
├── puntos.json
└── nodos.csv

src/test/java/test/
└── SalasFicaTest.java
```

## Cómo ejecutar
1. Abre IntelliJ → **Open** → selecciona la carpeta `SalasFicaHub`
2. IntelliJ detecta el `pom.xml` → clic en **Load Maven Project**
3. Maven descarga las dependencias automáticamente (requiere internet la primera vez)
4. Ejecuta `launcher/Main.java`

## Funcionamiento del mapa
El mapa carga tiles de OpenStreetMap en tiempo real (como Google Maps),
así que requiere conexión a internet para mostrarse. El cálculo de rutas
(Dijkstra, ponderado por distancia real vía fórmula de Haversine) **no**
necesita internet — solo la imagen del mapa.

## Origen de los datos del grafo (nodos.csv)
`nodos.csv` no es un grafo hecho a mano: se generó a partir de un export
real de OpenStreetMap (`.osm`) del sector del campus que cubre FICA. Se
extrajeron los senderos, calles y entradas de edificio
(`highway=footway/path/service/...` y `entrance=yes`) y el grafo se armó
directamente desde esos datos.

Motivo: con un grafo hecho a mano, las rutas podían cortar camino en
línea recta a través de edificios, o dar rodeos absurdos, porque no había
forma de saber si dos puntos estaban realmente conectados por un sendero.
Usando el `.osm` real, cada conexión del grafo es un tramo de camino que
efectivamente existe.

**Si el campus cambia (edificio nuevo, sendero cerrado, etc.) o hay que
ampliar la zona cubierta:**
1. Ir a [openstreetmap.org/export](https://www.openstreetmap.org/export)
2. Ajustar el recuadro a la zona que se necesita cubrir
3. Descargar el `.osm`
4. Regenerar `nodos.csv`, `puntos.json` y `salas.json` (nodoId) a partir
   de ese archivo — implica parsear XML y hacer snapping de edificios a
   la red de senderos más cercana.

No editar `nodos.csv` a mano — con 505 nodos reales, cualquier edición
manual rompe la coherencia con los datos de origen.

## Ramas
- `master` — rama principal protegida
- `feature/jonathan` — Jonathan Manquel
- `feature/bastian` — Bastián Escobar
- `feature/vicente` — Vicente Flores
