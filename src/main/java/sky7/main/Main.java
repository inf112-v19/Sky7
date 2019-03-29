package sky7.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import sky7.game.Client;
import sky7.game.IClient;
import sky7.gui.GUI;
import sky7.gui.splashScreen;
import sky7.host.Host;

public class Main {
    
    private static IClient cli;

    public static void main(String[] args) {
        
        // uncomment to start splashScreen, comment out all below
        // TODO add buttons to  menu, so we can start client, host etc from there
        
//        new Thread(new Runnable(){
//
//            @Override
//            public void run() {
//                LwjglApplicationConfiguration menu = new LwjglApplicationConfiguration();
//                menu.title = "Sky7 Games";
//                // TODO Width and Height should adapt to the resolution of a screen.
//                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//                menu.height = screenSize.height-400;
//                menu.width = (int) (menu.height*1.141928); // aspect ratio of splashScreen image is 1.141928
//                menu.foregroundFPS = 30;
//                menu.backgroundFPS = 30;
//                new LwjglApplication(new splashScreen(), menu);
//            }
//        }).start();
        
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
            cfg.foregroundFPS = 30;
            cfg.backgroundFPS = 30;
            
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
            Host host = new Host(cli);

            host.Begin();

        }
    }
}
