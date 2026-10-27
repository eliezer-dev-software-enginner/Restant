# Everest / Restant

This repository contains a fork of [Everest](https://github.com/RohitAwate/Everest), a desktop REST API client. The current application identifies itself as **Everest** in its build and window; **Restant** is the name of the fork's repository. It is written in Java with JavaFX and the Megalodonte UI framework.

## Current functionality

- Send GET, POST, PUT, PATCH and DELETE requests to an entered URL.
- Enter a text body for POST, PUT and PATCH, selecting JSON, XML, HTML or plain text as its content type.
- Display Basic and Digest authentication fields in the **Auth** tab. The current screen does not attach either selection to outgoing requests.
- See the response status, elapsed time, size and body.
- Save successful request details and responses to a local SQLite database at `~/.everest/history.sqlite` (the user's home directory).

The **Headers** and **Params** tabs currently display placeholders; they do not provide editors. The database stores history, but the current screen has no history browser or search. Themes are defined in code, while startup selects the light theme. These areas, along with keyboard shortcuts, response visualization and multipart forms, should not be assumed to work in this version.

## Build and run from source

Requirements:

- JDK 25. Gradle uses a Java 25 toolchain.
- The five Megalodonte `1.0.0-beta` modules (`base`, `components`, `reactivity`, `router`, `theme`) available in Maven Local. The build checks Maven Local and Maven Central; this repository does not include those modules or instructions for producing them.
- For `run`, JavaFX 25.0.1 platform libraries in a directory named `linux-25.0.1/lib` or `windows-25.0.1/lib` beneath `JAVAFX_MODULES_HOME`, according to the operating system. The Gradle OpenJFX plugin also resolves JavaFX dependencies for the build.

From the repository root, after satisfying those requirements:

```sh
# Linux: point to the parent directory containing linux-25.0.1/lib
export JAVAFX_MODULES_HOME=/path/to/javafx-platforms
./gradlew run
```

On Windows, set `JAVAFX_MODULES_HOME` to the corresponding parent directory and use `gradlew.bat run`. The `run` task selects the Windows path on Windows and the Linux path on other systems; macOS runtime support is not configured here.

To build the JAR:

```sh
./gradlew shadowJar
```

On Windows, use `gradlew.bat shadowJar`. The output name is derived from `appName`, `appVersion` and `appPatch` in `gradle.properties` (currently `build/libs/everest-2.0.0.jar`). The build runs tests before creating the JAR. JavaFX is excluded from that JAR, so it is not a fully self-contained runtime artifact. Packaging configuration and platform requirements are described in [scripts/PACKAGING.md](scripts/PACKAGING.md).

## Usage

1. Enter a complete HTTP or HTTPS URL, for example `https://example.com/`.
2. Select a method. For POST, PUT or PATCH, enter a body and select its format in the **Body** tab.
3. Click **Send** to view the response. Request and response details are saved locally after a response is received.

## Historical Everest material

The following images show the original Everest interface and may not match this fork's current screen. The build still declares [RichTextFX](https://github.com/FXMisc/RichTextFX), though the current screen does not expose its syntax highlighting:

![Original Everest header](https://user-images.githubusercontent.com/23148259/39124644-c886b47a-4719-11e8-953c-f079b3edb664.png)

![Original Everest home screen on Windows 10](https://user-images.githubusercontent.com/23148259/45769743-23e5a380-bc5e-11e8-9e45-5ea50342c19f.PNG)

![Original Everest GET request demonstration](https://user-images.githubusercontent.com/23148259/45769777-3c55be00-bc5e-11e8-9fbc-c8bf93b7dc5d.gif)

![Original Everest history search demonstration](https://user-images.githubusercontent.com/23148259/45769890-8a6ac180-bc5e-11e8-8f5e-6704eb0e9aa1.gif)

## Credits and license

The original Everest project is by [Rohit Awate](https://github.com/RohitAwate/Everest), as also recorded in `gradle.properties`. This repository is a fork. The repository includes the [Apache License 2.0](LICENSE).

For feedback, open an issue in this repository.
