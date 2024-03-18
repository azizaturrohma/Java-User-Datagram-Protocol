import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class PacketSendDemo {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Packet Send\n====================");
        String hostname = "localhost";

        System.out.println("Binding to a local port");

        // Create a datagram socket, bound to any available local port
        try (DatagramSocket socket = new DatagramSocket()) {
            System.out.println("Bound to local port " + socket.getLocalPort());

            // Read message from keyboard input / user input
            System.out.print("Type your message to send: ");
            String message = input.nextLine();

            // Create a message to send using a UDP packet
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(bout);
            pout.print(message);

            // Get the contents of our message as an array of bytes
            byte[] barray = bout.toByteArray();

            // Create a datagram packet, containing our byte array
            DatagramPacket packet = new DatagramPacket(barray, barray.length);

            // Lookup the specified hostname, and get an InetAddress
            System.out.println("Looking up hostname " + hostname);
            InetAddress remote_addr = InetAddress.getByName(hostname);
            System.out.println("Hostname resolved as " + remote_addr.getHostAddress());

            // Address packet to sender
            packet.setAddress(remote_addr);

            // Set port number to 2000
            packet.setPort(2000);

            // Send the packet - remember no guarantee of delivery
            socket.send(packet);
            System.out.println("Packet sent!");

            // Close scanner input
            input.close();
        } catch (SocketException ex) {
            System.err.println("Error: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}