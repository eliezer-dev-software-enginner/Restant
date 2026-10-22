package everest.models.requests;

import everest.auth.AuthProvider;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

@Getter
public abstract class EverestRequest {
    private URL target;
    private AuthProvider authProvider;
    @Setter
    private HashMap<String, String> headers = new HashMap<>();

    public void setTarget(String target) throws MalformedURLException {
        this.target = URI.create(target).toURL();
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }
}
