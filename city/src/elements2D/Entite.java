package elements2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/** La super classe d'entité qui est commune a tout les elements du jeu qui sont solides. Par exemple les batiments, les joueurs, les projectils*/
public abstract class Entite {
	//l'idenfiant
	protected int id;
	//la position
	protected Vector2 position;
	//la taille
	protected float width, height;
	//le rectangle pour les detections de colisions
	protected Rectangle bounds;
	//la texture
	protected Texture tex;
	//l'identifiant du quartier associé
	protected int id_q;
	
	public Entite(int id, Vector2 position, float width, float height, int id_q){
		this.id = id;
		this.position = position;
		this.width = width;
		this.height = height;
		this.id_q = id_q;
		bounds = new Rectangle(position.x, position.y, width, height);
	}
	
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.position = position;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public Rectangle getBound() {
		return bounds;
	}
	public void setBound(Rectangle bounds) {
		this.bounds = bounds;
	}
	public Texture getTexture(){
		return this.tex;
	}
	public int getID_q(){
		return this.id_q;
	}
	
}
