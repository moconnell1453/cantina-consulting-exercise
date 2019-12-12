package com.moco.cantina;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataParser {
    private String data;
    private JsonNode jsonData;

    public DataParser(String data) {
        this.data = data;
    }

    public List<JsonNode> search(String searchCriteria) {
        if (searchCriteria.startsWith(".")) {
            return search("classNames", searchCriteria.substring(1), getJsonData());
        } else if (searchCriteria.startsWith("#")) {
            return search("identifier", searchCriteria.substring(1), getJsonData());
        } else {
            return search("class", searchCriteria, getJsonData());
        }
    }

    private List<JsonNode> search(String field, String value, JsonNode data) {
        List<JsonNode> nodesFound = new ArrayList<>();

        // Check this node based on search criteria.
        if (data.has(field)) {
            JsonNode jsonNode = data.get(field);
            if (jsonNode instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) jsonNode;
                Iterator<JsonNode> arrayIterator = arrayNode.elements();
                while (arrayIterator.hasNext()) {
                    if (arrayIterator.next().asText().equals(value)) {
                        nodesFound.add(data);
                        break;
                    }
                }
            } else if (jsonNode.asText().equals(value)) {
                nodesFound.add(data);
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
                    nodesFound.addAll(search(field, value, arrayIterator.next()));
                }
            } else if (next instanceof ObjectNode) {
                nodesFound.addAll(search(field, value, next));
            }
        }

        return nodesFound;
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
