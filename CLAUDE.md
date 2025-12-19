# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

JikMBC is an Android application built with Jetpack Compose that displays content (entertainment and drama) with video trailers. The app uses a multi-module architecture organized by features and core functionality.

## Build Commands

```bash
# Build the entire project
./gradlew build

# Assemble debug APK
./gradlew assembleDebug

# Assemble release APK
./gradlew assembleRelease

# Run all tests
./gradlew test

# Run tests for a specific module (example)
./gradlew :feature:home:test

# Run instrumented tests (requires emulator/device)
./gradlew connectedAndroidTest

# Clean build artifacts
./gradlew clean

# Install debug build on connected device
./gradlew installDebug
```

## Module Architecture

The project uses TYPESAFE_PROJECT_ACCESSORS for type-safe module dependencies (e.g., `projects.core.ui`).

### Core Modules

- **`core:model`** - Domain models and data classes
  - `Content` data class with properties for content display
  - `ContentCategory` enum (ENTERTAINMENT, DRAMA)

- **`core:data`** - Data layer with repositories
  - `ContentRepository` interface defining data operations
  - `ContentRepositoryImpl` currently using mock data (MockEntertainment, MockDramas)
  - Mock data sources in `jik.imbc.data.mock` package

- **`core:ui`** - Shared UI utilities and components
  - **Package structure:**
    - `transition/` - Shared element transition support
      - `SharedTransitionLocals.kt` - Provides local shared transition scope
      - `SharedElementsKeys.kt` - Defines keys for shared elements
    - `palette/` - Palette extraction utilities for dynamic theming
      - `Palette.kt` - `extractRepresentativeColor()` for extracting colors from images
    - `layout/` - Custom layouts and modifiers
      - `EffectColumn.kt` - Custom column layout with effects
      - `Modifiers.kt` - Custom modifiers including `noRippleClickable()`
    - `count/` - Animated counter components
      - `AnimatedCounter.kt` - Animated numeric counter
    - `action/` - Click effects and interaction utilities
      - `ClickEffect.kt` - Click effect utilities

- **`core:designsystem`** - Design system components
  - Material3 theme configuration
  - Custom components (Chip, etc.)
  - Icons (`JbcIcons`)
  - Loading states (`EmptyLoading`)

### Feature Modules

Feature modules follow a consistent structure with navigation, ViewModels, and UI screens.

- **`feature:home`** - Home screen displaying content
  - `HomeScreen.kt` - Main UI with pager and content cards
  - `HomeViewModel.kt` - State management
  - `HomeUiState` - UI state sealed class
  - Navigation: Uses `HomeRoute` as entry point
  - Dependencies: core:data, core:model, core:ui, core:designsystem

- **`feature:detail`** - Content detail screen
  - `DetailScreen.kt` - Content details with trailer playback
  - `DetailViewModel.kt` - State management
  - Navigation: Type-safe navigation from home screen with contentId and origin
  - Dependencies: core:data, core:model, core:ui, core:designsystem, lib:videoplayer

### Library Modules

- **`lib:videoplayer`** - Custom video player implementation
  - Built with Media3 ExoPlayer
  - **Package structure:**
    - `player/trailer/` - Core trailer player implementation
      - `TrailerPlayer` - ExoPlayer wrapper for trailer playback
      - `TrailerPlayerState` - Player state management (INITIAL, CAN_PLAY, PAUSED, etc.)
      - `TrailerPlayerListener` - Player event handling
    - `player/vod/` - VOD (Video On Demand) player implementation
      - `VodPlayer` - Core VOD player
      - `VodPlayerState` - Player state management with `ActiveState` sealed interface
      - `VodPlayerListener` - Player event handling
      - `component/` - VOD player UI components
        - `ControllerIcon.kt` - Reusable icon component for player controls
    - `trailer/` - Trailer UI components
      - `TrailerSection` - Main composable combining player and controller
      - `TrailerViewModel` - Trailer state management
      - `TrailerUiState` - UI state for trailer playback
      - `TrailerThumbnail` - Video thumbnail display
    - `vod/` - VOD feature screens
      - `VodScreen` - VOD playback screen with controller (top, center, bottom controls)
      - `VodViewModel` - VOD state management
      - `VodUiState` - UI state for VOD playback
      - `VodActivity` - Activity for VOD playback
    - `component/` - Reusable UI components
      - `Slider.kt` - `VPSlider` custom slider with animated thumb and track
    - `icons/` - Video player icon resources
      - `VideoPlayerIcons` - Icon definitions for player controls (PlayArrow, Pause, Replay, etc.)
    - `screen/` - Screen utilities
      - `ImmersiveMode.kt` - Immersive mode extension for full-screen playback
  - **Key dependencies:**
    - Media3 ExoPlayer for video playback
    - Media3 UI Compose for PlayerSurface
    - Activity Compose for Compose integration in activities
  - **VOD Player Features:**
    - **Controller UI:**
      - Top controller: Back button and title display
      - Center controller: Backward (10s), Play/Pause/Replay, Forward (10s) controls
      - Bottom controller: Timeline slider (planned)
      - Gradient background for better visibility
    - **Auto-hiding controller:** Fades out after 3 seconds during playback
    - **Click-to-toggle:** Tap screen to show/hide controller
    - **State management:** Uses `ActiveState` sealed interface for active player states
    - **Immersive mode:** Full-screen experience with hidden system bars
  - **Trailer Player Features:**
    - Thumbnail preview before playback
    - Animated playback state indicators
    - Custom slider for seek control

