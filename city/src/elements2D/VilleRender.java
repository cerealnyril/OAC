package elements2D;

import java.util.Iterator;

import tools.Identifiants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ObjectMap;

/** Classe qui contient les fonctions graphiques de la ville a proprement parlé */
public class VilleRender {
	private Ville ville;
	private SpriteBatch batch;
	private Joueur joueur;
	private Maglev maglev;
	private OrthographicCamera camera;
	private float camWidth, camHeight;
	//pour les structures statiques des tableaux 
	ObjectMap<Integer, Batiment> batiments;
	ObjectMap<Integer, Bloc> blocs;
	//pour le debug des collisions
//	ShapeRenderer sr;
	
	public VilleRender(Ville ville){
		this.ville = ville;
		joueur = ville.getJoueur();
		maglev = ville.getMaglev();
		
		this.camera = new OrthographicCamera();
		joueur.giveCamera(this.camera);
		
		//plus c'est petit et plus on vois de pret
		this.camWidth = (Gdx.graphics.getWidth() /50) /2;
		this.camHeight = (Gdx.graphics.getHeight() / 50) /2;
		
		camera.setToOrtho(false, camWidth, camHeight);
		
		
		this.batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		//pour le debug des collisions
//		sr = new ShapeRenderer();
//		sr.setColor(Color.RED);
		
	}
	
