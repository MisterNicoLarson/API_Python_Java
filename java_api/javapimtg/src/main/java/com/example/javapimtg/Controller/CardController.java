package com.example.javapimtg.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.javapimtg.Model.Card;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CardController is a REST controller for managing card data.
 *
 * <p>This controller handles the following operations:</p>
 * <ul>
 *     <li>GET requests to return a list of all cards with their properties.</li>
 *     <li>POST requests to add a new card with the provided JSON data.</li>
 *     <li>DELETE requests to remove a specified card.</li>
 * </ul>
 *
 * <p>The controller reads from and writes to a JSON file (mtgCards.json) to manage the card data.</p>
 */
@RestController
@RequestMapping("/cards")
public class CardController {

    private static Path folderPath = Paths.get("mtgCards.json").toAbsolutePath().normalize();
    private static String pathJSON = folderPath.toString();
    private static File jsonFile = new File(pathJSON);

    /**
     * Handles GET requests to return a list of all cards with their properties.
     *
     * <p>This method reads the JSON file containing the card data, parses it into a map of cards,
     * and returns a string representation of all the cards with their properties.</p>
     *
     * @return A string representation of all the cards with their properties.
     */
    @GetMapping
    public static String returnAllCards() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            StringBuilder lCardString = new StringBuilder();
            for (Map.Entry<String, Card> entry : cardMap.entrySet()) {
                String cardName = entry.getKey();
                Card card = entry.getValue();
                lCardString.append("Name: ").append(cardName).append(",\nDetails: ").append(card).append("\n\n");
            }
            return lCardString.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }

    /**
     * Handles POST requests to add a new card with the provided JSON data.
     *
     * <p>This method reads the JSON file containing the card data, adds the new cards to the map,
     * and writes the updated map back to the JSON file.</p>
     *
     * @param cards A map of new cards to be added.
     * @return A message indicating that the new cards have been added to the collection.
     */
    @PostMapping
    public String postNewCards(@RequestBody Map<String, Card> cards) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            cardMap.putAll(cards);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, cardMap);
            return cards.keySet() + " has been added to the collection.";
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Handles DELETE requests to remove a specified card from the collection.
     *
     * <p>This method reads the JSON file containing the card data, removes the specified card from the map,
     * and writes the updated map back to the JSON file.</p>
     *
     * @param cards A map containing the card to be removed. The key is the card name, and the value is the Card object.
     * @return A message indicating that the specified card has been removed from the collection.
     */
    @DeleteMapping
    public String deleteSelectedCard(@RequestBody Map<String, Card> cards) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            for (String cardName : cards.keySet()) {
                cardMap.remove(cardName);
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, cardMap);
            return cards.keySet() + " has been removed from the collection.";
        } catch (Exception e) {
            return e.toString();
        }
    }

    @GetMapping("/{cardName}")
    public String getCard(@PathVariable String cardName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            StringBuilder lCardString = new StringBuilder();
            Card card = cardMap.get(cardName);
            if(card != null){
                lCardString.append("Name: ").append(cardName).append(",\nDetails: ").append(card).append("\n\n");
                return lCardString.toString();
            }
            else{
                return "The card isn't in the API.";
            }
            
        } catch (IOException e) {
            return e.toString();
        }
    }

    @PutMapping("/{cardName}")
    public String updateCard(@PathVariable String cardName, @RequestBody Card card) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            StringBuilder lCardString = new StringBuilder();
            Card cardToUpdate = cardMap.get(cardName);
            if(cardToUpdate != null){
                lCardString.append("Name: ").append(cardName).append(",\nDetails: ").append(card).append("\n\n");
                return cardName + " has been update.";
            }
            else{
                return "The card isn't in the API.";
            }
        } catch (IOException e) {
            return e.toString();
        }
    }

    @PatchMapping("/{cardName}")
    public String patchCard(@PathVariable String cardName, @RequestBody Card card) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            StringBuilder lCardString = new StringBuilder();
            Card cardToUpdate = cardMap.get(cardName);
            if(cardToUpdate != null){
                lCardString.append("Name: ").append(cardName).append(",\nDetails: ").append(card).append("\n\n");
                return cardName + " has been update.";
            }
            else{
                return "The card isn't in the API.";
            }
        } catch (IOException e) {
            return e.toString();
        }
    }

    @DeleteMapping("/{cardName}")
    public String removeCard(@PathVariable String cardName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            Card card = cardMap.get(cardName);
            if(card != null){
                cardMap.remove(cardName);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, cardMap);
                return cardName + " has been removed from the API.";
            }
            else{
                return "The card isn't in the API.";
            }
            
        } catch (IOException e) {
            return e.toString();
        }
    }
}