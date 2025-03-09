package client.commands;

import lombok.AllArgsConstructor;
import client.ClientPersonManager;
import shared.FieldFilter;
import shared.filters.FilterType;
import shared.responses.PersonsResponse;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RemoveByIdClientCommand implements ClientCommand {
    private final ClientPersonManager personManager;

    @Override
    public Boolean execute(String[] args) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("arg is not a number");
            return false;
        }

        ArrayList<FieldFilter> filters = new ArrayList<>();
        filters.add(new FieldFilter("id", id, FilterType.EQUALS));
        PersonsResponse response = personManager.removePersonsByFields(filters);
        response.persons.forEach(System.out::println);
        return true;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getHelpInfo() {
        return "Remove element from collection";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(List.of("id"));
    }
}