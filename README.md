# Gestión de Vehículos - Sistema CRUD

## Sobre Mí

Hola, Soy Axel Cannavina, estudiante de la Tecnicatura Universitaria en Programación en la UTN Avellaneda. Este proyecto es mi examen final de Programación II, donde aplico los conocimientos adquiridos en Programación Orientada a Objetos, estructuras de datos, interfaces y persistencia.

## Resumen del Proyecto

Este proyecto es un **Sistema de Gestión de Vehículos** desarrollado en Java. Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre una jerarquía de vehículos (Autos, Motos, Camiones) y ofrece funcionalidades avanzadas como filtrado, búsqueda, ordenamiento y exportación/importación de datos en varios formatos.

La aplicación cuenta con una **interfaz gráfica de usuario (GUI)** intuitiva desarrollada con JavaFX, lo que facilita la interacción del usuario.

### Funcionalidades Clave:

* **Gestión de Vehículos (CRUD):**
    * **Crear:** Añadir nuevos vehículos (Autos, Motos, Camiones) al sistema.
    * **Leer:** Visualizar la lista completa de vehículos.
    * **Actualizar:** Modificar los datos de un vehículo existente.
    * **Eliminar:** Remover vehículos del sistema.
* **Clasificación y Especialización:** Implementación de una jerarquía de herencia con una clase abstracta `Vehiculo` y sus subclases `Auto`, `Moto` y `Camion`, cada una con sus atributos y comportamientos específicos (ej. `calcularMantenimiento`, `obtenerTipoVehiculo`).
* **Cálculo de Precios de Venta:** Los vehículos que son `IVendible` (Auto, Moto, Camion) tienen un método para calcular su precio de venta basado en su depreciación.
* **Persistencia de Datos:**
    * **Guardado y Carga Binaria:** Guarda y recupera la lista de vehículos utilizando serialización de objetos (`.dat`).
    * **Exportación/Importación CSV:** Permite exportar e importar los datos de los vehículos a/desde un archivo CSV.
    * **Exportación/Importación JSON:** Soporte para exportar e importar datos en formato JSON, utilizando la librería Jackson.
* **Funcionalidades de Búsqueda y Filtrado:**
    * Filtrado avanzado de vehículos utilizando expresiones lambda y la interfaz `Predicate`.
    * Búsqueda por texto en los campos relevantes.
* **Ordenamiento:** Permite ordenar la lista de vehículos por año o por precio utilizando `Comparator`s personalizados.
* **Reportes:** Generación de un reporte detallado de los vehículos en formato de texto (`.txt`), incluyendo información de mantenimiento y precio de venta.
* **Iteración Personalizada:** Uso de un `VehiculoIterator` para recorrer la colección de vehículos.
* **Manejo de Excepciones:** Implementación de excepciones personalizadas (`DatosInvalidosException`, `VehiculoNoEncontradoException`) para un manejo de errores robusto.
* **Interfaz Gráfica de Usuario (JavaFX):**
    * Tabla interactiva para visualizar y seleccionar vehículos.
    * Formularios dinámicos para la creación y edición de vehículos, adaptándose al tipo de vehículo.
    * Botones para todas las operaciones CRUD y de persistencia.

### Demostración de la Interfaz Gráfica:

<img width="960" height="511" alt="Image" src="https://github.com/user-attachments/assets/e272a441-7536-43de-ab71-ef224dd57e9e" />

*Vista principal de la aplicación.*

<img width="960" height="509" alt="Image" src="https://github.com/user-attachments/assets/064de22c-ac6b-415e-a874-dbaad99b8a99" />

*Formulario para añadir o editar un vehículo.*

## Diagrama de Clases UML

Aquí se adjunta el diagrama UML que ilustra la estructura de clases, interfaces, enumeraciones y sus relaciones dentro del proyecto.

<img width="2180" height="1860" alt="Image" src="https://github.com/user-attachments/assets/4b7250f1-66d6-4d22-a4c2-c6591200f8e0" />

## Archivos Generados

La aplicación genera y/o utiliza los siguientes archivos para la persistencia y reporte de datos. Puedes encontrar ejemplos de estos archivos al ejecutar el proyecto.

* `vehiculos.dat`: Archivo binario para la serialización de objetos.
* `vehiculos.csv`: Archivo de texto delimitado por comas para exportación/importación.
* `vehiculos.json`: Archivo JSON para exportación/importación.
* `reporte_vehiculos_AAAA-MM-DD.txt`: Archivo de texto con un reporte detallado de los vehículos.

---
