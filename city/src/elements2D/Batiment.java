package elements2D;

import tools.Identifiants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Batiment extends EntiteStatic{
	private float x, y, z;
	private int l, L;
	private Sprite sprite;
	private float milieu_x, milieu_y;
	
	/** position du batiment qui prend un vecteur de position et une taille */
	public Batiment(int id, Vector2 position, float width, float height, int type, int sens, int id_q) {
		super(id, position, width, height, type, id_q);
		super.tex = selectTexture(type);
	}
	
	private Texture selectTexture(int type){
		Texture tex = TexRefs.batTex;
		//habitation
		if(type == Identifiants.immeubleBat){
			tex = TexRefs.immeubleTex;
		}
		//commerce
		else if(type == Identifiants.magasinBat){
			tex = TexRefs.magasinTex;
		}
		//production
		else if(type == Identifiants.usineBat){
			tex = TexRefs.usineTex;
		}
		else if(type == Identifiants.fermeBat){
			tex = TexRefs.fermeTex;
		}
		else if(type == Identifiants.centraleBat){
			tex = TexRefs.centraleTex;
		}
		//decoration
		else if(type == Identifiants.treeBat){
			tex = TexRefs.treeTex;
		}
		else if(type == Identifiants.parcBat){
			tex = TexRefs.parcTex;
		}
		//administratif
		else if(type == Identifiants.banqueBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.commissariatBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.ecoleBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.horlogeBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.interimBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.mairieBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.morgueBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.pensionnatBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.sanitariumBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.staseBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.tribunalBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.refugeBat){
			tex = TexRefs.batAdminTex;
		}
		else if(type == Identifiants.stationBat){
			tex = TexRefs.batAdminTex;
		}
		return tex;
	}
}
