package client;

import client.commands.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        ClientPersonManager personManager = new ClientPersonManager("192.168.10.80", 22233);
        ClientInvoker invoker = new ClientInvoker(new Scanner(System.in), new HashSet<>());
        ArrayList<ClientCommand> commands = Client.createCommands(personManager, invoker);
        invoker.setCommands(commands);
        invoker.startScanningInput();
    }

    public static ArrayList<ClientCommand> createCommands(ClientPersonManager personManager, ClientInvoker invoker) {
        ArrayList<ClientCommand> commands = new ArrayList<>();
        commands.add(new HelpClientCommand(invoker));
        commands.add(new InfoClientCommand(personManager));
        commands.add(new ShowClientCommand(personManager));
        commands.add(new AddClientCommand(personManager, invoker));
        commands.add(new UpdateClientCommand(personManager, invoker));
        commands.add(new RemoveByIdClientCommand(personManager));
        commands.add(new ClearClientCommand(personManager));
        commands.add(new ExecuteScriptClientCommand(personManager, invoker));
        commands.add(new ExitClientCommand(invoker));
        commands.add(new AddIfMaxClientCommand(personManager, invoker));
        commands.add(new RemoveLowerClientCommand(personManager, invoker));
        commands.add(new HistoryClientCommand(invoker));
        commands.add(new CountByHeightClientCommand(personManager));
        commands.add(new FilterContainsNameClientCommand(personManager));
        commands.add(new FilterGreaterThanWeightClientCommand(personManager));
        return commands;
    }

}
