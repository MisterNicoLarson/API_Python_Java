using Microsoft.AspNetCore.Mvc;

/// <summary>
/// A simple controller to return "Hello, World!" responses.
/// </summary>
[ApiController]
[Route("api/[controller]")]
public class HelloWorldController : ControllerBase
{
    /// <summary>
    /// Returns a simple "Hello, World!" message.
    /// </summary>
    /// <returns>A string message.</returns>
    [HttpGet]
    public ActionResult<string> Get()
    {
        return "Hello, World!";
    }

    /// <summary>
    /// Returns a personalized greeting message.
    /// </summary>
    /// <param name="name">The name of the person to greet.</param>
    /// <returns>A personalized greeting string.</returns>
    [HttpGet("{name}")]
    public ActionResult<string> GetWithName(string name)
    {
        return $"Hello, {name}!";
    }
}
