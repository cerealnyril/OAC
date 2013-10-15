package elements2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;

import tools.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Classe qui gère la construction d'une ligne à partir de tous les rails */
public class Ligne {
	
	private ObjectMap<Integer, Array<Waypoint>> waypoints;
	private ObjectMap<Integer, Array<Chemin>> all_chemins;
	private ObjectMap<Integer, Batiment> stations;
	private Array<Rail> rails;
	private Array<Rail> close_rails;
	private TreeMap<Integer, Noeud> noeuds;
	private boolean arc_done, updated;
	private Group labels;
	
	public Ligne(){
		this.waypoints = new ObjectMap<Integer, Array<Waypoint>>();
		this.all_chemins = new ObjectMap<Integer, Array<Chemin>>();
		this.stations = new ObjectMap<Integer, Batiment>();
		this.rails = new Array<Rail>();
		this.close_rails = new Array<Rail>();
		this.noeuds = new TreeMap<Integer, Noeud>();
		this.arc_done = false;
		this.updated = false;
		this.labels = new Group();
	}
/*----------------------------------ACCESSEUR-----------------------------------*/	
	/** Dis si on a mis à jour */
	public boolean isUpdated(){
		if(updated){
			updated = false;
			return true;
		}
		return false;
	}
	/** Retourne le groupe de labels */
	public Group getLabels(){
		return this.labels;
	}
	/** Donne une station en fonction de l'identifiant du quartier */
	public Vector2 getStation(int id_q){
		return this.stations.get(id_q).getPosition();
	}
	
	/** Retourne toutes les stations */
	public ObjectMap<Integer, Batiment> getStations(){
		return this.stations;
	}
	
	/** Indique si les arcs logiques sont formés */
	public boolean isArcDone(){
		return this.arc_done;
	}
	/** Renvois un point depuis son identifiant 
	 * @param id_q */
	private Waypoint getPoint(int id_next, int id_q) {
		Waypoint res = null;
		Iterator<Waypoint> iter_p = waypoints.get(id_q).iterator();
		while(iter_p.hasNext() && (res == null)){
			Waypoint test = iter_p.next();
			if(test.getID() == id_next){
				res = test;
			}
		}
		return res;
	}
	/** Retourne le tableau de vecteur en utilisant le graphe de navigation à partir d'une etape initiale vers une etapes finale */
	public Array<Vector2> getPath(int start, int end){
		Array<Vector2> result = new Array<Vector2>();
		//on commence par trouver le chemin de navigation 
		computePaths(getNoeud(start));
		List<Noeud> etapes = getEtapes(getNoeud(end));
		//a partir de ce chemin on vas se remplir une liste de résultat avec toutes les etapes intermediaires
		Noeud previous = null, current = null;
		Iterator<Noeud> iter_etapes = etapes.iterator();
		while(iter_etapes.hasNext()){
			Noeud etape = iter_etapes.next();
			current = etape;
			if((previous != null) && (current != null)){
				result.addAll(getPathIntermediaire(previous.getID(), current.getID()));
			}
			previous = current;
		}
		return result;
	}
	
	/** Retourne le tableau de vecteur de chemin pour aller d'un point à un point b mais pour des points directement adjacents */
	private Array<Vector2> getPathIntermediaire(int start, int end){
		Array<Vector2> result = new Array<Vector2>();
		Array<Chemin> chemins = all_chemins.get(start);
		boolean stop = false;
		//on calcul le chemin pour aller jusqu'au bout du quartier
		Chemin selected = null;
		if(chemins != null){
			Iterator<Chemin> iter = chemins.iterator();
			while(iter.hasNext() && (!stop)){
				Chemin test = iter.next();
				if(test.getDirQ() == end){
					selected = test;
					stop = true;
				}
			}
		}
		if(selected != null){
			result = selected.getEtapes();
			//et on calcul le chemin pour aller de ce bout de quartier jusqu'à la station du quartier suivant
			chemins = all_chemins.get(end);
			stop = false;
			selected = null;
			if(chemins != null){
				Iterator<Chemin> iter = chemins.iterator();
				while(iter.hasNext() && (!stop)){
					Chemin test = iter.next();
					if(test.getDirQ() == start){
						selected = test;
						stop = true;
					}
				}
			}
			if(selected != null){
				Array<Vector2> tmp = new Array<Vector2>();
				tmp.addAll(selected.getEtapes());
				tmp.reverse();
				result.addAll(tmp);
			}
		}
		return result;
	}
	
