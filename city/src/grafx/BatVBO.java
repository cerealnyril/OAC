package grafx;

import java.util.ArrayList;
import managers.Old_RenderManager;

/*import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;*/
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class BatVBO {
	
	private final double PI = 3.14159265358979323846;
	private int vaoId = 0;
	private int dataVboId = 0;
	private int indexVboId = 0;
	private int[] bufferIds = new int[3];

	private ArrayList<Float> vboBatFloats = new ArrayList<Float>();
	private int indiceCount;
	private Model model;
	private Vector3f modelPos = null;
	private Vector3f modelAngle = null;
	private Vector3f modelScale = null;

	
	public BatVBO(Model m, Vector3f coord){
		this.model = m;
		this.modelPos = coord;
		modelAngle = new Vector3f(0, 0, 0);
		modelScale = new Vector3f(1, 1, 1);
		indiceCount = model.getIndex().length;
	}
	
	/**met les données en mémoire tampon*/
	public void buffer(){
/*		bufferIds = RenderManager.buffer(model.getVertex(), model.getIndex(), indiceCount);
		for (int i=0; i<model.getVertex().length; i++){
			System.out.println("Vertex : "+model.getVertex()[i]);
		}
		for (int i=0; i<model.getIndex().length; i++){
			System.out.println("Index : "+model.getIndex()[i]);
		}
		vaoId = bufferIds[0];
		indexVboId = bufferIds[2];
		this.exitOnGLError("setupBatVBO");*/
	}
	

	

	
	
	public void render(){
		Old_RenderManager.render(vaoId,indexVboId,indiceCount);
		this.exitOnGLError("renderMap");
	}	


	private void exitOnGLError(String errorMessage) {
/*		int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}*/
	}

	public void cleanup() {
/*
		
		// Select the VAO
		GL30.glBindVertexArray(vaoId);
		
		// Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		
		// Delete the vertex VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(dataVboId);
		
		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(indexVboId);
		
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
		
		this.exitOnGLError("destroyOpenGL");*/
	}


	
	private float degreesToRadians(float degrees) {
		return degrees * (float)(PI / 180d);
	}

	public void print() {
		for (int i=0; i<model.getIndex().length; i++){
			System.out.println(model.getIndex()[i]);
		}
	}
	
}
