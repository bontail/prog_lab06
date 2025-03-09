package client.commands;

import client.ClientInvoker;
import client.ClientPersonManager;

import java.util.ArrayList;


public class AddIfMaxClientCommand extends AddClientCommand {

    public AddIfMaxClientCommand(ClientPersonManager personManager, ClientInvoker invoker) {
        super(personManager, invoker);
    }

    @Override
    public Boolean execute(String[] personArgs) {
        ArrayList<String> personInitArgs = getPersonInitArgs(personArgs);
        System.out.println(this.personManager.addPerson(true, personInitArgs).message);
        return true;
    }


    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getHelpInfo() {
        return "Add person to collection if this person is max";
    }
}