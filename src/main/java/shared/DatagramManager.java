package shared;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatagramManager {
    private static final Integer MAX_DATAGRAM_DATA_SIZE = 1000;
    private static final String DATAGRAMS_COUNT_SEPARATOR = "/";
    private static final String DATA_SEPARATOR = ";";
    private final HashMap<InetAddress, TreeMap<Long, String>> clientDatagrams = new HashMap<>();
    private final Pattern datagramPattern = Pattern.compile("^(\\d+)" + DATAGRAMS_COUNT_SEPARATOR + "(\\d+)" + DATA_SEPARATOR + "(.+)$");


    public String collectDatagrams(String request, InetAddress address) {
        Matcher matcher = this.datagramPattern.matcher(request);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid request");
        }
        long currentPacket = Long.parseLong(matcher.group(1));
        long totalPackets = Long.parseLong(matcher.group(2));
        String requestData = matcher.group(3);
        TreeMap<Long, String> datagramsMap = this.clientDatagrams.computeIfAbsent(address, k -> new TreeMap<>());
        datagramsMap.put(currentPacket, requestData);
        if (datagramsMap.size() == totalPackets) {
            this.clientDatagrams.remove(address);
            return String.join("", datagramsMap.values());
        }
        return null;
    }

    public void cleanDatagrams(InetAddress address) {
        this.clientDatagrams.remove(address);
    }

    private List<String> splitRequest(String request) {
        return List.of(request.split("(?<=\\G.{" + MAX_DATAGRAM_DATA_SIZE + "})"));
    }

    public List<String> splitSendData(String request) {
        List<String> rowRequestParts = splitRequest(request);
        ArrayList<String> preparedRequestParts = new ArrayList<>(rowRequestParts.size());
        for (int i = 0; i < rowRequestParts.size(); i++) {
            String requestPart = rowRequestParts.get(i);
            String part = (i + 1) + DATAGRAMS_COUNT_SEPARATOR + rowRequestParts.size() + DATA_SEPARATOR + requestPart;
            preparedRequestParts.add(part);
        }
        return preparedRequestParts;
    }
}
