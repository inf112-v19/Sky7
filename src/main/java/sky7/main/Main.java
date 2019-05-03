package sky7.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import sky7.gui.GUI;


public class Main {

    public static void main(String[] args) {

        startGUI g = new startGUI();
        Thread gui = new Thread(g);
        gui.start();
    }

    public static class startGUI implements Runnable {

        @Override
        public void run() {
            // TODO Refactor cfg
            LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
            cfg.title = "Sky7 Games";
            // TODO Width and Height should adapt to the resolution of a screen.
            cfg.width = 1366;
            cfg.height = 1536;
            cfg.foregroundFPS = 30;
            cfg.backgroundFPS = 30;

            new LwjglApplication(new GUI(), cfg);
        }
    }
}
