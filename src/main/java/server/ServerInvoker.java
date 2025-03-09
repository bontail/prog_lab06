package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.commands.ServerCommand;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.Response;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ServerInvoker {
    public final LinkedHashMap<RequestType, ServerCommand> commands = new LinkedHashMap<>();
    public final LinkedHashMap<RequestType, Request> requests = new LinkedHashMap<>();

    public ServerInvoker(ArrayList<ServerCommand> commands, ArrayList<Request> requests) {
        for (ServerCommand command : commands) {
            this.commands.put(command.getRequestType(), command);
        }
        for (Request request : requests) {
            this.requests.put(request.getType(), request);
        }
    }

    public String execute(String stringRequest, Boolean isFromLocalhost) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Request request = mapper.readValue(stringRequest, Request.class);
            if (this.requests.get(request.getType()) == null){
                return new Response(false, "Unknown request command").jsonData();
            }
            request = mapper.readValue(stringRequest, this.requests.get(request.getType()).getClass());
            ServerCommand command = this.commands.get(request.getType());
            if (command.isPrivate() && !isFromLocalhost) {
                return new Response(false, "Command is private").jsonData();
            }
            Response response = this.commands.get(request.getType()).execute(request);
            return response.jsonData();
        } catch (JsonProcessingException e) {
            try {
                return new Response(false, "Invalid command: " + e.getMessage()).jsonData();
            }catch (JsonProcessingException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
