package server.commands;

import lombok.AllArgsConstructor;
import server.ServerPersonManager;
import shared.reflection.DataCreator;
import shared.data.Person;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.requests.UpdatePersonRequest;
import shared.responses.Response;


@AllArgsConstructor
public class UpdatePersonServerCommand implements ServerCommand {
    protected ServerPersonManager personManager;

    @Override
    public Response execute(Request request) {
        try {
            UpdatePersonRequest personRequest = (UpdatePersonRequest) request;
            DataCreator personCreator = new DataCreator(Person.class);
            Person newPerson = (Person) personCreator.createInstance(personRequest.personArgs);
            Person oldPerson = this.personManager.getPersons().stream().filter(p -> p.getId() == personRequest.id).findFirst().orElse(null);
            String message;
            if (oldPerson != null) {
                message = "Success";
                oldPerson.setValuesFrom(newPerson);
            } else {
                message = "Fail";
            }
            return new Response(true, message + " update " + newPerson.getName());
        } catch (Exception e) {
            return new Response(false, "Invalid person args: " + e.getMessage());
        }
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.UPDATE_PERSON;
    }

    @Override
    public Boolean isPrivate() {
        return false;
    }
}
