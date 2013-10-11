package elements2D;

import tools.Identifiants;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Batiment extends EntiteStatic{
	
	/** position du batiment qui prend un vecteur de position et une taille */
	public Batiment(int id, Vector2 position, float width, float height, int type, int id_q) {
		super(id, position, width, height, type, id_q);
		super.tex = selectTexture(type);
	}
	
	private TextureRegion selectTexture(int type){
		TextureRegion tex = null;
		//habitation
		if(type == Identifiants.immeubleBat){
			tex = AssetsLoader.immeuble;
		}
		//commerce
		else if(type == Identifiants.magasinBat){
			tex = AssetsLoader.magasin;
		}
		//production
		else if(type == Identifiants.usineBat){
			tex = AssetsLoader.usine;
		}
		else if(type == Identifiants.fermeBat){
			tex = AssetsLoader.ferme;
		}
		else if(type == Identifiants.centraleBat){
			tex = AssetsLoader.centrale;
		}
		//decoration
		else if(type == Identifiants.treeBat){
			tex = AssetsLoader.tree;
		}
		else if(type == Identifiants.parcBat){
			tex = AssetsLoader.parc;
		}
		else if(type == Identifiants.statueBat){
			tex = AssetsLoader.statue;
		}
		else if(type == Identifiants.placeBat){
			tex = AssetsLoader.place;
		}
		//administratif
		else if(type == Identifiants.banqueBat){
			tex = AssetsLoader.banque;
		}
		else if(type == Identifiants.commissariatBat){
			tex = AssetsLoader.commissariat;
		}
		else if(type == Identifiants.ecoleBat){
			tex = AssetsLoader.ecole;
		}
		else if(type == Identifiants.horlogeBat){
			tex = AssetsLoader.horloge;
		}
		else if(type == Identifiants.interimBat){
			tex = AssetsLoader.interim;
		}
		else if(type == Identifiants.mairieBat){
			tex = AssetsLoader.mairie;
		}
		else if(type == Identifiants.morgueBat){
			tex = AssetsLoader.morgue;
		}
		else if(type == Identifiants.pensionnatBat){
			tex = AssetsLoader.pensionnat;
		}
		else if(type == Identifiants.sanitariumBat){
			tex = AssetsLoader.sanitarium;
		}
		else if(type == Identifiants.staseBat){
			tex = AssetsLoader.stase;
		}
		else if(type == Identifiants.tribunalBat){
			tex = AssetsLoader.tribunal;
		}
		else if(type == Identifiants.refugeBat){
			tex = AssetsLoader.refuge;
		}
		else if(type == Identifiants.stationBat){
			tex = AssetsLoader.station;
		}
		return tex;
	}
	
	public int getType(){
		return this.type;
	}
}
