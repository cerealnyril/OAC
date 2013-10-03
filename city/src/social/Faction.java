package social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

public class Faction {
	/** taux de naissance et de mort compris entre 0 et 1*/
	protected double natalite, mortalite;
	/** ratio de naissance entre les garçon et les filles */
	protected double ratio_HF;
	/** stack de priorité de construction */
	protected TreeMap<Integer, Integer> priorite_eco;
	/** parametres par defaut */
	TreeMap<Integer, Double> ratios_theo;
	/** limite en deça duquel on ne construit pas*/
	protected static double palier = 0.5;
	protected double impostion;
	
	public Faction(){
		setParams();
	}
/*---------------------------------ACCESSEURS-----------------------*/
	/** envois le taux de natalite. Il est inverse. Plus il est proche de 0
	 * et plus il est fort */
	public double getNatalite(){
		return this.natalite;
	}
	/** envois le taux de mortalite. Il est inverse. Plus il est proche de 0
	 * et plus il est fort */
	public double getMortalite(){
		return this.mortalite;
	}
	/** renvois le ratio de naissance entre garçon et filles */
	public double getRatioHF(){
		return this.ratio_HF;
	}
/*-----------------------------PARAMETRES---------------------------*/	
	/** - 0 : centrale
	  * - 1 : ferme
	  * - 2 : usine
	  * - 3 : immeuble
	  * - 4 : magasin armurerie
	  * - 5 : magasin bijouterie
	  * - 6 : biogen
	  * - 7 : mecha
	  * - 8 : pharma
	  * - 9 : tailleur
	  * - 10 : vide
	  * - 11 : internat
	  * - 12 : ecole */
	private void setParams(){
		this.natalite = 0.5;
		this.mortalite = 0.5;
		this.ratio_HF = 0.4;
		this.impostion = 0.5;
		ratios_theo = new TreeMap<Integer, Double>();
		/** 0 : centrale */
		ratios_theo.put(0, 0.6);
		/** 1 : ferme */
		ratios_theo.put(1, 0.9);
		/** 2 : usine */
		ratios_theo.put(2, 1.0);
		/** 3 : immeuble */
		ratios_theo.put(3, 0.9);
		/** 4 : ecoles */
		ratios_theo.put(4, 0.5);
		/** 5 : decoration de la ville */
		ratios_theo.put(5, 1.0);
	}
	public Vector<Integer> getDecisionConstruction(TreeMap<Integer, Double> ratios_reel){
		Vector<Integer> result = new Vector<Integer>();
		TreeMap<Double, Integer> tmp = new TreeMap<Double, Integer>();
		Iterator<Integer> iter = ratios_theo.keySet().iterator();
		while(iter.hasNext()){
			int type = iter.next();
			double valeure = ratios_reel.get(type)/ratios_theo.get(type);
			if(valeure > palier){
				while(tmp.containsKey(valeure)){
					valeure += 1.0/1000000;
				}
				tmp.put(valeure, type);
			}
		}
		Iterator<Double> iter_tmp = tmp.keySet().iterator();
		while(iter_tmp.hasNext()){
			result.add(tmp.get(iter_tmp.next()));
		}
		Collections.reverse(result);
		return result;
	}
	public double getImposition(){
		return this.impostion;
	}
}
