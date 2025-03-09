package shared.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static shared.requests.RequestType.SAVE_COLLECTION;

@Getter
@NoArgsConstructor
public class SaveCollectionRequest extends Request {
    public final RequestType type = SAVE_COLLECTION;
}
