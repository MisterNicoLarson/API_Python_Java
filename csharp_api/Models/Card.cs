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
}
