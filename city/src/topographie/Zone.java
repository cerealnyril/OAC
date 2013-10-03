package topographie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import tools.Identifiants;
import tools.ParamsGlobals;
import tools.Utils;


public class Zone {
	private ArrayList<Cell> cells, limit, availables, roads, canaux;//, rails;
	private ArrayList<Cell> noeuds, noeuds_dispos;
	private TreeMap<Integer, TreeMap<Integer, Cell>> index_on_x, index_on_y;
//	private Cell limit_haut, limit_bas, limit_gauche, limit_droite;
	private int taille_x, taille_y;
	private float centre_x, centre_y;
	private int id_quart, id_bloc, id_bat;
	private int perron, emplacement;
	private Cell station;
	private String type_txt;
	private Relief relief;
	
	public Zone(){
		cells = new ArrayList<Cell>();
		station = null;
		limit = new ArrayList<Cell>();
		index_on_x = new TreeMap<Integer, TreeMap<Integer, Cell>>();
		index_on_y = new TreeMap<Integer, TreeMap<Integer, Cell>>();
		centre_x = 0.0f;
		centre_y = 0.0f;
		taille_x = 0;
		taille_y = 0;
		perron = 0;
		id_quart = -1;
		id_bloc = -1;
		id_bat = -1;
		type_txt = "null";
		availables = new ArrayList<Cell>();
		roads = new ArrayList<Cell>();
		noeuds = new ArrayList<Cell>();
		noeuds_dispos = new ArrayList<Cell>();
		canaux = new ArrayList<Cell>();
		limit = new ArrayList<Cell>();
		relief = new Relief(this);
	}

/*--------------------------------ACCESSEURS--------------------------------*/
	/** renvois la cellule de la station */
	public Cell getStation(){
		return this.station;
	}
	/** retourne les cellules de limite */
	public ArrayList<Cell> getLimit(){
		return this.limit;
	}
	/** retourne les noeuds disponibles */
	public ArrayList<Cell> getNoeudsDispos(){
		return this.noeuds_dispos;
	}

