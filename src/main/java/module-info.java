module everest {
    requires java.net.http;
    requires java.sql;

    requires transitive megalodonte.base;
    requires transitive megalodonte.components;
    requires transitive megalodonte.reactivity;
    // Migrated from the non-existent megalodonte.router.v4 API to the current
    // megalodonte.router.v5. `Router` now takes a megalodonte.base.route.RouteTable,
    // routes are plain megalodonte.base.route.Route records, and screens receive a
    // megalodonte.base.route.v2.ScreenContextInterface (was v4.ScreenContext).
    requires megalodonte.router;

    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;

    requires org.slf4j;
    requires org.xerial.sqlitejdbc;

    requires static lombok;
    requires static ch.qos.logback.classic;

    exports everest;
}