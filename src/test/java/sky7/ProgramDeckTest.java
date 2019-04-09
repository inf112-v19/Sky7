package sky7;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import sky7.card.ICard;
import sky7.card.ProgramCard;
import sky7.card.ProgramDeck;

public class ProgramDeckTest {

    static ProgramDeck deck;
    int N = 1000;
    static int fullDeck;
    
    @BeforeClass
    public static void initiate() {
        deck = new ProgramDeck();
        fullDeck = deck.nRemainingCards();
        assertTrue(fullDeck == 84);
    }
    
    @Test
    public void sanityTest() {
        
        ArrayList<ICard> hand;
        
        // draw and return 9 cards N times
        for (int i=0; i<N ; i++) {
            hand = deck.draw(9);
            
            // assert that no duplicate cards occur
            for (int j=0; j<9 ; j++) {
                for (int k=j+1; k<9 ; k++) {
                    assertFalse(((ProgramCard)hand.get(j)).equals(((ProgramCard)hand.get(k))));
                }
            }
            
            deck.returnCards(hand);
            
            hand.clear();
        }
        
        // assert that the deck is complete after drawing and returning cards
        assertTrue(deck.nRemainingCards() == fullDeck);
    }

    @Test
    public void noTowCardsShouldHaveSamePriority(){
        ProgramDeck deck = new ProgramDeck();
        ProgramCard[] cards = deck.getProgramCardsForTesting();
        for (int i = 0; i < cards.length; i++) {
            for (int j = i+1; j < cards.length; j++) {
                assertTrue(cards[i].priorityN() != cards[j].priorityN());
            }
        }
    }
    
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
