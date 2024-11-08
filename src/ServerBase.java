import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ServerBase {
    private final int port;
    private static final Logger logger = Logger.getLogger(ServerBase.class.getName());

    public ServerBase(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                logger.info("Server started on port " + port);
                while (true) {
                    try (Socket clientSocket = serverSocket.accept();
                         ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                        logger.info("New connection established with " + clientSocket.getInetAddress());

                        // İsteğe bağlı işlem yapılabilir
                        Message responseMessage = new Message("Connection", "Established");
                        out.writeObject(responseMessage);  // Mesajı gönder
                        logger.info("Sent response: " + responseMessage);

                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Error handling client connection: " + e.getMessage(), e);
                    }
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not start server on port " + port + ": " + e.getMessage(), e);
            }
        }).start();
    }

    public void connectToServer(String host, int port) {
        new Thread(() -> {
            try (Socket socket = new Socket(host, port);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                logger.info("Connected to server on " + host + ":" + port);

                // Sunucuya bağlantı yapıldıktan sonra bir işlem gerçekleştirebilirsiniz
                Message message = new Message("STRT", "Connecting");
                out.writeObject(message);
                logger.info("Sent message to server: " + message);

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not connect to server on " + host + ":" + port + ": " + e.getMessage(), e);
            }
        }).start();
    }
}
