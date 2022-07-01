# A Totally Ordered Multicast Protocol

## Achieving total orderliness using a sequencer based multicast protocol with Java RMI

### This program implements a sequencer-based multicasting service and a small client application to exercise it.

### The program uses Java RMI and multicast sockets.

# TestSequencer

## Each instance of this class (several are created) will allow the user to enter strings and multicast them to a group of instances of TestSequencer

# Group.java

## TestSequencer uses an instance of Group for group communication services. Group in turn uses a MulticastSocket to receive incoming messages, and uses RMI to the sequencer for sending messages and other, sequencer-specific operations

# Sequencer.java, SequencerImpl.java

## The interface to and implementation of a sequencer

# History.java

## This is an implementation of the sequencer's history for storing a certain number of multicast messages. The sequencer keeps the history since some of the past messages may not have reached some group members. History does not need to have a fixed capacity, but obsolete items should be removed whenever its size grows beyond a threshold, say 1024 entries, or the sequencer may use a special synchronization phase with all the group members.

## Choosing a multicast address and time-to-live

## SequencerImpl should take as an argument the IP multicast address that the corresponding Groups are to use. To avoid collisions with numbers chosen by others, incorporate a random number into the multicast IP address that you use -- e.g. 234.day.month.rand, where day and month are chosen from a team member's birthday, and rand is a random number between 1 and 254.

## Don't set your sockets' time-to-live (TTL) to more than 1. That way, your multicast packets will not be transmitted beyond the local Mbone router.
