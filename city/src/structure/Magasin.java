package structure;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import profils.ProfilMagasin;
import profils.ProfilMetier;
import tools.Identifiants;
import tools.ParamsGlobals;
import tools.Utils;



public class Magasin extends Batiment{
	private int directeur;
	private ArrayList<Integer> vendeurs;
	private ArrayList<Integer> artisants;
	private int MAX_VENDEURS;
	private int MAX_ARTISANTS;
	private int consomation;
	private int prod_richesse;
	private String nom_type;
	private double physique;
	ProfilMagasin pm;
	ProfilMetier pj;
	/** la richesse du magasin est partiellement prélevée par le quartier.*/
	private double caisse;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	
	/** Type de magasin 
	 * - 0 : vide 
	 * - 1 : armurerie 
	 * - 2 : tailleur 
	 * - 3 : pharmacie
	 * - 4 : mechanic
	 * - 5 : biogénétic 
	 * - 6 : bijouterie*/
	
	public Magasin(int id_b, int id_q) {
		super(id_b, id_q);
		this.caisse = 100.0;
		this.directeur = -1;
		this.vendeurs = new ArrayList<Integer>();
		this.artisants = new ArrayList<Integer>();
		super.type = Identifiants.magasinBat;
		this.consomation = 0;
		this.nom_type = "Vide";	
		this.physique = 0.0;
		super.COUT = 500;
		this.pm = new ProfilMagasin();
		this.pj = new ProfilMetier(identifiant);
		this.setParams(this.type);
		internalClock();
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 1;
		super.facade = 2;
	}
/*----------------------------------------ACCESSEURS--------------------------------*/
	/** retourne le type du magasin */
	public int getEnseigne(){
		return super.type;
	}
	/** pour l'affichage stupide */
	public String getTypeNom(){
		return this.nom_type;
	}
	/** retourne la caisse du magasin */
	public double getRichesse(){
		return this.caisse;
	}
	/** retourne la consomation energétique du magasin qui dépend de son type */
	@Override
	public int getConsommationEnergie() {
		return consomation;
	}
	/** retourne la liste du personnel. 0 pour directeur 1 pour vendeur et 2 pour artisant */
	public TreeMap<Integer, Integer> getPersonnel(){
		TreeMap<Integer, Integer> result = new TreeMap<Integer, Integer>();
		if(directeur != -1){
			result.put(directeur, 3);
		}
		if(vendeurs.size() > 0){
			Iterator<Integer> iter = vendeurs.iterator();
			while(iter.hasNext()){
				result.put(iter.next(), 2);
			}
		}
		if(artisants.size() > 0){
			Iterator<Integer> iter = vendeurs.iterator();
			while(iter.hasNext()){
				result.put(iter.next(), 1);
			}
		}
		return result;
	}
	/** renvois le profil du metier du magasin */
	public ProfilMetier getProfilMetier(){
		if(super.type != 10){
			int total = 0;
			if(this.directeur == -1){
				total++;
			}
			total += (this.MAX_VENDEURS-vendeurs.size());
			total += (this.MAX_ARTISANTS-artisants.size());
			if(total > 0){
				pj.setNombrePostes(total);
			}
		}
		return this.pj;
	}
/*------------------------------------GESTION DU PERSONNEL---------------------------------------*/		
	/** vire des gens */
	public boolean licenciement(int licencie){
		boolean fired = false;
		if(licencie == directeur){
			directeur = -1;
			fired = true;
		}
		else if(vendeurs.contains(licencie)){
			removeVendeur(licencie);
			fired = true;
		}
		else if(artisants.contains(licencie)){
			removeArtisant(licencie);
			fired = true;
		}
		return fired;
	}
	/** enleve un artisant */
	private void removeArtisant(int id){
		this.artisants = Utils.removeInArrayList(this.artisants, id);
	}
	/** enleve un vendeur */
	private void removeVendeur(int id){
		this.vendeurs = Utils.removeInArrayList(this.vendeurs, id);
	}
	/** Embauche un directeur de l'établissement. Il dois etre qualifié et la place
	 * doit etre libre */
	public int setDirecteur(int candidat){
		if(directeur == -1){
			directeur = candidat;
			return -1;
		}
		return candidat;
	}
	public void addArtisant(int id){
		this.artisants.add(id);
	}
	public void addVendeur(int id){
		this.vendeurs.add(id);
	}
	/** embauche les candidats en temps que vendeurs si il reste de la place dans le magasin 
	 * et qu'il correspondent aux critères */
	public ArrayList<Integer> setVendeurs(ArrayList<Integer> candidats){
		ArrayList<Integer> to_remove = new ArrayList<Integer>();
		int nb = vendeurs.size();
		if(nb < MAX_VENDEURS){
			Iterator<Integer> iter = candidats.iterator();
			while(iter.hasNext()){
				int candidat = iter.next();
				if((nb < MAX_VENDEURS)){
					vendeurs.add(candidat);
					nb++;
					to_remove.add(candidat);
				}
			}
		}
		return to_remove;
	}
	
