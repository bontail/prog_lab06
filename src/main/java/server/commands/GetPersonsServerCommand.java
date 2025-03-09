package server.commands;

import lombok.AllArgsConstructor;
import server.ServerPersonManager;
import shared.FieldFilter;
import shared.FilterInvoker;
import shared.data.Person;
import shared.requests.GetPersonsRequest;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.PersonsResponse;
import shared.responses.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@AllArgsConstructor
public class GetPersonsServerCommand implements ServerCommand {
    protected ServerPersonManager personManager;

    @Override
    public Response execute(Request request) {
        try {
            GetPersonsRequest personsRequest = (GetPersonsRequest) request;
            ArrayList<Person> persons = getFilteredPersons(personsRequest.filters);
            return new PersonsResponse(true, persons);
        } catch (Exception e) {
            return new Response(false, "Invalid filter args: " + e.getMessage());
        }
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.GET_PERSONS;
    }

    @Override
    public Boolean isPrivate() {
        return false;
    }

    protected ArrayList<Person> getFilteredPersons(List<FieldFilter> filters) {
        Stream<Person> personsStream = personManager.getPersons().stream();
        for (FieldFilter fieldFilter : filters) {
            personsStream = addFilter(personsStream, fieldFilter);
        }
        return personsStream.collect(Collectors.toCollection(ArrayList::new));
    }

    protected Stream<Person> addFilter(Stream<Person> persons, FieldFilter filter) {
        return persons.filter(buildPredicate(filter));
    }

    protected Predicate<Person> buildPredicate(FieldFilter filter) {
        return FilterInvoker.createPredicate(filter);
    }
}
