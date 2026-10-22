package everest.screens.splashScreen;

import megalodonte.base.components.Component;
import megalodonte.base.components.ScreenComponent;
import megalodonte.components.SpacerVertical;
import megalodonte.components.Text;
import megalodonte.components.layout_components.Column;
import megalodonte.props.ColumnProps;
import megalodonte.props.TextProps;

public class SplashScreen implements ScreenComponent {

    @Override
    public Component render() {
        return new Column(new ColumnProps().spacingOf(16))
            .children(
                new SpacerVertical(100),
                new Text("Everest", new TextProps().fontSize(48)),
                new Text("REST API Client", new TextProps().fontSize(16)),
                new Text("Loading...", new TextProps().fontSize(14))
            );
    }
}