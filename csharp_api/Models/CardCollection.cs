using System;
using System.Collections.Generic;
using System.IO;
using System.Text.Json;
using Newtonsoft.Json;

namespace csharp_api.Models
{
    /// <summary>
    /// Represents a collection of cards stored in a dictionary.
    /// </summary>
    public class CardCollection
    {
        /// <summary>
        /// A dictionary containing cards, where the key is the card name.
        /// </summary>
        public Dictionary<string, Card> Cards { get; private set; } = new Dictionary<string, Card>();

        /// <summary>
        /// Default constructor initializing an empty collection.
        /// </summary>
        public CardCollection() { }

        /// <summary>
        /// Constructor that initializes the collection from a JSON file.
        /// </summary>
        /// <param name="filePath">The path to the JSON file.</param>
        public CardCollection(string filePath)
        {
            Cards = ReadJsonFile(filePath);
        }

        /// <summary>
        /// Reads a JSON file and deserializes it into a dictionary of cards.
        /// </summary>
        /// <param name="filePath">The path to the JSON file.</param>
        /// <returns>A dictionary with card names as keys and Card objects as values.</returns>
        public static Dictionary<string, Card> ReadJsonFile(string filePath)
        {
            if (!File.Exists(filePath))
            {
                return new Dictionary<string, Card>(); // Return empty collection if file does not exist
            }

            string text = File.ReadAllText(filePath);
            return JsonConvert.DeserializeObject<Dictionary<string, Card>>(text) ?? new Dictionary<string, Card>();
        }

        /// <summary>
        /// Saves the current card collection to a JSON file.
        /// </summary>
        /// <param name="filePath">The path to the JSON file where the collection should be saved.</param>
        public void SaveToFile(string filePath)
        {
            string json = JsonConvert.SerializeObject(Cards, Formatting.Indented);
            File.WriteAllText(filePath, json);
        }

        /// <summary>
        /// Removes a card from the collection.
        /// </summary>
        /// <param name="cardName">The name of the card to remove.</param>
        /// <returns>Returns true if the card was removed, false if the card was not found.</returns>
        public bool RemoveCard(string cardName)
        {
            if (Cards.ContainsKey(cardName))
            {
                Cards.Remove(cardName);
                return true;
            }
            return false;
        }
    }
}
