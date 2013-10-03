package structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;


import profils.ProfilMetier;
import tools.Identifiants;

public class Tribunal extends Batiment{
	private int juge;
	private int procureur;
	private int avocat;
	ProfilMetier pj;
	private static double SALAIRE = 10.0;
	
	public Tribunal(int id_b, int id_q) {
		super(id_b, id_q);
		super.type = Identifiants.tribunalBat;
		this.juge = -1;
		this.procureur = -1;
		this.avocat = -1;
		this.pj = new ProfilMetier(identifiant);
		pj.loadProfil(this.type);
		setTaille();
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
/*-----------------------ACCESSEURS--------------------------*/
	/** renvois le profil metier associé aux juristes */
	public ProfilMetier getProfilMetier(){
		int total = 0;
		if(this.juge == -1){
			total++;
		}
		if(this.avocat == -1){
			total++;
		}
		if(this.procureur == -1){
			total++;
		}
		if(total > 0){
			pj.setNombrePostes(total);
		}
		return this.pj;
	}
	@Override
	public int getConsommationEnergie() {
		return 0;
	}
/*--------------------GESTION DU PERSONNEL-------------------*/
	/** rajoute un juge */
	public void addJuge(int nouveau){
		this.juge = nouveau;
	}
	/** rajoute un procureur */
	public void addProcureur(int nouveau){
		this.procureur = nouveau;
	}
	/** rajoute un avocat */
	public void addAvocat(int nouveau){
		this.avocat = nouveau;
	}
	/** recrutement du personnel de justice */
	public void recrutement(ArrayList<Integer> candidats) {
		Iterator<Integer> iter = candidats.iterator();
		while(iter.hasNext()){
			int id = iter.next();
			if(this.juge == -1){
				addJuge(id);
			}
			else if(this.procureur == -1){
				addProcureur(id);
			}
			else if(this.avocat == -1){
				addAvocat(id);
			}
		}
	}
	/** vire des gens */
	public boolean licenciement(int licencie){
		boolean fired = false;
		if(this.juge == licencie){
			this.juge = -1;
			fired = true;
		}
		else if(this.procureur == licencie){
			this.procureur = -1;
			fired = true;
		}
		else if(this.avocat == licencie){
			this.avocat = -1;
			fired = true;
		}
		return fired;
	}
	/** versement des salaires des juristes */
	public TreeMap<Integer, Double> versementSalaire() {
		TreeMap<Integer, Double> salaires = new TreeMap<Integer, Double>();
		if(this.juge != -1){
			salaires.put(this.juge, SALAIRE*2);
		}
		else if(this.procureur != -1){
			salaires.put(this.procureur, SALAIRE);
		}
		else if(this.avocat != -1){
			salaires.put(this.avocat, SALAIRE);
		}
		return salaires;
	}
/*---------------------------JUGEMENT-------------------------*/
	/** désidérata entre 0 et 1. Plus c'est proche de 0 pour la condamnation est à mort est forte
	 * et vis versa 
	 * @return: renvois 0 pour acquitement, 1 pour mort et 2 pour amende */
	public int getJugement(double desiderata){
		if(Math.random() > 0.25){
			double jugement = Math.random();
			if(jugement > desiderata){
				return 2;
			}
			else{
				return 1;
			}
		}
		return 0;
	}
}
