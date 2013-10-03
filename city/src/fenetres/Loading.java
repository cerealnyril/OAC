package fenetres;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import elements2D.Ville;

public class Loading {
	private float start, end, pas, current;
	private SpriteBatch batch;
	private Image cadreImg, cadreBackImg, fillImg;
	private Texture fillTex, cadreTex, cadreBackTex;
	private Stage stage;
	private BitmapFont white;
	private TextButton validButton;
	private boolean loaded, finish;
	private Ville ville;
	
	public Loading(Ville ville){
		this.white = new BitmapFont(Gdx.files.internal("fonts/whitefont.fnt"), false);
		this.loaded = false;
		this.stage = new Stage();
		this.batch = new SpriteBatch();
		this.ville = ville;
		final TextButtonStyle style = new TextButtonStyle();
		style.font = white;
		this.validButton = new TextButton("<Appuyez sur ESPACE pour continuer>", style);
		this.validButton.setX((Gdx.graphics.getWidth()/2)-(validButton.getWidth()/2));
		this.validButton.setY(100);
		//texture
		this.fillTex = new Texture(Gdx.files.internal("menus/loading_fill.png"));
		this.cadreTex = new Texture(Gdx.files.internal("menus/loading_cache.png"));
		this.cadreBackTex = new Texture(Gdx.files.internal("menus/loading_cache_back.png"));
		//image pour la texture
		this.cadreBackImg = new Image(cadreBackTex);
		this.cadreBackImg.setX((Gdx.graphics.getWidth()/2)-(cadreBackImg.getWidth()/2));
		this.cadreBackImg.setY(50);
		this.cadreImg = new Image(cadreTex);
		this.cadreImg.setX((Gdx.graphics.getWidth()/2)-(cadreImg.getWidth()/2));
		this.cadreImg.setY(50);
		this.fillImg = new Image(fillTex);
		this.start = (-fillImg.getWidth()/2)+25;
		this.end = (Gdx.graphics.getWidth()/2)-(fillImg.getWidth()/2);
		this.fillImg.setX(start);
		this.fillImg.setY(50);
		stage.addActor(cadreBackImg);
		stage.addActor(fillImg);
		stage.addActor(cadreImg);
		this.finish = false;
	}
	public void render(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		
		batch.begin();
			stage.draw();
			if(ville.getNbAim() != -1){
				if(((ville.getNbCurrent()/ville.getNbAim()) > 0.8) && (!finish)){
					stage.addActor(validButton);
					finish = true;
				}
				fillImg.setX(start+getPas());
			}
		batch.end();
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
		      loaded = true;
		      dispose();
		}
	}
	public boolean isLoaded(){
		return this.loaded;
	}
	public void dispose(){
		batch.dispose();
		stage.dispose();
		cadreBackTex.dispose();
		cadreTex.dispose();
		fillTex.dispose();
	}
	private float getPas(){
		return ville.getNbCurrent()*((end-start)/ville.getNbAim());
	}
}
