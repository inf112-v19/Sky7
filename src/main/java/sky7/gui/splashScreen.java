package sky7.gui;

import java.util.HashMap;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class splashScreen implements ApplicationListener {
	private SpriteBatch batch;
	private ExtendViewport viewport;
	private OrthographicCamera camera;
	private Vector3 clickPos = new Vector3();
	private HashMap<String, Texture> textures;
	private int windowWidth, windowHeight;
	private Stage stage;
	private Sprite spr;

	public splashScreen() {
		textures = new HashMap<>();
	}

	public class Background extends Actor {
		public void draw(Batch batch, float alpha){
			batch.draw(textures.get("splashScreen"),0,0, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		}
	}

	public class Button extends Actor {
		public void draw(Batch batch, float alpha) {
			spr.draw(batch, alpha);
			if (isClicked(spr)) {
				//TODO: initiate GUI/ the game
				System.out.println("You pressed me!");
			}
		}
	}


	@Override
	public void create() {
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getWidth();

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(windowWidth, windowHeight, camera);

		textures.put("splashScreen", new Texture("assets/menu/splashScreen.png"));
		textures.put("button", new Texture("assets/dock/Confirm.png"));

		spr = new Sprite(textures.get("button"));
		spr.setX(6*128);
		spr.setY(6*128);
		
		stage = new Stage(viewport);
		Background myActor = new Background();
		Button myButton = new Button();

		stage.addActor(myActor);
		stage.addActor(myButton);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1); // Background Color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears every image from previous iteration of render.
		batch.setProjectionMatrix(camera.combined);

		stage.draw();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {	
		stage.dispose();
	}
	
	public boolean isClicked(Sprite sprite) {
		if (Gdx.input.justTouched()){
			camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (clickPos.x > sprite.getX() && clickPos.x < sprite.getX() + sprite.getWidth()) {
				if (clickPos.y > sprite.getY() && clickPos.y < sprite.getY() + sprite.getHeight()) {
					return true;
				}
			}
		}
		return false;
	}
	
}
