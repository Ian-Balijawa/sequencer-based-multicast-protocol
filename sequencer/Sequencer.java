package sequencer;
import java.rmi.*;
import java.net.*;
import java.io.*;

public interface Sequencer extends Remote
{
    // join -- request for "sender" to join sequencer's multicasting service;
    // returns an object specifying the multicast address and the first sequence number to expect
    public SequencerJoinInfo join(String sender)
        throws RemoteException, SequencerException;

    // send -- "sender" supplies the msg to be sent, its identifier,
    // and the sequence number of the last received message
   public void send(String sender, byte[] msg, long msgID, long lastSequenceReceived)
        throws RemoteException;

    // leave -- tell sequencer that "sender" will no longer need its services
   public void leave(String sender)
        throws RemoteException;

    // getMissing -- ask sequencer for the message whose sequence number is "sequence"
   public byte[] getMissing(String sender, long sequence)
        throws RemoteException, SequencerException;

    // heartbeat -- we have received messages up to number "lastSequenceReceived"
   public void heartbeat(String sender, long lastSequenceReceived)
        throws RemoteException;
}