# SalasFICA Hub

Sistema de orientación académica para la Facultad de Ingeniería y Ciencias de la Universidad de La Frontera (UFRO), desarrollado en Java utilizando Programación Orientada a Objetos (POO) y arquitectura Modelo-Vista-Controlador (MVC).

## Descripción del proyecto

SalasFICA Hub busca facilitar la orientación de estudiantes nuevos, visitantes y miembros de la comunidad universitaria dentro del campus UFRO. El sistema permite consultar salas académicas y visualizar una ruta desde distintos puntos de acceso hacia el destino seleccionado.

La aplicación utiliza una representación del campus mediante nodos e intersecciones, permitiendo calcular recorridos mediante algoritmos de búsqueda y mostrar instrucciones básicas de navegación al usuario.

---

## Objetivo

Desarrollar una herramienta que permita:

* Consultar salas académicas de la Facultad de Ingeniería y Ciencias.
* Seleccionar un punto de inicio dentro del campus.
* Calcular una ruta hacia la sala destino.
* Visualizar el recorrido en una interfaz gráfica.
* Aplicar conceptos de Programación Orientada a Objetos y arquitectura MVC.

---

## Funcionalidades implementadas

### Gestión de ubicaciones

* Registro de edificios académicos.
* Registro de salas.
* Registro de puntos de referencia.
* Modelado de intersecciones y nodos de navegación.

### Búsqueda y cálculo de rutas

* Selección de punto de origen.
* Búsqueda de salas por código.
* Generación de rutas mediante Breadth First Search (BFS).
* Cálculo estimado de distancia recorrida.

### Interfaz gráfica

* Ventana de bienvenida.
* Selección de sala destino.
* Visualización de mapa del campus.
* Visualización gráfica de rutas.
* Panel de instrucciones de navegación.

### Gestión de datos

* Separación de datos y lógica de negocio.
* Carga de información desde recursos del proyecto.

---

## Arquitectura del sistema

El proyecto implementa el patrón Modelo-Vista-Controlador (MVC).

### Modelo

Representa las entidades del dominio:

* Ubicacion
* Edificio
* Sala
* PuntoReferencia
* Nodo
* Ruta

### Vista

Gestiona la interacción con el usuario:

* BienvenidaView
* MapaView
* InstruccionesView

### Controlador

Coordina la comunicación entre modelo y vistas:

* AppController
* BusquedaController

### Datos

Gestiona la carga y administración de información:

* RepositorioDatos
* GrafoLoader

---

## Estructura del proyecto

```text
src
├── main
│   ├── java
│   │   ├── controlador
│   │   ├── datos
│   │   ├── launcher
│   │   ├── modelo
│   │   └── vista
│   │
│   └── resources
│       ├── data
│       └── img
```

---

## Tecnologías utilizadas

* Java 17
* Swing
* Maven
* Git
* GitHub

---

## Requisitos

* IntelliJ IDEA
* JDK 17 o superior
* Git

Verificar versión de Java:

```bash
java -version
```

---

## Ejecución del proyecto

### Clonar repositorio

```bash
git clone https://github.com/jmanquel02-ai/SalasFicaHub.git
```

### Abrir en IntelliJ IDEA

1. Abrir IntelliJ IDEA.
2. Seleccionar **Open**.
3. Abrir la carpeta raíz del proyecto.
4. Verificar que `src/main/java` esté marcado como **Sources Root**.

Si no lo está:

```text
Click derecho → src/main/java
Mark Directory As → Sources Root
```

### Ejecutar aplicación

Abrir:

```text
src/main/java/launcher/Main.java
```

Ejecutar:

```text
Run 'Main.main()'
```

---

## Conceptos de Programación Orientada a Objetos utilizados

* Encapsulamiento
* Herencia
* Polimorfismo
* Abstracción
* Composición
* Asociación
* Dependencia
* Arquitectura MVC

---

## Estado actual del proyecto

### Implementado

* Arquitectura MVC funcional.
* Modelo de dominio completo.
* Búsqueda de rutas mediante BFS.
* Interfaz gráfica operativa.
* Visualización de recorridos.
* Sistema básico de navegación.

### Próximas mejoras

* Instrucciones de navegación más detalladas.
* Mejoras en experiencia de usuario.

---

## Integrantes

* Jonathan Manquel
* Bastián Escobar
* Vicente Flores

---

## Asignatura

ICC490-1 Programación Orientada a Objetos

Universidad de La Frontera
Facultad de Ingeniería y Ciencias

2026
