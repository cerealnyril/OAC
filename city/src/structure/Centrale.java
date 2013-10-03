package structure;

import tools.Identifiants;

public class Centrale extends Batiment{
	private static int MAX_CORE = 8;
	private static int PROD_CORE = 50;
	private int cores;
	
	public Centrale(int id_b, int id_q) {
		super(id_b, id_q);
		cores = 1;
		super.type = Identifiants.centraleBat;
		super.COUT = 750;
		super.setFrais(super.COUT);
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 3;
		super.facade = 3;
	}
	@Override
	public int getConsommationEnergie() {
		return 0;
	}
	public int getEnergie(){
		return (PROD_CORE*cores);
	}
	public boolean addCore(){
		if(cores < MAX_CORE){
			cores++;
			addNiveau();
			super.setFrais(super.COUT/MAX_CORE);
			return true;
		}
		return false;
	}
	public int getNbCore(){
		return this.cores;
	}
	public void setNbCores(int c){
		this.cores = c;
	}
}