	/** retourne toutes les limites situées en haut */
	public ArrayList<Cell> getLimitHaut(){
		ArrayList<Cell> result = new ArrayList<Cell>();
		int y = (int) (this.centre_y+(this.taille_y)/2);
		int start_x = (int) (this.centre_x-(this.taille_x/2));
		int end_x = (int) (this.centre_x+(this.taille_x/2));
		Iterator<Cell> iter = this.limit.iterator();
		Cell tmp;
		while(iter.hasNext()){
			tmp = iter.next();
			if((tmp.getY() == y || tmp.getY() == y+1 || tmp.getY() == y-1) && (tmp.getX() != start_x && tmp.getX() != end_x)){
				result.add(tmp);
			}
		}
		return result;
	}
	/** retourne toutes les limites situées en haut */
	public ArrayList<Cell> getLimitBas(){
		ArrayList<Cell> result = new ArrayList<Cell>();
		int y = (int) (this.centre_y-(this.taille_y)/2);
		int start_x = (int) (this.centre_x-(this.taille_x/2));
		int end_x = (int) (this.centre_x+(this.taille_x/2));
		Iterator<Cell> iter = this.limit.iterator();
		Cell tmp;
		while(iter.hasNext()){
			tmp = iter.next();
			if((tmp.getY() == y || tmp.getY() == y+1 || tmp.getY() == y-1) && (tmp.getX() != start_x && tmp.getX() != end_x)){
				result.add(tmp);
			}
		}
		return result;
	}
	/** retourne toutes les limites situées en haut */
	public ArrayList<Cell> getLimitGauche(){
		ArrayList<Cell> result = new ArrayList<Cell>();
		int x = (int) (this.centre_x-(this.taille_x)/2);
		int start_y = (int) (this.centre_y-(this.taille_y/2));
		int end_y = (int) (this.centre_y+(this.taille_y/2));
		Iterator<Cell> iter = this.limit.iterator();
		Cell tmp;
		while(iter.hasNext()){
			tmp = iter.next();
			if((tmp.getX() == x || tmp.getX() == x+1 || tmp.getX() == x-1) && (tmp.getY() != start_y && tmp.getY() != end_y)){
				result.add(tmp);
			}
		}
		return result;
	}
	/** retourne toutes les limites situées en haut */
	public ArrayList<Cell> getLimitDroite(){
		ArrayList<Cell> result = new ArrayList<Cell>();
		int x = (int) (this.centre_x+(this.taille_x)/2);
		int start_y = (int) (this.centre_y-(this.taille_y/2));
		int end_y = (int) (this.centre_y+(this.taille_y/2));
		Iterator<Cell> iter = this.limit.iterator();
		Cell tmp;
		while(iter.hasNext()){
			tmp = iter.next();
			if((tmp.getX() == x || tmp.getX() == x+1 || tmp.getX() == x-1) && (tmp.getY() != start_y && tmp.getY() != end_y)){
				result.add(tmp);
			}
		}
		return result;
	}
	/** retourne un id unique en long */
	public String getUniqueID(){
		String result = this.id_quart+"_"+this.id_bloc+"_"+this.id_bat;
		return result;
	}
	/** renvois l'emplacement du batiment, utilisé pour les commerces : 1 en haut, 2 au milieu, 3 en bas */
	public int getEmplacement(){
		return this.emplacement;
	}
	/** renvois le type bloc */
	public int getTypeBloc(){
		return cells.get(0).getBlocType();
	}
	/** renvois le type de bat */
	public int getTypeBat(){
		return cells.get(0).getBatType();
	}
	/** retourne le centre de la zone sur X */
	public float getCentreX(){
		return this.centre_x;
	}
	/** retourne le centre de la zone sur Y */
	public float getCentreY(){
		return this.centre_y;
	}
	/** retourne la taille de la zone sur X */
	public int getTailleX(){
		return this.taille_x;
	}
	/** retourne la taille de la zone sur Y */
	public int getTailleY(){
		return this.taille_y;
	}
	/** donne la position de dépars sur X */
	public int getStartX(){
		return Utils.doubleToInt((this.centre_x - Utils.moitie(this.taille_x)));
	}
	/** donne la position de dépars sur Y */
	public int getStartY(){
		return Utils.doubleToInt((this.centre_y - Utils.moitie(this.taille_y)));
	}
	/** donne la position de fin sur X */
	public int getEndX(){
		return Utils.doubleToInt((this.centre_x + Utils.moitie(this.taille_x)));
	}
	/** donne la position de fin sur Y */
	public int getEndY(){
		return Utils.doubleToInt((this.centre_y + Utils.moitie(this.taille_y)));
	}
	/** retourne la cellule en haut */
	public Cell getHaut(Cell cell){
		return index_on_x.get(cell.getX()).get(cell.getY()+1);
	}
	/** retourne la cellule en bas */
	public Cell getBas(Cell cell){
		return index_on_x.get(cell.getX()).get(cell.getY()-1);
	}
	/** retourne la cellule à gauche */
	public Cell getGauche(Cell cell){
		return index_on_y.get(cell.getY()).get(cell.getX()-1);
	}
	/** retourne la cellule à droite */
	public Cell getDroite(Cell cell){
		return index_on_y.get(cell.getY()).get(cell.getX()+1);
	}
	/** renvois une cellule particulière */
	public Cell getCell(int x, int y){
		if(index_on_x.get(x) != null && index_on_x.get(x).get(y) != null){
			return index_on_x.get(x).get(y);
		}
		return null;
	}
	/** renvois une cellule particulière et si elle n'est pas dans la zone renvois celle du relief */
	public Cell getCellRelief(int x, int y){
		if(index_on_x.get(x) != null && index_on_x.get(x).get(y) != null){
			return index_on_x.get(x).get(y);
		}
		return relief.getCell(x, y);
	}
	/** renvois l'identifiant du quartier */
	public int getIdQuartier(){
		return this.id_quart;
	}
	/** renvois l'identifiant du bloc */
	public int getIdBloc(){
		return this.id_bloc;
	}
	/** renvois l'identifiant du batiment */
	public int getIdBatiment(){
		return this.id_bat;
	}
	/** renvois le type en version text */
	public String getTypeTxt(){
		return this.type_txt;
	}
	/** renvois les cellules de routes */
	public ArrayList<Cell> getRoads(){
		return this.roads;
	}
	/** renvois les noeuds */
	public ArrayList<Cell> getNoeuds(){
		return this.noeuds;
	}
	/** renvois les cellules disponibles */
	public ArrayList<Cell> getAvailables(){
		return this.availables;
	}
	/** renvois les cellules de la zone */
	public ArrayList<Cell> getCells(){
		return this.cells;
	}
	/** renvois les cellules d'altitude */
	public ArrayList<Cell> getHeightCells(){
		return relief.getCells();
	}
	/** retourne les cellules de canaux */
	public ArrayList<Cell> getCanaux(){
		return this.canaux;
	}
	/** retourne le coté qui donne sur la route : 1 pour haut, 2 pour droite, 3 pour bas, 4 pour gauche */
	public int getPerron(){
		return this.perron;
	}
/*--------------------------------MODIFICATIONS--------------------------------*/
	/** enregistre les noeuds qui sont toujours disponibles */
	public void setNoeudsDispos(ArrayList<Cell> noeuds){
		this.noeuds_dispos = noeuds;
	}
	/** enregistre la cellule qui servira de station de depars */
	public void setStation(Cell cell){
		this.station = getStation(cell);
		station.resetUpperLayer(Identifiants.rails);
	}
	private Cell getStation(Cell cell){
		Cell test = null;
		Cell gauche = getGauche(cell);
		Cell droite = getDroite(cell);
		Cell haut = getHaut(cell);
		Cell bas = getBas(cell);
		if(routeOuLibre(haut)){
			test = haut;
		}
		else if(routeOuLibre(bas)){
			test = bas;
		}
		else if(routeOuLibre(gauche)){
			test = gauche;
		}
		else if(routeOuLibre(droite)){
			test = droite;
		}
		if(test == null){
			test = getStation(gauche);
		}
		cell = test;
		return cell;
	}
	/** principalement pour les batiments de type commerce dit si il est en haut en bas ou au milieu */
	public void setEmplacement(Cell haut, Cell bas, Cell gauche, Cell droite){
		if(routeOuLibre(haut) && routeOuLibre(gauche) && routeOuLibre(droite)){
			emplacement = 1;
		}
		else if(routeOuLibre(bas) && routeOuLibre(gauche) && routeOuLibre(droite)){
			emplacement = 3;
		}
		else if(routeOuLibre(gauche) && routeOuLibre(bas) && routeOuLibre(haut)){
			emplacement = 3;
		}
		else if(routeOuLibre(droite) && routeOuLibre(bas) && routeOuLibre(haut)){
			emplacement = 1;
		}
		else{
			emplacement = 2;
		}
	}
	private boolean routeOuLibre(Cell cell){
		if(cell != null){
			if(cell.getBlocType() == Identifiants.roadBloc || cell.getBlocType() == Identifiants.vide){
				return true;
			}
		}
		return false;
	}
	/** indique le coté sur lequel se place le perron du batiment */
	public void setPerron(int x, int y){
		//en premier est ce que ce sera l'axe x ou l'axe y?
		if(Math.abs(x) > Math.abs(y)){
			perron = 1;
			if(x < 0){
				perron = 3;
			}
		}
		else{
			perron = 2;
			if(Math.abs(y) < 0){
				perron = 4;
			}
		}
	}
	/** mise en place du centre de la zone */
	public void setCentre(float x, float y){
		this.centre_x = x;
		this.centre_y = y;
	}
	/** augmente la taille */
	public void augmenteTaille(int x, int y){
		this.taille_x = (taille_x+Math.abs(x));
		this.taille_y = (taille_y+Math.abs(y));
	}
	/** initialise la taille sur x */
	public void setTailleX(int v){
		this.taille_x = v;
	}
	/** initialise la taille sur y */
	public void setTailleY(int v){
		this.taille_y = v;
	}
	/** attribution identifiant quartier */
	public void setIdQuartier(int q){
		this.id_quart = q;
	}
	/** attribution identifiant bloc */
	public void setIdBloc(int b){
		this.id_bloc = b;
	}
	/** attribution identifiant batiment */
	public void setIdBatiment(int b){
		this.id_bat = b;
	}
	/** met en place le nom du type */
	public void setTypeTxt(String s){
		this.type_txt = s;
	}
	/** rajoute une cellule de route */
	public void ajoutRoute(Cell cell){
		cell.resetBlocType(Identifiants.roadBloc);
		//mise à jour
		ParamsGlobals.MANAGER.updateObject(cell);
		//entrée dans la liste des routes
		this.roads.add(cell);
	}
	
