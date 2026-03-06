# codenames

## Running the Backend

Start the database:
```
docker compose build
docker compose up
```

## In Postman:

To get information about a specific game:
```
GET: localhost:8080/game/{id}
```

To create a new game:
```
POST: localhost:8080/create
```

To guess a card in a specific game:
```
POST: localhost:8080/game/{id}/guess/{position}
```
