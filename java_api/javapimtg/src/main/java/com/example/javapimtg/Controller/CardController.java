package com.example.javapimtg.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.javapimtg.Model.Card;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

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
     * @return A message indicating that the specified card has been removed from the collection or an error message.
     */
    @DeleteMapping
    public String deleteSelectedCard(@RequestBody Map<String, Card> cards) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});

            cards.keySet().forEach(cardMap::remove);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, cardMap);

            return cards.keySet() + " has been removed from the collection.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Retrieves the details of a card by its name.
     *
     * @param cardName The name of the card to retrieve.
     * @return A string containing the details of the card if found, or a message indicating that the card is not in the API.
     */
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

    /**
     * Updates the details of a card by its name.
     *
     * @param cardName The name of the card to update.
     * @param card The updated card details.
     * @return A message indicating whether the card was successfully updated or not found.
     */
    @PutMapping("/{cardName}")
    public String updateCard(@PathVariable String cardName, @RequestBody Card card) {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.info("Received PATCH request for card: {}", cardName);
        logger.info("Request body: {}", card);

        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            Card cardToUpdate = cardMap.get(cardName);
            if(cardToUpdate != null){
                cardMap.remove(cardName);
                cardMap.put(cardName, card);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, cardMap);
                return cardName + " has been update.";
            }
            else{
                return "The card isn't in the API.";
            }
        } catch (IOException e) {
            return e.toString();
        }
    }

    /**
     * Updates the details of a card by its name.
     *
     * @param cardName The name of the card to update.
     * @param card The updated card details.
     * @return A message indicating whether the card was successfully updated or not found.
     */
    @PatchMapping("/{cardName}")
    public String patchCard(@PathVariable String cardName, @RequestBody Card card) {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.info("Received PATCH request for card: {}", cardName);
        logger.info("Request body: {}", card);

        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            Card cardToUpdate = cardMap.get(cardName);
            if(cardToUpdate != null){
                cardMap.remove(cardName);
                cardMap.put(cardName, card);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, cardMap);
                return cardName + " has been update.";
            }
            else{
                return "The card isn't in the API.";
            }
        } catch (IOException e) {
            return e.toString();
        }
    }

    /**
     * Removes a card by its name.
     *
     * @param cardName The name of the card to remove.
     * @return A message indicating whether the card was successfully removed or not found.
     */
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

    /**
     * Searches for cards based on the given characteristics.
     *
     * @param ccm The converted mana cost (ccm) to search for (optional).
     * @param CCM The mana cost (CCM) to search for (optional).
     * @param color The color of the card to search for (optional).
     * @param keywords The keywords of the card to search for (optional).
     * @param type The type of the card to search for (optional).
     * @param text The text of the card to search for (optional).
     * @param legality The legality of the card to search for (optional).
     * @return A string containing the names of the matching cards, or a message indicating that no cards were found.
     */
    @GetMapping("/search")
    public String searchCards(
            @RequestParam(required = false) String CCM,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String legality) {
                
        ObjectMapper objectMapper = new ObjectMapper();
        logger.info("Searching for cards with the following characteristics: ccm={}, CCM={}, color={}, keywords={}, type={}, text={}, legality={}",
                    CCM, color, keywords, type, text, legality);

        try {
            Map<String, Card> cardMap = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Card>>() {});
            List<String> matchingCardNames = cardMap.entrySet().stream()
                    .filter(entry ->(CCM == null || entry.getValue().getCCM().equalsIgnoreCase(CCM)) &&
                                    (color == null || entry.getValue().getColor().equalsIgnoreCase(color)) &&
                                    (keywords == null || entry.getValue().getKeywords().contains(keywords)) &&
                                    (type == null || entry.getValue().getType().equalsIgnoreCase(type)) &&
                                    (text == null || entry.getValue().getText().contains(text)) &&
                                    (legality == null || entry.getValue().getLegality() != null && entry.getValue().getLegality().contains(legality)))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (!matchingCardNames.isEmpty()) {
                return "Matching cards: " + String.join(", ", matchingCardNames);
            } else {
                return "No cards found with the specified characteristics.";
            }
        } catch (IOException e) {
            return "An error occurred while searching for cards: " + e.getMessage();
        }
    }
}