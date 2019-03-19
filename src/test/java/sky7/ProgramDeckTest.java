package sky7;

import org.junit.Test;
import sky7.card.ICard;
import sky7.card.IDeck;
import sky7.card.ProgramCard;
import sky7.card.ProgramDeck;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ProgramDeckTest {

    @Test
    public void correctNumberMove3(){
        ProgramDeck deck = new ProgramDeck();
        ProgramCard[] cards = deck.getProgramCardsForTesting();
        int move3 = 0;
        for (int i = 0; i < cards.length; i++) {
            if( (cards[i]).move() == 3){
                move3++;
            }
        }

        assertEquals(6, move3);
    }

    @Test
    public void correctNumberMove2(){
        ProgramDeck deck = new ProgramDeck();
        ProgramCard[] cards = deck.getProgramCardsForTesting();
        int move2 = 0;
        for (int i = 0; i < cards.length; i++) {
            if( (cards[i]).move() == 2){
                move2++;
            }
        }

        assertEquals(12, move2);
    }

    @Test
    public void correctNumberMove1(){
        ProgramDeck deck = new ProgramDeck();
        ProgramCard[] cards = deck.getProgramCardsForTesting();
        int move1 = 0;
        for (int i = 0; i < cards.length; i++) {
            if( (cards[i]).move() == 1){
                move1++;
            }
        }

        assertEquals(18, move1);
    }

    @Test
    public void correctNumberRotatRight(){
        ProgramDeck deck = new ProgramDeck();
        ProgramCard[] cards = deck.getProgramCardsForTesting();
        int right = 0;
        for (int i = 0; i < cards.length; i++) {
            if( (cards[i]).rotate() == 1){
                right++;
            }
        }

        assertEquals(18, right);
    }

    @Test
    public void correctNumberRotatLeft(){
        ProgramDeck deck = new ProgramDeck();
        ProgramCard[] cards = deck.getProgramCardsForTesting();
        int left = 0;
        for (int i = 0; i < cards.length; i++) {
            if( (cards[i]).rotate() == -1){
                left++;
            }
        }

        assertEquals(18, left);
    }


    @Test
    public void correctNumberOfTurnArounds(){
        ProgramDeck deck = new ProgramDeck();
        ProgramCard[] cards = deck.getProgramCardsForTesting();
        int turnAround = 0;
        for (int i = 0; i < cards.length; i++) {
            if( (cards[i]).rotate() == 2){
                turnAround++;
            }
        }

        assertEquals(6, turnAround);
    }
}
