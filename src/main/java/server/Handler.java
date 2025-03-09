package server;

import ch.qos.logback.classic.Logger;
import lombok.AllArgsConstructor;
import shared.DatagramManager;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


@AllArgsConstructor
class Handler {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Handler.class);
    private static final DatagramManager datagramManager = new DatagramManager();
    private final ServerInvoker serverInvoker;


    public void handleDatagramChannel(DatagramChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(65507);
        try {
            InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
            buffer.flip();
            String request = new String(buffer.array(), 0, buffer.remaining());
            logger.info("Get {} from {}", request, clientAddress);
            String fullRequestData = datagramManager.collectDatagrams(request, clientAddress.getAddress());
            if (fullRequestData == null) {
                return;
            }
            String response = this.serverInvoker.execute(fullRequestData, clientAddress.getAddress().getHostAddress().equals("127.0.0.1"));
            for (String sendDataPart: datagramManager.splitSendData(response)){
                channel.send(ByteBuffer.wrap(sendDataPart.getBytes()), clientAddress);
                logger.info("Send {} to {}", sendDataPart, clientAddress);
            }
        } catch (Throwable e) {
            logger.error("Found error in handling {}", e.getMessage());
        }
    }
}