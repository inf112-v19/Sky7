package sky7.game;

import com.badlogic.gdx.math.Vector2;
import sky7.Client.IClient;
import sky7.board.IBoard;
import sky7.board.ICell;
import sky7.board.cellContents.Active.CogWheel;
import sky7.board.cellContents.Active.Laser;
import sky7.board.cellContents.DIRECTION;
import sky7.board.cellContents.Inactive.Flag;
import sky7.board.cellContents.Inactive.Hole;
import sky7.board.cellContents.Inactive.Wall;
import sky7.board.cellContents.Inactive.Wrench;
import sky7.board.cellContents.robots.RobotTile;
import sky7.card.ICard;
import sky7.card.ProgramCard;
import sky7.host.Host;

import java.util.*;

public class Game implements IGame {

    private Host host;
    private IClient client;
    private static final int NR_OF_PHASES = 5;
    private IBoard board;
    private List<Integer> destroyedRobots = new ArrayList<>();
    private boolean hosting;
    private boolean disableDamage = false;

    /**
     * The construct for a game engine on host.
     *
     * @param host
     * @param board
     */
    public Game(Host host, IBoard board) {
        this.host = host;
        this.board = board;
        hosting = true;
    }

    /**
     * The constructor for a game engine on client or host
     *
     * @param client
     * @param board
     */
    public Game(IClient client, IBoard board) {
        this.client = client;
        this.board = board;
    }


