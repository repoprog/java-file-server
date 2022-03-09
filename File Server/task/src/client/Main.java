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
//        FileStorage.makeDirs();
        System.out.println("System.out.println(Enter action (1 - get a file, 2 - create a file, 3 - delete a file):);\n");
        boolean quit = false;
        while (!quit) {
            String action = scanner.nextLine();
            switch (action) {
                case "1" -> client.getFile();
                case "2" -> client.createFile();
                case "3" -> client.deleteFile();
                case "4" -> client.showFiles();
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
        System.out.printf("Do you want to get the file by name or by id " +
                "(1 - name, 2 = id): ");
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Enter filename: ");
                String getterTYpe = "BY_NAME";
                getRequest(getterTYpe, scanner.nextLine());
            }
            case "2" -> {
                String getterType = "BY_ID";
                System.out.println("Enter id: ");
                getRequest(getterType, scanner.nextLine());
            }
        }
    }

    public void createFile() {
        System.out.println("Enter filename: ");
        String fileName = scanner.nextLine();
        System.out.println("Enter file content: ");
        String content = scanner.nextLine();
        putRequest(fileName, content);
    }

    public void deleteFile() {
        System.out.println("Do you want to delete the file by name or by id " +
                "(1 - name, 2 - id): ");
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Enter filename: ");
                String getterTYpe = "BY_NAME";
                deleteRequest(getterTYpe, scanner.nextLine());
            }
            case "2" -> {
                String getterType = "BY_ID";
                System.out.println("Enter id: ");
                deleteRequest(getterType, scanner.nextLine());
            }
        }
    }

    public void getRequest(String getterType, String file) {

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String send = GET + " " + getterType + " " + file;
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
                System.out.println("recieved: " + received);
                String[] rcvParts= received.split(" ");
                System.out.println("The response says that file was saved! ID = " + rcvParts[1]);
            } else {
                System.out.println("The response says that creating the file was forbidden!");
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRequest(String getterType, String file) {

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String send = DELETE + " " + getterType + " " + file;
            output.writeUTF(send);
            String received = input.readUTF();
            System.out.println("The request was sent.");
            if (received.startsWith("200")) {
                System.out.println("The response says that this file was deleted successfully!");
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
            System.out.println("The request was sent.");
            output.writeUTF("exit");
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void showFiles() {
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeUTF("show");
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}

