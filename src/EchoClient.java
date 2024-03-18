import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoClient {
    public static final int SERVICE_PORT = 7;
    public static final int BUFSIZE = 256;

    public static void main(String[] args) throws SocketException, IOException {
        String hostname = "localhost";
        InetAddress addr = InetAddress.getByName(hostname);

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (;;) {
            System.out.println("Write your message here..");

            // Read message from user, read line/line
            String message = reader.readLine();

            // Exit the program if user type "exit"
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program, thank you");
                break;
            }

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(bout);
            pout.print(message);

            // Get the contents of our message as an array of bytes
            byte[] barray = bout.toByteArray();

            // Create a datagram packet, containing our byte array
            DatagramPacket packet = new DatagramPacket(barray, barray.length, addr, SERVICE_PORT);
            System.out.println("Sending packet to " + hostname);
            socket.send(packet);

            System.out.println("Waiting for packet....");

            // Create a small packet for receiving UDP packets
            byte[] recbuf = new byte[BUFSIZE];
            DatagramPacket receivePacket = new DatagramPacket(recbuf, BUFSIZE);

            // Declare a timeout flag
            boolean timeout = false;

            // Catch any Interrupted IOException that is thrown
            // while waiting to receive a UDP packet
            try {
                socket.receive(receivePacket);
            } catch (InterruptedIOException ioe) {
                timeout = true;
            }

            if (!timeout) {
                System.out.println("packet received!");
                System.out.println("Details: " + receivePacket.getAddress());

                // Obtain a byte input stream to read the UDP packet
                ByteArrayInputStream bin = new ByteArrayInputStream(receivePacket.getData(), 0,
                        receivePacket.getLength());

                // Connect a reader for easier access
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(bin));
                System.out.println(reader2.readLine());
            } else {
                System.out.println("packet lost!");
            }

            // Close the socket
            socket.close();
        }
    }
}