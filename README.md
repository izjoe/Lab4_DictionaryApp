# Lab4_DictionaryApp

## Overview

This repository contains the Lab 4 homework project for Android mobile development.

The project is a **Dictionary Application** that allows users to search for word meanings, pronunciation, definitions, examples, synonyms, and related language information.

The application is designed to help users improve vocabulary and language learning through a clean mobile interface and real-time dictionary lookup.

The main purpose of this project is to practice Android development using API integration, asynchronous networking, Jetpack Compose, ViewModel, and modern UI design.

---

## Features

- Search English words
- View word definitions
- Display word meanings
- Show pronunciation
- Display phonetic spelling
- View example sentences
- Display synonyms and antonyms
- Online dictionary API integration
- Search history support
- Loading and error handling
- Modern Android UI
- Navigation between screens
- Responsive mobile layout

---

## Tech Stack

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Retrofit
- Gson Converter
- Coroutines
- Navigation Compose
- ViewModel
- Coil (if images/icons are used)
- Gradle Kotlin DSL

---

## Project Structure

```text
Lab4_DictionaryApp/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/dictionaryapp/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   ├── network/
│   │   │   │   │   └── repository/
│   │   │   │   ├── navigation/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/
│   │   │   │   │   ├── components/
│   │   │   │   │   └── theme/
│   │   │   │   └── viewmodel/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

---

## Main Components

### MainActivity.kt

`MainActivity.kt` is the main entry point of the application.

It initializes the Compose UI and controls screen navigation.

Main responsibilities:

- Start the application
- Apply app theme
- Initialize navigation
- Open dictionary search screen

---

### data/model

The `model` package contains dictionary-related data classes.

It may include:

- Word model
- Definition model
- Meaning model
- Phonetic model
- API response model

These models are used to parse dictionary API responses.

---

### data/network

The `network` package handles API communication.

Responsibilities include:

- Creating Retrofit service
- Sending dictionary search requests
- Receiving API responses
- Configuring base URL

The app fetches word information from an online dictionary API. Dictionary APIs typically support word meanings, pronunciation, examples, and synonyms. :contentReference[oaicite:1]{index=1}

---

### data/repository

The `repository` package acts as a bridge between the API layer and UI layer.

Responsibilities include:

- Searching words
- Returning dictionary data
- Handling API response errors
- Managing data flow

---

### navigation

The `navigation` package controls app screen transitions.

Typical navigation flow:

```text
Search Screen → Word Detail Screen
```

Users can search for a word and view detailed information.

---

### ui/screens

The `screens` package contains application screens.

Possible screens include:

- Home/Search screen
- Word detail screen
- Search history screen

---

### ui/components

The `components` package contains reusable UI components.

Examples:

- Search bar
- Word card
- Definition section
- Pronunciation section
- Example section

---

### viewmodel

The `viewmodel` package manages UI state and business logic.

Responsibilities include:

- Searching words
- Managing loading state
- Updating UI
- Handling API responses
- Error handling

---

## API Setup

This app may use an online dictionary API.

Some dictionary apps use:

- Free Dictionary API
- DictionaryAPI.dev
- Merriam-Webster API

If an API key is required, do not commit it directly to GitHub. Dictionary apps commonly use online APIs for word definitions and pronunciation. :contentReference[oaicite:2]{index=2}

Recommended setup:

Add the API key to `local.properties`:

```properties
DICTIONARY_API_KEY=your_api_key_here
```

Then load it through `BuildConfig`.

Example:

```kotlin
val apiKey = BuildConfig.DICTIONARY_API_KEY
```

---

## How to Run

### Requirements

Before running the project, make sure you have installed:

- Android Studio
- Android SDK
- JDK 17 or higher
- Android Emulator or a real Android device
- Internet connection
- Dictionary API key (if required)

---

### Run with Android Studio

1. Clone this repository:

```bash
git clone https://github.com/izjoe/Lab4_DictionaryApp.git
```

2. Open **Android Studio**.

3. Choose **Open an Existing Project**.

4. Select the cloned project folder:

```text
Lab4_DictionaryApp
```

5. Configure the API key if needed.

Example in `local.properties`:

```properties
DICTIONARY_API_KEY=your_api_key_here
```

6. Wait for Gradle sync.

7. Select an Android emulator or connect a real Android device.

8. Click the **Run** button.

9. The application will be built and launched.

---

### Run with Terminal

For macOS or Linux:

```bash
./gradlew build
```

For Windows:

```bash
gradlew.bat build
```

To install the debug version:

```bash
./gradlew installDebug
```

---

## How to Use

1. Open the application.
2. Enter a word in the search bar.
3. Tap the search button.
4. Wait for the result to load.
5. View:

- Word definition
- Pronunciation
- Example sentence
- Synonyms
- Antonyms

6. Search another word if needed.

---

## Demo

Demo folder:

```text
https://drive.google.com/drive/u/0/folders/1OWWOfUnb6ZgYrqEV3iq92Jf93EvwMupE
```

---

## Notes

- This is a Lab 4 Android homework project.
- The app requires internet connection.
- A dictionary API may be required.
- API keys should not be pushed to GitHub.
- Real devices may provide better performance than emulators.

---

## Future Improvements

- Add offline dictionary support
- Add favorite words
- Add pronunciation audio
- Add dark mode
- Add search history
- Add recent searches
- Add translation feature
- Improve UI design
- Add multiple language support
- Improve error handling
- Add word-of-the-day feature

---

## Author

**Nguyễn Bảo Châu**

- University: University of Information Technology – VNUHCM
- Major: Information System
- Email: baochaune21@gmail.com

---

## License

This project is used for educational purposes.
