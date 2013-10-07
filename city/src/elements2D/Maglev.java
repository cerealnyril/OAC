package elements2D;

import com.badlogic.gdx.math.Vector2;

/** Un maglev c'est comme un train mais c'est vachement plus classe de dire maglev non? */
public class Maglev extends EntiteMouveable{
	
	private float tolerance, inc = 1;
	private Ligne ligne;
	private int location;
	
	public Maglev(int id, Vector2 velocite, float speed, float rotation,
			float width, float height, int id_q, Ligne ligne) {
		super(id, velocite, speed, rotation, width, height, id_q);
		this.tolerance = 3;
		this.ligne = ligne;
		this.setPosition(new Vector2(10,10));
		super.tex = TexRefs.maglevTex;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
/*	private boolean isReached(){
//		System.out.println(path.get(waypoint).x+" - "+path.get(waypoint).y);
		float on_x = path.get(waypoint).x - getX();
		float on_y = path.get(waypoint).y - getY();
		if((on_x <= this.tolerance) && (on_y <= this.tolerance)){
			return true;
		}
		return false;
	}*/

}
