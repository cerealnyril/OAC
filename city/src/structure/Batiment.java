package structure;

import java.util.ArrayList;
import java.util.TreeMap;

import tools.ParamsGlobals;
import topographie.Cell;
import topographie.Zone;


public abstract class Batiment{
	protected int identifiant;
	protected int MAX_REP = 1000;
	protected int type;
	protected int facade, profondeur;
	protected double COUT;
	protected double frais;
	//variables de cartographie 
	protected Zone zone;
	protected int id_bloc, id_quartier;
	protected int lvl;
	
	public Batiment(int id, int id_q){
		this.id_bloc = id;
		this.id_quartier = id_q;
		this.frais = 0.0;
		this.lvl = 1;
		if(id != -1){
			this.identifiant = ParamsGlobals.MANAGER.registerObject(this);
		}
	}
/*------------------------------ACCESSEURS-------------------------------*/
	/** renvois les niveaux de models */
/*	public TreeMap<Integer, String> getModels(){
		return this.models;
	}*/
	/** renvois le type du batiment */
	public int getType(){
		return this.type;
	}
	/** renvois l'identifiant du batiment */
	public int getID(){
		return this.identifiant;
	}
	/** renvois l'identifiant du bloc */
	public int getIDBloc(){
		return this.id_bloc;
	}
	/** renvois l'identifiant du quartier */
	public int getIDQuartier(){
		return this.id_quartier;
	}
	/** renvois une seule et unique fois les frais engagés dans la construction du batiment */
	public double getFrais(){
		double result = this.frais;
		this.frais = 0.0;
		return result;
	}
	/** augmente les couts engagés dans la construction du batiments */
	public void setFrais(double cout){
		this.frais += cout;
	}
	/** renvois les cellules occupées par la zone du batiment */
	public ArrayList<Cell> getCells(){
		return zone.getCells();
	}
	public abstract int getConsommationEnergie();
	/** renvois le centre sur x de la zone */
	public float getCentreX(){
		return this.zone.getCentreX();
	}
	/** renvois le centre sur y de la zone */
	public float getCentreY(){
		return this.zone.getCentreY();
	}
	/** renvois la taille sur x de la zone */
	public int getTailleX(){
		return this.zone.getTailleX();
	}
	/** renvois la taille sur y de la zone */
	public int getTailleY(){
		return this.zone.getTailleY();
	}
	/** renvois l'emplacement enregistré dans la zone soit au bout à gauche au bout à droite ou au milieu */
	public int getEmplacement(){
		return this.zone.getEmplacement();
	}
	/** renvois la direction du perron, soit la direction de la facade par rapport à la route enregistré dans la zone */
	public int getPerron(){
		return this.zone.getPerron();
	}
	/** renvois le niveau du batiment, soit son nombre d'etage ou son niveau de developpement */
	public int getLvl(){
		return this.lvl;
	}
/*--------------------------------SETTEURS-------------------------------*/
	/** rajoute un nouveau model à la liste. sert pour les niveaux */
/*	public void addModel(TreeMap<Integer, String> m){
		models = m;
	}*/
	public void setZone(Zone z){
		zone = z;
		zone.setCenter();
		zone.setIdBatiment(this.identifiant);
	}
	/** Augmente la taille du batiment */
	public void addNiveau(){
		lvl++;
		ParamsGlobals.MANAGER.updateObject(this);
	}
/*-------------------------------POUR LA MAP----------------------------*/
	public int getFacade(){
		return this.facade*ParamsGlobals.MULT_TAILLE;
	}
	public int getProfondeur(){
		return this.profondeur*ParamsGlobals.MULT_TAILLE;
	}
	public void move(int x, int y){
		this.zone.updateIndexe(x, y);
//		ParamsGlobals.MANAGER.updateObject(this);
 //       Transmission.updateBat(zone);
	}
/*------------------------GESTION DES MODELS------------------------------*/
	/** renvois la position de la texture sur les ordonnées ce qui correspond à un style graphie*/
	public int getTexY(){
		return 1;
	}
	/** renvois la position de la texture sur les abscisses ce qui correspond à un niveau de developpement */
	public int getTexX(){
		return 1;
	}
}
