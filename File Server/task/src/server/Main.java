package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

enum ResponseStatus {
    OK(200),
    FORBIDDEN(403),
    NOT_FOUND(404);

    int code;

    ResponseStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

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
                    String sent = processRequest(received);
                    System.out.println("Received: " + received +
                            "\nSent: " + sent);
                    output.writeUTF(sent);

//                    quit = true; // end connection after one receive/sent
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static FileServer fileServer = new FileServer();

    public static String processRequest(String received) {
        String[] rcvParts = received.split(" ");
        String reqType = rcvParts[0];
        String fileName = rcvParts[1];
        String fileContent = rcvParts.length == 3 ? rcvParts[2] : "";
     return  switch (reqType) {
            case "get" -> fileServer.getFile(fileName);
            case "put" -> fileServer.addFile(fileName, fileContent);
//            case "delete" -> fileServer.deleteFile(fileName);
           default -> "bad request";
        };
    }
}

class FileServer {
    private List<String> filesList;

    public FileServer() {
        this.filesList = new ArrayList<>();
    }

    public String getFile(String fileName) {
        if (filesList.contains(fileName)) {
            filesList.indexOf(fileName);
           return ResponseStatus.OK.getCode() + " " + fileName;
        } else {
            return ResponseStatus.NOT_FOUND.getCode() + " " + fileName;
        }
    }

    public String addFile(String fileName, String fileContent) {
//        String regexp = "file([1-9]|10)"; fileName.matches(regexp) &&
        if (!filesList.contains(fileName)) {
            filesList.add(fileName);
            return ResponseStatus.OK.getCode() + " " + fileName;
        } else {
          return   ResponseStatus.NOT_FOUND.getCode() + " " + fileName;
        }
    }


    public void deleteFile(String fileName) {
        if (filesList.contains(fileName)) {
            filesList.remove(fileName);
            System.out.println("The file " + fileName + " was deleted");
        } else {
            System.out.println("The file " + fileName + " not found");
        }
    }
}

