package sky7.gui;

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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import sky7.Client.Client;
import sky7.Client.IClient;
import sky7.card.ICard;
import sky7.host.Host;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


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
	private Sprite reset, confirm, host, join, powerdown, wait, Board1, Board2;

	private boolean cardsChosen, hostLobby = false, clientLobby = false, mainMenu = true;

	private int cardXpos = 0;
	private int scaler = 128;

	private ArrayList<ICard> hand;
	private ICard[] registry = new ICard[5];
	private int cardsInReg = 0;
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
			textures.put("Plain", new Texture("assets/menu/Plain.png"));

			for (int i=0; i<7; i++) {
				textures.put("Robot" + i, new Texture("assets/robots/Robot" + i + ".png"));
			}

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
			Board1 = new Sprite(textures.get("Plain"));
			Board1.setPosition(scaler * 3, scaler * 7);
			Board2 = new Sprite(textures.get("Plain"));
			Board2.setPosition(scaler * 11, scaler * 7);
			
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
			font.getData().setScale(3);
			batch.draw(textures.get("Splashscreen"), 0, 0, windowWidth * scaler, windowHeight * scaler);
			
			wait.draw(batch);
			font.draw(batch, h.getnPlayers() + " Connected Players", 6 * scaler + 64, 6 * scaler);

			if (isClicked(wait)) {
				hostLobby = false;
				new Thread() {
					public void run() {
						h.Begin();
					}
				}.start();
				this.width = client.gameBoard().getWidth();
				this.height = client.gameBoard().getHeight();
				font.getData().setScale(2);
			}
			
			Board1.draw(batch);
			font.draw(batch, "DizzyDash", scaler * 3 + 32, scaler * 7 + 80);
			if(isClicked(Board1)){
				h.setBoardName("assets/Boards/DizzyDash.json");
				client.setBoardName("assets/Boards/DizzyDash.json");
			}
			
			Board2.draw(batch);
			font.draw(batch, "CheckMate", scaler*11 + 32, scaler * 7 + 80);
			if(isClicked(Board2)){
				h.setBoardName("assets/Boards/CheckMate.json");
				client.setBoardName("assets/Boards/CheckMate.json");
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

			if (!client.isGameOver() && client.getPlayer().getLifeToken() > 0) {
				chooseCards(); //Render 9 selectable cards
				showRegistry();
				showHealth(); //Render health of player

				/*
				 * render reset button only if at least one card is selected and
				 * when the player has not pressed the "ready" button
				 */
				if (!cardsChosen && cardsInReg > client.getPlayer().getNLocked()) {
					reset.draw(batch);
					if (isClicked(reset)) {
						reset();
					}
				}

				// Render "GO" button only if 5 cards are choosen and player has taken less than 9 damage
				if (cardsInReg == 5 && cardsChosen == false) {
					confirm.draw(batch);

					// if confirm is clicked:
					if (isClicked(confirm)) {
						cardsChosen = true;
						setRegistry();
					}
				}

				powerdown.draw(batch);
				if (isClicked(powerdown)) {
					System.out.println("Powering down next round");
					client.powerDown();
				}
			} else if (client.getPlayer().getLifeToken() == 0 && client.getPlayer().getDamage() >= 9){
				font.getData().setScale(8);
				font.draw(batch, "GAME OVER", 5*scaler, scaler);
			} else {
				font.getData().setScale(8);
				font.draw(batch, "YOU WON", 5*scaler, scaler);
			}
		}
		batch.end();
	}

	/**
	 * show registry in the screen
	 */
	public void showRegistry() {
	    for (int i=0; i<5; i++) {
            if (registry[i] != null) {
                registry[i].setX(64+scaler*(5+i));
                registry[i].setY(scaler);
                drawSprite(registry[i].GetSpriteRef(), 64+scaler*(5+i), scaler);
                font.draw(batch, registry[i].getPriority(), scaler*(5+i+1)-64 + 42, scaler + 93);
            }
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
		//		if (!hand.equals(client.getHand())) {
		if (client.isFinishedProcessing()) {
			reset();
		}
		//		}

		// if GO is not pressed, draw available cards
		if (!cardsChosen) {
			for (ICard card : hand) {
				drawSprite(card.GetSpriteRef(), card.getX(), card.getY());
				font.draw(batch, card.getPriority(), card.getX() + 42, card.getY() + 93);
			}
		}
		if (!cardsChosen && cardsInReg < 5) {

			//check if card is clicked
			if (Gdx.input.justTouched()) {
				camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));

				// go through cards in hand and see if the clickposition is the same as a cards position
				for (ICard card : hand) {
					if (clickPos.x <= scaler + card.getX() && clickPos.x > card.getX() && clickPos.y <= scaler) {
						if (card.getY() != scaler) {

							//							localregistry.set(pointer, card);
							for (int i=0; i<5; i++) {
								if (registry[i] == null) {
									registry[i] = card;
									cardsInReg++;
									break;
								}
							}

							System.out.println(cardsInReg + " card(s) choosen " + card.GetSpriteRef() + " \tPriority: \t" + card.getPriority());
							card.setY(-scaler);
						}
					}
				}
			}
		}
	}

	/**
	 * set 5 chosen chards.
	 */
	public void setRegistry() {
		//check if there actually are 5 chosen cards
		if (cardsInReg < 5) throw new IllegalStateException("GUI attempting to set registry with less than 5 cards");
		cardsChosen = true;
		for (int i = 0; i < 5; i++) {
			client.setCard(registry[i], i);
		}
		client.lockRegistry();
	}

	/**
	 * reset the chosen cards.
	 */
	public void reset() {
		System.out.println("\n----------- Resetting -----------");
		cardsChosen = false;
		cardXpos = 0;

		cardsInReg = client.getPlayer().getNLocked();
		System.out.println("Locked cards: " + cardsInReg);

		registry = client.getPlayer().getRegistry().clone();

		hand = client.getHand();

		setHandPos(hand);
		chooseCards();
		System.out.println("----------- end reset -----------");
	}

	/**
	 * set the x position for the cards to spread them across the map
	 *
	 * @param hand the hand to be set
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
	 * @param sprite the sprite to check
	 * @return true if a sprite is clicked, false otherwise.
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
	 * @param name name of the sprite
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void drawSprite(String name, float x, float y) {
		Sprite sprite = sprites.get(name);
		sprite.setPosition(x, y);
		sprite.draw(batch);
	}

	/**
	 * Show health and life tokens
	 */
	public void showHealth() {
		batch.draw(textures.get("Robot" + client.getPlayer().getPlayerNumber()), 13*scaler, scaler, scaler, scaler);
		font.draw(batch, "Damage: " + client.getPlayer().getDamage() + "\nTokens: " + client.getPlayer().getLifeToken(), 14 * scaler, 2 * scaler - 32);
	}

	/**
	 * Start the host
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
	 * Initiate client
	 */
	private void initiateClient() {
		hand = client.getHand();
		setHandPos(hand);
		boardprinter = new BoardPrinter(client.gameBoard().getWidth(), client.gameBoard().getHeight(), scaler, batch);
	}

	/**
	 * Connect client to hostName
	 *
	 * @param hostName string representing host
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