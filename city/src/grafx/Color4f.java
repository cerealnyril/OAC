package grafx;

public class Color4f {
	private float red,green,blue,alpha;
	
	public Color4f(){
		red = 0f;
		green = 0f;
		blue = 0f;
		alpha = 1f;
	}
	
	public Color4f(float r, float g, float b, float a){
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
	
	public void setRed(float r){
		red = r;
	}
	public void setGreen(float g){
		green = g;
	}
	public void setBlue(float b){
		blue = b;
	}
	public void setAlpha(float a){
		alpha = a;
	}
	
	public float getRed(){
		return red;
	}
	public float getGreen(){
		return green;
	}
	public float getBlue(){
		return blue;
	}
	public float getAlpha(){
		return alpha;
	}
	
	public void setColor(float r, float g, float b, float a){
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
}
