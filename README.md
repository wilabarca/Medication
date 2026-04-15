# Proyecto Medication

## Descripción General

Este proyecto es una aplicación móvil Android desarrollada en Kotlin, diseñada para la gestión de medicamentos. Permite a los usuarios registrar, visualizar, editar y eliminar información sobre sus medicamentos, así como configurar alarmas y buscar medicamentos. La aplicación sigue principios de diseño moderno y buenas prácticas de desarrollo para Android.

## Arquitectura

La aplicación está construida siguiendo los principios de **Clean Architecture** y el patrón **MVVM (Model-View-ViewModel)** para la capa de presentación. Esta aproximación promueve la separación de preocupaciones, la testabilidad y la mantenibilidad del código.

### Capas de la Arquitectura:

*   **Capa de Datos (Data Layer):** Responsable de la obtención y persistencia de datos. Incluye implementaciones de repositorios que interactúan con fuentes de datos locales (Room Database) y remotas (APIs RESTful a través de Retrofit).
*   **Capa de Dominio (Domain Layer):** Contiene la lógica de negocio central de la aplicación. Es independiente de cualquier detalle de implementación y define las entidades, casos de uso (Use Cases) e interfaces de repositorio. Los casos de uso orquestan las operaciones entre los repositorios para cumplir con los requisitos de negocio.
*   **Capa de Presentación (Presentation Layer):** Encargada de la interfaz de usuario. Utiliza **Jetpack Compose** para construir la UI de forma declarativa. Los `ViewModels` exponen el estado de la UI y manejan la lógica de presentación, interactuando con los casos de uso de la capa de dominio.

### Módulos de Características:

El proyecto está modularizado por características, lo que significa que cada funcionalidad principal (e.g., autenticación, gestión de medicamentos, búsqueda) tiene su propio conjunto de capas de datos, dominio y presentación. Esto mejora la escalabilidad y la organización del código.

## Tecnologías Utilizadas

*   **Lenguaje de Programación:** Kotlin
*   **UI Toolkit:** Jetpack Compose
*   **Inyección de Dependencias:** Hilt
*   **Base de Datos Local:** Room Persistence Library
*   **Redes:** Retrofit y OkHttp
*   **Tareas en Segundo Plano:** WorkManager
*   **Mensajería:** Firebase Messaging
*   **Carga de Imágenes:** Coil
*   **Serialización JSON:** Gson
*   **Navegación:** Jetpack Navigation Compose

## Estructura del Proyecto

La estructura del proyecto se organiza de la siguiente manera:

```
Medication/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/medication/
│   │   │   │   ├── core/                  # Componentes base y utilidades
│   │   │   │   ├── features/              # Módulos por característica
│   │   │   │   │   ├── auth/              # Característica de Autenticación
│   │   │   │   │   │   ├── data/          # Implementación de datos (repositorios, fuentes de datos)
│   │   │   │   │   │   ├── domain/        # Lógica de negocio (entidades, casos de uso, interfaces de repositorio)
│   │   │   │   │   │   └── presentation/  # UI y ViewModels
│   │   │   │   │   ├── medication/        # Característica de Gestión de Medicamentos
│   │   │   │   │   └── searchmedication/  # Característica de Búsqueda de Medicamentos
│   │   │   │   └── MainActivity.kt        # Actividad principal
│   │   │   └── res/                     # Recursos de Android (layouts, drawables, values)
│   │   └── AndroidManifest.xml
├── build.gradle.kts
├── gradle/
└── README.md
```

## Instalación y Ejecución

Para compilar y ejecutar este proyecto, necesitarás Android Studio con el SDK de Android configurado. Sigue estos pasos:

1.  Clona el repositorio:
    ```bash
    git clone https://github.com/wilabarca/Medication.git
    ```
2.  Abre el proyecto en Android Studio.
3.  Sincroniza el proyecto con los archivos Gradle.
4.  Ejecuta la aplicación en un emulador o dispositivo físico.

## Contribución

Las contribuciones son bienvenidas. Por favor, abre un 'issue' para discutir los cambios propuestos o envía un 'pull request'.

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles. (Nota: El archivo LICENSE no está incluido en el repositorio clonado, se asume una licencia común para proyectos de código abierto).
