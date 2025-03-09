package server.commands;

import lombok.AllArgsConstructor;
import server.ServerPersonManager;
import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.Response;


@AllArgsConstructor
public class SaveCollectionServerCommand implements ServerCommand {
    private ServerPersonManager personManager;

    @Override
    public Response execute(Request request) {
        this.personManager.save();
        return new Response(true, "");
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SAVE_COLLECTION;
    }

    @Override
    public Boolean isPrivate() {
        return true;
    }
}
