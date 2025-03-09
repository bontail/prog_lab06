package client.commands;
import lombok.AllArgsConstructor;
import client.ClientInvoker;

import java.util.ArrayList;

@AllArgsConstructor
public class ExitClientCommand implements ClientCommand {
    private final ClientInvoker invoker;


    @Override
    public Boolean execute(String[] args) {
        this.invoker.stopApp();
        System.out.println("Bye bye!");
        return true;
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getHelpInfo() {
        return "Exit app without saving";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(0);
    }
}