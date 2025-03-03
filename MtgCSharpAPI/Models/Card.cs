namespace MTGCSharpAPI.Models;

public class Card
{
    public string Name { get; set; } = string.Empty;
    public string ccm { get; set; } = string.Empty;
    public string CCM { get; set; } = string.Empty;
    public string Color { get; set; } = string.Empty;
    public List<string> Keywords { get; set; } = new();
    public string Type { get; set; } = string.Empty;
    public string Text { get; set; } = string.Empty;
    public string? Legality { get; set; } 
}
