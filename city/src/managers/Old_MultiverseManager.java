/** Useless pour le moment */
package managers;

import grafx.BatVBO;
import grafx.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.util.vector.Vector3f;


import structure.Batiment;
import structure.Bloc;
import structure.Quartier;
import tools.ParamsGlobals;
import topographie.Cell;

public class Old_MultiverseManager {
	
	private Map<Integer, Quartier> quartiers = Collections.synchronizedMap(new TreeMap<Integer, Quartier>());
	private Map<Integer, Bloc> blocs = Collections.synchronizedMap(new TreeMap<Integer, Bloc>());
	private Map<Integer, Batiment> batiments = Collections.synchronizedMap(new TreeMap<Integer, Batiment>());
	private ArrayList<BatVBO> batVbo = new ArrayList<BatVBO>();

	
	public Old_MultiverseManager(){
		
	}


	public void updateQuartier(int id, Quartier quartier) {
		synchronized(quartiers){
			if (quartiers.containsKey(id)) return;
			else quartiers.put(id, quartier);
		}
	}


	public void updateBloc(int id, Bloc bloc) {
		synchronized(blocs){
			if (blocs.containsKey(id)) return;
			else blocs.put(id, bloc);
		}		
	}


	public void updateBat(int id, Batiment batiment) {
		synchronized(batiments){
			if (batiments.containsKey(id)) return;
			else batiments.put(id, batiment);
		}		
	}


	public void setup() {
		synchronized(batiments){
			Iterator<Integer> iter = batiments.keySet().iterator();
			while(iter.hasNext()){	
				int i = iter.next();
				Batiment b = batiments.get(i);
				Vector3f coord = new Vector3f (b.getCentreX(),(float)b.getCells().get(0).getZ(),b.getCentreY());
				//Vector3f coord = new Vector3f (0,0,0);
//				Model m = ParamsGlobals.MANAGER.getModel(b.getType());
//				batVbo.add(new BatVBO(m,coord));
			}	
		}	
	}

	public void buffer(){
		/*Iterator<BatVBO> iter = batVbo.iterator();
		while(iter.hasNext()){
			BatVBO b = iter.next();
			b.buffer();
			//b.print();
		}	*/
		batVbo.get(0).buffer();
	}
	
	public void render() {
		/*Iterator<BatVBO> iter = batVbo.iterator();
		while(iter.hasNext()){
			BatVBO b = iter.next();
			b.render();
		}*/
		batVbo.get(0).render();
	}
	
	public void cleanup(){
		Iterator<BatVBO> iter = batVbo.iterator();
		while(iter.hasNext()){
			BatVBO b = iter.next();
			b.cleanup();
		}
	}
}