	/** Donne les rails de cloture */
	public Array<Rail> getCloseLine(){
		if(this.close_rails.size == 0){
			closeLine();
			updateJonction();
		}
		return this.close_rails;
	}
	
	/** Indique si les quartiers recherchés existe à l'heure actuelle */
	public boolean existStation(int i){
		if(all_chemins.get(i) != null){
			return true;
		}
		return false;
	}
	
	/** Fonction qui retourne les objets graphiques qui sont des rails */
	public Array<Rail> getRails(){
		if(rails.size == 0){
			Iterator<Integer> iter_1 = all_chemins.keys().iterator();
			while(iter_1.hasNext()){
				int id_q = iter_1.next();
				Array<Chemin> chemins = all_chemins.get(id_q);
//				System.out.println("-- Quartier "+id_q);
				Iterator<Chemin> iter_2 = chemins.iterator();
				while(iter_2.hasNext()){
					Chemin chemin = iter_2.next();
					
					rails.addAll(chemin.getRails());
//					System.out.println("Chemin "+chemin.getDirQ()+" et rails "+chemin.getRails().size);
//					System.out.println("Chemin du quartier "+id_q+" vers "+chemin.getDirQ()+" contenant "+chemin.getRails().size+" rails ");
				}
			}
		}
		return rails;
	}
	/** Retourne un point a partir de son identifiant */
	
/*------------------------FONCTIONS DE CREATION VISUEL--------------------------*/	
	/** Rajoute des stations à la ligne */
	public void addStation(Batiment batiment){
		this.stations.put(batiment.id_q, batiment);
	}

