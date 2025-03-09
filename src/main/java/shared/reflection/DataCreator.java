package shared.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataCreator extends ReflectionVisitor {

    public DataCreator(Class<?> cls) {
        super(cls);
    }

    private void checkArg(Class<?> clsForCheck, List<String> args){
        Validator validator = new Validator(clsForCheck);
        InvalidField invalidField = validator.checkArgs(args, 1L);
        if (invalidField != null) {
            throw new IllegalArgumentException(invalidField.message());
        }
    }

    public Object createInstance(ArrayList<String> args) {
        this.checkArg(this.cls, args.subList(0, this.getFieldsWithCheck().size()));
        int toIndex = args.size();
        List<Field> nestedFields = this.getNestedFields();
        Collections.reverse(nestedFields);
        for (Field field : nestedFields) {
            Validator nestedValidator = new Validator(field.getType());
            int startIndex = toIndex - nestedValidator.getFieldsWithCheck().size();
            this.checkArg(field.getType(), args.subList(startIndex, toIndex));
            toIndex = startIndex;
        }

        ArrayList<Object> nestedObjects = new ArrayList<>();
        ArrayList<Object> castedArgs = new ArrayList<>();

        try {
            nestedFields = this.getNestedFields();
            Collections.reverse(nestedFields);
            for (Field field : nestedFields) {
                DataCreator nestedDataCreator = new DataCreator(field.getType());
                int constructorArgsCount = nestedDataCreator.getFieldsWithCheck().size();
                nestedObjects.add(
                        nestedDataCreator.createInstance(
                                new ArrayList<>(
                                        args.subList(args.size() - constructorArgsCount, args.size())
                                )
                        )
                );
                args = new ArrayList<>(args.subList(0, args.size() - constructorArgsCount));
            }

            int i = 0;
            for (Field field : this.getFieldsWithCheck()) {
                castedArgs.add(this.castToFieldType(args.get(i), field.getType()));
                i++;
            }
            Collections.reverse(nestedObjects);
            castedArgs.addAll(nestedObjects);

            Object[] castedArgsArray = new Object[castedArgs.size()];
            for (i = 0; i < castedArgs.size(); i++) {
                castedArgsArray[i] = castedArgs.get(i);
            }

            Class<?>[] argTypes = new Class<?>[castedArgsArray.length];
            for (i = 0; i < castedArgs.size(); i++) {
                argTypes[i] = castedArgs.get(i).getClass();
            }

            Constructor<?> constructor = this.cls.getDeclaredConstructor(argTypes);
            return constructor.newInstance(castedArgsArray);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
