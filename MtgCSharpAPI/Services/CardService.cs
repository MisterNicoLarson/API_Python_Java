using System.Text.Json;
using MTGCSharpAPI.Models;

namespace MTGCSharpAPI.Services;

public class CardService
{
    private readonly string _filePath;

    public CardService(string filePath)
    {
        _filePath = filePath;
    }

    public Dictionary<string, Card>? LoadCards()
    {
        if (!File.Exists(_filePath))
        {
            Console.WriteLine("Fichier introuvable !");
            return null;
        }

        string json = File.ReadAllText(_filePath);
        return JsonSerializer.Deserialize<Dictionary<string, Card>>(json);
    }
}