	/** rajoute une cellule de canal */
	public void ajoutCanal(Cell cell){
		if(cell.getBlocType() != Identifiants.canal){
			cell.resetBlocType(Identifiants.canal);
			//mise à jour
			ParamsGlobals.MANAGER.updateObject(cell);
			//entrée dans la liste des canaux
			this.canaux.add(cell);
		}
	}
	
	/** gere l'orientation et le type des routes des canaux des limites des rails peut importe ça depend du type
	 * en entrée. Aprés il fait le tris 
	 * @param type : c'est le type de cellule qu'on traite. 0 pour des routes, 1 pour des chemins (les cellules libres), 2 pour les canaux */
	public void setIntersectionAndRotation(int type){
		Iterator<Cell> iter = null;
		if(type == 0){
			iter = this.roads.iterator();
		}
		else if(type == 1){
			iter = this.availables.iterator();
		}
		else if(type == 2){
			iter = this.canaux.iterator();
		}
		while(iter.hasNext()){
			Cell cell = iter.next();
			//si c'est une intersection le problème est différent il faut choisir si c'est un croisement à 2, 3 ou 4
			if(((type == 0 || type == 1) && isIntersectionRoute(cell)) || (type == 2 && isIntersectionCanal(cell))){
				if(cell.isRoad()){
					this.noeuds.add(cell);
				}
				int nb = 0;
				boolean g = false;
				boolean d = false;
				boolean h = false;
				boolean b = false;
				//une premiere passe pour reduire les calculs
				Cell voisin_gauche = this.getGauche(cell);
				if(voisin_gauche != null){
					//si c'est une route
					if((type == 0 || type == 1) && voisin_gauche.isFreeBloc()){
						g = true;
						nb++;
					}
					//si c'est un canal
					else if(type == 2 && voisin_gauche.isCanal()){
						g = true;
						nb++;
					}
				}
				Cell voisin_droite = this.getDroite(cell);
				if(voisin_droite != null){
					if((type == 0 || type == 1) && voisin_droite.isFreeBloc()){
						d = true;
						nb++;
					}
					else if(type == 2 && voisin_droite.isCanal()){
						d = true;
						nb++;
					}
				}
				Cell voisin_haut = this.getHaut(cell);
				if(voisin_haut != null){
					if((type == 0 || type == 1) && voisin_haut.isFreeBloc()){
						h = true;
						nb++;
					}
					else if(type == 2 && voisin_haut.isCanal()){
						h = true;
						nb++;
					}
				}
				Cell voisin_bas = this.getBas(cell);
				if(voisin_bas != null){
					if((type == 0 || type == 1) && voisin_bas.isFreeBloc()){
						b = true;
						nb++;
					}
					else if(type == 2 && voisin_bas.isCanal()){
						b = true;
						nb++;
					}
				}
				//cas tous les voisins sont bons
				if(nb == 4){
					cell.setRotation(0);
				}
				//les cas ou trois des voisins sont bons
				else if(nb == 3){
					//gauche - bas - haut
					if(g && b && h){
						cell.setRotation(0);
					}
					//droite - bas - haut
					else if(d && b && h){
						cell.setRotation(2);
					}
					//gauche - droite - bas
					else if(g && d && b){
						cell.setRotation(1);
					}
					//gauche - droite - haut
					else if(g && d && h){
						cell.setRotation(3);
					}
				}
				//les cas ou seulement deux des voisins sont bons
				else if(nb == 2){
					//gauche - haut
					if(g && h){
						cell.setRotation(3);
					}
					//gauche - bas
					else if(g && b){
						cell.setRotation(0);
					}
					//droite - haut
					else if(d && h){
						cell.setRotation(2);
					}
					//droite - bas
					else if(d && b){
						cell.setRotation(1);
					}
				}
				//le fait de faire un setVoisin ne sert que pour les routes ou les canaux
				if(type == 0){
					cell.setVoisinRoutes(nb);
				}
				else if(type == 2){
					cell.setVoisinCanaux(nb);
				}
				
			}
			//si ce n'est pas une intersection alors le seul problème est de determiner si on dois rotater par rapport à l'axe par defaut
			else{
				Cell voisin_gauche = this.getGauche(cell);
				Cell voisin_droite = this.getDroite(cell);
				if(type == 0 || type == 1){
					if((voisin_gauche != null && voisin_gauche.isFreeBloc()) || (voisin_droite != null && voisin_droite.isFreeBloc())){
						cell.setRotation(1);
					}
				}
				else if(type == 2){
					if((voisin_gauche != null && voisin_gauche.isCanal()) || (voisin_droite != null && voisin_droite.isCanal())){
						cell.setRotation(1);
					}
				}
			}
		}
	}
	/** augmente le niveau du batiment */
	public void upgrade(){
		Iterator<Cell> iter = cells.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			cell.incrementHeight();
		}
	}
	/** set centre de la zone */
	public void setCenter(){
		double max_x = -1000000000;
		double max_y = -1000000000;
		double min_x = 1000000000;
		double min_y = 1000000000;
		Iterator<Cell> iter = cells.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			if(cell.getX() > max_x){
				max_x = cell.getX();
			}
			if(cell.getY() > max_y){
				max_y = cell.getY();
			}
			if(cell.getX() < min_x){
				min_x = cell.getX();
			}
			if(cell.getY() < min_y){
				min_y = cell.getY();
			}
		}
		int d_x = (int) (max_x+min_x);
		int d_y = (int) (max_y+min_y);
		this.centre_x = Utils.moitie(d_x);
		this.centre_y = Utils.moitie(d_y);
	}
	/** taille générale de la zone */
	public void refreshTaille(){
		double max_x = -1000000000;
		double max_y = -1000000000;
		double min_x = 1000000000;
		double min_y = 1000000000;
		Iterator<Cell> iter = limit.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			if(cell.getX() > max_x){
				max_x = cell.getX();
			}
			if(cell.getY() > max_y){
				max_y = cell.getY();
			}
			if(cell.getX() < min_x){
				min_x = cell.getX();
			}
			if(cell.getY() < min_y){
				min_y = cell.getY();
			}
		}
		taille_x = (int) (max_x-min_x)+1;
		taille_y = (int) (max_y-min_y)+1;
	}
	/** ajout d'une cellule aux cellules disponibles */
	public void ajoutAvailable(Cell cell){
		this.availables.add(cell);
	}
