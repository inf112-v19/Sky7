package sky7.player;

import java.util.ArrayList;
import java.util.Arrays;

import sky7.card.ICard;

public class Player implements IPlayer {

    public static final int MAX_CARDS_IN_REGISTRY = 5;
    private static final int MAX_CARDS_IN_DECK = 9;
    private int damage = 0;
    private int lifeTokens = 3;
    private ArrayList<ICard> hand;
    private ArrayList<ICard> discard;
    private ICard[] registry;
    private ICard[] lockedRegistry;
    private int nLocked = 0;
    private int playerNumber = 1;


    public Player() {
        hand = new ArrayList<ICard>(MAX_CARDS_IN_DECK);
        registry = new ICard[MAX_CARDS_IN_REGISTRY];
        lockedRegistry = new ICard[5];

        for (int i = 0; i < lockedRegistry.length; i++) {
            lockedRegistry[i] = null;
        }
    }

    @Override
    public ICard[] getLockedRegistry() {
        return lockedRegistry;
    }

    @Override
    public boolean loseLifeToken() {
        return --lifeTokens < 0;
    }

    @Override
    public void applyDamage(int applyDamage) {
        updateDamage(damage + applyDamage);

        System.out.println("Robot " + playerNumber + " was dealt " + applyDamage + " damage.");

        if (damage > 4) {
            nLocked = damage - 4;
        }
    }

    @Override
    public void repairDamage(int applyHealth) {
        if (damage > 0) {
            updateDamage(damage - applyHealth);
        }

        System.out.println("Robot " + playerNumber + " repair " + applyHealth + " damage.");

        if (damage > 4) {
            nLocked = damage - 4;
        }

    }


    @Override
    public void updateDamage(int totalDamage) {
        damage = Math.max(0, totalDamage);
        if (damage > 4) {
            nLocked = damage - 4;
        } else nLocked = 0;
    }

    @Override
    public int getNLocked() {
        return nLocked;
    }

    @Override
    public ICard[] getRegistry() {
        return registry;
    }
    
    @Override
    public ArrayList<ICard> getChosenRegistry() {
        return new ArrayList<ICard>(Arrays.asList(registry));
    }

    @Override
    public void setHand(ArrayList<ICard> programCards) {

        hand = programCards;
        discard = new ArrayList<>(hand);
    }

    @Override
    public void clearRegistry() {
        for (int i=0; i<5-nLocked; i++) {
            registry[i] = null;
        }
    }

    @Override
    public void resetRegistry() {
        for (int i=0; i<5; i++) {
            registry[i] = null;
        }
    }

    @Override
    public ArrayList<ICard> getHand() {
        return hand;
    }

    @Override
    public int getPlayerNumber() {
        return this.playerNumber;
    }

    @Override
    public void setPlayerNumber(int playerNumber) throws IllegalArgumentException {
        if (playerNumber < 0) throw new IllegalArgumentException("Attempted to set player number less than 0");
        this.playerNumber = playerNumber;
    }

    @Override
    public ArrayList<ICard> getDiscard() {
        return new ArrayList<>(discard);
    }

    @Override
    public void setCard(ICard chosenCard, int positionInRegistry) {
        registry[positionInRegistry] = chosenCard;
        discard.remove(chosenCard);
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public int getLifeToken() {
        return lifeTokens;
    }

}
