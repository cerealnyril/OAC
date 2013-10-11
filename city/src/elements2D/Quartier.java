package elements2D;

import com.badlogic.gdx.math.Vector2;

public class Quartier extends EntiteStatic{

	public Quartier(int id, Vector2 position, float width, float height) {
		super(id, position, width, height, 0, id);
		super.tex = AssetsLoader.vague;
	}
}
