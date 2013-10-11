package elements2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class Joueur extends EntiteMouveable{
	
	private OrthographicCamera camera;
	
	public Joueur(int id, Vector2 velocite, float speed, float rotation, float width,
			float height, int id_q) {
		super(id, velocite, speed, rotation, width, height, id_q);
		//mise en place des textures 
		super.tex = AssetsLoader.joueur;
	}
	
	@Override
	public void update() {
		position.add(velocite.tmp().mul(Gdx.graphics.getDeltaTime()*speed));
		if(velocite.x != 0 || velocite.y != 0){
			rotation = velocite.angle() - 90;
		}
		bounds.x = position.x;
		bounds.y = position.y;
		getImg().setPosition(position.x, position.y);
		getImg().setRotation(rotation);
	}

	/** Donne l'instance de la camera au joueur pour les zoom ou les changements d'orientation */
	public void giveCamera(OrthographicCamera camera) {
		this.camera = camera;
	}
	/** Permet d'acceder à la camera par le joueur */
	public OrthographicCamera getCamera(){
		return this.camera;
	}
}