    @Override
    public void process(HashMap<Integer, ArrayList<ICard>> playerRegistrys) {
        destroyedRobots = new ArrayList<>();
        Queue<Queue<Event>> allPhases = findPlayerSequence(playerRegistrys);
        int count = 0;
        int phaseNr = 1;
        for (Queue<Event> phase : allPhases) {
            System.out.println("phase: " + count++);
            
            // B. Robots Move
            for (Event action : phase) {
                if (!destroyedRobots.contains(action.player))
                    tryToMove(action);
                if (foundWinner()) break;
            }
            
            // C. Board Elements Move
            expressConveyor();
            normalAndExpressConveyor();
            activatePushers(phaseNr);
            activateCogwheels();
            
            // D. Lasers Fire
            activateLasers();
            
            // E. Touch Checkpoints
            placeBackup();
            if (hosting) flags();
            phaseNr++;
        }
        //after 5th phase
        repairRobotsOnRepairSite();
        cleanUp();

        if (hosting) {
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
        
        RobotTile[] robots = board.getRobots();
        for (int i = 0; i < robots.length; i++) {
            if (robots[i] != null)
                for (ICell cell : board.getCell(board.getRobotPos()[i])) {
                    if (cell instanceof Flag) {
                        System.out.println("Robot " + robots[i].getId() + " visited flag " + ((Flag)cell).getFlagNumber());
                        host.robotVisitedFlag(robots[i].getId(), ((Flag)cell).getFlagNumber());
                        break;
                    }
                }
        }

        render(50);
    }

    private void placeBackup() {
        RobotTile[] robots = board.getRobots();
        for (int i = 0; i < robots.length; i++) {
            if (robots[i] != null)
                for (ICell cell : board.getCell(board.getRobotPos()[i])) {
                    if (cell instanceof Wrench || cell instanceof Flag) {
                        robots[i].setArchiveMarker(board.getRobotPos()[i]);
                        break;
                    }
                }
        }
    }

    private void repairRobotsOnRepairSite() {
        // REPAIR SITES: A robot on a repair site repairs 1 point of damage. A robot on a double tool repair site also draws 1 Option card.
        //TODO
        render(50);
    }

    /**
     * rotate all robots standing on a cogwheel.
     */
    private void activateCogwheels() {
        RobotTile[] robots = board.getRobots();
        for (int i = 0; i < robots.length; i++) {
            if (robots[i] != null)
                for (ICell cell : board.getCell(board.getRobotPos()[i])) {
                    if (cell instanceof CogWheel) {
                        board.rotateRobot(i, ((CogWheel) cell).getRotation());
                        break;
                    }
                }
        }
        render(50);
    }

    private void activateLasers() {
        //TODO lasers should only be laser start position.

        List<ICell> lasersHeads = new ArrayList<>();
        List<Vector2> headPosition = new ArrayList<>();

        lasersHeads.addAll(board.getLasers());
        headPosition.addAll(board.getLaserPos());

        for (int i = 0; i < board.getRobots().length; i++) {
            if (board.getRobots()[i] != null) {
                lasersHeads.add(board.getRobots()[i]);
                headPosition.add(board.getRobotPos()[i]);

            }
        }
        fireLasers(lasersHeads, headPosition); // presume that lasers are only laser start positions and robot position
    }

    private void fireLasers(List<?> heads, List<?> positions) {
        //presume that lasers and laserPos are the head positions of the lasers and robots.
        if (heads.isEmpty()) return;
        show(heads, positions);

        render(20);

        List<List<?>> next = moveLaserHeads(heads, positions);
        fireLasers(next.get(0), next.get(1));

        hide(heads, positions);
        render(20);
    }

    private List<List<?>> moveLaserHeads(List<?> heads, List<?> positions) {
        List<List<?>> list = new ArrayList<>();

        List<ICell> nextHeads = new ArrayList<>();
        List<Vector2> nextHeadPositions = new ArrayList<>();

        for (int i = 0; i < heads.size(); i++) {
            //For each laser head, get the next position of the head.

            ICell cell = (ICell) heads.get(i);
            Vector2 position = (Vector2) positions.get(i);

            if (!laserStopped(cell, position)) {
                if (cell instanceof Laser) {
                    Laser laser = (Laser) cell;
                    nextHeads.add(new Laser(false, laser.getDirection(), laser.nrOfLasers()));
                    nextHeadPositions.add(board.getDestination(position, laser.getDirection(), 1));
                } else if (cell instanceof RobotTile) {
                    RobotTile robot = (RobotTile) cell;
                    // TODO modifity the number of lasers for a robot when implemented.
                    nextHeads.add(new Laser(false, robot.getOrientation(), 1));

                    nextHeadPositions.add(board.getDestination(position, robot.getOrientation(), 1));
                }
            }
        }
        list.add(nextHeads);
        list.add(nextHeadPositions);

        return list;
    }

    private boolean laserStopped(ICell cell, Vector2 pos) {
        // if there is a wall it is edge of the board, then return true;
        // if the is a robot infront, then add damage to the robots health.
        Vector2 ahead = null;
        if (cell instanceof RobotTile) {
            RobotTile robot = (RobotTile) cell;
            ahead = board.getDestination(pos, robot.getOrientation(), 1);
        } else if (cell instanceof Laser) {
            Laser laser = (Laser) cell;
            ahead = board.getDestination(pos, laser.getDirection(), 1);
        }
        if (ahead == null || !board.containsPosition(ahead)) {
            return true;

        } else {
            for (ICell aheadCell : board.getCell(ahead)) {
                if (aheadCell instanceof Wall) return true;
                if (aheadCell instanceof RobotTile) {
                    applyDamage(((RobotTile) aheadCell).getId(), 1); // apply 1 damage
                    return true;
                }
            }
        }
        return false;
    }

    private void hide(List<?> lasers, List<?> laserPos) {

        // hide all lasers in the list.
        for (int i = 0; i < laserPos.size(); i++) {
            Vector2 pos = (Vector2) laserPos.get(i);
            ICell cell = (ICell) lasers.get(i);
            if (!(cell instanceof RobotTile) && !((Laser) cell).isStartPosition()) {
                board.removeCell(cell, pos);
            }
        }

    }

    private void show(List<?> lasers, List<?> laserPos) {
        // show all lasers in the list.
        for (int i = 0; i < lasers.size(); i++) {
            Object cell = lasers.get(i);
            if (cell instanceof Laser) {

                Laser laser = (Laser) cell;
                if (!laser.isStartPosition()) {
                    Vector2 pos = (Vector2) laserPos.get(i);
                    board.addCell(laser, pos);
                }
            }
        }
    }

    private void activatePushers(int phaseNr) {
        for(int i=0; i<board.getPushers().size(); i++){
            for(int j=0; j<board.getRobots().length; j++){
                if(board.getPusherPos().get(i).equals(board.getRobotPos()[j])){
                    if(board.getPushers().get(i).doActivate(phaseNr)){
                        if(robotCanGo(board.getRobots()[j].getId(), board.getPushers().get(i).getDirection())){
                            movePlayer(board.getRobots()[j].getId(), board.getPushers().get(i).getDirection());
                        }
                    }
                }
            }
        }
        render(50);
    }

    private void normalAndExpressConveyor() {
        //TODO
        render(50);
    }

    private void expressConveyor() {
        // TODO check if this robot is on a conveyor belt and there is another robot in front that is also on the convoyer belt
        render(50);
    }

    private void applyDamage(int playerID, int damage) {
        if (disableDamage) return;
        if (hosting) {
            host.applyDamage(playerID, damage);
        } else if (client.getPlayer().getPlayerNumber() == playerID) client.applyDamage(playerID, damage);
    }

    /**
     * Try to move a robot a step.
     *
     * @param action a Event containing the playerId and ProgramCard
     */
    private void tryToMove(Event action) {
        boolean dead = false;
        if (action.card.moveType()) {
            int steps = Math.abs(action.card.move());
            while (!dead && steps > 0) {
                DIRECTION dir = board.getRobots()[action.player].getOrientation();
                if (action.card.move() < 1) dir = dir.reverse();
                if (robotCanGo(action.player, dir)) {
                    dead = movePlayer(action.player, dir);
                    steps--;
                } else steps = 0;
            }
        } else {
            rotatePlayer(action);
            render(50);
        }

    }

    private boolean movePlayer(int player, DIRECTION dir) {
        // move player 1 step in direction dir
        Vector2 ahead = board.getDestination(board.getRobotPos()[player], dir, 1);
        boolean dead;
        if (board.containsPosition(ahead)) {
            for (ICell cell : board.getCell(ahead)) {
                if (cell instanceof RobotTile) {
                    int newPlayer = ((RobotTile) cell).getId();
                    movePlayer(newPlayer, dir);
                }
            }
            board.moveRobot(player, dir);
            render(100);
            dead = checkForHole(player);
        } else { // robot is outside the board.
            board.hideRobot(player);
            destroyedRobots.add(player);
            dead = true;
        }
        if (dead) render(50);
        return dead;
    }

    private boolean checkForHole(int player) {
        for (int i = 0; i < board.getRobots().length; i++) {
            if (board.getRobots()[i] != null && player == board.getRobots()[i].getId()) {
                for (ICell cell : board.getCell(board.getRobotPos()[i])) {
                    if (cell instanceof Hole) {
                        board.hideRobot(player);
                        destroyedRobots.add(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void rotatePlayer(Event action) {
        board.rotateRobot(action.player, action.card.rotate());
    }


    private boolean robotCanGo(Integer playerId, DIRECTION dir) {

        Vector2 here = board.getRobotPos()[playerId];

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
        if (!facingWall(ahead, direction.reverse())) {
            // check if there is an immovable robot in front
            for (ICell cell : board.getCell(ahead)) {
                if (cell instanceof RobotTile) {
                    return occupiedNextCell(ahead, direction);
                }
            }
            return false;
        } else return true;
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
        if (board.containsPosition(pos)) {
            for (ICell cell : board.getCell(pos)) {
                if (cell instanceof Wall && ((Wall) cell).getDirection() == direction) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(int milliSec) {
        // TODO call render if this game belongs to a client, else ignore.
        if (client != null) {
            client.updateBoard(board);
            try {
                Thread.sleep(milliSec);
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