package everest.models.requests;

public class HTTPConstants {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";

    public static final String PLAIN_TEXT = "PLAIN TEXT";
    public static final String JSON = "JSON";
    public static final String XML = "XML";
    public static final String HTML = "HTML";

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_PLAIN = "text/plain";
    public static final String CONTENT_TYPE_FORM_DATA = "multipart/form-data";
    public static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";

    public static String getSimpleContentType(String contentType) {
        if (contentType == null) return PLAIN_TEXT;
        return switch (contentType.toLowerCase().split(";")[0]) {
            case CONTENT_TYPE_JSON -> JSON;
            case CONTENT_TYPE_XML -> XML;
            case CONTENT_TYPE_HTML -> HTML;
            default -> PLAIN_TEXT;
        };
    }

    public static String getComplexContentType(String simpleType) {
        return switch (simpleType) {
            case JSON -> CONTENT_TYPE_JSON;
            case XML -> CONTENT_TYPE_XML;
            case HTML -> CONTENT_TYPE_HTML;
            default -> CONTENT_TYPE_PLAIN;
        };
    }
}
