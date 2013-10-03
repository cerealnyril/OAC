package structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;


import profils.ProfilMetier;
import tools.Identifiants;
import tools.Utils;

public class Interim extends Batiment{
	private TreeMap<Double, ProfilMetier> postes;
	private ArrayList<Integer> sans_emplois;
	public Interim(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.interimBat;
		this.postes = new TreeMap<Double, ProfilMetier>();
		this.sans_emplois = new ArrayList<Integer>();
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
/*----------------------------ACCESSEURS---------------------*/
	public ArrayList<Integer> getDemandeurs(){
		return this.sans_emplois;
	}
	@Override
	public int getConsommationEnergie() {
		return 0;
	}
/*----------------------------SETTEURS-----------------------*/
	/** rajoute une nouvelle offre d'emplois */
	public void ajoutJobs(ArrayList<ProfilMetier> jobs){
		Iterator<ProfilMetier> iter = jobs.iterator();
		while(iter.hasNext()){
			ProfilMetier pm = iter.next();
			double desirabilite = pm.getDesirabilite();
			while(postes.containsKey(desirabilite)){
				desirabilite += 0.0000001;
			}
			postes.put(1.0-desirabilite, pm);
		}
	}
	
	public void addDemandeur(int plus){
		this.sans_emplois.add(plus);
	}
	public void removeDemandeur(int moins){
		this.sans_emplois = Utils.removeInArrayList(this.sans_emplois, moins);
	}
/*---------------------SCORE LES GENS POUR LES JOBS------------------*/
	
	/** determine l'emplois que vont prendre les gens */
	public TreeMap<Integer, TreeMap<Integer, ArrayList<Integer>>> selectionMetier(TreeMap<Integer, ArrayList<String>> demandeurs){
		// map indigeste d'attribution des nouveaux emplois...
		TreeMap<Integer, TreeMap<Integer, ArrayList<Integer>>> n_e = new TreeMap<Integer, TreeMap<Integer, ArrayList<Integer>>>();
		ArrayList<Double> to_remove = new ArrayList<Double>();
		// iteration sur les postes disponibles classés par desirabilite
		Iterator<Double> iter_postes = postes.keySet().iterator();
		while(iter_postes.hasNext() && demandeurs.size() > 0){
			double clef = iter_postes.next();
			ProfilMetier pm = postes.get(clef);
			TreeMap<Integer, ArrayList<Integer>> n_e_m = new TreeMap<Integer, ArrayList<Integer>>();
			// les personnes recrutées pour ce poste
			ArrayList<Integer> recrutes = new ArrayList<Integer>();
			for(int p = 0; p < pm.getNombrePostes(); p++){
				// on score les candidats de la liste
				int id_best = selectCandidat(demandeurs, pm);
				// et ils se voient attribuer le boulot. Bravo!
				if(id_best != -1){
					removeDemandeur(id_best);
					demandeurs = cleanList(demandeurs, id_best);
					pm.reducePostes();
					recrutes.add(id_best);
				}
				/** si il ne reste plus de postes disponible on le supprime des offres d'emplois */
				if(pm.getNombrePostes() == 0){
					to_remove.add(clef);
				}
			}
			/** si on a recrute des gens on verifie si on ecrase pas une offre d'emplois existante auxquel cas
			 * on la rajoute aux anciennes*/
			if(recrutes.size() > 0){
				if(n_e_m.containsKey(pm.getIDBat())){
					n_e_m.get(pm.getIDBat()).addAll(recrutes);
				}
				else{
					n_e_m.put(pm.getIDBat(), recrutes);
				}
			}
			/** on verifie si on ecrase pas une offre d'emplois existante auxquel cas
			 * on la rajoute aux anciennes */
			if(n_e_m.size() > 0){
				if(n_e.containsKey(pm.getIDBloc())){
					n_e.get(pm.getIDBloc()).putAll(n_e_m);
				}
				else{
					n_e.put(pm.getIDBloc(), n_e_m);
				}
			}
		}
		/** on nettoie derrier nous */
		postes.keySet().removeAll(to_remove);
		return n_e;
	}
	private TreeMap<Integer, ArrayList<String>> cleanList(TreeMap<Integer, ArrayList<String>> to_clean, int remove){
		to_clean.keySet().remove(remove);
		return to_clean;
	}
	private int selectCandidat(TreeMap<Integer, ArrayList<String>> candidats, ProfilMetier pm){
		double max = 0.0;
		int id_best = -1;
		Iterator<Integer> iter = candidats.keySet().iterator();
		while(iter.hasNext()){
			int id_pers = iter.next();
			ArrayList<String> datas = candidats.get(id_pers);
			double score = score(datas, pm);
			if(score > max){
				score = max;
				id_best = id_pers;
			}
		}
		return id_best;
	}
	private double score(ArrayList<String> datas, ProfilMetier pm){
		double score = 0.0;
		/** pour l'education c'est son niveau multiplié par le ratio d'importance */
		double score_education = (Double.parseDouble(datas.get(0))/100.0)*pm.getEducation();
		/** pour le qi c'est le meme système */
		double score_qi = (Double.parseDouble(datas.get(1))/100.0)*pm.getQI();
		/** pour le sexe c'est un bonus. En dessous de 0.5 c'est un homme, au dessus c'est une femme */
		double score_sexe = 0.0;
		if(datas.get(2).equalsIgnoreCase("homme")){
			score_sexe = 1.0-pm.getRatio();
		}
		if(datas.get(2).equalsIgnoreCase("femme")){
			score_sexe = pm.getRatio();
		}
		/** ratio faction */
		//TODO : ratio faction
		/** ratio age */
		double age_pers = Double.parseDouble(datas.get(3));
		double esperance_pers = Double.parseDouble(datas.get(4));
		double score_age = (1.0-(age_pers/esperance_pers))*(1.0-pm.getAge());
		score = (score_education+score_qi+score_sexe+score_age)/4.0;
		return score;
	}
}
