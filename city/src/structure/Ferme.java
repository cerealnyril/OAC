package structure;

import tools.Identifiants;

public class Ferme  extends Batiment{
	private static int MAX_FIELD = 4;
	private static int PROD_FIELD = 50;
	private int fields;
	public static int COUT_BAT = 500;
	
	public Ferme(int id_b, int id_q) {
		super(id_b, id_q);
		fields = 1;
		super.type = Identifiants.fermeBat;
		super.COUT = 500;
		super.setFrais(super.COUT);
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 3;
		super.facade = 3;
	}
/*-----------------------------ACCESSEURS--------------------------*/
	public int getFood(){
		return (fields*PROD_FIELD);
	}

	public boolean addField(){
		if(fields < MAX_FIELD){
			fields++;
			addNiveau();
			super.setFrais(super.COUT/MAX_FIELD);
			return true;
		}
		return false;
	}
	public int getNbField(){
		return this.fields;
	}
	
	public void setNbFields(int f){
		this.fields = f;
	}

	@Override
	public int getConsommationEnergie() {
		return 0;
	}
}
