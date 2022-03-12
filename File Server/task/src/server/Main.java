package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

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
    static byte[] savedContent;

    public void start() {
        System.out.println("Server started!");
            loadFilesIndex();

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {

            while (true)
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    // Request from Client
                    String received = input.readUTF();
                    byte[] rcvContent = new byte[0];
                    if (received.startsWith("exit")) {
                        break;
                    } else if (received.startsWith("put")) {
                        int length = input.readInt();
                        rcvContent = new byte[length];
                        input.readFully(rcvContent, 0, rcvContent.length);
                    }
                    // Response to Client
                    String respond = processRequest(received, rcvContent);
                    output.writeUTF(respond);
                    if (received.startsWith("get") && respond.startsWith("200")) {
                        output.writeInt(savedContent.length);
                        output.write(savedContent);
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFilesIndex() {
        Map<Integer, String> indexMap =
                SerializationUtils.deserialize(FileStorage.INDEX_PATH, FileStorage.FILE);
        FileStorage.setFilesIndex(indexMap);
        int id = indexMap.keySet().stream().max(Integer::compareTo).orElse(0);
        FileStorage.setFileId(id);
    }

    public String processRequest(String received, byte[] content) {
        String[] rcvParts = received.split(" ");
        System.out.println(Arrays.toString(rcvParts));
        String requestType = rcvParts[0];
        return switch (requestType) {
            case "get" -> FileStorage.getFile(rcvParts[1], rcvParts[2]);
            case "put" -> FileStorage.saveFile(rcvParts[1], content);
            case "delete" -> FileStorage.deleteFile(rcvParts[1], rcvParts[2]);
            case "show" -> FileStorage.showFiles();
            default -> "bad request";
        };
    }
}


