package sky7.gui;

import java.io.FileNotFoundException;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sky7.game.IClient;

public class GUI implements ApplicationListener {
    private IClient game;
    private int width, height;
    private SpriteBatch batch;
    //abstract the name of textures.
    private HashMap<String, Texture> textures;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Sprite sp;
    private Vector3 clickPos = new Vector3();

    public GUI(IClient game) throws FileNotFoundException {
        this.game = game;
        game.generateBoard();
        this.width = game.gameBoard().getWidth();
        this.height = game.gameBoard().getHeight();


        textures = new HashMap<>();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(width * 128, height * 128);
        viewport = new FitViewport(width * 128, height * 128, camera);

        textures.put("robot", new Texture("assets/robot1.png"));
        textures.put("floor", new Texture("assets/floor/plain.png"));
        textures.put("CardPlaceHolder", new Texture("assets/cards/CardPlaceHolder.png"));
        textures.put("outline", new Texture("assets/cards/CardPlaceHolderOutline.png"));
        textures.put("dock", new Texture("assets/dock.png"));
        textures.put("unmarkedCard", new Texture("assets/cards/cardPlaceHolderFaded.png"));

        sp = new Sprite(new Texture("assets/cards/CardPlaceHolder2.png"));
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
        Gdx.gl.glClearColor(0, 0, 0, 1); // Background Color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears every image from previous iteration of render.
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // draw a grid of width*height, each square at 128*128 pixels
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                String[] texturesRef = game.gameBoard().getTileTexture(i, j); // Which texture belongs at position i,j
                for (String tex : texturesRef) {
                    // batch.draw(textures.get(tex), i*128, j*128);

                    // need extra parameters (last 2 128s to scale each texture to 128x128 instead of original 300x300)
                    // (j+2) leaves space for dock
                    batch.draw(textures.get(tex), i * 128, (j + 2) * 128, 128, 128);

                }
            }
        }

        Dock();

        sp.draw(batch);

        if (Gdx.input.isTouched()) {

            camera.unproject(clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (clickPos.x > sp.getX() && clickPos.x < sp.getX() + sp.getWidth()) {
                if (clickPos.y > sp.getY() && clickPos.y < sp.getY() + sp.getHeight()) {
                    sp.setX(sp.getX() + 128);
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
        // TODO Auto-generated method stub
        viewport.update(width, height, true);

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

        //draw outline of 4 discarded cards
        for (int i = 5; i < 9; i++) {
            batch.draw(textures.get("outline"), i * 128, 0);
        }

        //draw faded/not selected card
        for (int i = 0; i < 5; i++) {
            batch.draw(textures.get("unmarkedCard"), i * 128, 0);
        }
    }

}
