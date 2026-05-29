# CSVViewer & Developer Tools

A Kotlin Multiplatform desktop application providing essential tools for developers, built with Compose Multiplatform.

## Features

### 1. CSV Viewer
- Load and parse large CSV files (specifically tailored for log-style CSVs).
- Filter and search through records.
- View detailed information for each record, including formatted JSON request/response bodies.
- **New:** Text selection enabled for JSON details.

### 2. JWT Parser
- Decode and inspect JSON Web Tokens (JWT).
- View Header and Payload sections in an interactive tree structure.
- Signature inspection.
- **New:** Full text selection for all decoded sections.

## Project Structure

The project follows the standard Kotlin Multiplatform structure, with a focus on the Desktop target.

- `composeApp/src/desktopMain/kotlin/com/tools/csv_viewer/`
  - `csv/`: Components and logic for the CSV data grid and record details.
  - `jwt/`: Components for JWT decoding and JSON tree visualization.
  - `screens/`: Main screen layouts for the Menu, CSV Viewer, and JWT Parser.
  - `theme/`: Application-wide styling and themes.
  - `main.kt`: Entry point for the Desktop application.
  - `MainScreen.kt`: Main navigation and tool switching logic.

## Getting Started

### Prerequisites
- JDK 17 or higher
- Gradle (provided via `gradlew`)

### Running the Application
To launch the desktop application, run the following command:
```bash
./gradlew run
```

### Building the Application
To build the application for your current platform:
```bash
./gradlew assemble
```

## Technologies Used
- **Kotlin Multiplatform**
- **Compose Multiplatform** (Desktop)
- **JsonTree**: For interactive JSON visualization.
- **Apache Commons CSV**: For robust CSV parsing.
