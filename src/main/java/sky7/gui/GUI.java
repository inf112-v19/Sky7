package sky7.gui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import sky7.game.IGame;

public class GUI implements ApplicationListener {
    private IGame game;
    private int width, height;
    private SpriteBatch batch;
//    private BitmapFont font;
    private Texture txtr;
    
    public GUI(IGame game) {
        this.game = game;
        this.width = game.gameBoard().getWidth();
        this.height = game.gameBoard().getHeight();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
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
        
        // draw a grid of width*height, each square at 128*128 pixels
        for (int i = 0; i<width; i++) {
            for(int j=0; j<height; j++) {
                txtr = new Texture(game.gameBoard().getTileTexture(i, j));
                batch.draw(txtr, i*128, j*128);
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
