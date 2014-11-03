package ml.ravicake.peeingpong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MenuScreen implements Screen, InputProcessor {
	final PeeingPong game;
	
	OrthographicCamera camera;

	private float elapsedTime;
	TextureAtlas peeingAtlas;
	TextureRegion menuscreen;

	public MenuScreen(PeeingPong gameScreen) {
		this.game = gameScreen;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		Gdx.input.setInputProcessor(this);
		
		peeingAtlas = new TextureAtlas(Gdx.files.internal("peeingAtlas.pack"));
		menuscreen = peeingAtlas.findRegion("menuscreen");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 1, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//update the camera
		camera.update();
		elapsedTime += Gdx.graphics.getDeltaTime();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.batch.draw(menuscreen, 0, 0);
		game.batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.ENTER: 
			game.setScreen(new GameScreen(game));
			dispose();
			break;
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
		
		game.setScreen(new GameScreen(game));
		dispose();
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
