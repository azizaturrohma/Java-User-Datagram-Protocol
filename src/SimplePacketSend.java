import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SimplePacketSend {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        DatagramSocket socket = new DatagramSocket();
        String message = "Azizatur Rohma";

        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getLocalHost(),
                2000);

        // Send the packet
        socket.send(packet);

        // Close the socket
        socket.close();
    }
}
