package sky7.gui;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import sky7.game.IGame;

public class GUI implements ApplicationListener {
    private IGame game;
    private int width, height;
    private SpriteBatch batch;
    private HashMap<String, Texture> textures;
    
    public GUI(IGame game) {
        this.game = game;
        this.width = game.gameBoard().getWidth();
        this.height = game.gameBoard().getHeight();
        
       
        
        textures = new HashMap<>();
//        loadTextures();
    }

    private void loadTextures() {
        textures.put("robot", new Texture("assets/robot1.png"));
        textures.put("floor", new Texture("assets/Floor.jpg"));
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        Texture temp = new Texture("assets/robot1.png");
        textures.put("robot", new Texture("assets/robot1.png"));
        textures.put("floor", new Texture("assets/Floor.jpg"));
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
                String[] texturesRef = game.gameBoard().getTileTexture(i, j);
                for(String tex : texturesRef) {
                    batch.draw(textures.get(tex), i*128, j*128);
                }
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
