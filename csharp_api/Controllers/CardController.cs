using Microsoft.AspNetCore.Mvc;
using System.IO;
using csharp_api.Models;

[ApiController]
[Route("api/cards")]
public class CardController : ControllerBase
{
    private readonly string _filePath = "mtgCardCopy.json";
    private readonly CardCollection _cardCollection;

    public CardController()
    {
        _cardCollection = new CardCollection();
    }

    /// <summary>
    /// Gets the list of cards from the JSON file.
    /// </summary>
    [HttpGet]
    public ActionResult<CardCollection> GetCardsList()
    {
        var fullFilePath = Path.Combine(Directory.GetParent(Directory.GetCurrentDirectory()).FullName, _filePath);

        if (!System.IO.File.Exists(fullFilePath))
        {
            return NotFound("File not found.");
        }

        var cardCollection = CardCollection.ReadJsonFile(fullFilePath);
        return Ok(cardCollection);
    }
}
