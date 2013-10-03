package structure;

import java.util.ArrayList;


import tools.Identifiants;
import tools.Utils;
/** Les internats contienent l'ensemble des enfants il n'y a pas de limites de capacités */
public class Pensionnat extends Batiment{

	private ArrayList<Integer> internes;
	
	public Pensionnat(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.pensionnatBat;
		internes = new ArrayList<Integer>();
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
/*-----------------------GESTION DES ENFANTS--------------------*/
	/** rajout de nouveau enfants */
	public void ajoutEnfants(ArrayList<Integer> nouveaux){
		internes.addAll(nouveaux);
	}
	/** rajout d'un nouvel enfant */
	public void ajoutEnfant(int nouveau){
		internes.add(nouveau);
	}
	/** sortie d'enfants */
	public void sortieEnfants(int sortie){
		this.internes = Utils.removeInArrayList(this.internes, sortie);
	}
/*----------------------ACCESSEURS-----------------------------*/
	/** consommation energétique de l'internat 
	 * @return consommation de l'internat */
	public int getConsommationEnergie(){
		return internes.size()/2;
	}
	/** renvois la liste des identifiants des enfants */
	public ArrayList<Integer> getEnfants(){
		return this.internes;
	}
}
