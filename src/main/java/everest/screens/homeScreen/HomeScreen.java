package everest.screens.homeScreen;

import everest.models.requests.*;
import everest.models.responses.EverestResponse;
import everest.services.RequestService;
import everest.sync.SQLiteManager;
import megalodonte.base.state.State;
import megalodonte.base.UI;
import megalodonte.base.async.Async;
import megalodonte.base.components.Component;
import megalodonte.base.components.ScreenComponent;
import megalodonte.components.Button;
import megalodonte.components.Select;
import megalodonte.components.Text;
import megalodonte.components.inputs.TextAreaInput;
import megalodonte.components.layout_components.Column;
import megalodonte.components.layout_components.Row;
import megalodonte.components.v2.Input;
import megalodonte.props.ButtonProps;
import megalodonte.props.ColumnProps;
import megalodonte.props.InputProps;
import megalodonte.props.RowProps;
import megalodonte.props.SelectProps;
import megalodonte.props.TextProps;
import megalodonte.base.route.v2.ScreenContextInterface;
import megalodonte.v2.Show;

import java.util.List;
import java.util.Map;

public class HomeScreen implements ScreenComponent {

    private static final List<String> HTTP_METHODS = List.of("GET", "POST", "PUT", "PATCH", "DELETE");
    private static final List<String> RESPONSE_TYPES = List.of("JSON", "XML", "HTML", "Text");
    private static final List<String> AUTH_TYPES = List.of("none", "Basic", "Digest");

    private final State<String> url = State.of("https://");
    private final State<String> httpMethod = State.of("GET");
    private final State<String> requestBody = State.of("");
    private final State<String> requestTypeSelected = State.of("JSON");
    private final State<String> requestContentType = State.of("application/json");
    private final State<String> responseBody = State.of("");
    private final State<Integer> statusCode = State.of(0);
    private final State<String> statusText = State.of("");
    private final State<Long> responseTime = State.of(0L);
    private final State<Integer> responseSize = State.of(0);
    private final State<Boolean> isLoading = State.of(false);
    private final State<String> activeTab = State.of("body");
    private final State<String> authType = State.of("none");
    private final State<String> authUsername = State.of("");
    private final State<String> authPassword = State.of("");
    private final State<Map<String, String>> headers = State.of(Map.of());

    private final ScreenContextInterface ctx;

    public HomeScreen(ScreenContextInterface ctx) {
        this.ctx = ctx;
    }

    @Override
    public Component render() {
        return new Column(new ColumnProps().spacingOf(8))
            .children(
                renderMethodBar(),
                renderRequestOptions(),
                renderResponseSection()
            );
    }

    private Component renderMethodBar() {
        return new Row(new RowProps().spacingOf(8))
            .children(
                new Select<String>(new SelectProps().minWidth(100))
                    .items(HTTP_METHODS)
                    .value(httpMethod),
                new Input(url, new megalodonte.props.v2.InputProps().placeHolder("Enter URL...").fillWidth()),
                Show.when(
                    isLoading,
                    () -> new Button("Sending...", new ButtonProps()),
                    () -> new Button("Send", new ButtonProps()).onClick(this::sendRequest)
                )
            );
    }

    private Component renderRequestOptions() {
        return new Column(new ColumnProps().spacingOf(8))
            .children(
                renderTabs(),
                renderTabContent()
            );
    }

    private Component renderTabs() {
        return new Row(new RowProps().spacingOf(4))
            .children(
                new Button("Params", new ButtonProps().height(28)).onClick(() -> activeTab.set("params")),
                new Button("Headers", new ButtonProps().height(28)).onClick(() -> activeTab.set("headers")),
                new Button("Body", new ButtonProps().height(28)).onClick(() -> activeTab.set("body")),
                new Button("Auth", new ButtonProps().height(28)).onClick(() -> activeTab.set("auth"))
            );
    }

    private Component renderTabContent() {
        return Show.when(
            activeTab.map(tab -> tab.equals("body")),
            this::renderBodyTab,
            () -> Show.when(
                activeTab.map(tab -> tab.equals("auth")),
                this::renderAuthTab,
                () -> Show.when(
                    activeTab.map(tab -> tab.equals("headers")),
                    this::renderHeadersTab,
                    this::renderParamsTab
                )
            )
        );
    }

    private Component renderBodyTab() {
        return new Column(new ColumnProps().spacingOf(8))
            .children(
                new Row(new RowProps().spacingOf(8))
                    .children(
                        new Select<String>(new SelectProps().minWidth(120))
                            .items(RESPONSE_TYPES)
                            .value(requestTypeSelected),
                        new TextAreaInput(requestBody, new InputProps().placeHolder("Request body...").minHeight(150))
                    )
            );
    }

