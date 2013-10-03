package elements2D;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Classe qui gère la construction d'une ligne à partir de tous les rails */
public class Line {
	Array<Rail> rails_pack;
	ObjectMap<Integer, Array<Rail>> rails;
	public Line(){
		this.rails = new ObjectMap<Integer, Array<Rail>>();
		this.rails_pack = new Array<Rail>();
	}
	
	public void updateRails(int id, Array<Rail> rails){
		this.rails.put(id, rails);
		rails_pack.addAll(rails);
	}
	
	public Array<Rail> getRails(){
		return this.rails_pack;
	}
}
