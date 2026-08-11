# VCamera - Virtual Camera

A modern Android virtual camera application built with Kotlin and Jetpack Compose.

## Features

- **Virtual Camera**: Replace your camera input with local videos, images, or network streams
- **Multi-Protocol Support**: RTSP, HLS, DASH, and direct video streaming
- **Xposed Framework Integration**: Hook into other apps' camera calls
- **Modern UI**: Built with Jetpack Compose and Material 3
- **Clean Architecture**: MVVM with clean architecture principles

## Supported Protocols

- RTSP (Real Time Streaming Protocol)
- HLS (HTTP Live Streaming)
- DASH (Dynamic Adaptive Streaming over HTTP)
- Direct MP4/WebM playback

## Requirements

- Android 9.0 (API 28) or higher
- Xposed Framework (for camera hooking features)

## Building

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

## GitHub Actions

This project includes GitHub Actions workflow for automatic APK building. Every push to main/master will trigger a build and produce:
- Debug APK
- Release APK

You can download the APKs from the Actions tab in your GitHub repository.

## Project Structure

```
VCamera/
├── app/                    # Main application module
│   ├── camera/            # Camera engine and hooking
│   ├── data/              # Data layer (Room, repositories)
│   ├── di/                # Dependency injection (Hilt)
│   ├── domain/            # Domain layer (models, use cases)
│   ├── media/             # Media playback (ExoPlayer, MediaPlayer)
│   ├── presentation/      # UI layer (Jetpack Compose)
│   ├── service/           # Background services
│   └── util/              # Utility classes
├── core/                   # Shared core utilities
├── xposed/                 # Xposed module for camera hooking
└── .github/workflows/     # CI/CD configuration
```

## Architecture

- **Clean Architecture**: Separate data, domain, and presentation layers
- **MVVM**: Model-View-ViewModel pattern
- **Hilt**: Dependency injection
- **Room**: Local database
- **Jetpack Compose**: Modern declarative UI

## License

MIT License
