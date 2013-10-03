package monde;

/** classe de fonctions statiques permettant de calculer les ratio effectifs sur les quartiers de la ville */
public class Analyse {
	/** retourne le ratio de consommation energétique */
	public static double ratioFarm(double conso_food, double prod_food){
		double ratio = conso_food/prod_food;
		return ratio;
	}
	/** retourne le ratio de consommation de nourriture */
	public static double ratioCentrale(double conso_ene, double prod_ene){
		if(prod_ene == 0){
			prod_ene = 1.0;
		}
		double ratio = conso_ene/prod_ene;
		return ratio;
	}
	/** retourne le ratio d'engorgement des usines de soilent */
	public static double ratioUsine(double morts, double cap_usine){
		if(cap_usine == 0){
			cap_usine = 1.0;
		}
		double ratio = morts/cap_usine;
		return ratio;
	}
	/** retourne le ratio de sans abris sur la population totale moins les enfants */
	public static double ratioHabitation(double pop_totale, double sans_abris){
		if(sans_abris < 1.0){
			return 0.0;
		}
		double enfants = pop_totale - sans_abris;
		double ratio = sans_abris/(pop_totale-enfants);
		return ratio;
	}
	/** calcul le ratio d'enfants non scholarisés sur le nombre d'enfants  */
	public static double ratioEducation(double non_scholars, double scholars){
		if(non_scholars < 1.0){
			return 0.0;
		}
		double ratio = scholars/non_scholars;
		return ratio;
	}
	/** Calcul le ration de decorations dans la ville */
	public static double rationDecoration(int blocs_total, int deco_total) {
		double ratio = 0.0;
		if(deco_total == 0){
			ratio = 1.0;
		}
		else{
			ratio = blocs_total/(deco_total*3.0);
		}
		return ratio;
	}
	/** calcul si le ratio de chance que l'on un proces pour le soilent */
	public static double ratioPeinalSoilent(double soilent, double hobos) {
		double ratio_soilent = 0.0;
		if(hobos > 0.0){
			ratio_soilent = 1.0-(soilent/1000.0);
			if(ratio_soilent < 0.0){
				ratio_soilent = 0.0;
			}
		}
		return ratio_soilent;
	}
	/** calcul le ratio de chance que l'on fasse un proces pour l'argent */
	public static double ratioPeinalCagnotte(double cagnotte, boolean build) {
		double ratio_cagnotte = 0.0;
		if(!build){
			ratio_cagnotte = 1.0-(cagnotte/1000.0);
			if(ratio_cagnotte < 0.0){
				ratio_cagnotte = 0.0;
			}
		}
		return ratio_cagnotte;
	}
}
