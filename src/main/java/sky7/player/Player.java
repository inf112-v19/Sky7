package sky7.player;

import sky7.dock.Dock;
import sky7.dock.IDock;

public class Player implements IPlayer {
    
    private IDock dock;
    private int health = 10;
    private int lifeTokens = 3;
    
    public Player() {
        dock = new Dock();
    }

    @Override
    public void applyDamage(int damage) {
        health -= damage;
        dock.updateHealth(health);
        
        if (health < 6) {
            dock.lockCards(damage);
        }
        
        // if health reaches 0, consume a life token and respawn
    }

    @Override
    public void repair(int damage) {
        if (health < 6) {
            dock.unlockCards(Math.min(damage, 6-health));
        }
        
        health = Math.min(10, health + damage);
        dock.updateHealth(health);
        
        
    }
    
    
    
    

}
