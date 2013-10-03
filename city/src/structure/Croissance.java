package structure;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

/** classe permettant de stocker les directions de croissance des quartiers */
public class Croissance {
	private int in_x , in_y, to_x, to_y;
//	private boolean reverse;
	private int voisin_gauche, voisin_droite, voisin_haut, voisin_bas;
	
	/** Tout un tas de test pour choisir la direction dans laquelle on vas incrementer*/
	public Croissance(int x, int y){
		in_x = 0;
		in_y = 0;
		if(Math.abs(x) > Math.abs(y)){
			in_x = 1;
			if(x < 0){
				in_x = -1;
			}
		}
		else if(Math.abs(x) < Math.abs(y)){
			in_y = 1;
			if(y < 0){
				in_y = -1;
			}
		}
		//si c'est un coin (coin)
		else if(Math.abs(x) == Math.abs(y)){
			if(x < 0 && y < 0){
				in_y = -1;
			}
			else if(x < 0 && y > 0){
				in_x = -1;
			}
			else if(x > 0 && y > 0){
				in_y = 1;
			}
			else if(x > 0 && y < 0){
				in_x = 1;
			}
		}
		voisin_gauche = -1;
		voisin_droite = -1;
		voisin_haut = -1;
		voisin_bas = -1;
	}
/*-------------------------------FONCTION DE CROISSANCE INTERNE------------------------*/
	/** croissance selon l'axe x ou y */
	public void crois(){
		to_x = in_x;
		to_y = in_y;
	}
/*------------------------INITIALISATION DES VOISINS--------------------*/
	public void setHaut(int id){
		this.voisin_haut = id;
	}
	public void setBas(int id){
		this.voisin_bas = id;
	}
	public void setGauche(int id){
		this.voisin_gauche = id;
	}
	public void setDroite(int id){
		this.voisin_droite = id;
	}
/*-------------------------------ACCESSEURS-----------------------------*/
	/** renvois la nouvelle position sur l'axe x */
	public int toX(){
		return this.to_x;
	}
	/** renvois la nouvelle position sur l'axe y*/
	public int toY(){
		return this.to_y;
	}
	/** renvois le voisin à bouger */
	public ArrayList<Integer> quartierToMove(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		if(to_x > 0){
			if(voisin_droite != -1){
				result.add(voisin_droite);
			}
		}
		if(to_x < 0){
			if(voisin_gauche != -1){
				result.add(voisin_gauche);
			}
		}
		if(to_y > 0){
			if(voisin_haut != -1){
				result.add(voisin_haut);
			}
		}
		if(to_y < 0){
			if(voisin_bas != -1){
				result.add(voisin_bas);
			}
		}
		return result;
	}
	/** Dit si on crois sur X*/
	public boolean axeX(){
		if(in_x != 0){
			return true;
		}
		return false;
	}
}
