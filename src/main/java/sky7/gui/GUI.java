package sky7.gui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GUI implements ApplicationListener {
    
    private SpriteBatch batch;
//    private BitmapFont font;
    private Texture floor;
    
    public GUI() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        floor = new Texture("assets/Floor.jpg");
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        for (int i = 0; i<=1024; i+=128) {
            for(int j=0; j<=1024; j+=128) {
                batch.draw(floor, i, j);
            }
        }
        batch.end();
    }

    @Override
    public void resize(int arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

}
