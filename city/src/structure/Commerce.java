package structure;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import profils.ProfilMetier;
import tools.Identifiants;
import tools.ParamsGlobals;




public class Commerce extends Bloc{
	private double richesse;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public Commerce(int id) {
		super(id);
		super.type = Identifiants.commerceBloc;
		super.type_txt = "Commerce";
		super.MAX_BAT = 3;
		setTaille();
		this.listeBatiments = new TreeMap<Integer, Magasin>();
		this.richesse = 0.0;
		internalClock();
	}
	private void setTaille(){
		super.profondeur = 6;
		super.facade = 1;
	}
	/** initialisation des magasins*/
	public void initMagasins(){
		/** par defaut les terrain sont remplis avec des enseignes vides */
		for(int i = 0; i < MAX_BAT; i++){
			Magasin mag = new Magasin(this.identifiant, this.id_quartier);
			ajoutBatiment(mag);
		}
	}
/*--------------------------------------------ACCESSEURS-------------------------------------------*/	

	/** renvois les offres d'emplois */
	@SuppressWarnings("unchecked")
	public ArrayList<ProfilMetier> getEmplois(){
		ArrayList<ProfilMetier> result = new ArrayList<ProfilMetier>();
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		while(iter.hasNext()){
			int id_bat = iter.next();
			Magasin bat = (Magasin) listeBatiments.get(id_bat);
			ProfilMetier pm = bat.getProfilMetier();
			if(pm.getNombrePostes() > 0){
				pm.setBloc(this.getID());
				result.add(pm);
			}
		}
		return result;
	}
	/** renvois le type d'un magasin */
	public int getMagType(int id_bat){
		Magasin mag = (Magasin) this.listeBatiments.get(id_bat);
		return mag.getEnseigne();
	}
	/** versement du salaire, renvois une map avec l'id de la personne et son salaire */
	@SuppressWarnings("unchecked")
	public TreeMap<Integer, Double> versementSalaires(){
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		while(iter.hasNext()){
			Magasin mag = (Magasin) listeBatiments.get(iter.next());
			salaires.putAll(mag.versementSalaire());
		}
		return salaires;
	}
/*-----------------------------GESTION DES RICHESSES DES MAGASINS------------------*/	
	/** Mise à jour de la richesse du bloc de commerce */
	@SuppressWarnings("unchecked")
	private void getRichesse(){
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Magasin mag = (Magasin) listeBatiments.get(id);
			richesse += mag.getRichesse();
		}
	}
	/** préleve une partie de la richesse du quartier de commerce en fonction du taux d'imposition */
	public double impots(double prelevement){
		double result = richesse*prelevement;
		richesse = (richesse - result);
		return result;
	}
/*----------------------------GESTION DES EMPLOYES ET DES MAGASINS---------------------*/	
	/** envois des candidats en entretiens d'embauche dans les magasins et retourne les niveaux 
	 * hierarchiques attribués */
	public void recrutement(ArrayList<Integer> candidats, int id_bat){
		if(candidats.size() > 0){
			Magasin bat = (Magasin) listeBatiments.get(id_bat);
			bat.recrutement(candidats);
		}
	}
	/** renvois les employés avec leur niveau hierarchique en fonction du batiment */
	public TreeMap<Integer, Integer> getEmployees(int id_bat){
		Magasin mag = (Magasin) listeBatiments.get(id_bat);
		return mag.getPersonnel();
	}
	/** vire quelqu'un sans indemnité de licenciement. Quand il meurt en général. Ce sont des fonctionnaires
	 * ils ont la sécurité de l'emplois */
	@SuppressWarnings("unchecked")
	public int licenciement(int fired){
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		boolean stop = false;
		int id_bat = -1;
		while(iter.hasNext() && !stop){
			id_bat = iter.next();
			Magasin bat = (Magasin) listeBatiments.get(id_bat);
			stop = bat.licenciement(fired);
		}
		return id_bat;
	}
	/** réorganise hierarchiquement les postes */
	public void promotionInterne(Vector<Integer> nouveau, int id_bat){
		Magasin mag = (Magasin) listeBatiments.get(id_bat);
		mag.promotionInterne(nouveau);
	}
/*----------------------------------------HORLOGE INTERNE----------------------------*/	
	/** Thread des fonctions qui vont vérifier l'état des éléments inclus dans le 
	 * quartier au fur et à mesure */
	public void internalClock() {
		final Runnable checker = new Runnable() {
			public void run() { 
				getRichesse();
			}
		};
		scheduler.scheduleAtFixedRate(checker, ParamsGlobals.HEURES, ParamsGlobals.HEURES, SECONDS);
	}
}
