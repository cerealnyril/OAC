package monde;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


import structure.Batiment;
import structure.Bloc;
import structure.Croissance;
import structure.Frontiere;
import structure.Quartier;
import structure.RLE;
import tools.ParamsGlobals;
import topographie.Cell;


/** pour le moment cette classe gère toute la création de la ville et de ses composants 
 * gérés par le document XML */
public class Ville {
	/** gestion de la grande horloge */
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private TreeMap<Integer, Quartier> listeQuartiers;
	private String nom;
	private int age, heure, id;
    //pour la cartographie
    private int current_x, current_y, side, tour;
	private int taille;
	private Quartier citadelle;

	public Ville(String n) {
		heure = 0;
		nom = n;
		listeQuartiers = new TreeMap<Integer, Quartier>();
		//pour la cartographie
		current_x = -1;
		current_y = 0;
		side = 0;
		tour = 1;
		id = ParamsGlobals.MANAGER.registerObject(this);
	}
/*------------------------------------ACCESSEURS--------------------------------------------*/	
/*-- Pour la ville */
	public String getNom(){
		return this.nom;
	}
	public int getAge(){
		return this.age;
	}
	public int getHeure(){
		return this.heure;
	}
	public int getID(){
		return this.id;
	}
	public ArrayList<Integer> getQuartiersID(){
		return new ArrayList<Integer>(listeQuartiers.keySet());
	}
	/** retourne la taille maximale de la ville sur un carré avec une puissance de 2*/
	public int getTaille(){
		return this.taille;
	}
/*-- Pour les quartiers */
	public Quartier getQuartier(int id){
		return this.listeQuartiers.get(id);
	}
	public String getNomQuart(int id){
		return this.listeQuartiers.get(id).getNom();
	}
	public int getAdmBlocsID(int id){
		return this.listeQuartiers.get(id).getAdministrationID();
	}
	public ArrayList<Integer> getComBlocsID(int id){
		return this.listeQuartiers.get(id).getCommercesIDs();
	}
	public ArrayList<Integer> getProdBlocsID(int id){
		return this.listeQuartiers.get(id).getProductionsIDs();
	}
	public ArrayList<Integer> getHabBlocsID(int id){
		return this.listeQuartiers.get(id).getHabitationsIDs();
	}
	public TreeMap<Integer, String> getPopQuart(int id){
		return this.listeQuartiers.get(id).getInteraction().getPopQuart();
	}
	/** Retourne tous les blocs pour l'affichage */
	public ArrayList<Bloc> getAllBlocs(){
		ArrayList<Bloc> all = new ArrayList<Bloc>();
		Iterator<Integer> iter_q = this.listeQuartiers.keySet().iterator();
		while(iter_q.hasNext()){
			Quartier q = getQuartier(iter_q.next());
			Iterator<Integer> iter_b = q.getBlocsID().iterator();
			while(iter_b.hasNext()){
				all.add(q.getBloc(iter_b.next()));
			}
		}
		return all;
	}
	/** Retourne tous les batiments pour l'affichage */
	public ArrayList<Batiment> getAllBats(){
		ArrayList<Batiment> all = new ArrayList<Batiment>();
		Iterator<Integer> iter_q = this.listeQuartiers.keySet().iterator();
		while(iter_q.hasNext()){
			Quartier q = getQuartier(iter_q.next());
			Iterator<Integer> iter_b = q.getBlocsID().iterator();
			while(iter_b.hasNext()){
				Bloc b = q.getBloc(iter_b.next());
				Iterator<Integer> iter_bat = b.getBatimentsID().iterator();
				while(iter_bat.hasNext()){
					all.add(b.getBatiment(iter_bat.next()));
				}
			}
		}
		return all;
	}
	/**Retourne toute les routes */
	public ArrayList<Cell> getAllRoutes(){
		ArrayList<Cell> routes = new ArrayList<Cell>();
		Iterator<Integer> iter_q = this.listeQuartiers.keySet().iterator();
		while(iter_q.hasNext()){
			Quartier q = getQuartier(iter_q.next());
			routes.addAll(q.getRoads());
		}
		return routes;
	}
	/**Retourne tous les chemins */
	public ArrayList<Cell> getAllChemins(){
		ArrayList<Cell> routes = new ArrayList<Cell>();
		Iterator<Integer> iter_q = this.listeQuartiers.keySet().iterator();
		while(iter_q.hasNext()){
			Quartier q = getQuartier(iter_q.next());
			routes.addAll(q.getFreeCells());
		}
		return routes;
	}
	/**Retourne tous les canaux */
	public ArrayList<Cell> getAllCanaux(){
		ArrayList<Cell> routes = new ArrayList<Cell>();
		Iterator<Integer> iter_q = this.listeQuartiers.keySet().iterator();
		while(iter_q.hasNext()){
			Quartier q = getQuartier(iter_q.next());
			routes.addAll(q.getCanaux());
		}
		return routes;
	}