	/** embauche les candidats en temps qu'artisants si il reste de la place dans le magasin 
	 * et qu'il correspondent aux critères */
	public ArrayList<Integer> setArtisants(ArrayList<Integer> candidats){
		ArrayList<Integer> to_remove = new ArrayList<Integer>();
		int nb = artisants.size();
		if(nb < MAX_ARTISANTS){
			Iterator<Integer> iter = candidats.iterator();
			while(iter.hasNext()){
				int candidat = iter.next();
				if((nb < MAX_ARTISANTS)){
					artisants.add(candidat);
					nb++;
					to_remove.add(candidat);
				}
			}
		}
		return to_remove;
	}
	/** versement des salaires */
	public TreeMap<Integer, Double> versementSalaire(){
//		System.out.println("caisse "+caisse);
		double base = (caisse/6.0);
		double total = 0;
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		if(this.directeur != -1){
			double salaire = base;
			salaires.put(directeur, salaire);
			total += salaire;
		}
		Iterator<Integer> iter_art = this.artisants.iterator();
		while(iter_art.hasNext()){
			double salaire = base/this.artisants.size();
			salaires.put(iter_art.next(), salaire);
			total += salaire;
		}
		Iterator<Integer> iter_vend = this.vendeurs.iterator();
		while(iter_vend.hasNext()){
			double salaire = base/this.vendeurs.size();
			salaires.put(iter_vend.next(), salaire);
			total += salaire;
		}
//		System.out.println("total "+total);
		this.caisse = (caisse-total);
		return salaires;
	}
/*---------------------------------------EMBAUCHE-----------------------------------------*/	
	/** embauche de gens supplémentaires */
	public void recrutement(ArrayList<Integer> candidats){
		ArrayList<Integer> to_remove = new ArrayList<Integer>();
		/** le premier candidat est le directeur */
		int premier = candidats.get(0);
		if(setDirecteur(premier) != -1){
			to_remove.add(premier);
			candidats.remove(0);
		}
		/** les second sont les artisants du magasin */
		to_remove.addAll(setArtisants(candidats));
		/** enfin on rajoute les vendeurs */
		candidats.removeAll(to_remove);
		to_remove.addAll(setVendeurs(candidats));
	}
	
	/** reorganise en interne les employés suite à un licenciement */
	public void promotionInterne(Vector<Integer> nouveau){
		Iterator<Integer> iter = nouveau.iterator();
		while(iter.hasNext()){
			int id = iter.next();
			/** si il n'y a pas de directeur */
			if(this.directeur == -1){
				/** on prend l'artisant */
				if(this.artisants.size() > 0 && this.artisants.contains(id)){
					this.directeur = id;
					removeArtisant(id);
				}
				/** ou le vendeur */
				else{
					this.directeur = id;
					removeVendeur(id);
				}
			}
			else if((this.artisants.size() < this.MAX_ARTISANTS) && 
					(!this.artisants.contains(id))){
				this.artisants.add(id);
			}
			else{
				this.vendeurs.add(id);
			}
		}
	}
/*-------------------------------------GESTION DU TYPE DE MAGASIN----------------------------*/	
	
	/** vas charger les parametres de la classe depuis les classes abstraites en fonction du type */
	public void setParams(int t){
		pm.loadProfil(t);
		this.nom_type = pm.getNom();
		this.MAX_VENDEURS = pm.getMaxVend();
		this.MAX_ARTISANTS = pm.getMaxArt();
		this.consomation = pm.getConso();
		this.prod_richesse = pm.getRevenus();
		this.physique = pm.getPhysique();
		this.type = pm.getType();
		pj.loadProfil(t);
	}
/*--------------------------------------------HORLOGE INTERNE-------------------------------*/	
	/** Thread des fonctions qui incremente la richesse totale du magasin  */
	public void internalClock() {
		final Runnable checker = new Runnable() {
			public void run() { 
				incrementRichesse();
			}
		};
		scheduler.scheduleAtFixedRate(checker, ParamsGlobals.HEURES, ParamsGlobals.HEURES, SECONDS);
	}
	/** incremente la richesse du magasin depuis le type */
	private void incrementRichesse(){
		caisse += prod_richesse;
	}	
}
