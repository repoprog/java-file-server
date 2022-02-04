package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileStorage {
    private static final String ROOT = System.getProperty("user.dir") +
            File.separator + "src" + File.separator
            + "server" + File.separator + "data" + File.separator;

    public static void makeDir() {
        File folder = new File(ROOT);
        folder.mkdir();
    }

    public static String getFile(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(ROOT + fileName)));
            return ResponseStatus.OK.getCode() + " " + content;
        } catch (IOException e) {
            return ResponseStatus.NOT_FOUND.getCode() + "";
        }
    }

    public static String addFile(String fileName, String fileContent) {
        File file = new File(ROOT + fileName);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseStatus.OK.getCode() + "";
        } else {
            return ResponseStatus.FORBIDDEN.getCode() + "";
        }
    }

    public static String deleteFile(String fileName) {
        File file = new File(ROOT + fileName);
        return file.delete() ? ResponseStatus.OK.getCode() + ""
                : ResponseStatus.NOT_FOUND.getCode() + "";
    }
}