	/** Retourne tous les RLE*/
	public ArrayList<RLE> getAllRLEs(){
		ArrayList<RLE> rles = new ArrayList<RLE>();
		Iterator<Integer> iter_q = this.listeQuartiers.keySet().iterator();
		while(iter_q.hasNext()){
			Quartier q = getQuartier(iter_q.next());
			rles.addAll(q.getRLEs());
		}
		return rles;
	}
	/** Retourne tous les quartiers */
	public ArrayList<Quartier> getAllQuartiers(){
		ArrayList<Quartier> result = new ArrayList<Quartier>();
		Iterator<Integer> iter = getQuartiersID().iterator();
		while(iter.hasNext()){
			result.add(getQuartier(iter.next()));
		}
		return result;
	}
/*------------------------------------CHARGEMENT---------------------------------------*/
	/** initialisation des transports en commun */
/*	public void setMetros(){
		//initialise les ligne de metro en interne
		Iterator<Integer> iter = this.getQuartiersID().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Quartier quartier = this.getQuartier(id);
			quartier.updateInnerLines();
		}
		//ferme les ligne de metro avec les voisins
		iter = this.getQuartiersID().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Quartier quartier = this.getQuartier(id);
			quartier.closeLines();
		}
	}*/
	/** lancement de tout les timers de tout les quartiers 
	 * @param serv */
	public void setTimer(){
		internalClock();
	}
	
	/** attribut le nom de la ville */
	public void setNom(String nom){
		this.nom = nom;
	}
	/** attribut l'age de la ville */
	public void setAge(int age){
		this.age = age;
	}
	/** attribut de l'heure de la ville */
	public void setHeure(int heure){
		this.heure = heure;
	}
	/** ajout le quartier */
	public void ajoutQuartier(Quartier quartier){
		if(quartier.getID() == -1){
			initQuartier(quartier);
		}
		else{
			placeQuartier(quartier);
		}
		ParamsGlobals.MANAGER.updateObject(quartier);
	}
	/** place la citadelle de depars */
	private void initQuartier(Quartier quartier){
/*		Croissance croissance = new Croissance(0, 0);	
		quartier.setCroissance(croissance);
		quartier.initZone(0, 0, Globals.GAP, Globals.GAP);
		citadelle = quartier;*/
		int limit = ParamsGlobals.GAP;
		if(ParamsGlobals.GAP%2 == 0){
			limit+= ParamsGlobals.MULT_TAILLE;
		}
		quartier.initZone(0, 0, limit, limit);
		citadelle = quartier;
//		refreshTaille();
	}
	/** positionne les quartiers de la ville */
	private void placeQuartier(Quartier quartier){
		int limit = ParamsGlobals.GAP;
		if(ParamsGlobals.GAP%2 == 0){
			limit+= ParamsGlobals.MULT_TAILLE;
		}
		Croissance croissance = new Croissance(current_x, current_y);	
		quartier.setCroissance(croissance);
		quartier.initZone(current_x*limit, current_y*limit, limit, limit);
		incrementPos();
		ParamsGlobals.MANAGER.updateObject(quartier);
		listeQuartiers.put(quartier.getID(), quartier);
		refreshTaille();
	}
	/** rajoute un bloc dans un quartier de la ville  */
	public void ajoutBloc(Bloc bloc, int id_q){
		Quartier quartier = this.listeQuartiers.get(id_q);
		quartier.ajoutBloc(bloc);
		/** test si la ville a grossie */
		int expansion = quartier.asExpanded();
		if(expansion > 0){
			deplaceQuartierVoisin(quartier, expansion);
			refreshTaille();
		}
		ParamsGlobals.MANAGER.updateObject(bloc);
		ParamsGlobals.MANAGER.updateObject(quartier);
	}
/*------------------------------------HORLOGE--------------------------------------------*/
	/** Thread de la ville. Gere l'affichage sur terminal et les backups */
	public void internalClock() {
		final Ville ville = this;
		final Runnable checker = new Runnable() {
			public void run() { 
				periodeTemps();
				heure++;
				System.out.println(heure+" "+ville.getAge());
			}
		};
		scheduler.scheduleAtFixedRate(checker, ParamsGlobals.HEURES, ParamsGlobals.HEURES, TimeUnit.SECONDS);
	}

