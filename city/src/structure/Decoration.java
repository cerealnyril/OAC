package structure;

import java.util.TreeMap;

import tools.Identifiants;


public class Decoration extends Bloc{
/** Cette classe servira aux structures decoratives */
	public Decoration(int id) {
		super(id);
		listeBatiments = new TreeMap<Integer, Batiment>();
		super.type_txt = "Decorations";
		super.type = Identifiants.decorationBloc;
		setTaille();
	}
	/** pour les décorations de la ville on a toujours qu'un seul batiment qui peut etre de 3 formes */
	private void setTaille(){
		double rand = Math.random();
		if(rand < 0.5){
			super.profondeur = 1;
			super.facade = 1;
		}
		else if(rand < 0.8){
			super.profondeur = 2;
			super.facade = 2;
		}
		else{
			super.profondeur = 2;
			super.facade = 3;
		}
	}
	/** génére le mobilier urbain associé */
	public void initMU(){
		MobilierUrbain mu = new MobilierUrbain(this.identifiant, this.id_quartier);
		mu.generateType(super.profondeur, super.facade);
		ajoutBatiment(mu);
	}
}
