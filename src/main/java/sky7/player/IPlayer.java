package sky7.player;

import sky7.card.IProgramCard;

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
    IProgramCard[] getRegistry();

    /**
     * @param programCards the program cards the player can choose from.
     */
    void setHand(IProgramCard[] programCards);

    /**
     * @param chosenCards the cards the player has chosen.
     */
    void setRegistry(IProgramCard[] chosenCards);

    /**
     * @return the hand that the player was dealt.
     */
    IProgramCard[] getHand();
}
