package monde;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import social.Personne;
import structure.Quartier;

public class Interaction {
	private Quartier quartier;
	private TreeMap<Integer, Personne> population;
	
	public Interaction(){
		this.population = new TreeMap<Integer, Personne>();
	}
/*---------------------------------ACCESS DES HABITANTS---------------------*/
	public void registerHabitant(Personne pers){
		synchronized(this) {
			pers.setInteraction(this);
			int id = pers.getId();
			while(population.containsKey(id)){
				id++;
			}
			pers.resetID(id);
			population.put(id, pers);
	    }
	}
	public void ajoutEnfant(Personne pers){
		quartier.getAdministration().ajoutEnfant(pers.getId());
	}
	/** enleve une personne de la liste de la population a partir d'un id */
	public void removeID(int id){
		population.keySet().remove(id);
	}
	/** enleve des gens de la liste de la population à partir d'un tableau d'id*/
	public void removeIDs(ArrayList<Integer> morts){
		population.keySet().removeAll(morts);
	}
	/** supprime une personne a partir de la personne */
	public void removePersonne(Personne pers){
		this.population.remove(pers.getId());
	}
	/** supprime des gens a partir d'objets personnes */
	public void removePersonnes(ArrayList<Personne> pers){
		ArrayList<Integer> ids = this.getIDFromPersonne(pers);
		removeIDs(ids);
	}
	/** envois les morts à la ville */
/*---------------------------------------ACCESSEURS------------------------*/
	public Personne getHabitantFromID(int id){
		return population.get(id);
	}
	public ArrayList<Integer> getHabitantsID(){
		return new ArrayList<Integer>(population.keySet());
	}
	public TreeMap<Integer, Personne> getPopulation(){
		return this.population;
	}
	/** retourne le nombre d'habitants dans le quartier */
	public int getPopSize(){
		return this.population.size();
	}
	/** renvois le coefficient intellectuel d'un habitant */
	public double getIQ(int pers){
		return getHabitantFromID(pers).getQI();
	}
	/** renvois un tableau de tout les coefficients intellectuels de la population*/
	public TreeMap<Integer, Double> getIQs(){
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>();
		Iterator<Integer> iter = population.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			result.put(id, getIQ(id));
		}
		return result;
	}
	/** renvois les coefficients intellectuels spécifiques d'une partie de la population */
	public TreeMap<Integer, Double> getSpecificIQs(ArrayList<Integer> cibles){
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>();
		Iterator<Integer> iter = cibles.iterator();
		while(iter.hasNext()){
			int id = iter.next();
			result.put(id, getIQ(id));
		}
		return result;
	}
	/** met à jour le niveau d'éducation d'une partie de la population */
	public void updateEducation(TreeMap<Integer, Double> grads){
		Iterator<Integer> iter = grads.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Personne pers = population.get(id);
			pers.setEducation(grads.get(id));
		}
	}
	/** renvois le dernier identifiant de la population */
	public int populationMaxID(){
		return population.lastKey();
	}
	public Quartier getQuartier(){
		return this.quartier;
	}
	/** renvois la consommation alimentaire d'une personne */
	public int getConsommationNourriture(int id_pers){
		return population.get(id_pers).getConsommationMiam();
	}
	/** renvois la consommation alimentaire globale */
	public int getConsommationNourritureTotale(){
		int conso_food = 0;
		Iterator<Integer> iter_pers = population.keySet().iterator();
		while(iter_pers.hasNext()){
			int id_pers = iter_pers.next();
			conso_food += getConsommationNourriture(id_pers);
		}
		return conso_food;
	}
	/** renvois la valeurs en soilent de la personne */
	public double getSoilentValue(int id){
		Personne pers = population.get(id);
		double moite = (pers.getEsperanceVie()/2.0);
		double colect = (pers.getAge()/moite);
		if(colect > 1.0){
			colect = (colect-1.0);
		}
		return colect;
	}
	/** renvois la richesse de la personne */
	public double getRichesse(int id){
		return population.get(id).getCagnotte();
	}
	/** change la profession d'une personne */
	public void setProfession(int id, int prof){
		population.get(id).setProfession(prof);
	}
