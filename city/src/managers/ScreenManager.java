package managers;

import reseau.Client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import elements2D.AssetsLoader;
import fenetres.ClientSelectMenu;
import fenetres.ServerManageMenu;

/**
 * Classe gérante de la partie graphique du client ou du serveur de l'application.
 * @author synoril
 *
 */
public class ScreenManager extends Game{
	public SpriteBatch batch;
	boolean mode;
	
	public ScreenManager(boolean mode){
		this.mode =  mode;
	}
	
	@Override
	public void create() {
		AssetsLoader.load();
		batch = new SpriteBatch();
		if(mode){
			this.setScreen(new ServerManageMenu(this));
		}
		else{
			this.setScreen(new ClientSelectMenu(this));
		}
	}

	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
		AssetsLoader.dispose();
	}
}
