package sky7.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sky7.gui.GUI;

public class Main {

    public static void main(String[] args) {
        
        startGUI x = new startGUI();
        
        Thread gui = new Thread(x);
        
        gui.start();
    }
    
    public static class startGUI implements Runnable {

        @Override
        public void run() {
            LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
            cfg.title = "hello-world";
            cfg.width = 1280;
            cfg.height = 1024;
            
            new LwjglApplication(new GUI(), cfg);
        }
     }
}
