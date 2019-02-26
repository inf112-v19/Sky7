package sky7.player;

import sky7.card.IProgramCard;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Player implements IPlayer {

    private int health = 10;
    private int lifeTokens = 3;
    private IProgramCard[] hand;
    private IProgramCard[] registry;
    private boolean[] locked = new boolean[5];
    private int nLocked = 0;


    public Player() {
        hand = new IProgramCard[9];
        registry = new IProgramCard[5];

    }

    @Override
    public void applyDamage(int damage) {
        health -= damage;
        updateHealth(health);

        if (health < 6) {
            lockCards(damage);
        }

        // if health reaches 0, consume a life token and respawn
    }

    @Override
    public void repair(int damage) {
        if (health < 6) {
            unlockCards(Math.min(damage, 6 - health));
        }

        health = Math.min(10, health + damage);
        updateHealth(health);


    }

    @Override
    public void lockCards(int n) {
        for (int i = nLocked; i < nLocked + n; i++) {
            locked[i] = true;
        }
        nLocked += n;
    }

    @Override
    public void unlockCards(int n) {
        for (int i = nLocked - 1; i >= nLocked - n; i--) {
            locked[i] = false;
        }
        nLocked -= n;
    }

    @Override
    public void updateHealth(int x) {
        health = x;
    }

    @Override
    public IProgramCard[] getRegistry() {
        return registry;
    }

    @Override
    public void setHand(IProgramCard[] programCards) {
       hand = programCards;
    }

    @Override
    public void setRegistry(IProgramCard[] chosenCards) {
        registry = chosenCards;
    }

    @Override
    public IProgramCard[] getHand() {
        return hand;
    }


}
