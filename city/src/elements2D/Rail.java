package elements2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Rail extends EntiteStatic{
		
	
	public Rail(Vector2 position, float width, float height) {
		super(0, position, width, height, -1, 0);
		int type = 0;
		super.tex = selectTexture(type);
//		super.createSprite();
	}
	private TextureRegion selectTexture(int type){
		TextureRegion res = AssetsLoader.rail_I;
		if(width > height){
			res = AssetsLoader.rail_I;
		}
		//vas falloir faire le test avec le type
		return res;
	}
}
