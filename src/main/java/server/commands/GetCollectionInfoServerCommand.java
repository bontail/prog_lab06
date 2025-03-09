package server.commands;

import lombok.AllArgsConstructor;
import server.ServerPersonManager;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.Response;


@AllArgsConstructor
public class GetCollectionInfoServerCommand implements ServerCommand {
    protected ServerPersonManager personManager;

    @Override
    public Response execute(Request request) {
        return new Response(true, personManager.getInfo());
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.GET_COLLECTION_INFO;
    }

    @Override
    public Boolean isPrivate() {
        return false;
    }
}
