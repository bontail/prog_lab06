package client.commands;

import client.ClientPersonManager;
import lombok.AllArgsConstructor;
import shared.responses.Response;

import java.util.ArrayList;

@AllArgsConstructor
public class ClearClientCommand implements ClientCommand {
    private final ClientPersonManager personManager;
    
    @Override
    public Boolean execute(String[] args) {
        Response response = this.personManager.clearPersons();
        System.out.println(response.message);
        return true;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelpInfo() {
        return "Clears all persons from collection";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(0);
    }
}