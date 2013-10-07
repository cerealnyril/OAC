package managers;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.badlogic.gdx.math.Vector2;

import reseau.Message;
import reseau.Paquet;
import reseau.Serveur;
import structure.Batiment;
import structure.Bloc;
import structure.Frontiere;
import structure.Quartier;
import structure.RLE;
import structure.Rail;
import tools.Identifiants;
import tools.ParamsGlobals;
import tools.ReseauGlobals;
import topographie.Cell;

/** le manager qui gère les choses mis à jour et qui envois la synchronisation */
public class CommunicationManager {
	private Map<Integer, Vector2> deplacements_quartiers = Collections.synchronizedMap(new TreeMap<Integer, Vector2>());
	private Map<Integer, Bloc> blocs = Collections.synchronizedMap(new TreeMap<Integer, Bloc>());
	private Map<Integer, Batiment> batiments = Collections.synchronizedMap(new TreeMap<Integer, Batiment>());
	private Map<Integer, Quartier> quartiers = Collections.synchronizedMap(new TreeMap<Integer, Quartier>());
	private List<RLE> rles = Collections.synchronizedList(new ArrayList<RLE>());
	private List<Rail> rails = Collections.synchronizedList(new ArrayList<Rail>());
	private List<Cell> routes = Collections.synchronizedList(new ArrayList<Cell>());
	private List<Cell> canaux = Collections.synchronizedList(new ArrayList<Cell>());
	private List<Cell> chemins = Collections.synchronizedList(new ArrayList<Cell>());

	private Serveur serveur;
	private static Logger logger = Logger.getLogger("serveurLog");
	
	public CommunicationManager(){
		serveur = new Serveur(ReseauGlobals.SERVEUR_PORT);
		try {
			logger.info("Lancement du serveur.");
			serveur.connect(this);
		} catch (Exception e) {
			logger.severe("Lancement du serveur impossible : "+e);
		}
		
	}
	public Serveur getServeur() {
		return this.serveur;
	}
/*---------------------------POUR LA GESTION DES MAJ------------------------------ */
	public void updateQuartier(int id, Quartier quartier) {
		Vector2 vecteur = quartier.getTranslation();
		synchronized(deplacements_quartiers){
			if (deplacements_quartiers.containsKey(id)){
				vecteur.x += deplacements_quartiers.get(id).x;
				vecteur.y += deplacements_quartiers.get(id).y;
			}
			deplacements_quartiers.put(id, vecteur);
		}
		synchronized(quartiers){
			quartiers.put(id, quartier);
		}
	}


	public void updateBloc(int id, Bloc bloc) {
		synchronized(blocs){
			if (blocs.containsKey(id)) return;
			else blocs.put(id, bloc);
		}		
	}


	public void updateBat(int id, Batiment batiment) {
		synchronized(batiments){
			if (batiments.containsKey(id)) return;
			else batiments.put(id, batiment);
		}		
	}
	public void updateRail(Rail rail){
		synchronized(rail){
			rails.add(rail);
		}
	}
	
