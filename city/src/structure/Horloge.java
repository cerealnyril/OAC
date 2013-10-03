package structure;

import tools.Identifiants;

public class Horloge extends Batiment{

	public Horloge(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.horlogeBat;
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 4;
		super.facade = 4;
	}
	@Override
	public int getConsommationEnergie() {
		return 0;
	}

}
