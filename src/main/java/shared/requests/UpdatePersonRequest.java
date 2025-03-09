package shared.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import static shared.requests.RequestType.UPDATE_PERSON;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonRequest extends Request{
    public final RequestType type = UPDATE_PERSON;
    public Long id;
    public ArrayList<String> personArgs;
}