package shared.validator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectionVisitor {
    public final Class<?> cls;

    public ReflectionVisitor(Class<?> cls) {
        this.cls = cls;
    }

    public ArrayList<Field> getFieldsWithCheck() {
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : this.cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Check.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    public ArrayList<Field> getNestedFields() {
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : this.cls.getDeclaredFields()) {
            if (!field.getType().getName().startsWith("shared") || field.getType().isEnum()) {
                continue;
            }
            fields.add(field);
        }
        return fields;
    }

    public Object castToFieldType(String value, Class<?> fieldType) throws InvocationTargetException, IllegalAccessException {
        if (value == null) {
            return null;
        }

        Object castedValue = null;

        if (fieldType.equals(Float.class)) {
            castedValue = Float.parseFloat(value);
        } else if (fieldType.equals(Double.class)) {
            castedValue = Double.parseDouble(value);
        } else if (fieldType.equals(Long.class)) {
            castedValue = Long.parseLong(value);
        } else if (fieldType.equals(Integer.class)) {
            castedValue = Integer.parseInt(value);
        } else if (fieldType.equals(Short.class)) {
            castedValue = Short.parseShort(value);
        } else if (fieldType.equals(Byte.class)) {
            castedValue = Byte.parseByte(value);
        } else if (fieldType.equals(Boolean.class)) {
            castedValue = Boolean.parseBoolean(value);
        }
        if (fieldType.equals(String.class)) {
            castedValue = value;
        }
        if (fieldType.isEnum()) {
            Method valueOf;
            try {
                valueOf = fieldType.getMethod("valueOf", String.class);
            } catch (NoSuchMethodException e) {
                return null;
            }
            valueOf.setAccessible(true);
            castedValue = valueOf.invoke(null, value);
        }
        return castedValue;
    }

}
