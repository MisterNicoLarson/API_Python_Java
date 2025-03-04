using Microsoft.AspNetCore.Mvc;
using System.IO;
using csharp_api.Models;

[ApiController]
[Route("api/cards")]
public class CardController : ControllerBase
{
    private readonly string _filePath;
    private readonly CardCollection _cardCollection;

    public CardController()
    {
        _filePath = Path.Combine(Directory.GetParent(Directory.GetCurrentDirectory())?.FullName ?? "", "mtgCards.json");

        if (System.IO.File.Exists(_filePath))
        {
            _cardCollection = new CardCollection(_filePath);
        }
        else
        {
            _cardCollection = new CardCollection(); // Collection vide si le fichier n'existe pas
        }
    }

    /// <summary>
    /// Gets the list of all cards from the JSON file.
    /// </summary>
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
    /// Gets a specific card by name.
    /// </summary>
    /// <param name="cardName">The name of the card.</param>
    [HttpGet("{cardName}")]
    public ActionResult<Card> GetCardByName(string cardName)
    {
        if (_cardCollection.Cards.TryGetValue(cardName, out var card))
        {
            return Ok(card);
        }

        return NotFound($"Card '{cardName}' not found.");
    }
}
