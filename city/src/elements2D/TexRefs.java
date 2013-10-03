package elements2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/** Toutes les references aux textures pour qu'elles soient deja chargées dans le systeme */
public class TexRefs {
	public static Texture joueurTex = new Texture(Gdx.files.internal("graphics2D/perso.png"));//new Texture(Gdx.files.internal("graphics2D/joueur.png"));
	//toutes les textures pour les blocs
	public static Texture adminTex = new Texture(Gdx.files.internal("graphics2D/administratif.png"));
	public static Texture prodTex = new Texture(Gdx.files.internal("graphics2D/production.png"));
	public static Texture habTex = new Texture(Gdx.files.internal("graphics2D/habitation.png"));
	public static Texture comTex = new Texture(Gdx.files.internal("graphics2D/commerce.png"));
	public static Texture loisTex = new Texture(Gdx.files.internal("graphics2D/loisir.png"));
	public static Texture decoTex = new Texture(Gdx.files.internal("graphics2D/decoration.png"));
	public static Texture vagueTex = new Texture(Gdx.files.internal("graphics2D/vague.png"));
	//toutes les textures pour les elements statics affiliés au blocs
	public static Texture frontiere1Tex = new Texture(Gdx.files.internal("graphics2D/frontiere1.png"));
	public static Texture frontiere2Tex = new Texture(Gdx.files.internal("graphics2D/frontiere2.png"));
	public static Texture rail1Tex = new Texture(Gdx.files.internal("graphics2D/rail.png"));
	public static Texture rail2Tex = new Texture(Gdx.files.internal("graphics2D/rail2.png"));
	public static Texture rocade1Tex = new Texture(Gdx.files.internal("graphics2D/rocade.png"));
	public static Texture rocade2Tex = new Texture(Gdx.files.internal("graphics2D/rocade2.png"));
	//toutes les textures pour les batiments
	public static Texture batTex = new Texture(Gdx.files.internal("graphics2D/batiment.png"));
	public static Texture immeubleTex = new Texture(Gdx.files.internal("graphics2D/immeuble.png"));
	public static Texture magasinTex = new Texture(Gdx.files.internal("graphics2D/magasin.png"));
	public static Texture batAdminTex = new Texture(Gdx.files.internal("graphics2D/admin.png"));
	public static Texture usineTex = new Texture(Gdx.files.internal("graphics2D/usine.png"));
	public static Texture fermeTex = new Texture(Gdx.files.internal("graphics2D/ferme.png"));
	public static Texture centraleTex = new Texture(Gdx.files.internal("graphics2D/centrale.png"));
	public static Texture treeTex = new Texture(Gdx.files.internal("graphics2D/tree.png"));
	public static Texture parcTex = new Texture(Gdx.files.internal("graphics2D/parc.png"));
	//pour les textures associées au cellules
	public static Texture route1Tex = new Texture(Gdx.files.internal("graphics2D/route.png"));
	public static Texture route2Tex = new Texture(Gdx.files.internal("graphics2D/route2.png"));
	public static Texture route3Tex = new Texture(Gdx.files.internal("graphics2D/route3.png"));
	public static Texture route4Tex = new Texture(Gdx.files.internal("graphics2D/route4.png"));
	public static Texture canal1Tex = new Texture(Gdx.files.internal("graphics2D/canal.png"));
	public static Texture canal2Tex = new Texture(Gdx.files.internal("graphics2D/canal2.png"));
	public static Texture canal3Tex = new Texture(Gdx.files.internal("graphics2D/canal3.png"));
	public static Texture canal4Tex = new Texture(Gdx.files.internal("graphics2D/canal4.png"));
}
