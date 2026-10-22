package everest.services;

import everest.Main;
import everest.models.requests.*;
import everest.models.responses.EverestResponse;
import megalodonte.base.async.Async;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RequestService {
    private static final HttpClient client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .connectTimeout(Duration.ofSeconds(30))
        .build();

    private static final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

    public static CompletableFuture<EverestResponse> executeAsync(EverestRequest request) {
        return CompletableFuture.supplyAsync(() -> execute(request), executor);
    }

    public static EverestResponse execute(EverestRequest request) {
        try {
            long initialTime = System.currentTimeMillis();

            HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                .uri(request.getTarget().toURI())
                .timeout(Duration.ofSeconds(30));

            appendHeaders(httpRequestBuilder, request);
            appendAuthHeader(httpRequestBuilder, request);

            HttpRequest httpRequest = buildRequest(httpRequestBuilder, request);
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            long finalTime = System.currentTimeMillis();

            return mapResponse(httpResponse, initialTime, finalTime);
        } catch (Exception e) {
            throw new RuntimeException("Request execution failed: " + e.getMessage(), e);
        }
    }

    private static void appendHeaders(HttpRequest.Builder builder, EverestRequest request) {
        request.getHeaders().forEach(builder::header);
        builder.header("User-Agent", Main.APP_NAME);
    }

    private static void appendAuthHeader(HttpRequest.Builder builder, EverestRequest request) {
        if (request.getAuthProvider() != null && request.getAuthProvider().isEnabled()) {
            builder.header("Authorization", request.getAuthProvider().getAuthHeader());
        }
    }

    private static HttpRequest buildRequest(HttpRequest.Builder builder, EverestRequest request) throws IOException {
        if (request instanceof DataRequest dataRequest) {
            String contentType = dataRequest.getContentType();
            String overriddenContentType = request.getHeaders().get("Content-Type");
            if (overriddenContentType != null) contentType = overriddenContentType;

            return switch (dataRequest.getRequestType()) {
                case HTTPConstants.POST -> builder.POST(bodyPublisher(dataRequest, contentType)).build();
                case HTTPConstants.PUT -> builder.PUT(bodyPublisher(dataRequest, contentType)).build();
                case HTTPConstants.PATCH -> builder.method("PATCH", bodyPublisher(dataRequest, contentType)).build();
                default -> builder.GET().build();
            };
        } else if (request instanceof DELETERequest) {
            return builder.DELETE().build();
        } else {
            return builder.GET().build();
        }
    }

    private static HttpRequest.BodyPublisher bodyPublisher(DataRequest dataRequest, String contentType) throws IOException {
        if (dataRequest.getBody() == null || dataRequest.getBody().isEmpty()) {
            return HttpRequest.BodyPublishers.noBody();
        }

        return switch (contentType) {
            case HTTPConstants.CONTENT_TYPE_FORM_URLENCODED -> {
                StringBuilder form = new StringBuilder();
                dataRequest.getStringTuples().forEach((key, value) -> {
                    if (!form.isEmpty()) form.append("&");
                    form.append(key).append("=").append(value);
                });
                yield HttpRequest.BodyPublishers.ofString(form.toString());
            }
            case HTTPConstants.CONTENT_TYPE_OCTET_STREAM -> {
                Path filePath = Path.of(dataRequest.getBody());
                if (!Files.exists(filePath)) {
                    throw new IOException("File not found: " + dataRequest.getBody());
                }
                yield HttpRequest.BodyPublishers.ofFile(filePath);
            }
            default -> HttpRequest.BodyPublishers.ofString(dataRequest.getBody());
        };
    }

    private static EverestResponse mapResponse(HttpResponse<String> httpResponse, long initialTime, long finalTime) {
        EverestResponse response = new EverestResponse();
        response.setBody(httpResponse.body());
        response.setStatusCode(httpResponse.statusCode());
        response.setTime(initialTime, finalTime);
        response.setSize(httpResponse.body() != null ? httpResponse.body().length() : 0);
        response.setContentType(httpResponse.headers().firstValue("Content-Type").orElse("text/plain"));
        response.setHeaders(httpResponse.headers().map());
        return response;
    }
}
