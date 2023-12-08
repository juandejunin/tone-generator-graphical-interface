# Generador de Tonos

Este proyecto es un generador de tonos que utiliza JavaFX para la interfaz de usuario y Java Sound API para la generación de tonos.

## Características

- Reproduce tonos de diferentes frecuencias y duraciones.
- Soporta varios tipos de formas de onda: Seno, Cuadrada, Diente de Sierra y Triangular.
- Interfaz de usuario intuitiva con campos para ingresar frecuencia y duración, así como un menú desplegable para seleccionar la forma de onda.
- Implementa un botón de reproducción para activar la generación de tonos.

## Requisitos

- Java Development Kit (JDK) 17 o superior.
- Maven para la construcción y gestión de dependencias.

## Instrucciones de Uso

1. Clona el repositorio a tu máquina local.
   ```
   git clone https://github.com/tuusuario/generador-de-tonos.git
   ```
2. Navega al directorio del proyecto.
   ```
   cd generador-de-tonos
   ```
3. Compila y ejecuta la aplicación utilizando Maven.
   ```
   mvn clean javafx:run
   ```
4. Ingresa la frecuencia, duración y selecciona la forma de onda en la interfaz de usuario.
5. Haz clic en el botón "Reproducir" para escuchar el tono generado.

## Configuración del Proyecto

El proyecto está configurado con Maven y utiliza las siguientes dependencias:

- JavaFX para la interfaz de usuario.
- ControlsFX y BootstrapFX para mejorar la apariencia de la interfaz.
