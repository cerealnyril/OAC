package grafx;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.nio.FloatBuffer;


/*import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;*/
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import tools.ParamsGlobals;

import GLSL.ShaderLoader;

public class Camera{

	private final double PI = 3.14159265358979323846;

	private Matrix4f viewMatrix = null;
	private Vector3f cameraPos = new Vector3f(0, 0, -200f);
    private float tilt = 0;
    private float pan = 0;
    private float roll = 0;
    private float speed = 0.05f;

    
    public Camera(){
    }
    
	public void update(){
//		float delta = ParamsGlobals.MANAGER.getdelta();
		//-- Update matrices
		// Reset view and model matrices
		viewMatrix = new Matrix4f();
       
        /**
         * Processes mouse input and converts it in to camera movement.
         */
/*        final float MAX_LOOK_UP = 90;
        final float MAX_LOOK_DOWN = -90;
        float mouseDX = Mouse.getDX() * speed * 0.6f;
        float mouseDY = Mouse.getDY() * speed * 0.6f;
        if (pan + mouseDX >= 360) {
            pan = pan + mouseDX - 360;
        } else if (pan + mouseDX < 0) {
            pan = 360 - pan + mouseDX;
        } else {
            pan += mouseDX;
        }
        if (tilt - mouseDY >= MAX_LOOK_DOWN
                && tilt - mouseDY <= MAX_LOOK_UP) {
            tilt += -mouseDY;
        } else if (tilt - mouseDY < MAX_LOOK_DOWN) {
            tilt = MAX_LOOK_DOWN;
        } else if (tilt - mouseDY > MAX_LOOK_UP) {
            tilt = MAX_LOOK_UP;
        }		
		
        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_Z);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_Q);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (keyUp && keyRight && !keyLeft && !keyDown) {
            moveFromLook(-delta * speed, 0, delta * speed);
        }
        if (keyUp && keyLeft && !keyRight && !keyDown) {
            moveFromLook(delta * speed, 0, delta * speed);
        }
        if (keyUp && !keyLeft && !keyRight && !keyDown) {
            moveFromLook(0, 0, delta * speed);

        }
        if (keyDown && keyLeft && !keyRight && !keyUp) {
            moveFromLook(delta * speed, 0, -delta * speed);
        }
        if (keyDown && keyRight && !keyLeft && !keyUp) {
            moveFromLook(-delta * speed, 0, -delta * speed);
        }
        if (keyDown && !keyUp && !keyLeft && !keyRight) {
            moveFromLook(0, 0, -delta * speed);
        }
        if (keyLeft && !keyRight && !keyUp && !keyDown) {
            moveFromLook(delta * speed, 0, 0);
        }
        if (keyRight && !keyLeft && !keyUp && !keyDown) {
            moveFromLook(-delta * speed, 0, 0);
        }
        if (flyUp && !flyDown) {
            cameraPos.y += delta * speed;
        }
        if (flyDown && !flyUp) {
            cameraPos.y -= delta * speed;
        }
        

		Matrix4f.rotate(degreesToRadians(tilt), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(degreesToRadians(pan), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(degreesToRadians(roll), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);	
		// Upload matrices to the uniform variables
		Globals.MANAGER.getRenderer().uploadViewMatrix(viewMatrix);

		
		this.exitOnGLError("logicCycle");
	*/}
	


	
	private float coTangent(float angle) {
		return (float)(1f / Math.tan(angle));
	}
	
	private float degreesToRadians(float degrees) {
		return degrees * (float)(PI / 180d);
	}
	
	private void exitOnGLError(String errorMessage) {
		/*int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}*/
	}
	

	
    /**
     * Move in the direction you're looking. That is, this method assumes a new coordinate system where the axis you're
     * looking down is the z-axis, the axis to your left is the x-axis, and the upward axis is the y-axis.
     *
     * @param dx the movement along the x-axis
     * @param dy the movement along the y-axis
     * @param dz the movement along the z-axis
     */
    public void moveFromLook(float dx, float dy, float dz) {
        cameraPos.z += (float) (dx * (float) cos(toRadians(pan - 90)) + dz * cos(toRadians(pan)));
        cameraPos.x -= (float) (dx * (float) sin(toRadians(pan - 90)) + dz * sin(toRadians(pan)));
        cameraPos.y += (float) (dy * (float) sin(toRadians(tilt - 90)) + dz * sin(toRadians(tilt)));
    }
    
	public void cleanup() {

	}
		
}