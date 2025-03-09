package client.commands;
import lombok.AllArgsConstructor;
import client.ClientPersonManager;
import shared.responses.Response;

import java.util.ArrayList;

@AllArgsConstructor
public class InfoClientCommand implements ClientCommand {
    private final ClientPersonManager personManager;

    @Override
    public Boolean execute(String[] args) {
        Response response = personManager.getCollectionInfo();
        System.out.println(response.message);
        return true;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getHelpInfo() {
        return "Get information about collection";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(0);
    }
}
