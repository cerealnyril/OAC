package elements2D;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class Controles implements InputProcessor{
	
	private Ville ville;
	private Joueur joueur;
	private final int KEY_UP = Keys.Z;
	private final int KEY_DOWN = Keys.S;
	private final int KEY_LEFT = Keys.Q;
	private final int KEY_RIGHT = Keys.D;
	private final int KEY_CAM_ROTATE_CLOCKWISE = Keys.E;
	private final int KEY_CAM_ROTATE_ANTECLOCKWISE = Keys.A;
	private int orientation;
	private boolean lock_up, lock_down, lock_left, lock_right;
	
	public Controles(Ville ville){
		this.ville = ville;
		this.joueur = ville.getJoueur();
		this.orientation = 0;
		this.lock_up = false;
		this.lock_down = false;
		this.lock_left = false;
		this.lock_right = false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		//Joueur
		switch(keycode){
			case KEY_UP:
				if(!lock_up){
					joueur.getVelocite().y = 1;
				}
			break;
			case KEY_DOWN:
				if(!lock_down){
					joueur.getVelocite().y = -1;
				}
			break;
			case KEY_LEFT:
				if(!lock_left){
					joueur.getVelocite().x = -1;
				}
			break;
			case KEY_RIGHT:
				if(!lock_right){
					joueur.getVelocite().x = 1;
				}
			break;
			}
		
		//Camera
		//on tourne a droite
		if(keycode == KEY_CAM_ROTATE_ANTECLOCKWISE){
			joueur.getCamera().rotate(-90, 0, 0, 1);
			if(orientation < 3){
				orientation ++;
			}
			else{
				orientation = 0;
			}
			
		}
		//on tourne a gauche
		if(keycode == KEY_CAM_ROTATE_CLOCKWISE){
			joueur.getCamera().rotate(90, 0, 0, 1);
			if(orientation > 0 ){
				orientation --;
			}
			else{
				orientation = 3;
			}			
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		//joueur
		switch(keycode){
		case KEY_UP:
			if(joueur.getVelocite().y == 1){
				joueur.getVelocite().y = 0;
			}
		break;
		case KEY_DOWN:
			if(joueur.getVelocite().y == -1){
				joueur.getVelocite().y = 0;
			}
		break;
		case KEY_LEFT:
			if(joueur.getVelocite().x == -1){
				joueur.getVelocite().x = 0;
			}
		break;
		case KEY_RIGHT:
			if(joueur.getVelocite().x == 1){
				joueur.getVelocite().x = 0;
			}
		break;
	}
		return true;
	}
/*-------------------------BLOQUEURS DE CONTROLES EN CAS DE COLLISION-----------------*/
	public void setLock_up() {
		this.lock_up = true;
	}

	public void setLock_down() {
		this.lock_down = true;
	}

	public void setLock_left() {
		this.lock_left = true;
	}

	public void setLock_right() {
		this.lock_right = true;
	}
	public void resetLocks(){
		this.lock_down = false;
		this.lock_left = false;
		this.lock_right = false;
		this.lock_up = false;
	}
	
	/** Dit si on a posé des verrous*/
	public boolean isLocked(){
		if(this.lock_down || this.lock_left || this.lock_up || this.lock_right){
			return true;
		}
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if((joueur.getCamera().zoom > 0.5 && amount < 0) || (joueur.getCamera().zoom < 50 && amount > 0)){
			joueur.getCamera().zoom += 0.1*amount;
		}
		return true;
	}

}
