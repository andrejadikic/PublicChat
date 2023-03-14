package threads;

import model.Message;
import starter.ServerMain;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class Server implements Runnable{
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String user;

    public Server(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        login();
        notifyOther();
        printHistory();

        while(true){
            try {
                String msg=in.readLine();
                if(msg!=null && msg.equals("!exit")){
                    ServerMain.activeUsers.iterator().forEachRemaining((Server x) ->{
                        if(!x.equals(this)) {
                            x.out.println(user+" left the chat.");
                        }
                    });
                    close();
                    ServerMain.activeUsers.remove(this);
                    ServerMain.users.remove(user);

                    break;
                }
                Message message = new Message(msg,user, LocalDateTime.now());
                ServerMain.messagesHistory.add(message);
                ServerMain.activeUsers.iterator().forEachRemaining((Server server) ->{
                    if(!server.equals(this)) {
                        server.out.println(message);
                    }
                });

            } catch (IOException e) {
                ServerMain.activeUsers.remove(this);
                ServerMain.users.remove(user);
                break;
            }
        }

        close();

    }

    private void printHistory() {
        for( int i = (ServerMain.messagesHistory.size()>=100) ? ServerMain.messagesHistory.size()-100:0;i<ServerMain.messagesHistory.size();i++){
            out.println(ServerMain.messagesHistory.get(i));
        }
    }

    private void login(){
        while (true){
            try {
                String username=in.readLine();
                if(ServerMain.users.contains(username)){
                    out.println("Username already exists.");
                }
                else{
                    user=username;
                    ServerMain.users.add(user);
                    out.println("Welcome " + user);
                    ServerMain.activeUsers.add(this);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyOther() {
        ServerMain.activeUsers.iterator().forEachRemaining((Server active) ->{
            if(!active.equals(this))
                active.out.println("User "+ user + " has joined the chat");
        });
    }


    private void close(){
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
