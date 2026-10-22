package everest.models.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class DataRequest extends EverestRequest {
    private String requestType;
    private String body;
    private String contentType;
    private HashMap<String, String> stringTuples = new HashMap<>();
    private HashMap<String, String> fileTuples = new HashMap<>();

}
