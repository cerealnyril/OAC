package elements2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Rail extends EntiteStatic{
		
	private float angle;
	
	public Rail(Vector2 position, float width, float height, int angle, int genre, int id) {
		super(id, position, width, height, -1, 0);
		this.angle = angle;
		super.tex = selectTexture(angle, genre);
		setRotation(this.angle);
	}
	private TextureRegion selectTexture(int angle, int genre){
		TextureRegion res = null;
		if(genre == 1){
			res = AssetsLoader.rail_I;
			if(angle == 0){
				res = AssetsLoader.rail_;
			}
			this.angle = 0;
		}
		else if(genre == 2){
			res = AssetsLoader.rail_L;
			this.angle = angle*90;
		}
		else if(genre == 3){
			res = AssetsLoader.rail_Y;
			this.angle = angle*90;
		}
		//vas falloir faire le test avec le type
		return res;
	}
}
