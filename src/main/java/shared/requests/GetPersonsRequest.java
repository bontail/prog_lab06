package shared.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shared.FieldFilter;

import java.util.ArrayList;

import static shared.requests.RequestType.GET_PERSONS;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPersonsRequest extends Request {
    public final RequestType type = GET_PERSONS;
    public ArrayList<FieldFilter> filters;

}
