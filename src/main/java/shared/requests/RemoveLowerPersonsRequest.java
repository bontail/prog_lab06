package shared.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import static shared.requests.RequestType.REMOVE_LOWER_PERSONS;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveLowerPersonsRequest extends Request{
    public final RequestType type = REMOVE_LOWER_PERSONS;
    public ArrayList<String> personArgs;
}