namespace csharp_api.Models; 

using System.Collections.Generic;
using Newtonsoft.Json;

/// <summary>
/// Represents a playing card with various attributes such as mana cost, color, type, and legality.
/// </summary>
public class Card
{
    /// <summary>
    /// The converted mana cost (CCM) of the card.
    /// </summary>
    [JsonProperty("ccm")]
    public string CCM { get; set; }

    /// <summary>
    /// The color of the card (e.g., "Red", "Blue", "Green").
    /// </summary>
    [JsonProperty("color")]
    public string color { get; set; }

    /// <summary>
    /// A list of keywords associated with the card (e.g., "Flying", "Haste").
    /// </summary>
    [JsonProperty("keywords")]
    public List<string> keywords { get; set; } = new List<string>();

    /// <summary>
    /// The type of the card (e.g., "Creature", "Sorcery").
    /// </summary>
    [JsonProperty("type")]
    public string type { get; set; }

    /// <summary>
    /// The descriptive text or effect of the card.
    /// </summary>
    [JsonProperty("text")]
    public string text { get; set; }

    /// <summary>
    /// A list of formats in which the card is legally playable (e.g., "Standard", "Modern").
    /// </summary>
    [JsonProperty("legality")]
    public List<string>? legality { get; set; } = new List<string>();
}
