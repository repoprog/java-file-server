package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        System.out.println("System.out.println(Enter action (1 - get a file, 2 - create a file, 3 - delete a file):);\n");

        while (true) {
            String action = scanner.nextLine();
            switch (action) {
                case "1" -> client.getFile();
                case "2" -> client.createFile();
                case "3" -> client.deleteFile();
//                case "exit" ->
                default -> System.out.println("Wrong command");
            }
        }
    }

}

class Client {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;
    Scanner scanner = new Scanner(System.in);
    String PUT = "put";
    String GET = "get";
    String DELETE = "delete";


    public void getFile() {
        System.out.println("Enter filename: ");
        getRequest(scanner.nextLine());
    }

    public void createFile() {
        System.out.println("Enter filename: ");
        String fileName = scanner.nextLine();
        System.out.println("Enter file content: ");
        String content = scanner.nextLine();
        putRequest(fileName, content);
    }

    public void deleteFile() {
        System.out.println("Enter filename: ");
        deleteRequest(scanner.nextLine());
    }


    public void getRequest(String fileName) {

        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {

            String sent = GET + " " + fileName;
            output.writeUTF(sent);
            String received = input.readUTF();
            System.out.println("The request was sent.");
            System.out.println("Sent: " + sent +
                    "\nReceived: " + received);

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void putRequest(String fileName, String content) {

        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {

            String sent = PUT + " " + fileName;
            output.writeUTF(sent);
            String received = input.readUTF();
            System.out.println("The request was sent.");
            System.out.println("Sent: " + sent +
                    "\nReceived: " + received);

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRequest(String file) {

        System.out.println("The request was sent.");
    }

}
