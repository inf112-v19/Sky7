package sky7.game;

import com.badlogic.gdx.math.Vector2;
import sky7.Client.Client;
import sky7.board.IBoard;
import sky7.board.ICell;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.robots.RobotTile;
import sky7.card.ICard;
import sky7.card.ProgramCard;
import sky7.host.Host;

import java.util.*;

public class Game implements IGame {

    private Host host;
    private Client client;
    private static final int NR_OF_PHASES = 5;
    private IBoard board;

    /**
     * The construct for a game engine on host.
     *
     * @param host
     * @param board
     */
    public Game(Host host, IBoard board) {
        this.host = host;
        this.board = board;
    }

    /**
     * The constructor for a game engine on client.
     *
     * @param client
     * @param board
     */
    public Game(Client client, IBoard board) {
        this.client = client;
        this.board = board;
    }


    @Override
    public void process(HashMap<Integer, ArrayList<ICard>> playerRegistrys) {
        Queue<Queue<Event>> allPhases = findPlayerSequence(playerRegistrys);
        List<Integer> destroyedRobots = new ArrayList<>();
        for (Queue<Event> phase : allPhases) {
            for (Event player : phase) {
                tryToMove(player);
                expressConveyor();
                normalAndExpressConveyor();
                activatePushers();
                activateCogwheels();
                activateLasers();
                placeBackup();
                flags();
                if (foundWinner()) break;
            }
        }
        //after 5th phaze
        repairRobotsOnRepairSite();
        cleanUp();

        if (host != null) {
            host.finishedProcessing(board);
        } else client.finishedProcessing(board);
    }

    private void cleanUp() {
        // TODO this should be done differently
    }

    private boolean foundWinner() {
        //TODO winner if the clients player, can be found from RoboTile.playerNr.
        /*int winner = 0;
        if (host != null) {
            host.setWinner(0);
            return true;
        } else return true;*/
        return false;
    }

    private void flags() {
        // check winning condition.
        //TODO

        render();
    }

    private void placeBackup() {
        render();
        //REPAIR SITES: A robot on a repair site places their Archive marker (where they respawn) there. (They do NOT repair.)
    }

    private void repairRobotsOnRepairSite() {
        // REPAIR SITES: A robot on a repair site repairs 1 point of damage. A robot on a double tool repair site also draws 1 Option card.
        //TODO
        render();
    }

    private void activateCogwheels() {
        //TODO
        render();
    }

    private void activateLasers() {
        //TODO
        render();
    }

    private void activatePushers() {
        //TODO
        render();
    }

    private void normalAndExpressConveyor() {
        //TODO
        render();
    }

    private void expressConveyor() {
        // TODO check if this robot is on a conveyor belt and there is another robot in front that is also on the convoyer belt
        board.moveConveyors();
        render();
    }

    /**
     * Try to move a robot a step.
     *
     * @param action a Event containing the playerId and ProgramCard
     */
    private void tryToMove(Event action) {
        if (action.card.moveType()) {
            int steps = Math.abs(action.card.move());
            while (steps > 0) {
                DIRECTION dir = board.getRobots()[action.player].getOrientation();
                if (action.card.move() < 1) dir = dir.reverse();
                if (canGo(action.player, dir)) {
                    movePlayer(action.player, dir);
                    steps--;
                    render();
                }
            }
        } else {
            rotatePlayer(action);
            render();
        }

    }

    private void movePlayer(int player, DIRECTION dir) {
        // move player 1 step in direction dir
        Vector2 ahead = board.getDestination(board.getRobotPos()[player], dir, 1);

        if (board.containsPosition(ahead)) {
            for (ICell cell : board.getCell(ahead)) {
                if (cell instanceof RobotTile) {
                    int newPlayer = ((RobotTile) cell).getId();
                    movePlayer(newPlayer, dir);
                }
            }
            board.moveRobot(player, dir);
        } else {
            board.hideRobot(player);
        }
    }

    private void rotatePlayer(Event action) {
        board.rotateRobot(action.player, action.card.rotate());
    }

    private boolean canGo(Integer id, DIRECTION dir) {

        Vector2 here = board.getRobotPos()[id];

        if (facingWall(here, dir)) return false;

        if (occupiedNextCell(here, dir)) return false;

        return true;
    }

    /**
     * Check if next cell can be moved into
     *
     * @param from      the position the robot wants to move from.
     * @param direction the direction the robot want to move.
     * @return true if the
     */
    private boolean occupiedNextCell(Vector2 from, DIRECTION direction) {
        Vector2 ahead = board.getDestination(from, direction, 1);

        // if we are facing a wall, then we cannot move a robot.
        if (!facingWall(ahead, direction.reverse()))

            // check if there is an immovable robot in front
            if (board.containsPosition(ahead))
                for (ICell cell : board.getCell(ahead)) {
                    if (cell instanceof RobotTile) {
                        return occupiedNextCell(ahead, direction);
                    }

                }
        return false;
    }

    /**
     * check if current cell does not hinder movement in the direction {@param direction}. eks if
     *
     * @param pos       the current position
     * @param direction the direction to check
     * @return true if there is no obstacle, such as a wall in the {@param direction}
     */
    private boolean facingWall(Vector2 pos, DIRECTION direction) {
        // TODO check if there is a wall facing movement direction in the current cell
        System.out.println(board.getWidth());
        System.out.println(board.getHeight());
        System.out.println(pos);
        if (board.containsPosition(pos))
            for (ICell cell : board.getCell(pos)) {
                if (cell instanceof Wall && ((Wall) cell).getDirection() == direction) {
                    return true;
                }
            }
        return false;
    }

    @Override
    public void render() {
        // TODO call render if this game belongs to a client, else ignore.
        if (client != null) {
            client.updateBoard(board);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Queues all the phases, where each phase contains one card from each player sorted by the cards priority
     *
     * @param playerRegistries the registry of all players
     * @return All 5 phases queued, where each phase is a queue of Event containing player Id and A card.
     */
    private Queue<Queue<Event>> findPlayerSequence(HashMap<Integer, ArrayList<ICard>> playerRegistries) {
        // TODO make test for this.

        Queue<Queue<Event>> phases = new LinkedList<>();
        for (int i = 0; i < NR_OF_PHASES; i++) {
            Queue<Event> phase = new LinkedList<>();
            for (Integer playerID : playerRegistries.keySet()) {
                phase.add(new Event(playerID, (ProgramCard) playerRegistries.get(playerID).get(i)));
            }
            phases.add(phase);
        }
        return phases;
    }


    /**
     * An event is a card associated with a player.
     */
    private class Event implements Comparable<Event> {
        private Integer player;
        private ProgramCard card;

        private Event(Integer player, ProgramCard card) {
            this.player = player;
            this.card = card;
        }

        @Override
        public int compareTo(Event other) {
            return Integer.compare(card.priorityN(), other.card.priorityN());
        }
    }
}
