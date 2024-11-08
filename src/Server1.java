import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class Server1 extends ServerBase {
    private static final Logger logger = Logger.getLogger(Server1.class.getName());

    public Server1(int port) {
        super(port);
    }

    @Override
    public void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(getPort())) {
                logger.info("Server1 started on port " + getPort());
                while (true) {
                    try (Socket clientSocket = serverSocket.accept();
                         ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                         ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                        Object inputObject = in.readObject();
                        Message requestMessage;
                        if (inputObject instanceof Message) {
                            requestMessage = (Message) inputObject;
                        } else {
                            logger.severe("Invalid message type received.");
                            continue;
                        }

                        Message responseMessage;
                        if ("STRT".equals(requestMessage.getDemand())) {
                            responseMessage = new Message("STRT", "YEP");
                        } else {
                            responseMessage = new Message("STRT", "NOP");
                        }

                        out.writeObject(responseMessage);
                        logger.info("Server1 sent response: " + responseMessage);

                        // Capacity nesnesini oluştur ve plotter'a gönder
                        Capacity capacity = new Capacity(1000, System.currentTimeMillis());
                        sendCapacityToPlotter(capacity);

                    } catch (Exception e) {
                        logger.severe("Error handling client connection in Server1: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.severe("Could not start Server1 on port " + getPort() + ": " + e.getMessage());
            }
        }).start();
    }

    private void sendCapacityToPlotter(Capacity capacity) {
        try (Socket plotterSocket = new Socket("localhost", 12348);
             PrintWriter plotterOut = new PrintWriter(plotterSocket.getOutputStream(), true)) {

            Gson gson = new Gson();
            String capacityJson = gson.toJson(Map.of(
                    "server_status", capacity.getServerStatus(),
                    "timestamp", capacity.getTimestamp()
            ));
            plotterOut.println(capacityJson);
            logger.info("Sent capacity data to plotter: " + capacityJson);
        } catch (IOException e) {
            logger.severe("Error sending capacity data to plotter: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server1 server = new Server1(12345);
        server.startServer();
    }
}
