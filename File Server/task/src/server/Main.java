package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

enum ResponseStatus {
    OK(200),
    FORBIDDEN(403),
    NOT_FOUND(404);

    final int code;

    ResponseStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}

class Server {

    private final String address = "127.0.0.1";
    private final int port = 23456;

    public void start() {

        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {

            while (true)
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String received = input.readUTF();
                    if (received.startsWith("exit")) {
                        break;
                    }
                    String respond = processRequest(received);
                    output.writeUTF(respond);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String processRequest(String received) {
        String[] rcvParts = received.split(" ");
        String reqType = rcvParts[0];
        String fileName = rcvParts[1];
        String fileContent = rcvParts.length == 3 ? rcvParts[2] : "";
        return switch (reqType) {
            case "get" -> FileStorage.getFile(fileName);
            case "put" -> FileStorage.addFile(fileName, fileContent);
            case "delete" -> FileStorage.deleteFile(fileName);
            default -> "bad request";
        };
    }
}


