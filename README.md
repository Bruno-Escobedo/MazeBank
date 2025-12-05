# 🏦 MazeBank

![Kotlin](https://img.shields.io/badge/Kotlin-FF5722?logo=kotlin\&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?logo=androidstudio\&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=android\&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-339933?logo=node.js\&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?logo=mariadb\&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?logo=git\&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github\&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-007ACC?logo=java\&logoColor=white)
![MVVM](https://img.shields.io/badge/MVVM-6A1B9A?logo=architecture\&logoColor=white)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

**Aplicación Bancaria Móvil — Kotlin + Jetpack Compose**

MazeBank es una aplicación móvil desarrollada en **Kotlin** utilizando **Jetpack Compose**, conectada a un backend en **Node.js + MariaDB**.
Su objetivo es simular la experiencia moderna de una banca digital: autenticación, dashboard financiero, transferencias, préstamos, inversiones y recuperación de contraseña.

---

## ✨ Características principales

* 🔐 **Inicio de sesión y registro seguro**
* 📊 **Dashboard dinámico** con resumen financiero
* 💸 **Transferencias** a cuentas propias y externas
* 💳 **Solicitud de préstamos** con flujo completo
* 📈 **Inversiones y trading** (vista demostrativa)
* 🔄 **Recuperación de contraseña**
* 🧱 **Arquitectura MVVM**, Retrofit, Repository Pattern
* 🧪 **Pruebas unitarias e instrumentadas**
* 🌐 **Consumo de API REST con Retrofit y Gson**
* 🛠️ **Control de versiones con Git y GitHub**
* 🖥️ **Desarrollo en Android Studio con Material 3 y Navigation Component**

---

## 🧩 Tecnologías utilizadas

* **Lenguaje:** Kotlin
* **UI/UX:** Jetpack Compose, Material 3, Navigation Component
* **Backend:** Node.js, Express
* **Base de datos:** MariaDB
* **API REST:** Retrofit + Gson
* **Arquitectura:** MVVM + Repositorios
* **Pruebas:** JUnit, Android Instrumented Tests
* **Control de versiones:** Git · GitHub
* **IDE:** Android Studio

---

## 🌐 Backend / API

* Base URL: `https://tu-backend.com/api` <!-- reemplaza con tu backend -->
* Endpoints principales: `/login`, `/transfer`, `/loans`

---

## 📁 Estructura del Proyecto (con comentarios y emojis)

```
─ src
    ├── androidTest/                           # 🧪 Pruebas instrumentadas
    │       ExampleInstrumentedTest.kt
    │
    ├── main/
    │   ├── AndroidManifest.xml                # 📄 Declaraciones del proyecto Android
    │   ├── ic_logo_maze-playstore.png         # 🖼️ Icono principal Play Store
    │   │
    │   ├── java/com/mazebank/                 # 💻 Lógica principal
    │   │   ├── MainActivity.kt                # 🎬 Actividad principal
    │   │   ├── Navigation.kt                  # 🧭 Navegación con Compose
    │   │   │
    │   │   ├── data/                          # 📦 Capa de datos
    │   │   │   ├── UserManager.kt             # 🔐 Manejo de sesión local
    │   │   │   ├── model/                     # 🧩 Modelos de datos
    │   │   │   │     ApiModels.kt
    │   │   │   │     UserData.kt
    │   │   │   ├── network/                   # 🌐 Cliente HTTP
    │   │   │   │     RetrofitInstance.kt      # ⚙️ Configuración de Retrofit
    │   │   │   └── repository/                # 📚 Repositorios (capa intermedia)
    │   │   │         AuthRepository.kt
    │   │   │
    │   │   ├── screens/                       # 🖥️ Todas las pantallas de la app
    │   │   │   ├── auth/                      # 🔐 Autenticación
    │   │   │   │     BankFormScreen.kt
    │   │   │   │     CredentialsScreen.kt
    │   │   │   │     LoginScreen.kt
    │   │   │   │     RegisterScreen.kt
    │   │   │   │     SuccessModal.kt
    │   │   │   │     UserFormScreen.kt
    │   │   │   │
    │   │   │   ├── dashboard/                 # 📊 Dashboard principal
    │   │   │   │     DashboardScreen.kt
    │   │   │   │
    │   │   │   ├── investments/               # 📈 Inversiones
    │   │   │   │     InvestmentsScreen.kt
    │   │   │   │     TradingScreen.kt
    │   │   │   │
    │   │   │   ├── loan/                      # 💳 Préstamos
    │   │   │   │     LoanScreen.kt
    │   │   │   │     LoanSuccessScreen.kt
    │   │   │   │
    │   │   │   ├── recovery/                  # 🔄 Recuperación de contraseña
    │   │   │   │     PasswordRecoveryScreen.kt
    │   │   │   │
    │   │   │   └── transfer/                  # 💸 Transferencias
    │   │   │         AddAccountScreen.kt
    │   │   │         AmountScreen.kt
    │   │   │         ConfirmationScreen.kt
    │   │   │         OtherPersonScreen.kt
    │   │   │         SuccessScreen.kt
    │   │   │         TransferMainScreen.kt
    │   │   │
    │   │   ├── ui/theme/                      # 🎨 Temas, colores y tipografía
    │   │   │     Color.kt
    │   │   │     Theme.kt
    │   │   │     Type.kt
    │   │   │
    │   │   └── viewmodels/                    # 🧠 Lógica de negocio (MVVM)
    │   │         AuthViewModel.kt
    │   │         TransferViewModel.kt
    │   │
    │   └── res/                               # 🎨 Recursos gráficos
    │       ├── drawable/                      # 🖼️ Íconos vectoriales
    │       ├── layout/                        # 🧱 Diseño XML (si aplica)
    │       ├── mipmap-*/                      # 🖍️ Iconos launcher (todas densidades)
    │       ├── values/                        # 🧩 Strings, colores, estilos
    │       └── xml/                           # ⚙️ Configuración avanzada
    │
    └── test/                                  # 🧪 Pruebas unitarias
            ExampleUnitTest.kt
```

---

## ▶️ Cómo ejecutar el proyecto

1. Clona el repositorio:

   ```bash
   git clone https://github.com/Bruno-Escobedo/MazeBank.git
   ```
2. Abre el proyecto en **Android Studio**.
3. Espera a que **Gradle** termine la sincronización.
4. Configura tu URL del backend en:
   `data/network/RetrofitInstance.kt`
5. Ejecuta la app en un emulador o dispositivo físico.

---

## 👥 Autores

* **Escobedo Negrete Bruno Uriel**
* **Parra Bautista Santiago**
* **Ramos Valencia Lorena**

---

## ⚠️ Aviso

Este proyecto es solo con fines educativos. No maneja dinero real ni información sensible. Toda la información simulada es para pruebas y demostración.
