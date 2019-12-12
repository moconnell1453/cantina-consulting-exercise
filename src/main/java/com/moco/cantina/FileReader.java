package com.moco.cantina;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {
    private String filepath;

    public FileReader(String filepath) {
        this.filepath = filepath;
    }

    public String getFileData() {
        return filepath.startsWith("http") ? getFileDataWeb() : getFileDataLocal();
    }

    private String getFileDataWeb() {
        try {
            StringBuilder dataBuilder = new StringBuilder();
            URL url = new URL(filepath);
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                dataBuilder.append(scanner.nextLine());
            }
            return dataBuilder.toString();
        } catch (MalformedURLException e) {
            System.out.printf("Filepath '%s' is not a valid URL%n", filepath);
        } catch (IOException e) {
            System.out.printf("Failed to read data from filepath '%s'.%n", filepath);
        }

        return null;
    }

    private String getFileDataLocal() {
        Path path = Paths.get(filepath);
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            return stream.collect(Collectors.joining());
        } catch (IOException e) {
            System.out.printf("Error reading from specified filepath: %s%n", filepath);
        }

        return null;
    }
}
