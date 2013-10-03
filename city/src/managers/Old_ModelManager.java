package managers;

import grafx.Model;
import grafx.OBJLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.Level;


public class Old_ModelManager {
	
	private TreeMap<Integer,Model> models = new TreeMap<Integer,Model>();
	
	public Old_ModelManager(){
		
	}
	
	public Model addModel(String modelFile){
		Model m=new Model();
		try {
			m = OBJLoader.loadModel(new File(modelFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.setId(models.size());
		models.put(models.size(),m);
//		Logger.getLogger(ModelManager.class).log(Level.INFO, "Model ajouté");
		return m;
	}

	public Model getModel(int type) {
		//TODO : faire ça mieux
		return models.get(0);
	}
	

	

}