	/** Ajoute un nouveau point dans la ville aux mise à jours */
	public void update(int id, float x, float y, int jour, int id_q, int next_rail, int id_quart_next){
		this.updated = true;
		this.close_rails.clear();
		this.rails.clear();
		Waypoint point = new Waypoint(id, x, y, jour, next_rail, id_q);
		//si il existe une chemin pour ce quartier
		Array<Chemin> chemins = new Array<Chemin>();
		Chemin chemin = new Chemin(id_quart_next);
		if(this.all_chemins.get(id_q) != null){
			chemins = this.all_chemins.get(id_q);
			Iterator<Chemin> iter = chemins.iterator();
			boolean stop = false;
			int index = 0;
			while(iter.hasNext() && !stop){
				//si il existe un chemin dans cette direction
				Chemin test_chemin = iter.next();
//				System.out.println("Quartier suivant visé "+id_quart_next+" et celui du chemin "+test_chemin.getDirQ());
				if(test_chemin.getDirQ() == id_quart_next){
					chemin = test_chemin;
					chemins.removeIndex(index);
					stop = true;
				}
				index++;
			}
		}
		//sinon c'est qu'on avais pas encore créé de quartier comme ça donc on en profite pour en créér un en speed pour les noeuds 
		else{
			this.noeuds.put(id_q, new Noeud(id_q));
		}
		insertPoint(point);
		chemin.addPoint(point);
		chemins.add(chemin);
		all_chemins.put(id_q, chemins);
	}
	/** Fonction ayant pour but de poser les points de jonction */
	private void updateJonction(){
		//pour le debug
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/whitefont.fnt"), false);
		Label.LabelStyle style = new Label.LabelStyle();
		style.font = font;
		font.scale(-0.96f);
		//itération sur tous les quartiers pour récupérer leurs points de jonction
		Iterator<Integer> iter_q = this.waypoints.keys().iterator();
		while(iter_q.hasNext()){
			int id_q = iter_q.next();
			Iterator<Waypoint> iter = this.waypoints.get(id_q).iterator();
			while(iter.hasNext()){
				Waypoint point = iter.next();
				//label pour s'y retrouver
				String texte = ""+point.getID();
				if(texte.length() == 0){
					texte = "ERROR";
				}
				Label label = new Label(texte, style);
				label.setPosition(point.getX()-0.5f, point.getY());
				labels.addActor(label);
				int genre = point.getGenre();
				int angle = point.getAngleDroite();
				if(genre != 1){
					angle = point.getAngleJonction();
				}
				Rail rail = new Rail(new Vector2(point.getX(), point.getY()), 1, 1, angle, genre, -1);
				close_rails.add(rail);
			}
		}
	}
	/** Utilitaire de création de point */
	private void insertPoint(Waypoint point){
		Array<Waypoint> points = new Array<Waypoint>();
		if((this.waypoints.get(point.getID_q()) != null) && (this.waypoints.get(point.getID_q()).get(0).getJour() == point.getJour())){
			points = this.waypoints.get(point.getID_q());
		}
		points.add(point);
		this.waypoints.put(point.getID_q(), points);
	}
	/** Fonction qui ferme la ligne */
	private void closeLine(){
		Iterator<Integer> iter_q = all_chemins.keys().iterator();
		//on vas chopper successivement chaque quartier
		while(iter_q.hasNext()){
			int id_quartier = iter_q.next();
			
			//pour chaque quartier on prend les chemins
			Array<Chemin> chemins = all_chemins.get(id_quartier);
			Iterator<Chemin> iter = chemins.iterator();
			//et pour chaque chemin on ferme
			while(iter.hasNext()){
				Chemin chemin_source = iter.next();
				if(!chemin_source.isClosed()){
					if(all_chemins.get(chemin_source.getDirQ()) != null){
						Waypoint source = chemin_source.getFin();
						Waypoint cible = getCorres(chemin_source, id_quartier);
						if((cible != null) && (source != null)){
							Waypoint tmp = source;
							float dist_x = Utils.floatToInt(Math.abs(source.getX()-cible.getX()));
							float dist_y = Utils.floatToInt(Math.abs(source.getY()-cible.getY()));
							//on prend le plus petit
							if(dist_x > dist_y){
								
								if(source.getX() > cible.getX()){
									tmp = cible;
								}
								if(dist_x > 1){
									dist_x--;
								}
							}
							else{
								if(source.getY() > cible.getY()){
									tmp = cible;
								}
								if(dist_y > 1){
									dist_y--;
								}
							}
							//on calcul le point de depart
							float start_x = tmp.getX();
							float start_y = tmp.getY();
							int angle = 0;
							if((dist_x > dist_y) && (dist_x > 1)){
								start_x++;
								angle = 1;
							}
							else if(dist_y > 1){
								start_y++;
							}
							//le nouveau rail
							Rail rail = new Rail(new Vector2(start_x, start_y), dist_x, dist_y, angle, 1, -1);
							this.close_rails.add(rail);
							System.out.println("On a le rail clos "+close_rails.size);
						}
					}
				}
				chemin_source.close();
			}
		}
		//après avoir fermé les rails graphiquement on vas créer les arcs logiques
		if(!arc_done){
			makeArcs();
		}
	}
	
	/** Fonction qui retourne le point de correspondance que doit rejoindre un point de depars */
	private Waypoint getCorres(Chemin source, int id_source){
		Array<Chemin> candidats = all_chemins.get(source.getDirQ());
		Iterator<Chemin> iter = candidats.iterator();
		Waypoint cible = null;
		while(iter.hasNext() && (cible == null)){
			Chemin test = iter.next();
			if(!test.isClosed()){
				if((test.getDirQ() == id_source)){
					cible = test.getFin();
					test.close();
				}
			}
		}
		return cible;
	}
	
/*====================CLASSES INTERNES POINTS ET ARCS====================*/
	/** La classe qui gère les points d'intersections */	
	private class Waypoint{
		private Vector2 position;
		private int jour, id, id_next, id_q;
		private Waypoint next_p, previous_p;
		
