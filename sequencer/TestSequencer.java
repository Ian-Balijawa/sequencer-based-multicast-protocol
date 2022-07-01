package sequencer;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;

public class TestSequencer implements Group.MsgHandler, Runnable {
    String returned;
    Group group;
    Thread myThread;
    String clientName;
    boolean paused;
    int rate;

    public TestSequencer(String host, String clientName) {
        // super("TestSequencer");
        returned = "Fred";
        paused = false;
        this.clientName = clientName;
        try {
            group = new Group(host, this, clientName);
            myThread = new Thread(this);
            myThread.start();
        } catch (Exception grp) {
            System.out.println("Can't create goup" + grp);
            grp.printStackTrace();
        }
    }

    // main
    public static void main(String[] kjm) throws MalformedURLException, RemoteException, NotBoundException {
        //
        if (kjm.length < 2) {
            System.out.println("we need more arguments");

        } else {
            TestSequencer ts = new TestSequencer(kjm[0], kjm[1]);
        }

    }

    public void run() {
        try {
            rate = 8;
            int i = 0;
            do {
                do {
                    if (rate <= 90)
                        try {
                            Thread.sleep((90 - rate) * 10);
                        } catch (Exception d) {
                        }

                } while (paused);
                BufferedReader write = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter your message:");
                String message = write.readLine();
                if (message.trim().equals("exit")) {
                    group.leave();
                    System.exit(1);
                }
                group.send((new String(clientName + message + i++)).getBytes());
            } while (true);
        } catch (Exception d) {
        }
    }

    public void handle(int count, byte[] msg) {
        String msg1 = new String(msg, 0, count);
    }
}