	public void updateRLE(RLE rle){
		synchronized(rle){
			rles.add(rle);
		}
	}
	/** Ajoute les nouvelles routes dans le tableau qui sera utilisé pour envoyer l'update */
	public void updateRoute(Cell cell){
		synchronized(routes){
			routes.add(cell);
		}
	}
	/** Ajoute les nouveaux chemins dans le tableau qui sera utilisé pour envoyer l'update */
	public void updateChemin(Cell cell){
		synchronized(chemins){
			chemins.add(cell);
		}
	}
	/** Ajoute les nouveaux canaux dans le tableau qui sera utilisé pour envoyer l'update */
	public void updateCanal(Cell cell){
		synchronized(canaux){
			canaux.add(cell);
		}
	}
/*------------------POUR LA CREATION ET L'ENVOIS DE PAQUETS------------------- */
	/** Envois au nouveau client de toute la ville */
	public void broadcastFull(final int idClient, final int port, final InetAddress ia){
		Thread r = new Thread() {
		   	public void run() {
		   			//on prepare tout ça pour avoir une idée de combien on en envois
		   			ArrayList<Quartier> quartiers = ParamsGlobals.VILLE.getAllQuartiers();
		   			ArrayList<Bloc> blocs = ParamsGlobals.VILLE.getAllBlocs();
		   			ArrayList<Batiment> bats = ParamsGlobals.VILLE.getAllBats();
		   			ArrayList<Cell> routes = ParamsGlobals.VILLE.getAllRoutes();
		   			ArrayList<Cell> canaux = ParamsGlobals.VILLE.getAllCanaux();
		   			ArrayList<RLE> rles = ParamsGlobals.VILLE.getAllRLEs();
		   			ArrayList<Rail> rails = ParamsGlobals.VILLE.getAllRails();
		   			int total = quartiers.size()+blocs.size()+bats.size()+routes.size()+canaux.size()+rles.size()+rails.size();
		   			
		   			//le premier message pour annoncer combien j'en envois
		   			Message msg = new Message(idClient, getVillePack(total), port, ia);
		   			serveur.send(msg);
		   			//les vrais envois par type
		   			//les quartiers 
		   			Iterator<Quartier> iter_quart = quartiers.iterator();
					while(iter_quart.hasNext()){
						msg = new Message(idClient, getQuartPack(iter_quart.next()), port, ia);
						serveur.send(msg);
					}
					//les blocs
			   		Iterator<Bloc> iter_bloc = blocs.iterator();
					while(iter_bloc.hasNext()){
						msg = new Message(idClient, getBlocPack(iter_bloc.next()), port, ia);
						serveur.send(msg);
					}
					//les batiments
			   		Iterator<Batiment> iter_bat = bats.iterator();
					while(iter_bat.hasNext()){
						msg = new Message(idClient, getBatPack(iter_bat.next()), port, ia);
						serveur.send(msg);
					}
					//les cellules de route
					Iterator<Cell> iter_routes = routes.iterator();
					while(iter_routes.hasNext()){
						Cell route = iter_routes.next();
						msg = new Message(idClient, getCellPack(route, route.getIDQuartier()), port, ia);
						serveur.send(msg);
					}
					/*Iterator<Cell> iter_chemins = ParamsGlobals.VILLE.getAllChemins().iterator();
					while(iter_chemins.hasNext()){
						Cell chemin = iter_chemins.next();
						Message msg = new Message(idClient, getCellPack(chemin, chemin.getIDQuartier()), port, ia);
						serveur.send(msg);
					}*/
					//les cellules de canaux
					Iterator<Cell> iter_canaux = canaux.iterator();
					while(iter_canaux.hasNext()){
						Cell canal = iter_canaux.next();
						msg = new Message(idClient, getCellPack(canal, canal.getIDQuartier()), port, ia);
						serveur.send(msg);
					}
					//les rle : frontieres, rocades
					Iterator<RLE> iter_rle = rles.iterator();
					while(iter_rle.hasNext()){
						msg = new Message(idClient, getRLEPack(iter_rle.next()), port, ia);
						serveur.send(msg);
					}
					//les rails
					Iterator<Rail> iter_rail = rails.iterator();
					while(iter_rail.hasNext()){
						msg = new Message(idClient, getRailPack(iter_rail.next()), port, ia);
						serveur.send(msg);
					}
		   		}
		};
		r.start();	
	}
	/** Enclenche l'envois de tous les paquets pour les grand changements de ville */
	public void broadcastUpdate() {
		// lancement d'un thread pour vider les maps
		Thread r = new Thread() {
		   	public void run() {
		   			//pour la remise des dalles de quartier
		   			Iterator<Integer> iter_quart = quartiers.keySet().iterator();
		   			while(iter_quart.hasNext()){
		   				int id = iter_quart.next();
		   				serveur.sendToAll(getQuartPack(quartiers.get(id)));
		   			}
		   			//la translation de tout ce qu'on a deja
		   			Iterator<Integer> iter_trans = deplacements_quartiers.keySet().iterator();
		   			while(iter_trans.hasNext()){
		   				int id = iter_trans.next();
		   				serveur.sendToAll(getTranslatePack(id, deplacements_quartiers.get(id)));
		   			}
		   			deplacements_quartiers.clear();
		   			//et tout les petits nouveaux 
			   		Iterator<Integer> iter_bloc = blocs.keySet().iterator();
					while(iter_bloc.hasNext()){
						serveur.sendToAll(getBlocPack(blocs.get(iter_bloc.next())));
					}
					blocs.clear();
			   		Iterator<Integer> iter_bat = batiments.keySet().iterator();
					while(iter_bat.hasNext()){
						serveur.sendToAll(getBatPack(batiments.get(iter_bat.next())));
					}
					batiments.clear();
					Iterator<Cell> iter_route = routes.iterator();
					while(iter_route.hasNext()){
						Cell cell = iter_route.next();
						serveur.sendToAll(getCellPack(cell, cell.getIDQuartier()));
					}
					routes.clear();
					Iterator<Cell> iter_canal = canaux.iterator();
					while(iter_canal.hasNext()){
						Cell cell = iter_canal.next();
						serveur.sendToAll(getCellPack(cell, cell.getIDQuartier()));
					}
					canaux.clear();
					Iterator<RLE> iter_rle = rles.iterator();
					while(iter_rle.hasNext()){
						serveur.sendToAll(getRLEPack(iter_rle.next()));
					}
					rles.clear();
		   		}
		};
		r.start();
	}	
	/** Création d'un paquet info de la ville avec le nombre de batiments qu'on vas balancer */
	private Paquet getVillePack(int total){
		Paquet pack = new Paquet(
								ParamsGlobals.VILLE.getHeure(),//tailleX-> heure
								ParamsGlobals.VILLE.getAge(),//tailleY -> jour
								0,//startX
								0,//startY
								0,//z
								0, //texX
								0, //texY
								0,//rotation
								0, //type
								0,//id
								5,//classe
								total,//quartier -> nb packs
								false,//gimmi
								false,//ak
								false,//ping
								false);//pong
		return pack;
	}
	/** Creation d'un paquet de translation */
	private Paquet getTranslatePack(int id, Vector2 vecteur){
		Paquet pack = new Paquet(
								0,
								0,
								vecteur.x, 
								vecteur.y, 
								0,
								0, 
								0, 
								0,
								0, 
								0,
								3,
								id,
								false,//gimmi
								false,//ak
								false,//ping
								false);//pong
		return pack;
	}
	/** Creation d'un paquet de cellule */
	private Paquet getCellPack(Cell cell, int id_q){
		int type = 0;
		int nb = 0;
		if(cell.isRoad()){
			type = Identifiants.roadBloc;
			nb = cell.getNbRoutes();
		}
		else if(cell.isFreeBloc()){
			type = Identifiants.vide;
		}
		else if(cell.isCanal()){
			type = Identifiants.canal;
			nb = cell.getNbCanaux();
		}
		Paquet pack = new Paquet(
								1, 
								1, 
								cell.getCentreX()-1, 
								cell.getCentreY()-1, 
								1, //Z
								0, 
								nb, 
								cell.getRotation(),
								type, 
								0,
								2,
								id_q,
								false,//gimmi
								false,//ak
								false,//ping
								false);//pong
		return pack;
	}
	/** Creation d'un paquet batiment */
	private Paquet getRLEPack(RLE rle){
		int clos = 0;
		if(rle.getType() == Identifiants.frontiereBat){
			Frontiere frontiere = (Frontiere) rle;
			if(frontiere.getClos()){
				clos = 1;
			}
		}
		Paquet pack = new Paquet(
								rle.getTailleX(), 
								rle.getTailleY(), 
								rle.getStartX()-0.5f, 
								rle.getStartY()-0.5f, 
								1, //Z
								0, 
								0,//rle.getTexY(), 
								clos, // deviens le boolean de cloture
								rle.getType(), 
								rle.getJour(),
								4,
								rle.getIDQuartier(),
								false,//gimmi
								false,//ak
								false,//ping
								false);//pong
		return pack;
	}
	/** Création d'un paquet de rail */
	/** Creation d'un paquet batiment */
	private Paquet getRailPack(Rail rail){
		Paquet pack = new Paquet(
								rail.getID(), //l'identifiant en fait l'ordre du rail
								0, 
								rail.getX()-0.5f, 
								rail.getY()-0.5f, 
								2, //Z
								rail.getIDQn(),//le total on s'en fout en fait... on vas dire le quartier suivant
								rail.getNext(),//rle.getTexY(), -> le prochain rail
								0,
								rail.getType(), 
								rail.getJour(),
								7,
								rail.getIDQuartier(),
								false,//gimmi
								false,//ak
								false,//ping
								false);//pong
		return pack;
	}
	/** Creation d'un paquet batiment */
	private Paquet getBatPack(Batiment bat){
		float start_x = bat.getCentreX() - (bat.getTailleX()/2f);
		float start_y = bat.getCentreY() - (bat.getTailleY()/2f);
		Paquet pack = new Paquet(
								bat.getTailleX(), 
								bat.getTailleY(), 
								start_x, 
								start_y, 
								1, //Z
								bat.getTexX(), 
								bat.getTexY(), 
								bat.getPerron(),
								bat.getType(), 
								bat.getID(),
								1,
								bat.getIDQuartier(),
								false,//gimmi
								false,//ak
								false,//ping
								false);//pong
		return pack;
	}
	
