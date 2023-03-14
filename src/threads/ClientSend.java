package threads;

import java.io.PrintWriter;
import java.util.Scanner;

public class ClientSend implements Runnable{
    private PrintWriter out;
    private Scanner scanner;

    public ClientSend(PrintWriter out, Scanner scanner) {
        this.out = out;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        while(true) {
            if(Thread.currentThread().isInterrupted()){
                System.out.println("Lost connection");
                break;
            }
            String msg = scanner.nextLine();
            out.println(msg);
            if(msg.equals("!exit")){
                break;
            }
        }
    }
}
