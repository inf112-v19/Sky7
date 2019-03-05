package sky7.gui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;

import sky7.board.ICell;
import sky7.card.ICard;
import sky7.card.IProgramCard;
import sky7.card.ProgramCard;
import sky7.game.IClient;

public class GUI implements ApplicationListener {
	private IClient game;
	private int width, height;
	private SpriteBatch batch;
	private BitmapFont font;
	//abstract the name of textures.
	private HashMap<String, Texture> textures;
	private HashMap<String, Sprite> sprites;
	private ExtendViewport viewport;
	private OrthographicCamera camera;
//	private Sprite emptyCard, Move1, Move2, Move3, MoveBack, RotateLeft, RotateRight, uTurn;
	private Vector3 clickPos = new Vector3();
	TextureAtlas textureAtlas;
	private boolean cardsChoosen = true;

	public GUI(IClient game) throws FileNotFoundException {
		this.game = game;
		textures = new HashMap<>();
		sprites = new HashMap<>();
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

//			emptyCard = textureAtlas.createSprite("EmptyCard");
			addSprites();
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		textureAtlas.dispose();
		sprites.clear();
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

		//		emptyCard.draw(batch);
		//		
		//		if (Gdx.input.isTouched()){
		//			camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		//			if (clickPos.x > emptyCard.getX() && clickPos.x < emptyCard.getX() + emptyCard.getWidth()) {
		//				if (clickPos.y > emptyCard.getY() && clickPos.y < emptyCard.getY() + emptyCard.getHeight()) {
		//					emptyCard.setX(emptyCard.getX() + 128);
		//				}
		//			}
		//		}

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
		if (cardsChoosen) {	
			String[] chosenCards = new String[5];

			ArrayList<ICard> hand = game.getHand();

//			for (int i=0; i<hand.size(); i++) {
//				System.out.println(hand.get(i).GetSpriteRef());
//			}
			ICard card0 = hand.get(0);
			drawSprite(card0.GetSpriteRef(), 0, 0);
			
			ICard card1 = hand.get(1);
			drawSprite(card1.GetSpriteRef(), 1*128, 0);
			
			ICard card2 = hand.get(2);
			drawSprite(card2.GetSpriteRef(), 2*128, 0);
			
			ICard card3 = hand.get(3);
			drawSprite(card3.GetSpriteRef(), 3*128, 0);
			ICard card4 = hand.get(4);
			drawSprite(card4.GetSpriteRef(), 4*128, 0);
			ICard card5 = hand.get(5);
			drawSprite(card5.GetSpriteRef(), 5*128, 0);
			ICard card6 = hand.get(6);
			drawSprite(card6.GetSpriteRef(), 6*128, 0);
			ICard card7 = hand.get(7);
			drawSprite(card7.GetSpriteRef(), 7*128, 0);
			ICard card8 = hand.get(8);
			drawSprite(card8.GetSpriteRef(), 8*128, 0);
			
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//				cardsChoosen = false;
			}
//			game.setCard(chosenCard, positionInRegistry);
		}
	}
	
	public void playerCards() {
		// TODO: print string[] chosenCards
	}

	public void addSprites() {
		Array<AtlasRegion> regions = textureAtlas.getRegions();
		for (AtlasRegion region : regions) {
			Sprite sprite = textureAtlas.createSprite(region.name);
			sprites.put(region.name, sprite);
		}
	}

	public void drawSprite(String name, float x, float y) {
		Sprite sprite = sprites.get(name);
		sprite.setPosition(x, y);
		sprite.draw(batch);
	}
}
