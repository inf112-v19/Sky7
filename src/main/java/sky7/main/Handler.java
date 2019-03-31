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

public class Handler {

    private LwjglApplication startScreen;
    private IClient client;
    
    public Handler() {
        startSplashScreen();
    }
    
    public void startSplashScreen() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                LwjglApplicationConfiguration menu = new LwjglApplicationConfiguration();
                menu.title = "Sky7 Games";
                // TODO Width and Height should adapt to the resolution of a screen.
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                menu.height = screenSize.height-400;
                menu.width = (int) (menu.height*1.141928); // aspect ratio of splashScreen image is 1.141928
                menu.foregroundFPS = 30;
                menu.backgroundFPS = 30;
                menu.disableAudio = true;
                startScreen = new LwjglApplication(new splashScreen(Handler.this), menu);
            }
        }).start();
    }
    
    public void startGameAsHost() throws InterruptedException {
        startScreen.exit();
        Thread.sleep(500);
        startClient();
        Thread.sleep(500);
        startGUI();
        Thread.sleep(500);
        startHost();
    }

    private void startHost() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                new Host(client);
            }
        }).start();
    }

    private void startGUI() {
        
        
        new Thread(new Runnable(){

            @Override
            public void run() {
                LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
                cfg.title = "Sky7 Games";
                // TODO Width and Height should adapt to the resolution of a screen.
                cfg.width = 1280;
                cfg.height = 1024;
                cfg.foregroundFPS = 30;
                cfg.backgroundFPS = 30;
                try {
                    new LwjglApplication(new GUI(client), cfg);
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }

    private void startClient() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                client = new Client();
            }
        }).start();
    }

}
