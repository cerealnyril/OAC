package structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;


import profils.ProfilMetier;
import tools.Identifiants;
import tools.Utils;

public class Restaurant extends Batiment{
	private int directeur;
	private static int MAX_CUISINIERS = 3;
	private ArrayList<Integer> cuisiniers;
	private static int MAX_SERVEURS = 6;
	private ArrayList<Integer> serveurs;
	private static int MORALE = 0;
	private boolean recrutement;
	ProfilMetier pj;
	/** la richesse du restaurant est partiellement prélevée par le quartier.*/
	private double caisse;
	
	public Restaurant(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.restaurantBat;
		this.caisse = 0.0;
		this.directeur = -1;
		this.recrutement = false;
		this.cuisiniers = new ArrayList<Integer>();
		this.serveurs = new ArrayList<Integer>();
		this.pj = new ProfilMetier(identifiant);
		setParams();
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 3;
		super.facade = 2;
	}
/*-------------------------------ACCESSEURS----------------------------*/
	public int getConsommationEnergie(){
		return cuisiniers.size()*5;
	}
	public int getMorale(){
		return this.MORALE;
	}
	/** la capacite de service d'un restaurant dépend du nombre de serveur,
	 * sachant que les serveurs sont embauché à raison de deux par cuisiniers*/
	private int getCapacite(){
		return this.serveurs.size();
	}
	/** retourne la caisse du magasin */
	public double getRichesse(){
		return this.caisse;
	}
	/** retourne la liste du personnel. 0 pour directeur 1 pour vendeur et 2 pour artisant */
	public TreeMap<Integer, Integer> getPersonnel(){
		TreeMap<Integer, Integer> result = new TreeMap<Integer, Integer>();
		if(directeur != -1){
			result.put(directeur, 3);
		}
		if(cuisiniers.size() > 0){
			Iterator<Integer> iter = cuisiniers.iterator();
			while(iter.hasNext()){
				result.put(iter.next(), 2);
			}
		}
		if(serveurs.size() > 0){
			Iterator<Integer> iter = serveurs.iterator();
			while(iter.hasNext()){
				result.put(iter.next(), 1);
			}
		}
		return result;
	}
	/** renvois le profil du metier du magasin */
	public ProfilMetier getProfilMetier(){
		return this.pj;
	}
/*----------------------------GESTION DU PERSONNEL------------------------*/
	/** vire des gens */
	public boolean licenciement(int licencie){
		boolean fired = false;
		if(licencie == directeur){
			directeur = -1;
			fired = true;
		}
		else if(cuisiniers.contains(licencie)){
			removeCuisinier(licencie);
			fired = true;
		}
		else if(serveurs.contains(licencie)){
			removeServeur(licencie);
			fired = true;
		}
		return fired;
	}
	/** enleve un cuisinier */
	private void removeCuisinier(int id){
		this.cuisiniers = Utils.removeInArrayList(this.cuisiniers, id);
	}
	/** enleve un serveur */
	private void removeServeur(int id){
		this.serveurs = Utils.removeInArrayList(this.serveurs, id);
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
	
	/** embauche les candidats en temps que serveur si il reste de la place dans le magasin 
	 * et qu'il correspondent aux critères */
	public TreeMap<Integer, Integer> setServeurs(ArrayList<Integer> candidats){
		TreeMap<Integer, Integer> hierarchie = new TreeMap<Integer, Integer>();
		int nb = serveurs.size();
		if(nb < MAX_SERVEURS){
			Iterator<Integer> iter = candidats.iterator();
			while(iter.hasNext()){
				int candidat = iter.next();
				if((nb < MAX_SERVEURS)){
					serveurs.add(candidat);
					nb++;
					hierarchie.put(candidat, 1);
				}
			}
		}
		return hierarchie;
	}
	/** rajoute un seul serveur sans test (pour initialisation) */
	public void setServeur(int id){
		this.serveurs.add(id);
	}
	
	/** embauche les candidats en temps que cuisinier si il reste de la place dans le magasin 
	 * et qu'il correspondent aux critères */
	public TreeMap<Integer, Integer> setCuisiniers(ArrayList<Integer> candidats){
		TreeMap<Integer, Integer> hierarchie = new TreeMap<Integer, Integer>();
		int nb = cuisiniers.size();
		if(nb < MAX_CUISINIERS){
			Iterator<Integer> iter = candidats.iterator();
			while(iter.hasNext()){
				int candidat = iter.next();
				if((nb < MAX_CUISINIERS)){
					cuisiniers.add(candidat);
					nb++;
					hierarchie.put(candidat, 2);
				}
			}
		}
		return hierarchie;
	}
	
	/** rajoute un seul cuisinier sans test (pour initialisation) */
	public void setCuisinier(int id){
		this.cuisiniers.add(id);
	}
	
	/** versement des salaires */
	public TreeMap<Integer, Double> versementSalaire(){
		double base = (caisse/6.0);
		double total = 0;
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		if(this.directeur != -1){
			double salaire = base;
			salaires.put(directeur, salaire);
			total += salaire;
		}
		Iterator<Integer> iter_cui = this.cuisiniers.iterator();
		while(iter_cui.hasNext()){
			double salaire = base/this.cuisiniers.size();
			salaires.put(iter_cui.next(), salaire);
			total += salaire;
		}
		Iterator<Integer> iter_serv = this.serveurs.iterator();
		while(iter_serv.hasNext()){
			double salaire = base/this.serveurs.size();
			salaires.put(iter_serv.next(), salaire);
			total += salaire;
		}
		this.caisse = (caisse-total);
		return salaires;
	}
	/** etat de la demande de poste */
	public boolean getRecrutement(){
		return this.recrutement;
	}
	/** fonction qui vérifie si tout les postes du magasin sont occupés */
	private void checkRecrutement(){
		this.recrutement = false;
		if(super.type != 10){
			int total = 0;
			if(this.directeur == -1){
				total++;
			}
			total += (this.MAX_CUISINIERS-cuisiniers.size());
			total += (this.MAX_SERVEURS-serveurs.size());
			if(total > 0){
				this.recrutement = true;
				pj.setNombrePostes(total);
			}
		}
	}
	
	/** embauche de gens supplémentaires */
	public TreeMap<Integer, Integer> recrutement(ArrayList<Integer> tmp){
		ArrayList<Integer> candidats = new ArrayList<Integer>();
		candidats.addAll(tmp);
		TreeMap<Integer, Integer> hierarchie = new TreeMap<Integer, Integer>();
		/** le premier candidat est le directeur */
		int premier = candidats.get(0);
		if(setDirecteur(premier) != -1){
			hierarchie.put(premier, 3);
			candidats.remove(0);
		}
		/** le second sont les cuisiniers du magasin */
		hierarchie.putAll(setCuisiniers(candidats));
		/** enfin on rajoute des artisants */
		candidats.removeAll(hierarchie.keySet());
		hierarchie.putAll(setServeurs(candidats));
		return hierarchie;
	}
	
	/** reorganise en interne les employés suite à un licenciement */
	public void promotionInterne(Vector<Integer> nouveau){
		Iterator<Integer> iter = nouveau.iterator();
		while(iter.hasNext()){
			int id = iter.next();
			/** si il n'y a pas de directeur */
			if(this.directeur == -1){
				/** on prend l'artisant */
				if(this.cuisiniers.size() > 0 && this.cuisiniers.contains(id)){
					this.directeur = id;
					removeCuisinier(id);
				}
				/** ou le serveur */
				else{
					this.directeur = id;
					removeServeur(id);
				}
			}
			else if((this.cuisiniers.size() < this.MAX_CUISINIERS) && 
					(!this.cuisiniers.contains(id))){
				this.cuisiniers.add(id);
			}
			else{
				this.serveurs.add(id);
			}
		}
		checkRecrutement();
	}
/*---------------------------------INITIALISATION-------------------------*/
	public void setParams(){
		pj.loadProfil(super.type);
	}
}
