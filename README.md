# SalasFICA Hub

**Programación Orientada a Objetos — Universidad de La Frontera**
Facultad de Ingeniería y Ciencias

---

# Requisitos

* IntelliJ IDEA
* JDK 17 o superior
* Git

Verificar instalación de Java:

```bash
java -version
```

---

# Ejecución del proyecto

## Paso 1: Clonar el repositorio

### ⚠️ IMPORTANTE

La versión funcional correspondiente al **Avance 02** se encuentra en la rama:

# `avance2-integracion`

Para obtener correctamente el código del proyecto debe clonarse directamente dicha rama:

```bash
git clone -b avance2-integracion https://github.com/jmanquel02-ai/SalasFicaHub.git
```

Si se clona únicamente la rama principal (`master`), no se obtendrá la versión completa del proyecto correspondiente al avance entregado.

---

## Paso 2: Abrir en IntelliJ IDEA

1. Abrir IntelliJ IDEA.
2. Seleccionar **Open**.
3. Abrir la carpeta raíz del proyecto.
4. Verificar que `src/main/java` esté marcado como **Sources Root**.

Si no lo está:

```text
Click derecho → src/main/java
Mark Directory As → Sources Root
```

---

## Paso 3: Ejecutar la aplicación

Abrir:

```text
src/main/java/launcher/Main.java
```

Ejecutar:

```text
Run 'Main.main()'
```

---

# Conceptos de Programación Orientada a Objetos aplicados

* Encapsulamiento
* Herencia
* Polimorfismo
* Abstracción
* Composición
* Asociación
* Dependencia
* Arquitectura MVC

---

# Estado actual del proyecto

## Implementado

* Arquitectura MVC funcional.
* Modelo de dominio completo.
* Búsqueda de rutas mediante BFS.
* Interfaz gráfica operativa.
* Visualización de recorridos.
* Sistema básico de navegación.
* Organización modular del código.

## Próximas mejoras

* Mejoras en las instrucciones de navegación.
* Mejor experiencia visual para el usuario.

---

# Integrantes

* Jonathan Manquel
* Bastián Escobar
* Vicente Flores

---

# Asignatura

**ICC490-1 — Programación Orientada a Objetos**

Universidad de La Frontera
Facultad de Ingeniería y Ciencias

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
