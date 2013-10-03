package structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import tools.Identifiants;
import tools.ParamsGlobals;
import tools.Utils;
import topographie.Cell;
import topographie.Zone;



/** La classe quartier contient toutes les fonctions et les variables communes aux blocs quel que soit leurs
 * type */
public class Bloc {
	/** coordonée du bloc dans la ville */
	protected int identifiant;
	protected String type_txt;
	@SuppressWarnings("rawtypes")
	protected TreeMap listeBatiments;
	/** type des batiment avec liste correspondante */
	protected TreeMap<Integer, ArrayList<Integer>> batimentsType;
	protected int consommation;
	protected int type;
	protected int facade, profondeur;
	/** frais de construction du tour*/
	protected double frais;
	protected Zone zone;
	protected int id_quartier;
	protected int MAX_BAT;
	protected float max_height, min_height;

	/** le constructeurs de bloc ne fait que lui attribuer une position */
	public Bloc(int id){
		this.batimentsType = new TreeMap<Integer, ArrayList<Integer>>();
		this.type_txt = "Abstract";
		this.id_quartier = id;
		if(id != -1){
			this.identifiant = ParamsGlobals.MANAGER.registerObject(this);
		}
	}
	/** initialise la zone géographique */
	public void setZone(Zone z){
		//objet de zone pour la cartographie 
		zone = z;
		if(type != Identifiants.admininistrationBloc){
			zone.setCenter();
		}
		zone.setIdBloc(this.identifiant);
		zone.setTypeTxt(type_txt);
	}
/*------------------------------ACCESSEURS-----------------------------------*/
	/** renvois la hauteur minimale du bloc */
	public float getMinHeight(){
		return this.min_height;
	}
	/** renvois la hauteur maximale du bloc */
	public float getMaxHeight(){
		return this.max_height;
	}
	/** retourne la quantité maximale de batiment qu'on peut mettre dedans */
	public int getCapaciteMax(){
		return this.MAX_BAT;
	}
	/** La coordonée est une sorte d'identifiant qui sera utilisé pour le GUI et determiner 
	 * sa position par rapport au quartier voisins */
	public int getID(){
		return this.identifiant;
	}
	/** renvois le nom du type de bloc */
	public String getTypeTxt(){
		return this.type_txt;
	}
	/** renvois l'identifiant du quartier associé */
	public int getIDQuartier(){
		return this.id_quartier;
	}
	/** retourne la liste des batiments du quartier */
	@SuppressWarnings("unchecked")
	public TreeMap<Integer, ?> getListeBatiments() {
		return this.listeBatiments;
	}
	/** renvois le type du bloc */
	public int getType(){
		return this.type;
	}
	/** renvois la taille sur X */
	public int getTailleX(){
		return zone.getTailleX();
	}
	/** renvois la taille sur Y */
	public int getTailleY(){
		return zone.getTailleY();
	}
	/** renvois le centre en X */
	public float getCentreX(){
		return zone.getCentreX();
	}
	/** renvois le centre en Y */
	public float getCentreY(){
		return zone.getCentreY();
	}
	/** renvois la coordonée de depars sur X */
	public int getStartX(){
		return zone.getStartX();
	}
	/** renvois la coordonée de depars sur Y */
	public int getStartY(){
		return zone.getStartY();
	}
	/** renvois la coordonée de fin sur X */
	public int getEndX(){
		return zone.getEndX();
	}
	/** renvois la coordonée de fin sur Y */
	public int getEndY(){
		return zone.getEndY();
	}
	/** renvois un tableau des identifiants des batiments */
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getBatimentsID(){
		return new ArrayList<Integer>(listeBatiments.keySet());
	}
	/** renvois un batiment en fonction de son identifiant */
	public Batiment getBatiment(int id){
		return (Batiment) listeBatiments.get(id);
	}
	/** renvois tout les identifiants des batiments qui correspondant au type demandé */
	public ArrayList<Integer> getBatimentsFromType(int type){
		if(this.batimentsType.get(type) != null){
			return this.batimentsType.get(type);
		}
		return new ArrayList<Integer>();
	}
	/** renvois la consommation d'energie du bloc basée sur les batiments qu'ils contients */
	@SuppressWarnings("unchecked")
	public int getConsommationEnergie(){
		int conso = 0;
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		while(iter.hasNext()){
			Batiment bat = (Batiment) listeBatiments.get(iter.next());
			conso += bat.getConsommationEnergie();
		}
		return conso;
	}
	public int getFacade(){
		return this.facade*ParamsGlobals.MULT_TAILLE;
	}
	public int getProfondeur(){
		return this.profondeur*ParamsGlobals.MULT_TAILLE;
	}
/*-------------------------------MODIFICATION-------------------------------*/
	/** change la hauteure maximale du bloc sur la map */
	public void setMinHeight(float m){
		this.min_height = m;
	}
	/** change la hauteure minimale du bloc sur la map */
	public void setMaxHeight(float m){
		this.max_height = m;
	}
	/** fonction qui créé le batiment citadelle */
	@SuppressWarnings("unchecked")
	public void initCitadelle() {
		listeBatiments = new TreeMap<Integer, Batiment>();
		Batiment bat = new Citadelle(this.identifiant, this.id_quartier);
		/** ajout du batiment dans le bloc */
		listeBatiments.put(bat.getID(), bat);
		ArrayList<Integer> batiments = new ArrayList<Integer>();
		batiments.add(bat.getID());
		batimentsType.put(bat.getType(), batiments);
		/** fonction de cartographie */
		Zone z = new Zone();
		z.setTailleX(bat.getFacade());
		z.setTailleY(bat.getProfondeur());
		z.setCentre(0, 0);
		z.setIdQuartier(zone.getIdQuartier());
		z.setIdBloc(this.identifiant);
		int start_x = z.getStartX();
		int end_x = z.getEndX();
		int start_y = z.getStartY();
		int end_y = z.getEndY();
		for(int y= start_y; y < end_y; y++){
	    	for(int x=start_x; x < end_x; x++){
	    		Cell cell = zone.getCell(x, y);
	    		cell.resetBatType(bat.getType());
	    		zone.removeAvailable(cell);
    			z.indexation(cell);
	    	}
	    }
		bat.setZone(z);
		ParamsGlobals.MANAGER.updateObject(bat);
	}
	/** mise à jour de la liste des batiments du bloc */
	@SuppressWarnings("unchecked")
	public void ajoutBatiment(Batiment bat){
		/** ajout du batiment dans le bloc */
		listeBatiments.put(bat.getID(), bat);
		ArrayList<Integer> batiments = new ArrayList<Integer>();
		if(!batiments.contains(bat.getID())){
			batiments.add(bat.getID());
		}
		if(batimentsType.containsKey(bat.getType())){
			batiments.addAll(batimentsType.get(bat.getType()));
		}
		else{
			batimentsType.put(bat.getType(), batiments);
		}
		/** fonction de cartographie */
		if(bat.getType() == Identifiants.horlogeBat){
			initBat(bat);
		}
		else{
			placeBat(bat);
		}
		ParamsGlobals.MANAGER.updateObject(bat);
	}
	/** retourne le nombre de batiments du quartier */
	public int getNombreBatiments() {
		return listeBatiments.size();
	}
	/** récupère les frais engagés pour les constructions */
	public double frais(){
		double result = frais;
		this.frais = 0.0;
		return result;
	}
	/** refait les indexes du bloc */
	public void move(int to_x, int to_y){
		if(zone != null){
			zone.updateIndexe(to_x, to_y);
			@SuppressWarnings("unchecked")
			Iterator<Integer> iter = listeBatiments.keySet().iterator();
			while(iter.hasNext()){
				int clef = iter.next();
				Batiment batiment = (Batiment) listeBatiments.get(clef);
				batiment.move(to_x, to_y);
//				ParamsGlobals.MANAGER.updateObject(batiment);
			}
		}
	}
/*==============================POUR LA CARTOGRAPHIE============================*/
	/** retourne les cellules qui ne sont pas et dont les voisins le sont */
	public ArrayList<Cell> getCandidats(){
		ArrayList<Cell> results = new ArrayList<Cell>();
		Iterator<Cell> iter = zone.getCells().iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			Cell haut = zone.getHaut(cell);
			Cell bas = zone.getBas(cell);
			Cell gauche = zone.getGauche(cell);
			Cell droite = zone.getDroite(cell);
			if(cell.isFreeBat() && (
					haut.isFreeBloc() || !haut.isFreeBat()) ||
					bas.isFreeBloc() || !bas.isFreeBat() ||
					gauche.isFreeBloc() || !gauche.isFreeBat() ||
					droite.isFreeBloc() || !droite.isFreeBat()){
				results.add(cell);
			}
		}
		return results;
	}