	public void render(){
		
		
		this.camera.position.set(joueur.position.x, joueur.position.y, 0);
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		batch.begin();
			//on commence par dessiner les dalles de quartiers
			Quartier tmp_quart;
			Iterator<Integer> iter_quartier = ville.getQuartiers().keys().iterator();
			while(iter_quartier.hasNext()){
				tmp_quart = ville.getQuartierFromId(iter_quartier.next()); 
				//cette methode dessine a partir d'un point de depars sur x et y pour une taille.
				batch.draw(tmp_quart.getTexture(), tmp_quart.getPosition().x , tmp_quart.getPosition().y, 
						tmp_quart.getWidth()/2, tmp_quart.getHeight()/2, tmp_quart.getWidth(), tmp_quart.getHeight(), 1, 1, 
						0, 0, 0, tmp_quart.getTexture().getWidth(), tmp_quart.getTexture().getHeight(), 
						false, false);
			}
			//et on dessine les blocs
			Bloc tmp_bloc;
			Iterator<Integer> iter_bloc = ville.getBlocs().keys().iterator();
			while(iter_bloc.hasNext()){
				tmp_bloc = ville.getBlocFromId(iter_bloc.next()); 
				//cette methode dessine a partir d'un point de depars sur x et y pour une taille.
				batch.draw(tmp_bloc.getTexture(), tmp_bloc.getPosition().x , tmp_bloc.getPosition().y, 
						tmp_bloc.getWidth()/2, tmp_bloc.getHeight()/2, tmp_bloc.getWidth(), tmp_bloc.getHeight(), 1, 1, 
						0, 0, 0, tmp_bloc.getTexture().getWidth(), tmp_bloc.getTexture().getHeight(), 
						false, false);
			}
			//et on dessine les batiments
			Batiment tmp_bat;
			Iterator<Integer> iter_bat = ville.getBatiments().keys().iterator();
			while(iter_bat.hasNext()){
				tmp_bat = ville.getBatimentFromId(iter_bat.next()); 
				batch.draw(tmp_bat.getTexture(), tmp_bat.getPosition().x, tmp_bat.getPosition().y, 
						tmp_bat.getWidth()/2, tmp_bat.getHeight()/2, tmp_bat.getWidth(), tmp_bat.getHeight(), 1, 1, 
						0, 0, 0, tmp_bat.getTexture().getWidth(), tmp_bat.getTexture().getHeight(), 
						false, false);
			}
			//et on dessine les routes
			Route tmp_route;
			Iterator<Route> iter_route = ville.getRoutes().iterator();
			while(iter_route.hasNext()){
				tmp_route = iter_route.next(); 
				batch.draw(tmp_route.getTexture(), tmp_route.getPosition().x, tmp_route.getPosition().y, 
						tmp_route.getWidth()/2, tmp_route.getHeight()/2, 1, 1, 1, 1, 
						tmp_route.getSens(), 0, 0, tmp_route.getTexture().getWidth(), tmp_route.getTexture().getHeight(), 
						false, false);
			}
			//et on dessine les canaux
			Canal tmp_canal;
			Iterator<Canal> iter_canal = ville.getCanaux().iterator();
			while(iter_canal.hasNext()){
				tmp_canal = iter_canal.next(); 
				batch.draw(tmp_canal.getTexture(), tmp_canal.getPosition().x, tmp_canal.getPosition().y, 
						tmp_canal.getWidth()/2, tmp_canal.getHeight()/2, 1, 1, 1, 1, 
						tmp_canal.getSens(), 0, 0, tmp_canal.getTexture().getWidth(), tmp_canal.getTexture().getHeight(), 
						false, false);
			}
			//on dessine les rles
			RLE tmp_rle;
			Iterator<Integer> iter_rle = ville.getRLEs().keys().iterator();
			while(iter_rle.hasNext()){
				int id = iter_rle.next();
				Iterator<RLE> iter = ville.getRLEs().get(id).iterator();
				while(iter.hasNext()){
					tmp_rle = iter.next();
					batch.draw(tmp_rle.getTexture(), tmp_rle.getPosition().x, tmp_rle.getPosition().y, 
							tmp_rle.getWidth()/2, tmp_rle.getHeight()/2, tmp_rle.getWidth(), tmp_rle.getHeight(), 1, 1, 
							0, 0, 0, tmp_rle.getTexture().getWidth(), tmp_rle.getTexture().getHeight(), 
							false, false);
				}
			}
			//la texture du joueur, sa position sur x et y, le milieu du joeur sur x et y (pour que ça tourne sur lui meme), la taille largeur hauteur, le scale a 1, 1 le fait de prendre en compte la rotation
			batch.draw(joueur.getTexture(), joueur.getPosition().x, joueur.getPosition().y, 
					joueur.getWidth()/2, joueur.getHeight()/2, joueur.getWidth(), joueur.getHeight(), 1, 1, 
					joueur.getRotation(), 0, 0, joueur.getTexture().getWidth(), joueur.getTexture().getHeight(), 
					false, false);
			//et on dessine les rails
			Rail tmp_rail;
			Iterator<Rail> iter_rail = ville.getRails().iterator();
			while(iter_rail.hasNext()){
				tmp_rail = iter_rail.next();
				batch.draw(tmp_rail.getTexture(), tmp_rail.getPosition().x, tmp_rail.getPosition().y, 
						tmp_rail.getWidth()/2, tmp_rail.getHeight()/2, tmp_rail.getWidth(), tmp_rail.getHeight(), 1, 1, 
						0, 0, 0, tmp_rail.getTexture().getWidth(), tmp_rail.getTexture().getHeight(), 
						false, false);
			}
			//on ferme les rails
			iter_rail = ville.getClosureRails().iterator();
			while(iter_rail.hasNext()){
				tmp_rail = iter_rail.next();
				batch.draw(tmp_rail.getTexture(), tmp_rail.getPosition().x, tmp_rail.getPosition().y, 
						tmp_rail.getWidth()/2, tmp_rail.getHeight()/2, tmp_rail.getWidth(), tmp_rail.getHeight(), 1, 1, 
						0, 0, 0, tmp_rail.getTexture().getWidth(), tmp_rail.getTexture().getHeight(), 
						false, false);
			}
			//on affiche le ou les maglevs
			batch.draw(maglev.getTexture(), maglev.getPosition().x, maglev.getPosition().y, 
					maglev.getWidth()/2, maglev.getHeight()/2, maglev.getWidth(), maglev.getHeight(), 1, 1, 
					maglev.getRotation(), 0, 0, maglev.getTexture().getWidth(), maglev.getTexture().getHeight(), 
					false, false);
		batch.end();
		
		//pour le debug des collisions
//		sr.setProjectionMatrix(camera.combined);
//		sr.begin(ShapeType.Rectangle);
//		sr.rect(joueur.getPosition().x, joueur.getPosition().y, joueur.getWidth(), joueur.getHeight());
//		sr.end();
	}
	
	public void dispose(){
		batch.dispose();
	}
	/** Je ne sais pas encore comment je vais m'en servir mais dans l'absolu c'est le zoom */
	public OrthographicCamera getCamera(){
		return this.camera;
	}
}
