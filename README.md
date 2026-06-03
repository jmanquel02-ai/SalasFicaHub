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
- Java 17
- Maven
- Java Swing + JMapViewer
- JUnit 5
- OpenStreetMap

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
1. Abrir en IntelliJ IDEA
2. Maven descarga las dependencias automáticamente
3. Ejecutar `launcher/Main.java`

## Ramas
- `master` — rama principal protegida
- `feature/jonathan` — Jonathan Manquel
- `feature/bastian` — Bastián Escobar
- `feature/vicente` — Vicente Flores
