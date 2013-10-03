package elements2D;

import com.badlogic.gdx.math.Vector2;

/** La super classe pour les objets ayant une consistance solide mais ne se deplaceants pas. Pas exemple les batiments*/
public abstract class EntiteStatic extends Entite{

	protected int type;
	public EntiteStatic(int id, Vector2 position, float width, float height, int type, int id_q) {
		super(id, position, width, height, id_q);
		this.type = type;
	}

}
