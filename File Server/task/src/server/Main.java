package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {

    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    public static void main(String[] args) {
        System.out.println("Server started!");

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            boolean quit = false;
            while (!quit)
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String received = input.readUTF();
                    String sent = "All files were sent!";
                    System.out.println("Received: " + received +
                            "\nSent: " + sent);
                    output.writeUTF(sent);

                    quit = true; // end connection after one receive/sent
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}