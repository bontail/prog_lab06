package shared.filters;

import shared.data.Person;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public abstract class BasePersonFilter {

    public static Method getMethod(String fieldName){
        try {
            Method method = Person.class.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    abstract public FilterType getFilterType();

    abstract public Predicate<Person> createPredicate(String fieldName, Object value);
}