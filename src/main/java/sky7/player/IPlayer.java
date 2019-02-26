package sky7.player;

public interface IPlayer {
    
    void applyDamage(int damage);
    
    void repair(int damage);

    void lockCards(int n);

    void unlockCards(int n);

    void updateHealth(int x);
}
