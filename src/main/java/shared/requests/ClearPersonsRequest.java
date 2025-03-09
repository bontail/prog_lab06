package shared.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static shared.requests.RequestType.CLEAR_PERSONS;

@Getter
@NoArgsConstructor
public class ClearPersonsRequest extends Request{
    public final RequestType type = CLEAR_PERSONS;
}
