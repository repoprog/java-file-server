package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class Main {

    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    public static void main(String[] args) {

        System.out.println("Client started!");

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String sent = "Give me everything you have!";
            output.writeUTF(sent);
            String received = input.readUTF();

            System.out.println("Sent: " + sent +
                    "\nReceived: " + received);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}