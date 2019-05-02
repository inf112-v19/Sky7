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
import sky7.Client.Client;
import sky7.card.ICard;
import sky7.host.Host;
import sky7.Client.IClient;


public class GUI implements ApplicationListener {
	private IClient client;
	private int width, height, windowWidth, windowHeight;
	private SpriteBatch batch;
	private BitmapFont font;
	private Host h;

	//abstract the name of textures.
	private HashMap<String, Texture> textures;
	private HashMap<String, Sprite> sprites;
	private ExtendViewport viewport;
	private OrthographicCamera camera;
	private Vector3 clickPos = new Vector3();
	private TextureAtlas textureAtlas;
	private Sprite reset, confirm, host, join, powerdown, wait;

	private boolean cardsChoosen, hostLobby = false, clientLobby = false, mainMenu = true;

	private int cardXpos = 0;
	private int pointer = 0;
	private int yPos = 64;
	private int scaler = 128;

	private ArrayList<ICard> hand;
	private ArrayList<ICard> localregistry = new ArrayList<>();
	TextInput listener;
	BackGround background;
	BoardPrinter boardprinter;

	public GUI(){
		textures = new HashMap<>();
		sprites = new HashMap<>();
	}

	public GUI(IClient client) throws FileNotFoundException {
		this.client = client;
		textures = new HashMap<>();
		sprites = new HashMap<>();
	}

