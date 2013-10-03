/** Useless pour le moment */
package managers;

import grafx.Vertex;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;


/*import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;*/
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

import tools.ParamsGlobals;

import GLSL.ShaderLoader;

public class Old_RenderManager {
	
	private final double PI = 3.14159265358979323846;
	private int vsId = 0;
	private int fsId = 0;
	private static int pId = 0;
	private int projectionMatrixLocation = 0;
	private int viewMatrixLocation = 0;
	private int modelMatrixLocation = 0;
	private Matrix4f projectionMatrix = null;
	private Matrix4f viewMatrix = null;
	private Matrix4f modelMatrix = null;
	private FloatBuffer matrix44Buffer = null;
	
	/**initialise les shaders*/
	public void setupShaders() {		
/*		//On charge le vertex shader
		vsId = ShaderLoader.loadShader("src/GLSL/MapVertex.glsl", GL20.GL_VERTEX_SHADER);
		//On charge le fragment shader
		fsId = ShaderLoader.loadShader("src/GLSL/MapFragment.glsl", GL20.GL_FRAGMENT_SHADER);
		//On créé un nouveau programme de gestion des shaders
		pId = GL20.glCreateProgram();
		//On lie nos shaders à notre programme
		GL20.glAttachShader(pId, vsId);
		GL20.glAttachShader(pId, fsId);
		//On lie la position à l'attribute 0
		GL20.glBindAttribLocation(pId, 0, "in_Position");
		//On lie la couleur à l'attribute 1
		GL20.glBindAttribLocation(pId, 1, "in_Color");
		GL20.glLinkProgram(pId);
		//On compile le programme des shaders
		//On récupère les positions uniformes des matrices
		projectionMatrixLocation = GL20.glGetUniformLocation(pId, "projectionMatrix");
		viewMatrixLocation = GL20.glGetUniformLocation(pId, "viewMatrix");
		modelMatrixLocation = GL20.glGetUniformLocation(pId, "modelMatrix");
		//On valide la programme.

		GL20.glValidateProgram(pId);		
		
		exitOnGLError("setupShaders");*/
	}

	/**met les données en mémoire tampon*/
//	public static int[] buffer(float[] vertices, int[] indices, int indiceCount){
/*		int[] bufferIds = new int[3];
		//D'abord les vertex
		FloatBuffer verticesFloatBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesFloatBuffer.put(vertices);
		verticesFloatBuffer.flip();
		//Ensuite les index
		IntBuffer indicesIntBuffer = BufferUtils.createIntBuffer(indiceCount);
		indicesIntBuffer.put(indices);
		indicesIntBuffer.flip();
		
		//On crée un Vertex Array Object et on le sélectionne
		bufferIds[0] = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(bufferIds[0]);
		
		//Une fois le VAO sélectionné, on créé un Vertex Buffer Object et on le sélectionne
		bufferIds[1] = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferIds[1]);
		//Puis en ajoute nos vertex dedans
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STREAM_DRAW);
		
		//On indique au VAO comment trouver les données de position (attribute 0)
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, 
				false, Vertex.stride, Vertex.positionByteOffset);
		//les données de couleur (attribute 1)
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, 
				false, Vertex.stride, Vertex.colorByteOffset);

		//On désélectionne le VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		//On désélectionne le VAO
		GL30.glBindVertexArray(0);
		
		//On créé un second VBO pour les index
		bufferIds[2] = GL15.glGenBuffers();
		//On le sélectionne
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, bufferIds[2]);
		//On ajoute nos index dedans
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesIntBuffer, 
				GL15.GL_STATIC_DRAW);
		//On désélectionne le VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		exitOnGLError("Buffer");
		return bufferIds;*/
//	}
	
	public static void render(int vaoId, int indexVboId, int indiceCount){
		//On appelle notre programme de shaders
/*		GL20.glUseProgram(pId);
		
		//On lie le VAO et ses attributes
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		//On lie le VBO des index qui contient les informations sur l'ordre des vertex
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVboId);
		
		//On dessine les vertex
		GL11.glDrawElements(GL11.GL_TRIANGLES, indiceCount, GL11.GL_UNSIGNED_INT, 0);
		
		//On désélectionne le VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		//Les attributes du VAO
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		//Le VAO
		GL30.glBindVertexArray(0);
		//On cesse d'utilise notre programme de shaders
		GL20.glUseProgram(0);
		exitOnGLError("render");*/
	}
	
	/**initialise les matrices*/
	public void setupMatrices() {
		//D'abord la matrice de projection (frustum)
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float)ParamsGlobals.RES_WIDTH / (float)ParamsGlobals.RES_HEIGHT;
		float near_plane = 0.1f;
		float far_plane = ParamsGlobals.DISTANCE_VIEW;
		
		float y_scale = this.coTangent(this.degreesToRadians(ParamsGlobals.FOV / 2f));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = far_plane - near_plane;
		
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
		
		//Matrice de vue
		viewMatrix = new Matrix4f();
		
		//Matrice de modèle
		modelMatrix = new Matrix4f();
		
		//On créé un FloatBuffer del a bonne taille pour stocker nos matrices plus tard
//		matrix44Buffer = BufferUtils.createFloatBuffer(16);
		exitOnGLError("setupMapMatrices");
	}
	
	/**charge les matrices dans la CG, à faire après les avoir trituré, avant de rendre*/
	public void uploadMatrices() {
/*		GL20.glUseProgram(pId);
		
		projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);
		viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);
		modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
		
		GL20.glUseProgram(0);*/
	}
	
	/**charge les matrices dans la CG, à faire après les avoir trituré, avant de rendre*/
	public void uploadViewMatrix(Matrix4f m) {
/*		GL20.glUseProgram(pId);
		
		projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);
		m.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);
		modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
		
		GL20.glUseProgram(0);*/
	}
	
	public void cleanup() {
/*		Globals.MANAGER.cleanup();
		// Delete the shaders
		GL20.glUseProgram(0);
		GL20.glDetachShader(pId, vsId);
		GL20.glDetachShader(pId, fsId);
		
		GL20.glDeleteShader(vsId);
		GL20.glDeleteShader(fsId);
		GL20.glDeleteProgram(pId);

		exitOnGLError("destroyOpenGL");
		Display.destroy();
		System.exit(0);
*/	}
	
	private float degreesToRadians(float degrees) {
		return degrees * (float)(PI / 180d);
	}
	
	private float coTangent(float angle) {
		return (float)(1f / Math.tan(angle));
	}
	
	private static void exitOnGLError(String errorMessage) {
	/*	int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}*/
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public FloatBuffer getMatrix44Buffer() {
		return matrix44Buffer;
	}
}
