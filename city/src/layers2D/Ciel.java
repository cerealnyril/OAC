package layers2D;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.Color;


public class Ciel{
	private int heure;
	private Color ambiantLight;
	private float R, G, B, alpha;
	private long ScaleMinute, ScaleHeure, ScalePeriode;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	
	public Ciel(){
		this.R = 0;
		this.G = 1;
		this.B = 0;
		this.alpha = 0.5f;
		this.ScaleMinute = -1;
		this.ScaleHeure = -1;
		this.ScalePeriode = -1;
		ambiantLight =  new Color(R, G, B, alpha);
	}

	public Color getAmbiantLight(){
//		System.out.println("Color "+ambiantLight.r+", "+ambiantLight.g+", "+ambiantLight.b);
		return this.ambiantLight;
	}
	/** Initialise les periodes pour le déroulement du temps */
	public void setTimeScales(float minutesScale, float heureScale, float periodeScale){
		boolean set = false;
		if((this.ScaleMinute == -1) || (this.ScaleHeure == -1) || (this.ScalePeriode == -1)){
			this.ScaleMinute = (long) minutesScale;
			this.ScaleHeure = (long) heureScale;
			this.ScalePeriode = (long) periodeScale;
			set = true;
		}
		if(set){
//			System.out.println("Les intervalles minutes "+ScaleMinute+" heures "+ScaleHeure+" periodes "+ScalePeriode);
			setRGB();
			internalClock();
		}
	}
	
	public void upHeure(int heure) {
		//partie initialisation si cela n'existe pas deja 
		this.heure = heure;
	}
	public void internalClock() {
		final Runnable checker = new Runnable() {
			public void run() {
				setRGB();
			}
		};
		scheduler.scheduleAtFixedRate(checker, this.ScaleMinute, this.ScaleMinute, TimeUnit.SECONDS);
	}
	/** Change les couleurs en fonction de la journée */
	private void setRGB(){
		System.out.println("heure "+heure+" < "+ScalePeriode+" ? ");
		if(heure < ScalePeriode){
			R = 0.425f;
			G = 0.44f;
			B = 0.425f;
			alpha = 1f;
		}
		else if(heure < ScalePeriode*2){
			R = 0.025f;
			G = 0.025f;
			B = 0.55f;
			alpha = 1f;
		}
		else if(heure < ScalePeriode*3){
			R = 0.275f;
			G = 0.1f;
			B = 0.1f;
			alpha = 1f;
		}
		ambiantLight.set(R, G, B, alpha);
	}
}
