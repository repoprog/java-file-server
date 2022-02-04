package client;

import server.FileStorage;

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
        FileStorage.makeDir();
        System.out.println("System.out.println(Enter action (1 - get a file, 2 - create a file, 3 - delete a file):);\n");
        boolean quit = false;
        while (!quit) {
            String action = scanner.nextLine();
            switch (action) {
                case "1" -> client.getFile();
                case "2" -> client.createFile();
                case "3" -> client.deleteFile();
                case "exit" -> {
                    quit = true;
                    client.shutDownServer();
                }
                default -> System.out.println("Wrong command");
            }
        }
    }
}

class Client {
    private final String address = "127.0.0.1";
    private final int port = 23456;
    Scanner scanner = new Scanner(System.in);
    private final String PUT = "put";
    private final String GET = "get";
    private final String DELETE = "delete";


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

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String send = GET + " " + fileName;
            output.writeUTF(send);
            String received = input.readUTF();
            System.out.println("The request was sent.");
            if (received.startsWith("200")) {
                System.out.println("The content of the file is: " +
                        received.substring(4));
            } else {
                System.out.println("The response says that the file was not found!");
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void putRequest(String fileName, String content) {

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {

            String send = PUT + " " + fileName + " " + content;
            output.writeUTF(send);
            String received = input.readUTF();
            System.out.println("The request was sent.");
            if (received.startsWith("200")) {
                System.out.println("The response says that file was created!");
            } else {
                System.out.println("The response says that creating the file was forbidden!");
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRequest(String fileName) {

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String send = DELETE + " " + fileName;
            output.writeUTF(send);
            String received = input.readUTF();
            System.out.println("The request was sent.");
            if (received.startsWith("200")) {
                System.out.println("The response says that the file was successfully deleted!");
            } else {
                System.out.println("The response says that the file was not found!");
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void shutDownServer() {
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeUTF("exit");
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}

