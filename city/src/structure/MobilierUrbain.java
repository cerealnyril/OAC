package structure;

import tools.Identifiants;

public class MobilierUrbain extends Batiment{

	public MobilierUrbain(int id_b, int id_q) {
		super(id_b, id_q);
	}

	@Override
	public int getConsommationEnergie() {
		return 0;
	}

	/** Vu qu'il n'y a qu'un objet de mobilier urbain par bloc de décoration on vas piocher en fonction de la taille du bloc */
	public void generateType(int x, int y){
		super.profondeur = x;
		super.facade = y;
		//c'est un arbre ou une statue
		if(x == 1 && y == 1){
			double rand = Math.random();
			if(rand < 0.5){
				super.type = Identifiants.treeBat;
			}
			else{
				super.type = Identifiants.statueBat;
			}
		}
		//c'est une place (une fontaine)
		else if(x == 2 && y == 2){
			super.type = Identifiants.placeBat;
		}
		// c'est un parc
		else{
			super.type = Identifiants.parcBat;
		}
	}
}
