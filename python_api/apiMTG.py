from flask import Flask, request, jsonify
import json

app = Flask(__name__)

path_json_dict = "mtgCards.json"

def load_cards():
    """
    Loads cards from a JSON file.

    This function attempts to read and load the content of a JSON file located at the path specified by
    `path_json_dict`. If the file is not found or if the content is not valid JSON, the function returns
    an empty dictionary.

    Returns:
        dict: A dictionary containing the card data loaded from the JSON file, or an empty dictionary in case of an error.
    """
    try:
        with open(path_json_dict, encoding='utf8') as file:
            return json.load(file)
    except (FileNotFoundError, json.JSONDecodeError):
        return {}

def save_cards(data):
    """
    Saves the card data to a JSON file.

    This function writes the provided data to a JSON file located at the path specified by `path_json_dict`.
    The data is serialized to JSON format with an indentation of 4 spaces for readability.

    Args:
        data (dict): The dictionary containing the card data to be saved.

    Returns:
        None
    """
    with open(path_json_dict, "w", encoding='utf8') as outfile:
        json.dump(data, outfile, indent=4)

dataCards = load_cards()

@app.route("/")
def welcome_route():
    """
    Handles the root route of the web application.

    This function is a route handler for the root URL ("/") of the web application. It returns a simple welcome message.

    Returns:
        str: A welcome message.
    """
    client_ip = request.remote_addr
    user_agent = request.headers.get('User-Agent', 'Unknown')
    return f"Welcome {user_agent} this your ip {client_ip}"
    
API_KEYS = {
    "user1": "key123",
    "user2": "key456",
}

@app.before_request
def authenticate_request():
    """
    Authenticates the request using an API key.

    This function checks the 'x-api-key' header in the request and verifies if it is valid.
    If the API key is not valid, it returns a 401 Unauthorized response.

    Returns:
        tuple: A JSON response with an error message and a 401 status code if the API key is invalid.
    """
    api_key = request.headers.get('x-api-key')
    if api_key not in API_KEYS.values():
        return jsonify({"error": "Unauthorized"}), 401

@app.route("/cards/<string:cardName>", methods=["GET", "PUT", "PATCH"])
def handle_card(cardName):
    """
    Handles requests for a specific card.

    This function handles GET, PUT, and PATCH requests for a card identified by `cardName`.
    - For GET requests, it returns the details of the card if it exists.
    - For PUT and PATCH requests, it updates the card details with the provided JSON data.

    Args:
        cardName (str): The name of the card to handle.

    Returns:
        tuple: A response message and an HTTP status code.
    """
    if request.method == "GET":
        card = dataCards.get(cardName)
        if not card:
            return f"Card '{cardName}' not found.", 404
        
        response = f"{cardName}\n"
        for prop, value in card.items():
            response += f"{prop} : {value}\n"

        return response, 200

    elif request.method in ["PUT", "PATCH"]:
        cardDetail = request.get_json()
        if not cardDetail:
            return "Invalid input", 400
        dataCards[cardName] = cardDetail
        save_cards(dataCards)
        return f"'{cardName}' has been modified.", 200

@app.route("/cards", methods=["GET", "POST", "DELETE"])
def handle_cards():
    """
    Handles requests for managing cards.

    This function handles GET, POST, and DELETE requests for managing the collection of cards.
    - For GET requests, it returns a list of all cards with their properties.
    - For POST requests, it adds a new card with the provided JSON data.
    - For DELETE requests, it removes a specified card.

    Returns:
        tuple: A JSON response and an HTTP status code.
    """
    if request.method == "GET":
        pretty_cards = []
        for cardName, properties in dataCards.items():
            card_info = {"cardName": cardName, "properties": properties}
            pretty_cards.append(card_info)
        return jsonify(pretty_cards), 200
    elif request.method == "POST":
        new_card = request.get_json()
        cardName, cardDetail = next(iter(new_card.items()))
        if cardName in dataCards:
            return jsonify({"error": "Card already exists"}), 400
        dataCards[cardName] = cardDetail
        save_cards(dataCards)
        return jsonify({"message": f"{cardName} has been added."}), 201
    elif request.method == "DELETE":
        delete_request = request.get_json()
        cardName = next(iter(delete_request.keys()))
        if cardName not in dataCards:
            return jsonify({"error": "Card not found"}), 404
        del dataCards[cardName]
        save_cards(dataCards)
        return jsonify({"message": f"{cardName} has been removed."}), 204
    
@app.route("/cards/search", methods=["GET"])
def handle_advanced_search():
    """
    Handles advanced search requests for cards.

    This function processes GET requests to search for cards based on various criteria such as name, type, color,
    and keywords. It returns a list of matching cards.

    Query Parameters:
        name (str): The name to search for in the card names.
        type (str): The type to filter cards by.
        color (str): The color to filter cards by.
        keyword (str): The keyword to search for in the card's keywords.

    Returns:
        tuple: A JSON response containing the matching cards and an HTTP status code.
    """
    search_name = request.args.get("name", "").lower()
    search_type = request.args.get("type", "").lower()
    search_color = request.args.get("color", "").lower()
    search_keyword = request.args.get("keyword", "").lower()

    matching_cards = {}

    for card_name, card in dataCards.items():
        if search_name and search_name not in card_name.lower():
            continue
        if search_type and search_type != card.get("type", "").lower():
            continue
        if search_color and search_color != card.get("color", "").lower():
            continue
        if search_keyword and search_keyword not in [kw.lower() for kw in card.get("keywords", [])]:
            continue

        matching_cards[card_name] = card

    if not matching_cards:
        return jsonify({"message": "No cards found matching the search criteria."}), 404

    return jsonify(matching_cards), 200

@app.before_request
def log_request_info():
    """
    Logs detailed information about each incoming request.

    This function is executed before each request and logs various details about the request, including the client's IP address,
    User-Agent string, URL, HTTP method, referrer, and accepted languages.

    Returns:
        None
    """
    client_ip = request.remote_addr
    user_agent = request.headers.get('User-Agent', 'Unknown')
    url = request.url
    method = request.method
    referrer = request.referrer or "No Referrer"
    languages = request.accept_languages.to_header()
    
    print(f"Request Info:")
    print(f"  Client IP: {client_ip}")
    print(f"  User-Agent: {user_agent}")
    print(f"  URL: {url}")
    print(f"  Method: {method}")
    print(f"  Referrer: {referrer}")
    print(f"  Languages: {languages}")

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)