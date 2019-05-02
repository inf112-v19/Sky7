package sky7.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class ProgramDeck implements IDeck {
    
    private Stack<IProgramCard> availableCards;
    
    public ProgramDeck() {
        availableCards = new Stack<>();
        
        //generate the 84 cards in a program deck
        
        for (int i=0; i<18; i++) {
            availableCards.push(new ProgramCard(80+20*i, 0, 1)); //rotate right
            availableCards.push(new ProgramCard(70+20*i, 0, -1)); //rotate left
            availableCards.push(new ProgramCard(490+10*i, 1, 0)); //move forward 1
        }
        
        for (int i=0; i<12; i++) {
            availableCards.push(new ProgramCard(670+10*i, 2, 0)); //move forward 2
        }
        
        for (int i=0; i<6; i++) {
            availableCards.push(new ProgramCard(10+10*i, 0, 2)); //rotate 180 (u-turn)
            availableCards.push(new ProgramCard(790+10*i, 3, 0)); //move forward 3
            availableCards.push(new ProgramCard(430+10*i, -1, 0)); //move backward 1 
        }
        
        shuffle();
    }

    @Override
    public ArrayList<ICard> draw(int n) {
    	if(n==1) System.out.println("dead");
//        if (n<1) throw new IllegalArgumentException("Cannot draw less than 1 card.");
        if (n>72) throw new IllegalArgumentException("Cannot draw more than 72 cards (8 players * 9 cards)");
        if (n>availableCards.size()) throw new IllegalArgumentException("Attempting to draw more cards than available");
        
        ArrayList<ICard> drawn = new ArrayList<>();
        for (int i=0; i<n; i++) {
            drawn.add(availableCards.pop());
        }

        return drawn;
    }

    @Override
    public void returnCards(ArrayList<ICard> cards) {
        for (ICard card : cards) {
            availableCards.push((ProgramCard)card);
        }
        
        shuffle();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(availableCards);
    }



    public ProgramCard[] getProgramCardsForTesting(){
        ProgramCard[] deck = new ProgramCard[availableCards.size()];
        int i = 0;
        while(!availableCards.isEmpty()){
            deck[i++] = (ProgramCard) availableCards.pop();
        }

        return deck;
    }

    @Override
    public int nRemainingCards() {
        return availableCards.size(); 
    }
}