package structure;

import java.util.ArrayList;

import tools.Identifiants;


public class Morgue extends Batiment{
	private ArrayList<Integer> corpses;
	public Morgue(int id_b, int id_q) {
		super(id_b, id_q);
		corpses = new ArrayList<Integer>();
		super.type = Identifiants.morgueBat;
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
/*--------------------------------------ACCESSEURS----------------------------*/
	/** renvois les corps dans la morgue */
	public ArrayList<Integer> getCorpses(){
		return this.corpses;
	}
	/** calcul la consommation d'energie de la morgue qui dépend du nombre de corps*/
	public int getConsommationEnergie() {
		return this.corpses.size();
	}
/*---------------------------------------GESTION DES CORPS--------------------*/
	/** rajoute des corps dans la morgue */
	public void addCorpses(ArrayList<Integer> co){
		this.corpses.addAll(co);
	}
	/** rajoute un seul corps */
	public void addCorpse(int hab) {
		this.corpses.add(hab);
	}
	/** remet à 0 les corps dans la morgue */
	public void resetCorpses(ArrayList<Integer> co){
		this.corpses = co;
	}
}
