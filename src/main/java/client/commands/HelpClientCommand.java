package client.commands;
import lombok.AllArgsConstructor;
import client.ClientInvoker;

import java.util.ArrayList;

@AllArgsConstructor
public class HelpClientCommand implements ClientCommand {
    private final ClientInvoker invoker;


    @Override
    public Boolean execute(String[] args) {
        for (ClientCommand command : invoker.getCommands()){
            System.out.print(command.getName() + ": " + command.getHelpInfo());
            if (!command.getArgs().isEmpty()){
                System.out.print(" [" + String.join(" ", command.getArgs()) + ']');
            }
            System.out.println();
        }
        return true;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelpInfo() {
        return "Get information about all enabled commands";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(0);
    }
}