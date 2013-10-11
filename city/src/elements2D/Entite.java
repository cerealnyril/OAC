package elements2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/** La super classe d'entité qui est commune a tout les elements du jeu qui sont solides. 
 * Par exemple les batiments, les joueurs, les projectils*/
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
	protected TextureRegion tex;
	//l'identifiant du quartier associé
	protected int id_q;
	//la rotation
	protected float rotation;
	//l'image
	protected Image img;
	
	public Entite(int id, Vector2 position, float width, float height, int id_q){
		this.id = id;
		this.position = position;
		this.width = width;
		this.height = height;
		this.id_q = id_q;
		this.rotation = 0;
		bounds = new Rectangle(position.x, position.y, width, height);
	}
/*--------------------------------ACCESSEURS-------------------------------*/
	/** Retourne l'identifiant de l'entité */
	public int getID(){
		return this.id;
	}
	/** Retourne le vecteur de position*/
	public Vector2 getPosition() {
		return this.position;
	}
	/** Retourne spécifiquement la coordonée X du vecteur de position */
	public float getX(){
		return this.position.x;
	}
	/** Retourne spécifiquement la coordonée Y du vecteur de position */
	public float getY(){
		return this.position.y;
	}
	/** Retourne la longueure */
	public float getWidth() {
		return width;
	}
	/** Retourne la hauteur*/
	public float getHeight() {
		return height;
	}
	/** Retourne les limites de l'entité pour les eventuelles detections de collision ou d'entrée */
	public Rectangle getBound() {
		return bounds;
	}
	public int getID_q(){
		return this.id_q;
	}
	/** Renvois l'image */
	public Image getImg(){
		if(this.img == null){
			img = new Image(tex);
			img.setSize(width, height);
			img.setOrigin((width/2), (this.height/2));
			img.setPosition(position.x, position.y);
			img.setRotation(this.rotation);
		}
		return this.img;
	}
/*------------------------------SETTEURS-------------------------------*/
	/** Positionne a partir d'un vecteur et met à jour les limites */
	public void setPosition(Vector2 position) {
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.position = position;
		getImg().setPosition(position.x, position.y);
	}
	
	/** Renseigne la longueure */
	public void setWidth(float width) {
		this.width = width;
	}

	/** Renseigne la hauteur */
	public void setHeight(float height) {
		this.height = height;
	}
	
	/** Assible des limites */
	public void setBound(Rectangle bounds) {
		this.bounds = bounds;
	}
	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
		getImg().setRotation(rotation);
	}
}
