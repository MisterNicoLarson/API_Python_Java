package com.example.javapimtg.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Card class represents a card with various properties such as CCM, color, keywords, type, text, and legality.
 *
 * <p>This class is used to map JSON data to a Java object using Jackson annotations.</p>
 *
 * <p>The class includes the following properties:</p>
 * <ul>
 *     <li>CCM: The converted mana cost of the card.</li>
 *     <li>color: The color of the card.</li>
 *     <li>keywords: A list of keywords associated with the card.</li>
 *     <li>type: The type of the card (e.g., Instant, Creature).</li>
 *     <li>text: The text description of the card.</li>
 *     <li>legality: A list of formats in which the card is legal.</li>
 * </ul>
 */
public class Card {
    @JsonProperty("CCM")
    private String CCM;

    @JsonProperty("color")
    private String color;

    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;

    @JsonProperty("legality")
    private List<String> legality;

    // Getters and Setters
    public String getCCM() {
        return CCM;
    }

    public void setCCM(String CCM) {
        this.CCM = CCM;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getLegality() {
        return legality;
    }

    public void setLegality(List<String> legality) {
        this.legality = legality;
    }

    /**
     * Returns a string representation of the Card object.
     *
     * <p>The string representation includes all non-null and non-empty properties of the card.</p>
     *
     * @return A string representation of the Card object.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Card{");

        if (CCM != null) {
            builder.append("CCM='").append(CCM).append("', ");
        }
        if (color != null) {
            builder.append("color='").append(color).append("', ");
        }
        if (keywords != null && !keywords.isEmpty()) {
            builder.append("keywords=").append(keywords).append(", ");
        }
        if (type != null) {
            builder.append("type='").append(type).append("', ");
        }
        if (text != null) {
            builder.append("text='").append(text).append("', ");
        }
        if (legality != null && !legality.isEmpty()) {
            builder.append("legality=").append(legality).append(", ");
        }

        if (builder.lastIndexOf(", ") == builder.length() - 2) {
            builder.delete(builder.length() - 2, builder.length());
        }

        builder.append("}");
        return builder.toString();
    }
}
