package ml.ravicake.peeingpong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PeeingPong extends Game {
	
	SpriteBatch batch;
	BitmapFont font;
	BitmapFont fontShadow;

	public void create() {
		batch = new SpriteBatch();
		//Use LibGDX's default Arial font.
		//font = new BitmapFont();
		Texture fontImage = new Texture(Gdx.files.internal("MyFont_0.png"));
		font = new BitmapFont(Gdx.files.internal("MyFont.fnt"),
				new TextureRegion(fontImage), false);
		fontShadow = new BitmapFont(Gdx.files.internal("MyFont.fnt"),
				new TextureRegion(fontImage), false);
		font.setColor(Color.RED);
		font.scale((float) -0.6);
		fontShadow.setColor(Color.BLACK);
		this.setScreen(new MenuScreen(this));
	}

	public void render() {
		super.render(); //important!
	}
	
	public void dispose() {
		batch.dispose();
		font.dispose();
		fontShadow.dispose();
	}

}