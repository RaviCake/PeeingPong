package ml.ravicake.peeingpong;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen, InputProcessor {
	
	final PeeingPong game;
	
	OrthographicCamera camera;

	float elapsedTime;
		
	TextureAtlas atlas;
	TextureRegion platform;
	TextureRegion drop;
	TextureRegion tank;
	TextureRegion background;
	TextureRegion water;
	TextureRegion[] boy = new TextureRegion[2];
	TextureRegion[] fish = new TextureRegion[2];
	TextureRegion fishDead;
	Animation boyAnim;
	Animation fishAnim;
	Array <Rectangle> peeArray;
	
	Rectangle tankRect, tankRect2;

	long lastDropTime;

	float boyX = 160;

	float gravity = 10;
	
	boolean DoPee = false;
	
	float waterY, water2Y;
	float waterLevel = 100, waterLevel2 = 100;
	

	public GameScreen(PeeingPong peeingPong) {
		game = peeingPong;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		Gdx.input.setInputProcessor(this);
		
		//loading assets
		atlas = new TextureAtlas(Gdx.files.internal("peeingAtlas.pack"));
		boy[0] = atlas.findRegion("boya1");
		boy[1] = atlas.findRegion("boya2");
		fish[0] = atlas.findRegion("fish1");
		fish[1] = atlas.findRegion("fish2");
		fishDead = atlas.findRegion("fish3");
		background = atlas.findRegion("background2");
		tank = atlas.findRegion("aquarium");
		drop = atlas.findRegion("drop");
		platform = atlas.findRegion("platform");
		water = atlas.findRegion("water");
		boyAnim = new Animation(1/2f,boy);
		fishAnim = new Animation(1/2f,fish);
		
		peeArray = new Array <Rectangle>();
		
		tankRect = new Rectangle();
		tankRect.height = 10;
		tankRect.width = 122;
		tankRect.x = 150;
		tankRect.y = 10;
	
		tankRect2 = new Rectangle();
		tankRect2.height = 10;
		tankRect2.width = 122;
		tankRect2.x = 450;
		tankRect2.y = 10;
		
		waterY = 10;
		water2Y = 10;
		
	}
	private void peeing(){
		Rectangle pee = new Rectangle();
		pee.height = 8;
		pee.width = 8;
		pee.x = boyX+ 15;
		pee.y = 292+(175/2);
		peeArray.add(pee);
		lastDropTime = TimeUtils.nanoTime();
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//update the camera
		camera.update();
		elapsedTime += Gdx.graphics.getDeltaTime();
		game.batch.setProjectionMatrix(camera.combined);
		
		//drawing stuffs
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		for (Rectangle pee : peeArray) {
			game.batch.draw(drop, pee.x, pee.y);
		}
		game.batch.draw(boyAnim.getKeyFrame(elapsedTime, true), boyX, 292);
		game.batch.draw(platform, 60, 248);
		game.batch.draw(fishAnim.getKeyFrame(elapsedTime, true), tankRect.x + 20, tankRect.y + 20);
		game.batch.draw(water, tankRect.x, waterY);
		game.batch.draw(tank,tankRect.x,tankRect.y);
		game.batch.draw(fishAnim.getKeyFrame(elapsedTime, true), 470, tankRect.y + 20);
		game.batch.draw(water, tankRect2.x, water2Y);
		game.batch.draw(tank,450,tankRect.y);
		game.batch.draw(platform, 60, -38);
		
		game.batch.end();
		
		///////////// CONTROLS //////////
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			boyX = touchPos.x - 64 / 2;
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			boyX -= 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			boyX += 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.SPACE)){
			peeing();
			gravity++;
			}

		// make sure the bucket stays within the screen bounds
		if (boyX < 62)
			boyX = 62;
		if (boyX > (680 + 60) -70)
			boyX = (680 + 60) - 70;
		
		if ( waterY > 10){
			waterY = 10;
		}
		
		if(waterY < -40){
			game.batch.begin();
			game.font.draw(game.batch, "Water Level LOW", 10, 460);
			game.batch.end();
		}
		
		if (waterY  < -60){
			waterY = -60;
			game.batch.begin();
			game.batch.draw(fishDead, tankRect.x + 20, tankRect.y + 20);
			game.batch.end();
			game.setScreen(new GameOverScreen(game));
			dispose();
		}
		
		if ( water2Y > 10){
			water2Y = 10;
		}
		if(water2Y < -40){
			game.batch.begin();
			game.font.draw(game.batch, "Water Level LOW ", 610, 460);
			game.batch.end();
		}
		if (water2Y  < -60){
			water2Y = -60;
			game.batch.begin();
			game.batch.draw(fishDead, 470, tankRect.y + 20);
			game.batch.end();
			game.setScreen(new GameOverScreen(game));
			dispose();
		}
		
		// water physics
		waterY -= 0.2;
		water2Y -= 0.2;
		waterLevel -= 0.3;
		waterLevel2 -= 0.3;

		////////////////////////////////
		
		////////////////////////////////Iterating peeing()
		Iterator<Rectangle> iter = peeArray.iterator();
		while (iter.hasNext()) {
			Rectangle pee = iter.next();

			pee.y -= 1200 * Gdx.graphics.getDeltaTime() + gravity;
			pee.x -= 300 * Gdx.graphics.getDeltaTime();
				
			if (pee.y < -900) {
				iter.remove();
				gravity = 0;
				}

			if (pee.overlaps(tankRect)) {
				iter.remove();
				waterY +=0.6;
				waterLevel += 0.7;
				if(waterLevel > 100){
					waterLevel = 100;
				}
			}
			if (pee.overlaps(tankRect2)) {
				iter.remove();
				water2Y +=0.6;				
				waterLevel2 += 0.7;
				if(waterLevel2 > 100){
					waterLevel2 = 100;
				}
			}
		}
		/////////////////////////////////////////////
		
		

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
		dispose();

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		atlas.dispose();

	}
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.SPACE: peeing();
			break;
		case Keys.LEFT: boyX-=10;
			break;
		case Keys.RIGHT: boyX+=10;
		}
		return true;
	}
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		peeing();
		gravity++;
		return true;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
