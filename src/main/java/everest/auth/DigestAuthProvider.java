package everest.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class DigestAuthProvider implements AuthProvider {
    private String username;
    private String password;
    private boolean enabled;
    private String realm;
    private String nonce;
    private String qop;
    private String opaque;
    private String method;
    private String uri;

    public DigestAuthProvider(String username, String password) {
        this.username = username;
        this.password = password;
        this.enabled = true;
        this.nonce = UUID.randomUUID().toString();
        this.qop = "auth";
        this.opaque = UUID.randomUUID().toString();
    }

    @Override
    public boolean isEnabled() {
        return enabled && (username != null && !username.isEmpty());
    }

    @Override
    public String getAuthHeader() {
        String nc = "00000001";
        String cnonce = UUID.randomUUID().toString().substring(0, 16);

        String ha1 = md5(username + ":" + realm + ":" + password);
        String ha2 = md5(method + ":" + uri);
        String response = md5(ha1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2);

        return String.format(
            "Digest username=\"%s\", realm=\"%s\", nonce=\"%s\", uri=\"%s\", response=\"%s\", qop=%s, nc=%s, cnonce=\"%s\", opaque=\"%s\"",
            username, realm, nonce, uri, response, qop, nc, cnonce, opaque
        );
    }

    public void processChallenge(String wwwAuthenticate) {
        if (wwwAuthenticate == null || !wwwAuthenticate.startsWith("Digest ")) return;

        String header = wwwAuthenticate.substring(7);
        realm = extractValue(header, "realm");
        nonce = extractValue(header, "nonce");
        qop = extractValue(header, "qop");
        opaque = extractValue(header, "opaque");
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String extractValue(String header, String key) {
        int start = header.indexOf(key + "=\"");
        if (start == -1) return "";
        start += key.length() + 2;
        int end = header.indexOf("\"", start);
        return header.substring(start, end);
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
