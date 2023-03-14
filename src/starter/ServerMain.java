package starter;

import model.Message;
import threads.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerMain {
    public static int PORT = 9000;
    public static List<String> users = new CopyOnWriteArrayList<>();
    public static List<Message> messagesHistory = new CopyOnWriteArrayList<>();
    public static List<String> forbiddenWords = new CopyOnWriteArrayList<>(Arrays.asList("dogma", "drugs","heroine","nigga","nazi"));
    public static List<Server> activeUsers = new CopyOnWriteArrayList<>();

    public static void main(String[] args)  {
        try {
            ServerSocket serverSocket = new ServerSocket(ServerMain.PORT);
            while(true){
                Socket socket = serverSocket.accept();
                Thread serverThread = new Thread(new Server(socket));
                serverThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
}
