package client.commands;

import client.ClientInvoker;
import client.ClientPersonManager;
import shared.responses.Response;

import java.util.ArrayList;
import java.util.Arrays;


public class UpdateClientCommand extends AddClientCommand {

    public UpdateClientCommand(ClientPersonManager personManager, ClientInvoker invoker) {
        super(personManager, invoker);
    }

    @Override
    public Boolean execute(String[] personArgs) {
        long id;
        try {
            id = Long.parseLong(personArgs[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        String[] newPersonArgs = Arrays.copyOfRange(personArgs, 1, personArgs.length);
        ArrayList<String> personInitArgs = getPersonInitArgs(newPersonArgs);

        Response response = this.personManager.updatePerson(id, personInitArgs);
        System.out.println(response.message);
        return true;
    }


    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getHelpInfo() {
        return "Update a person by id";
    }

    @Override
    public ArrayList<String> getArgs(){
        ArrayList<String> args = new ArrayList<>();
        args.add("id");
        args.addAll(super.getArgs());
        return args;
    }
}