	@Override
	public void create() {
		try {
			width = 12;
			height = 16;
			windowWidth = width + 4;
			windowHeight = height + 2;
			batch = new SpriteBatch();
			font = new BitmapFont();
			font.getData().setScale(2, 2);
			font.setColor(Color.GOLDENROD);
			camera = new OrthographicCamera();
			viewport = new ExtendViewport(windowWidth * scaler, windowHeight * scaler, camera);
			textures.put("floor", new Texture("assets/floor/plain.png"));
			textures.put("outline", new Texture("assets/cards/Outline.png"));
			textures.put("dock", new Texture("assets/dock.png"));
			textures.put("reset", new Texture("assets/menu/Reset2.png"));
			textures.put("Go", new Texture("assets/menu/Go2.png"));
			textures.put("health", new Texture("assets/health.png"));
			textures.put("Splashscreen", new Texture("assets/menu/splashscreen.png"));
			textures.put("Host", new Texture("assets/menu/Host2.png"));
			textures.put("Join", new Texture("assets/menu/Join2.png"));
			textures.put("PowerDown", new Texture("assets/menu/PowerDown2.png"));
			textures.put("Begin", new Texture("assets/menu/Begin.png"));
			textureAtlas = new TextureAtlas("assets/cards/Cards.txt");
			reset = new Sprite(textures.get("reset"));
			reset.setPosition(scaler * 4, scaler + 32);
			confirm = new Sprite(textures.get("Go"));
			confirm.setPosition(scaler * 11, scaler + 32);
			host = new Sprite(textures.get("Host"));
			host.setPosition(scaler * 9, scaler * 7);
			join = new Sprite(textures.get("Join"));
			join.setPosition(scaler * 5, scaler * 7);
			powerdown = new Sprite(textures.get("PowerDown"));
			powerdown.setPosition(scaler * 13, 32);
			wait = new Sprite(textures.get("Begin"));
			wait.setPosition(scaler * 7, scaler * 7);
			addSprites();
			listener = new TextInput(this);
			background = new BackGround(windowWidth, windowHeight, scaler, textures, batch);

		} catch (Exception e) {
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

		if (mainMenu) {
			batch.draw(textures.get("Splashscreen"), 0, 0, windowWidth * scaler, windowHeight * scaler);
			host.draw(batch);
			join.draw(batch);

			if (isClicked(host)) {
				mainMenu = false;
				startHost();
			}

			if (isClicked(join)) {
				// take input from user
				Gdx.input.getTextInput(listener, "Enter Host IP", "", "Enter IP here");
			}

		} else if (hostLobby) {
			batch.draw(textures.get("Splashscreen"), 0, 0, windowWidth * scaler, windowHeight * scaler);
			wait.draw(batch);
			font.draw(batch, h.getnPlayers() + " Connected Players", 7 * scaler, 6 * scaler);

			if (isClicked(wait)) {
				hostLobby = false;
				new Thread() {
					public void run() {
						h.Begin();
					}
				}.start();
				this.width = client.gameBoard().getWidth();
				this.height = client.gameBoard().getHeight();
			}

		} else if (clientLobby) {
			batch.draw(textures.get("Splashscreen"), 0, 0, windowWidth * scaler, windowHeight * scaler);
			font.draw(batch, client.getNPlayers() + " Connected Players", 7 * scaler, 6 * scaler);
			if (client.readyToRender()) {
				initiateClient();
				clientLobby = false;
			}
		} else {
			background.showDock(); //Render background and registry slots
			boardprinter.showBoard(client);
			showHealth(); //Render health of player
			chooseCards(); //Render 9 selectable cards
			showRegistry();

			/*
			 * render reset button only if at least one card is selected and
			 * when the player has not pressed the "ready" button
			 */
			if (!cardsChoosen && pointer > client.getPlayer().getNLocked()) {
				reset.draw(batch);
				if (isClicked(reset)) {
					reset();
				}
			}

			// Render "GO" button only if 5 cards are choosen and player has taken less than 9 damage
			if (localregistry.size() == 5 && Integer.parseInt((String) client.getPlayer().getDamage()) <= 9) {
				confirm.draw(batch);

				// if confirm is clicked:
				if (isClicked(confirm)) {
					cardsChoosen = true;
					setRegistry();
					//					setRegistry();
					//					pointer = client.getPlayer().getNLocked();
				}
			}

			powerdown.draw(batch);
			if (isClicked(powerdown)) {
				System.out.println("Powering down next round");
				client.powerDown();
			}

		}
		batch.end();
	}

	public void showRegistry() {
		for (ICard currentCards : localregistry) {
			if (currentCards.getY() != scaler) {
				currentCards.setY(scaler);
				currentCards.setX((scaler * 5) + yPos);
				yPos += scaler;
			}
			drawSprite(currentCards.GetSpriteRef(), currentCards.getX(), currentCards.getY());
			font.draw(batch, currentCards.getPriority(), currentCards.getX() + 42, currentCards.getY() + 93);
		}
	}

	@Override
	public void resize(int width, int height) {
		//		viewport.update(width, height), true);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void resume() {
	}

	/**
	 * pick which cards you want to use
	 */
	public void chooseCards() {
		// check if the current hand is not the same as the hand in Client
		if (!hand.equals(client.getHand())) {
			reset();
		}
		// if GO is not pressed, draw available cards
		if (!cardsChoosen) {
			for (ICard card : hand) {
				drawSprite(card.GetSpriteRef(), card.getX(), card.getY());
				font.draw(batch, card.getPriority(), card.getX() + 42, card.getY() + 93);
			}
		}
		if (!cardsChoosen && localregistry.size() < 5) {

			//check if card is clicked
			if (Gdx.input.justTouched()) {
				camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));

				// go through cards in hand and see if the clickposition is the same as a cards position
				for (ICard card : hand) {
					if (clickPos.x <= scaler + card.getX() && clickPos.x > card.getX() && clickPos.y <= scaler) {
						if (card.getY() != scaler) {
							localregistry.add(card);
							System.out.println(pointer + " card(s) choosen " + card.GetSpriteRef() + " \tPriority: \t" + card.getPriority());
							card.setY(-scaler);
							pointer++;
						}
					}
				}
			}
		}
	}

	public void setRegistry() {
		//check if there actually are 5 chosen cards
		cardsChoosen = true;
		for (int i = 0; i < localregistry.size(); i++) {
			client.setCard(localregistry.get(i), i);
		}
		client.lockRegistry();
	}

	public void reset() {
		System.out.println("\n----------- Resetting -----------");
		cardsChoosen = false;
		yPos = 64;
		cardXpos = 0;
		pointer = client.getPlayer().getNLocked();
		
		localregistry = (ArrayList<ICard>) client.getPlayer().getRegistry().clone();

		hand = client.getHand();

		//Print out new cards
		for (ICard card : hand) {
			System.out.print(card.GetSpriteRef() + " Priority: " + card.getPriority() + " \t");
		}
		
		//Print out players current registry
		System.out.println("\n\nCurrent registry: ");
		for (ICard card : localregistry) {
			System.out.print(card.GetSpriteRef() + " Priority: " + card.getPriority() + " \t");
		}

		System.out.println("\n\nPointer/Locked: \t" + pointer + "\nPlayer Registry size: \t" + localregistry.size());
		
		resetcardpos(localregistry);
		
		if (localregistry.size() <= 5) {
			leftshift(localregistry);
		}
		
		setHandPos(hand);
		chooseCards();
		cardXpos = 0;
	}

	private void resetcardpos(ArrayList<ICard> localregistry) {
		for (ICard card : localregistry) {
			card.setX(0);
			card.setY(0);
		}
	}

	private void leftshift(ArrayList<ICard> arr) {
		for (int i = 0; i < arr.size(); i++) {
			ICard temp = arr.get(i);
			temp.setY(scaler);
			temp.setX((scaler * 5) + yPos);
			yPos += scaler;
		}
	}

	/**
	 * set the x position for the cards to spread them accross the map
	 *
	 * @param hand
	 */
	private void setHandPos(ArrayList<ICard> hand) {
		for (ICard card : hand) {
			card.setX((3 * scaler + 64) + cardXpos);
			card.setY(0);
			cardXpos += scaler;
		}
	}


	/**
	 * check if the clicked position is a sprite
	 *
	 * @param sprite
	 * @return
	 */
	public boolean isClicked(Sprite sprite) {
		if (Gdx.input.justTouched()) {
			camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (clickPos.x > sprite.getX() && clickPos.x < sprite.getX() + sprite.getWidth()) {
				if (clickPos.y > sprite.getY() && clickPos.y < sprite.getY() + sprite.getHeight()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add sprites to a textureAtlas as in create() method
	 */
	public void addSprites() {
		Array<AtlasRegion> regions = textureAtlas.getRegions();
		for (AtlasRegion region : regions) {
			Sprite sprite = textureAtlas.createSprite(region.name);
			sprites.put(region.name, sprite);
		}
	}

	/**
	 * draw a sprite in the set position
	 *
	 * @param name
	 * @param x
	 * @param y
	 */
	public void drawSprite(String name, float x, float y) {
		Sprite sprite = sprites.get(name);
		sprite.setPosition(x, y);
		sprite.draw(batch);
	}

	/**
	 * Show health and healthtokens
	 */
	public void showHealth() {
		font.draw(batch, "Damage: " + client.getPlayer().getDamage() + "\nTokens: " + client.getPlayer().getLifeToken(), 12 * scaler + 72, 2 * scaler - 32);
	}

	/**
	 *
	 */
	private void startHost() {
		hostLobby = true;
		client = new Client();
		h = new Host(client);

		initiateClient();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	private void initiateClient() {
		hand = client.getHand();
		setHandPos(hand);
		boardprinter = new BoardPrinter(client.gameBoard().getHeight(), client.gameBoard().getWidth(), scaler, batch);
	}

	/**
	 *
	 * @param hostName
	 */
	public void connectClient(String hostName) {
		client = new Client();
		client.join(hostName);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mainMenu = false;
		clientLobby = true;
	}

}