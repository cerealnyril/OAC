package elements2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/** Un maglev c'est comme un train mais c'est vachement plus classe de dire maglev non? */
public class Maglev extends EntiteMouveable{
	
	private float inc = 1;
	private Ligne ligne;
	private int location;
	private int id_q_aim = 105;
	private static float SPEED = 2f;
	private Array<Vector2> chemin;
	
	public Maglev(int id, Vector2 velocite, float rotation,
			float width, float height, int id_q, Ligne ligne) {
		super(id, velocite, SPEED, rotation, width, height, id_q);
		this.ligne = ligne;
		super.tex = AssetsLoader.maglev;
		this.location = -1;
		this.chemin = new Array<Vector2>();
	}
	
	public void setStation(int id_q){
		super.id_q = id_q;
		Vector2 position = ligne.getStation(id_q);
		this.setPosition(new Vector2(position.x, position.y));
		this.location = 0;
	}
	/** Renvois la position du maglev. Sert principalement à l'initialisation pour etre sur qu'il est positionné sur les rails ou dans une station */
	public int getLocation(){
		return this.location;
	}
	/** Met a jour la position du maglev sur le chemin de fer en fonction des points qu'il traverse */
	@Override
	public void update() {	
		//on initialise les chemins si cela n'a pas deja été fait afin d'etre sure qu'on ne vas pas chercher un truc qui n'existe pas encore
		if((chemin.size == 0) && (ligne.existStation(id_q_aim)) && (ligne.existStation(id_q)) && ligne.isArcDone()){
			this.chemin = ligne.getPath(this.id_q, this.id_q_aim);
			this.setPosition(new Vector2(chemin.get(location)));
		}
		//si on a un chemin on le suit
		if((chemin.size > 0) && ligne.isArcDone()){
			//En gros l'angle c'est la position courante par rapport au prochain waypoint.premier argument la distance par rapport au prochain waypoint
			float angle = (float) Math.atan2(chemin.get(location).y - super.getY(), chemin.get(location).x - super.getX());
			//cosin et sin vont toujours tendre vers 1 donc 1 * speed
			velocite.set((float) Math.cos(angle) * super.speed, (float) Math.sin(angle) * super.speed);
			super.setPosition(new Vector2(
					position.x + velocite.x * Gdx.graphics.getDeltaTime(), 
					position.y + velocite.y * Gdx.graphics.getDeltaTime()));
			//ah oui mais l'angle est en radiant et nous on veux des degrés donc on dois convertir
			super.setRotation((angle * MathUtils.radDeg)-90);
			//faut gerer le fait qu'on change de waypoint
			if(isReached()){
				//pour eviter que ça saute
				super.setPosition(new Vector2(chemin.get(location).x, chemin.get(location).y));
				if(location == chemin.size-1){
					inc = -1;
				}
				else if(location == 0){
					inc = 1;
				}
				location+=inc;
			}
		}
	}
	/** Indique si le point intermediaire est atteind afin de se deplacer le long du rail vers le point suivant */
	private boolean isReached(){
		float on_x = Math.abs(chemin.get(location).x - super.getX());
		float on_y = Math.abs(chemin.get(location).y - super.getY());
//		System.out.println("on "+on_x+", "+on_y);
		if((on_x <= 0.2f) && (on_y <= 0.2f)){
			return true;
		}
		return false;
	}

}
