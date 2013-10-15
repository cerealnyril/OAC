package com.essai.gdx;

import java.util.logging.Logger;

import managers.MetaManager;
import managers.ScreenManager;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


/**
 * Classe principale de l'application bureau.
 * @author synoril
 *
 */
public class Main {

	public static void main(String[] args) {
         
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "city";
		cfg.useGL20 = true;
		cfg.width = 1024;
		cfg.height = 512;
		boolean mode = false;
		//si c'est un serveur
		if(args.length > 0 && args[0].equals("--serveur")){
			mode = true;
			Logger logger = Logger.getLogger("serveurLog");
			//si le deuxieme argument indique que l'on ne dois pas lancer de fenetre graphique
			if(args.length > 1 && args[1].equals("--console")){
				logger.info("Mode console");
			}
			else{
				logger.info("Mode graphique");
				new LwjglApplication(new ScreenManager(mode), cfg);
			}
			//lancement du serveur
			new MetaManager();
		}
		else{
			//lancement de la fenetre client
			Logger logger = Logger.getLogger("clientLog");
			logger.info("Lancement du client");
			new LwjglApplication(new ScreenManager(mode), cfg);
		}
	}
}
