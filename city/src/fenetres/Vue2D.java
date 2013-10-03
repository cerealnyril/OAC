package fenetres;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import elements2D.Ville;
import elements2D.VilleRender;

import managers.ScreenManager;


public class Vue2D  implements Screen{
	private final ScreenManager jeu;
	private Ville ville;
	private VilleRender renderVille;
	private Loading loading;
	
	
	public Vue2D(ScreenManager jeu, Ville ville) {
		this.jeu = jeu;
		this.ville = ville;
		renderVille = new VilleRender(ville);
		loading = new Loading(ville);
	}

	@Override
	public void render(float delta) {
		if(!loading.isLoaded()){
			//on rend l'ecran de chargement
			loading.render(delta);
		}
		else{
			//a chaque coup ça met a jour le monde
			ville.update();
			//ça appel le rendu
			renderVille.render();
			//on choppe le nombre de models a charger dans la ville
		}
		
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
		dispose();
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
		ville.dispose();
	}

}