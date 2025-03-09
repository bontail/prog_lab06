package client.commands;
import lombok.AllArgsConstructor;
import client.ClientPersonManager;
import shared.responses.PersonsResponse;

import java.util.ArrayList;

@AllArgsConstructor
public class ShowClientCommand implements ClientCommand {
    protected final ClientPersonManager personManager;

    @Override
    public Boolean execute(String[] args) {
        PersonsResponse response = personManager.getAllPersons();
        printPersons(response);
        return true;
    }

    protected void printPersons(PersonsResponse response){
        response.persons.forEach(System.out::println);
        if (response.persons.isEmpty()){
            System.out.println("[]");
        }
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getHelpInfo() {
        return "Show all persons";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(0);
    }
}