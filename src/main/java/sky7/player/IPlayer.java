package sky7.player;

import sky7.card.ICard;
import sky7.card.IProgramCard;

import java.util.ArrayList;

public interface IPlayer {

    /**
     * Subtract damage from players health
     * @param damage integer representing damage
     */
    void applyDamage(int damage);

    /**
     * Add damage to health with upper MAX_HEALTH. Unlock locked cards as much as health allows.
     * @param damage integer representing damage
     */
    void repair(int damage);

    /**
     * If health is less than 6 lock numberOfCards.
     * @param numberOfCards integer representing how many cards
     */
    void lockCards(int numberOfCards);

    /**
     * unlock numberOfCards number of cards.
     * @param numberOfCards integer representing how many cards
     */
    void unlockCards(int numberOfCards);

    /**
     * update health with newHealth
     * @param newHealth the new health of this player.
     */
    void updateHealth(int newHealth);

    /**
     * @return a list of cards chosen by player
     */
    ArrayList<ICard> getRegistry();

    /**
     * @param programCards the program cards the player can choose from.
     */
    void setHand(ArrayList<ICard>  programCards);

    /**
     * @param chosenCards the cards the player has chosen.
     */
    void setRegistry(ArrayList<ICard>  chosenCards);

    /**
     * @return the hand that the player was dealt.
     */
    ArrayList<ICard> getHand();

    /**
     *
     * @return the id of this player
     */
    public int getPlayerNumber();

    /**
     * @param playerNumber the id of this player.
     */
    void setPlayerNumber(int playerNumber);

    /**
     * @return the card not choosen by the player.
     */
    ArrayList<ICard> getDiscard();

    /**
     * Put in the register a new chosen card at a position.
     * @param chosenCard the card that the player chose
     * @param positionInRegistry the position of the card in the registry.
     */
    void setCard(ICard chosenCard, int positionInRegistry);
}