/*------------------------------------UTILITAIRES---------------------------*/
	/** fonction de traduction des identifiants en personnes */
	public ArrayList<Personne> getPersonnesFromID(ArrayList<Integer> ids){
		ArrayList<Personne> corps = new ArrayList<Personne>();
		Iterator<Integer> iter = ids.iterator();
		while(iter.hasNext()){
			Personne pers = population.get(iter.next());
			if(pers != null){
				corps.add(pers);
			}
		}
		return corps;
	}
	/** fonction de traduction des personnes vers les identifiants */
	public ArrayList<Integer> getIDFromPersonne(ArrayList<Personne> personne){
		ArrayList<Integer> ids = new ArrayList<Integer>();
		Iterator<Personne> iter = personne.iterator();
		while(iter.hasNext()){
			ids.add(iter.next().getId());
		}
		return ids;
	}
	/** verse les salaires aux habitants */
	public void versementSalaires(TreeMap<Integer, Double> salaires){
		Iterator<Integer> iter = salaires.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			double paie = salaires.get(id);
			population.get(id).versementSalaire(paie);
		}
	}
	/** renvois les gens et leurs argent */
	public TreeMap<Integer, Double> getPersRichesse(){
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>();
		Iterator<Integer> iter = population.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			result.put(id, population.get(id).getCagnotte());
		}
		return result;
	}
	/** renvois des gens spécifiquement dans une liste avec leur argent */
	public TreeMap<Integer, Double> getSpecificPersRichesse(ArrayList<Integer> pers){
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>();
		Iterator<Integer> iter = pers.iterator();
		while(iter.hasNext()){
			int id = iter.next();
			result.put(id, population.get(id).getCagnotte());
		}
		return result;
	}
	/** renvois la liste des critère normaux pour l'embauche */
	public TreeMap<Integer, ArrayList<String>> getSpecificPersEmbauche(ArrayList<Integer> pers){
		TreeMap<Integer, ArrayList<String>> result = new TreeMap<Integer, ArrayList<String>>();
		Iterator<Integer> iter = pers.iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Personne personne = population.get(id);
			ArrayList<String> datas = new ArrayList<String>();
			datas.add(""+personne.getEducation());
			datas.add(""+personne.getQI());
			datas.add(personne.getSexe());
			datas.add(""+personne.getAge());
			datas.add(""+personne.getEsperanceVie());
			result.put(id, datas);
		}
		return result;
	}
	/** trouve le restaurant le plus proche et emmene manger */
	public void goManger(){
		//TODO : fonction pour aller manger 
	}
/*----------------------------------INITIALISATION---------------------------*/
	public void setQuartier(Quartier quart){
		this.quartier = quart;
	}
	public void initPopulation(TreeMap<Integer, Personne> p){
		population = p;
		/** et maintenant qu'on a toute la population du quartier on
		 * leur passe cet objet */
		Iterator<Integer> iter = population.keySet().iterator();
		while(iter.hasNext()){
			int id_pers = iter.next();
			Personne pers = population.get(id_pers);
			if(pers.isAlive()){
				pers.timerStart();
			}
			pers.setInteraction(this);
		}
	}
/*----------------------------------SORTIE XML-----------------------------------*/
	/** renvois la population du quartier pour sauvegarde */
	public TreeMap<Integer, String> getPopQuart(){
		TreeMap<Integer, String > result = new TreeMap<Integer, String>();
		Iterator<Integer> iter = population.keySet().iterator();
		while(iter.hasNext()){
			int id = iter.next();
			Personne pers = population.get(id);
			double qi = pers.getQI();
			String prenom = pers.getPrenom();
			String nom = pers.getNom();
			int age = pers.getAge();
			int esperance = pers.getEsperanceVie();
			String sexe = pers.getSexe();
			int profession = pers.getProfession();
			double education = pers.getEducation();
			String content = qi+";"+prenom+";"+nom+";"+age+";"+esperance+";"+sexe+";"+profession+";"+education;
			result.put(id, content);
		}
		return result;
	}
}
