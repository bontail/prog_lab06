package client.commands;

import client.ClientInvoker;
import client.ClientPersonManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class ExecuteScriptClientCommand implements ClientCommand {
    ClientPersonManager personManager;
    ClientInvoker invoker;

    public ExecuteScriptClientCommand(ClientPersonManager personManager, ClientInvoker invoker) {
        this.invoker = invoker;
        this.personManager = personManager;
    }

    @Override
    public Boolean execute(String[] args) {
        String filename = args[0];
        try {
            HashSet<String> openedExecutionScripts = this.invoker.getOpenedExecutionScripts();
            if (openedExecutionScripts.contains(filename)) {
                System.err.println("Recursive execution " + filename);
                return false;
            }
            openedExecutionScripts.add(filename);
            File file = new File(filename);
            Scanner currentScanner = this.invoker.sc;
            this.invoker.changeScanner(new Scanner(file));
            this.invoker.startScanningInput();
            this.invoker.changeScanner(currentScanner);
            openedExecutionScripts.remove(filename);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid filename " + e.getMessage() + " " + filename);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getHelpInfo() {
        return "Execute commands from file";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(List.of("filename"));
    }
}

// TODO: add some packets sending