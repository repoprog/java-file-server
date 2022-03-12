package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Client {
    private final String address = "127.0.0.1";
    private final int port = 23456;
    Scanner scanner = new Scanner(System.in);

    private final String PUT = "put";
    private final String GET = "get";
    private final String DELETE = "delete";
    private static final String FILE_DRIVE_PATH = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "client" +
            File.separator + "data" +
            File.separator;

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

    public void putFile() {
        System.out.println("Enter name of the file: ");
        String savedFile = scanner.nextLine();
        String[] savedParts = savedFile.split("\\.");
        String fileFormat = "." + savedParts[1];
        byte[] content = getFileAsByteArray(savedFile);
        System.out.println("Enter name of the file to be saved on server: ");
        String fileName = scanner.nextLine();
        fileName = fileName.isEmpty() ? generateFileName() + fileFormat : fileName;
        System.out.println(fileName);
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
            String request = GET + " " + getterType + " " + file;
            output.writeUTF(request);
            String received = input.readUTF();
            System.out.println("The request was sent.");
            if (received.startsWith("200")) {
                System.out.println("The file was downloaded!");
                int length = input.readInt();
                byte[] savedContent = new byte[length];
                input.readFully(savedContent, 0, savedContent.length);
                saveFileOnDrive(savedContent);
            } else {
                System.out.println("The response says that this file is not found!");
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void putRequest(String fileName, byte[] content) {

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String request = PUT + " " + fileName;
            output.writeUTF(request);
            output.writeInt(content.length);
            output.write(content);
            String received = input.readUTF();
            System.out.println("The request was sent.");
            if (received.startsWith("200")) {
                String[] rcvParts = received.split(" ");
                System.out.println("Response says that file is saved! ID = " + rcvParts[1]);
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
            String request = DELETE + " " + getterType + " " + file;
            output.writeUTF(request);
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

    public void makeDirs() {
        File file = new File(FILE_DRIVE_PATH);
        file.mkdirs();
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

    public byte[] getFileAsByteArray(String fileName) {
        byte[] fileAsBytes = new byte[0];
        try {
            fileAsBytes = (Files.readAllBytes(Paths.get(FILE_DRIVE_PATH + fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileAsBytes;
    }

    public String generateFileName() {
        LocalDateTime dt = LocalDateTime.now();
        return dt.toString().replace(":", "-").replace(".", "");
    }

    public void saveFileOnDrive(byte[] fileContent) {
        System.out.print("Specify a name for it:");
        File file = new File(FILE_DRIVE_PATH + scanner.nextLine());
        if (!file.exists()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("File saved on the hard drive!");
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