/*====================================GESTION DES BATIMENTS===================================*/
/*-------------------------------------ACCESSEURS----------------------------------------------*/
/*	public ArrayList<Cell> getCellsBat(int id_bat){
		Batiment batiment = (Batiment) listeBatiments.get(id_bat);
		return batiment.getCells();
	}*/
/*	public ArrayList<Cell> getCells(){
		return zone.getCells();
	}
	public ArrayList<Cell> getDispo(){
		return zone.getAvailables();
	}*/
/*------------------------------MODIFIFCATION DE LA GRILLE-------------------------------------*/
	/** on positionne l'horloge au milieu */
	public void initBat(Batiment bat){
		Zone z = new Zone();
		z.setTailleX(bat.getFacade());
		z.setTailleY(bat.getProfondeur());
		z.setCentre(zone.getCentreX(), zone.getCentreY());
		z.setIdQuartier(zone.getIdQuartier());
		z.setIdBloc(this.identifiant);
		int start_x = z.getStartX();
		int end_x = z.getEndX();
		int start_y = z.getStartY();
		int end_y = z.getEndY();
		for(int y= start_y; y < end_y; y++){
	    	for(int x=start_x; x < end_x; x++){
	    		Cell cell = zone.getCell(x, y);
	    		cell.resetBatType(bat.getType());
	    		zone.removeAvailable(cell);
    			z.indexation(cell);
	    	}
	    }
		bat.setZone(z);
	}
	/** extrait la forme du bloc */
	private void placeBat(Batiment bat){
		Zone z = new Zone();
		z.setIdQuartier(zone.getIdQuartier());
		z.setIdBloc(this.identifiant);
		ArrayList<Cell> disponibles = zone.getAvailables();
		int x_size = bat.getFacade();
		int y_size = bat.getProfondeur();
		Cell cell = selectCandidat(x_size,y_size, disponibles);
		if(cell == null){
			int tmp = x_size;
			x_size = y_size;
			y_size = tmp;
			cell = selectCandidat(x_size, y_size, disponibles);
		}
		z.setTailleX(x_size);
		z.setTailleY(y_size);
		/** on choisi la direction qui nous interesse */
		int direction_x = getXDirection(cell.getX(), cell.getY(), disponibles);
		int direction_y = getYDirection(cell.getX(), cell.getY(), disponibles);
		/** on met en place les parametres pour la promenade */
		int x_inc = 0;
		int y_inc = 0;
		if(Math.abs(direction_x) > Math.abs(direction_y)){
			x_inc = 1;
			if(direction_x < 0){
				x_inc = -1;
			}
		}
		else{
			y_inc = 1;
			if(direction_y < 0){
				y_inc = -1;
			}
		}
		z.setPerron(x_inc, y_inc);
		/** si la facade est le long de l'axe y et la profondeure sur l'axe x */
		if(Math.abs(x_inc) > Math.abs(y_inc)){
			/** on prépare la facade */
			int start = cell.getY()- Utils.doubleToInt(Utils.moitie(y_size));
			int end = (start+y_size);
			if(y_size == 1){
				start = cell.getY();
				end = start+1;
			}
			/** on test en profondeur le long de chaques points de la façade */
			while(start < end){
				ArrayList<Integer> tmp = getXPath(cell.getX(), start, new ArrayList<Integer>(), x_size, x_inc);
				Iterator<Integer> iter = tmp.iterator();
				while(iter.hasNext()){
					int pos_x = iter.next();
					Cell comp = zone.getCell(pos_x, start);
					comp.resetBatType(bat.getType());
					zone.removeAvailable(comp);
					z.indexation(comp);
				}
				start++;
			}
		}
		/** si la facade est le long de l'axe x et la profondeure sur l'axe y */
		else{
			/** on prépare la facade */
			int start = cell.getX()- Utils.doubleToInt(Utils.moitie(x_size));
			int end = (start+x_size);
			if(x_size == 1){
				start = cell.getX();
				end = start+1;
			}
			/** on test en profondeur le long de chaques points de la façade */
			while(start < end){
				ArrayList<Integer> tmp = getYPath(start, cell.getY(), new ArrayList<Integer>(), y_size, y_inc);
				Iterator<Integer> iter = tmp.iterator();
				while(iter.hasNext()){
					int pos_y = iter.next();
					Cell comp =  zone.getCell(start, pos_y);
					comp.resetBatType(bat.getType());
					zone.removeAvailable(comp);
					z.indexation(comp);
				}
				start++;
			}
		}
		//mis en place des derniers elments de zone
		setEmplacement(z);
		z.setCenter();
		bat.setZone(z);
	}
	/** calcul l'emplacement */
	/** retourne la cellule en limite haut*/
	public void setEmplacement(Zone z){
		Cell haut = null;
		Cell bas = null;
		Cell gauche = null;
		Cell droite = null;
		ArrayList<Cell> candidats = z.getCells();
		Iterator<Cell> iter = candidats.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			Cell test = zone.getHaut(cell);
			if(!candidats.contains(test)){
				haut = test;
			}
			test = zone.getBas(cell);
			if(!candidats.contains(test)){
				bas = test;
			}
			test = zone.getGauche(cell);
			if(!candidats.contains(test)){
				gauche = test;
			}
			test = zone.getDroite(cell);
			if(!candidats.contains(test)){
				droite = test;
			}
		}
		z.setEmplacement(haut, bas, droite, gauche);
	}
	/** renvoie les coordonées de disponible sur x */
	private ArrayList<Integer> getXPath(int x, int y, ArrayList<Integer> results, int needed, int inc){
		Cell voisin = zone.getCell(x, y);
		if(voisin.getBatType() == Identifiants.vide){
			needed = needed -1;
			results.add(x);
			if(needed == 0){
				return results;
			}
			return getXPath(x+inc, y, results, needed, inc);
		}
		return null;
	}
	/** renvoie les coordonées de disponible sur y */
	private ArrayList<Integer> getYPath(int x, int y, ArrayList<Integer> results, int needed, int inc){
		Cell voisin = zone.getCell(x, y);
		if(voisin.getBatType() == Identifiants.vide){
			needed = needed -1;
			results.add(y);
			if(needed == 0){
				return results;
			}
			return getYPath(x, y+inc, results, needed, inc);
		}
		return null;
	}
	/** regarde parmis tout les candidats celui qui est le plus approprié */
	private Cell selectCandidat(int x_size, int y_size, ArrayList<Cell> disponibles){
		TreeMap<Double, Cell> selected = new TreeMap<Double, Cell>();
		Iterator<Cell> iter = disponibles.iterator();
		while(iter.hasNext()){
			boolean valid = false;
			double score = 0.0;
			Cell cell = iter.next();
			/** dit combien de cases sont occupées sur les axes */
			int direction_x = getXDirection(cell.getX(), cell.getY(), disponibles);
			int direction_y = getYDirection(cell.getX(), cell.getY(), disponibles);
			/** on met en place les parametres pour la promenade */
			int x_inc = 0;
			int y_inc = 0;
			if(Math.abs(direction_x) > Math.abs(direction_y)){
				x_inc = 1;
				if(direction_x < 0){
					x_inc = -1;
				}
			}
			else{
				y_inc = 1;
				if(direction_y < 0){
					y_inc = -1;
				}
			}
			/** si la facade est le long de l'axe y et la profondeure sur l'axe x */
			if(Math.abs(x_inc) > Math.abs(y_inc)){
				/** on prépare la facade */
				int start = cell.getY()- Utils.doubleToInt(Utils.moitie(y_size));
				int end = (start+y_size);
				if(y_size == 1){
					start = cell.getY();
					end = start+1;
				}
				/** on test en profondeur le long de chaques points de la façade */
				while(start < end){
					double score_pass = scoreXPath(cell.getX(), start, x_size, x_inc, 0, disponibles);
					if(score_pass != -1){
						start++;
						score += score_pass;
						valid = true;
					}
					else{
						start = end;
						valid = false;
					}
				}
			}
			/** si la facade est le long de l'axe x et la profondeure sur l'axe y */
			else{
				/** on prépare la facade */
				int start = cell.getX()- Utils.doubleToInt(Utils.moitie(x_size));
				int end = (start+x_size);
				if(x_size == 1){
					start = cell.getX();
					end = start+1;
				}
				/** on test en profondeur le long de chaques points de la façade */
				while(start < end){
					double score_pass = scoreYPath(start, cell.getY(), y_size, y_inc, 0, disponibles);
					if(score_pass != -1){
						start++;
						score += score_pass;
						valid = true;
					}
					else{
						start = end;
						valid = false;
					}
				}
			}
			/** si dispo et qu'en plus c'est plus oocupé que la normale on prend celle là */
			if(valid){
				selected.put(score, cell);
			}
		}
		Cell selection = null;
		if(selected.size() > 0){
			selection = selected.get(selected.lastKey());
		}
		return selection;
	}
	/** calcul le score en fonction du nombre de cellules de type vide et des voisins de 
	 * ces cellules vides pour une façade en X */
	private double scoreXPath(int x, int y, int needed, int inc, double score, ArrayList<Cell> disponibles){
		Cell voisin = zone.getCell(x, y);
		double scoring = scoreVoisinage(voisin, disponibles);
		if(scoring != -1){
			needed = needed -1;
			score += scoring;
			if(needed == 0){
				return score;
			}
			return scoreXPath(x+inc, y, needed, inc, score, disponibles);
		}
		return -1;
	}
	/** calcul le score en fonction du nombre de cellules de type vide et des voisins de 
	 * ces cellules vides pour une façade en Y */
	private double scoreYPath(int x, int y, int needed, int inc, double score, ArrayList<Cell> disponibles){
		Cell voisin = zone.getCell(x, y);
		double scoring = scoreVoisinage(voisin, disponibles);
		if(scoring != -1){
			needed = needed -1;
			score += scoring;
			if(needed == 0){
				return score;
			}
			return scoreYPath(x, y+inc, needed, inc, score, disponibles);
		}
		return -1;
	}
	/** calcul du score de la cellule dapres les autres cellules dans son entourage */
	private double scoreVoisinage(Cell cell, ArrayList<Cell> disponibles){
		double result = 0;
		if(cell != null && disponibles.contains(cell)){
			Cell haut = zone.getHaut(cell);
			Cell bas = zone.getBas(cell);
			Cell gauche = zone.getGauche(cell);
			Cell droite = zone.getDroite(cell);
			result += scoreAffiniteBat(haut);
			result += scoreAffiniteBat(bas);
			result += scoreAffiniteBat(gauche);
			result += scoreAffiniteBat(droite);
			return result;
		}
		return -1;
	}
	/** le score d'affinité d'une cellule batiment augmente d'avantage si on voisin est une route*/
	private double scoreAffiniteBat(Cell cell){
		//et oui c'est chelou mais on gagne des points quand le voisin est occupé
		if(cell != null){
			//si c'est une route ou une case dispo 
			if(cell.isFreeBloc()){
				return 1.0;
			}
		}
		return 0.0;
	}
