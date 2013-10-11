package elements2D;

import reseau.Paquet;
import tools.Identifiants;
import layers2D.Air;
import layers2D.Circadien;
import layers2D.Tangible;
import layers2D.Sol;
import managers.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

/** Classe qui contient les fonctions logiques de la ville */
public class Ville {
	
	private Joueur joueur;
//	private int jour;
	private int nb_current, nb_aim;

	private Sol sol;
	private Air air;
	private Tangible tangible;
	private Circadien circadien;
	
	
	private Controles controles;
	
	public Ville(ScreenManager jeu){
		this.joueur = new Joueur(-1, new Vector2(0, 0), 5f, 0, 0.20f, 0.20f, 0);
		this.controles = new Controles(this);
		Gdx.input.setInputProcessor(controles);	
//		this.jour = 0;
		this.nb_current = 0;
		this.nb_aim = -1;
		
		//chargement des assets
		AssetsLoader.load();
		
		//Création des différents groupes de niveaux
		this.sol = new Sol();
		this.tangible = new Tangible();
		this.air = new Air();
		this.circadien = new Circadien();
	}
	
	public void update(){
		//si collision alors 
		if(tangible.overlap(joueur.getBound())){
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
		air.update();
	}
	
	public void dispose(){
		AssetsLoader.dispose();
	}
/*--------------------------------------ACCESSEURS-------------------------------------*/	
	/** Renvois la quantité de model qui est à charger que le client dis */
	public float getNbAim(){
		return (float) this.nb_aim;
	}
	
	/** Renvois la quantité de models deja charge dans la ville */
	public float getNbCurrent(){
		return (float) this.nb_current;
	}
	
	/** Renvois le groupe qui contient les éléments au sol */
	public Group getSol(){
		return sol.getSol();
	}
	
	/** Renvois le groupe qui contient les éléments aeriens*/
	public Group getAir(){
		return air.getAir();
	}
	
	/** Renvois le groupe qui contient les éléments circadients en gros l'eclairage en fonction du jour */
	public Group getCircadien(){
		return this.circadien;
	}
	
	/** Renvois le groupe qui contient les éléments tangibles avec lequels on peut interagir */
	public Group getTangible(){
		return tangible.getTangible();
	}
	
	/** Renvois le joueur courant sur le plateau */
	public Joueur getJoueur(){
		return this.joueur;
	}
	
/*--------------------------ARRIVEE DE NOUVEAUX OBJETS OU UPDATES-----------------------*/	
	/** Gère l'arrive de nouveaux batiments */
	private void upBatiment(Batiment bat){
		if(tangible.upBatiment(bat)){
			air.upStation(bat);
		}
	}
	/** Gére l'arrivée de nouveaux blocs*/
	private void upBloc(Bloc bloc){
		this.sol.upBloc(bloc);
	}
	/** Récéption des routes */
	private void upRoute(Route route){
		this.sol.upRoute(route);
	}
	/** Récéption de canaux */
	private void upCanal(Canal canal){
		this.sol.upCanal(canal);
	}
	/** Gere l'arrivée de nouvelles frontieres */
	private void upFrontiere(Frontiere frontiere){
		tangible.upFrontiere(frontiere);
	}
	/** Gere l'arrivée de nouvelles rocades */
	private void upRocade(Rocade rocade){
		sol.upRocade(rocade);
	}
	/** S'occupe de modifier les points de jonction de la ligne*/
	private void upLigne(int id, float x, float y, int jour, int id_q, int next, int id_quart_next){
		//ligne.update(id, x, y, jour, id_q, next, id_quart_next);
		air.upPoint(id, x, y, jour, id_q, next, id_quart_next);
	}
	
	/** Fonction qui translate tout sur la map quand la ville grandie */
	private void upTranslate(int id_quartier, Vector2 translation){
		this.sol.translation(id_quartier, translation);
		this.tangible.translation(id_quartier, translation);
	}
	/** Transfert d'un quartier pour la dalle au sole */
	private void upQuartier(Quartier quartier){
		sol.upQuartier(quartier);
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
			upBatiment(new Batiment(pack.getId(), new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getId_q()));
		}
		//pour les cellules genre routes rails canaux chemins
		else if(pack.getClasse() == 2){
			if(pack.getType() == Identifiants.roadBloc){
				Route route = new Route(new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getTexY(), pack.getId_q());
				route.setRotation(pack.getSens()*90);
				upRoute(route);
				
			}
			else if(pack.getType() == Identifiants.canal){
				Canal canal = new Canal(new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getTexY(), pack.getId_q());
				canal.setRotation(pack.getSens()*90);
				upCanal(canal);
			}
		}
		//pour la translation
		else if(pack.getClasse() == 3){
			upTranslate(pack.getId_q(), pack.getVectorTranslate());
		}
		//pour les RLE
		else if(pack.getClasse() == 4){
			if(pack.getType() == Identifiants.frontiereBat){
				upFrontiere(new Frontiere(new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getId_q(), pack.getJour(), pack.getClos()));
			}
			else if(pack.getType() == Identifiants.roadBloc){
				upRocade(new Rocade(new Vector2(pack.getX(), pack.getY()), pack.getL(), pack.getl(), pack.getType(), pack.getId_q(), pack.getJour()));
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
