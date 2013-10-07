package elements2D;

import java.util.Iterator;

import tools.Utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Classe qui gère la construction d'une ligne à partir de tous les rails */
public class Ligne {
	
	private ObjectMap<Integer, Array<Waypoint>> noeuds;
	private ObjectMap<Integer, Array<Chemin>> all_chemins;
	private ObjectMap<Integer, Batiment> stations;
	private Array<Rail> rails;
	private Array<Rail> close_rails;
	
	public Ligne(){
		this.noeuds = new ObjectMap<Integer, Array<Waypoint>>();
		this.all_chemins = new ObjectMap<Integer, Array<Chemin>>();
		this.stations = new ObjectMap<Integer, Batiment>();
		this.rails = new Array<Rail>();
		this.close_rails = new Array<Rail>();
	}

	/** Rajoute des stations à la ligne */
	public void addStation(Batiment batiment){
		this.stations.put(batiment.id_q, batiment);
	}

	/** Donne une station en fonction de l'identifiant du quartier */
	public Vector2 getStation(int id_q){
		return this.stations.get(id_q).getPosition();
	}
	/** Donne les rails de cloture */
	public Array<Rail> getCloseLine(){
		if(this.close_rails.size == 0){
			closeLine();
		}
		return this.close_rails;
	}
	
	/** Ajoute un nouveau point dans la ville aux mise à jours */
	public void update(int id, float x, float y, int jour, int id_q, int next_rail, int id_quart_next){
		this.close_rails.clear();
		this.rails.clear();
		Waypoint point = new Waypoint(id, x, y, jour, next_rail);
		Array<Waypoint> points = noeuds.get(id_q);
		Array<Waypoint> new_points = new Array<Waypoint>();
		//si il existe deja des valeurs dans la liste des points et si les jours de ces points sont les mêmes que les nouveaux
		if((points != null) && (points.size > 0) && (points.get(0).getJour() == jour)){
			new_points.addAll(points);
		}
		new_points.add(point);
		this.noeuds.put(id_q, new_points);
		//si il existe une chemin pour ce quartier
		Array<Chemin> chemins = new Array<Chemin>();
		Chemin chemin = new Chemin(id_quart_next);
		if(this.all_chemins.get(id_q) != null){
			chemins = this.all_chemins.get(id_q);
			Iterator<Chemin> iter = this.all_chemins.get(id_q).iterator();
			boolean stop = false;
			while(iter.hasNext() && !stop){
				//si il existe un chemin dans cette direction
				Chemin test_chemin = iter.next();
				if(test_chemin.getDirQ() == id_quart_next){
					chemin = test_chemin;
					stop = true;
				}
			}
		}
		chemin.addPoint(point);
		chemins.add(chemin);
		all_chemins.put(id_q, chemins);
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
						//System.out.println("Progresse t'on? Depuis quartier "+id_quartier+" jusqu'à "+chemin_source.getDirQ());
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
								dist_x+=0.5f;
							}
							else{
								if(source.getY() > cible.getY()){
									tmp = cible;
								}
								dist_y+=0.5f;
							}
							Rail rail = new Rail(new Vector2(tmp.getX(), tmp.getY()), dist_x, dist_y);
							this.close_rails.add(rail);
							//System.out.println("Distance sur x "+dist_x+" et y "+dist_y);
						}
					}
				}
				chemin_source.close();
			}
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
	
	/** Fonction qui retourne les objets graphiques qui sont des rails */
	public Array<Rail> getRails(){
		if(rails.size == 0){
			Iterator<Integer> iter_1 = all_chemins.keys().iterator();
			while(iter_1.hasNext()){
				Iterator<Chemin> iter_2 = all_chemins.get(iter_1.next()).iterator();
				while(iter_2.hasNext()){
					rails.addAll(iter_2.next().getRails());
				}
			}
		}
		return rails;
	}
	/** La classe qui gère les points d'intersections */	
	private class Waypoint{
		private Vector2 position;
		private int jour, next_rail, id;
		
		public Waypoint(int id, float x, float y, int jour, int next){
			this.position = new Vector2();
			this.position.x = x;
			this.position.y = y;
			this.jour = jour;
			this.next_rail = next;
			this.id = id;
		}
		public float getX(){
			return this.position.x;
		}
		public float getY(){
			return this.position.y;
		}
		public int getJour(){
			return this.jour;
		}
		public int getNextRail(){
			return this.next_rail;
		}
		public int getID(){
			return this.id;
		}
	}
/** La classe qui gère les tronçons entre les points */
	private class Chemin{
		
		private ObjectMap<Integer, Waypoint> path;
		private int dir_quartier;
		private Waypoint debut, fin;
		private Array<Rail> rails;
		private boolean closed;
		
		public Chemin(int dir){
			this.path = new ObjectMap<Integer, Waypoint>();
			this.dir_quartier = dir;
			this.debut = null;
			this.fin = null;
			this.rails = new Array<Rail>();
			this.closed = false;
		}
	/*------------------------SETTEURS ET FCT------------------------*/
		/** Ajoute un point à la liste des points du chemin et renseigne le premier et le dernier
		 * point du chemin */
		public void addPoint(Waypoint point){
			path.put(point.getID(), point);
			if(point.getNextRail() == -1){
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
			rails.clear();
			Iterator<Integer> iter = path.keys().iterator();
			Waypoint current = null, previous = null;
			//calcul de tout les rails jusqu'au dernier
			while(iter.hasNext()){
				current = path.get(iter.next());
				if(previous != null){
					Waypoint tmp = previous;
					int taille_x = Utils.floatToInt(current.getX()-previous.getX());
					int taille_y = Utils.floatToInt(current.getY()-previous.getY());
					if((current.getX() < previous.getX()) || (current.getY() < previous.getY())){
						tmp = current;
					}
					float start_x = tmp.getX();
					float start_y = tmp.getY();
					taille_x = Math.abs(taille_x);
					taille_y = Math.abs(taille_y);
					if(taille_x == 0){
						taille_x++;
						start_y+=0.5;
					}
					if(taille_y == 0){
						taille_y++;
						start_x+=0.5;
					}
					if(!(taille_x > 1 && taille_y > 1)){
						Rail rail = new Rail(new Vector2(start_x, start_y), taille_x, taille_y);
						rails.add(rail);
					}
				}
				previous = current;
			}
			//calcul du dernier rail 
			Rail rail = new Rail(new Vector2(current.getX(), current.getY()), 1, 1);
			rails.add(rail);
		}
		public void close(){
			this.closed = true;
		}
	/*-------------------------ACCESSEURS--------------------------*/
		public Waypoint getDebut(){
			return debut;
		}
		public Waypoint getFin(){
			return fin;
		}
		public int getDirQ(){
			return this.dir_quartier;
		}
		public Array<Rail> getRails(){
			return this.rails;
		}
		public boolean isClosed(){
			return this.closed;
		}
	}
}
