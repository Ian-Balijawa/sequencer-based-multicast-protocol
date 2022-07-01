package sequencer;
import java.io.*;
import java.net.*;

public class SequencerJoinInfo implements Serializable
{
    public InetAddress addr;
    public long sequence;

    public SequencerJoinInfo(InetAddress addr, long sequence) {
        this.addr = addr;
        this.sequence = sequence;
    }
}
 