/*----------------------------SELECTION DE LA DIRECTION--------------------------*/
	/** retourne l'inverse de la direction la plus occupée sur X*/
	private int getXDirection(int x, int y, ArrayList<Cell> disponibles){
		Cell cell = zone.getCell(x, y);
		Cell droite = zone.getDroite(cell);
		Cell gauche = zone.getGauche(cell);
		int count_droite = 0;
		int count_gauche = 0;
		//comptage directionnel 
		if(droite != null){
			if(disponibles.contains(droite)){
				count_droite = 1;
			}
			if(!disponibles.contains(droite)){
				count_droite = 2;
			}
		}
		if(gauche != null){
			if(disponibles.contains(gauche)){
				count_gauche = 1;
			}
			if(!disponibles.contains(gauche)){
				count_gauche = 2;
			}
		}
		if(count_droite >= count_gauche){
			return -count_droite;
		}
		if(count_gauche > count_droite){
			return count_gauche;
		}
		return 0;
	}
	/** retourne l'inverse de la direction la plus occupée sur X*/
	private int getYDirection(int x, int y, ArrayList<Cell> disponibles){		
		Cell cell = zone.getCell(x, y);
		Cell haut = zone.getHaut(cell);
		Cell bas = zone.getBas(cell);
		int count_haut = 0;
		int count_bas = 0;
		if(haut != null){
			if(disponibles.contains(haut) && !haut.isFreeBat()){
				count_haut = 1;
			}
			if(!disponibles.contains(haut) && haut.isFreeBloc()){
				count_haut = 2;
			}
		}
		if(bas != null){
			if(disponibles.contains(bas) && !bas.isFreeBat()){
				count_bas = 1;
			}
			if(!disponibles.contains(bas) && bas.isFreeBloc()){
				count_bas = 2;
			}
		}
		if(count_haut > count_bas){
			return -count_haut;
		}
		if(count_bas > count_haut){
			return count_bas;
		}
		return 0;
	}
/*------------------------GESTION DES MODELS------------------------------*/
	/** renvois la position de la texture sur les ordonnées ce qui correspond à un style graphie*/
	public int getTexY(){
		return 1;
	}
}