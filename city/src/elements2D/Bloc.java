package elements2D;

import tools.Identifiants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bloc extends EntiteStatic{
	
	public Bloc(int id, Vector2 position, float width, float height, int type, int id_q) {
		super(id, position, width, height, type, id_q);
		super.tex = selectTexture(type);
	}
	
	private TextureRegion selectTexture(int type){
		TextureRegion tex = AssetsLoader.vague;
		if(type == Identifiants.admininistrationBloc){
			tex = AssetsLoader.administation;
		}
		if(type == Identifiants.productionBloc){
			tex = AssetsLoader.production;
		}
		if(type == Identifiants.habitationBloc){
			tex = AssetsLoader.habitation;
		}
		if(type == Identifiants.commerceBloc){
			tex = AssetsLoader.commerce;
		}
		if(type == Identifiants.decorationBloc){
			tex = AssetsLoader.decoration;
		}
		if(type == Identifiants.loisirBloc){
			tex = AssetsLoader.loisir;
		}
		return tex;
	}
}
