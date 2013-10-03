package social;

import static java.util.concurrent.TimeUnit.SECONDS;

import tools.ParamsGlobals;
import tools.SelectionNom;

public class Femme extends Personne{
	
	private boolean enceinte;
	private static int MAX_AGE = 120;
	
	public Femme(int id) {
		super(id);
		super.prenom = SelectionNom.getNomFille();
		this.enceinte = false;
	}
	@Override
	public String getSexe() {
		return "femme";
	}
	@Override
	public void setEsperanceVie(int reputation_quartier) {
		double ratio = 0.3;
		double rand = Math.random();
		double fixe = MAX_AGE*(1-ratio)+(ratio*(reputation_quartier/1000));
		double chance = MAX_AGE*ratio*rand;
		super.esperance_vie = (int) (fixe+chance);	
	}
	/** vérifie si la fille est enceinte et si c'est le cas elle acouche
	 * sinon */
	private void gestionEnceinte(){
//		System.out.println("enceinte? "+enceinte);
		/** si elle est déja enceinte elle acouche */
//		if(enceinte){
			Personne enfant = creationEnfant();
			interaction.registerHabitant(enfant);
			interaction.ajoutEnfant(enfant);
			this.enceinte = false;
//		}
//		else{
//			checkPregnant();
//		}
	}
	/** premier passage regarde si elle est enceinte. Si non on vois si elle le deviens.
	 * Au deuxieme tour si elle l'était elle acouche */
	public void checkPregnant(){
		Faction fac = interaction.getQuartier().getFaction();
		if(Math.random() > fac.getNatalite() && age > 18){
			enceinte = true;
		}
	}
	/** renvois un objet enfant */
	private Personne creationEnfant(){
		Personne enfant;
		Faction fac = interaction.getQuartier().getFaction();
		int new_id = interaction.populationMaxID()+1;
		if(Math.random() < fac.ratio_HF){
			enfant = new Femme(new_id);
			((Femme) enfant).timerStart();
		}
		else{
			enfant = new Homme(new_id);
			((Homme) enfant).timerStart();
		}
		enfant.setEsperanceVie(reputation);
		enfant.initQI(this.QI);
		enfant.setNom(this.nom);
		return enfant;
	}
/*-------------------------------HORLOGES INTERNES---------------------------------*/
	public void timerStart(){
		horlogeInterne();
	}
	private void horlogeInterne() {
		final Femme femme = this;
		final Runnable checker = new Runnable() {
			int heure = 0;
			public void run() { 
//				System.out.println("Femme "+femme.identifiant+" age "+age);
				femme.gestionMort();
				femme.gestionEtat();
				if(heure == ParamsGlobals.JOURNEE){
					femme.augmentationAge();
					femme.gestionEnceinte();
					heure = 0;
				}
				heure++;
			}
		};
		scheduler.scheduleAtFixedRate(checker, ParamsGlobals.HEURES, ParamsGlobals.HEURES, SECONDS);
//		scheduler.scheduleAtFixedRate(checker, 1, 1, SECONDS);
	}
}
