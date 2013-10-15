package structure;

import tools.Identifiants;
import tools.ParamsGlobals;


/** la classe rail stock les objets rails pour l'ensemble de la ville. Ces objets sont encodés selon le principe du RLE etendu à la direction */
public class Rail{

	private int type, jour, id_quartier, id, genre, orientation;
	private int x, y, id_rail_next, id_quart_next;
	
	public Rail(int x, int y, int id_q, int jour, int id, int idqn) {
		this.type = Identifiants.rails;
		this.x = x;
		this.y = y;
		this.id_quartier = id_q;
		this.id = id;
		this.id_quart_next = idqn;
		this.id_rail_next = -1;
		this.genre = 1;
		this.orientation = -1;
		ParamsGlobals.MANAGER.updateObject(this);
	}
/*-------------------------------SETTEUR--------------------------------*/
	/** Permet de donner un indentifiant au rail suivant 
	 * @param cible */
	public void setNext(int id_rail_next){
		this.id_rail_next = id_rail_next;
	}
	/** defini le genre de rail */
	public void setGenre(int genre){
		this.genre = genre;
	}
	/** defini l'orientation */
	public void setOrientation(int orientation){
		this.orientation = orientation;
	}
/*------------------------------ACCESSEURS------------------------------*/	
	public int getID(){
		return this.id;
	}
	public int getIDQuartier(){
		return this.id_quartier;
	}
	public int getType(){
		return this.type;
	}
	public int getJour() {
		return this.jour;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getNext(){
		return this.id_rail_next;
	}
	public int getIDQn(){
		return this.id_quart_next;
	}
	/** Retourne le genre de rail, en L ou I*/
	public int getGenre(){
		return this.genre;
	}
	/** Retourne l'orientation du rail */
	public int getOrientation(){
		return this.orientation;
	}
}
