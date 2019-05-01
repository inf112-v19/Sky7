package sky7.main;

import java.io.FileNotFoundException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import sky7.Client.Client;
import sky7.Client.IClient;
import sky7.gui.GUI;


public class Main {

    private static IClient client;

    public static void main(String[] args) {

        new Thread() {
            public void run() {
                client = new Client();
            }
        }.start();

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
    }

    public static class startGUI implements Runnable {

        @Override
        public void run() {
            // TODO Refactor cfg
            LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
            cfg.title = "Sky7 Games";
            // TODO Width and Height should adapt to the resolution of a screen.
            cfg.width = 1755;
//            cfg.height = 1536;
            cfg.height = 1200;
            cfg.foregroundFPS = 30;
            cfg.backgroundFPS = 30;

            new LwjglApplication(new GUI(), cfg);
        }
    }
}
