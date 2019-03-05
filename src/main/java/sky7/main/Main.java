package sky7.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import sky7.game.Client;
import sky7.game.IClient;
import sky7.gui.GUI;
import sky7.host.Host;
import sky7.host.IHost;

import java.io.FileNotFoundException;

public class Main {
    
    private static IClient cli;

    public static void main(String[] args) {
        
        clientThread cThread = new clientThread();
        Thread client = new Thread(cThread);
        client.start();
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        startGUI g = new startGUI();
        Thread gui = new Thread(g);
        gui.start();
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        startHost h = new startHost();
        Thread host = new Thread(h);
        host.start();
    }
    
    public static class clientThread implements Runnable {

        @Override
        public void run() {
            cli = new Client();
        }
        
    }
    
    public static class startGUI implements Runnable {

        @Override
        public void run() {
            // TODO Refactor cfg
            LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
            cfg.title = "Sky7 Games";
            // TODO Width and Height should adapt to the resolution of a screen.
            cfg.width = 1280;
            cfg.height = 1024;

            try {
                new LwjglApplication(new GUI(cli), cfg);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
     }
    
    public static class startHost implements Runnable {

        @Override
        public void run() {
            
            new Host(cli);
        }
    }
}
