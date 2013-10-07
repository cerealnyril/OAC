package elements2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Rail extends EntiteStatic{
		
	
	public Rail(Vector2 position, float width, float height) {
		super(0, position, width, height, -1, 0);
		super.tex = selectTexture();
	}
	private Texture selectTexture(){
		Texture res = TexRefs.rail1Tex;
		if(width > height){
			res = TexRefs.rail2Tex;
		}
		return res;
	}
}
