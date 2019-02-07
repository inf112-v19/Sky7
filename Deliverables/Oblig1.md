# Oblig1 - Sky7 RoboRally

## Part 1

- Group name: Sky7.
- Team lead: Carl August Gjørsvik.
- Key account manager: Brigt Arve Toppe Håvardstun.

### Competence
  
In summery, every team member has the programming skills needed for this task and some additional abilies that could potensially improve the projects workflow. Every team member has taken courses INF100, INF101 and INF102. Thus we are all experienced with OOP.

- Henrik:
  - Network
  - Made games in java.
- Carl:
  - Network
  - Made gamed in java.
  - Word/Excel (Data representation) through profesjonall work. Likt resten av laget, har han også deltatt i diverse  
  - Participated in compatative programming.
- Maren:
  - Participated in compatative programming.
- Brigt:
  - JavaFX
  - Konkurranseprogrammering. 
  - PLayes boardgames 
- Fromsa:
  - Python 
  - Haskell 
  - Participated in compatative programming.

### Technical tools to be used:
- Programming
  - Java (Eclipse, IntelliJ)
  - LibGDX
  - Git version controll.
- Communication
  - Slack
  - Github project board
  - Google Docs

## Part 2

### Overall goal of application
Create a PC-game implementation of the board game RoboRally. 
It should be playable by 2 to 8 players from 8 PCs on the same network. 
Some simplifications must be done; remove “Option” cards, remove timer

### Application Requirements
- Board
  - Static tiles
  - Holes
  - Walls
  - Floor
  - Interacting tiles
  - Backup station
  - Cogwheel
  - Repair station
  - Laser
  - Piston
  - Conveyor belt
  - Flags
  - 1 Robot per grid square
- Robots
  - Visible
  - Position on board
  - Movable
  - Health
  - Laser
  - Power-down (repair)
- UI-border
  - Health status
  - Cards on hand
  - Cards in play
- Cards
  - Priority
  - Actions (Instructions)
- Game rounds
  - Before round
    - Hand out cards
    - Players can move cards from hand to play
    - Players can click ready when instructions are complete / auto ready if powerdown
  - 5 Phases
    - Reveal cards and move robots in order of priority
    - After round
      - Repair ?

### Priority in the first iteration
1. The overall structure of the boardgame
2. Create Interfaces in accordance with structure plan
3. Get a basic understanding of LibGDX
4. Create a barebone game class with a visual board and 1 robot
5. Prepare a presentation

## Part 3

### Process- and projectplan
We selected a combination of Kanban and Scrum. There are no pre-defined roles for the team. Although, there are technically lead and account manager, the team is encouraged to collaborate and chip in when any one person becomes overwhelmed. Deliverables are determined by sprints, or set periods of time in which a set of work must be completed and ready for review. For delegation and prioritization we use a git projectboard, where each team member works on one issue at a time. This resembles a "pull system". We allows for changes to be made to a project mid-stream, allowing for iterations and continuous improvement prior to the completion of a project, because the size of the project is not to big. Finally we measure productivity using velocity through sprints. Each sprint is laid out back-to-back and/or concurrently so that each additional sprint relies on the success of the one before it. 


### Programming process of the first programming:
Everybody can post on the backlog and everybodt else can contribute in definition of the backlog issue, until it is aproaved and broken down into accurate tasks by the team lead and appended onto the todo list. Each team member should take and assign themselves an issue from the git projectboard. Any team member can only work on one issue at a time.

### Meetings:
  First wednesday after delivery a retrospektive-meeting will be held.
  The wednesday after that, a status-meeting will be held.
  Communication will be mainly over slack.

#### First meeting: 
  - introduction, start create a plan
#### Second meeting: 
  - create a structure plan
#### Third meeting:  
  - When small changes are made, merge.
  - When big chanves are made: run a pull request, and inform on slack of the request. 


## Summery

### What worked:
  - The code was completed with out much problems.
  - The meetings were productive.
  - Workflow is improving.
  
### What did not work:
Team agreed to use trello because it was supposedly easy, but there were to many platforms of communication to keep track. This problem lead to some of the team members coding more than others and working on the same issue. Thus not everybody had overview. Therefore, inorder to use fewer platforms we changed to git projectboard. It is working better so far. 

### Plan for next iteration so far:
  - Add the cards to the game and make them clickable.
  - Move the bots.
  - Create a board generator that reads from a file storing the boards.

