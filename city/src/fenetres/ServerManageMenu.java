package fenetres;

import java.util.logging.Logger;

import managers.ScreenManager;

import reseau.Serveur;
import tools.ParamsGlobals;
import tools.ReseauGlobals;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import elements2D.AssetsLoader;

public class ServerManageMenu implements Screen {

	final ScreenManager jeu;
//	Serveur serveur;

	OrthographicCamera camera;
//	private static Logger logger = Logger.getLogger("serveurLog");
	
	public ServerManageMenu(ScreenManager jeu) {
		this.jeu = jeu;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
/*		serveur = new Serveur(ReseauGlobals.SERVEUR_PORT);
		try {
			logger.info("Lancement du serveur.");
			serveur.connect();
		} catch (Exception e) {
			logger.severe("Lancement du serveur impossible : "+e);
		}*/
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		jeu.batch.setProjectionMatrix(camera.combined);
		BitmapFont font = AssetsLoader.whiteFont;
		jeu.batch.begin();
		font.draw(jeu.batch, "Fenetre du Serveur ", 50, 480);
		font.draw(jeu.batch, "Clients connectés: " + ParamsGlobals.MANAGER.getServeur().registeredCount() , 600, 480);
		font.draw(jeu.batch, ParamsGlobals.MANAGER.getServeur().getClientInfos() , 15, 300);
		//affichage sequentiel des clients connect�s
		jeu.batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
