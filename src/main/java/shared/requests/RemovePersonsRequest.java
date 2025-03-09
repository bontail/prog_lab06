package shared.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shared.FieldFilter;

import java.util.ArrayList;

import static shared.requests.RequestType.REMOVE_PERSONS;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RemovePersonsRequest extends Request {
    public final RequestType type = REMOVE_PERSONS;
    public ArrayList<FieldFilter> filters;
}
