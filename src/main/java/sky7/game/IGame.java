package sky7.game;

import sky7.card.ICard;

import java.util.ArrayList;
import java.util.HashMap;

public interface IGame {

    void process(HashMap<Integer,ArrayList<ICard>> cards);

    void render(int milliSec);

}
