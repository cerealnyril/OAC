package topographie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;


import tools.ParamsGlobals;
import tools.Utils;

public class Relief {
	private TreeMap<Integer, TreeMap<Integer, Cell>> index_on_x, index_on_y;
	private List<Cell> cells;
	private Zone zone_courante;
	/** donne le niveau de detail du relief */
	private static int DETAIL = (int) (ParamsGlobals.GAP/3.0);
	
	public Relief(Zone z){
		index_on_x = new TreeMap<Integer, TreeMap<Integer, Cell>>();
		index_on_y = new TreeMap<Integer, TreeMap<Integer, Cell>>();
		cells = Collections.synchronizedList(new ArrayList<Cell>());
		zone_courante = z;
	}
	/** Fonction qui initialise et créé le relief */
	public void initCells(){
		int start_x = zone_courante.getStartX();
		int start_y = zone_courante.getStartY();
		int end_x = zone_courante.getEndX();
		int end_y = zone_courante.getEndY();
		for(int y = start_y; y < end_y; y++){
	    	for(int x = start_x; x < end_x; x++){
	    		Cell cell = new Cell(x, y);
	    		cells.add(cell);
	    		if(index_on_x.get(x) != null){
	    			index_on_x.get(x).put(y, cell);
	    		}
	    		else{
	    			TreeMap<Integer, Cell> nouvelle = new TreeMap<Integer, Cell>();
	    			nouvelle.put(y, cell);
	    			index_on_x.put(x, nouvelle);
	    		}
	    		if(index_on_y.get(y) != null){
	    			index_on_y.get(y).put(x, cell);
	    		}
	    		else{
	    			TreeMap<Integer, Cell> nouvelle = new TreeMap<Integer, Cell>();
	    			nouvelle.put(x, cell);
	    			index_on_y.put(y, nouvelle);
	    		}
	    	}
    	}
		
	}
	/** Fonction qui créé les cellules pour toute la zone*/
	public void populate(TreeMap<Integer, ArrayList<Cell>> transmit){
		//iteration pour chaque quartier qui transmet
		Iterator<Integer> iter = transmit.keySet().iterator();
		while(iter.hasNext()){
			ArrayList<Cell> to_transmit = transmit.get(iter.next());
			transmitHeight(to_transmit.get(0), to_transmit.get(1), to_transmit.get(2));
		}
		int iterations = DETAIL;
		generate(new ArrayList<Integer>(index_on_x.keySet()), 
				new ArrayList<Integer>(index_on_y.keySet()), 
				iterations);
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
	/** transmet la hauteur des cellules des quartiers contigues */
	public void transmitHeight(Cell un, Cell deux, Cell trois){
		//si c'est un voisin sur X
		if(index_on_x.get(un.getX()) != null && index_on_x.get(deux.getX()) != null && index_on_x.get(trois.getX()) != null){
			if(index_on_x.get(un.getX()) != null && ((index_on_y.get(un.getY()+1) != null) || (index_on_y.get(un.getY()-1) != null))){
				Cell cell = null;
				if(index_on_y.get(un.getY()+1) != null){
					cell = index_on_x.get(un.getX()).get(un.getY()+1);
				}
				else if(index_on_y.get(un.getY()-1) != null){
					cell = index_on_x.get(un.getX()).get(un.getY()-1);
				}
				if(cell != null){
					cell.resetZ(un.getZ());
				}
			}
			if(index_on_x.get(deux.getX()) != null && ((index_on_y.get(deux.getY()+1) != null) || (index_on_y.get(deux.getY()-1) != null))){
				Cell cell = null;
				if(index_on_y.get(deux.getY()+1) != null){
					cell = index_on_x.get(deux.getX()).get(deux.getY()+1);
				}
				else if(index_on_y.get(deux.getY()-1) != null){
					cell = index_on_x.get(deux.getX()).get(deux.getY()-1);
				}
				if(cell != null){
					cell.resetZ(deux.getZ());
				}
			}
			if(index_on_x.get(trois.getX()) != null && ((index_on_y.get(trois.getY()+1) != null) || (index_on_y.get(trois.getY()-1) != null))){
				Cell cell = null;
				if(index_on_y.get(trois.getY()+1) != null){
					cell = index_on_x.get(trois.getX()).get(trois.getY()+1);
				}
				else if(index_on_y.get(trois.getY()-1) != null){
					cell = index_on_x.get(trois.getX()).get(trois.getY()-1);
				}
				if(cell != null){
					cell.resetZ(trois.getZ());
				}
			}
		}
		//si c'est un voisin sur Y
		if(index_on_y.get(un.getY()) != null && index_on_y.get(deux.getY()) != null && index_on_y.get(trois.getY()) != null){
			if(index_on_y.get(un.getY()) != null && ((index_on_x.get(un.getX()+1) != null) || (index_on_x.get(un.getX()-1) != null))){
				Cell cell = null;
				if(index_on_x.get(un.getX()+1) != null){
					cell = index_on_y.get(un.getY()).get(un.getX()+1);
				}
				else if(index_on_x.get(un.getX()-1) != null){
					cell = index_on_y.get(un.getY()).get(un.getX()-1);
				}
				if(cell != null){
					cell.resetZ(un.getZ());
				}
			}
			if(index_on_y.get(deux.getY()) != null && ((index_on_x.get(deux.getX()+1) != null) || (index_on_x.get(deux.getX()-1) != null))){
				Cell cell = null;
				if(index_on_x.get(deux.getX()+1) != null){
					cell = index_on_y.get(deux.getY()).get(deux.getX()+1);
				}
				else if(index_on_x.get(deux.getX()-1) != null){
					cell = index_on_y.get(deux.getY()).get(deux.getX()-1);
				}
				if(cell != null){
					cell.resetZ(deux.getZ());
				}
			}
			if(index_on_y.get(trois.getY()) != null && ((index_on_x.get(trois.getX()+1) != null) || (index_on_x.get(trois.getX()-1) != null))){
				Cell cell = null;
				if(index_on_x.get(trois.getX()+1) != null){
					cell = index_on_y.get(trois.getY()).get(trois.getX()+1);
				}
				else if(index_on_x.get(trois.getX()-1) != null){
					cell = index_on_y.get(trois.getY()).get(trois.getX()-1);
				}
				if(cell != null){
					cell.resetZ(trois.getZ());
				}
			}
		}
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
	/** retourne la cellule en diagonal bas gauche */
	public Cell getDBG(Cell cell){
		if(index_on_x.get(cell.getX()-1) != null){
			return index_on_x.get(cell.getX()-1).get(cell.getY()-1);
		}
		return null;
	}
	/** retourne la cellule en diagonal bas droit */
	public Cell getDBD(Cell cell){
		if(index_on_x.get(cell.getX()+1) != null){
			return index_on_x.get(cell.getX()+1).get(cell.getY()-1);
		}
		return null;
	}
	/** retourne la cellule en diagonal haut gauche */
	public Cell getDHG(Cell cell){
		if(index_on_x.get(cell.getX()-1) != null){
			return index_on_x.get(cell.getX()-1).get(cell.getY()+1);
		}
		return null;
	}
	/** retourne la cellule en diagonal haut droit */
	public Cell getDHD(Cell cell){
		if(index_on_x.get(cell.getX()+1) != null){
			return index_on_x.get(cell.getX()+1).get(cell.getY()+1);
		}
		return null;
	}
	/** Fonction qui attribue plusieurs hauteurs en une seule fois */
	private void generateForPack(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		double total = 0.0;
		double somme = 0.0;
		Iterator<Integer> iter_x = index_x.iterator();
		while(iter_x.hasNext()){
			int x = iter_x.next();
			Iterator<Integer> iter_y = index_y.iterator();
			while(iter_y.hasNext()){
				int y = iter_y.next();
				Cell cell = getCell(x, y);
				if(cell != null && cell.getZ() != -1){
					somme += cell.getZ();
					total ++;
				}
			}
		}
		double avg = somme/total;
		iter_x = index_x.iterator();
		while(iter_x.hasNext()){
			int x = iter_x.next();
			Iterator<Integer> iter_y = index_y.iterator();
			while(iter_y.hasNext()){
				int y = iter_y.next();
				Cell cell = getCell(x, y);
				if(cell != null && cell.getZ() == -1){
					cell.resetZ(noisify(avg));
				}
			}
		}
	}
	/** Fonction de génération du relief 
	 * @param index_y 
	 * @param index_x */
	private void generate(ArrayList<Integer> index_x, ArrayList<Integer> index_y, int iterations){
		int start_x = index_x.get(0);
		int end_x = index_x.get(index_x.size()-1);
		int start_y = index_y.get(0);
		int end_y = index_y.get(index_y.size()-1);
		if((end_x - start_x <= 3) || (end_y - start_y <= 3)){
			generateForPack(index_x, index_y);
			iterations = 0;
		}
		//récupération des huits cellules qui nous permettrons de faire nos 4 carrés suivants
		Cell gh = getGH(index_x, index_y);
		Cell gm = getGM(index_x, index_y);
		Cell gb = getGB(index_x, index_y);
		Cell mh = getMH(index_x, index_y);
		Cell mm = getMM(index_x, index_y);
		Cell mb = getMB(index_x, index_y);
		Cell dh = getDH(index_x, index_y);
		Cell dm = getDM(index_x, index_y);
		Cell db = getDB(index_x, index_y);
		//calcul des hauteurs
		attributeHeightAtPoint(gh, gb, gm);
		attributeHeightAtPoint(dh, db, dm);
		attributeHeightAtPoint(gh, dh, mh);
		attributeHeightAtPoint(gb, db, mb);
		attributeHeightAtPoint(mh, mb, mm);
		//envois de l'itération si il ne reste plus rien à faire pour le relief ce tour ci
		iterations = iterations-1;
		//ici on vas essayer d'obtenir les 4 carrés reformés 
		if(iterations > 0){
			//carré gauche haut
			index_x = getQuadX(gh, mh);
			index_y = getQuadY(gm, gh);
			generate(index_x, index_y, iterations);
			//carré gauche bas
			index_x = getQuadX(gm, mm);
			index_y = getQuadY(gb, gm);
			generate(index_x, index_y, iterations);
			//carré droit haut
			index_x = getQuadX(mh, dh);
			index_y = getQuadY(mm, mh);
			generate(index_x, index_y, iterations);
			//carré droit bas
			index_x = getQuadX(mm, dm);
			index_y = getQuadY(mb, mm);
			generate(index_x, index_y, iterations);
		}
	}
	/** enleve la limite et la transforme en cellules disponibles */
	public void removeLimit(ArrayList<Cell> limit){
		/** enleve la limite */
//		System.out.println("limit "+limit.size());
		Iterator<Cell> iter = limit.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			cell.resetBlocType(-1);
			index_on_x.get(cell.getX()).remove(cell.getY());
			index_on_y.get(cell.getY()).remove(cell.getX());
//			cells.remove(cell);
		}
	}
	/** entend le plateau de jeu et recalcul les hauteurs pour la nouvelle zone 
	 * @param to_transmit */
	public ArrayList<Cell> expension(){
		ArrayList<Cell> nouvelles = new ArrayList<Cell>();
		int start_x = zone_courante.getStartX();
		int start_y = zone_courante.getStartY();
		int end_x = zone_courante.getEndX();
		int end_y = zone_courante.getEndY();
		for(int y = start_y; y < end_y; y++){
	    	for(int x = start_x; x < end_x; x++){
	    		if(x == (start_x+1) || x == (end_x-1) || y == (start_y+1) || y == (end_y-1)){
	    			Cell cell = getCell(x, y);
	    			if(cell == null){
	    				cell = new Cell(x, y);
	    				cells.add(cell);
	    			}
	    			nouvelles.add(cell);
		    		if(index_on_x.get(x) != null){
		    			index_on_x.get(x).put(y, cell);
		    		}
		    		else{
		    			TreeMap<Integer, Cell> nouvelle = new TreeMap<Integer, Cell>();
		    			nouvelle.put(y, cell);
		    			index_on_x.put(x, nouvelle);
		    		}
		    		if(index_on_y.get(y) != null){
		    			index_on_y.get(y).put(x, cell);
		    		}
		    		else{
		    			TreeMap<Integer, Cell> nouvelle = new TreeMap<Integer, Cell>();
		    			nouvelle.put(x, cell);
		    			index_on_y.put(y, nouvelle);
		    		}
	    		}
	    	}
    	}
		return nouvelles;
	}
	/** met en place la hauteur par rapport au voisin */
	public void expendHeight(TreeMap<Integer, ArrayList<Cell>> voisins, ArrayList<Cell> cells){
//		System.out.println(voisins);
		Iterator<Cell> iter = cells.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			int total = 0;
			int nb_div = 0;
			//calcul de la moyenne des voisins sur le plateau
			Cell gauche = getGauche(cell);
			if(gauche != null && gauche.getZ() != -1){
				total += 2*gauche.getZ();
				nb_div+=2;
			}
			Cell droite = getDroite(cell);
			if(droite != null && droite.getZ() != -1){
				total += 2*droite.getZ();
				nb_div+=2;
			}
			Cell haut = getHaut(cell);
			if(haut != null && haut.getZ() != -1){
				total += 2*haut.getZ();
				nb_div+=2;
			}
			Cell bas = getBas(cell);
			if(bas != null && bas.getZ() != -1){
				total += 2*bas.getZ();
				nb_div+=2;
			}
			//pour les diagonales quand il y'en a 
			Cell dhg = getDHG(cell);
			if(dhg != null && dhg.getZ() != -1){
				total += dhg.getZ();
				nb_div++;
			}
			Cell dhd = getDHD(cell);
			if(dhd != null && dhd.getZ() != -1){
				total += dhd.getZ();
				nb_div++;
			}
			Cell dbg = getDBG(cell);
			if(dbg != null && dbg.getZ() != -1){
				total += dbg.getZ();
				nb_div++;
			}
			Cell dbd = getDBD(cell);
			if(dbd != null && dbd.getZ() != -1){
				total += dbd.getZ();
				nb_div++;
			}
			//ajout à la moyenne de la hauteur de la cellule du voisin
			Iterator<Integer> iter_q = voisins.keySet().iterator();
			while(iter_q.hasNext()){
				ArrayList<Cell> tests = voisins.get(iter_q.next());
				Iterator<Cell> iter_test = tests.iterator();
				boolean stop = false;
				while(!stop && iter_test.hasNext()){
					Cell test = iter_test.next();
					if(test.getX() == cell.getX() && (test.getY()+1 == cell.getY() || test.getY()-1 == cell.getY())){
						total += 10*test.getZ();
						stop = true;
						nb_div+=10;
					}
					else if(test.getY() == cell.getY() && (test.getX()+1 == cell.getX() || test.getX()-1 == cell.getX())){
						total += 10*test.getZ();
						stop = true;
						nb_div+=10;
					}
				}
			}
			if(nb_div > 0){
				cell.resetZ(total/nb_div);
			}
		}
	}
	/** retourne l'index des clefs sur X pour le nouveau carré */
	private ArrayList<Integer> getQuadX(Cell start, Cell end){
		if(start.getX() != end.getX()){
			ArrayList<Integer> filtre_end = new ArrayList<Integer>(index_on_x.tailMap(start.getX()).keySet());
			ArrayList<Integer> filtre_start = new ArrayList<Integer>(index_on_x.headMap(end.getX()).keySet());
			filtre_end.retainAll(filtre_start);
			filtre_end.add(end.getX());
			return filtre_end;
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(end.getX());
		return result;
	}
	/** retourne l'index des clefs sur Y pour le nouveau carré */
	private ArrayList<Integer> getQuadY(Cell start, Cell end){
		if(start.getY() != end.getY()){
			ArrayList<Integer> filtre_end = new ArrayList<Integer>(index_on_y.tailMap(start.getY()).keySet());
			ArrayList<Integer> filtre_start = new ArrayList<Integer>(index_on_y.headMap(end.getY()).keySet());
			filtre_end.retainAll(filtre_start);
			filtre_end.add(end.getY());
			return filtre_end;
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(end.getY());
		return result;
	}
	/** attribue l'altitude à la cellule centrale par rapport aux cellules de bord*/
	private void attributeHeightAtPoint(Cell start_cell, Cell end_cell, Cell cell){
		if(start_cell.getZ() == -1){
			start_cell.resetZ(Math.random()*ParamsGlobals.HEIGHT);
		}
		if(end_cell.getZ() == -1){
			end_cell.resetZ(Math.random()*ParamsGlobals.HEIGHT);
		}
		double start = start_cell.getZ();
		double end= end_cell.getZ();
		double moyenne = noisify(Utils.avg(start, end));
		if(cell.getZ() == -1){
			cell.resetZ(moyenne);
		}
	}
	private double noisify(double initial){
		double result = initial;
		double aleatoire = (Math.random())/ParamsGlobals.HEIGHT;
		if(Math.random() > 0.5){
			aleatoire = -aleatoire;
		}
		if(result > aleatoire){
			result += aleatoire;
		}
		return result;
	}
	/** renvois la cellule en haut à gauche 
	 * @param index_y 
	 * @param index_x */
	private Cell getGH(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(0);
		int y = index_y.get(index_y.size()-1);
		return index_on_x.get(x).get(y);
	}
	
	/** renvois la cellule au milieu à gauche */
	private Cell getGM(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(0);
		int y = index_y.get(Utils.moitiePos(index_y.size()-1));
		return index_on_x.get(x).get(y);
	}
	
	/** renvois la cellule en bas à gauche */
	private Cell getGB(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(0);
		int y = index_y.get(0);
		return index_on_x.get(x).get(y);
	}
	
	/** renvois la cellule en haut au milieu */
	private Cell getMH(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(Utils.moitiePos(index_x.size()-1));
		int y = index_y.get(index_y.size()-1);
		return index_on_x.get(x).get(y);
	}
	
	/** renvois la cellule au milieu au milieu */
	private Cell getMM(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(Utils.moitiePos(index_x.size()-1));
		int y = index_y.get(Utils.moitiePos(index_y.size()-1));
		return index_on_x.get(x).get(y);
	}
	
	/** renvois la cellule en bas au milieu */
	private Cell getMB(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(Utils.moitiePos(index_x.size()-1));
		int y = index_y.get(0);
		return index_on_x.get(x).get(y);
	}
	
	/** renvois la cellule en haut à droite */
	private Cell getDH(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(index_x.size()-1);
		int y = index_y.get(index_y.size()-1);
		return index_on_x.get(x).get(y);
	}
	
	/** renvois la cellule au milieu à droite */
	private Cell getDM(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(index_x.size()-1);
		int y = index_y.get(Utils.moitiePos(index_y.size()-1));
		return index_on_x.get(x).get(y);
	}
	
	/** renvois la cellule en bas à droit */
	private Cell getDB(ArrayList<Integer> index_x, ArrayList<Integer> index_y){
		int x = index_x.get(index_x.size()-1);
		int y = index_y.get(0);
		return index_on_x.get(x).get(y);
	}
	
	/** fonction qui retourne la cellule de relief */
	public Cell getCell(int x, int y){
		if(index_on_x.get(x) != null){
			return index_on_x.get(x).get(y);
		}
		return null;
	}
	/** met à jour l'indexe */
	public void updateIndexe(int x, int y){
		TreeMap<Integer, TreeMap<Integer, Cell>> new_index_on_x = new TreeMap<Integer, TreeMap<Integer, Cell>>();
		Iterator<Integer> iter_ind_x = index_on_x.keySet().iterator();
		while(iter_ind_x.hasNext()){
			int prim_key = iter_ind_x.next();
			TreeMap<Integer, Cell> content = index_on_x.get(prim_key);
			TreeMap<Integer, Cell> new_content = new TreeMap<Integer, Cell>();
			Iterator<Integer> iter = content.keySet().iterator();
			while(iter.hasNext()){
				int sec_key = iter.next();
				Cell cell = content.get(sec_key);
				cell.resetX(cell.getX()+x);
				cell.resetY(cell.getY()+y);
				new_content.put(cell.getY(), cell);
			}
			new_index_on_x.put(prim_key+x, new_content);
		}
		index_on_x = new_index_on_x;
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
		index_on_y = new_index_on_y;
	}
	/** renvois toutes les cellules de relief */
	public ArrayList<Cell> getCells() {
		return new ArrayList<Cell>(this.cells);
	}
}
