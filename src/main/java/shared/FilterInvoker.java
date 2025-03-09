package shared;


import shared.data.Person;
import shared.filters.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class FilterInvoker {
    public final static HashMap<FilterType, BasePersonFilter> filters = new HashMap<>();
    static {
        ArrayList<BasePersonFilter> filtersArray = new ArrayList<>();
        filtersArray.add(new ContainsFilter());
        filtersArray.add(new EqualsFilter());
        filtersArray.add(new GreaterEqualFilter());
        filtersArray.add(new GreaterThanFilter());
        filtersArray.add(new LessEqualFilter());
        filtersArray.add(new LessThanFilter());
        for (BasePersonFilter filter : filtersArray) {
            filters.put(filter.getFilterType(), filter);
        }
    }

    public static Predicate<Person> createPredicate(FieldFilter fieldFilter) {
        return filters.get(fieldFilter.getFilterType()).createPredicate(fieldFilter.getFieldName(), fieldFilter.getValue());
    }
}