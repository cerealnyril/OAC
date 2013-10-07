package elements2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Frontiere extends RLE{

	private boolean clos;
	private Texture texClos;
	
	public Frontiere(Vector2 position, float width, float height, int type, int id_q, int jour, int clos) {
		super(position, width, height, type, id_q, jour);
		super.tex = TexRefs.frontiere1Tex;
		if(width > height){
			super.tex = TexRefs.frontiere2Tex;
		}
		this.clos = false;
		if(clos == 1){
			this.clos = true;
		}
		this.texClos = TexRefs.frontiere_closTex;
	}
	public boolean isClos(){
		return this.clos;
	}
	public Texture getClosTex(){
		return this.texClos;
	}
}