	/** Creation d'un paquet bloc */
	private Paquet getBlocPack(Bloc bloc){
		int tailleX = bloc.getTailleX()-2;
		int tailleY = bloc.getTailleY()-2;
		float start_x = bloc.getStartX();
		start_x+=0.5f;
		if(start_x < 0){
			start_x+=1f;
		}
		float start_y = bloc.getStartY();
		start_y+=0.5f;
		if(start_y < 0){
			start_y+=1f;
		}
		//du bidouillage pour que ce soit aligné pour l'administratif
		if(bloc.getType() == Identifiants.admininistrationBloc){
			if(start_x < 0){
				start_x-=1f;
			}
			if(start_y < 0){
				start_y-=1f;
			}
		}
		Paquet pack = new Paquet(
								tailleX, 
								tailleY, 
								start_x, 
								start_y, 
								0, //Z
								0, 
								bloc.getTexY(), 
								0,
								bloc.getType(), 
								bloc.getID(),
								0,
								bloc.getIDQuartier(),
								false,//gimmi
								false,//ak
								false,//ping
								false);//pong
		return pack;
	}
	/** Création d'un paquet taille du quartier */
	private Paquet getQuartPack(Quartier quartier){
		Paquet pack = new Paquet(
								quartier.getTailleX(),//tailleX
								quartier.getTailleY(),//tailleY 
								quartier.getStartX(),//startX
								quartier.getStartY(),//startY
								0,//z
								0, //texX
								0, //texY
								0,//rotation
								0, //type
								0,//id
								6,//classe
								quartier.getID(),//quartier
								false,//gimmi
								false,//ak
								false,//ping
								false);//pong
		return pack;
	}
}
