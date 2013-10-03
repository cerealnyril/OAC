package structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;


import profils.ProfilMetier;

import tools.Identifiants;
import tools.Utils;

public class Commissariat extends Batiment{
	private ArrayList<Integer> commissaires;
	private ArrayList<Integer> inspecteurs;
	private ArrayList<Integer> policiers;
	ProfilMetier pj;
	private int capacite;
	private static double SALAIRE = 5.0;
	private static int EFFICACITE = 10;
	private static int MAX_COM = 1;
	private static int MAX_INS = 2;
	private static int MAX_COP = 4;
	
	public Commissariat(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.commissariatBat;
		this.commissaires = new ArrayList<Integer>();
		this.inspecteurs = new ArrayList<Integer>();
		this.policiers = new ArrayList<Integer>();
		this.pj = new ProfilMetier(identifiant);
		this.capacite = 1;
		pj.loadProfil(this.type);
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
/*-----------------------------------ACCESSEURS-----------------------------------*/
	@Override
	public int getConsommationEnergie() {
		return (this.commissaires.size()+this.inspecteurs.size()+this.policiers.size());
	}
	public TreeMap<Integer, Integer> getPersonnel(){
		TreeMap<Integer, Integer> result = new TreeMap<Integer, Integer>();
		Iterator<Integer> iter_co = commissaires.iterator();
		while(iter_co.hasNext()){
			result.put(iter_co.next(), 3);
		}
		Iterator<Integer> iter_in = inspecteurs.iterator();
		while(iter_in.hasNext()){
			result.put(iter_in.next(), 2);
		}
		Iterator<Integer> iter_po = policiers.iterator();
		while(iter_po.hasNext()){
			result.put(iter_po.next(), 1);
		}
		return result;
	}
	/** versement des salaires des policiers */
	public TreeMap<Integer, Double> versementSalaire() {
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		Iterator<Integer> iter_co = commissaires.iterator();
		while(iter_co.hasNext()){
			salaires.put(iter_co.next(), 3.0*SALAIRE);
		}
		Iterator<Integer> iter_in = inspecteurs.iterator();
		while(iter_in.hasNext()){
			salaires.put(iter_in.next(), 2.0*SALAIRE);
		}
		Iterator<Integer> iter_po = policiers.iterator();
		while(iter_po.hasNext()){
			salaires.put(iter_po.next(), 1.0*SALAIRE);
		}
		return salaires;
	}
/*--------------------------------GESTION DU PERSONNEL-------------------------------*/
	public void recrutement(ArrayList<Integer> candidats) {
		ArrayList<Integer> to_remove = new ArrayList<Integer>();
		/** le premier candidat est le commissaire */
		to_remove.addAll(setCommissaire(candidats));
		candidats.removeAll(to_remove);
		/** le second est l'inspecteur */
		to_remove.addAll(setInspecteur(candidats));
		candidats.removeAll(to_remove);
		/** enfin on rajoute des policiers */
		setPolicier(candidats);
	}
	/** rajoute des policiers jusqu'a remplissage */
	private ArrayList<Integer> setPolicier(ArrayList<Integer> candidats) {
		ArrayList<Integer> to_remove = new ArrayList<Integer>();
		int nb = this.policiers.size();
		if(nb < MAX_COP*this.capacite){
			Iterator<Integer> iter = candidats.iterator();
			while(iter.hasNext()){
				int candidat = iter.next();
				if((nb < MAX_COP*this.capacite)){
					this.policiers.add(candidat);
					nb++;
					to_remove.add(candidat);
				}
			}
		}
		return to_remove;
	}
	/** rajoute des inspecteurs jusqu'a remplissage */
	private ArrayList<Integer> setInspecteur(ArrayList<Integer> candidats) {
		ArrayList<Integer> to_remove = new ArrayList<Integer>();
		int nb = this.inspecteurs.size();
		if(nb < MAX_INS*this.capacite){
			Iterator<Integer> iter = candidats.iterator();
			while(iter.hasNext()){
				int candidat = iter.next();
				if((nb < MAX_INS*this.capacite)){
					this.inspecteurs.add(candidat);
					nb++;
					to_remove.add(candidat);
				}
			}
		}
		return to_remove;
	}
	/** rajoute des commissaires jusqu'à remplissage */
	private ArrayList<Integer> setCommissaire(ArrayList<Integer> candidats) {
		ArrayList<Integer> to_remove = new ArrayList<Integer>();
		int nb = this.commissaires.size();
		if(nb < MAX_COM*this.capacite){
			Iterator<Integer> iter = candidats.iterator();
			while(iter.hasNext()){
				int candidat = iter.next();
				if((nb < MAX_COM*this.capacite)){
					this.commissaires.add(candidat);
					nb++;
					to_remove.add(candidat);
				}
			}
		}
		return to_remove;
	}
	/** renvois le profil metier associé aux policiers apres avoir rajouté le nombre de postes */
	public ProfilMetier getProfilMetier(int pop_size){
		int cap = capacite*EFFICACITE; 
		if(cap < pop_size){
			incrementCapacite();
		}
		else{
			int dispo = (this.inspecteurs.size()+this.policiers.size()+this.commissaires.size())*this.capacite;
			if(dispo < pop_size){
				int to_add = (((MAX_COM+MAX_INS+MAX_COP)*this.capacite)-pj.getNombrePostes());
				if(to_add > 0){
					pj.setNombrePostes(to_add);
				}
			}
		}
		return this.pj;
	}
	/** incremente la capacite */
	private void incrementCapacite(){
		capacite++;
		int add_postes = MAX_COM+MAX_INS+MAX_COP;
		pj.addPostes(add_postes);
	}
	/** rajoute un commissaire */
	public void ajoutCommissaire(int id){
		this.commissaires.add(id);
	}
	/** rajoute un inspecteur */
	public void ajoutInspecteur(int id){
		this.inspecteurs.add(id);
	}
	/** rajoute un policier */
	public void ajoutPolicier(int id){
		this.policiers.add(id);
	}
	/** supprimer un commissaire */
	private void removeCommissaire(int id){
		Utils.removeInArrayList(this.commissaires, id);
	}
	/** supprimer un inspecteur */
	private void removeInspecteur(int id){
		Utils.removeInArrayList(this.inspecteurs, id);
	}
	/** supprimer un policier */
	private void removePolicier(int id){
		Utils.removeInArrayList(this.policiers, id);
	}
	/** licenciement */
	public boolean licenciement(int id){
		boolean fired = false;
		if(this.commissaires.contains(id)){
			removeCommissaire(id);
			fired = true;
		}
		else if(this.inspecteurs.contains(id)){
			removeInspecteur(id);
			fired = true;
		}
		else if(this.policiers.contains(id)){
			removePolicier(id);
			fired = true;
		}
		return fired;
	}
}
