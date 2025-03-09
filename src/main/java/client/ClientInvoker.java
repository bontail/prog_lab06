package client;

import lombok.Getter;
import client.commands.ClientCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Save all commands
 * Save history
 * Works with console
 */
public class ClientInvoker {
    private final static int HISTORY_SIZE = 11;
    public LinkedHashMap<String, ClientCommand> commands = new LinkedHashMap<>();
    private boolean isRunningApp = true;
    public ArrayDeque<ClientCommand> history = new ArrayDeque<>(HISTORY_SIZE);
    public Scanner sc;

    @Getter
    public HashSet<String> openedExecutionScripts;


    public ClientInvoker(Scanner sc, HashSet<String> openedExecutionScripts) {
        this.sc = sc;
        this.sc.useDelimiter("\n");
        this.openedExecutionScripts = openedExecutionScripts;
    }


    public ArrayList<ClientCommand> getCommands() {
        return new ArrayList<>(this.commands.values());
    }

    public void setCommands(@NotNull ArrayList<ClientCommand> commands) {
        for (ClientCommand command : commands) {
            this.commands.put(command.getName(), command);
        }
    }

    public void stopApp() {
        this.isRunningApp = false;
    }

    public void changeScanner(Scanner newScanner) {
        this.sc = newScanner;
        this.sc.useDelimiter("\n");
    }

    private void addCommandToHistory(@NotNull ClientCommand command) {
        if (this.history.size() == HISTORY_SIZE) {
            this.history.removeFirst();
        }
        this.history.push(command);
    }

    public Collection<ClientCommand> getHistory() {
        return this.history;
    }

    public String[] getLineTokens() {
        String line = sc.next();
        String[] tokens = line.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("?")) {
                tokens[i] = null;
            }
        }
        return tokens;
    }

    public void startScanningInput() {
        while (this.isRunningApp && sc.hasNext()) {
            String[] tokens = this.getLineTokens();
            if (tokens.length == 1 && tokens[0].isEmpty()) {
                continue;
            }

            ClientCommand command = this.commands.get(tokens[0]);
            if (command == null) {
                System.out.println("Unknown command: " + tokens[0]);
                continue;
            }

            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (args.length != command.getArgs().size()) {
                System.out.println("Invalid number of arguments: " + args.length);
                System.out.println("Expected arguments (" + command.getArgs().size() + ") : " + command.getArgs());
                continue;
            }
            try {
                if (command.execute(args)) {
                    this.addCommandToHistory(command);
                }
            } catch (BadResponseError e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
