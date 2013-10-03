package structure;

import static java.util.concurrent.TimeUnit.SECONDS;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


import tools.Identifiants;
import tools.ParamsGlobals;
import tools.Utils;



public class Production extends Bloc{
	private double soilent;
	private ArrayList<Double> source;
	/** Rappel des types 
	 * - 0 : centrale
	 * - 1 : ferme 
	 * - 2 : usine */
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public Production(int id) {
		super(id);
		listeBatiments = new TreeMap<Integer, Batiment>();
		super.type = Identifiants.productionBloc;
		super.type_txt = "Production";
		super.MAX_BAT = 2;
		setTaille();
		soilent = 0.0;
		source = new ArrayList<Double>();
	}
	private void setTaille(){
//		int taille = 16;
		super.profondeur = 3;
		super.facade = 6;
	}
/*---------------------------------ACCESSEURS----------------------------*/	
	/** calcul la production énergétique totale */
	public int getEnergie(){
		Iterator<Integer> iter = getCentralesIDs().iterator();
		int energie = 0;
		while(iter.hasNext()){
			Centrale cent = getCentrale(iter.next());
			energie += cent.getEnergie();
		}
		return energie;
	}
	
	/** calcul de la production de nourriture totale */
	public int getFood(){
		Iterator<Integer> iter = getFermesIDs().iterator();
		int food = 0;
		while(iter.hasNext()){
			Ferme farm = getFerme(iter.next());
			food += farm.getFood();
		}
		return food;
	}
	private ArrayList<Integer> getCentralesIDs(){
		return super.getBatimentsFromType(0);
	}
	private ArrayList<Integer> getFermesIDs(){
		return super.getBatimentsFromType(1);
	} 
	private ArrayList<Integer> getUsinesIDs(){
		return super.getBatimentsFromType(2);
	}
	private Centrale getCentrale(int id){
		return (Centrale) this.listeBatiments.get(id);
	}
	private Ferme getFerme(int id){
		return (Ferme) this.listeBatiments.get(id);
	}
	private Usine getUsine(int id){
		return (Usine) this.listeBatiments.get(id);
	}
	/** renvois la capacité globale de process des usines */
	public int getUsineCap(){
		int cap = 0;
		@SuppressWarnings("unchecked")
		Iterator<Integer> iter = listeBatiments.keySet().iterator();
		while(iter.hasNext()){
			int id_bat = iter.next();
			if(((Batiment) listeBatiments.get(id_bat)).getType() == 2){
				Usine us = (Usine) listeBatiments.get(id_bat);
				cap += us.getNbGrinder();
			}
		}
		return cap;
	}
/*-------------------------------------CONSTRUCTION-----------------------------------*/	
	/** Gestion de la création d'une centrale, agrandissement ou ajout. 
	 * Si saturé renvoie faux*/
	public boolean buildCentrale(){
		boolean done = false;
		Iterator<Integer> iter = getCentralesIDs().iterator();
		while(iter.hasNext() && (!done)){
			Centrale cent = getCentrale(iter.next());
			if(cent.addCore()){
//				Transmission.updateCentrale(this.getID(), cent);
				frais +=  cent.getFrais();
				done = true;
			}
		}
		/** si un réacteur n'a pas suffit on essaie de construire une nouvelle centrale */
		int nb_bat = listeBatiments.size();
		if((nb_bat < MAX_BAT) && (!done)){
			Centrale new_cent = new Centrale(this.identifiant, this.id_quartier);
			ajoutBatiment(new_cent);
//			Transmission.addCentrale(super.getID(), new_cent);
			frais +=  new_cent.getFrais();
			done = true;
		}
		return done;
	}
	
	/** gestion de la construction des fermes. Agrandissement ou création d'une nouvelle. 
	 * Si saturé renvois faux */
	public boolean buildFarm(){
		boolean done = false;
		Iterator<Integer> iter = getFermesIDs().iterator();
		while(iter.hasNext() && (!done)){
			Ferme farm = getFerme(iter.next());
			if(farm.addField()){
				frais +=  farm.getFrais();
				done = true;
			}
		}
		/** si un champ n'a pas suffit on essaie de construire une nouvelle ferme */
		int nb_bat = listeBatiments.size();
		if(nb_bat < MAX_BAT && (!done)){
			Ferme new_farm = new Ferme(this.identifiant, this.id_quartier);
			ajoutBatiment(new_farm);
			frais +=  new_farm.getFrais();
			done = true;
		}
		return done;
	}
	
