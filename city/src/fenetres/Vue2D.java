package fenetres;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

import elements2D.Ville;
import elements2D.VilleRender;

public class Vue2D  implements Screen{
	private Ville ville;
	private VilleRender renderVille;
	private Loading loading;
	
	
	public Vue2D(Ville ville) {
		this.ville = ville;
		renderVille = new VilleRender(ville);
		loading = new Loading(ville);
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
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
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		ville.dispose();
		renderVille.dispose();
	}

}
