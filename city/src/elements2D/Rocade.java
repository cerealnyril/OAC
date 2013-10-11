package elements2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Rocade extends EntiteStatic{
	
	private int jour;
	
	public Rocade(Vector2 position, float width, float height, int type, int id_q, int jour) {
		super(0, position, width, height, type, id_q);
		int genre = 0;
		super.tex = selectTexture(genre);
		this.jour = jour;
	}
	/** Permet de selectionner la bonne texture en fonction du type */
	private TextureRegion selectTexture(int genre){
		TextureRegion res = AssetsLoader.rocade_I;
		return res;
	}
	
	/** Renvois le jour auquel a été créé la rocade */
	public int getJour(){
		return this.jour;
	}
}
