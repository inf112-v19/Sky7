package sky7.gui;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sky7.game.IGame;

public class GUI implements ApplicationListener {
	private IGame game;
	private int width, height;
	private SpriteBatch batch;
	private HashMap<String, Texture> textures;
	private Viewport viewport;
	private OrthographicCamera camera;

	public GUI(IGame game) {
		this.game = game;
		this.width = game.gameBoard().getWidth();
		this.height = game.gameBoard().getHeight();



		textures = new HashMap<>();
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		textures.put("robot", new Texture("assets/robot1.png"));
		textures.put("floor", new Texture("assets/floor/plain.png"));
		textures.put("CardPlaceHolder", new Texture("assets/cards/CardPlaceHolder.png"));
		camera = new OrthographicCamera(width, height);
		viewport = new FitViewport(width, height, camera);
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		// draw a grid of width*height, each square at 128*128 pixels
		for (int i = 0; i<width; i++) {
			for(int j=0; j<height; j++) {
				String[] texturesRef = game.gameBoard().getTileTexture(i, j);
				for(String tex : texturesRef) {
					// batch.draw(textures.get(tex), i*128, j*128);
					
					// need extra parameters (last 2 128s to scale each texture to 128x128 instead of original 300x300)
				    // (j+2) leaves space for dock
					batch.draw(textures.get(tex), i*128, (j+2)*128, 128, 128);
				}
			}
		}
		
		// draw 9 cards in dock 
		for (int i=0; i<9; i++) {
		    batch.draw(textures.get("CardPlaceHolder"), 40+i*90, 64, 84, 128);
        }
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		viewport.update(width, height, true);

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}
