package server.commands;

import server.ServerPersonManager;
import shared.data.Person;
import shared.requests.RemovePersonsRequest;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.PersonsResponse;
import shared.responses.Response;

import java.util.ArrayList;


public class RemovePersonsServerCommand extends GetPersonsServerCommand {

    public RemovePersonsServerCommand(ServerPersonManager personManager) {
        super(personManager);
    }

    @Override
    public Response execute(Request request) {
        try {
            RemovePersonsRequest personsRequest = (RemovePersonsRequest) request;
            ArrayList<Person> persons = getFilteredPersons(personsRequest.filters);
            personManager.removePersons(persons);
            return new PersonsResponse(true, persons);
        } catch (Exception e) {
            return new Response(false, "Invalid filter args: " + e.getMessage());
        }
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.REMOVE_PERSONS;
    }


}