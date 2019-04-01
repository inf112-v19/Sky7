# Sky7

## How to run
- Clone git repo
- Import to an IDE as existing maven project
- Run Main.java (src/main/java/sky7/main)

## Current functionality
- One player can play endless rounds by selecting cards from hand and playing them by clicking "go".
- Walls block the path of robots.
- Player can push another robot(placed there for demonstration, not controlled).
- Cogwheels rotate robots.
- Player is dealt new cards each round.

## Documentation
- Class diagram in Documents folder

## Known bugs
- Trying to load a board which is not quadratic crashes the game.
	- It is likely a mix-up of x and y somewhere in Board.

## Tests
### JUNIT tests
- Located in [test folder](src/test/java/sky7)
- Run [TestSuite](src/test/java/sky7/TestSuite.java) as JUnit test - combines all tests in a Suite.
- The TestSuite takes ~40 sec as of 01.04.19 due to simulation of rounds.
- ICell
    - Test that the priority of each ICell is correct
- BoardGenerator
    - Tests that BoardGenerator can read a random Json file,
    and that the height and width is correct.
- BoardTests
    - Tests that the you can retrieve information correctly from Board. 
    - Tests that the movement of the robots on the board is correct follow the rules.
- FlagTest
    - Basic check for flag values
- RoboTest
    - Checks that RoboTile rotates correctly
- ProgramDeck
    - Test that the values of ProgramDeck matches the number of different
     cards as shown in the rulebook
- Host
    - Test the different states of host. Currently tests BEGIN, DEAL_CARDS, WAIT_FOR_CLIENTS and TERMINATED.
- Client
	- Asserts that connected clients retain the correct playerNumber.
	- Assert that the 5 cards chosen by clients are the same 5 returned to host as registry.
	- Assert that clients always return 5 cards assigned to registry, 
	and the remaining as discard (n=100) (none lost, duplicated or created).
### Manual Tests
- Instructions in [Documents](Documents/UserTest.md)

22e3450d273bd813e33374b470ca2e933aee2699
