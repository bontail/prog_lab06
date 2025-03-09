package shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shared.filters.FilterType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FieldFilter {
    public String fieldName;
    public Object value;
    public FilterType filterType;
}
