using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/hello")]
public class HelloController : ControllerBase
{
    [HttpGet("")]
    public string GetHello()
    {
        return "Hello, World!";
    }
}
