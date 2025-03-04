using Microsoft.AspNetCore.Mvc;
using System.IO;
using csharp_api.Models;

[ApiController]
[Route("api/cards")]
public class CardController : ControllerBase
{
    private readonly string _filePath;
    private readonly CardCollection _cardCollection;

    /// <summary>
    /// Initializes the CardController, loading the card collection from a JSON file if it exists.
    /// </summary>
    public CardController()
    {
        _filePath = Path.Combine(Directory.GetParent(Directory.GetCurrentDirectory())?.FullName ?? "", "mtgCards.json");

        if (System.IO.File.Exists(_filePath))
        {
            _cardCollection = new CardCollection(_filePath);
        }
        else
        {
            // Initializes an empty collection if the file does not exist
            _cardCollection = new CardCollection();
        }
    }

    /// <summary>
    /// Retrieves the full list of cards in the collection.
    /// </summary>
    /// <returns>
    /// Returns a 200 OK response with the card collection if it contains cards.
    /// Returns a 404 Not Found response if the collection is empty.
    /// </returns>
    [HttpGet]
    public ActionResult<CardCollection> GetCardsList()
    {
        if (_cardCollection.Cards.Count == 0)
        {
            return NotFound("No cards found.");
        }

        return Ok(_cardCollection);
    }

    /// <summary>
    /// Retrieves a specific card by its name.
    /// </summary>
    /// <param name="cardName">The name of the card to retrieve.</param>
    /// <returns>
    /// Returns a 200 OK response with the card details if found.
    /// Returns a 404 Not Found response if the card does not exist.
    /// </returns>
    [HttpGet("{cardName}")]
    public ActionResult<Card> GetCardByName(string cardName)
    {
        if (_cardCollection.Cards.TryGetValue(cardName, out var card))
        {
            return Ok(card);
        }

        return NotFound($"Card '{cardName}' not found.");
    }

    /// <summary>
    /// Adds a new card to the collection.
    /// </summary>
    /// <param name="newCard">The card object containing all details.</param>
    /// <returns>
    /// Returns a 201 Created response with the added card if successful.
    /// Returns a 400 Bad Request response if the card data is invalid.
    /// Returns a 409 Conflict response if a card with the same name already exists.
    /// </returns>
    [HttpPost]
    public ActionResult<Card> PostNewCard([FromBody] Card newCard)
    {
        if (newCard == null || newCard.Details == null)
        {
            return BadRequest("Invalid card data.");
        }

        if (string.IsNullOrWhiteSpace(newCard.Name))
        {
            return BadRequest("Card name is required.");
        }

        if (_cardCollection.Cards.ContainsKey(newCard.Name))
        {
            return Conflict($"A card with the name '{newCard.Name}' already exists.");
        }

        // Adds the card to the collection
        _cardCollection.Cards[newCard.Name] = newCard;
        _cardCollection.SaveToFile(_filePath);

        return CreatedAtAction(nameof(GetCardByName), new { cardName = newCard.Name }, newCard);
    }

    /// <summary>
    /// Deletes a card from the collection by its name.
    /// </summary>
    /// <param name="cardName">The name of the card to delete.</param>
    /// <returns>
    /// Returns a 204 No Content response if the deletion is successful.
    /// Returns a 404 Not Found response if the card does not exist.
    /// </returns>
    [HttpDelete("{cardName}")]
    public ActionResult DeleteCard(string cardName)
    {
        if (!_cardCollection.Cards.ContainsKey(cardName))
        {
            return NotFound($"Card '{cardName}' not found.");
        }

        _cardCollection.Cards.Remove(cardName);
        _cardCollection.SaveToFile(_filePath);

        return NoContent();
    }
}