/*--------------------------------GESTION DES BLOCS PAR LES QUARTIERS------------------------------*/
	/** ajout dans les deux listes qui permettent de retrouver des 
	 * cellules sur x et sur y */
	public void indexation(Cell cell){
		TreeMap<Integer, Cell> tmp_x = index_on_x.get(cell.getX());
		if(tmp_x == null){
			tmp_x = new TreeMap<Integer, Cell>();
		}
		tmp_x.put(cell.getY(), cell);
		TreeMap<Integer, Cell> tmp_y = index_on_y.get(cell.getY());
		if(tmp_y == null){
			tmp_y = new TreeMap<Integer, Cell>();
		}
		tmp_y.put(cell.getX(), cell);
		cell.setIDQuartier(id_quart);
		cells.add(cell);
		index_on_x.put(cell.getX(), tmp_x);
		index_on_y.put(cell.getY(), tmp_y);
	}
	/** initialise les premieres cases disponibles autour de la forme de l'administration */
	public void initAvailables(ArrayList<Cell> cells, int id_bloc, Zone z){
		Iterator<Cell> iter = cells.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			int x = cell.getX();
			int y = cell.getY();
			Cell haut = this.index_on_x.get(x).get(y+1);
			Cell bas = this.index_on_x.get(x).get(y-1);
			Cell gauche = this.index_on_y.get(y).get(x-1);
			Cell droite = this.index_on_y.get(y).get(x+1);
			if(haut == null || bas == null || gauche == null || droite == null){
				cell.resetBlocType(Identifiants.vide);
				availables.add(cell);
				z.removeAvailable(cell);
			}
		}
	}
	/** enleve la cellule disponible des cellules occupée */
	public void removeOccupieds(ArrayList<Cell> to_remove){
		Iterator<Cell> iter = to_remove.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			for(int i = 0; i < cells.size(); i++){
				Cell test = cells.get(i);
				if(test.getX() == cell.getX() && test.getY() == cell.getY()){
					cells.remove(i);
				}
			}
		}
	}
	/** initialisation du relief */
	public void setRelief(TreeMap<Integer, ArrayList<Cell>> to_transmit){
		relief.populate(to_transmit);
	}
	/** bord infranchissable qui vas s'etendre au fur et à mesure que le quartier grandis*/
	public void initLimit(){
		relief.initCells();
		ArrayList<Cell> new_cells = new ArrayList<Cell>();
		int start_x = getStartX();
		int start_y = getStartY();
		int end_x = getEndX();
		int end_y = getEndY();
		for(int y = start_y; y < end_y; y++){
	    	for(int x = start_x; x < end_x; x++){
	    		if(x == start_x || x == (end_x-1) || y == start_y || y == (end_y-1)){
	    			Cell cell = relief.getCell(x, y);
	    			cell.resetBlocType(Identifiants.border);
	    			removeAvailable(cell);
	    			indexation(cell);
	    			new_cells.add(cell);
	    		}
	    	}
    	}
		limit = new_cells;
	}
	/** bord infranchissable qui vas s'etendre au fur et à mesure que le quartier grandis*/
	public void resizeLimit(TreeMap<Integer, ArrayList<Cell>> to_transmit){
		ArrayList<Cell> nouvelles = relief.expension();
		limit.removeAll(nouvelles);
		removeLimit();
		Iterator<Cell> iter = nouvelles.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			cell.resetBlocType(Identifiants.border);
			removeAvailable(cell);
			indexation(cell);
		}
		limit = nouvelles;
