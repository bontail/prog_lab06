package shared.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static shared.requests.RequestType.DEFAULT;


@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
    public final RequestType type = DEFAULT;

    public String jsonData() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}

