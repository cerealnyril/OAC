package managers;

import tools.ParamsGlobals;

//import org.lwjgl.Sys;
//import org.lwjgl.opengl.Display;

public class Old_TimeManager {
	
	private long lastFrame = 0;
	private long lastFPS = getTime();
	private int fps = 0;
	
	public Old_TimeManager(){}

	/** Horloge en Millisecondes.
	 * Sys.getTime() -> donne le temps en 'ticks'
	 * Sys.getTimerResolution() -> donne le nombre de ticks par seconde
	 * on multiplie par 1000, on a une horloge précise à 1 ms.
	 */
	public long getTime() {
	//    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
		return 0;
		//fin modif
	}
	
	/** En faisant un appel à cette fonction à chaque frame, on obtient le temps écoulé entre 2 frames.
	 * Donc, en intégrant delta dans les formules de mouvement, on créé des mouvement in dépendants des fps.
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame  = time;
	    //System.out.println(delta);
	    	
	    return delta;
	}
	
	public void updateFPS() {
	    if (getTime() - lastFPS  > 1000) {
//	        Display.setTitle(Globals.VERSION+" - FPS : " + fps ); 
	        fps = 0;
	        lastFPS += 1000;
	    }
	    fps++;
	}
	
}
