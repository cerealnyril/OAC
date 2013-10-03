package elements2D;

import com.badlogic.gdx.math.Vector2;

public class RLE extends EntiteStatic{

	private int sens, jour;
	
	public RLE(Vector2 position, float width, float height, int type, int id_q, int jour) {
		super(0, position, width, height, type, id_q);
		this.sens = 0;
		/*if(width > height){
			super.width = height;
			super.height = width;
			this.sens = 1;
		}*/
		this.jour = jour;
//		super.tex = TexRefs.vagueTex;
	}
	
	public int getSens(){
		return this.sens*90;
	}
	public int getJour(){
		return this.jour;
	}
}
