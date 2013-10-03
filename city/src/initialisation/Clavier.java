package initialisation;

//------------programme Clavier-------------------

//classe fournissant des fonctions de lecture au clavier

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tools.ParamsGlobals;

public class Clavier {
	public static String lireString() {//lecture d'une chaine
		String ligne_lue=null;
		try {
			InputStreamReader lecteur=new InputStreamReader(System.in);
			BufferedReader entree=new BufferedReader(lecteur);
			ligne_lue=entree.readLine();
		}
		catch(IOException err) {
                        Logger.getLogger(Clavier.class.getName()).log(Level.SEVERE, err.toString());
			System.exit(0);
		}
		return ligne_lue;
		
	}
/**************************************/
	public static float lireFloat() {//lecture d'un float
		float x=0f; //valeur à lire
		try {
			String ligne_lue=lireString();
			x=Float.parseFloat(ligne_lue);
		}
		catch(NumberFormatException err) {
                        Logger.getLogger(Clavier.class.getName()).log(Level.SEVERE, "***Erreur de données : Réel attendu***");
			System.exit(0);
		}
		return x;
	}
/*********************************************/
	public static double lireDouble() {//lecture d'un double
		double x=0; //valeur à lire
		try {
			String ligne_lue=lireString();
			x=Double.parseDouble(ligne_lue);
		}
		catch(NumberFormatException err) {
			Logger.getLogger(Clavier.class.getName()).log(Level.SEVERE, "***Erreur de données : Double attendu***");
			System.exit(0);
		}
		return x;
	}
/*******************************************/
	public static int lireInt() {//lecture d'un double
		int x=0; //valeur à lire
		try {
			String ligne_lue=lireString();
			x=Integer.parseInt(ligne_lue);
		}
		catch(NumberFormatException err) {
			Logger.getLogger(Clavier.class.getName()).log(Level.SEVERE, "***Erreur de données : Entier attendu***");
			System.exit(0);
		}
		return x;
	}
/*******************************************/
}