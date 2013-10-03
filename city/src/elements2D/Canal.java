package elements2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Canal extends EntiteStatic{

	private int sens;
	public Canal(Vector2 position, float width, float height, int type, int sens, int nb_voisins, int id_q) {
		super(0, position, width, height, type, id_q);
		super.tex = selectTexture(nb_voisins);
//		System.out.println("Canal avec "+nb_voisins+" voisins");
		this.sens = sens;
	}
	public int getSens(){
		return this.sens*90;
	}
	
	private Texture selectTexture(int nb){
		Texture tex = TexRefs.canal1Tex;
		if(nb == 2){
			tex = TexRefs.canal2Tex;
		}
		else if(nb == 3){
			tex = TexRefs.canal3Tex;
		}
		else if(nb == 4){
			tex = TexRefs.canal4Tex;
		}
		return tex;
	}
}
