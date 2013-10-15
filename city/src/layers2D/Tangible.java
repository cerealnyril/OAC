package layers2D;

import java.util.Iterator;

import tools.Identifiants;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import elements2D.Batiment;
import elements2D.Entite;
import elements2D.Frontiere;

/** La couche qui contient les elements avec lequels on a une interaction */
public class Tangible extends Group{

	private ObjectMap<Integer, Batiment> batiments;
	private ObjectMap<Integer, Array<Frontiere>> frontieres;
	
	public Tangible(){
		this.batiments = new ObjectMap<Integer, Batiment>();
		this.frontieres = new ObjectMap<Integer, Array<Frontiere>>();
	}
	
/*----------------------------------ACCESSEURS------------------------------------*/	
	/** Retourne un batiment dans la map des batiments */
	private Batiment getBatimentFromId(int id){
		return batiments.get(id);
	}

/*-----------------------------SETTEURS UPDATES ET FONCTIONS--------------------------------*/
	/** Gere l'arrivée de nouvelles frontieres */
	public void upFrontiere(Frontiere frontiere){
		//Si ce n'est pas le meme jour on vire tous le RLEs
		if(frontieres.get(frontiere.getID_q()) != null && frontieres.get(frontiere.getID_q()).get(0).getJour() != frontiere.getJour()){
			menageFrontiere(frontieres.get(frontiere.getID_q()));
			frontieres.remove(frontiere.getID_q());
			//TODO supprimer les acteurs frontiere existants 
		}
		//Si le rle n'est pas vide 
		Array<Frontiere> mes_frontieres = new Array<Frontiere>();
		if(frontieres.get(frontiere.getID_q()) != null){
			mes_frontieres.addAll(frontieres.get(frontiere.getID_q()));
		}
		//Dans tous les cas on rajoute le nouvel objet
		mes_frontieres.add(frontiere);
		frontieres.put(frontiere.getID_q(), mes_frontieres);
		this.addActor(frontiere.getImg());
	}
	
	/** Déplace tous les objets fixes selon un axe */
	public void translation(int id_quartier, Vector2 translation) {
		Iterator<Integer> iter_bats = batiments.keys().iterator();
		Batiment bat;
		while(iter_bats.hasNext()){
			bat = batiments.get(iter_bats.next());
			if(bat.getID_q() == id_quartier){
				bat.setPosition(new Vector2((bat.getPosition().x + translation.x),
						(bat.getPosition().y + translation.y)));
			}
		}
	}
	
	/** Gère l'arrive de nouveaux batiments */
	public boolean upBatiment(Batiment bat){
		//si le batiment existe deja
		if(getBatimentFromId(bat.getID()) != null){
			getBatimentFromId(bat.getID()).setPosition(bat.getPosition());
		}
		//sinon on créé un nouveau 
		else{
			this.batiments.put(bat.getID(), bat);
			this.addActor(bat.getImg());
			if(bat.getType() == Identifiants.stationBat){
				return true;
			}
		}
		return false;
	}
	
	/** Indique si oui ou non le joueur est en train de faire une collision */
	public boolean overlap(Rectangle bound){
		//overlap avec les batiments
/*		Iterator<Integer> iter_b = batiments.keys().iterator();
		while(iter_b.hasNext()){
			Rectangle test = batiments.get(iter_b.next()).getBound();
			if(joueur.getBound().overlaps(test)){
				return true;
			}
		}*/
		//ou avec les canaux
/*		Iterator<Canal> iter_c = canaux.iterator();
		while(iter_c.hasNext()){
			Rectangle test = iter_c.next().getBound();
			if(joueur.getBound().overlaps(test)){
				return true;
			}
		}*/
		//ou avec les canaux
		return false;
	}
	public void update(){
	}
	
	private void menageFrontiere(Array<Frontiere> to_clean){
		Iterator<Frontiere> iter = to_clean.iterator();
		while(iter.hasNext()){
			this.removeActor(iter.next().getImg());
		}
	}
}
