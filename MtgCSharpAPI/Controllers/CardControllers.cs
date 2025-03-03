using Microsoft.AspNetCore.Mvc;
using MTGCSharpAPI.Models;
using MTGCSharpAPI.Services;

[ApiController]
[Route("api/cards")]
public class CardController : ControllerBase
{
    private readonly CardService _cardService;

    public CardController(CardService cardService)
    {
        _cardService = cardService;
    }

    [HttpGet]
    public ActionResult<Dictionary<string, Card>> GetCards()
    {
        var cards = _cardService.LoadCards();
        if (cards == null)
        {
            return NotFound("Fichier JSON introuvable ou erreur de lecture.");
        }
        return Ok(cards);
    }
}
