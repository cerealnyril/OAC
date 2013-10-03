package elements2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Route extends EntiteStatic{

	private int sens;
	public Route(Vector2 position, float width, float height, int type, int sens, int nb_voisins, int id_q) {
		super(0, position, width, height, type, id_q);
		super.tex = selectTexture(nb_voisins);
		this.sens = sens;
	}
	
	public int getSens(){
		return this.sens*90;
	}
	
	private Texture selectTexture(int nb){
		Texture tex = TexRefs.route1Tex;
		if(nb == 2){
			tex = TexRefs.route2Tex;
		}
		else if(nb == 3){
			tex = TexRefs.route3Tex;
		}
		else if(nb == 4){
			tex = TexRefs.route4Tex;
		}
		return tex;
	}
}
