package shared.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import static shared.requests.RequestType.ADD_PERSON;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddPersonRequest extends Request {
    public final RequestType type = ADD_PERSON;
    public Boolean ifMax;
    public ArrayList<String> personArgs;

}