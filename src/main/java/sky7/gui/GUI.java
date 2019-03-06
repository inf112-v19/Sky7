package sky7.gui;

import java.io.FileNotFoundException;
import java.util.*;
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
	private Vector3 clickPos = new Vector3();
	TextureAtlas textureAtlas;

	private boolean cardsChoosen = false;
	private int pointer = 0;
	private int yPos = 0;
	private ICard[] chosenCards = new ICard[5];
	private ArrayList<ICard> hand;

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

			hand = game.getHand();
			addSprites();
			initiateCards(hand);
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
		playerCards();

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

	}

	public void Dock() {
		for (int i = 0; i < width; i++) {
			batch.draw(textures.get("dock"), i * 128, 0);
			batch.draw(textures.get("dock"), i * 128, 128);
		}
		for (int i = 0; i < 5; i++) {
			batch.draw(textures.get("outline"), i * 128, 128);
		}
	}
	
	// check if user has chosen all 5 cards
	// and put the chosen cards in the game-client, and lock the registry
	public void playerCards() {
		if (chosenCards[4] != null) {
			cardsChoosen = true;

			for (int i=0; i<chosenCards.length; i++) {
				game.setCard(chosenCards[i], i);
			}
			game.lockRegistry();
		}
	}

	// add sprites to a texturemap
	public void addSprites() {
		Array<AtlasRegion> regions = textureAtlas.getRegions();
		for (AtlasRegion region : regions) {
			Sprite sprite = textureAtlas.createSprite(region.name);
			sprites.put(region.name, sprite);
		}
	}

	// draw sprites
	public void drawSprite(String name, float x, float y) {
		Sprite sprite = sprites.get(name);
		sprite.setPosition(x, y);
		sprite.draw(batch);
	}

	// set the x position for the cards to spread them accross the map
	private void initiateCards(ArrayList<ICard> hand) {
		int cardX = 0;
		for (ICard card : hand) {
			card.setX(cardX);
			cardX+=128;
		}	
	}

	public void chooseCards() {
		if (!cardsChoosen) {
			ICard card0 = hand.get(0);
			drawSprite(card0.GetSpriteRef(), card0.getX(), card0.getY());
			ICard card1 = hand.get(1);
			drawSprite(card1.GetSpriteRef(), card1.getX(), card1.getY());
			ICard card2 = hand.get(2);
			drawSprite(card2.GetSpriteRef(), card2.getX(), card2.getY());
			ICard card3 = hand.get(3);
			drawSprite(card3.GetSpriteRef(), card3.getX(), card3.getY());
			ICard card4 = hand.get(4);
			drawSprite(card4.GetSpriteRef(), card4.getX(), card4.getY());
			ICard card5 = hand.get(5);
			drawSprite(card5.GetSpriteRef(), card5.getX(), card5.getY());
			ICard card6 = hand.get(6);
			drawSprite(card6.GetSpriteRef(), card6.getX(), card6.getY());
			ICard card7 = hand.get(7);
			drawSprite(card7.GetSpriteRef(), card7.getX(), card7.getY());
			ICard card8 = hand.get(8);
			drawSprite(card8.GetSpriteRef(), card8.getX(), card8.getY());

			if (Gdx.input.justTouched()) {
				camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				if(clickPos.x <= 128 && clickPos.y <= 128) {
					if (card0.getY() != 128) {
						chosenCards[pointer] = card0;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card0.GetSpriteRef());
						card0.setX(yPos);
						card0.setY(128);
						yPos += 128;
					}
				}
				if(clickPos.x <= 2*128 && clickPos.y <= 128 && clickPos.x > 128) {
					if (card1.getY() != 128) {
						chosenCards[pointer] = card1;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card1.GetSpriteRef());
						card1.setX(yPos);
						card1.setY(128);
						yPos += 128;
					}
				}
				if(clickPos.x <= 3*128 && clickPos.y <= 128 && clickPos.x > 128*2) {
					if (card2.getY() != 128) {
						chosenCards[pointer] = card1;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card2.GetSpriteRef());
						card2.setX(yPos);
						card2.setY(128);
						yPos += 128;
					}
				}
				if(clickPos.x <= 4*128 && clickPos.y <= 128 && clickPos.x > 128*3) {
					if (card3.getY() != 128) {
						chosenCards[pointer] = card3;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card3.GetSpriteRef());
						card3.setX(yPos);
						card3.setY(128);
						yPos += 128;
					}
				}
				if(clickPos.x <= 5*128 && clickPos.y <= 128 && clickPos.x > 128*4) {
					if (card4.getY() != 128) {
						chosenCards[pointer] = card4;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card4.GetSpriteRef());
						card4.setX(yPos);
						card4.setY(128);
						yPos += 128;
					}
				}
				if(clickPos.x <= 6*128 && clickPos.y <= 128 && clickPos.x > 128*5) {
					if (card5.getY() != 128) {
						chosenCards[pointer] = card5;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card5.GetSpriteRef());
						card5.setX(yPos);
						card5.setY(128);
						yPos += 128;
					}
				}
				if(clickPos.x <= 7*128 && clickPos.y <= 128 && clickPos.x > 128*6) {
					if (card6.getY() != 128) {
						chosenCards[pointer] = card6;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card6.GetSpriteRef());
						card6.setX(yPos);
						card6.setY(128);
						yPos += 128;
					}
				}
				if(clickPos.x <= 8*128 && clickPos.y <= 128 && clickPos.x > 128*7) {
					if (card7.getY() != 128) {
						chosenCards[pointer] = card7;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card7.GetSpriteRef());
						card7.setX(yPos);
						card7.setY(128);
						yPos += 128;
					}
				}
				if(clickPos.x <= 9*128 && clickPos.y <= 128 && clickPos.x > 128*8) {
					if (card8.getY() != 128) {
						chosenCards[pointer] = card8;
						pointer++;
						System.out.println(pointer + " card(s) choosen " + card8.GetSpriteRef());
						card8.setX(yPos);
						card8.setY(128);
						yPos += 128;
					}
				}
			}
		}
	}
}
