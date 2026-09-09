![everestheader](https://user-images.githubusercontent.com/23148259/39124644-c886b47a-4719-11e8-953c-f079b3edb664.png)

Everest _(formerly RESTaurant)_ is an upcoming REST API testing client written in JavaFX.

![home](https://user-images.githubusercontent.com/23148259/45769743-23e5a380-bc5e-11e8-9e45-5ea50342c19f.PNG)
_Everest running on Windows 10._
# Why Everest?
- Everest is written in Java. Thus, it is significantly **lighter on resources and more responsive** than its Electron-based alternatives like _Postman_. It aims to provide the same level of functionality in a lighter, native but equally slick package.
- Aesthetic is very important. With a **gorgeous, flat design**, Everest is a pleasure to look at and to work with. It is also entirely theme-_able_.

  > I want you to want to use it!

- Being a Java application, Everest is inherently **cross-platform**. It will run anywhere there's a JVM.
- Everest will offer cloud synchronization of your projects powered by [Summit](https://github.com/RohitAwate/Summit). It will be available as a cloud service early next year or you may also choose to self-host it.

# Live Features 🔥

#### All of the most common requests
GET, POST, PUT, DELETE and PATCH requests. HEAD and OPTIONS coming soon.

#### Comprehensive Request Builder
- Add request headers.
- Append query parameters. _(with live preview)_
- **Syntax highlighting** for JSON and XML, powered by [RichTextFX](https://github.com/FXMisc/RichTextFX).
- Quickly add key-value pairs for URL-encoded and multipart-form bodies.
 
#### View Response Details 
- HTTP status code, content type, elapsed time, body size.
- **Visualizer** to view JSON responses graphically. _(Aesthetic improvements coming with Alpha 1.4)_
- View response headers.

![get](https://user-images.githubusercontent.com/23148259/45769777-3c55be00-bc5e-11e8-9fbc-c8bf93b7dc5d.gif)
_Making a GET request with Everest._

#### API Authentication
- Basic Auth
- Digest Auth

#### Custom Themes
Everest is entirely theme-_able_ via CSS. For more details, refer [this guide](THEMES.md).

#### Efficient multi-tabbing
Everest utilizes a technique called _pseudo tab-switching_ to maintain a low memory footprint even when heavy multi-tabbing. I have written a highly technical piece about this on my [dev.to](https://dev.to/rohit).

_**Bonus**: The '+' button for adding new tabs is finally live!_

#### History
Everest maintains the history of all the requests made by you. You can search with any of the request's components: the target, the headers, the method, the body or even the files added to the request. Everest will **intelligently rank** the results based on their relevance.

![history](https://user-images.githubusercontent.com/23148259/45769890-8a6ac180-bc5e-11e8-8f5e-6704eb0e9aa1.gif)

_Everest's search feature in action._

# Upcoming features ⏳
#### OAuth Support
Everest will fully support both of the OAuth standards. Work on **OAuth 2.0 is in progress** right now and will be available with Alpha 1.4. OAuth 1.0 will follow next. 

#### Everest Project
- This will be Everest's equivalent of Postman's Collection or Insomnia's Workspace.
- Will house **named-requests**.
- Will support **environment variables**. Every request can have its own.
- Can be exported/imported.

#### Summit
![summitheader](https://user-images.githubusercontent.com/23148259/45769968-cbfb6c80-bc5e-11e8-95c7-7d418dee54d5.png)
- [Summit](https://github.com/RohitAwate/Summit) is the synchronization server for Everest.
- It will allow for synchronization of your Everest Projects across multiple devices and other members of your team.
- It will use a Node.js-_powered_ RESTful API and Socket.IO.
- It will be available early next year as a service. You may also choose to self-host Summit.

#### Extension API
This will allow developers to create extensions for Everest which can for example, sync to Google Drive, or visualize the response bodies in a certain fashion or summon Batman.

#### Mock Server
This local server can be used to quickly create a mock REST-_ful_ service with the endpoints of your choice, producing the output of your choice.

# Keymap ⌨️
| Shortcut     | Task                     |
|--------------|--------------------------|
| Ctrl + T     | New Tab                  |
| Ctrl + W     | Close tab                |
| Ctrl + H     | Toggle History           |
| Ctrl + Enter | Send request             |
| Ctrl + L     | Focus address bar        |
| Ctrl + M     | Select HTTP method       |
| Ctrl + F     | Focus history search bar |
| Alt + P      | Focus Query Params tab   |
| Alt + A      | Focus Authentication tab |
| Alt + H      | Focus Headers tab        |
| Alt + B      | Focus Body tab           |

# Releases 🚀
Everest is under active development and you can get the latest alpha build from [Releases](https://github.com/RohitAwate/Everest/releases). Make sure you read the release notes to understand what works and what doesn't, how to report issues and how to run the binary.

# Building from source 🔨
Everest is built with **Gradle** using the **Megalodonte framework** (JavaFX-based declarative UI). It requires JDK 25 and JavaFX 25.0.1.

## Requirements
- **JDK 25** (Java toolchain)
- **JavaFX 25.0.1** platform libs (downloaded via the OpenJFX plugin)
- **Megalodonte framework** published to Maven Local (`megalodonte-base`, `megalodonte-components`, `megalodonte-reactivity`, `megalodonte-router`, `megalodonte-theme`, all `1.0.0-beta`)
- Environment variable `JAVAFX_MODULES_HOME` pointing to a folder containing `windows-25.0.1/` / `linux-25.0.1/` JavaFX platforms

## Steps
1. Clone the repository: `git clone https://github.com/RohitAwate/Everest.git`
2. Enter the repository: `cd Everest`
3. Build the fat JAR: `gradlew shadowJar`
4. Run: `gradlew run`

The deployable artifact is `build/libs/everest-2.0.0.jar` (standalone, includes all dependencies).

## Architecture (Megalodonte)
Since this migration, Everest uses the Megalodonte framework with a declarative, reactive UI:

| Aspect | Before | After |
|---|---|---|
| Build | Maven (pom.xml) | Gradle (Kotlin DSL) |
| Java | 1.8 | 25 |
| UI | FXML + JFoenix | Megalodonte components (`ScreenComponent`) |
| HTTP | Jersey JAX-RS | Java `java.net.http.HttpClient` |
| JSON | Jackson 2.9.3 | Jackson 2.17.0 |
| SQLite | 3.21.0.1 | 3.45.1.0 |
| Themes | External CSS | `ThemeInterface` (`Themes.LIGHT`/`Themes.DARK`) |
| Routing | N/A (single window) | Router v4 (`AppRoutes`) |

See `../megalodonte-skills/` for detailed reference documentation on the framework patterns.

# License ⚖️
Everest is licensed under the [Apache 2.0 License](LICENSE).

# Suggestions and improvements
Use these options to reach me:
- Open a GitHub issue.
- Email me at rohitawate121@gmail.com.
- Tweet me [@TheRohitAwate](https://twitter.com/TheRohitAwate).
