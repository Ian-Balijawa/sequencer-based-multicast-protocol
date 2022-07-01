package sequencer;

import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.Hashtable;

public class History extends Hashtable<String, Long> {
    // creating a hashtable to store the contents of the message, sender, sequence
    // number
    public Hashtable<String, Long> history;
    public static final int MAX_HISTORY = 1024;
    public long historyCleanedTo;

    // creating a class constructor to initialize variables
    public History() {
        history = new Hashtable<>();
        historyCleanedTo = -1L;
    }

    // adding a sender to the history
    public void noteReceived(String sender, long received) {
        history.put(sender, received);
    }

    // adding message to the history buffer
    public void addMessage(String sender, long sequenceNo, byte[] msg) {
        // check if message is available
        if (msg != null) {
            history.put(sender, sequenceNo);
        }

        // clearing buffer
        if (size() > MAX_HISTORY) {
            long minimum = 0x7fffffL;

            // getting the different sender in the hash table
            for (Enumeration<String> enum1 = history.keys(); enum1.hasMoreElements();) {
                String sent = enum1.nextElement();
                Long have = history.get(sent); // which is the already received DatagramPacket

                if (have < minimum) {
                    // assign new minimum number
                    minimum = have;
                }
            }

            // clearing datagrams
            for (long s = historyCleanedTo + 1L; s <= minimum; s++) {
                remove(String.valueOf(s));
                historyCleanedTo = s;
            }
        }
    }

    // getting a missing datagram
    public byte[] getMsg(long sequence) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(sequence);
        return buffer.array();
    }

    // removing a sender
    public synchronized void eraseSender(String sender) {
        history.remove(sender);
    }
}
