package elements2D;

import java.util.Iterator;

import reseau.Paquet;
import tools.Identifiants;
import managers.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Classe qui contient les fonctions logiques de la ville */
public class Ville {
	
	private Joueur joueur;
	private Maglev maglev;
	private int jour;
	private int nb_current, nb_aim;

	//pour les structures statiques des tableaux 
	private ObjectMap<Integer, Batiment> batiments;
	private ObjectMap<Integer, Bloc> blocs;
	private ObjectMap<Integer, Array<RLE>> rles;
	private Array<Route> routes;
	private Array<Canal> canaux;
	private ObjectMap<Integer, Quartier> quartiers;
	private Ligne ligne;
	
	private Controles controles;
	
	public Ville(ScreenManager jeu){
		this.joueur = new Joueur(-1, new Vector2(0, 0), 5f, 0, 0.25f, 0.25f, 0);
		this.batiments = new ObjectMap<Integer, Batiment>();
		this.blocs = new ObjectMap<Integer, Bloc>();
		this.rles = new ObjectMap<Integer, Array<RLE>>();
		this.routes = new Array<Route>();
		this.canaux = new Array<Canal>();
		this.quartiers = new ObjectMap<Integer, Quartier>();
		this.controles = new Controles(this);
		Gdx.input.setInputProcessor(controles);	
		this.jour = 0;
		this.nb_current = 0;
		this.nb_aim = -1;
		this.ligne = new Ligne();
		this.maglev = new Maglev(-2, new Vector2(0,0), 100, 0, 1, 1, 103, ligne);
	}
	
	public Joueur getJoueur(){
		return this.joueur;
	}
	
	public Maglev getMaglev(){
		return this.maglev;
	}
	
	public void update(){
		//si collision alors 
		if(overlap()){
			//on verifie qu'on a pas deja mis des verrous
			if(!controles.isLocked()){
				//on arrette et on verouille en fonction de la direction d'ou on venais
				if(joueur.getVelocite().x == -1){
					joueur.getVelocite().x = 0;
					controles.setLock_left();
				}
				if(joueur.getVelocite().x == 1){
					joueur.getVelocite().x = 0;
					controles.setLock_right();
				}
				if(joueur.getVelocite().y == -1){
					joueur.getVelocite().y = 0;
					controles.setLock_down();
				}
				if(joueur.getVelocite().y == 1){
					joueur.getVelocite().y = 0;
					controles.setLock_up();
				}
			}
		}
		//si il n'y a plus de collision on leve les verrous
		else{
			controles.resetLocks();
		}
		joueur.update();
		maglev.update();
	}
	
	/** Gestion de la collision entre le joueur et les batiments */
	public boolean overlap(){
		//overlap avec les batiments
		Iterator<Integer> iter_b = batiments.keys().iterator();
		while(iter_b.hasNext()){
			Rectangle test = batiments.get(iter_b.next()).getBound();
			if(joueur.getBound().overlaps(test)){
				return true;
			}
		}
		//ou avec les canaux
		Iterator<Canal> iter_c = canaux.iterator();
		while(iter_c.hasNext()){
			Rectangle test = iter_c.next().getBound();
			if(joueur.getBound().overlaps(test)){
				return true;
			}
		}
		//ou avec les frontieres
/*		Iterator<RLE> iter_f = rles.iterator();
		while(iter_f.hasNext()){
			RLE rle = iter_f.next();
			if(rle)
			Rectangle test = iter_c.next().getBound();
			if(joueur.getBound().overlaps(test)){
				return true;
			}
		}*/
		//ou avec les canaux
		return false;
	}
	public void dispose(){
	}
/*--------------------------------------ACCESSEURS-------------------------------------*/	
	/** Retourne un batiment dans la map des batiments */
	public Batiment getBatimentFromId(int id){
		return batiments.get(id);
	}
	/** Retourne un bloc dans la map de blocs */
	public Bloc getBlocFromId(int id){
		return blocs.get(id);
	}
	/** Retourne les batiments de la map */
	public ObjectMap<Integer, Batiment> getBatiments(){
		return batiments;
	}
	/** Retourne les blocs de la map */
	public ObjectMap<Integer, Bloc> getBlocs(){
		return this.blocs;
	}
	/** Retourne les routes de la map */
	public Array<Route> getRoutes(){
		return this.routes;
	}
	/** Retourne les canaux de la map */
	public Array<Canal> getCanaux(){
		return this.canaux;
	}
	/** Retourne les rles de la map */
	public ObjectMap<Integer, Array<RLE>> getRLEs(){
		return this.rles;
	}
	/** Retourne les quartiers de la map*/
	public ObjectMap<Integer, Quartier> getQuartiers(){
		return this.quartiers;
	}
	/** Retourne une quartier depuis son id */
	public Quartier getQuartierFromId(int id){
		return this.quartiers.get(id);
	}
	/** Renvois la quantité de model qui est à charger que le client dis */
	public float getNbAim(){
		return (float) this.nb_aim;
	}
	/** Renvois la quantité de models deja charge dans la ville */
	public float getNbCurrent(){
		return (float) this.nb_current;
	}
	/** Renvois les objets graphiques de type rails */
	public Array<Rail> getRails(){
		return ligne.getRails();
	}
	/** Renvois les dernier rails pour lier les quartiers */
	public Array<Rail> getClosureRails(){
		return ligne.getCloseLine();
	}
/*--------------------------ARRIVEE DE NOUVEAUX OBJETS OU UPDATES-----------------------*/	
	/** Gère l'arrive de nouveaux batiments */
	private void upBatiment(Batiment bat){
		System.out.println("Types des batiments en entrée "+bat.getType());
		
		//si le batiment existe deja
		if(getBatimentFromId(bat.id) != null){
			getBatimentFromId(bat.id).setPosition(bat.getPosition());
		}
		//sinon on créé un nouveau 
		else{
			this.batiments.put(bat.id, bat);
			// et en fonction de son type si c'est une station on l'ajoute à la ligne
			if(bat.getType() == Identifiants.stationBat){
				this.ligne.addStation(bat);
			}
		}
	}
	/** Gére l'arrivée de nouveaux blocs*/
	private void upBloc(Bloc bloc){
		//si le bloc existe deja
		if(getBlocFromId(bloc.id) != null){
			getBlocFromId(bloc.id).setPosition(bloc.getPosition());
		}
		//sinon on créé un nouveau
		else{
			this.blocs.put(bloc.id, bloc);
		}
	}
	/** Récéption des routes */
	private void upRoute(Route route){
		this.routes.add(route);
	}
	/** Récéption de canaux */
	private void upCanal(Canal canal){
		this.canaux.add(canal);
	}
	/** S'occupe du sous type particulier de RLE que sont les frontieres */
	private void upRLE(RLE rle){
		//Si ce n'est pas le meme jour on vire tous le RLEs
		if(rles.get(rle.getID_q()) != null && rles.get(rle.getID_q()).get(0).getJour() != rle.getJour()){
			this.jour = rle.getJour();
			rles.remove(rle.getID_q());
		}
		//Si le rle n'est pas vide 
		Array<RLE> mes_rles = new Array<RLE>();
		if(rles.get(rle.getID_q()) != null){
			mes_rles.addAll(rles.get(rle.getID_q()));
		}
		//Dans tous les cas on rajoute le nouvel objet
		mes_rles.add(rle);
		rles.put(rle.getID_q(), mes_rles);
	}