    private Component renderAuthTab() {
        return new Column(new ColumnProps().spacingOf(8))
            .children(
                new Select<String>(new SelectProps().minWidth(120))
                    .items(AUTH_TYPES)
                    .value(authType),
                Show.when(
                    authType.map(t -> !t.equals("none")),
                    () -> new Column(new ColumnProps().spacingOf(8))
                        .children(
                            new Input(authUsername, new megalodonte.props.v2.InputProps().placeHolder("Username").fillWidth()),
                            new Input(authPassword, new megalodonte.props.v2.InputProps().placeHolder("Password").fillWidth())
                        )
                )
            );
    }

    private Component renderHeadersTab() {
        return new Text("Headers configuration", new TextProps().fontSize(14));
    }

    private Component renderParamsTab() {
        return new Text("Query parameters", new TextProps().fontSize(14));
    }

    private Component renderResponseSection() {
        return new Column(new ColumnProps().spacingOf(8))
            .children(
                renderResponseInfo(),
                new TextAreaInput(responseBody, new InputProps().placeHolder("Response will appear here...").minHeight(300).editable(false))
            );
    }

    private Component renderResponseInfo() {
        return new Row(new RowProps().spacingOf(12))
            .children(
                new Text("Status: ", new TextProps().fontSize(13)),
                new Text(statusCode.map(String::valueOf), new TextProps().fontSize(13)),
                new Text(statusText, new TextProps().fontSize(13)),
                new Text("Time: ", new TextProps().fontSize(13)),
                new Text(responseTime.map(t -> t + "ms"), new TextProps().fontSize(13)),
                new Text("Size: ", new TextProps().fontSize(13)),
                new Text(responseSize.map(s -> s + "B"), new TextProps().fontSize(13))
            );
    }

    private void sendRequest() {
        String currentUrl = url.get();
        if (currentUrl == null || currentUrl.isBlank()) {
            clearResponseInfo();
            responseBody.set("Enter a URL before sending a request.");
            return;
        }

        clearResponseInfo();
        responseBody.set("");
        isLoading.set(true);

        try {
            EverestRequest request = createRequest();
            if (request == null) {
                isLoading.set(false);
                return;
            }

            RequestService.executeAsync(request).whenComplete((response, error) -> {
                UI.runOnUi(() -> {
                    isLoading.set(false);
                    if (error != null) {
                        responseBody.set("Error: " + unwrap(error));
                        statusText.set("Error");
                    } else {
                        displayResponse(response);
                        saveToHistory(request, response);
                    }
                });
            });
        } catch (Exception e) {
            isLoading.set(false);
            responseBody.set("Invalid request: " + e.getMessage());
            statusText.set("Invalid request");
        }
    }

    private void clearResponseInfo() {
        statusCode.set(0);
        statusText.set("");
        responseTime.set(0L);
        responseSize.set(0);
    }

    private EverestRequest createRequest() throws Exception {
        String currentUrl = url.get();
        String method = httpMethod.get();

        return switch (method) {
            case "GET" -> {
                GETRequest req = new GETRequest();
                req.setTarget(currentUrl);
                req.setHeaders(new java.util.HashMap<>(headers.get()));
                yield req;
            }
            case "POST", "PUT", "PATCH" -> {
                DataRequest req = new DataRequest();
                req.setRequestType(method);
                req.setTarget(currentUrl);
                req.setBody(requestBody.get());
                requestContentType.set(switch (requestTypeSelected.get()) {
                    case "XML" -> HTTPConstants.CONTENT_TYPE_XML;
                    case "HTML" -> HTTPConstants.CONTENT_TYPE_HTML;
                    case "Text" -> HTTPConstants.CONTENT_TYPE_PLAIN;
                    default -> HTTPConstants.CONTENT_TYPE_JSON;
                });
                req.setContentType(requestContentType.get());
                req.setHeaders(new java.util.HashMap<>(headers.get()));
                yield req;
            }
            case "DELETE" -> {
                DELETERequest req = new DELETERequest();
                req.setTarget(currentUrl);
                req.setHeaders(new java.util.HashMap<>(headers.get()));
                yield req;
            }
            default -> null;
        };
    }

    private void displayResponse(EverestResponse response) {
        statusCode.set(response.getStatusCode());
        statusText.set(EverestResponse.getReasonPhrase(response.getStatusCode()));
        responseTime.set(response.getTime());
        responseSize.set(response.getSize());
        responseBody.set(response.getBody() != null ? response.getBody() : "No body returned.");
    }

    private void saveToHistory(EverestRequest request, EverestResponse response) {
        Async.Run(() -> {
            SQLiteManager.saveRequest(
                httpMethod.get(),
                url.get(),
                headers.get().toString(),
                requestBody.get(),
                requestContentType.get(),
                response.getStatusCode(),
                response.getBody(),
                response.getTime(),
                response.getSize()
            );
        });
    }

    private static String unwrap(Throwable t) {
        Throwable current = t;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current.getMessage() != null ? current.getMessage() : current.getClass().getSimpleName();
    }
}
