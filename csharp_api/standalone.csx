#r "bin/Debug/net8.0/csharp_api.dll" 

using System;
using System.IO;
using System.Text.Json;
using System.Collections.Generic;
using csharp_api.Models;

string pathFile = "../mtgCardCopy.json";

/// <summary>
/// Function which says hello
/// </summary>
/// <param name="name">The name of the person we should greet</param>
void SayHello(string name)
{
    Console.WriteLine($"Hello, {name}!");
}

/// <summary>
/// Load a json file
/// </summary>
/// <param name="path">Path to the json file</param>
/// <returns></returns>
Dictionary<string, Card> LoadCardFromJSONFile(string path)
{
    string text = File.ReadAllText(path);
    return JsonSerializer.Deserialize<Dictionary<string, Card>>(text);
}

SayHello("Developer");
Dictionary<string, Card> listOfCard = LoadCardFromJSONFile(pathFile);

Card test = listOfCard["Duress"];
Console.WriteLine(test.ToString());