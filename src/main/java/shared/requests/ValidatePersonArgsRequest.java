package shared.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import static shared.requests.RequestType.VALIDATE_PERSON_ARGS;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidatePersonArgsRequest extends Request{
    public final RequestType type = VALIDATE_PERSON_ARGS;
    public Long stage;
    public ArrayList<String> personArgs;
}