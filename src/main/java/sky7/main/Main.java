package sky7.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import sky7.game.Game;
import sky7.game.IGame;
import sky7.gui.GUI;

public class Main {
    
    private static IGame game;

    public static void main(String[] args) {
        
        gameEngine g = new gameEngine();
        Thread engine = new Thread(g);
        engine.start();
        
        startGUI x = new startGUI();
        Thread gui = new Thread(x);
        gui.start();
    }
    
    public static class gameEngine implements Runnable {

        @Override
        public void run() {
            game = new Game();
        }
        
    }
    
    public static class startGUI implements Runnable {

        @Override
        public void run() {
            LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
            cfg.title = "hello-world";
            cfg.width = 1280;
            cfg.height = 1024;
            
            new LwjglApplication(new GUI(game), cfg);
        }
     }
}
