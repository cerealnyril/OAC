/** Useless pour le moment */
package managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Old_TextureManager {
	
	public Texture sol;
	
	public Old_TextureManager(){
		
	}
	private Texture loadTex(String file){
		try{
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("assets/Textures/"+file)));
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException ex){
			ex.printStackTrace();
		} return null;
	}
	public void loadTextures(){
		sol = loadTex("Terrain/pavement.png");
	}
}
