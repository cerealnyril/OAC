package controles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;

import com.badlogic.gdx.Input.Keys;

import tools.ParamsGlobals;
import tools.Utils;

/** Classe chargée du chargement des controles depuis le fichier de controles */
public class ControlLoad {

	private TreeMap<Integer, String> controles;
	
	public ControlLoad(){
		controles = new TreeMap<Integer, String>();
		try {
			FileReader input = new FileReader(ParamsGlobals.FIC_CONTROLES);
			BufferedReader in = new BufferedReader(input);
			String line;
			line = in.readLine();
			while(line != null){
				line = in.readLine();
				traiteLigne(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** Converti une ligne en chaine de caractère en une entrée de map */
	private void traiteLigne(String ligne){
		String[] tmp = ligne.split("-");
		for(int i = 0; i < tmp.length; i++){
			controles.put(Utils.stringToInt(tmp[0]), tmp[1]);
		}
	}
	public TreeMap<Integer, String> getControles(){
		return this.controles;
	}
}