### App Module

- **`app`** - Main application module
  - **Files:**
    - `MainActivity.kt` - Single activity host
    - `JbcNavHost.kt` - Navigation graph setup
  - **Configuration:**
    - Application ID: `jik.imbc.jikmbc`
    - Dependencies: feature:home, feature:detail, core:ui, core:designsystem
  - **Navigation:**
    - Uses Navigation Compose with type-safe navigation
    - Start destination: `HomeRoute`
    - Navigation from home → detail passes `contentId` and `origin` (for shared element transitions)
    - Each feature module exposes navigation extensions (`homeScreen()`, `detailScreen()`, `navigateDetail()`)

## Key Technical Details

### SDK Configuration
- compileSdk: 36
- minSdk: 26
- targetSdk: 36
- Java/Kotlin target: Java 17

### Dependencies Management
- Uses version catalog (`gradle/libs.versions.toml`)
- **Key versions:**
  - Kotlin: 2.2.21
  - Android Gradle Plugin: 8.11.2
  - Compose BOM: 2025.11.01 (for unified Compose versions)
  - Navigation Compose: 2.9.6
  - Media3 ExoPlayer: 1.8.0
  - Coil: 2.7.0
  - Lifecycle: 2.10.0
- **Key libraries:**
  - Navigation Compose for type-safe navigation
  - Coil for image loading
  - Palette API for color extraction
  - Media3 ExoPlayer for video playback
  - Material3 for UI components

### Shared Element Transitions
The app uses Compose's experimental SharedTransitionApi:
- `LocalSharedTransitionScope` and `LocalAnimatedContentScope` for transition coordination
- `SharedElementsKeys.kt` defines keys for shared elements
- `ContentCardElementOrigin` tracks transition origins

### Dynamic Theming
The app extracts representative colors from content images using the Palette API:
- `extractRepresentativeColor()` in core:ui
- Used for animated background gradients in home screen

### Data Layer
Currently using mock data:
- No network layer implemented yet
- Repository pattern in place for future real data integration
- Mock data in `jik.imbc.data.mock` package (Entertainment.kt, Drama.kt)

## Development Workflow

### Adding New Features
1. Create feature module if needed (follow existing feature module structure)
2. Add module to `settings.gradle.kts`
3. Implement navigation in feature module following existing patterns
4. Register navigation in `JbcNavHost.kt`
5. Add dependencies using type-safe project accessors

### Modifying UI
- UI components should use Material3 from designsystem
- Shared UI utilities go in core:ui
- Feature-specific UI stays within feature modules
- Use `noRippleClickable()` from core:ui for clickable elements without ripple effect

### Working with Video Player
- **Trailer player:** Use `TrailerSection` composable from lib:videoplayer
- **VOD player:** Launch `VodActivity` for full-screen playback
- **Custom controls:** Extend existing controller components in respective `component/` directories
- **Player states:** Both players use sealed interfaces for state management (`TrailerPlayerState`, `VodPlayerState`)
- **Immersive mode:** Use `applyImmersiveMode()` extension for full-screen activities
