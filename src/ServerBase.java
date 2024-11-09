import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;

public abstract class ServerBase {
    private final int port;
    private static final Logger logger = Logger.getLogger(ServerBase.class.getName());
    private static final Gson gson = new Gson();

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
                         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        logger.info("New connection established with " + clientSocket.getInetAddress());

                        // İstemciden gelen JSON verisini oku
                        String requestJson = in.readLine();
                        if (requestJson != null) {
                            Message requestMessage = gson.fromJson(requestJson, Message.class);
                            logger.info("Received request: " + requestMessage);

                            // Mesaja bağlı olarak yanıt oluştur
                            Message responseMessage = new Message("Connection", "Established");
                            String responseJson = gson.toJson(responseMessage);
                            out.println(responseJson);  // JSON formatında yanıt gönder
                            logger.info("Sent response: " + responseJson);
                        }

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
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                logger.info("Connected to server on " + host + ":" + port);

                // Sunucuya JSON formatında bir mesaj gönder
                Message message = new Message("STRT", "Connecting");
                String messageJson = gson.toJson(message);
                out.println(messageJson);
                logger.info("Sent message to server: " + messageJson);

                // Sunucudan gelen yanıtı oku
                String responseJson = in.readLine();
                if (responseJson != null) {
                    Message responseMessage = gson.fromJson(responseJson, Message.class);
                    logger.info("Received response from server: " + responseMessage);
                }

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not connect to server on " + host + ":" + port + ": " + e.getMessage(), e);
            }
        }).start();
    }
}
