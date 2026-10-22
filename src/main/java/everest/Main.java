package everest;

import java.nio.file.Path;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import megalodonte.ListenerManager;
import megalodonte.application.Context;
import megalodonte.application.ErrorReporter;
import megalodonte.application.MegalodonteApp;
import megalodonte.application.MegalodonteApplication;
import megalodonte.base.UI;
import megalodonte.base.async.Async;
import megalodonte.base.theme.ThemeManager;
import megalodonte.base.route.RouteTable;
import megalodonte.router.v5.Router;
import everest.core.AppRoutes;
import everest.core.Themes;
import everest.sync.SQLiteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final boolean devMode = "true".equals(System.getenv("DEV_MODE"));

    public static final String APP_NAME = "Everest";

    public static final String APP_VERSION = System.getProperty("everest.appVersion", "dev");

    public static final String BASE_TITLE = String.format("%s - %s", APP_NAME, APP_VERSION);

    public static final String ICON_PATH = "/assets/Logo.png";

    public static Image loadIcon() {
        return new Image(Objects.requireNonNull(Main.class.getResourceAsStream(ICON_PATH)));
    }

    public static class AppHost extends MegalodonteApplication {}

    public static void main(String[] args) {
        log.info("Starting {} version {}", APP_NAME, APP_VERSION);
        MegalodonteApp.appName(APP_NAME);
        MegalodonteApp.appIcon(ICON_PATH);
        MegalodonteApp.run(AppHost.class, args, context -> {
            final var stage = context.javafxStage();
            stage.getIcons().add(loadIcon());
            ErrorReporter.register(Main::handleAppError);
            initialize(context);
        }, event -> {
            if (event == MegalodonteApp.Event.CloseRequest) {
                handleClose();
            }
        });
    }

    public static void handleClose() {
        log.info("Shutting down {}", APP_NAME);
        ListenerManager.disposeAll();
        SQLiteManager.close();
        Platform.exit();
        System.exit(0);
    }

    public static void initialize(Context context) {
        ThemeManager.setTheme(Themes.LIGHT);

        var routes = new AppRoutes().routes();
        Router router = new Router(new RouteTable(routes, AppRoutes.Screens.SPLASH.name()));
        context.useRouter(router).start();

        Async.Run(() -> {
            SQLiteManager.initialize();
            log.info("Database initialized successfully");

            String initialRoute = AppRoutes.Screens.HOME.name();

            UI.runOnUi(() -> {
                var result = router.navigateOnStage(initialRoute, context.javafxStage());
                context.useView(result);
            });
        });
    }

    private static void handleAppError(Throwable t) {
        log.error("Unhandled application error", t);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(t instanceof IllegalArgumentException
                ? t.getMessage()
                : "An unexpected error occurred. Details have been logged.");
            alert.show();
        });
    }
}