//		refreshTaille();
		relief.expendHeight(to_transmit, nouvelles);
//		relief.updateCanaux(nouvelles);
	}
	/** enleve la limite et la transforme en cellules disponibles */
	private void removeLimit(){
		/** enleve la limite */
		Iterator<Cell> iter = limit.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			cell.resetBlocType(-1);
			index_on_x.get(cell.getX()).remove(cell.getY());
			index_on_y.get(cell.getY()).remove(cell.getX());
			cells.remove(cell);
		}
	}
	/** met à jour l'indexe */
	public void updateIndexe(int x, int y){
		index_on_x = updateIndexX(x, y);
		index_on_y = updateIndexY(x, y);
		this.centre_x = centre_x+x;
		this.centre_y = centre_y+y;
	}
	
	/** met à jour l'indexe sur X */
	private TreeMap<Integer, TreeMap<Integer, Cell>> updateIndexX(int x, int y){
		TreeMap<Integer, TreeMap<Integer, Cell>> new_index_on_x = new TreeMap<Integer, TreeMap<Integer, Cell>>();
		Iterator<Integer> iter_prim = index_on_x.keySet().iterator();
		while(iter_prim.hasNext()){
			int prim_clef = iter_prim.next();
			TreeMap<Integer, Cell> content = new TreeMap<Integer, Cell>();
			Iterator<Integer> iter_sec = index_on_x.get(prim_clef).keySet().iterator();
			while(iter_sec.hasNext()){
				int sec_clef = iter_sec.next();
				Cell cell = index_on_x.get(prim_clef).get(sec_clef);
				content.put(sec_clef+y, cell);
			}
			int new_key = prim_clef+x;
			new_index_on_x.put(new_key, content);
		}
		return new_index_on_x;
		
	}
	/** met à jour l'indexe sur Y*/
	private TreeMap<Integer, TreeMap<Integer, Cell>> updateIndexY(int x, int y){
		TreeMap<Integer, TreeMap<Integer, Cell>> new_index_on_y = new TreeMap<Integer, TreeMap<Integer, Cell>>();
		Iterator<Integer> iter_prim = index_on_y.keySet().iterator();
		while(iter_prim.hasNext()){
			int prim_clef = iter_prim.next();
			TreeMap<Integer, Cell> content = new TreeMap<Integer, Cell>();
			Iterator<Integer> iter_sec = index_on_y.get(prim_clef).keySet().iterator();
			while(iter_sec.hasNext()){
				int sec_clef = iter_sec.next();
				Cell cell = index_on_y.get(prim_clef).get(sec_clef);
				content.put(sec_clef+x, cell);
			}
			int new_key = prim_clef+y;
			new_index_on_y.put(new_key, content);
		}
		return new_index_on_y;
	}
	/** deplace le centre de la zone */
	public void updateIndexQuartier(int x, int y){
		relief.updateIndexe(x, y);
		updateIndexe(x, y);
	}
	/** enleve la cellule occupée des cellules disponibles */
	public void removeAvailable(Cell cell){
		for(int i = 0; i < availables.size(); i++){
			Cell test = availables.get(i);
			if(test.getX() == cell.getX() && test.getY() == cell.getY()){
				availables.remove(i);
			}
		}
	}
	/** enleve la cellule occupée des cellules*/
	public void removeCell(Cell cell){
		for(int i = 0; i < cells.size(); i++){
			Cell test = cells.get(i);
			if(test.getX() == cell.getX() && test.getY() == cell.getY()){
				cells.remove(i);
			}
		}
	}
	/** Une cellule est une intersection si elle a deux voisins qui ne sont pas sur le meme axe */
	public boolean isIntersectionRoute(Cell cell){
		if((routeOuLibre(getGauche(cell)) || routeOuLibre(getDroite(cell))) 
			&&
			(routeOuLibre(getHaut(cell)) || routeOuLibre(getBas(cell)))){
			return true;
		}
		return false;
	}
	/** Une cellule est une intersection si elle a deux voisins qui ne sont pas sur le meme axe */
	public boolean isIntersectionCanal(Cell cell){
		if((getGauche(cell).isCanal() || getDroite(cell).isCanal()) 
				&&
			(getHaut(cell).isCanal() || getBas(cell).isCanal())){
			return true;
		}
		return false;
	}
	/** retourne les cellules sur la ligne en se basant sur le relief */
	public ArrayList<Cell> getCellLine(Cell source, Cell cible){
		ArrayList<Cell> result = new ArrayList<Cell>();
		if(source.getX() == cible.getX()){
			int start = source.getY();
			int end = cible.getY();
			int i = start;
			while(i != end){
				Cell cell = getCellRelief(source.getX(), i);
				result.add(cell);
				if(start > end){
					i--;
				}
				else{
					i++;
				}
			}
		}
		else if(source.getY() == cible.getY()){
			int start = source.getX();
			int end = cible.getX();
			int i = start;
			while(i != end){
				Cell cell = getCellRelief(i, source.getY());
				result.add(cell);
				if(start > end){
					i--;
				}
				else{
					i++;
				}
			}
		}
		return result;
	}
	/** Retourne les cellules entre deux cellules alignées */
	public ArrayList<Cell> getCellsOnAxe(Cell source, Cell cible){
		ArrayList<Cell> result = new ArrayList<Cell>();
		if(source.getX() == cible.getX()){
			int start = source.getY();
			int end = cible.getY();
			int i = start;
			while(i != end){
				Cell cell = getCell(source.getX(), i);
				if(cell == null){
					cell = relief.getCell(source.getX(), i);
					if(cell == null){
						cell = new Cell(source.getX(), i);
					}
					indexation(cell);
				}
				result.add(cell);
				cell.resetUpperLayer(Identifiants.rails);
				if(start > end){
					i--;
				}
				else{
					i++;
				}
			}
		}
		else if(source.getY() == cible.getY()){
			int start = source.getX();
			int end = cible.getX();
			int i = start;
			while(i != end){
				Cell cell = getCell(i, source.getY());
				if(cell == null){
					cell = relief.getCell(i, source.getY());
					if(cell == null){
						cell = new Cell(i, source.getY());
					}
					indexation(cell);
				}
				result.add(cell);
				cell.resetUpperLayer(Identifiants.rails);
				if(start > end){
					i--;
				}
				else{
					i++;
				}
			}
		}
		return result;
	}
