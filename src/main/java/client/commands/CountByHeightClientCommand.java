package client.commands;
import lombok.AllArgsConstructor;
import client.ClientPersonManager;
import shared.FieldFilter;
import shared.filters.FilterType;
import shared.responses.PersonsResponse;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CountByHeightClientCommand implements ClientCommand {
    private final ClientPersonManager personManager;
    
    @Override
    public Boolean execute(String[] args) {
        long height;
        try {
            height = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("arg is not a number");
            return false;
        }

        ArrayList<FieldFilter> filters = new ArrayList<>();
        filters.add(new FieldFilter("height", height, FilterType.EQUALS));
        PersonsResponse response = this.personManager.getPersonsByFields(filters);
        System.out.println("Found " + response.getPersons().size() + " person(s)");
        return true;
    }

    @Override
    public String getName() {
        return "count_by_height";
    }

    @Override
    public String getHelpInfo() {
        return "Count persons by height";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(List.of("height"));
    }
}