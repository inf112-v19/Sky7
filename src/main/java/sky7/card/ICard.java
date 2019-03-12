package sky7.card;

public interface ICard {
	
	CharSequence getPriority();
	
	int getX();
	int getY();

	String GetSpriteRef();
	void setX(int input);
	void setY(int input);
}
