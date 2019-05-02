# Sky7

## How to run
### Use if an IDE
- Clone https://github.com/inf112-v19/Sky7.git
- Import to an IDE as existing maven project
- Run Main.java (src/main/java/sky7/main)

### Jar
- Download Sky7RoboRally-V0.1.zip from 
	https://github.com/inf112-v19/Sky7/releases/tag/V0.1-alpha
- Extract the zip file
- Run Sky7RoboRally-V1.0.jar
	- Windows: Open cmd, navigate to the folder containing the jar,
		enter "java -jar Sky7RoboRally-V1.0.jar"
	- Mac: TODO
	- Linux: TODO

## Current functionality
- Game starts in "menu" with only one button. This will later be populated with options to host or connect, what board to load etc.
- One player can play endless rounds by selecting cards from hand and playing them by clicking "go".
- Walls block the path of robots.
- Player can push another robot(placed there for demonstration, not controlled).
- Cogwheels rotate robots.
- Player is dealt new cards each round.

## Documentation
- Class diagram in [Documents/Class diagram](Documents/Class diagram).
- Network diagram in [Documents/Class diagram](Documents/Class diagram).
- DrawPriority explanation in [Documents](Documents).

## Known bugs
- 

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
- DrawPriorityTest
	- Tests that ICell classes behave and compare correctly in a TreeSet.
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
- Instructions in [Documents/UserTest.md](Documents/UserTest.md)

22e3450d273bd813e33374b470ca2e933aee2699
