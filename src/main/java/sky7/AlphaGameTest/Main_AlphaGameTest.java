package sky7.AlphaGameTest;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main_AlphaGameTest {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "hello-world";
        cfg.width = 1280;
        cfg.height = 1024;

        new LwjglApplication(new AlphaGameTest(), cfg);
    }
}

