package everest.auth;

public interface AuthProvider {
    boolean isEnabled();
    String getAuthHeader();
}
