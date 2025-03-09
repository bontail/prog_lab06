package shared.data;
import lombok.*;
import shared.validator.Check;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {
    @Check(minFloat = -434)
    private Float x; //Значение поля должно быть больше -434

    @Check(notNull = true)
    private Float y; //Поле не может быть null
}
