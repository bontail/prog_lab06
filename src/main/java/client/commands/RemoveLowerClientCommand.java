package client.commands;

import client.ClientInvoker;
import client.ClientPersonManager;
import shared.responses.PersonsResponse;

import java.util.ArrayList;


public class RemoveLowerClientCommand extends AddClientCommand {

    public RemoveLowerClientCommand(ClientPersonManager personManager, ClientInvoker invoker) {
        super(personManager, invoker);
    }

    @Override
    public Boolean execute(String[] personArgs) {
        ArrayList<String> personInitArgs = getPersonInitArgs(personArgs);
        PersonsResponse personsResponse = this.personManager.removeLowerPerson(personInitArgs);
        printPersons(personsResponse);
        return true;
    }

    protected void printPersons(PersonsResponse response){
        System.out.println("Successfully removed lower persons:");
        response.persons.forEach(System.out::println);
        if (response.persons.isEmpty()){
            System.out.println("[]");
        }
    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String getHelpInfo() {
        return "Remove persons that are lower then taking";
    }
}