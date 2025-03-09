package shared.validator;

public record InvalidField(Integer index, String name, String message) {}