		public Waypoint(int id, float x, float y, int jour, int next, int id_q){
			this.position = new Vector2();
			this.position.x = x;
			this.position.y = y;
			this.jour = jour;
			this.id_next = next;
			this.id = id;
			this.id_q = id_q;
		}
/*--------------------------ACCESSEURS--------------------*/
		/** Retourne la position sur X du point */
		public float getX(){
			return this.position.x;
		}
		
		/** Retourne la position sur Y du point */
		public float getY(){
			return this.position.y;
		}
		
		/** Retourne le jour de création du point */
		public int getJour(){
			return this.jour;
		}
		
		/** Retourne l'identifiant du point */
		public int getID(){
			return this.id;
		}
		
		/** Retourne l'identifiant du quartier associé au point */
		public int getID_q(){
			return this.id_q;
		}
		
		/** Retourne l'identifiant du point suivant et -1 si il n'y en a pas */
		public int getIDNext(){
			if((this.next_p != null) && (this.id_next == -1)){
				this.id_next = next_p.getID();
			}
			return this.id_next;
		}
		
		/** Retourne le point suivant et -1 si il n'y en a pas */
		public Waypoint getNextP(){
			if((this.next_p == null) && (this.id_next != -1)){
				this.next_p = getPoint(this.id_next, this.id_q);
			}
			if(this.next_p == null){
				this.next_p = this;
			}
			return this.next_p;
		}
		
		/** Retourne le point précedent et -1 si il n'y en a pas */
		public Waypoint getPreviousP(){
			if(this.previous_p == null){
				this.previous_p = this;
			}
			return this.previous_p;
		}
		
		/** Retourne le genre de point soit une ligne droite soit un angle */
		public int getGenre(){
			if((getNextP().getX() != getPreviousP().getX()) && (getNextP().getY() != getPreviousP().getY())){
				return 2;
			}
			return 1;
		}
		
		/** Retourne l'orientation pour les points de catégorie 1 */
		public int getAngleDroite(){
			Waypoint prev = getPreviousP();
			Waypoint next = getNextP();
			if(prev.getX() == next.getX()){
				return 1;
			}
			return 0;
		}
		
		/** Retourne l'angle pour les points de jonction entre deux tronçons de rails*/
		public int getAngleJonction(){
			int angle = 0;
			Waypoint prev = getPreviousP();
			Waypoint next = getNextP();
//			System.out.println("-----Calcul de l'angle du point "+this.id+" ("+this.getX()+", "+this.getY()+") ");
//			System.out.println("Le précédent est "+prev.getID()+" ("+prev.getX()+", "+prev.getY()+") "+" et le suivant "+next.getID()+" ("+next.getX()+", "+next.getY()+")");
			if(this.getY() == prev.getY()){
//				System.out.println("- Alignés sur Y entre courant et précédent ");
				if(this.getX() < prev.getX()){
	//				System.out.println("- courant < précédent sur X");
					if(this.getY() < next.getY()){
						angle = 2;
					}
					else if(this.getY() > next.getY()){
						angle = 1;
					}
				}
				else{
//					System.out.println("- courant > précédent sur X");
					if(this.getY() < next.getY()){
						angle = 3;
					}
					else if(this.getY() > next.getY()){
						angle = 0;
					}
				}
			}
			else if(this.getY() == next.getY()){
//				System.out.println("- Alignés sur Y entre courant et suivant ");
				if(this.getX() < next.getX()){
//					System.out.println("- courant < suivant sur X");
					if(this.getY() < prev.getY()){
						angle = 2;
						
					}
					else if(this.getY() > prev.getY()){
						angle = 1;
					}
				}
				else{
//					System.out.println("- courant > suivant sur X");
					if(this.getY() < prev.getY()){
						angle = 3;
					}
					else if(this.getY() > prev.getY()){
						angle = 0;
					}
				}
			}
			else if (this.getX() == prev.getX()){
//				System.out.println("- Alignés sur X entre courant et précédent ");
				if(this.getY() > prev.getY()){
//					System.out.println("- courant > précédent sur Y");
					if(this.getX() > next.getX()){
						angle = 0;
					}
					else if(this.getX() < next.getX()){
						angle = 3;
					}
				}
				else{
//					System.out.println("- courant < précédent sur Y");
					if(this.getX() > next.getX()){
						angle = 1;
					}
					else if(this.getX() < next.getX()){
						angle = 2;
					}
				}
			}
			else if(this.getX() == next.getX()){
//				System.out.println("- Alignés sur X entre courant et suivant ");
				if(this.getY() > next.getY()){
//					System.out.println("- courant > suivant sur Y");
					if(this.getX() > prev.getX()){
						angle = 0;
					}
					else if(this.getX() < prev.getX()){
						angle = 3;
					}
				}
				else{
//					System.out.println("- courant < suivant sur Y");
					if(this.getX() > prev.getX()){
						angle = 1;
					}
					else if(this.getX() < prev.getX()){
						angle = 2;
					}
				}
			}
//			System.out.println("Angle en "+angle);
			return angle;
		}
/*--------------------------SETTEURS--------------------*/
		public void setNextPoint(Waypoint next){
			this.next_p = next;
		}
		public void setPreviousPoint(Waypoint previous){
			this.previous_p = previous;
		}
	}
	
/** La classe qui gère les tronçons entre les points */
	private class Chemin{
		
