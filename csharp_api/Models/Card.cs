namespace csharp_api.Models;

using System.Collections.Generic;
using Newtonsoft.Json;

/// <summary>
/// Represents a playing card with various attributes such as mana cost, color, type, and legality.
/// </summary>
public class Card
{
    /// <summary>
    /// The name of the card.
    /// </summary>
    [JsonProperty("name")]
    public string Name { get; set; } = string.Empty;

    /// <summary>
    /// The details of the card.
    /// </summary>
    [JsonProperty("details")]
    public CardDetails Details { get; set; } = new CardDetails();

    /// <summary>
    /// Returns a string representation of the card.
    /// </summary>
    /// <returns>
    /// A formatted string containing the card's name and its details.
    /// </returns>
    public override string ToString()
    {
        return $"Card Name: {Name}\n" +
               $"{Details.ToString()}";
    }
}

/// <summary>
/// Contains detailed attributes of a card.
/// </summary>
public class CardDetails
{
    /// <summary>
    /// The converted mana cost (CCM) of the card.
    /// </summary>
    [JsonProperty("ccm")]
    public string CCM { get; set; } = string.Empty;

    /// <summary>
    /// The color of the card (e.g., "Red", "Blue", "Green").
    /// </summary>
    [JsonProperty("color")]
    public string Color { get; set; } = string.Empty;

    /// <summary>
    /// A list of keywords associated with the card (e.g., "Flying", "Haste").
    /// </summary>
    [JsonProperty("keywords")]
    public List<string> Keywords { get; set; } = new List<string>();

    /// <summary>
    /// The type of the card (e.g., "Creature", "Sorcery").
    /// </summary>
    [JsonProperty("type")]
    public string Type { get; set; } = string.Empty;

    /// <summary>
    /// The descriptive text or effect of the card.
    /// </summary>
    [JsonProperty("text")]
    public string Text { get; set; } = string.Empty;

    /// <summary>
    /// A list of formats in which the card is legally playable (e.g., "Standard", "Modern").
    /// </summary>
    [JsonProperty("legality")]
    public List<string>? Legality { get; set; } = new List<string>();

    /// <summary>
    /// Returns a string representation of the card details.
    /// </summary>
    /// <returns>
    /// A formatted string containing the card's information, including the converted mana cost (CCM), 
    /// color, type, description text, keywords, and the formats in which the card is legal to play.
    /// </returns>
    /// <remarks>
    /// This method provides an easy-to-read representation of the CardDetails object, displaying 
    /// key information in a human-readable format. Keywords and legality formats are separated by commas, 
    /// and null values are handled to avoid formatting issues.
    /// </remarks>
    public override string ToString()
    {
        return $"Card Details:\n" +
            $"- CCM: {CCM}\n" +
            $"- Color: {Color}\n" +
            $"- Type: {Type}\n" +
            $"- Text: {Text}\n" +
            $"- Keywords: {string.Join(", ", Keywords)}\n" +
            $"- Legality: {string.Join(", ", Legality ?? new List<string>())}";
    }

}
