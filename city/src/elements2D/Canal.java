package elements2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Canal extends EntiteStatic{

	public Canal(Vector2 position, float width, float height, int type, int nb_voisins, int id_q) {
		super(0, position, width, height, type, id_q);
		super.tex = selectTexture(nb_voisins);
	}	
	private TextureRegion selectTexture(int nb){
		TextureRegion tex = AssetsLoader.canal_I;
		if(nb == 2){
			tex = AssetsLoader.canal_L;
		}
		else if(nb == 3){
			tex = AssetsLoader.canal_T;
		}
		else if(nb == 4){
			tex = AssetsLoader.canal_X;
		}
		return tex;
	}
}
