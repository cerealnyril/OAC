package elements2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Rocade extends EntiteStatic{
	
	private int jour;
	
	public Rocade(Vector2 position, float width, float height, int type, int id_q, int jour, int orientation) {
		super(0, position, width, height, type, id_q);
		super.tex = selectTexture(orientation);
		this.jour = jour;
	}
	/** Permet de selectionner la bonne texture en fonction du type */
	private TextureRegion selectTexture(int orientation){
		TextureRegion res = AssetsLoader.rocade_I;
		if(orientation == -1){
			if(width > height){
				res = AssetsLoader.rocade_;
			}
		}
		else{
			res = AssetsLoader.rocade_L;
		}
		return res;
	}
	
	/** Renvois le jour auquel a été créé la rocade */
	public int getJour(){
		return this.jour;
	}
}
