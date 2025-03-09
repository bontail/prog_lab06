package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import shared.DatagramManager;
import shared.FieldFilter;
import shared.requests.*;
import shared.responses.PersonsResponse;
import shared.responses.Response;
import shared.responses.ValidatePersonArgsResponse;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


/**
 * Class for save Person collection
 */
@AllArgsConstructor
public class ClientPersonManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DatagramManager datagramManager = new DatagramManager();
    public final String serverHost;
    public final Integer serverPort;


    private Response sendRequest(Request request) {
        return sendRequest(request, Response.class);
    }

    private Response sendRequest(Request request, Class<?> responseClass) {
        Response response = send(request, responseClass);
        if (!response.getIsSuccess()) {
            throw new BadResponseError(response.message);
        }
        return response;
    }

    private Response send(Request request, Class<?> responseClass) {
        try (DatagramSocket socket = new DatagramSocket()) {
            String requestData = request.jsonData();
            InetSocketAddress serverSocketAddress = new InetSocketAddress(this.serverHost, this.serverPort);
            for (String sendDataPart : datagramManager.splitSendData(requestData)) {
                socket.send(new DatagramPacket(sendDataPart.getBytes(), sendDataPart.getBytes().length, serverSocketAddress));
            }
            int timeout = 5000;
            for (int i = 0; i < 3; i++) {
                byte[] buff = new byte[65507];
                DatagramPacket packet = new DatagramPacket(buff, buff.length);
                try {
                    socket.setSoTimeout(timeout);
                    socket.receive(packet);
                } catch (SocketTimeoutException e) {
                    System.out.println("The server did not respond full request (" + this.serverHost + ":" + this.serverPort + ')');
                    System.out.println("Repeat wait...");
                    continue;
                }
                String responsePart = new String(packet.getData(), 0, packet.getLength());
                String fullResponse = datagramManager.collectDatagrams(responsePart, packet.getAddress());
                if (fullResponse == null) {
                    i = 0;
                    continue;
                }
                return (Response) objectMapper.readValue(fullResponse, responseClass);
            }
            datagramManager.cleanDatagrams(serverSocketAddress.getAddress());
            return new Response(false, "The full response from the server was not received");
        } catch (SocketException e) {
            return new Response(false, "Socket exception: " + e.getMessage());
        } catch (JsonProcessingException e) {
            return new Response(false, "Invalid json data: " + e.getMessage());
        } catch (IOException e) {
            return new Response(false, "Send exception: " + e.getMessage());
        }
    }

    public Response getCollectionInfo() {
        return sendRequest(new GetCollectionInfoRequest());
    }

    public Response clearPersons() {
        return sendRequest(new ClearPersonsRequest());
    }

    public PersonsResponse getAllPersons() {
        return getPersonsByFields(new ArrayList<>());
    }

    public PersonsResponse getPersonsByFields(ArrayList<FieldFilter> filters) {
        return (PersonsResponse) sendRequest(new GetPersonsRequest(filters), PersonsResponse.class);
    }

    public PersonsResponse removePersonsByFields(ArrayList<FieldFilter> filters) {
        return (PersonsResponse) sendRequest(new RemovePersonsRequest(filters), PersonsResponse.class);
    }

    public PersonsResponse removeLowerPerson(ArrayList<String> personArgs) {
        return (PersonsResponse) sendRequest(new RemoveLowerPersonsRequest(personArgs), PersonsResponse.class);
    }

    public ValidatePersonArgsResponse validatePersonArgs(Long stage, ArrayList<String> args) {
        return (ValidatePersonArgsResponse) sendRequest(new ValidatePersonArgsRequest(stage, args), ValidatePersonArgsResponse.class);
    }

    public Response addPerson(ArrayList<String> personArgs) {
        return addPerson(false, personArgs);
    }

    public Response addPerson(Boolean ifMax, ArrayList<String> personArgs) {
        return sendRequest(new AddPersonRequest(ifMax, personArgs));
    }

    public Response updatePerson(Long id, ArrayList<String> personArgs) {
        return sendRequest(new UpdatePersonRequest(id, personArgs));
    }
}
