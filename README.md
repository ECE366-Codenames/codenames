# codenames

## Project Description

Codenames is a popular word-based board game in which two teams compete to identify their team’s “secret agents” from a 5x5 grid of words. Each team has one “spymaster” who knows which words are their secret agents and, on their turn, gives their teammates, or “field operators,” a one-word clue that links secret agents and a number indicating how many secret agents they are hinting at. For example, the spymaster could give a clue such as “paper, 3” for the words “book,” “tree,” and “pen.” After the spymaster gives their clue, the spymaster’s teammate(s) guess from the grid of words which are the secret agents. If a team guesses the "assassin" card, they lose instantly. Whichever team is able to find all of their secret agents first without guessing the assassin card wins the game. 

Our project implements a website for playing a four-player version of the game. Upon entering the site, players can create their own accounts and log in using an email and password. Each player's display name is simply the prefix of their email (i.e. the segment before "@"). 

From the home page, players can start new games and join others using unique codes. Games run with exactly 4 players. Additionally, a “Rules" section provides players with the basic game rules.

Games are initialized with two teams of two players. Within each team, one player is chosen as the spymaster and the other player is the field operator. The spymaster is able to see the cards with colors, indicating the target words, “bystander” words, and the “assassin.” The field operator is only able to see the 5x5 grid of words.

The spymaster of the red team is first prompted to enter a clue and number, which is subsequently displayed to the field operator. The clue is checked to ensure that it is only one word, not greater than 20 characters, only contains letters, and is not any word displayed on the board. 

The field operator is then instructed to select their guess cards, which they do by using their cursor and clicking cards on the user interface. The cards the field operator selects will are “turned over.” If the card is their team’s color (i.e. a correct guess), the field operator is allowed to continue guessing or end their turn. If the card is not theirs (i.e. it is neutral or the other team’s), their turn ends automatically, and the other team’s turn begins. If the card is black, the game ends, and the team loses. Whichever team successfully identifies all of their cards first, without selecting the assassin card, wins.

Note that if any player leaves the game while in progress, the game is aborted. 

When the game ends, players may select the home button to return to the home page.


## Running the Backend

Start the database:
```
docker compose build
docker compose up
```
Run the React App in Browser:
```
cd frontend
npm run dev
```
Then ```o + enter``` to open in browser

## To deploy to Azure:
Run the shell script (container instance muse be running):
```
./deploy.sh
```
If it says authentication required:
```
az acr login --name codenames 
```

## OR: In Postman

To get information about a specific game:
```
GET: localhost:8080/game/{id}
```
or
```
GET: localhost:8080/game/{id}?spymaster={true/false}
```

To create a new game:
```
POST: localhost:8080/create
```

To guess a card in a specific game:
```
POST: localhost:8080/game/{id}/guess/{position}
```
