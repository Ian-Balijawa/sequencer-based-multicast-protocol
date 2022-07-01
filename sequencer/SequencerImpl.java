package sequencer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class SequencerImpl extends UnicastRemoteObject implements Sequencer {

    private static final long serialVersionUID = 1L;
    History history;
    Vector<String> myClients;
    InetAddress grpIP;
    MulticastSocket multicastSock;
    final int PORT = 4446;
    final int MAX_MSG_LENGTH = 1024;
    String name;
    int sequenceNo;

    public SequencerImpl(String string) throws RemoteException {
        this.name = string;
        try {
            history = new History();
            myClients = new Vector<>();
            multicastSock = new MulticastSocket(PORT);
            grpIP = InetAddress.getByName("224.6.7.8");
        } catch (Exception e) {
            System.out.println("Failed to initialize sequencer " + e);
        }
    }

    public static void main(String[] args) {
        try {
            SequencerImpl impl = new SequencerImpl("MySequencer");
            Naming.rebind("/MySequencer", impl);
            System.out.println("Ready to continue..");
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public SequencerJoinInfo join(String sender) throws RemoteException, SequencerException {
        if (myClients.contains(sender)) {
            throw new SequencerException(sender + "not unique");
        } else {
            myClients.addElement(sender);
            history.noteReceived(sender, sequenceNo);
            return new SequencerJoinInfo(grpIP, sequenceNo);

        }
    }

    @Override
    public void send(String sender, byte[] msg, long msgID, long lastSequenceReceived) throws RemoteException {
        try {
            // Marshalling the data
            ByteArrayOutputStream bstream = new ByteArrayOutputStream(MAX_MSG_LENGTH);
            DataOutputStream dstream = new DataOutputStream(bstream);
            dstream.writeLong(++sequenceNo);
            dstream.write(msg, 0, msg.length);
            DatagramPacket message = new DatagramPacket(bstream.toByteArray(), bstream.size(), grpIP, PORT);
            multicastSock.send(message);
        } catch (Exception ex) {
            System.out.println("couldnt send message" + ex);
        }
        history.noteReceived(sender, lastSequenceReceived);
        history.addMessage(sender, sequenceNo, msg);

    }

    @Override
    public void leave(String sender) throws RemoteException {
        // remove the client name from the sequencer list
        myClients.removeElement(sender);
        // remove from oour history file
        history.eraseSender(sender);
    }

    @Override
    public byte[] getMissing(String sender, long sequence) throws RemoteException, SequencerException {
        byte[] exist = history.getMsg(sequence);
        if (exist != null)// if the number is there
        {
            System.out.print("Sequencer gets missing" + sequence);
            return exist;
        } else {
            System.out.print("Sequencer couldn't get sequence number" + sequence);
            throw new SequencerException("couldn't get sequence number" + sequence);
        }
    }

    @Override
    public void heartbeat(String sender, long lastSequenceReceived) throws RemoteException {
        System.out.print(sender + "Heartbeat:" + lastSequenceReceived);
        history.noteReceived(sender, lastSequenceReceived);
    }

    // extra method to help receive packets
    public void receive() throws IOException {
        byte[] buffer = new byte[1000];
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
        multicastSock.receive(messageIn);
        String receivedMessage = new String(messageIn.getData());
        System.out.println("Message received: " + receivedMessage);
    }
}