package com.moco.cantina;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Iterator;
import java.util.List;

public class DataParser {
    private String data;
    private JsonNode jsonData;

    public DataParser(String data) {
        this.data = data;
    }

    public void search(String searchCriteria) {
        if (searchCriteria.startsWith(".")) {
            search("classNames", searchCriteria.substring(1), getJsonData());
        } else if (searchCriteria.startsWith("#")) {
            search("identifier", searchCriteria.substring(1), getJsonData());
        } else {
            search("class", searchCriteria, getJsonData());
        }
    }

    private void search(String field, String value, JsonNode data) {
        // Check this node based on search criteria.
        if (data.has(field)) {
            boolean nodeFound = false;
            JsonNode jsonNode = data.get(field);
            if (jsonNode instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) jsonNode;
                Iterator<JsonNode> arrayIterator = arrayNode.elements();
                while (arrayIterator.hasNext()) {
                    if (arrayIterator.next().asText().equals(value)) {
                        nodeFound = true;
                        break;
                    }
                }
            } else if (jsonNode.asText().equals(value)) {
                nodeFound = true;
            }

            if (nodeFound) {
                System.out.printf("Node Found: %n%s%n", data.toPrettyString());
            }
        }

        // Iterate through elements, recursing on arrays
        Iterator<JsonNode> iterator = data.elements();
        while (iterator.hasNext()) {
            JsonNode next = iterator.next();
            if (next instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) next;
                Iterator<JsonNode> arrayIterator = arrayNode.elements();
                while (arrayIterator.hasNext()) {
                    search(field, value, arrayIterator.next());
                }
            } else if (next instanceof ObjectNode) {
                search(field, value, next);
            }
        }
    }

    private JsonNode getJsonData() {
        if (jsonData == null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                jsonData = mapper.readTree(data);
            } catch (JsonProcessingException e) {
                System.out.printf("Data provided is not valid JSON%n.");
            }
        }

        return jsonData;
    }
}
