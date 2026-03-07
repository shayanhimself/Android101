# CLAUDE.md - Android 101 (Fake Store)

## Project Overview

Educational Android app demonstrating modern Android development with Jetpack Compose. Displays product data fetched from a Fake Store API with pull-to-refresh, loading/error states, and Material Design 3 theming.

**Package:** `com.shayan.android101`
**Architecture:** Single-module MVVM

## Tech Stack

- **Language:** Kotlin 2.0.0 (JVM target 11)
- **UI:** Jetpack Compose with Material 3 (BOM 2024.12.01)
- **Networking:** Ktor Client 3.0.3 (OkHttp engine, Gson serialization)
- **Image Loading:** Coil Compose 2.7.0
- **State Management:** StateFlow + ViewModel
- **Build:** Gradle 8.9 with Kotlin DSL, AGP 8.7.3
- **Min SDK:** 24 / **Target SDK:** 35

## Project Structure

```
app/src/main/java/com/shayan/android101/
├── MainActivity.kt              # Entry point, sets up Compose content
├── datamodel/Product.kt         # Product and Rating data classes
├── network/
│   ├── FakeStoreApi.kt          # API interface (fetches from fake-store-api)
│   └── KtorClient.kt           # Ktor HTTP client singleton
├── viewmodels/ProductViewModel.kt  # Manages product state (Loading/Product/Error)
└── ui/
    ├── components/MyAppBar.kt   # Custom TopAppBar composable
    ├── screens/ProductScreen.kt # Main product display screen
    └── theme/                   # Material 3 theme (Color, Dimens, Theme, Typography)
```

## Build & Run

```bash
./gradlew assembleDebug        # Build debug APK
./gradlew installDebug         # Install on connected device/emulator
```

## Testing

```bash
./gradlew test                 # Run unit tests
./gradlew connectedAndroidTest # Run instrumented tests (requires device/emulator)
```

Tests are in `app/src/test/` (unit) and `app/src/androidTest/` (instrumented). Currently contains example tests only.

## Key Patterns

- **MVVM:** ViewModels expose `StateFlow`, Compose screens collect via `collectAsStateWithLifecycle()`
- **Network client:** `KtorClient` is a Kotlin `object` singleton with OkHttp engine, Gson content negotiation, and logging
- **API:** Base URL is `https://shayanhimself.github.io/fake-store-api`, endpoint `/products/{id}.json` (IDs 1-5)
- **Three-state UI:** `ViewState` sealed pattern — Loading, Product (success), Error
- **Theme:** Custom Material 3 color scheme with light/dark support; spacing constants in `Dimens.kt`

## Dependencies

All dependencies are managed via the version catalog at `gradle/libs.versions.toml`. Key entries:
- `libs.ktor.*` — Ktor client modules
- `libs.coil.compose` — Image loading
- `libs.androidx.lifecycle.*` — ViewModel and lifecycle
- `libs.androidx.compose.*` — Compose UI via BOM

## Code Style

- Kotlin official code style (`kotlin.code.style=official` in `gradle.properties`)
- Composable functions use PascalCase
- Packages: `datamodel`, `network`, `viewmodels`, `ui.components`, `ui.screens`, `ui.theme`
- No linter (detekt/ktlint) configured
