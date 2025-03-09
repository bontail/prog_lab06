package server.commands;

import lombok.AllArgsConstructor;
import server.ServerPersonManager;
import shared.validator.DataCreator;
import shared.data.Person;
import shared.requests.RemoveLowerPersonsRequest;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.PersonsResponse;
import shared.responses.Response;

import java.util.ArrayList;

@AllArgsConstructor
public class RemoveLowerPersonsServerCommand implements ServerCommand {
    ServerPersonManager personManager;

    @Override
    public Response execute(Request request) {
        try {
            RemoveLowerPersonsRequest removeLowerRequest = (RemoveLowerPersonsRequest) request;
            DataCreator personCreator = new DataCreator(Person.class);
            ArrayList<Person> persons = personManager.removeLowerPersons((Person) personCreator.createInstance(removeLowerRequest.personArgs));
            return new PersonsResponse(true, persons);
        } catch (Exception e) {
            return new Response(false, "Invalid filter args: " + e.getMessage());
        }
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.REMOVE_LOWER_PERSONS;
    }

    @Override
    public Boolean isPrivate() {
        return false;
    }
}