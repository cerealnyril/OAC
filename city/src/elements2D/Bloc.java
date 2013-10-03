package elements2D;

import tools.Identifiants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Bloc extends EntiteStatic{
	
	public Bloc(int id, Vector2 position, float width, float height, int type, int id_q) {
		super(id, position, width, height, type, id_q);
		super.tex = selectTexture(type);
//		super.tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	private Texture selectTexture(int type){
		Texture tex = TexRefs.vagueTex;
		if(type == Identifiants.admininistrationBloc){
			tex = TexRefs.adminTex;
		}
		if(type == Identifiants.productionBloc){
			tex = TexRefs.prodTex;
		}
		if(type == Identifiants.habitationBloc){
			tex = TexRefs.habTex;
		}
		if(type == Identifiants.commerceBloc){
			tex = TexRefs.comTex;
		}
		if(type == Identifiants.decorationBloc){
			tex = TexRefs.decoTex;
		}
		if(type == Identifiants.loisirBloc){
			tex = TexRefs.loisTex;
		}
		return tex;
	}
}
