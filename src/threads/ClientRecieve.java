package threads;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientRecieve implements Runnable{
    private BufferedReader in;
    private Thread sender;

    public ClientRecieve(BufferedReader in, Thread sender) {
        this.in = in;
        this.sender = sender;
    }

    @Override
    public void run() {
        while(true){
            try {
                String msg = in.readLine();
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                System.out.println(msg);
            } catch (IOException e) {
                sender.interrupt();
                break;
            }


        }

    }
}
