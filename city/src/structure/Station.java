package structure;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import tools.Identifiants;
import topographie.Cell;
import topographie.Zone;


public class Station extends Batiment{
	private Zone zone_quartier;
	private ArrayList<Noeud> non_eco, eco;
	private ArrayList<Rail> rails;
	private int jour;
	private Noeud limit_haut, limit_bas, limit_droite, limit_gauche, depart;
	private boolean haut, bas, gauche, droite;
	
	public Station(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.stationBat;
		setTaille();
		this.eco = new ArrayList<Noeud>();
		this.non_eco = new ArrayList<Noeud>();
		this.rails = new ArrayList<Rail>();
		this.jour = 0;
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
	
	@Override
	public int getConsommationEnergie() {
		return 0;
	}
/*==============================FONCTIONNALITES METRO AERIEN==============================*/
/*----------------------------------------ACCESSEURS-----------------------------------*/
	/** retourne les rails eux memes */
	public ArrayList<Rail> getRLEs(){
//		System.out.println("J'ai combien de rails dans ma besace??? "+rails.size());
		return rails;
	}
	/** dit si il y'a une zone courante */
	public boolean asZone(){
		if(this.zone_quartier != null){
			return true;
		}
		return false;
	}
	/** Fonction de nettoyage*/
	public void clearStructures(){
/*		Iterator<Rail> iter = rails.iterator();
		while(iter.hasNext()){
			iter.next().releaseCells();
		}*/
		rails.clear();
	}
/*----------------------------------------SETTEURS-------------------------------------*/
	/** Met à jour le jour pour les RLE */
	public void upJour(){
		this.jour++;
	}
	/** lie la zone courante 
	 * @param zone_droite 
	 * @param zone_gauche 
	 * @param zone_bas 
	 * @param zone_haut */
	public void setCurrentZones(Zone z, Zone zone_haut, Zone zone_bas, Zone zone_gauche, Zone zone_droite){
		this.zone_quartier = z;
		zone_quartier.setStation(zone.getCell((int) zone.getCentreX(), (int) zone.getCentreY()));
		if(zone_haut != null && zone_haut.getIdQuartier() != -1){
			this.haut = true;
		}
		if(zone_bas != null && zone_bas.getIdQuartier() != -1){
			this.bas = true;
		}
		if(zone_gauche != null && zone_gauche.getIdQuartier() != -1){
			this.gauche = true;
		}
		if(zone_droite != null && zone_droite.getIdQuartier() != -1){
			this.droite = true;
		}
	}
	
	/** Met à jour le graph de navigation interne */
	public void updateGraph(){
		makeNoeuds();
		makeArcs();
	}

	public void updateRails(){
		computePaths(depart);
		if(limit_haut != null){
			List<Noeud> etapes = getEtapes(limit_haut);
			Iterator<Noeud> iter = etapes.iterator();
			Noeud source  = depart;
			Noeud cible = null;
			while(iter.hasNext()){
				cible = iter.next();
				makeRail(source, cible);
				source = cible;
			}
		}
		if(limit_bas != null){
			List<Noeud> etapes = getEtapes(limit_bas);
			Iterator<Noeud> iter = etapes.iterator();
			Noeud source  = depart;
			Noeud cible = null;
			while(iter.hasNext()){
				cible = iter.next();
				makeRail(source, cible);
				source = cible;
			}
		}
		if(limit_gauche != null){
			List<Noeud> etapes = getEtapes(limit_gauche);
			Iterator<Noeud> iter = etapes.iterator();
			Noeud source  = depart;
			Noeud cible = null;
			while(iter.hasNext()){
				cible = iter.next();
				makeRail(source, cible);
				source = cible;
			}
		}
		if(limit_droite != null){
			List<Noeud> etapes = getEtapes(limit_droite);
			Iterator<Noeud> iter = etapes.iterator();
			Noeud source  = depart;
			Noeud cible = null;
			while(iter.hasNext()){
				cible = iter.next();
				makeRail(source, cible);
				source = cible;
			}
		}
	}
	/** dit si la suite de cellules est sur X */
	private void makeRail(Noeud source, Noeud cible){
		ArrayList<Cell> tmp = this.zone_quartier.getCellLine(source.getCell(), cible.getCell());
		int x = tmp.size();
		int y = 1;
		if(source.getCell().getY() == cible.getCell().getY()){
			x = 1;
			y = tmp.size();
		}
		Rail rail = new Rail(tmp, y, x, this.id_quartier, this.jour);
		rails.add(rail);
	}
	private void makeNoeuds(){
		Cell cell_haut = null;
		Cell cell_bas = null;
		Cell cell_gauche = null;
		Cell cell_droite = null;
		int max_haut = -100000;
		int max_bas = 100000;
		int max_gauche = 100000;
		int max_droite = -100000;
		//si c'est la premiere fois on prend toutes les intersections 
		if(non_eco.size() == 0){
			Iterator<Cell> iter = zone_quartier.getNoeuds().iterator();
			while(iter.hasNext()){
				Cell test = iter.next();
				Noeud nouveau = new Noeud(test);
				non_eco.add(nouveau);
				if(max_haut < test.getY()){
					cell_haut = test;
					max_haut = test.getY();
				}
				if(max_bas > test.getY()){
					cell_bas = test;
					max_bas = test.getY();
				}
				if(max_gauche > test.getX()){
					cell_gauche = test;
					max_gauche = test.getX();
				}
				if(max_droite < test.getX()){
					cell_droite = test;
					max_droite = test.getX();
				}
			}
			this.depart = new Noeud(this.zone_quartier.getStation());
			non_eco.add(depart);
		}
		//sinon on récupère les nouveaux noeuds
		else{
			Iterator<Cell> iter_new = zone_quartier.getNoeuds().iterator();
			while(iter_new.hasNext()){
				Cell test_new = iter_new.next();
				Iterator<Noeud> iter_old = this.non_eco.iterator();
				boolean test = false;
				while(iter_old.hasNext() && !test){
					Cell test_old = iter_old.next().getCell();
					if((test_new.getX() == test_new.getX()) && (test_new.getY() == test_old.getY())){
						test = true;
					}
				}
				if(!test){
					Noeud nouveau = new Noeud(test_new);
					non_eco.add(nouveau);
				}
			}
		}
		//on rajoute les noeuds les plus à gauche droite haut et bas en fonction des voisins disponibles
		Iterator<Cell> iter_limit = zone_quartier.getLimit().iterator();
		while(iter_limit.hasNext()){
			Cell test = iter_limit.next();
			if((haut) && (cell_haut != null) && (this.limit_haut == null)){
				if((test.getX() == cell_haut.getX()) && (test.getY() > cell_haut.getY())){
					this.limit_haut = new Noeud(test);
					non_eco.add(this.limit_haut);
				}
			}
			if((bas) && (cell_bas != null) && (this.limit_bas == null)){
				if(test.getX() == cell_bas.getX() && (test.getY() < cell_bas.getY())){
					this.limit_bas = new Noeud(test);
					non_eco.add(this.limit_bas);
				}
			}
			if((gauche) && (cell_gauche != null) && (this.limit_gauche == null)){
				if(test.getY() == cell_gauche.getY() && (test.getX() < cell_gauche.getX())){
					this.limit_gauche = new Noeud(test);
					non_eco.add(this.limit_gauche);
				}
			}
			if((droite) && (cell_droite != null) && (this.limit_droite == null)){
				if(test.getY() == cell_droite.getY() && (test.getX() > cell_droite.getX())){
					this.limit_droite = new Noeud(test);
					non_eco.add(this.limit_droite);
				}
			}
		}
	}
	
	private void makeArcs(){
		ArrayList<Noeud> tmp_comp = new ArrayList<Noeud>();
		tmp_comp.addAll(non_eco);
		Iterator<Noeud> iter_source = non_eco.iterator();
		while(iter_source.hasNext()){
			Noeud noeud_source = iter_source.next();
			//on commence par mettre des distances halucinement longues
			int dist_haut = 100000;
			int dist_bas = 100000;
			int dist_gauche = 100000;
			int dist_droite = 100000;
			//on set les voisins à null
			Noeud voisin_gauche = null;
			Noeud voisin_bas = null;
			Noeud voisin_droite = null;
			Noeud voisin_haut = null;
			//et on compare tout
			Iterator<Noeud> iter_cible = tmp_comp.iterator();
			while(iter_cible.hasNext()){
				Noeud noeud_cible = iter_cible.next();
				//bien évidément si ce ne sont pas les memes noeuds
				if((noeud_source.getCell().getX() != noeud_cible.getCell().getX()) || (noeud_source.getCell().getY() != noeud_cible.getCell().getY())){
					//si ils sont sur le meme X
					if(noeud_source.getCell().getX() == noeud_cible.getCell().getX()){
						int distance = Math.abs(noeud_cible.getCell().getY()-noeud_source.getCell().getY());
						if((distance < dist_haut) && (noeud_source.getCell().getY() < noeud_cible.getCell().getY())){
							dist_haut = Math.abs(distance);
							voisin_haut = noeud_cible;
						}
						// si on descend et si c'est le moins loin
						else if(distance < dist_bas && (noeud_source.getCell().getY() > noeud_cible.getCell().getY())){
							dist_bas = Math.abs(distance);
							voisin_bas = noeud_cible;
						}
					}
					//sinon si ils sont sur le meme Y
					else if(noeud_source.getCell().getY() == noeud_cible.getCell().getY()){
						int distance = Math.abs(noeud_cible.getCell().getX())-Math.abs(noeud_source.getCell().getX());
						if((distance < dist_droite) && (noeud_source.getCell().getX() < noeud_cible.getCell().getX())){
							dist_droite = Math.abs(distance);
							voisin_droite = noeud_cible;
						}
						// si on descend et si c'est le moins loin
						else if((distance < dist_gauche) && (noeud_source.getCell().getX() > noeud_cible.getCell().getX())){
							dist_gauche = Math.abs(distance);
							voisin_gauche = noeud_cible;
						}
					}
				}
			}
			//on s'occupe maintenant des arcs
			ArrayList<Arc> tmp = new ArrayList<Arc>();
			if(voisin_bas != null){
				ArrayList<Cell> path = zone_quartier.getClearPath(noeud_source.getCell(), voisin_bas.getCell());
				if(path != null){
					tmp.add(new Arc(voisin_bas, 1.0));
				}
			}
			if(voisin_haut != null){
				ArrayList<Cell> path = zone_quartier.getClearPath(noeud_source.getCell(), voisin_haut.getCell());
				if(path != null){
					tmp.add(new Arc(voisin_haut, 1.0));
				}
			}
			if(voisin_gauche != null){
				ArrayList<Cell> path = zone_quartier.getClearPath(noeud_source.getCell(), voisin_gauche.getCell());
				if(path != null){
					tmp.add(new Arc(voisin_gauche, 1.0));
				}
			}
			if(voisin_droite != null){
				ArrayList<Cell> path = zone_quartier.getClearPath(noeud_source.getCell(), voisin_droite.getCell());
				if(path != null){
					tmp.add(new Arc(voisin_droite, 1.0));
				}
			}
			Arc[] arcs = new Arc[tmp.size()];
			Iterator<Arc> iter_tmp = tmp.iterator();
			int i = 0;
			while(iter_tmp.hasNext()){
				Arc arc = iter_tmp.next();
				arcs[i] = arc;
				i++;
			}
			noeud_source.adjacencies = arcs;
			if(voisin_gauche != null && voisin_droite != null && voisin_haut != null && voisin_bas != null){
				tmp_comp.remove(noeud_source);
				this.eco.add(noeud_source);
			}
		}
		this.non_eco = tmp_comp;
		uploadNoeudsDispos();
	}
	/** Utilitaire pour mettre les noeuds dispos dans la zone */
	private void uploadNoeudsDispos(){
		Cell tmp;
		ArrayList<Cell> result = new ArrayList<Cell>();
		Iterator<Noeud> iter = this.non_eco.iterator();
		while(iter.hasNext()){
			tmp = iter.next().getCell();
			if(tmp.isRoad()){
				result.add(tmp);
			}
		}
		zone_quartier.setNoeudsDispos(result);
	}
/*---------------------------CALCUL DES CHEMINS LES PLUS COURS----------------------------*/
	public static List<Noeud> getEtapes(Noeud target){
        List<Noeud> path = new ArrayList<Noeud>();
        for (Noeud vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
	
	public static void computePaths(Noeud source){
        source.minDistance = 0.;
        PriorityQueue<Noeud> vertexQueue = new PriorityQueue<Noeud>();
        vertexQueue.add(source);
        while (!vertexQueue.isEmpty()) {
        	Noeud u = vertexQueue.poll();
            for (Arc e : u.adjacencies)
            {
                Noeud v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                	vertexQueue.remove(v);
                	v.minDistance = distanceThroughU ;
                	v.previous = u;
                	vertexQueue.add(v);
                }
            }
        }
	}
/*-------------------------CLASSES DE GRAPH----------------------------*/
	/** classe de stockage des noeuds */
	private class Noeud implements Comparable<Noeud>{
		public Cell cell;
	    public Arc[] adjacencies;
	    public double minDistance = Double.POSITIVE_INFINITY;
	    public Noeud previous;
	    public Noeud(Cell cell) { 
	    	this.cell = cell;
	    }
	    public int compareTo(Noeud other){
	        return Double.compare(minDistance, other.minDistance);
	    }
	    public Cell getCell(){
	    	return this.cell;
	    }
	}
	/** classe de stockage des arcs */
	class Arc{
	    public final Noeud target;
	    public final double weight;
	    public Arc(Noeud argTarget, double argWeight){ 
	    	target = argTarget; weight = argWeight; 
	    }
	}
}
