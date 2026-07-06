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

| Tecnología    | Para qué sirve                |
|---------------|---------------------------------|
| Java 17       | Lenguaje base                    |
| Maven         | Gestión de dependencias           |
| json-simple   | Leer archivos JSON                 |
| jxmapviewer2  | Mapa interactivo OpenStreetMap/Swing|
| junit-jupiter | Pruebas unitarias                    |

Todas las dependencias vienen de Maven Central, no hay que descargar nada manualmente.

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
└── RepositorioDatosTest.java
```

## Cómo ejecutar
1. Abre IntelliJ → **Open** → selecciona la carpeta `SalasFicaHub`
2. IntelliJ detecta el `pom.xml` → clic en **Load Maven Project**
3. Maven descarga las dependencias automáticamente (requiere internet la primera vez)
4. Ejecuta `launcher/Main.java`

## Funcionamiento del mapa
El mapa carga tiles de OpenStreetMap en tiempo real, así que requiere conexión
a internet para mostrarse. El cálculo de rutas no necesita internet — solo la
imagen del mapa.

## Datos de navegación
`nodos.csv`, `puntos.json`, `salas.json` y `edificios.json` (en
`src/main/resources/data/`) contienen el modelo de datos del campus que usa
el motor de rutas. No editar `nodos.csv` a mano — cualquier edición manual
puede romper la coherencia de las conexiones entre nodos. El detalle técnico
del modelo de datos y del motor de rutas se presenta en la exposición del
proyecto.

## Ramas
- `master` — rama principal protegida
- `feature/jonathan` — Jonathan Manquel
- `feature/bastian` — Bastián Escobar
- `feature/vicente` — Vicente Flores
