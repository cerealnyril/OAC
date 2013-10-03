package social;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import tools.ParamsGlobals;
import tools.SelectionNom;

public class Homme  extends Personne{
	private static int MAX_AGE = 100;
	public Homme(int id) {
		super(id);
		super.prenom = SelectionNom.getNomGarcon();
	}

	@Override
	public void setEsperanceVie(int reputation_quartier) {
		double ratio = 0.3;
		double rand = Math.random();
		double fixe = MAX_AGE*(1-ratio)+(ratio*(reputation_quartier/1000));
		double chance = MAX_AGE*ratio*rand;
		super.esperance_vie = (int) (fixe+chance);	
	}
	@Override
	public String getSexe() {
		return "homme";
	}
	/*-------------------------------HORLOGES INTERNES---------------------------------*/
	public void timerStart(){
		horlogeInterne();
	}
	private void horlogeInterne() {
		final Homme homme = this;
		final Runnable checker = new Runnable() {
			int heure = 0;
			public void run() { 
//				System.out.println("Homme "+homme.identifiant+" age "+homme.age);
				homme.gestionMort();
				homme.gestionEtat();
				if(heure == ParamsGlobals.JOURNEE){
					augmentationAge();
					heure = 0;
				}
				heure++;
			}
		};
		scheduler.scheduleAtFixedRate(checker, ParamsGlobals.HEURES, ParamsGlobals.HEURES, SECONDS);
	}
}
