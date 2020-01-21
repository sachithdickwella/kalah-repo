# Kalah Game - Code Challenge

This game service designed as per the details and rules available in [Wikipedia](https://en.wikipedia.org/wiki/Kalah) 
and the PDF file provided along with the challenge. This is six (6) stone Kalah game and each player starts with six (6) 
seeds in each of the player's pit/house, and the store/kalah is empty at the beginning of the game. Two players could 
play one instance of the game from any client whichever have the access to the server, and the player should have the 
game id to access the game instance.

The application design consist of two components.

1. Service application which developed using Spring boot.
2. Redis persistent module that administer stateful capability of the service endpoints.

### Source code and environment prerequisites:

This application is developed and compiled using Java 11.0.5 LTS version and for successful compilation of the program 
would require Java 11 or later version. Although, haven't use any experimental feature newly introduced with Java 11.

Docker daemon should be up and running and have installed `docker-compose` deploy the `docker-compose.yml` file. Here is 
[how to install docker-compose](https://docs.docker.com/compose/install/) on you system.

### Prepare to deploy:

Spring application hasn't implemented with any plugin to provision docker images. Although the `docker-compose.yml` file 
in the project path would give single kick start of the entire environment including the creation of application's docker 
image under the tag of `backbase/kalah:latest` using the `Dockerfile` in the classpath.

In order to deploy the application environment follow below steps;

- **Build the project source using the maven command;**
```shell script
mvn -T 1.5C clean install
```
or 
```shell script
mvn -T 1.5C clean package
```
Although, there is a huge different running each of these commands, in this case it wouldn't matter as long as the 
`docker-compose.yml` file refers the `.jar` file in `${project.basedir}/target` directory.

- **Deploy the `docker-compose.yml` file using `docker-compose` command;**
```docker
docker-compose up -d
```
or, if you have made any change to the application's `Dockerfile` in the class path and need to build it again with the 
changes you made;
```docker
docker-compose up -d --build
```

### Using the services:

There are two endpoints available with the application in order to fulfill the requirement;

- **Create a game instance by making a request to following endpoint using `curl` CLI tool;**

```shell script
curl --header "Content-Type: application/json" --request POST http://<hostname>:<port>/games
```
and, a successful attempt will give you this response with **HTTP 201 Created** code;

```json
{
    "id": 8840384032536030098,
    "url": "http://localhost:8080/games/8840384032536030098"
}
```
This response contains the ID that newly created for the game instance, and the URL that the player can access the game 
instance.

- **Play the game using the URL provided by previous service call, like this;**

```shell script
curl --header "Content-Type: application/json" --request PUT http://<host>:<port>/games/{gameId}/pits/{pitId}
```

this would response back with the following JSON response along with **HTTP 200 Ok** code;

```json
{
    "id": 8840384032536030098,
    "url": "http://<hostname>:<port>/games/8840384032536030098",
    "status": {
        "1": "6",
        "2": "6",
        "3": "6",
        "4": "6",
        "5": "6",
        "6": "6",
        "7": "0",
        "8": "6",
        "9": "6",
        "10": "6",
        "11": "6",
        "12": "6",
        "13": "6",
        "14": "0"
    }
}
```
As a matter of fact, player wouldn't able to see exactly the same response in `status` attribute as the first move have 
been made to get this response from the service. That depends on the `{pitId}`, the first player has chosen to start 
with.

In addition to that following error responses would get from the service if any of the player make an illegal or invalid 
move with **HTTP 406 Not Acceptable** code and distinguished message;

- When it's not the player's turn to make a move;
```json
{
    "timestamp": "2020-01-12T15:44:41.165685",
    "status": 406,
    "error": "Not Acceptable",
    "message": "It's the PLAYER_1's turn",
    "payload": {
        "id": 8840384032536030098,
        "url": "http://localhost:8080/games/8840384032536030098",
        "status": {
            "1": "0",
            "2": "7",
            "3": "7",
            "4": "7",
            "5": "7",
            "6": "7",
            "7": "1",
            "8": "6",
            "9": "6",
            "10": "6",
            "11": "6",
            "12": "6",
            "13": "6",
            "14": "0"
        }
    }
}
```

- When an invalid `pitId` is provided to make a move;
```json
{
    "timestamp": "2020-01-12T15:46:39.573101",
    "status": 406,
    "error": "Not Acceptable",
    "message": "Pit id 20 is invalid",
    "payload": {
        "id": 8840384032536030098,
        "url": "http://localhost:8080/games/8840384032536030098",
        "status": {
            "1": "0",
            "2": "7",
            "3": "7",
            "4": "7",
            "5": "7",
            "6": "7",
            "7": "1",
            "8": "6",
            "9": "6",
            "10": "6",
            "11": "6",
            "12": "6",
            "13": "6",
            "14": "0"
        }
    }
}
```

- When the player tries to move seeds/stones from his/her store/kalah;
```json
{
    "timestamp": "2020-01-12T15:48:44.468593",
    "status": 406,
    "error": "Not Acceptable",
    "message": "Cannot grab seeds from 7 store",
    "payload": {
        "id": 8840384032536030098,
        "url": "http://localhost:8080/games/8840384032536030098",
        "status": {
            "1": "0",
            "2": "7",
            "3": "7",
            "4": "7",
            "5": "7",
            "6": "7",
            "7": "1",
            "8": "6",
            "9": "6",
            "10": "6",
            "11": "6",
            "12": "6",
            "13": "6",
            "14": "0"
        }
    }
}
```

- and, when the player tries pickup seeds/stones from an empty pit;
```json
{
    "timestamp": "2020-01-12T15:50:06.506078",
    "status": 406,
    "error": "Not Acceptable",
    "message": "Chosen pit 1, is empty, PLAYER_1's got another chance",
    "payload": {
        "id": 8840384032536030098,
        "url": "http://localhost:8080/games/8840384032536030098",
        "status": {
            "1": "0",
            "2": "7",
            "3": "7",
            "4": "7",
            "5": "7",
            "6": "7",
            "7": "1",
            "8": "6",
            "9": "6",
            "10": "6",
            "11": "6",
            "12": "6",
            "13": "6",
            "14": "0"
        }
    }
}
```
These `error` responses suppose to ease the players' experience during the troubleshooting any user mistake.

As this document mentioned earlier, all the game statuses will be saved in a Redis store since the game instance created 
and each player's movement also be recorded with Redis incrementally and after seven (7) days that instance would be expired 
and removed despite the game is completed or not.

### Unit and integration tests:

Service endpoints and aforementioned scenarios are covered with the unit tests.