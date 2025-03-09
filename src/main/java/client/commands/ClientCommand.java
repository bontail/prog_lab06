package client.commands;

import java.util.ArrayList;

public interface ClientCommand {
    Boolean execute(String[] args);
    String getName();
    String getHelpInfo();
    ArrayList<String> getArgs();
}
