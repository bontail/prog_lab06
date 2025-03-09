package client.commands;
import lombok.AllArgsConstructor;
import client.ClientInvoker;

import java.util.ArrayList;


@AllArgsConstructor
public class HistoryClientCommand implements ClientCommand {
    private final ClientInvoker invoker;

    @Override
    public Boolean execute(String[] args) {
        int i = 1;
        for (ClientCommand command : this.invoker.getHistory()){
            System.out.println(i + ": " + command.getName());
            i++;
        }

        return true;
    }

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public String getHelpInfo() {
        return "Get history successes commands";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(0);
    }
}