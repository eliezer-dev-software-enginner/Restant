package everest.core;

import megalodonte.base.theme.*;

public class Themes {
    public static final ThemeInterface DARK = new ThemeInterface() {
        @Override
        public ThemeColors colors() {
            return new ThemeColors(
                "#1e293b",
                "#1a2235",
                "#60a5fa",
                "#334155",
                "#ffffff",
                "#94a3b8",
                "#334155",
                "#A9A9A9",
                "#1e3a5f",
                "#93c5fd",
                "#2d3a4f",
                "#10b981",
                "#f59e0b",
                "#ef4444"
            );
        }

        @Override
        public ThemeTypography typography() {
            return new ThemeTypography("Roboto", 35, 20, 16, 13);
        }

        @Override
        public ThemeSpacing spacing() {
            return new ThemeSpacing(4, 8, 12, 20, 32);
        }

        @Override
        public ThemePadding padding() {
            return new ThemePadding(4, 8, 12, 20, 32);
        }

        @Override
        public ThemeBorder border() {
            return new ThemeBorder(1, 4, 8, 12);
        }
    };

    public static final ThemeInterface LIGHT = new ThemeInterface() {
        @Override
        public ThemeColors colors() {
            return new ThemeColors(
                "#f7f7f7",
                "#ffffff",
                "#2563eb",
                "#dbeafe",
                "#000000",
                "#5a5a5a",
                "#cccccc",
                "#9ca3af",
                "#dbeafe",
                "#3b82f6",
                "#eff6ff",
                "#10b981",
                "#f59e0b",
                "#ef4444"
            );
        }

        @Override
        public ThemeTypography typography() {
            return new ThemeTypography("Roboto", 35, 20, 16, 13);
        }

        @Override
        public ThemeSpacing spacing() {
            return new ThemeSpacing(4, 8, 12, 20, 32);
        }

        @Override
        public ThemePadding padding() {
            return new ThemePadding(4, 8, 12, 20, 32);
        }

        @Override
        public ThemeBorder border() {
            return new ThemeBorder(1, 4, 8, 12);
        }
    };
}