	public void periodeTemps(){
            if(heure <= 9){
			Logger.getLogger(Ville.class.getName()).log(Level.INFO, heure+" du jour");
		}
		else if(heure <= 18){
			Logger.getLogger(Ville.class.getName()).log(Level.INFO, heure+" de la nuit");
		}
		else{
			Logger.getLogger(Ville.class.getName()).log(Level.INFO, heure+" de autre");
		}
		if(heure >= ParamsGlobals.HEURES_JOUR){
			updateQuartiers();
//			updateTransports();
			heure = 0;
			age++;
			refreshTaille();
			ParamsGlobals.MANAGER.executeManagement();
		}
	}
	/** lance un cycle d'analyse sur le quartier */
	public void updateQuartiers(){
		Iterator<Integer> iter = listeQuartiers.keySet().iterator();
		while(iter.hasNext()){
			int id_quart = iter.next();
			Quartier quartier = listeQuartiers.get(id_quart);
			quartier.cycle();
			// test si la ville a grossie
			int expansion = quartier.asExpanded();
			if(expansion > 0){
				deplaceQuartierVoisin(quartier, expansion);
				refreshTaille();
			}
		}
	}
/*==================================POUR LA CARTOGRAPHIE=====================================*/
	/** recalcule les tailles sur x et y */
	private void refreshTaille(){
		int id_droite = -1;
		int id_gauche = -1;
		int id_haut = -1;
		int id_bas = -1;
		int max_droite = -1000000000;
		int max_gauche = 1000000000;
		int max_haut = -1000000000;
		int max_bas = 1000000000;
		int taille_x = 0;
		int taille_y = 0;
		int result = 0;
		//on commence par prendre les quartiers les plus sur les bords et on garde les ids
		Iterator<Integer> iter = listeQuartiers.keySet().iterator();
		while(iter.hasNext()){
			int clef = iter.next();
			Quartier quartier = listeQuartiers.get(clef);
			if(quartier.getCentreX() > max_droite){
				max_droite = (int) quartier.getCentreX();
				id_droite = quartier.getID();
			}
			if(quartier.getCentreX() < max_gauche){
				max_gauche = (int) quartier.getCentreX();
				id_gauche = quartier.getID();
			}
			if(quartier.getCentreY() > max_haut){
				max_haut = (int) quartier.getCentreY();
				id_haut = quartier.getID();
			}
			if(quartier.getCentreY() < max_bas){
				max_bas = (int) quartier.getCentreY();
				id_bas = quartier.getID();
			}
		}
		//puis on extrait les tailles sur x et sur y
		if(id_droite != -1 && id_gauche != -1){
			Quartier droite = listeQuartiers.get(id_droite);
			Quartier gauche = listeQuartiers.get(id_gauche);
			taille_x = 
					Math.abs(gauche.getZone().getStartX())+Math.abs(droite.getZone().getEndX());
//					(int) (Math.abs(gauche.getCentreX()-Utils.moitiePos(gauche.getTailleX()))+
//					Math.abs(droite.getCentreX()+Utils.moitiePos(droite.getTailleX())));
		}
		if(id_haut != -1 && id_bas != -1){
			Quartier haut = listeQuartiers.get(id_haut);
			Quartier bas = listeQuartiers.get(id_bas);
			taille_y = 
					Math.abs(bas.getZone().getStartY())+Math.abs(haut.getZone().getEndY());
//					(int) (Math.abs(bas.getCentreY()-Utils.moitiePos(bas.getTailleY()))+
//					Math.abs(haut.getCentreY()+Utils.moitiePos(haut.getTailleY())));
		}
		//on prend la plus grande
		result = taille_y;
		if(taille_x > taille_y){
			result = taille_x;
		}
		result = result*ParamsGlobals.TAILLE_PLATEAU;
		double exposant = 0;
		//on retourne la puissance de deux
		while(result > Math.pow(2.0, exposant)){
			exposant++;
		}
		taille = (int) Math.pow(2.0, exposant)+1;
	}
	/*-----------------------------POSITIONNEMENT DES QUARTIERS----------------------------*/
	/** mise à jour de tout les voisins de tout les quartiers */
	public void setVoisins(){
		Iterator<Integer> iter = listeQuartiers.keySet().iterator();
		while(iter.hasNext()){
			int key = iter.next();
			Quartier quartier = listeQuartiers.get(key);
			float min_x = quartier.getZone().getStartX();
			float min_y = quartier.getZone().getStartY();
			float max_x = quartier.getZone().getEndX();
			float max_y = quartier.getZone().getEndY();
			//et apres pour tout les voisins
			TreeMap<Integer, Quartier> all_quart = new TreeMap<Integer, Quartier>();
			all_quart.put(citadelle.getID(), citadelle);
			all_quart.putAll(listeQuartiers);
			Iterator<Integer> iter_test = /*listeQuartiers*/all_quart.keySet().iterator();
			while(iter_test.hasNext()){
				int clef = iter_test.next();
				if(clef != key){
					Quartier test = all_quart.get(clef);
					float test_min_x = test.getZone().getStartX();
					float test_min_y = test.getZone().getStartY();
					float test_max_x = test.getZone().getEndX();
					float test_max_y = test.getZone().getEndY();
					//si le point est la dedans alors c'est occupé
					//voisin de gauche et de droite
					if((min_y == test_min_y) && (max_y == test_max_y)){
						//gauche
						if(min_x == test_max_x){
							quartier.getCroissance().setGauche(clef);
//							if(test.getID() != -1){
								quartier.setVoisinGauche(test);
//							}
//							System.out.println("quartier "+quartier.getID()+" voisin gauche "+test.getID());
						}
						//droite
						else if(max_x == test_min_x){
							quartier.getCroissance().setDroite(clef);
//							if(test.getID() != -1){
								quartier.setVoisinDroite(test);
//							}
//							System.out.println("quartier "+quartier.getID()+" voisin droite "+test.getID());
						}
					}
					//voisin du haut et du bas
					if((min_x == test_min_x) && (max_x == test_max_x)){
						//haut
						if(max_y == test_min_y){
							quartier.getCroissance().setHaut(clef);
//							if(test.getID() != -1){
								quartier.setVoisinHaut(test);
//							}
//							System.out.println("quartier "+quartier.getID()+" voisin haut "+test.getID());
						}
						//bas
						if(min_y == test_max_y){
							quartier.getCroissance().setBas(clef);
//							if(test.getID() != -1){
								quartier.setVoisinBas(test);
//							}
//							System.out.println("quartier "+quartier.getID()+" voisin bas "+test.getID());
						}
					}
				}
			}
		}
	}
/*-----------------------------CALCUL DES POSITIONS-----------------------------------*/
	/** incremente la position des centres à l'initialisation */
	private void incrementPos(){
		switch(side){
			case 0:
				posLeft();
			break;
			case 1:
				posUp();
			break;
			case 2:
				posRight();
			break;
			case 3:
				posDown();
			break;
		}
	}
	/** placement selon l'axe de gauche */
	private void posLeft(){
		current_y++;
		changeSide(current_y);
	}
	/** placement selon l'axe de droite */
	private void posRight(){
		current_y--;
		changeSide(current_y);
	}
	/** placement selon l'axe en haut */
	private void posUp(){
		current_x++;
		changeSide(current_x);
	}
	/** placement selon l'axe en bas*/
	private void posDown(){
		current_x--;
		changeSide(current_x);
	}
	/** changement de coté si limite atteinte*/
	private void changeSide(int pos){
		if(Math.abs(pos) == tour){
			side++;
			if(side>3){
				side = 0;
			}
		}
		if(isOccupied(current_x, current_y)){
			if(side == 0){
				tour++;
				current_x = -tour;
			}
		}
	}
	/** verifie si la zone est occupée */
	private boolean isOccupied(int pos_x, int pos_y){
		Iterator<Integer> iter = listeQuartiers.keySet().iterator();
		while(iter.hasNext()){
			int clef = iter.next();
			Quartier quartier = listeQuartiers.get(clef);
			float min_x = quartier.getZone().getStartX();
			float min_y = quartier.getZone().getStartY();
			float max_x = quartier.getZone().getEndX();
			float max_y = quartier.getZone().getEndY();
			//si le point est la dedans alors c'est occupé
			if(((pos_x*ParamsGlobals.GAP) > min_x) && ((pos_x*ParamsGlobals.GAP) < max_x) 
					&& ((pos_y*ParamsGlobals.GAP) > min_y) && ((pos_y*ParamsGlobals.GAP) < max_y)){
				return true;
			}
		}
		return false;
	}
	/** déplace un quartier */
	public void deplaceQuartierVoisin(Quartier quartier, int nb_exp){
		Croissance crois = quartier.getCroissance();
		ArrayList<Integer> to_moves = crois.quartierToMove();
		Iterator<Integer> iter = to_moves.iterator();
		while(iter.hasNext()){
			Quartier quartier_to_move = listeQuartiers.get(iter.next());
			int to_x = crois.toX()*nb_exp*2;
			int to_y = crois.toY()*nb_exp*2;
			quartier_to_move.move(to_x, to_y);
			ParamsGlobals.MANAGER.updateObject(quartier_to_move);
		}
	}
/*-------------------------------------ACCESSEURS---------------------------------------*/
	/** renvois toutes les cellules de tout les quartiers de toute la ville de tout le monde entier */
/*	public ArrayList<Cell> getAllCells(){
		ArrayList<Cell> cells = new ArrayList<Cell>();
		Iterator<Integer> iter = listeQuartiers.keySet().iterator();
		while(iter.hasNext()){
			int clef = iter.next();
			cells.addAll(listeQuartiers.get(clef).getAllCells());
		}
		cells.addAll(citadelle.getAllCells());
		return cells;
	}*/
}