package shared.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static shared.requests.RequestType.GET_COLLECTION_INFO;

@Getter
@NoArgsConstructor
public class GetCollectionInfoRequest extends Request {
    public final RequestType type = GET_COLLECTION_INFO;
}
