package client;

import server.FileStorage;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        client.makeDirs();
        FileStorage.makeDirs();
        System.out.println("System.out.println(Enter action " +
                "(1 - get a file, 2 - save a file, 3 - delete a file):);\n");
        boolean quit = false;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        while (!quit) {
            String action = scanner.nextLine();
            switch (action) {
                case "1" -> client.getFile();
                case "2" -> client.putFile();
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

