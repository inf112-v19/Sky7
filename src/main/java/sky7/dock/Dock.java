package sky7.dock;

import java.util.ArrayList;

import sky7.card.IProgramCard;

public class Dock implements IDock {

    ArrayList<IProgramCard> hand;
    IProgramCard[] registry;
    boolean[] locked = new boolean[5];
    int nLocked = 0;
    int health = 10;
    
    public Dock() {
        hand = new ArrayList<>();
        registry = new IProgramCard[5];
    }

    @Override
    public void lockCards(int n) {
        for (int i=nLocked; i<nLocked+n; i++) {
            locked[i] = true;
        }
        nLocked += n;
    }

    @Override
    public void unlockCards(int n) {
        for (int i=nLocked-1; i>=nLocked-n; i--) {
            locked[i] = false;
        }
        nLocked -= n;
    }

    @Override
    public void updateHealth(int x) {
        health = x;
    }

}
