package client.commands;

import client.ClientPersonManager;
import shared.FieldFilter;
import shared.filters.FilterType;
import shared.responses.PersonsResponse;

import java.util.ArrayList;
import java.util.List;

public class FilterGreaterThanWeightClientCommand extends ShowClientCommand {
    public FilterGreaterThanWeightClientCommand(ClientPersonManager personManager) {
        super(personManager);
    }

    @Override
    public Boolean execute(String[] args) {
        if (args.length != 1) {
            System.out.println("filter_greater_than_weight need one args");
            return false;
        }

        float weight;
        try {
            weight = Float.parseFloat(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        ArrayList<FieldFilter> filters = new ArrayList<>();
        filters.add(new FieldFilter("weight", weight, FilterType.GREATER_THAN));
        PersonsResponse response = this.personManager.getPersonsByFields(filters);
        printPersons(response);
        return true;
    }

    @Override
    public String getName() {
        return "filter_greater_than_weight";
    }

    @Override
    public String getHelpInfo() {
        return "Get persons that weight greater than the given weight";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(List.of("weight"));
    }
}