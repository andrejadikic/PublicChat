package starter;

import threads.ClientRecieve;
import threads.ClientSend;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static final int PORT = 9000;
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            socket = new Socket("127.0.0.1",PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            while (!usernameCheck(scanner, in, out)) {

            }
        } catch (IOException e) {
            try {
                socket.close();
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if(Thread.currentThread().isInterrupted()){
            return;
        }
        Thread sender = new Thread(new ClientSend(out,scanner));
        Thread receiver = new Thread(new ClientRecieve(in,sender));
        receiver.start();
        sender.start();

        try {
            sender.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        receiver.interrupt();
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean usernameCheck(Scanner scanner, BufferedReader in, PrintWriter out){
        System.out.println("Enter username:");
        String username=scanner.nextLine();
        out.println(username);
        try {
            String s=in.readLine();
            System.out.println(s);
            return !s.equals("Username already exists.");
        } catch (IOException e) {
            System.out.println("Connection lost.");
            Thread.currentThread().interrupt();
        }
        return true;
    }
}
