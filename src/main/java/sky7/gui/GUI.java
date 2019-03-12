package sky7.gui;

import java.io.FileNotFoundException;
import java.util.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	private TextureAtlas textureAtlas;
	private Sprite reset, confirm;

	private boolean cardsChoosen = false;
	private int pointer, yPos, cardXpos = 0;
	private int scaler = 128;
	//	private ICard[] chosenCards = new ICard[5];
	private ArrayList<ICard> hand;
	private ArrayList<ICard> currentHand = new ArrayList<>(4);

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

			font.getData().setScale(2, 2);
			font.setColor(Color.GOLDENROD);

			camera = new OrthographicCamera();
			viewport = new ExtendViewport(width * scaler, (height+2) * scaler, camera);

			textures.put("robot", new Texture("assets/robot1.png"));
			textures.put("floor", new Texture("assets/floor/plain.png"));
			textures.put("outline", new Texture("assets/cards/Outline.png"));
			textures.put("dock", new Texture("assets/dock.png"));
			textures.put("unmarkedCard", new Texture("assets/cards/EmptyCard.png"));
			textures.put("reset", new Texture("assets/dock/Reset.png"));
			textures.put("confirm", new Texture("assets/dock/Confirm.png"));

			textureAtlas = new TextureAtlas("assets/cards/Cards.txt");

			reset = new Sprite(textures.get("reset"));
			confirm = new Sprite(textures.get("confirm"));
			reset.setPosition(scaler*9, 20);
			confirm.setPosition(scaler*10, 20);

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
					batch.draw(cell.getTexture(), i * scaler, (j + 2) * scaler, scaler, scaler);
				}
			}
		}

		Dock();
		chooseCards();
		showcurrenthand();
		if(!cardsChoosen) {
			reset.draw(batch);
			if (isClicked(reset)) {
				reset();
			}
		}

		if (pointer == 5) {
			confirm.draw(batch);
			if (isClicked(confirm)) {
				playerCards();
			}
		}
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
			batch.draw(textures.get("dock"), i * scaler, 0);
			batch.draw(textures.get("dock"), i * scaler, scaler);
		}
		for (int i = 0; i < 5; i++) {
			batch.draw(textures.get("outline"), i * scaler, scaler);
		}
	}

	// check if user has chosen all 5 cards
	// and put the chosen cards in the game-client, and lock the registry
	public void playerCards() {
		if (currentHand.get(4) != null) {
			cardsChoosen = true;
			for (int i=0; i<currentHand.size(); i++) {
				game.setCard(currentHand.get(i), i);
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
		for (ICard card : hand) {
			card.setX(cardXpos);
			card.setY(0);
			cardXpos+=scaler;
		}	
	}

	public void chooseCards() {
		if(!hand.equals(game.getHand())) {
			hand = game.getHand();
			reset();
		}
		for (ICard card : hand) {
			drawSprite(card.GetSpriteRef(), card.getX(), card.getY());
			font.draw(batch, card.getPriority(), card.getX()+42, card.getY()+93);
		}
		if (!cardsChoosen && pointer != 5) {
			//check if card is clicked
			if (Gdx.input.justTouched()) {
				camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				for(ICard card : hand) {
					if(clickPos.x <= scaler+card.getX() && clickPos.x > card.getX() && clickPos.y <= scaler) {
						if (card.getY() != scaler) {
							currentHand.add(card);
							pointer++;
							System.out.println(pointer + " card(s) choosen " + card.GetSpriteRef() + " \tPriority: \t" + card.getPriority());
							// just move them outside the map for now lol
							card.setY(23+scaler);
						}
					}
				}
			}
		}
	}

	public void showcurrenthand() {
		for (ICard currentCards : currentHand) {
			drawSprite(currentCards.GetSpriteRef(), currentCards.getX(), currentCards.getY());
			font.draw(batch, currentCards.getPriority(), currentCards.getX()+42, currentCards.getY()+93);
		}
		if (!cardsChoosen && pointer != 6) {
			for (ICard card : currentHand) {
				if (card.getY() != scaler) {
					card.setX(yPos);
					card.setY(scaler);
					yPos += scaler;
				}
			}
		}
	}
	//reset chosen cards, reset position etc
	public void reset() {
		System.out.println("----------- Resetting Cards -----------");
		cardsChoosen = false;
		pointer = 0;
		yPos = 0;
		cardXpos = 0;
		currentHand.clear();
		hand = game.getHand();
		for (ICard card : hand) {
			System.out.print(card.GetSpriteRef() + " Priority: " + card.getPriority() + " \t" );
		}
		initiateCards(hand);
		initiateCards(currentHand);
		showcurrenthand();
		chooseCards();
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
