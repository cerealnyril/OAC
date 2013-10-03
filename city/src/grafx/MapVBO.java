package grafx;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import managers.Old_RenderManager;

/*import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;*/
import org.lwjgl.util.glu.GLU;

import tools.ParamsGlobals;
import topographie.Cell;

public class MapVBO {
	
	private int vaoId = 0;
	private int dataVboId = 0;
	private int indexVboId = 0;



	private ArrayList<Float> vboMapFloats = new ArrayList<Float>();
	private ArrayList<Integer> mapIndices = new ArrayList<Integer>();
	private int indiceCount;
	private ArrayList<Quad> quads = new ArrayList<Quad>();
	private int mapIndex = 0;

	
	public MapVBO(){
	}
	
	/**Ajoute un quad correspondant à une cellule.*/
	public void addCellToVboMap(Cell cell, float[] color) {
		quads.add(new Quad(cell.getCentreX(),(float)cell.getCentreY(), (float)cell.getZ(),color[0],color[1],color[2]));
		mapIndices.add(mapIndex);
		mapIndex +=1;
		mapIndices.add(mapIndex);
		mapIndex +=1;
		mapIndices.add(mapIndex);
		mapIndices.add(mapIndex);
		mapIndex +=1;
		mapIndices.add(mapIndex);
		mapIndex -=3;
		mapIndices.add(mapIndex);
		mapIndex += 4;
	}
	
	/**développe les quads en Vertex*/
	public void bufferQuads(){
		//On commence par ajouter chaque vertex de chaque quad à la liste des points.
		Iterator<Quad> i = quads.iterator();
		while (i.hasNext()){
			Quad q = i.next();
			float[] f = new float[32];
			f = q.getElements();
			for (int j=0 ; j<32 ; j++){
				vboMapFloats.add(f[j]);
			}
		}
	}
	
	/**met les données en mémoire tampon*/
	public void buffer(){
		bufferQuads();
		float[] vertices = getFloats();
		for (int i=0 ; i<vertices.length ; i++){
		}
		int[] indices = getIndex();
		for (int i=0 ; i<indices.length ; i++){
		}
		indiceCount = indices.length;
/*		int [] bufferIds = RenderManager.buffer(vertices,indices,indiceCount);
		vaoId = bufferIds[0];
		dataVboId = bufferIds[1];
		indexVboId = bufferIds[2];*/
	}	
	
	public void render(){
		Old_RenderManager.render(vaoId,indexVboId,indiceCount);
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
		
		this.exitOnGLError("destroyOpenGL");
*/	}

	public void logicCycle() {
	
	}
	

	/**retourne un tableau de flot correspondant à la liste des points à rendre, au format <ppppcccctt>*/
	public float[] getFloats(){
		int count = 0;
		float[] vertex = new float[vboMapFloats.size()];
			Iterator<Float> iter = vboMapFloats.iterator();
			while(iter.hasNext()){
				float f = iter.next();
				vertex[count]=f;
				count ++;
			}
		return vertex;
	}

	/**retourne un tableau d'int correspondant à la liste des index des points à rendre, permet de ne pas rendre de points en double*/
	public int[] getIndex(){
		int count = 0;
		int[] index = new int[mapIndices.size()];
		Iterator<Integer> iter = mapIndices.iterator();
		while(iter.hasNext()){
			int i = iter.next();
			index[count]=i;
			count ++;
		}
		return index;
	}

	public void setHeight() {
		//On ajoute un peu de Z au points pour éviter le chevauchement.
		Iterator<Quad> i = quads.iterator();
		while (i.hasNext()){
			Quad q = i.next();
			q.setHeight();
		}
	}
}
