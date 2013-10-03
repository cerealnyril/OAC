package structure;
import java.util.ArrayList;
import java.util.Iterator;

import tools.Identifiants;


public class Immeuble extends Batiment{
	/** Nombre maximum d'étages par batiment. */
	private static int MAX_HAB_ETAGES = 1;
	/** Nombre maximum d'étages permis */
	private static int MAX_ETAGES = 4;
	/** liste des habitants de l'immeuble */
	private ArrayList<Integer> listeGens;
	
	public Immeuble(int id_b, int id_q) {
		super(id_b, id_q);
		listeGens = new ArrayList<Integer>();
		super.COUT = 400;
		super.type = Identifiants.immeubleBat;
		setTaille();
		super.setFrais(super.COUT);
	}
	private void setTaille(){
		super.profondeur = 1;
		super.facade = 1;
	}
	/** renvois la capacité maximale des habitants par etage */
	public int getCapaciteMaxGensEtage(){
		return this.MAX_HAB_ETAGES;
	}
	/** renvois la capacité maximale des etages par immeuble */
	public int getCapaciteMaxEtage(){
		return this.MAX_ETAGES;
	}
	/** renvois la capacité totale */
	public int getCapaciteMaxTotal(){
		return this.MAX_ETAGES*MAX_HAB_ETAGES;
	}
	/** renvois la liste des identifiants des habitants de l'immeuble 
	 * @return liste des identifiants des habitatants de l'immeuble */
	public ArrayList<Integer> getListeGens() {
		return listeGens;
	}
	
	/** Fonction d'ajout de nouveau habitants. Peut lancer la construction d'etages, remplir l'immeuble courant
	 * et renvois les habitants en trop 
	 * @param gens : habitants a ajouter a l'immeuble */
	public ArrayList<Integer> addLocataires(ArrayList<Integer> gens) {
		/** liste des gens restants */
		ArrayList<Integer> left = new ArrayList<Integer>();
		/** vérification du nombre d'habitants à ajouter par rapport au nombre d'étage */
		int max_space = getCapaciteMaxTotal();
		int nb_gens = getPopulationBatiment();
		/** rajout des gens dans l'immeuble avec construction d'�tages */
		Iterator<Integer> iter = gens.iterator();
		while(iter.hasNext()){
			if(max_space > nb_gens){
				listeGens.add(iter.next());
				nb_gens++;
				/** on construit un etage si necessaire */
				if(checkEtages() == 1){
					super.lvl++;
					addNiveau();
					super.setFrais(super.COUT/MAX_ETAGES);
				}
			}
			else{
				left.add(iter.next());
			}
		}
		return left;
	}
	/** vérifie le nombre d'habitant. Renvois :
	 * - "-1" si le batiment doit etre detruit pour nombre d'habitants insufissants 
	 * - "0" si rien ne se passe on si on constuit ou detruit un nouvel �tage (le quartier n'a pas � le savoir)
	 * - "1" pour la construction d'un nouveau batiment (�tages */
	private int checkEtages(){
		int nb_hab = listeGens.size();
		/** si il n'y a aucun habitant on detruit le batiment */
		if(nb_hab == 0){
			return -1;
		}
		/** si le nombre d'étage est insuffisant on en créé de nouveaux */
		if(nb_hab > MAX_HAB_ETAGES*super.lvl){
			if(MAX_HAB_ETAGES >= super.lvl){
				return 1;
			}
			else{
				while(nb_hab > (MAX_HAB_ETAGES*super.lvl)){
					super.lvl++;
					addNiveau();
				}
			}
		}
		return 0;
	}
	/** calcul et retourne la consommation energétique dans l'immeuble 
	 * @return consommation energétique */
	public int getConsommationEnergie() {
		int conso = listeGens.size()+super.lvl;
		return conso;
	}

	/** retourne le nombre d'habitant de l'immeuble 
	 * @return taille de la liste des locataires */
	public int getPopulationBatiment() {
		return listeGens.size();
	}
}
