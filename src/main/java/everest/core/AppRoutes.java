package everest.core;

import megalodonte.base.route.Route;
import megalodonte.base.route.RouteProps;
import everest.Main;
import everest.screens.splashScreen.SplashScreen;
import everest.screens.homeScreen.HomeScreen;

import java.util.Set;

public class AppRoutes {
    public enum Screens {
        SPLASH,
        HOME,
        DASHBOARD
    }

    final int MIN_WIDTH = 600;
    final int MIN_HEIGHT = 500;

    final int MAX_WIDTH = 1100;
    final int MAX_HEIGHT = 650;

    public Set<Route> routes() {
        return Set.of(
            new Route(Screens.SPLASH.name(), ctx -> new SplashScreen(),
                new RouteProps(MIN_WIDTH, MIN_HEIGHT, Main.BASE_TITLE, false)),
            new Route(Screens.HOME.name(), HomeScreen::new,
                new RouteProps(MAX_WIDTH, MAX_HEIGHT, Main.BASE_TITLE, true))
        );
    }
}
