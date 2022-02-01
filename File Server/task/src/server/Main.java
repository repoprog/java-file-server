package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileServer server = new FileServer();
        boolean quit = false;
        Scanner scanner = new Scanner(System.in);
        while (!quit) {
            String[] input = scanner.nextLine().split(" ");
            String command = input[0];
            String fileName = input.length == 2 ? input[1] : "";
            switch (command.toLowerCase()) {
                case "add" -> server.addFile(fileName);
                case "get" -> server.getFile(fileName);
                case "delete" -> server.deleteFile(fileName);
                case "exit" -> quit = true;
                default -> System.out.println("wrong command");
            }
        }
    }
}

class FileServer {
    private List<String> filesList;

    public FileServer() {
        this.filesList = new ArrayList<>();
    }

    public void addFile(String fileName) {
        String regexp = "file([1-9]|10)";
        if (fileName.matches(regexp) && !filesList.contains(fileName)) {
            filesList.add(fileName);
            System.out.println("The file " + fileName + " added successfully");
        } else {
            System.out.println("Cannot add the file " + fileName);
        }
    }

    public void getFile(String fileName) {
        if (filesList.contains(fileName)) {
            filesList.indexOf(fileName);
            System.out.println("The file " + fileName + " was sent");
        } else {
            System.out.println("The file " + fileName + " not found");
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