		private ObjectMap<Integer, Waypoint> path;
		private int dir_quartier;
		private Waypoint debut, fin;
		private Array<Rail> rails_internes;
		private boolean closed;
		
		public Chemin(int dir){
			this.path = new ObjectMap<Integer, Waypoint>();
			this.dir_quartier = dir;
			this.debut = null;
			this.fin = null;
			this.rails_internes = new Array<Rail>();
			this.closed = false;
		}
		
/*------------------------SETTEURS ET FCT------------------------*/
		/** Ajoute un point à la liste des points du chemin et renseigne le premier et le dernier
		 * point du chemin */
		public void addPoint(Waypoint point){
			path.put(point.getID(), point);
			if(point.getIDNext() == -1){
				fin = point;
			}
			else if(debut == null || point.getID() < debut.getID()){
				debut = point;
			}
			if(path.size > 1){
				updateRail();
			}
		}
		
		/** recréé les rails sur le chemin. ça evite de devoir tout reparser et comme de 
		 * toute façon les rails suivent les chemins. */
		private void updateRail(){
			this.rails_internes.clear();
			Iterator<Integer> iter = path.keys().iterator();
			Waypoint current = null, previous = null;
			//calcul de tout les rails jusqu'au dernier
			int id = 0;
			while(iter.hasNext()){
				current = path.get(iter.next());
				if(previous != null){
					current.setPreviousPoint(previous);
					Waypoint tmp = previous;
					int taille_x = Utils.floatToInt(current.getX()-previous.getX());
					int taille_y = Utils.floatToInt(current.getY()-previous.getY());
					if((taille_x > 1) || (taille_y > 1)){
						if((current.getX() < previous.getX()) || (current.getY() < previous.getY())){
							tmp = current;
						}
						float start_x = tmp.getX();
						float start_y = tmp.getY();
						taille_x = Math.abs(taille_x);
						taille_y = Math.abs(taille_y);
						if(taille_x == 0){
							taille_x++;
							start_y++;
							if(taille_y > 1){
								taille_y--;
							}
						}
						if(taille_y == 0){
							taille_y++;
							start_x++;
							if(taille_x > 1){
								taille_x--;
							}
						}
						int angle = 0;
						if(previous.getX() == current.getX()){
							angle = 1;
						}
						if(!(taille_x > 1 && taille_y > 1)){
							Rail rail = new Rail(new Vector2(start_x, start_y), taille_x, taille_y, angle, 1, id);
							this.rails_internes.add(rail);
							id++;
						}
					}
				}
				previous = current;
			}
		}
		
