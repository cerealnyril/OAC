package layers2D;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import elements2D.Batiment;
import elements2D.Ligne;
import elements2D.Maglev;
import elements2D.Rail;

/** Groupe qui contient les objets aeriens*/
public class Air extends Group{
	private Maglev maglev;
	private Ligne ligne;
	
	public Air(){
		this.ligne = new Ligne();
		this.maglev = new Maglev(-2, new Vector2(0,0), 90, 1, 1, 0, ligne);
	}
	
	public Maglev getMaglev(){
		return this.maglev;
	}
	
	public void update(){
		if(ligne.isUpdated()){
			int total = 0;
			this.clear();
			Iterator<Rail> iter = ligne.getRails().iterator();
			while(iter.hasNext()){
				total++;
				this.addActor(iter.next().getImg());
			}
			iter = ligne.getCloseLine().iterator();
			while(iter.hasNext()){
				total++;
				this.addActor(iter.next().getImg());
			}
			this.addActor(maglev.getImg());
			this.addActor(ligne.getLabels());
		}
		this.maglev.update();
	}
	/** Rajoute une nouvelle station */
	public void upStation(Batiment bat){
		this.ligne.addStation(bat);
		if(maglev.getLocation() == -1){
			maglev.setStation(bat.getID_q());
		}
	}
	
	public void upPoint(int id, float x, float y, int jour, int id_q, int next, int id_quart_next){
		ligne.update(id, x, y, jour, id_q, next, id_quart_next);
	}
}
