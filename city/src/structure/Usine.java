package structure;

import tools.Identifiants;

public class Usine extends Batiment{
	private static int MAX_GRINDER = 4;
	private int grinder;
	
	public Usine(int id_b, int id_q) {
		super(id_b, id_q);
		grinder = 1;
		super.type = Identifiants.usineBat;
		super.COUT = 500;
		super.setFrais(super.COUT);
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 3;
		super.facade = 3;
	}
/*------------------------------ACCESSEURS---------------------------------*/
	@Override
	public int getConsommationEnergie() {
		return this.grinder*5;
	}
/*-----------------------------TRAITEMENT DES CORPS----------------------- */	
	/** en fonction de la source */
	public double extractSoilent(double val){
		return (val*100.0);
	}
	public int getSpeed(){
		return this.grinder;
	}
/*------------------------GESTION DE LA CONSTRUCTION------------------------*/	
	/** Rajout d'un grinder pour augmenter la vitesse de traitement */	
	public boolean addGrinder(){
		if(grinder < MAX_GRINDER){
			addNiveau();
			grinder++;
			super.setFrais(super.COUT/MAX_GRINDER);
			return true;
		}
		return false;
	}
	public int getNbGrinder(){
		return this.grinder;
	}
	
	public void setNbGrinders(int g){
		this.grinder = g;
	}
}
