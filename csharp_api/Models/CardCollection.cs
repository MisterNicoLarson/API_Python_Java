namespace csharp_api.Models; 

using System;
using System.Collections.Generic;
using System.IO;
using System.Text.Json;

/// <summary>
/// Represents a collection of cards stored in a dictionary.
/// </summary>
public class CardCollection
{
    /// <summary>
    /// A dictionary containing cards, where the key is the card name.
    /// </summary>
    public Dictionary<string, Card> Cards { get; set; } = new Dictionary<string, Card>();

    /// <summary>
    /// Reads a JSON file and deserializes it into a dictionary of cards.
    /// </summary>
    /// <param name="filePath">The path to the JSON file.</param>
    /// <returns>A dictionary with card names as keys and Card objects as values.</returns>
    public static Dictionary<string, Card> ReadJsonFile(string filePath)
    {
        if (!File.Exists(filePath))
        {
            throw new FileNotFoundException("The specified file was not found.", filePath);
        }

        string text = File.ReadAllText(filePath);
        return JsonSerializer.Deserialize<Dictionary<string, Card>>(text);
    }
}
