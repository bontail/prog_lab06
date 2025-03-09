package client.commands;

import client.ClientPersonManager;
import shared.FieldFilter;
import shared.filters.FilterType;
import shared.responses.PersonsResponse;

import java.util.ArrayList;
import java.util.List;

public class FilterContainsNameClientCommand extends ShowClientCommand {

    public FilterContainsNameClientCommand(ClientPersonManager personManager) {
        super(personManager);
    }

    @Override
    public Boolean execute(String[] args) {
        ArrayList<FieldFilter> filters = new ArrayList<>();
        filters.add(new FieldFilter("name", args[0], FilterType.CONTAINS));
        PersonsResponse response = this.personManager.getPersonsByFields(filters);
        printPersons(response);
        return true;
    }

    @Override
    public String getName() {
        return "filter_contains_name";
    }

    @Override
    public String getHelpInfo() {
        return "Get persons that contains the given name";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(List.of("name"));
    }
}