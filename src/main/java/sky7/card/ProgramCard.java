package sky7.card;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ProgramCard implements IProgramCard, Comparable<ProgramCard> {

	private int priority, move, rotate;
	private boolean moveType;
	private String sprite;

	/**
	 * @param priority Card priority number
	 * @param move Move value, positive forward, negative to reverse
	 * @param rotate rotation value, positive clockwise, negative counter-clockwise
	 */
	public ProgramCard(int priority, int move, int rotate) {
		this.priority = priority;
		this.move = move;
		this.rotate = rotate;

		if (move > 0)
			moveType = true;
		else
			moveType = false;

	}

	@Override
	public int priorityN() {
		return priority;
	}

	@Override
	public int move() {
		return move;
	}

	@Override
	public int rotate() {
		return rotate;
	}

	@Override
	public int compareTo(ProgramCard other) {
		return Integer.compare(this.priority, other.priorityN());
	}

	@Override
	public String toString() {
		return super.toString();//TODO return a string representation of the card
	}

	@Override
	public boolean moveType() {
		return moveType;
	}

	@Override
	public String GetSpriteRef() { // Auto-generated method stub
		if (sprite == null) {
			if (move == 1) {
//				Texture tex = new Texture("assets/cards/Move1.png");
//				return new Sprite(tex);
				return "Move1";
			}
			if (move == 2) {
//				Texture tex = new Texture("assets/cards/Move2.png");
//				return new Sprite(tex);
				return "Move2";
			}
			if (move == 3) {
//				Texture tex = new Texture("assets/cards/Move3.png");
//				return new Sprite(tex);
				return "Move3";
			}
			if (move == -1) {
//				Texture tex = new Texture("assets/cards/MoveBack.png");
//				return new Sprite(tex);
				return "MoveBack";
			}
			if (rotate == 1) {
//				Texture tex = new Texture("assets/cards/RotateRight.png");
//				return new Sprite(tex);
				return "RotateRight";
			}
			if (rotate == -1) {
//				Texture tex = new Texture("assets/cards/RotateLeft.png");
//				return new Sprite(tex);
				return "RotateLeft";
			}
			if (rotate == -2) {
//				Texture tex = new Texture("assets/cards/uTurn.png");
//				return new Sprite(tex);
				return "uTurn";
			}
		}
		return "null";
	}

}