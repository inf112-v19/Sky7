# Sky7

## How to run
- Clone git repo
- Import to an IDE as existing maven project
- Run Main.java (src/main/java/sky7/main)

## Current functionality
- One player can move a robot on the board by clicking on cards to put them in the registry, then click "Go" to have the game engine execute the plan.

## Documentation
- Class diagram in Documents folder

## Known bugs
- The robot does not move backward when activating a "MoveBack" card.

## Tests
- Located at in [test folder](src/test/java/inf112/skeleton/app)
- Tests ICell properties
    - Test that the priority of each ICell is correct
- IBoardGenerator
    - Tests that IBoardGenerator can read a random Json file,
    and that the height and width is correct.
- ProgramDeck
    - Test that the values of ProgramDeck matches the number of different
     cards as shown in the rulebook

22e3450d273bd813e33374b470ca2e933aee2699
