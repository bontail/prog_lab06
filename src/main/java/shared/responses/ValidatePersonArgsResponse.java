package shared.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shared.validator.InvalidField;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidatePersonArgsResponse extends Response {
    public Boolean isSuccess;
    public InvalidField invalidField;
    public String nextFieldName;
}
