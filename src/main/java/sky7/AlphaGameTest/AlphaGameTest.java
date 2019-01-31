package sky7.AlphaGameTest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AlphaGameTest extends ApplicationAdapter{
	SpriteBatch batch;
	Texture floor;

	@Override
	public void create() {
		batch = new SpriteBatch();
		floor = new Texture("Floor.jpg");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
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
	public void dispose() {
		batch.dispose();
		floor.dispose();
	}
}