		/** Ferme la structure pour economiser des iterations (de la combinatoire) */
		public void close(){
			this.closed = true;
		}
		
/*-------------------------ACCESSEURS--------------------------*/
		/** Retourne les vecteurs 2 qui correspondent au points traversés pendant les chemins */
		public Array<Vector2> getEtapes(){
			Array<Vector2> result = new Array<Vector2>();
			Iterator<Integer> iter = path.keys().iterator();
			while(iter.hasNext()){
				Waypoint point = path.get(iter.next());
				result.add(new Vector2(point.getX(), point.getY()));
			}
			return result;
		}
		/** Retourne le dernier point du chemin */
		public Waypoint getFin(){
			return fin;
		}
		/** Retourne l'identifiant du quartier vers lequel point le chemin */
		public int getDirQ(){
			return this.dir_quartier;
		}
		/** Retourne les rails représentatif des liaison entre les points du chemin */
		public Array<Rail> getRails(){
			return this.rails_internes;
		}
		/** Ferme le chemin pour les test lors de l'iteration d'initialisation */
		private boolean isClosed(){
			return this.closed;
		}
	}
/*-------------------CLASSES ET FONCTION DU GRAPH DE NAVIGATION-------------------*/
	
	/** Renvois un noeud dans la liste a partir de l'identifiant du quartier */
	public Noeud getNoeud(int id){
		return this.noeuds.get(id);
	}
	
	/** Créé tous les arcs logique une et une seule fois. En esperant que les glissements de quartier à quartier ne fassent
	 * pas sauter les liaisons globales */
	private void makeArcs(){
		ArrayList<Arc> tmp_arc = new ArrayList<Arc>();
		//on parcours tous les quartiers pour créer les chemins
		Iterator<Integer> iter_q = all_chemins.keys().iterator();
		while(iter_q.hasNext()){
			int id_quartier = iter_q.next();
			Noeud noeud_source = getNoeud(id_quartier);
			Iterator<Chemin> iter_ch = all_chemins.get(id_quartier).iterator();
			while(iter_ch.hasNext()){
				Chemin chemin = iter_ch.next();
				Noeud noeud_cible = getNoeud(chemin.getDirQ());
				if(noeud_cible != null){
					tmp_arc.add(new Arc(noeud_cible, 1.0));
				}
			}
			//creation du tableau avec le nombre d'arcs sortant du quartier vers les autres quartiers
			Arc[] arcs = new Arc[tmp_arc.size()];
			Iterator<Arc> iter_tmp = tmp_arc.iterator();
			int i = 0;
			while(iter_tmp.hasNext()){
				Arc arc = iter_tmp.next();
				arcs[i] = arc;
				i++;
			}
			noeud_source.adjacencies = arcs;
		}
		arc_done = true;
	}
	
	/** Retourne l'ensemble des noeuds que l'on dois traverser pour atteindre un but */
	private static List<Noeud> getEtapes(Noeud target){
        List<Noeud> path = new ArrayList<Noeud>();
        for (Noeud vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
	
	/** calcul les plus cours chemins */
	private static void computePaths(Noeud source){
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
	
	/** classe de stockage des noeuds */
	private class Noeud implements Comparable<Noeud>{
	    public Arc[] adjacencies;
	    public double minDistance = Double.POSITIVE_INFINITY;
	    private int id_q;
	    public Noeud previous;
	    
	    public Noeud(int id_q) { 
	    	this.id_q = id_q;
	    }
	    public int compareTo(Noeud other){
	        return Double.compare(minDistance, other.minDistance);
	    }
	    /** Retourne l'identifiant du quartier et donc de la station associée au noeud */
	    public int getID(){
	    	return this.id_q;
	    }
	}
	
	/** classe de stockage des arcs */
	private class Arc{
	    public final Noeud target;
	    public final double weight;
	    public Arc(Noeud argTarget, double argWeight){ 
	    	target = argTarget; weight = argWeight; 
	    }
	}
}
