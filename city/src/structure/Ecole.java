package structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;


import profils.ProfilMetier;
import tools.Identifiants;
import tools.Utils;

public class Ecole extends Batiment{
	private static int MAX_ETUDIANT_CLASSE = 10;
	private static double SALAIRE = 10.0;
	private int classes;
	private ArrayList<Integer> listeEleves;
	private ArrayList<Integer> listeProfs;
	ProfilMetier pj;
	public Ecole(int id_b, int id_q) {
		super(id_b, id_q);
		super.COUT = 750;
		super.type = Identifiants.ecoleBat;
		super.setFrais(super.COUT);
		listeEleves = new ArrayList<Integer>();
		listeProfs = new ArrayList<Integer>();
		classes = 1; 
		this.pj = new ProfilMetier(identifiant);
		pj.loadProfil(this.type);
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
/*----------------------------------ACCESSEURS----------------------------------*/
	/** les couts de fonctionnement du batiment 
	 * @return */
	public int getCouts(){
		return listeEleves.size();
	}
	/** chaque tour de quartier les enfants grandissent et donc leurs notes augmentent*/
	public TreeMap<Integer, Double> graduation(TreeMap<Integer, Double> qis){
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>();
		Iterator<Integer> iter = qis.keySet().iterator();
		while(iter.hasNext()){
			int id_enfant = iter.next();
			double qi = qis.get(id_enfant);
			result.put(id_enfant, 0.1*qi);
		}
		return result;
	}
	/** calcul la consommation energétique de l'école 
	 * @return consommation energétique du quartier */
	public int getConsommationEnergie(){
		return classes*10;
	}
	/** retourne la liste des eleves */
	public ArrayList<Integer> getEleves(){
		return this.listeEleves;
	}
	/** retourne la liste des professeurs */
	public ArrayList<Integer> getProfs(){
		return this.listeProfs;
	}
	/** renvois le nombre de classes de cours */
	public int getClasses(){
		return this.classes;
	}
	/** versement du salaire des profs */
	@SuppressWarnings("static-access")
	public TreeMap<Integer, Double> versementSalaire() {
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		Iterator<Integer> iter = this.listeProfs.iterator();
		while(iter.hasNext()){
			salaires.put(iter.next(), this.SALAIRE);
		}
		return salaires;
	}
/*---------------------------GESTION DE LA POPULATION---------------------------*/
	/** ajoute un eleve */
	public void addEleve(int id){
		listeEleves.add(id);
		if((listeEleves.size()/MAX_ETUDIANT_CLASSE) > classes){
			classes++;
			addNiveau();
			super.setFrais(super.COUT/MAX_ETUDIANT_CLASSE);
		}
		super.setFrais(10.0);
	}
	/** suppression de classes */
	public void removeEleve(int id){
		this.listeEleves = Utils.removeInArrayList(this.listeEleves, id);
	}
	/** ajoute un prof */
	public void addProf(int id){
		listeProfs.add(id);
	}
/*-------------------------------RECRUTEMENT PROFS----------------------------*/
	/** renvois le profil metier associé aux professeurs */
	public ProfilMetier getProfilMetier(){
		int total = classes - (listeProfs.size()+pj.getNombrePostes());
//		System.out.println("classes "+classes+" les profs "+listeProfs+" nombre de postes en attente "+pj.getNombrePostes());
		if(total > 0){
			pj.setNombrePostes(total);
		}
		return this.pj;
	}
	/** recrutement des professeurs */
	public TreeMap<Integer, Integer> recrutement(ArrayList<Integer> candidats) {
		TreeMap<Integer, Integer> result = new TreeMap<Integer, Integer>();
		Iterator<Integer> iter = candidats.iterator();
		while(iter.hasNext()){
			int id_prof = iter.next();
			addProf(id_prof);
			result.put(id_prof, 1);
		}
		return result;
	}
	/** licencie une personne */
	public boolean licenciement(int licencie){
		int base = this.listeProfs.size();
		this.listeProfs = Utils.removeInArrayList(this.listeProfs, licencie);
		if(this.listeProfs.size() > base){
			return true;
		}
		return false;
	}
}
