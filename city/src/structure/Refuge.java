package structure;

import java.util.ArrayList;


import tools.Identifiants;
import tools.Utils;

public class Refuge extends Batiment{
	private ArrayList<Integer> hobos;
	public Refuge(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.refugeBat;
		this.hobos = new ArrayList<Integer>();
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
/*----------------------------SETTEURS----------------------*/
	/** retire un sans abris du refuge */
	public void removeHobo(int id){
		this.hobos = Utils.removeInArrayList(this.hobos, id);
	}
	/** ajoute un sans abris */
	public void addHobo(int id){
		this.hobos.add(id);
	}
	/** met à jour les sans abris */
	public void resetHobos(ArrayList<Integer> ho){
		this.hobos = ho;
	}
/*---------------------------ACCESSEURS---------------------*/
	/** retourne les sans abris */
	public ArrayList<Integer> getHobos(){
		return this.hobos;
	}
	/** renvois la consommation energétique du batiment de refuge 
	 * des sans abris */
	public int getConsommationEnergie() {
		return this.hobos.size();
	}
}
