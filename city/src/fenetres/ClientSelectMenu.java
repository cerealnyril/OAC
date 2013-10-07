package fenetres;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import managers.ScreenManager;

import reseau.Client;
import tools.ReseauGlobals;


import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import elements2D.Ville;

/** Classe de connexion*/
public class ClientSelectMenu implements Screen {
	
	private final ScreenManager jeu;
	private Client client;
	
	private Texture backTex;
	private Sprite backSprite;
	private SpriteBatch batch;
	private TweenManager manager;
	private Stage stage;
	private BitmapFont white, red;
	private TextureAtlas atlas;
	private TextButton startButton, controlButton, netButton; 
	private Image background;
	private Sound backSound, mouse_overSound, mouse_clickSound, menu_activateSound;
	
	//booleens pour le type de menu selectionné
	boolean mainMenu;
	
	
	private static Logger logger = Logger.getLogger("clientLog");

	public ClientSelectMenu(ScreenManager jeu) {
		this.jeu = jeu;
		this.mainMenu = false;
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		manager.update(delta);
		stage.act(delta);
		
		batch.begin();
			//si le menu principal est activé
			if(mainMenu){
				long id = this.backSound.play();
				this.backSound.setLooping(0, true);
				this.backSound.setVolume(id, 0.5f);
				stage.draw();
			}
			else{
				long id = this.menu_activateSound.play();
				this.menu_activateSound.setVolume(id, 0.2f);
				backSprite.draw(batch);
			}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		backTex = new Texture(Gdx.files.internal("menus/main.png"));
		//des filtres pour le scale
		backTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backSprite = new Sprite(backTex);
		//pour l'animation de fondu
		backSprite.setColor(1, 1, 1, 0);
		
		//sons
		backSound = Gdx.audio.newSound(Gdx.files.internal("sounds/menu.ogg"));
		menu_activateSound = Gdx.audio.newSound(Gdx.files.internal("sounds/menu_activate.ogg"));
		batch = new SpriteBatch();
		//tout ça c'est pour l'animation de fondu et la suite
		Tween.registerAccessor(Sprite.class, new SpriteTween());
		manager = new TweenManager();
		//pour le gestionnaire de quand l'animation de fondu est terminée, appeler une fonction
		TweenCallback cb = new TweenCallback(){
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				loadMainMenu();
			}
		};
		//pour l'animation du fondu
		Tween.to(this.backSprite, SpriteTween.ALPHA, 2.5f).target(1).ease(TweenEquations.easeInQuad).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
		
		//pour le menu
		stage = new Stage();
	}
	/** fenetre de chargement du menu principal aprés l'animation*/
	private void loadMainMenu(){
		Gdx.input.setInputProcessor(stage);
		this.atlas = new TextureAtlas();
		this.white = new BitmapFont(Gdx.files.internal("fonts/whitefont.fnt"), false);
		this.red = new BitmapFont(Gdx.files.internal("fonts/redfont.fnt"), false);
		final TextButtonStyle style_normal = new TextButtonStyle();
		final TextButtonStyle style_over = new TextButtonStyle();
		//sons
		this.mouse_overSound = Gdx.audio.newSound(Gdx.files.internal("sounds/mouse_over.ogg"));
		this.mouse_clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/mouse_click.ogg"));
		//styles
		style_normal.font = white;
		style_over.font = red;
		
		background = new Image(backTex);
		background.toBack();
		
		//contenus
		this.startButton = new TextButton("Lancer le jeu", style_normal);
		this.controlButton = new TextButton("Controles", style_normal);
		this.netButton = new TextButton("Réseau", style_normal);
		
		//positions
		this.startButton.setX(30);
		this.startButton.setY(200);
		this.startButton.align(Align.left);
		
		this.controlButton.setX(30);
		this.controlButton.setY(150);
		this.controlButton.align(Align.left);
		
		this.netButton.setX(30);
		this.netButton.setY(100);
		this.netButton.align(Align.left);
		
		//listeners
		this.startButton.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {
				startButton.setStyle(style_over);
				super.enter(event, x, y, pointer, fromActor);
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer,
					Actor toActor) {
				startButton.setStyle(style_normal);
				super.exit(event, x, y, pointer, toActor);
			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
					mouse_clickSound.play();
					launchGame();
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
			}
		});
		
		this.controlButton.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {
				controlButton.setStyle(style_over);
				mouse_overSound.play();
				super.enter(event, x, y, pointer, fromActor);
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer,
					Actor toActor) {
				controlButton.setStyle(style_normal);
				super.exit(event, x, y, pointer, toActor);
			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
					mouse_clickSound.play();
					jeu.setScreen(new ConfigControls(jeu));
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				
			}
		});
		
		this.netButton.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer,
					Actor fromActor) {
				netButton.setStyle(style_over);
				mouse_overSound.play();
				super.enter(event, x, y, pointer, fromActor);
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer,
					Actor toActor) {
				netButton.setStyle(style_normal);
				super.exit(event, x, y, pointer, toActor);
			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
					mouse_clickSound.play();
					jeu.setScreen(new ConfigNetwork(jeu));
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
			}
		});
		
		//ajout dans la scene
		stage.addActor(background);
		stage.addActor(startButton);
		stage.addActor(controlButton);
		stage.addActor(netButton);
		
		mainMenu = true;
	}
	/** Fonction de lancement du jeu */
	private void launchGame(){
		//connection du client
		try {
			client = new Client(ReseauGlobals.SERVEUR_PORT, InetAddress.getByName("127.0.0.1"));
			client.connect();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Ville ville = new Ville(jeu);
		client.setVille(ville);
		jeu.setScreen(new Vue2D(jeu, ville));
		//demande toute la ville
		client.askForAllCity();
		if(stage != null){
			stage.clear();
		}
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
		this.atlas.dispose();
		this.white.dispose();
		this.red.dispose();
		this.backTex.dispose();
		this.stage.dispose();
		this.batch.dispose();
		this.mouse_clickSound.dispose();
		this.menu_activateSound.dispose();
		this.mouse_overSound.dispose();
		this.backSound.dispose();
	}
}
