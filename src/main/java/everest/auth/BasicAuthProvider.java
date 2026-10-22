package everest.auth;

import java.util.Base64;

public class BasicAuthProvider implements AuthProvider {
    private String username;
    private String password;
    private boolean enabled;

    public BasicAuthProvider(String username, String password) {
        this.username = username;
        this.password = password;
        this.enabled = true;
    }

    @Override
    public boolean isEnabled() {
        return enabled && (username != null && !username.isEmpty());
    }

    @Override
    public String getAuthHeader() {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