	/** gestion de la construction des usines. Agrandissement ou création d'une nouvelle. 
	 * Si saturé renvoie vrai */
	public boolean buildUsine(){
		boolean need = true;
		Iterator<Integer> iter = getUsinesIDs().iterator();
		while(iter.hasNext() && (need)){
			Usine us = getUsine(iter.next());
			if(us.addGrinder()){
//				Transmission.updateUsine(zone);
				frais +=  us.getFrais();
				need = false;
			}
		}
		/** si un grinder n'a pas suffit on essaie de construire une nouvelle usine */
		int nb_bat = listeBatiments.size();
		if(nb_bat < MAX_BAT && (need)){
			Usine new_us = new Usine(this.identifiant, this.id_quartier);
			ajoutBatiment(new_us);
//			Transmission.addUsine(zone);
			frais +=  new_us.getFrais();
			need = false;
		}
		return need;
	}
	/** création d'un noveau bloc de production en fonction des paramètres
	 * @param nb_farm : nombre de fermes 
	 * @param nb_cent : nombre de centrales */
	public void creation(int nb_farm, int nb_cent){
		/** construction des fermes */
		for(int i = 0; i < nb_farm; i++){
			buildFarm();
		}
		/** construction des centrales */
		for(int i = 0; i < nb_farm; i++){
			buildCentrale();
		}
	}
	public void createCentrale(int nb_cent){
		for(int i = 0; i < nb_cent; i++){
			buildCentrale();
		}
	}
	public void createFerme(int nb_farm){
		for(int i = 0; i < nb_farm; i++){
			buildFarm();
		}
	}
/*---------------------------------GESTION SOILENT-----------------------------*/	
	/** extrait les gens en fonction du stock de soilent */
	private void extractSoilent(){
		Iterator<Integer> iter = getUsinesIDs().iterator();
		int s = 0;
		int max_s = source.size();
		ArrayList<Double> processessed = new ArrayList<Double>();
		while(iter.hasNext()){
			Usine us = getUsine(iter.next());
			for(int i = 0; i < us.getNbGrinder(); i++){
				if(s < max_s){
					soilent += us.extractSoilent(source.get(i));
					processessed.add(source.get(i));
					s++;
				}
			}
		}
		source.removeAll(processessed);
	}
	/** récupération du soilent pour transmission aux blocs marchants */
	public double transfertSoilent(){
		double transfert = soilent;
		soilent= 0.0;
		return transfert;
	}
	/** charge les sources à transformer en soilent qu'on sait pas ce que c'est non non non */
	public ArrayList<Integer> updateSource(TreeMap<Integer, Double> s){
		/** recuperation des capacites de stockages des usines */
		ArrayList<Integer> left = new ArrayList<Integer>();
		left.addAll(s.keySet());
		Iterator<Integer> iter_us = getUsinesIDs().iterator();
		int capacite = 0;
		while(iter_us.hasNext()){
			Usine us = getUsine(iter_us.next());
			capacite += us.getNbGrinder();
		}
		/** chargement des usines */
		Iterator<Integer> iter_pers = s.keySet().iterator();
		int loaded = 0;
		while(iter_pers.hasNext() && (loaded < capacite)){
			loaded++;
			int id_pers = iter_pers.next();
			source.add(s.get(id_pers));
			left = Utils.removeInArrayList(left, id_pers);
		}
		/** lancement des usines */
		internalClock();
		return left;
	}
/*------------------------------------------HORLOGE-------------------------------------*/
	/** Thread des fonctions qui updater la quantité de soilentgreen/sponch dispo */
	public void internalClock() {
		final Runnable checker = new Runnable() {
			public void run() { 
				extractSoilent();
			}
		};
		final ScheduledFuture<?> smallBen = scheduler.scheduleAtFixedRate(checker, ParamsGlobals.HEURES, ParamsGlobals.HEURES, SECONDS);
		if(source.size() == 0){
			smallBen.cancel(true);
		}
	}
/*--------------------------------CHARGEMENT DEPUIS FICHIER---------------------------------*/
	public void ajoutCentrale(Centrale centrale){
		ajoutBatiment(centrale);
//		Transmission.addCentrale(super.getID(), centrale);
	}
	public void ajoutFerme(Ferme ferme){
		ajoutBatiment(ferme);
//		Transmission.addFerme(super.getID(), ferme);
	}
	public void ajoutUsine(Usine usine){
		ajoutBatiment(usine);
//		Transmission.addUsine(super.getID(), usine);
	}
}
