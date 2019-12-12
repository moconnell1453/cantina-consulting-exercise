package com.moco.cantina;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Get file path from arguments or input
        String filepath = args.length == 0 ? promptForFilepath() : args[0];

        // Read contents of file
        FileReader fileReader = new FileReader(filepath);

        // Parse file data as JSON
        DataParser dataParser = new DataParser(fileReader.getFileData());

        // Read search criteria from input
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String searchCriteria = scanner.next();
            List<JsonNode> nodesFound = dataParser.search(searchCriteria);
            System.out.printf("Found %d matching search criteria.%n", nodesFound.size());
            for (int i = 0; i < nodesFound.size(); i ++) {
                System.out.printf("Node %d:%n%s%n", i+1, nodesFound.get(i).toPrettyString());
            }
        }

        System.out.println();
    }

    private static String promptForFilepath() {
        // Get filepath from stdin
        System.out.printf("Enter filepath for view hierarchy JSON file.%n");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
