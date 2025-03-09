package server.commands;

import shared.requests.Request;
import shared.requests.RequestType;
import shared.responses.Response;

public interface ServerCommand {
    Response execute(Request request);
    RequestType getRequestType();
    Boolean isPrivate();
}
