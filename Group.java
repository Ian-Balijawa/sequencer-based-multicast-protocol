package sequencer;
import java.net.*;
import java.util.*;
import java.io.*;
import java.rmi.*;

public class Group implements Runnable
{

    public Group(String host, MsgHandler handler, String senderName)  throws GroupException
    {
       // contact Sequencer on "host" to join group,
       // create MulticastSocket and thread to listen on it,
       // perform other initialisations
    }

    public void send(byte[] msg) throws GroupException
    {
        // send the given message to all instances of Group using the same sequencer
    }

    public void leave()
    {
       // leave group
    }

    public void run()
    {
        // repeatedly: listen to MulticastSocket created in constructor, and on receipt
        // of a datagram call "handle" on the instance
        // of Group.MsgHandler which was supplied to the constructor
    }

    public interface MsgHandler
    {
         public void handle(int count, byte[] msg);
    }

    public class GroupException extends Exception
    {
        public GroupException(String s)
        {
            super(s);
        }
    }

    public class HeartBeater extends Thread
    {
        // This thread sends heartbeat messages when required
    }

}