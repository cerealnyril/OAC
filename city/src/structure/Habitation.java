package structure;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import tools.Identifiants;
import tools.ParamsGlobals;




public class Habitation extends Bloc{
	/** Map des locataires avec id du locataire et id du batiment */
	private TreeMap<Integer, Integer> locataires;
	
	public Habitation(int id) {
		super(id);
		listeBatiments = new TreeMap<Integer, Immeuble>();
		locataires = new TreeMap<Integer, Integer>();
		super.type = Identifiants.habitationBloc;
		super.type_txt = "Habitation";
		super.MAX_BAT = randMaxBat();
		setTaille();
		super.frais = 0.0;
	}
	private void setTaille(){
//		int taille = 4;
		Immeuble test = new Immeuble(-1, this.id_quartier);
		super.profondeur = MAX_BAT*(test.getProfondeur())/ParamsGlobals.MULT_TAILLE;
		super.facade = test.getFacade()/ParamsGlobals.MULT_TAILLE;
	}
	private int randMaxBat(){
		double rand = Math.random();
		if(rand > 0.7){
			return 3;
		}
		return 2;
	}
	public int getMaxHab(){
		Immeuble test = new Immeuble(-1, this.id_quartier);
		return test.getCapaciteMaxGensEtage()*test.getCapaciteMaxEtage()*super.MAX_BAT;
	}
/*-----------------------------------------------ACCESSEURS---------------------------------------------*/	
	/** Cette fonction fait le total de tout les habitants de tout les batiments du
	 * quartier */
	public int getNombreGens(){
		int total = 0;
		@SuppressWarnings("unchecked")
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		while(iter.hasNext()){
			total += ((Immeuble) listeBatiments.get(iter.next())).getPopulationBatiment();
		}
		return total;
	}
	/** Cette fonction retourne la liste des gens habitant dans les batiments du quartier.
	 * C'est une sorte de map avec comme clef l'objet habitant et comme valeure l'identifiant
	 * de son batiment */
	public TreeMap<Integer, ArrayList<Integer>> getListeGens() {
		TreeMap<Integer, ArrayList<Integer>> population = new TreeMap<Integer, ArrayList<Integer>>();
		@SuppressWarnings("unchecked")
		Iterator<Integer> iter_bat = listeBatiments.keySet().iterator();
		while(iter_bat.hasNext()){
			int id_bat = iter_bat.next();
			Immeuble bat = (Immeuble) listeBatiments.get(id_bat);
			ArrayList<Integer> gens = new ArrayList<Integer>();
			gens.addAll(bat.getListeGens());
			population.put(id_bat, gens);
		}
		return population;
	}
/*--------------------------------MODIFICATEURS D'ETAT----------------------------------*/	
	/** set de l'autorisation de constuire des habitations */
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> migration(ArrayList<Integer> hobos) {
		Iterator<Integer> iter_bat = listeBatiments.keySet().iterator();
		ArrayList<Integer> to_add = new ArrayList<Integer>();
		/** rajout dans les batiments existants */
		while(iter_bat.hasNext()){
			int id_bat = iter_bat.next();
			Immeuble im = (Immeuble) listeBatiments.get(id_bat);
//			int old_etage = im.getEtages();
			to_add.addAll(hobos);
			hobos = im.addLocataires(hobos);
			super.frais += im.getFrais();
/*			if(im.getEtages() > old_etage){
				Transmission.updateImmeuble(zone);
			}*/
			/** ajout à la liste locataires */
			to_add.removeAll(hobos);
			to_add = addLocataires(to_add, id_bat);
		}
		/** création de nouveaux batiments */
		while((hobos.size() > 0) && (canBuild())){
			Immeuble im = new Immeuble(this.identifiant, this.id_quartier);
			ajoutBatiment(im);
			super.frais += im.getFrais();
			to_add.addAll(hobos);
			hobos = im.addLocataires(hobos);
			to_add.removeAll(hobos);
			to_add = addLocataires(to_add, im.getID());
		}
		return hobos;
	}

	private boolean canBuild(){
		if(listeBatiments.size() < MAX_BAT){
			return true;
		}
		return false;
	}
	/** fonction interne de rajout du locataire avec son id et le batiment correspondant dans la table
	 * des correspondances */
	private ArrayList<Integer> addLocataires(ArrayList<Integer> loc, int id_im){
		Iterator<Integer> iter_loc = loc.iterator();
		while(iter_loc.hasNext()){
			locataires.put(iter_loc.next(), id_im);
		}
		return new ArrayList<Integer>();
	}
	/** fonction qui enleve un groupe de locataires en cas de décès */
	public void removeGens(ArrayList<Integer> to_remove){
		this.locataires.keySet().removeAll(to_remove);
	}
	/** fonction qui enleve une personne */
	public void removePersonne(int to_remove){
		this.locataires.keySet().remove(to_remove);
	}
}
