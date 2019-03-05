package sky7.gui;

import java.io.FileNotFoundException;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.*;

import sky7.board.ICell;
import sky7.card.IProgramCard;
import sky7.game.IClient;

public class GUI implements ApplicationListener {
	private IClient game;
	private int width, height;
	private SpriteBatch batch;
	private BitmapFont font;
	//abstract the name of textures.
	private HashMap<String, Texture> textures;
	private ExtendViewport viewport;
	private OrthographicCamera camera;
	private Sprite emptyCard;
	private Vector3 clickPos = new Vector3();
	TextureAtlas textureAtlas;
	private boolean cardsChoosen = true;

	public GUI(IClient game) throws FileNotFoundException {
		this.game = game;
		textures = new HashMap<>();
	}

	@Override
	public void create() {
		try {
			game.generateBoard();
			this.width = game.gameBoard().getWidth();
			this.height = game.gameBoard().getHeight();

			batch = new SpriteBatch();
			font = new BitmapFont();

			camera = new OrthographicCamera();
			viewport = new ExtendViewport(width * 128, height * 128, camera);

			textures.put("robot", new Texture("assets/robot1.png"));
			textures.put("floor", new Texture("assets/floor/plain.png"));
			textures.put("outline", new Texture("assets/cards/Outline.png"));
			textures.put("dock", new Texture("assets/dock.png"));
			textures.put("unmarkedCard", new Texture("assets/cards/EmptyCard.png"));

			textureAtlas = new TextureAtlas("assets/cards/Cards.txt");
			emptyCard = textureAtlas.createSprite("EmptyCard");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1); // Background Color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears every image from previous iteration of render.
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		// draw a grid of width*height, each square at 128*128 pixels
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (ICell cell : game.gameBoard().getTileTexture(i, j)) {
					batch.draw(cell.getTexture(), i * 128, (j + 2) * 128, 128, 128);
				}
			}
			/*String[] texturesRef = game.gameBoard().getTileTexture(i, j); // Which texture belongs at position i,j
                for (String tex : texturesRef) {
                    // batch.draw(textures.get(tex), i*128, j*128);

                    // need extra parameters (last 2 128s to scale each texture to 128x128 instead of original 300x300)
                    // (j+2) leaves space for dock
                    batch.draw(textures.get(tex), i * 128, (j + 2) * 128, 128, 128);

                }*/
		}

		Dock();
		chooseCards();
		emptyCard.draw(batch);

		if (Gdx.input.isTouched()){
			camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (clickPos.x > emptyCard.getX() && clickPos.x < emptyCard.getX() + emptyCard.getWidth()) {
				if (clickPos.y > emptyCard.getY() && clickPos.y < emptyCard.getY() + emptyCard.getHeight()) {
					emptyCard.setX(emptyCard.getX() + 128);
				}
			}
		}


		// draw 9 cards in dock
		//		for (int i=0; i<9; i++) {
		//		    batch.draw(textures.get("CardPlaceHolder"), 40+i*90, 64, 84, 128);
		//        }

		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	public void Dock() {
		for (int i = 0; i < width; i++) {
			//draw lower dock
			batch.draw(textures.get("dock"), i * 128, 0);
			//draw higher dock
			batch.draw(textures.get("dock"), i * 128, 128);
		}
		//draw outline of 5 selected cards
		for (int i = 0; i < 5; i++) {
			batch.draw(textures.get("outline"), i * 128, 128);
		}
	}
	public void chooseCards() {
//		if (cardsChoosen) {	
//			String[] chosen = new String[5];
//			//			font.draw(batch, chosen, 200, 200);
//
////			String chosen = game.getRegistry();
////			font.draw(batch, chosen, 200, 200);
////
////			IProgramCard[] hand = game.getHand();
////			for (int i=0; i<9; i++) {
//				//			batch.draw(hand[i], i*128, 0);
//			}
//		}
	}
}
