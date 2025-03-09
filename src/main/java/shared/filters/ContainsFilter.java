package shared.filters;

import shared.data.Person;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import static shared.filters.FilterType.CONTAINS;

public class ContainsFilter extends BasePersonFilter {

    @Override
    public FilterType getFilterType() {
        return CONTAINS;
    }

    public Predicate<Person> createPredicate(String fieldName, Object value) {
        Method method = getMethod(fieldName);
        return person -> {
            try {
                return String.valueOf(method.invoke(person)).contains(String.valueOf(value));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
