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
    public void process(HashMap<Integer, ArrayList<ICard>> playerRegistrys, boolean[] powerDown) {
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
        respawnRobots();
        repairRobotsOnRepairSite();
        powerDownRepair(powerDown);


        if (hosting) host.finishedProcessing(board);
    }

    private void respawnRobots() {
        for (Integer id : destroyedRobots) {
            RobotTile robot = board.getRobots()[id];
            robot.setOrientation(DIRECTION.NORTH);
            board.addCell(robot, robot.getArchiveMarker());
            board.getRobotPos()[id] = robot.getArchiveMarker();
        }
        destroyedRobots.clear();
    }

    /**
     * repair all damage for robots in power down.
     *
     * @param powerDown list of robots who chose to power down
     */
    private void powerDownRepair(boolean[] powerDown) {
        if (hosting) {
            host.powerDownRepair(powerDown);
//            System.out.println("(Host) Robots in Power Down state are repairing.");
        } else {
            client.powerDownRepair(powerDown);
//            System.out.println("(Client) Robots in Power Down state are repairing.");
        }
    }

    /**
     * check if a robot visits a flag and update that robot visited that flag.
     */
    private void flags() {
        RobotTile[] robots = board.getRobots();
        for (int i = 0; i < robots.length; i++) {
            if (robots[i] != null)
                for (ICell cell : board.getCell(board.getRobotPos()[i])) {
                    if (cell instanceof Flag) {
                        System.out.println("Robot " + robots[i].getId() + " visited flag " + ((Flag) cell).getFlagNumber());
                        host.robotVisitedFlag(robots[i].getId(), ((Flag) cell).getFlagNumber());
                        break;
                    }
                }
        }

        render(50);
    }

    /**
     * update a robots archive marker if the robot is visiting a wrench or a flag.
     */
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

    /**
     * A robot on a repair site repairs 1 point of damage.
     * A robot on a double tool repair site also draws 1 Option card (if we have option cards).
     */
    private void repairRobotsOnRepairSite() {
        for (int i = 0; i < board.getWrenches().size(); i++) {
            for (int j = 0; j < board.getRobots().length; j++) {
                if (board.getWrenchPositions().get(i).equals(board.getRobotPos()[j])) {
                    if (board.getWrenches().get(i).getType() == 1) {
                        repairDamage(board.getRobots()[j].getId(), 1);
                    } else if (board.getWrenches().get(i).getType() == 2) {
                        repairDamage(board.getRobots()[j].getId(), 1);
                        //TODO should this draw 1 option card?
                    }
                }
            }

        }
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

    /**
     * activate lasers
     */
    private void activateLasers() {
        //TODO lasers should only be laser start position.

        List<Laser> lasersHeads = new ArrayList<>();
        List<Vector2> headPositions = new ArrayList<>();

        // get laserStartPositions.
        // add new laser lines at start positions

        for (int i = 0; i < board.getLasers().size(); i++) {
            Laser startLaser = board.getLasers().get(i);
            lasersHeads.add(new Laser(false, startLaser.getDirection(), startLaser.nrOfLasers()));
            headPositions.add(board.getLaserPos().get(i));
        }

        // get robot positions
        // add new laser lines ahead of robots.

        for (int i = 0; i < board.getRobots().length; i++) {
            RobotTile robot = board.getRobots()[i];
            if (robot != null) {
                if (robotAhead(robot.getOrientation(), board.getRobotPos()[i])) {
                    lasersHeads.add(new Laser(false, robot.getOrientation(), 1));
                    headPositions.add(board.getDestination(board.getRobotPos()[i], robot.getOrientation(), 1));
                }
            }
        }

        Pair<List<Laser>, List<Vector2>> lasers = new Pair<>(lasersHeads, headPositions);
        fireLasers(lasers); // presume that lasers are only laser start positions and robot position
    }

    /**
     * check if there is a robot a head
     *
     * @param dir in what direction to check
     * @param pos from position
     * @return true if there is a robot ahead, false otherwise.
     */
    private boolean robotAhead(DIRECTION dir, Vector2 pos) {
        if (facingWall(pos, dir)) return false;
        Vector2 ahead = board.getDestination(pos, dir, 1);
        if (!board.containsPosition(ahead)) return false;
        if (facingWall(ahead, dir.reverse())) return false;
        for (ICell cell : board.getCell(ahead)) {
            if (cell instanceof RobotTile)
                return true;
        }
        return robotAhead(dir, ahead);
    }

    /**
     * fire lasers
     * @param lasers laser to be fire
     */
    private void fireLasers(Pair<List<Laser>, List<Vector2>> lasers) {
        //presume that lasers and laserPos are the head positions of the lasers and robots.
        if (lasers.a.isEmpty()) return;

        show(lasers);
        render(20);

        Pair<List<Laser>, List<Vector2>> next = moveLaserHeads(lasers);
        fireLasers(next);

        hide(lasers);
        render(20);
    }

    /**
     * For each laser head, get the next position of the head.
     * @param lasers list of laser to find the next for
     * @return
     */
    private Pair<List<Laser>, List<Vector2>> moveLaserHeads(Pair<List<Laser>, List<Vector2>> lasers) {
        Pair<List<Laser>, List<Vector2>> nextLasers;

        List<Laser> nextHeads = new ArrayList<>();
        List<Vector2> nextHeadPositions = new ArrayList<>();

        for (int i = 0; i < lasers.a.size(); i++) {
            //For each laser head, get the next position of the head.

            Laser laser = lasers.a.get(i);
            Vector2 position = lasers.b.get(i);

            if (!laserStopped(laser, position)) {
                nextHeads.add(new Laser(false, laser.getDirection(), laser.nrOfLasers()));
                nextHeadPositions.add(board.getDestination(position, laser.getDirection(), 1));
            }
        }

        return new Pair<>(nextHeads, nextHeadPositions);
    }

    /**
     * Check if a laser fire in a robot or a wall. Should also apply damage if it hits a robot.
     *
     * @param laser the laser to check
     * @param pos laser position
     * @return true if it fires a wall or a robot, false otherwise.
     */
    private boolean laserStopped(Laser laser, Vector2 pos) {

        // if pos has a robot. apply damage return true
        for (ICell cell : board.getCell(pos)) {
            if (cell instanceof RobotTile) {
                applyDamage(((RobotTile) cell).getId(), 1);
                return true;
            }
        }
        // if facing wall
        if (facingWall(pos, laser.getDirection())) return true;
        else {
            Vector2 ahead = board.getDestination(pos, laser.getDirection(), 1);
            if (board.containsPosition(ahead)) {
                return facingWall(pos, laser.getDirection().reverse());
            }
            return false;
        }
    }

    /**
     * dont show lasers.
     *
     * @param lasers to be hided.
     */
    private void hide(Pair<List<Laser>, List<Vector2>> lasers) {

        // hide all lasers in the list.
        for (int i = 0; i < lasers.a.size(); i++) {
            board.removeCell(lasers.a.get(i), lasers.b.get(i));
        }

    }

    /**
     * show lasers.
     * @param lasers to be showed.
     */
    private void show(Pair<List<Laser>, List<Vector2>> lasers) {
        // show all lasers in the list.
        for (int i = 0; i < lasers.a.size(); i++) {
            board.addCell(lasers.a.get(i), lasers.b.get(i));
        }
    }

    /**
     * Activate pushers.
     * If a robot stands on a pusher that activates in current phase and the robot is not blocked, it should be pushed.
     *
     * @param phaseNr current phase
     */
    private void activatePushers(int phaseNr) {
        for (int i = 0; i < board.getPushers().size(); i++) {
            for (int j = 0; j < board.getRobots().length; j++) {
                if (board.getPusherPos().get(i).equals(board.getRobotPos()[j])) {
                    if (board.getPushers().get(i).doActivate(phaseNr)) {
                        if (robotCanGo(board.getRobots()[j].getId(), board.getPushers().get(i).getDirection())) {
                            movePlayer(board.getRobots()[j].getId(), board.getPushers().get(i).getDirection());
                        }
                    }
                }
            }
        }
        render(50);
    }

    private void normalAndExpressConveyor() {
        board.moveConveyors(false);
        render(50);
    }

    private void expressConveyor() {
        // TODO check if this robot is on a conveyor belt and there is another robot in front that is also on the convoyer belt

        board.moveConveyors(true);
        render(50);
    }

    /**
     * Decrease the robots health. If robot lose life token and dont have more than one, robot lose.
     *
     * @param playerID player to be damaged.
     * @param damage amount of damage
     */
    private void applyDamage(int playerID, int damage) {
        if (disableDamage) return;
        if (hosting) {
            if (host.applyDamage(playerID, damage)) killRobot(playerID);
        } else if (client.applyDamage(playerID, damage)) killRobot(playerID);
    }

    /**
     * Increase the robots health.
     * @param playerID player to be repaired
     * @param health amount of repair
     */
    private void repairDamage(int playerID, int health) {
        if (disableDamage) return;
        if (hosting) host.repairDamage(playerID, health);
        else client.repairDamage(playerID, health);

    }

    /**
     * @param playerID player to be checked.
     * @return true if player loose a life token, false otherwise.
     */
    private boolean loseLifeToken(int playerID) {
        if (hosting) {
            return host.loseLifeToken(playerID);
        } else return client.loseLifeToken(playerID);
    }

    /**
     * If robot go outside the board, it should be hided from the board.
     * If robot dont have more life tokens, it should be destroyed.
     * @param playerID robot that lose.
     */
    private void killRobot(int playerID) {
        board.hideRobot(playerID);
        // not playerID has entered game over, respawn
        if (!loseLifeToken(playerID)) destroyedRobots.add(playerID);
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

    /**
     * Move player in dir direction.
     *
     * @param player robot to be moved
     * @param dir move in this direction
     * @return true if robot goes in a hole or go outside the board, false otherwise.
     */
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
            killRobot(player);
            dead = true;
        }
        if (dead) render(50);
        return dead;
    }

    /**
     * check if robot stands on a hole.
     * @param player robot to check
     * @return true if robot stands on a hole, false otherwise.
     */
    private boolean checkForHole(int player) {
        for (int i = 0; i < board.getRobots().length; i++) {
            if (board.getRobots()[i] != null && player == board.getRobots()[i].getId()) {
                for (ICell cell : board.getCell(board.getRobotPos()[i])) {
                    if (cell instanceof Hole) {
                        killRobot(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Rotate the player with actions instructs.
     * @param action tell what direction to rotate in.
     */
    private void rotatePlayer(Event action) {
        board.rotateRobot(action.player, action.card.rotate());
    }

    /**
     * check if a robot can go in dir direction.
     * @param playerId robot to check for
     * @param dir direction to check
     * @return true if the robot can go, false otherwise.
     */
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
        if (board.containsPosition(ahead)) {
            if (!facingWall(ahead, direction.reverse())) {
                // check if there is an immovable robot in front

                for (ICell cell : board.getCell(ahead)) {
                    if (cell instanceof RobotTile) {
                        return occupiedNextCell(ahead, direction);
                    }
                }
                return false;
            } else return true;
        } else return false;
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
    private class Pair<A, B> {
        private A a;
        private B b;

        private Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getA() {
            return a;
        }

        public B getB() {
            return b;
        }
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