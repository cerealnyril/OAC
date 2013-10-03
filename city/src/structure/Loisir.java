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


public class Loisir extends Bloc{
	
	private TreeMap<Integer, ProfilMetier> emplois;
	private double richesse;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public Loisir(int id) {
		super(id);
		/** par defaut on ne peux pas construire dans un quartier de loisir */
//		super.setType(4, "Loisir");
		super.type = Identifiants.loisirBloc;
		super.type_txt = "Loisir";
		super.MAX_BAT = 4;
		setTaille();
		this.emplois = new TreeMap<Integer, ProfilMetier>();
		this.richesse = 0.0;
		listeBatiments = new TreeMap<Integer, Batiment>();
		internalClock();
	}
	private void setTaille(){
		super.profondeur = 6;
		super.facade = 2;
	}
	/** Cette fonction sert au lancement initial du jeu pour créer un
	 * certain nombre de batiments choisis aléatoirements*/
	public void generateContents(){
		
	}
	public TreeMap<Integer, Double> versementSalaires() {
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		return salaires;
	}
/*-----------------------------GESTION DES RICHESSES DES MAGASINS------------------*/	
	/** Mise à jour de la richesse du bloc de commerce */
	@SuppressWarnings("unchecked")
	private void getRichesse(){
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Restaurant re = (Restaurant) listeBatiments.get(id);
			richesse += re.getRichesse();
		}
	}
	/** préleve une partie de la richesse du quartier de commerce en fonction du taux d'imposition */
	public double impots(double prelevement){
		double result = richesse*prelevement;
		richesse = (richesse - result);
		return result;
	}
/*------------------------------------PERSONNEL----------------------------------*/
	@SuppressWarnings("unchecked")
	private ArrayList<ProfilMetier> getEmplois() {
		ArrayList<ProfilMetier> result = new ArrayList<ProfilMetier>();
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		this.emplois = new TreeMap<Integer, ProfilMetier>();
		while(iter.hasNext()){
			int id_bat = iter.next();
			Restaurant bat = (Restaurant) listeBatiments.get(id_bat);
			ProfilMetier pm = bat.getProfilMetier();
			if(pm.getNombrePostes() > 0){
				pm.setBloc(this.getID());
				result.add(pm);
			}
		}
		return result;
	}
	/** envois des candidats en entretiens d'embauche dans les batiments et retourne les niveaux 
	 * hierarchiques attribués */
	public TreeMap<Integer, Integer> recrutement(ArrayList<Integer> candidats, int id_bat){
		TreeMap<Integer, Integer> hierarchie = new TreeMap<Integer, Integer>();
		if(candidats.size() > 0){
			Restaurant re = (Restaurant) listeBatiments.get(id_bat);
			hierarchie.putAll(re.recrutement(candidats));
		}
		return hierarchie;
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
			Restaurant re = (Restaurant) listeBatiments.get(id_bat);
			stop = re.licenciement(fired);
		}
		return id_bat;
	}
	/** réorganise hierarchiquement les postes */
	public void promotionInterne(Vector<Integer> nouveau, int id_bat){
		Restaurant re = (Restaurant) listeBatiments.get(id_bat);
		re.promotionInterne(nouveau);
	}
	/** renvois les employés avec leur niveau hierarchique en fonction du batiment */
	public TreeMap<Integer, Integer> getEmployees(int id_bat){
		Restaurant re = (Restaurant) listeBatiments.get(id_bat);
		return re.getPersonnel();
	}
/*-----------------------------------ACCESSEURS----------------------------------*/
	public int getReputation(){
		int morale = 0;
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Restaurant re = (Restaurant) listeBatiments.get(id);
			morale += re.getMorale();
		}
		return morale;
	}
	/*----------------------------------------HORLOGE INTERNE----------------------------*/	
	/** Thread des fonctions qui vont vérifier l'état des éléments inclus dans le 
	 * quartier au fur et à mesure */
	public void internalClock() {
		final Runnable checker = new Runnable() {
			public void run() { 
				getRichesse();
				getEmplois();
			}
		};
		scheduler.scheduleAtFixedRate(checker, ParamsGlobals.HEURES, ParamsGlobals.HEURES, SECONDS);
	}
}
