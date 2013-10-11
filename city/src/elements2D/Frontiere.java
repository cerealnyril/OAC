package elements2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Frontiere extends EntiteStatic{

	private boolean clos;
	private TextureRegion texClos;
	private int jour;
	
	public Frontiere(Vector2 position, float width, float height, int type, int id_q, int jour, int clos) {
		super(0,position, width, height, type, id_q);
		super.tex = AssetsLoader.frontiereI;
		if(width > height){
			//super.tex = TexRefs.frontiere2Tex;
		}
		this.clos = false;
		if(clos == 1){
			this.clos = true;
		}
		this.texClos = AssetsLoader.frontiereL;
		this.jour = jour;
	}
	
	/** Permet de selectionner la bonne texture en fonction du type */
	private TextureRegion selectTexture(int genre){
		TextureRegion res = AssetsLoader.rocade_I;
		return res;
	}
	
	/** Indique si la frontiere est une frontiere fermée */
	public boolean isClos(){
		return this.clos;
	}
	
	/** Retourne le jour de création du rail */
	public int getJour(){
		return this.jour;
	}
}
