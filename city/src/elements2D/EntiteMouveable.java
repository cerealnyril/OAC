package elements2D;

import com.badlogic.gdx.math.Vector2;

/** La classe abstraite qui gere tout ce qui est solide et se deplace */
public abstract class EntiteMouveable extends Entite{
	
	protected Vector2 velocite;
	protected float speed;

	public EntiteMouveable(int id, Vector2 velocite, float speed, float rotation, float width, float height, int id_q) {
		super(id, velocite, width, height, id_q);
		this.speed = speed;
		this.velocite = new Vector2(0, 0);
	}

	public Vector2 getVelocite() {
		return velocite;
	}
	
	public void setVelocite(Vector2 velocite) {
		this.velocite = velocite;
	}
	
	public abstract void update();
}
