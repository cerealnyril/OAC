package structure;

import java.util.ArrayList;

import tools.Identifiants;


public class Sanitarium extends Batiment{
	private ArrayList<Integer> patients;
	public Sanitarium(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.sanitariumBat;
		patients = new ArrayList<Integer>();
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
	@Override
	public int getConsommationEnergie() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void addPatient(int id){
		patients.add(id);
	}
}