	/** S'occupe de modifier les points de jonction de la ligne*/
	private void upLigne(int id, float x, float y, int jour, int id_q, int next, int id_quart_next){
		ligne.update(id, x, y, jour, id_q, next, id_quart_next);
	}
	
	/** Fonction qui translate tout sur la map quand la ville grandie */
	private void upTranslate(int id_quartier, Vector2 translation){
		//on vas commencer par parcourir chaque liste et si ça colle en id alors on translate
		Iterator<Integer> iter = blocs.keys().iterator();
		Bloc bloc;
		while(iter.hasNext()){
			bloc = blocs.get(iter.next());
			if(bloc.getID_q() == id_quartier){
				bloc.setPosition(new Vector2((bloc.getPosition().x + translation.x),
						(bloc.getPosition().y + translation.y)));
			}
		}
		Iterator<Integer> iter_bats = batiments.keys().iterator();
		Batiment bat;
		while(iter_bats.hasNext()){
			bat = batiments.get(iter_bats.next());
			if(bat.getID_q() == id_quartier){
				bat.setPosition(new Vector2((bat.getPosition().x + translation.x),
						(bat.getPosition().y + translation.y)));
			}
		}
		Iterator<Route> iter_route = routes.iterator();
		Route route;
		while(iter_route.hasNext()){
			route = iter_route.next();
			if(route.getID_q() == id_quartier){
				route.setPosition(new Vector2((route.getPosition().x + translation.x),
						(route.getPosition().y + translation.y)));
			}
		}
		Iterator<Canal> iter_canaux = canaux.iterator();
		Canal canal;
		while(iter_canaux.hasNext()){
			canal = iter_canaux.next();
			if(canal.getID_q() == id_quartier){
				canal.setPosition(new Vector2((canal.getPosition().x + translation.x),
						(canal.getPosition().y + translation.y)));
			}
		}
	}
	/** Transfert d'un quartier pour la dalle au sole */
	private void upQuartier(Quartier quartier){
		quartiers.put(quartier.id, quartier);
//		System.out.println("quartiers au total "+quartiers.size);
	}
/*------------------------------------GESTION DES PAQUETS--------------------------------------------*/		
	public void getMsg(Paquet pack){
		nb_current++;
		if(pack.getClasse() == 0){
			int texY = pack.getTexY();
			upBloc(new Bloc(pack.getId(), new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getId_q()));
		}
		//pour les batiments
		else if(pack.getClasse() == 1){
			int texX = pack.getTexX();
			int texY = pack.getTexY();
			upBatiment(new Batiment(pack.getId(), new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getSens(), pack.getId_q()));
		}
		//pour les cellules genre routes rails canaux chemins
		else if(pack.getClasse() == 2){
			if(pack.getType() == Identifiants.roadBloc){
				upRoute(new Route(new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getSens(), pack.getTexY(), pack.getId_q()));
			}
			else if(pack.getType() == Identifiants.canal){
				upCanal(new Canal(new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getSens(), pack.getTexY(), pack.getId_q()));
			}
		}
		//pour la translation
		else if(pack.getClasse() == 3){
			upTranslate(pack.getId_q(), pack.getVectorTranslate());
		}
		//pour les RLE
		else if(pack.getClasse() == 4){
			if(pack.getType() == Identifiants.frontiereBat){
				upRLE(new Frontiere(new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getId_q(), pack.getJour(), pack.getClos()));
			}
			else if(pack.getType() == Identifiants.roadBloc){
				upRLE(new Rocade(new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getId_q(), pack.getJour()));
			}
		}
		//pour les information sur la ville
		else if(pack.getClasse() == 5){
			this.nb_aim = pack.getNbTotal();
		}
		//pour les information sur le quartier
		else if(pack.getClasse() == 6){
			upQuartier(new Quartier(pack.getId_q(), new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl()));
		}
		//pour les rails
		else if(pack.getClasse() == 7){
			upLigne(pack.getIDRail(), pack.getX(), pack.getY(), pack.getJour(), pack.getId_q(), pack.getNext(), pack.getIDQn());
		}
	}
}
