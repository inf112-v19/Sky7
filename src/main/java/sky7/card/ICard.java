package sky7.card;

public interface ICard {
	/**
	 * Priority of the card. Lowest priority will be played first.
	 *
	 * @return priority of this card
	 */
	CharSequence getPriority();

	int getX();

	int getY();

	/**
	 *
	 * @return string representing a sprite reference
	 */
	String GetSpriteRef();

	void setX(int input);

	void setY(int input);
}
