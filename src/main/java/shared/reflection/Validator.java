package shared.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;


/**
 * Class for checking fields in class which have @Check
 * Uses reflection
 */
public class Validator extends ReflectionVisitor {
    private final HashMap<Long, ArrayList<Field>> stages = new HashMap<>();

    public Validator(Class<?> cls) {
        this(cls, 1);
    }

    public Validator(Class<?> cls, long stagesCount) {
        super(cls);
        ArrayList<Field> fields = new ArrayList<>(this.getFieldsWithCheck());
        stages.put(stagesCount, fields);
        for (Field field : this.getNestedFields()) {
            Validator nestedValidator = new Validator(field.getType(), 0);
            for (Field nestedTypeField : nestedValidator.getFieldsWithCheck()) {
                stagesCount++;
                stages.put(stagesCount, new ArrayList<>(Collections.singleton(nestedTypeField)));
            }
        }
    }

    public String getNextFieldName(Long stage) {
        ArrayList<Field> fieldsArray = this.stages.get(stage + 1);
        if (fieldsArray == null) {
            return null;
        }
        if (fieldsArray.isEmpty()) {
            return null;
        }
        Field field = fieldsArray.get(0);
        if (field == null) {
            return null;
        }
        return getFieldNameByType(field.getDeclaringClass()) + ' ' + field.getName();
    }

    private String getFieldNameByType(Class<?> type) {
        for (Field field : this.getNestedFields()) {
            if (field.getType().equals(type)) {
                return field.getName();
            }
        }
        return "";
    }

    private InvalidField validateArg(int index, String arg, Field field) {
        Check annotation = field.getAnnotation(Check.class);

        Object castedValue = null;
        try {
            castedValue = this.castToFieldType(arg, field.getType());
        } catch (InvocationTargetException | IllegalArgumentException e) {
            if (e instanceof IllegalArgumentException || e.getCause().getClass().equals(IllegalArgumentException.class)) {
                String message = "Value '" + arg + "' is invalid for type " + field.getType().getSimpleName();
                if (field.getType().isEnum()) {
                    message += " (Valid values are: " + Arrays.toString(Stream.of(field.getType().getEnumConstants()).map(Object::toString).toArray(String[]::new)) + ")";
                }
                return new InvalidField(index, field.getName(), message);
            }
        } catch (Throwable e) {
            return new InvalidField(index, field.getName(), e.getMessage());
        }
        if (castedValue == null) {
            if (annotation.notNull()){
                return new InvalidField(index, field.getName(), "Value '" + arg + "' is invalid (Cannot be null)");
            }
            return null;
        }
        if (annotation.notEmptyString() && ((String) castedValue).isEmpty()) {
            return new InvalidField(index, field.getName(), "Value '" + arg + "' is invalid (Cannot be empty)");
        }
        if (field.getType().isEnum() || field.getType().getSimpleName().equals("String")) {
            return null;
        }
        try {
            Method method = Check.class.getDeclaredMethod("min" + castedValue.getClass().getSimpleName());
            method.setAccessible(true);
            Object minValue = method.invoke(annotation);
            if (!validateMinValue(minValue, castedValue)) {
                return new InvalidField(index, field.getName(), "Value '" + arg + "' is invalid (min value is " + minValue.toString() + ")");
            }

            method = Check.class.getDeclaredMethod("max" + castedValue.getClass().getSimpleName());
            method.setAccessible(true);
            Object maxValue = method.invoke(annotation);
            if (!validateMaxValue(maxValue, castedValue)) {
                return new InvalidField(index, field.getName(), "Value '" + arg + "' is invalid (max value is " + maxValue.toString() + ")");
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return new InvalidField(index, field.getName(), e.getMessage());
        }
        return null;
    }

    public boolean validateMinValue(Object minValue, Object currentValue) {
        return new BigDecimal(String.valueOf(minValue)).compareTo(new BigDecimal(String.valueOf(currentValue))) <= 0;
    }

    public boolean validateMaxValue(Object maxValue, Object currentValue) {
        return new BigDecimal(String.valueOf(maxValue)).compareTo(new BigDecimal(String.valueOf(currentValue))) >= 0;
    }

    public InvalidField checkArgs(List<String> args, Long stage) {
        ArrayList<Field> fields = this.stages.get(stage);
        int argIndex = 0;
        for (Field field : fields) {
            InvalidField invalidField = this.validateArg(argIndex, args.get(argIndex), field);
            if (invalidField != null) {
                return invalidField;
            }
            argIndex++;
        }
        return null;
    }
}