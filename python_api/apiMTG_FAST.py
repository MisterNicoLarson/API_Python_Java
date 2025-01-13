from fastapi import FastAPI, Request, HTTPException, Query
from fastapi.responses import JSONResponse
from typing import List, Dict, Optional
import json
from pydantic import BaseModel
import logging

# Setup logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Path to the JSON file
path_json_dict = "../mtgCards.json"

def load_cards(path: str) -> Dict:
    """
    Loads the card data from a JSON file.

    Args:
        path (str): The path to the JSON file containing card data.

    Returns:
        Dict: A dictionary containing the card data.

    Raises:
        FileNotFoundError: If the file is not found.
        JSONDecodeError: If the file content is not valid JSON.
    """
    try:
        with open(path, encoding='utf8') as file:
            return json.load(file)
    except FileNotFoundError:
        logging.error(f"File not found: {path}")
        raise
    except json.JSONDecodeError as e:
        logging.error(f"Invalid JSON in file {path}: {e}")
        raise


def save_cards(data: Dict) -> None:
    """
    Saves the card data to a JSON file.

    Args:
        data (dict): The dictionary containing the card data to be saved.

    Returns:
        None
    """
    with open(path_json_dict, "w", encoding='utf8') as outfile:
        json.dump(data, outfile, indent=4)


class CardDetails(BaseModel):
    """
    Represents the detailed attributes of an MTG card.

    Attributes:
        ccm (str): The converted mana cost of the card.
        CCM (str): The converted mana cost of the card (alternative representation).
        color (str): The color of the card.
        keyword (Optional[List[str]]): A list of keywords associated with the card.
        type (str): The type of the card.
        text (str): The text description of the card.
        legality (Optional[List[str]]): A list of legalities associated with the card.
    """
    ccm: str
    CCM: str
    color: str
    keyword: Optional[List[str]] = []
    type: str = ""
    text: str = ""
    legality: Optional[List[str]] = []

    def pretty_str(self) -> str:
        """Returns a human-readable string representation of the card details."""
        return (
            f"CCM: {self.CCM}, ccm: {self.ccm}, Color: {self.color}, "
            f"Keywords: {', '.join(self.keyword)}, Type: {self.type}, "
            f"Text: {self.text}, Legality: {', '.join(self.legality)}"
        )


class Card(BaseModel):
    """
    Represents an MTG card with a name and associated details.

    Attributes:
        name (str): The name of the card.
        details (CardDetails): The detailed attributes of the card.
    """
    name: str
    details: CardDetails

    def pretty_str(self) -> str:
        """Returns a human-readable string representation of the card."""
        return f"Name: {self.name}, Details: ({self.details.pretty_str()})"


app = FastAPI()


@app.get("/")
async def welcome_route(request: Request):
    """
    Handles the root route of the web application.

    This function is a route handler for the root URL ("/") of the web application. 
    It returns a simple welcome message.

    Args:
        request (Request): The request object.

    Returns:
        str: A welcome message including the client's IP address and user agent.
    """
    client_ip = request.client.host
    user_agent = request.headers.get('User-Agent', 'Unknown')
    return f"Welcome {user_agent}, your IP address is {client_ip}"


@app.get("/cards")
async def find_single_card() -> JSONResponse:
    """
    Handles GET requests to retrieve all cards.

    Returns:
        JSONResponse: A JSON response containing the list of cards.
    """
    dataCards = load_cards(path_json_dict)
    if not dataCards:
        return JSONResponse(content={"message": "No cards found"}, status_code=404)

    response_content = [
        {"name": card_name, "details": details}
        for card_name, details in dataCards.items()
    ]
    return JSONResponse(content={"cards": response_content}, status_code=200)


@app.post("/cards")
async def create_new_card(card: Card) -> JSONResponse:
    """
    Handles POST requests to create a new card.

    Args:
        card (Card): The card object to be added.

    Returns:
        JSONResponse: A response indicating success or failure.
    """
    dataCards = load_cards(path_json_dict)
    cardName = card.name

    if cardName in dataCards:
        return JSONResponse(
            content={"message": f"Card '{cardName}' already exists."},
            status_code=400
        )

    dataCards[cardName] = card.details.dict()
    save_cards(dataCards)

    return JSONResponse(
        content={"message": f"Card '{cardName}' successfully created."},
        status_code=201
    )


@app.get("/cards/{cardName}")
async def get_card(cardName: str):
    """
    Retrieves the details of a card by its name.

    Args:
        cardName (str): The name of the card to retrieve.

    Returns:
        dict: The card details.
    """
    dataCards = load_cards(path_json_dict)
    card = dataCards.get(cardName)
    if not card:
        raise HTTPException(status_code=404, detail=f"Card '{cardName}' not found.")
    
    return card


@app.put("/cards/{cardName}")
@app.patch("/cards/{cardName}")
async def update_card(cardName: str, cardDetail: CardDetails):
    """
    Updates the details of a card by its name.

    Args:
        cardName (str): The name of the card to update.
        cardDetail (CardDetails): The new details of the card.

    Returns:
        dict: A confirmation message for the update.
    """
    dataCards = load_cards(path_json_dict)
    dataCards[cardName] = cardDetail.dict()
    save_cards(dataCards)

    return {"message": f"'{cardName}' has been modified."}


@app.delete("/cards")
async def delete_route_cards(card: Card) -> JSONResponse:
    """
    Deletes a card from the collection by its name and updates the JSON file.

    Args:
        card (Card): The card object containing the name of the card to delete.

    Returns:
        JSONResponse: A response indicating success or failure.
    """
    dataCards = load_cards(path_json_dict)
    card_name = card.name

    if card_name not in dataCards:
        raise HTTPException(status_code=404, detail="Card not found")

    del dataCards[card_name]
    save_cards(dataCards)

    return JSONResponse(content={"message": f"Card '{card_name}' successfully deleted."}, status_code=200)


@app.get("/cards/search")
async def handle_advanced_search(
    name: Optional[str] = Query("", min_length=0),
    type: Optional[str] = Query("", min_length=0),
    color: Optional[str] = Query("", min_length=0),
    keyword: Optional[str] = Query("", min_length=0)
):
    """
    Handles advanced search requests for cards.

    This function allows for searching cards based on various criteria such as name, type, color,
    and keywords. It returns a list of matching cards.

    Query Parameters:
        name (str): The name to search for in card names.
        type (str): The type to filter cards by.
        color (str): The color to filter cards by.
        keyword (str): The keyword to search for in the card's keywords.

    Returns:
        dict: A JSON response containing the matching cards or an error message.
    """
    search_name = name.lower() if name else ""
    search_type = type.lower() if type else ""
    search_color = color.lower() if color else ""
    search_keyword = keyword.lower() if keyword else ""

    matching_cards = {}
    dataCards = load_cards(path_json_dict)

    for card_name, card in dataCards.items():
        if search_name and search_name not in card_name.lower():
            continue
        if search_type and search_type != card.type.lower():
            continue
        if search_color and search_color != card.color.lower():
            continue
        if search_keyword and search_keyword not in [kw.lower() for kw in card.keyword]:
            continue

        matching_cards[card_name] = card

    if not matching_cards:
        raise HTTPException(status_code=404, detail="No cards found matching the search criteria.")

    return matching_cards
