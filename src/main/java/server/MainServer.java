package server;

import ch.qos.logback.classic.Logger;
import server.commands.*;
import shared.requests.*;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;

public class MainServer {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(MainServer.class);

    private static final int PORT = 22233;
    private static final ServerPersonManager personManager = new ServerPersonManager();
    private static final ServerInvoker serverInvoker;

    static {
        ArrayList<ServerCommand> commands = new ArrayList<>();
        ArrayList<Request> requests = new ArrayList<>();

        commands.add(new ClearPersonsServerCommand(personManager));
        commands.add(new GetPersonsServerCommand(personManager));
        commands.add(new GetCollectionInfoServerCommand(personManager));
        commands.add(new RemovePersonsServerCommand(personManager));
        commands.add(new ValidatePersonArgsServerCommand(personManager));
        commands.add(new AddPersonServerCommand(personManager));
        commands.add(new RemoveLowerPersonsServerCommand(personManager));
        commands.add(new UpdatePersonServerCommand(personManager));
        commands.add(new SaveCollectionServerCommand(personManager));

        requests.add(new AddPersonRequest());
        requests.add(new ClearPersonsRequest());
        requests.add(new GetPersonsRequest());
        requests.add(new GetCollectionInfoRequest());
        requests.add(new RemoveLowerPersonsRequest());
        requests.add(new RemovePersonsRequest());
        requests.add(new UpdatePersonRequest());
        requests.add(new ValidatePersonArgsRequest());
        requests.add(new SaveCollectionRequest()
        );

        serverInvoker = new ServerInvoker(commands, requests);
    }

    public static void main(String[] args) {
        logger.info("Starting server...");
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    Dumper.saveDump(personManager.getPersons());
                    System.out.println("Stopping server shutdown");
                }
        ));
        try (DatagramChannel channel = DatagramChannel.open()){
            Handler handler = new Handler(serverInvoker);
            channel.bind(new InetSocketAddress(PORT));
            channel.configureBlocking(false);

            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            while (true) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                for (; keyIterator.hasNext(); keyIterator.remove()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        handler.handleDatagramChannel(channel);
                    }
                }
            }
        } catch (Throwable e) {
            logger.error("Found error: {}", e.getMessage());
        }
    }
}