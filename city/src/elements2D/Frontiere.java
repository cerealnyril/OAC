package elements2D;

import com.badlogic.gdx.math.Vector2;

public class Frontiere extends RLE{

	public Frontiere(Vector2 position, float width, float height, int type, int id_q, int jour) {
		super(position, width, height, type, id_q, jour);
		super.tex = TexRefs.frontiere1Tex;
		if(width > height){
			super.tex = TexRefs.frontiere2Tex;
		}
	}
}
