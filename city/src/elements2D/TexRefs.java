package elements2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/** Toutes les references aux textures pour qu'elles soient deja chargées dans le systeme */
public class TexRefs {
	public static Texture joueurTex = new Texture(Gdx.files.internal("graphics2D/perso.png"));//new Texture(Gdx.files.internal("graphics2D/joueur.png"));
/*-----------------------------------------TEXTURES DE BLOCS--------------------------------------------*/
	public static Texture adminTex = new Texture(Gdx.files.internal("graphics2D/administratif.png"));
	public static Texture prodTex = new Texture(Gdx.files.internal("graphics2D/production.png"));
	public static Texture habTex = new Texture(Gdx.files.internal("graphics2D/habitation.png"));
	public static Texture comTex = new Texture(Gdx.files.internal("graphics2D/commerce.png"));
	public static Texture loisTex = new Texture(Gdx.files.internal("graphics2D/loisir.png"));
	public static Texture decoTex = new Texture(Gdx.files.internal("graphics2D/decoration.png"));
	public static Texture vagueTex = new Texture(Gdx.files.internal("graphics2D/vague.png"));
	//toutes les textures pour les elements statics affiliés au blocs
	public static Texture frontiere1Tex = new Texture(Gdx.files.internal("graphics2D/frontiere1.png"));
	public static Texture frontiere_closTex = new Texture(Gdx.files.internal("graphics2D/frontiere_cloture.png"));
	public static Texture frontiere2Tex = new Texture(Gdx.files.internal("graphics2D/frontiere2.png"));
	public static Texture rail1Tex = new Texture(Gdx.files.internal("graphics2D/rail.png"));
	public static Texture rail2Tex = new Texture(Gdx.files.internal("graphics2D/rail2.png"));
	public static Texture rocade1Tex = new Texture(Gdx.files.internal("graphics2D/rocade.png"));
	public static Texture rocade2Tex = new Texture(Gdx.files.internal("graphics2D/rocade2.png"));
/*-----------------------------------------TEXTURES DE BATIMENTS--------------------------------------------*/
	public static Texture batTex = new Texture(Gdx.files.internal("graphics2D/batiment.png"));
	//administratif
	public static Texture stationTex = new Texture(Gdx.files.internal("graphics2D/station.png"));
	public static Texture batAdminTex = new Texture(Gdx.files.internal("graphics2D/admin.png"));
	public static Texture horlogeTex = new Texture(Gdx.files.internal("graphics2D/horloge.png"));
	public static Texture morgueTex = new Texture(Gdx.files.internal("graphics2D/morgue.png"));
	public static Texture sanitariumTex = new Texture(Gdx.files.internal("graphics2D/sanitarium.png"));
	public static Texture banqueTex = new Texture(Gdx.files.internal("graphics2D/banque.png"));
	public static Texture interimTex = new Texture(Gdx.files.internal("graphics2D/interim.png"));
	public static Texture tribunalTex = new Texture(Gdx.files.internal("graphics2D/tribunal.png"));
	//habitation
	public static Texture immeubleTex = new Texture(Gdx.files.internal("graphics2D/immeuble.png"));
	//production
	public static Texture usineTex = new Texture(Gdx.files.internal("graphics2D/usine.png"));
	public static Texture fermeTex = new Texture(Gdx.files.internal("graphics2D/ferme.png"));
	public static Texture centraleTex = new Texture(Gdx.files.internal("graphics2D/centrale.png"));
	//decoration
	public static Texture treeTex = new Texture(Gdx.files.internal("graphics2D/tree.png"));
	public static Texture parcTex = new Texture(Gdx.files.internal("graphics2D/parc.png"));
	public static Texture statueTex = new Texture(Gdx.files.internal("graphics2D/statue.png"));
	public static Texture placeTex = new Texture(Gdx.files.internal("graphics2D/place.png"));
	//commerce
	public static Texture magasinTex = new Texture(Gdx.files.internal("graphics2D/magasin.png"));
	//loisirs
/*-----------------------------------------TEXTURES DE CELLULES--------------------------------------------*/
	public static Texture route1Tex = new Texture(Gdx.files.internal("graphics2D/route.png"));
	public static Texture route2Tex = new Texture(Gdx.files.internal("graphics2D/route2.png"));
	public static Texture route3Tex = new Texture(Gdx.files.internal("graphics2D/route3.png"));
	public static Texture route4Tex = new Texture(Gdx.files.internal("graphics2D/route4.png"));
	public static Texture canal1Tex = new Texture(Gdx.files.internal("graphics2D/canal.png"));
	public static Texture canal2Tex = new Texture(Gdx.files.internal("graphics2D/canal2.png"));
	public static Texture canal3Tex = new Texture(Gdx.files.internal("graphics2D/canal3.png"));
	public static Texture canal4Tex = new Texture(Gdx.files.internal("graphics2D/canal4.png"));
	//pour les tests
	public static Texture testTex = new Texture(Gdx.files.internal("graphics2D/test.png"));
	//pour les vehicules
	public static Texture maglevTex = new Texture(Gdx.files.internal("graphics2D/maglev.png"));
}