/*--------------------------------------OUTILS---------------------------------------------*/
	/** dis si les cellules sont voisines */
	public boolean isVoisin(Cell source, Cell cible){
		if(getHaut(source) == cible || getBas(source) == cible || getGauche(source) == cible || getDroite(source) == cible){
			return true;
		}
		return false;
	}
	/** dit si le chemin est libre */
	public ArrayList<Cell> getClearPath(Cell source, Cell cible){
		ArrayList<Cell> result = new ArrayList<Cell>();
		if(source.getX() == cible.getX()){
			int start = source.getY();
			int end = cible.getY();
			int i = start;
//			System.out.println("Sur le meme X on vas dans Y de "+start+" à "+end);
			while(i != end){
				Cell test = getCellRelief(source.getX(), i);
				if(!routeOuLibre(test) && !test.isCanal()){
//					System.out.println("Et merde on viens de rencontrer un "+test.getBatType());
					return null;
				}
				if(start > end){
					i--;
				}
				else{
					i++;
				}
				result.add(test);
			}
			return result;
		}
		else if(source.getY() == cible.getY()){
			int start = source.getX();
			int end = cible.getX();
			int i = start;
//			System.out.println("Sur le meme Y on vas dans X de "+start+" à "+end);
			while(i != end){
				Cell test = getCellRelief(i, source.getY());
				if(!routeOuLibre(test) && !test.isCanal()){
//					System.out.println("Et merde on viens de rencontrer un "+test.getBatType());
					return null;
				}
				if(start > end){
					i--;
				}
				else{
					i++;
				}
				result.add(test);
			}
			return result;
		}
		return null;
	}

	/** fonction qui vérifie si la cellule est suffisament basse pour devenir un canal et si elle n'est pas trop pret du quartier administratif de
	 * depars */
	public boolean lowEnought(Cell cell){
		double max = ParamsGlobals.LVL_CANAUX*ParamsGlobals.HEIGHT;
		//on test si c'est assez bas
		if(cell != null && cell.getZ() < max){
				return true;
		}
		return false;
	}
	/** fonction qui boucle toutes les cellules disponibles pour enlever celles qui ne servent à rien */
	public void sweepAvailables(){
		ArrayList<Cell> to_remove = new ArrayList<Cell>();
		Iterator<Cell> iter = availables.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			Cell haut = getHaut(cell);
			Cell bas = getBas(cell);
			Cell gauche = getGauche(cell);
			Cell droite = getDroite(cell);
			//test si aucune cellule autour n'est disponible alors on transforme en route
			if(!notNullAndType(haut, Identifiants.vide) && !notNullAndType(bas, Identifiants.vide) && !notNullAndType(gauche, Identifiants.vide) && !notNullAndType(droite, Identifiants.vide)){
				ajoutRoute(cell);
				to_remove.add(cell);
			}
			//si la cellule peut devenir une continuité de route
			if((notNullAndType(gauche, Identifiants.roadBloc) && notNullAndType(droite, Identifiants.roadBloc)) || 
					(notNullAndType(haut, Identifiants.roadBloc) && notNullAndType(bas, Identifiants.roadBloc))){
				ajoutRoute(cell);
				to_remove.add(cell);
			}
			//si la cellule peut devenir une continuité de canal
			if((notNullAndType(gauche, Identifiants.canal) && notNullAndType(droite, Identifiants.canal)) || 
					(notNullAndType(haut, Identifiants.canal) && notNullAndType(bas, Identifiants.canal))){
				ajoutCanal(cell);
				to_remove.add(cell);
			}
			//pour les routes sur les angles
			if((notNullAndType(haut, Identifiants.roadBloc) && (notNullAndType(droite, Identifiants.roadBloc) || notNullAndType(gauche, Identifiants.roadBloc)))
					|| (notNullAndType(bas, Identifiants.roadBloc) && (notNullAndType(droite, Identifiants.roadBloc) || notNullAndType(gauche, Identifiants.roadBloc)))
					){
				ajoutRoute(cell);
				to_remove.add(cell);
			}
		}
		availables.removeAll(to_remove);
	}
	/** verifie */
	public boolean notNullAndType(Cell cell, int type){
		if(cell != null && cell.getBlocType() == type){
			return true;
		}
		return false;
	}
}
