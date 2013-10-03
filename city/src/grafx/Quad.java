package grafx;

public class Quad {
	
	private float size = 0.5f;
	/**
	 *     A      D
	 * 
	 * 
	 *     B      C
	 */
	private Vertex A,B,C,D;
	
	// The amount of bytes an element has
	public static final int elementBytes = 4;
	
	// Elements per parameter
	public static final int positionElementCount = 16;
	public static final int colorElementCount = 16;
	public static final int textureElementCount = 8;
	
	// Bytes per parameter
	public static final int positionBytesCount = positionElementCount * elementBytes;
	public static final int colorByteCount = colorElementCount * elementBytes;
	public static final int textureByteCount = textureElementCount * elementBytes;
	
	// Byte offsets per parameter
	public static final int positionByteOffset = 0;
	public static final int colorByteOffset = positionByteOffset + positionBytesCount;
	public static final int textureByteOffset = colorByteOffset + colorByteCount;
	
	// The amount of elements that a quad has
	public static final int elementCount = positionElementCount + 
			colorElementCount + textureElementCount;	
	// The size of a quad in bytes
	public static final int stride = positionBytesCount + colorByteCount + 
			textureByteCount;
		
	/*public Quad (Vector3f coord, float size, Color4f color){
		A = new Vertex();
		A.setXYZ(coord.x-size,coord.x+size,coord.z);
		A.setRGBA(color.getRed(),color.getBlue(),color.getGreen(),color.getAlpha());
		A.setST(0, 0);
		B = new Vertex();
		B.setXYZ(coord.x-size,coord.x-size,coord.z);
		B.setRGBA(color.getRed(),color.getBlue(),color.getGreen(),color.getAlpha());
		B.setST(0, 1);
		C = new Vertex();
		C.setXYZ(coord.x+size,coord.x+size,coord.z);
		C.setRGBA(color.getRed(),color.getBlue(),color.getGreen(),color.getAlpha());
		C.setST(1, 1);
		D = new Vertex();
		D.setXYZ(coord.x+size,coord.x+size,coord.z);
		D.setRGBA(color.getRed(),color.getBlue(),color.getGreen(),color.getAlpha());
		D.setST(1, 0);
		this.size = size;
	}*/
	
	public Quad (float x, float y, float z, float r, float g, float b){
		A = new Vertex();
		A.setXYZ((float)(x-size),(float)(y+size),z);
		A.setRGBA(r,g,b,1f);
		A.setST(0, 0);
		B = new Vertex();
		B.setXYZ(x-size,y-size,z);
		B.setRGBA(r,g,b,1f);
		B.setST(0, 1);
		C = new Vertex();
		C.setXYZ(x+size,y-size,z);
		C.setRGBA(r,g,b,1f);
		C.setST(1, 1);
		D = new Vertex();
		D.setXYZ(x+size,y+size,z);
		D.setRGBA(r,g,b,1f);
		D.setST(1, 0);
	}
	
	public float[] getElements(){
		float[] elements = new float[32];
		int i=0;
		float[] elementsA = A.getElements();
		float[] elementsB = B.getElements();
		float[] elementsC = C.getElements();
		float[] elementsD = D.getElements();
		elements[i] = elementsA[0];
		i++;
		elements[i] = elementsA[1];
		i++;
		elements[i] = elementsA[2];
		i++;
		elements[i] = elementsA[3];
		i++;
		elements[i] = elementsA[4];
		i++;
		elements[i] = elementsA[5];
		i++;
		elements[i] = elementsA[6];
		i++;
		elements[i] = elementsA[7];
		i++;
		//elements[i] = elementsA[8];
		//i++;
		//elements[i] = elementsA[9];
		//i++;
		elements[i] = elementsB[0];
		i++;
		elements[i] = elementsB[1];
		i++;
		elements[i] = elementsB[2];
		i++;
		elements[i] = elementsB[3];
		i++;
		elements[i] = elementsB[4];
		i++;
		elements[i] = elementsB[5];
		i++;
		elements[i] = elementsB[6];
		i++;
		elements[i] = elementsB[7];
		i++;
		//elements[i] = elementsB[8];
		//i++;
		//elements[i] = elementsB[9];
		//i++;
		elements[i] = elementsC[0];
		i++;
		elements[i] = elementsC[1];
		i++;
		elements[i] = elementsC[2];
		i++;
		elements[i] = elementsC[3];
		i++;
		elements[i] = elementsC[4];
		i++;
		elements[i] = elementsC[5];
		i++;
		elements[i] = elementsC[6];
		i++;
		elements[i] = elementsC[7];
		i++;
		//elements[i] = elementsC[8];
		//i++;
		//elements[i] = elementsC[9];
		//i++;
		elements[i] = elementsD[0];
		i++;
		elements[i] = elementsD[1];
		i++;
		elements[i] = elementsD[2];
		i++;
		elements[i] = elementsD[3];
		i++;
		elements[i] = elementsD[4];
		i++;
		elements[i] = elementsD[5];
		i++;
		elements[i] = elementsD[6];
		i++;
		elements[i] = elementsD[7];
		//i++;
		//elements[i] = elementsD[8];
		//i++;
		//elements[i] = elementsD[9];
		return elements;
	}
	
	public void setHeight(){
		A.setHeight();
		B.setHeight();
		C.setHeight();
		D.setHeight();
	}
}