import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;

public class Server3 extends ServerBase {
    public Server3(int port) {
        super(port);
    }

    @Override
    public void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(getPort())) {
                System.out.println("Server3 started on port " + getPort());
                while (true) {
                    try (Socket clientSocket = serverSocket.accept();
                         ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                         ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                        Object inputObject = in.readObject();
                        Message requestMessage;

                        if (inputObject instanceof Message) {
                            requestMessage = (Message) inputObject;
                        } else {
                            System.err.println("Invalid message type received.");
                            continue;
                        }

                        Message responseMessage;
                        if ("STRT".equals(requestMessage.getDemand())) {
                            responseMessage = new Message("STRT", "YEP");
                        } else {
                            responseMessage = new Message("STRT", "NOP");
                        }

                        out.writeObject(responseMessage);
                        System.out.println("Server3 sent response: " + responseMessage);

                        // Capacity nesnesini oluştur ve plotter'a gönder
                        Capacity capacity = new Capacity(1000, System.currentTimeMillis());
                        sendCapacityToPlotter(capacity);

                    } catch (Exception e) {
                        System.err.println("Error handling client connection in Server3: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Could not start Server3 on port " + getPort() + ": " + e.getMessage());
            }
        }).start();
    }

    private void sendCapacityToPlotter(Capacity capacity) {
        try (Socket plotterSocket = new Socket("localhost", 12348);
             PrintWriter plotterOut = new PrintWriter(plotterSocket.getOutputStream(), true)) {

            Gson gson = new Gson();
            String capacityJson = gson.toJson(Map.of(
                    "server3_status", capacity.getServerStatus(),
                    "timestamp", capacity.getTimestamp()
            ));
            plotterOut.println(capacityJson);
            System.out.println("Sent capacity data to plotter: " + capacityJson);

        } catch (IOException e) {
            System.err.println("Error sending capacity data to plotter: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server3 server = new Server3(12347);
        server.startServer();
    }
}
