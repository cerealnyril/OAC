package structure;

import tools.Identifiants;

public class Banque extends Batiment{
	private double cagnotte;
	private double soilent;
	public Banque(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.banqueBat;
		this.cagnotte = 0.0;
		this.soilent = 0.0;
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
/*-------------------------ACCESSEURS------------------------*/
	/** renvois la cagnotte financiere de la ville */
	public double getCagnotte(){
		return this.cagnotte;
	}
	/** renvois le soilent stocké dans la ville */
	public double getSoilent(){
		return this.soilent;
	}
	@Override
	public int getConsommationEnergie() {
		return 0;
	}
/*-------------------------UTILITAIRES------------------------*/
	/** retrait des frais de construction*/
	public boolean autorisation_construction(double prix){
		if(prix < cagnotte){
			cagnotte = (cagnotte - prix);
			return true;
		}
		return false;
	}
/*--------------------------SETTEURS--------------------------*/
	/** ajout d'argent dans la cagnotte */
	public void updateCagnotte(double ajout){
		this.cagnotte += ajout;
	}
	/** ajout de soilent dans la banque */
	public void updateSoilent(double soilent){
		this.soilent += soilent;
	}
/*-------------------------INITIALISATION----------------------*/
	/** initialisation de la cagnotte de la banque*/
	public void setCagnotte(double cagnotte){
		this.cagnotte = cagnotte;
	}
	/** initialisation du soilent dans la banque */
	public void setSoilent(double soilent){
		this.soilent = soilent;
	}
}
