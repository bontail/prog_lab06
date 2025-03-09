package server.commands;

import lombok.AllArgsConstructor;
import server.ServerPersonManager;
import shared.validator.InvalidField;
import shared.validator.Validator;
import shared.data.Person;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.requests.ValidatePersonArgsRequest;
import shared.responses.Response;
import shared.responses.ValidatePersonArgsResponse;


@AllArgsConstructor
public class ValidatePersonArgsServerCommand implements ServerCommand {
    protected ServerPersonManager personManager;

    public Response execute(Request request) {
        ValidatePersonArgsRequest validatePersonArgsRequest = (ValidatePersonArgsRequest) request;
        Validator personValidator = new Validator(Person.class);
        InvalidField invalidField = personValidator.checkArgs(validatePersonArgsRequest.personArgs, validatePersonArgsRequest.stage);
        return new ValidatePersonArgsResponse(true, invalidField, personValidator.getNextFieldName(validatePersonArgsRequest.stage));
    }


    @Override
    public RequestType getRequestType() {
        return RequestType.VALIDATE_PERSON_ARGS;
    }

    @Override
    public Boolean isPrivate() {
        return false;
    }
}
