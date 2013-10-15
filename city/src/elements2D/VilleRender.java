package elements2D;

import lights.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

/** Classe qui contient les fonctions graphiques de la ville a proprement parlé */
public class VilleRender {
	private Ville ville;
	private SpriteBatch batch;
	private Joueur joueur;
	private OrthographicCamera camera;
	private float camWidth, camHeight;
	private RayHandler handler;
	
	private Stage stage_monde;
	private World world;
	
	public VilleRender(Ville ville){
		
		this.ville = ville;
		joueur = ville.getJoueur();
		
		this.camera = new OrthographicCamera();
		joueur.giveCamera(this.camera);
		
		//plus c'est petit et plus on vois de pret
		this.camWidth = (Gdx.graphics.getWidth() /50) /2;
		this.camHeight = (Gdx.graphics.getHeight() / 50) /2;
		
		camera.setToOrtho(false, camWidth, camHeight);
		
		
		this.batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		
		stage_monde = new Stage();
		stage_monde.addActor(ville.getSol());
		stage_monde.addActor(ville.getTangible());
		stage_monde.addActor(joueur.getImg());
		stage_monde.addActor(ville.getAir());
		stage_monde.setCamera(camera);
		
		//pour les lumières 
		world = ville.getWorld();
		handler = ville.getLightHandler();
		handler.setCombinedMatrix(camera.combined);
	}
	
	public void render(){
		
		this.camera.position.set(joueur.position.x, joueur.position.y, 0);
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		
		stage_monde.act(Gdx.graphics.getDeltaTime());
		
		batch.begin();
			stage_monde.draw();
		batch.end();
		
		handler.updateAndRender();
	}
	
	public void dispose(){
		batch.dispose();
		stage_monde.dispose();
	}
	/** Je ne sais pas encore comment je vais m'en servir mais dans l'absolu c'est le zoom */
	public OrthographicCamera getCamera(){
		return this.camera;
	}
}
