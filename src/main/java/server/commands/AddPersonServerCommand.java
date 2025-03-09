package server.commands;

import lombok.AllArgsConstructor;
import server.ServerPersonManager;
import shared.reflection.DataCreator;
import shared.data.Person;
import shared.requests.AddPersonRequest;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.Response;



@AllArgsConstructor
public class AddPersonServerCommand implements ServerCommand {
    protected ServerPersonManager personManager;

    @Override
    public Response execute(Request request) {
        try {
            AddPersonRequest personRequest = (AddPersonRequest) request;
            DataCreator personCreator = new DataCreator(Person.class);
            Person person = (Person) personCreator.createInstance(personRequest.personArgs);
            Boolean add = this.personManager.addPerson(personRequest.ifMax, person);
            String message = "";
            if (add) {
                message += "Success";
            } else {
                message += "Fail";
            }
            return new Response(true, message + " add " + person.getName());
        } catch (Exception e) {
            return new Response(false, "Invalid person args: " + e.getMessage());
        }
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.ADD_PERSON;
    }

    @Override
    public Boolean isPrivate() {
        return false;
    }
}
