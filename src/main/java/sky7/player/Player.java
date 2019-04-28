package sky7.player;

import sky7.card.ICard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Player implements IPlayer {

    public static final int MAX_CARDS_IN_REGISTRY = 5;
    private static final int MAX_CARDS_IN_DECK = 9;
    private int damage = 0;
    private int lifeTokens = 3;
    private ArrayList<ICard> hand;
    private ArrayList<ICard> registry;
    private Set<ICard> discard;
    private int nLocked = 0;
    private int playerNumber = 1;


    public Player() {
        hand = new ArrayList<ICard>(MAX_CARDS_IN_DECK);
        registry = new ArrayList<ICard>(MAX_CARDS_IN_REGISTRY);

    }

    @Override
    public void applyDamage(int applyDamage) {
        updateDamage(damage+applyDamage);

        System.out.println("Robot " + playerNumber + " was dealt " + applyDamage + " damage.");
        
        if (damage > 4) {
            nLocked = damage-4;
        }

        // if health reaches 0, consume a life token and respawn
    }

    @Override
    public void repair(int repairDamage) {
        updateDamage(damage-repairDamage);
        
        if (damage > 4) {
            nLocked = damage-4;
        } else nLocked = 0;
    }

    @Override
    public void updateDamage(int totalDamage) {
        damage = Math.max(0, totalDamage);
    }

    @Override
    public int getNLocked() {
        return nLocked;
    }
    
    @Override
    public ArrayList<ICard> getRegistry() {
        return registry;
    }

    @Override
    public void setHand(ArrayList<ICard> programCards) {
        
        hand = programCards;
        discard = new LinkedHashSet<>(hand);
    }

    @Override
    public void clearRegistry() {
        if (registry.size() > 0) {
            for (int i=0; i<5-nLocked; i++) {
                registry.remove(0); // remove the (5-nLocked) left-most cards
            }
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
        if (playerNumber < 0) throw new IllegalArgumentException("playerNumber should be bigger than 0");
        this.playerNumber = playerNumber;
    }

    @Override
    public ArrayList<ICard> getDiscard() {

        return new ArrayList<>(discard);
    }

    @Override
    public void setCard(ICard chosenCard, int positionInRegistry) {
        /*if (positionInRegistry >= 0 && positionInRegistry < MAX_CARDS_IN_REGISTRY) {
            if (registry.size() != 0) {
                ICard temp = registry.get(positionInRegistry); // TODO add test for this.
                if (temp != null) discard.add(temp);
                discard.remove(chosenCard);
                registry.add(positionInRegistry, chosenCard);
            }else {
                discard.remove(chosenCard);
                registry.add(positionInRegistry, chosenCard);
            }
        }*/
        registry.add(positionInRegistry, chosenCard);
        discard.remove(chosenCard);
    }

	@Override
	public CharSequence getDamage() {
		String healthStr = Integer.toString(damage);
		return healthStr;
	}

	@Override
	public CharSequence getLifeToken() {
		String lifeTokensStr = Integer.toString(lifeTokens);
		return lifeTokensStr;
	}

    @Override
    public void decreaseHealth(int i) {
        //TODO check if health is below 0
        health--;
    }


}
