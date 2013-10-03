package structure;

import tools.Identifiants;

public class Citadelle extends Batiment{

	public Citadelle(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.citadelle;
		this.identifiant = -1;
		setTaille();
	}
	
	@Override
	public int getConsommationEnergie() {
		return 0;
	}
	private void setTaille(){
		super.profondeur = 14;
		super.facade = 14;
	}
}
