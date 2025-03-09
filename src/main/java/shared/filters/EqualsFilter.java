package shared.filters;

import shared.data.Person;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.function.Predicate;

import static shared.filters.FilterType.EQUALS;

public class EqualsFilter extends BasePersonFilter {

    @Override
    public FilterType getFilterType() {
        return EQUALS;
    }

    public Predicate<Person> createPredicate(String fieldName, Object value) {
        Method method = getMethod(fieldName);
        return person -> {
            try {
                if (Person.class.getDeclaredField(fieldName).getType().getSimpleName().equals("String")){
                    return String.valueOf(method.invoke(person)).equals(String.valueOf(value));
                }
                return new BigDecimal(String.valueOf(method.invoke(person))).compareTo(new BigDecimal(String.valueOf(value))) == 0;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
