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
- Located in [test folder](src/test/java/sky7)
- Run TestSuite as JUnit test - combines all tests in a Suite
- ICell
    - Test that the priority of each ICell is correct
- BoardGenerator
    - Tests that BoardGenerator can read a random Json file,
    and that the height and width is correct.
- ProgramDeck
    - Test that the values of ProgramDeck matches the number of different
     cards as shown in the rulebook

22e3450d273bd813e33374b470ca2e933aee2699
