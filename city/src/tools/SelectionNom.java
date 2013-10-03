package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

/** classe de s�lection des nom que cela soit pour les quartiers ou pour les habitants */
public class SelectionNom {

	/** Vas chercher dans le fichier un nom de fille au hasard */
	public static String getNomFille(){
		String nom = "";
		ArrayList<String> proba = new ArrayList<String>();
		try {
			FileReader input = new FileReader(ParamsGlobals.NOMS_FILLES);
			BufferedReader in = new BufferedReader(input);
			String line;
			line = in.readLine();
			while(line != null){
				proba.add(line);
				line = in.readLine();
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.shuffle(proba);
		nom = proba.get(0);
		return nom;
	}
	
	/** Vas chercher dans le fichier un nom de garcon au hasard */
	public static String getNomGarcon(){
		String nom = "";
		ArrayList<String> proba = new ArrayList<String>();
		try {
			FileReader input = new FileReader(ParamsGlobals.NOMS_HOMMES);
			BufferedReader in = new BufferedReader(input);
			String line;
			line = in.readLine();
			while(line != null){
				proba.add(line);
				line = in.readLine();
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.shuffle(proba);
		nom = proba.get(0);
		return nom;
	}
	
	/** Vas chercher dans le fichier associé un nom de quartier au hasard */
	public static String getNomQuartier(int coord){
		String nom = "";
		try {
			FileReader input = new FileReader(ParamsGlobals.NOMS_QUARTIERS);
			BufferedReader in = new BufferedReader(input);
			int pos = 0;
			String line;
			line = in.readLine();
			while(line != null && pos != coord){
				line = in.readLine();
				pos++;
			}
			nom = line;
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nom;
	}
}
