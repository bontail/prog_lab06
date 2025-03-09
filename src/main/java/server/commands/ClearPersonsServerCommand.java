package server.commands;

import lombok.AllArgsConstructor;
import server.ServerPersonManager;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.Response;


@AllArgsConstructor
public class ClearPersonsServerCommand implements ServerCommand {
    protected ServerPersonManager personManager;

    public Response execute(Request request) {
        personManager.clear();
        return new Response(true, "Successful clear persons");
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.CLEAR_PERSONS;
    }

    @Override
    public Boolean isPrivate() {
        return false;
    }
}
