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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.*;
import sky7.board.ICell;
import sky7.board.cellContents.robots.RobotTile;
import sky7.card.ICard;
import sky7.host.Host;
import sky7.Client.IClient;


public class GUI implements ApplicationListener {
	private IClient game;
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

	private int pointer, cardXpos = 0;
	private int yPos = 64;
	private int scaler = 128;

	private ArrayList<ICard> hand;
	private ArrayList<ICard> registry = new ArrayList<>(5);
	TextInput listener;
	BackGround background;
	BoardPrinter boardprinter;

    public GUI(IClient game) throws FileNotFoundException {
        this.game = game;
        textures = new HashMap<>();
        sprites = new HashMap<>();
    }

	@Override
	public void create() {
		try {
			width = 12;
			height = 12;
			windowWidth = width+4;
			windowHeight = height+2;

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
			reset.setPosition(scaler*4, scaler+32);

			confirm = new Sprite(textures.get("Go"));
			confirm.setPosition(scaler*11, scaler+32);

            confirm = new Sprite(textures.get("confirm"));
            confirm.setPosition(scaler*11, scaler+20);

			join = new Sprite(textures.get("Join"));
			join.setPosition(scaler*5, scaler*7);

			powerdown = new Sprite(textures.get("PowerDown"));
			powerdown.setPosition(scaler*13, 32);

			wait = new Sprite(textures.get("Begin"));
			wait.setPosition(scaler*7, scaler*7);

			hand = game.getHand();
			addSprites();
			setHandPos(hand);
			listener = new TextInput(this);

			background = new BackGround(windowWidth, windowHeight, scaler, textures, batch);
			boardprinter = new BoardPrinter(width, height, scaler, batch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

            powerdown = new Sprite(textures.get("PowerDown"));
            powerdown.setPosition(scaler*12+72, 16);

            hand = game.getHand();
            addSprites();
            setHandPos(hand);
            listener = new TextInput(this);
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

		if (mainMenu) {
			batch.draw(textures.get("Splashscreen"), 0, 0, windowWidth*scaler, windowHeight*scaler);
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
			batch.draw(textures.get("Splashscreen"), 0, 0, windowWidth*scaler, windowHeight*scaler);
			wait.draw(batch);
			font.draw(batch, h.getnPlayers() + " Connected Players", 7*scaler, 6*scaler);

			if (isClicked(wait)) {
				hostLobby = false;
				new Thread() {
					public void run() {
						h.Begin();
					}
				}.start();
				this.width = game.gameBoard().getWidth();
				this.height = game.gameBoard().getHeight();
			}

		} else if (clientLobby) {
		    batch.draw(textures.get("Splashscreen"), 0, 0, windowWidth*scaler, windowHeight*scaler);
		    font.draw(batch, game.getNPlayers() + " Connected Players", 7*scaler, 6*scaler);
		    if (game.readyToRender()) clientLobby = false;
		} else {
			background.showDock(); //Render background and registry slots
			boardprinter.showBoard(game);
			showHealth(); //Render health of player
			showRegistry(); //Render the cards the player has chosen
			chooseCards(); //Render 9 selectable cards
		}


		/*
		 * render reset button only if at least one card is selected and
		 * when the player has not pressed the "ready" button
		 */

		if(!cardsChoosen && pointer != 0) {
			reset.draw(batch);
			if (isClicked(reset)) {
				reset();
			}
		}

		// Render "GO" button only if 5 cards are choosen
		if (pointer == 5) {
			confirm.draw(batch);
			powerdown.draw(batch);
			//if powerdown is clicked:
			if(isClicked(powerdown)) {
				System.out.println("Powering down next round");
				//TODO: some logic for powering down
			}
			// if confirm is clicked:
			if (isClicked(confirm)) {
				setRegistry();
				pointer = 0;
			}
		}
		batch.end();
	}

	private void startHost() {
		hostLobby = true;
		h = new Host(game);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void connectClient(String hostName) {
		game.join(hostName);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mainMenu = false;
		clientLobby = true;
	}

	@Override
	public void resize(int width, int height) {
		//		viewport.update(width, height), true);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		batch.setProjectionMatrix(camera.combined);
	}

    //find the rotation of the robot
    private int findRotation(RobotTile robot) {
        switch (robot.getOrientation()) {
            case EAST:
                return 270;
            case SOUTH:
                return 180;
            case WEST:
                return 90;
            default:
                return 0;
        }
    }


	// Show health and healthtokens
	public void showHealth() {
		font.draw(batch, "Health: " + game.getPlayer().getDamage() + "\nTokens: " + game.getPlayer().getLifeToken(), 12*scaler+72, 2*scaler-32);
	}

                }
            }
        }
    }

    // Show health and healthtokens
    public void showHealth() {
        font.draw(batch, "Health: " + game.getPlayer().getHealth() + "\nTokens: " + game.getPlayer().getLifeToken(), 12*scaler+72, 2*scaler-32);
    }

    /*
     * check if user has chosen all 5 cards
     * and put the chosen cards in the game-client, and lock the registry
     */
    public void setRegistry() {
        //check if there actually are 5 chosen cards
        if (registry.get(4) != null) {
            cardsChoosen = true;
            for (int i=0; i<registry.size(); i++) {
                game.setCard(registry.get(i), i);
            }
            game.lockRegistry();
        }
    }

    /*
     * Add sprites to a textureAtlas as in create() method
     */
    public void addSprites() {
        Array<AtlasRegion> regions = textureAtlas.getRegions();
        for (AtlasRegion region : regions) {
            Sprite sprite = textureAtlas.createSprite(region.name);
            sprites.put(region.name, sprite);
        }
    }

    // draw a sprite in the set position
    public void drawSprite(String name, float x, float y) {
        Sprite sprite = sprites.get(name);
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    // pick which cards you want to use
    public void chooseCards() {
        // check if the current hand is not the same as the hand in Client
        if(!hand.equals(game.getHand())) {
            hand.clear();
            reset();
        }
        // if GO is not pressed, draw all 9 available cards
        if(!cardsChoosen) {
            for (ICard card : hand) {
                drawSprite(card.GetSpriteRef(), card.getX(), card.getY());
                font.draw(batch, card.getPriority(), card.getX()+42, card.getY()+93);
            }
        }

        if (!cardsChoosen && pointer != 5) {
            //check if card is clicked
            if (Gdx.input.justTouched()) {
                camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                // go through cards in hand and see if the clickposition is the same as a cards position
                for(ICard card : hand) {
                    if(clickPos.x <= scaler+card.getX() && clickPos.x > card.getX() && clickPos.y <= scaler) {
                        if (card.getY() != scaler) {
                            registry.add(card);
                            pointer++;
                            System.out.println(pointer + " card(s) choosen " + card.GetSpriteRef() + " \tPriority: \t" + card.getPriority());
                            // just move them outside the map for now lol
                            card.setY(-scaler);
                        }
                    }
                }
            }
        }
    }

    //show chosen cards
    public void showRegistry() {
        for (ICard currentCards : registry) {
            drawSprite(currentCards.GetSpriteRef(), currentCards.getX(), currentCards.getY());
            font.draw(batch, currentCards.getPriority(), currentCards.getX()+42, currentCards.getY()+93);
        }
        if (!cardsChoosen && pointer <= 5) {
            for (ICard card : registry) {
                /*
                 * if a card is not moved up to registry, do so
                 * and set xPosition according to number of chosen cards
                 */
                if (card.getY() != scaler) {
                    card.setY(scaler);
                    card.setX((scaler*5)+yPos);
                    yPos += scaler;
                }
            }
        }
    }

    //check if the clicked position is a sprite
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

    //reset chosen cards, reset position etc
    public void reset() {
        System.out.println("\n----------- Resetting Cards -----------");
        cardsChoosen = false;
        pointer = 0;
        yPos = 64;
        cardXpos = 0;
        registry.clear();
        hand = game.getHand();

        for (ICard card : hand) {
            System.out.print(card.GetSpriteRef() + " Priority: " + card.getPriority() + " \t" );
        }

        resetCardPos(hand);
        resetCardPos(registry);
        setHandPos(hand);
        showRegistry();
        chooseCards();
    }

    //reset the cardpositions
    private void resetCardPos(ArrayList<ICard> cards) {
        for (ICard card : cards) {
            card.setX(0);
            card.setY(0);
        }
    }

    // set the x position for the cards to spread them accross the map
    private void setHandPos(ArrayList<ICard> hand) {
        for (ICard card : hand) {
            card.setX((3*scaler+64)+cardXpos);
            card.setY(0);
            cardXpos+=scaler;
        }
    }
}
