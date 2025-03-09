package shared.filters;

import shared.data.Person;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.function.Predicate;

import static shared.filters.FilterType.*;

public class LessEqualFilter extends BasePersonFilter {

    @Override
    public FilterType getFilterType() {
        return LESS_EQUAL;
    }

    public Predicate<Person> createPredicate(String fieldName, Object value) {
        Method method = getMethod(fieldName);
        return person -> {
            try {
                return new BigDecimal(String.valueOf(method.invoke(person))).compareTo(new BigDecimal(String.valueOf(value))) <= 0;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
