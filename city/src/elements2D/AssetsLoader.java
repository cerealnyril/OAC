package elements2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetsLoader {
	//les atlas generaux
	private static TextureAtlas atlas_routes; 
	private static TextureAtlas atlas_canaux;
	private static TextureAtlas atlas_rails;
	private static TextureAtlas atlas_batiments;
	private static TextureAtlas atlas_blocs;
	private static TextureAtlas atlas_animate;
	
	//-- les identifications de localisation dans les atlas
	//pour les routes
	public static TextureRegion route_I, route_L, route_T, route_X;
	//- pour les rocades
	public static TextureRegion rocade_I, rocade_L, rocade_T, rocade_X;
	//- pour les rails
	public static TextureRegion rail_I, rail_C, rail_X;
	//- pour les canaux
	public static TextureRegion canal_I, canal_L, canal_T, canal_X;
	//- pour les batiments
	//d'habitation
	public static TextureRegion immeuble;
	//d'administration
	public static TextureRegion banque, commissariat, ecole, horloge, interim, morgue, pensionnat, refuge, sanitarium, tribunal, station, mairie, stase;
	//de loisirs
	public static TextureRegion restaurant;
	//de production
	public static TextureRegion centrale, ferme, usine;
	//de décoration
	public static TextureRegion fontaine, tree, statue, place, parc;
	//de commerce
	public static TextureRegion magasin;
	//autres 
	public static TextureRegion frontiereI, frontiereL;
	//- pour les blocs
	public static TextureRegion administation, production, habitation, decoration, loisir, commerce, vague;
	//- pour les objets animables
	public static TextureRegion joueur, maglev;
	
	public static void load() {
		loadRoutes();
		loadCanaux();
		loadRails();
		loadBatiments();
		loadBlocs();
		loadAnimates();
	}
	
	public static void dispose(){
		atlas_routes.dispose();
		atlas_canaux.dispose();
		atlas_rails.dispose();
	}
	
	/** Charge l'atlas des route ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadRoutes(){
		atlas_routes = new TextureAtlas(Gdx.files.internal("routes.atlas"));
		route_I = atlas_routes.findRegion("route_I");
		route_L = atlas_routes.findRegion("route_L");
		route_T = atlas_routes.findRegion("route_T");
		route_X = atlas_routes.findRegion("route_X");
		rocade_I = atlas_routes.findRegion("rocade_I");
		rocade_L = atlas_routes.findRegion("rocade_L");
		rocade_T = atlas_routes.findRegion("rocade_T");
		rocade_X = atlas_routes.findRegion("rocade_X");
	}
	
	/** Charge l'atlas des canaux ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadCanaux(){
		atlas_canaux = new TextureAtlas(Gdx.files.internal("canaux.atlas"));
		canal_I = atlas_canaux.findRegion("canal_I");
		canal_L = atlas_canaux.findRegion("canal_L");
		canal_T = atlas_canaux.findRegion("canal_T");
		canal_X = atlas_canaux.findRegion("canal_X");
		
	}
	
	/** Charge l'atlas des rails ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadRails(){
		atlas_rails = new TextureAtlas(Gdx.files.internal("rails.atlas"));
		rail_I = atlas_rails.findRegion("rail_I");
		rail_C = atlas_rails.findRegion("rail_C");
		rail_X = atlas_rails.findRegion("rail_X");
	}	
	
	/** Charge l'atlas des batiments ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadBatiments(){
		atlas_batiments = new TextureAtlas(Gdx.files.internal("batiments.atlas"));
		//d'habitation
		immeuble = atlas_batiments.findRegion("immeuble");
		//d'administration
		banque = atlas_batiments.findRegion("banque");
		commissariat = atlas_batiments.findRegion("commissariat");
		ecole = atlas_batiments.findRegion("ecole");
		horloge = atlas_batiments.findRegion("horloge");
		interim = atlas_batiments.findRegion("interim");
		morgue = atlas_batiments.findRegion("morgue");
		pensionnat = atlas_batiments.findRegion("pensionnat");
		refuge = atlas_batiments.findRegion("refuge");
		sanitarium = atlas_batiments.findRegion("sanitarium");
		tribunal = atlas_batiments.findRegion("tribunal");
		station = atlas_batiments.findRegion("station");
		mairie = atlas_batiments.findRegion("mairie");
		stase = atlas_batiments.findRegion("stase");
		//de loisirs
		restaurant = atlas_batiments.findRegion("restaurant");
		//de production
		centrale = atlas_batiments.findRegion("centrale");
		ferme = atlas_batiments.findRegion("ferme");
		usine = atlas_batiments.findRegion("usine");
		//de décoration
		fontaine = atlas_batiments.findRegion("fontaine");
		tree = atlas_batiments.findRegion("tree");
		statue = atlas_batiments.findRegion("statue");
		place = atlas_batiments.findRegion("place");
		parc = atlas_batiments.findRegion("parc");
		//de commerce
		magasin = atlas_batiments.findRegion("magasin");
		//autres
		frontiereI = atlas_batiments.findRegion("frontiereI");
		frontiereL = atlas_batiments.findRegion("frontiereL");
	}
	
	/** Charge l'atlas des blocs ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadBlocs(){
		atlas_blocs = new TextureAtlas(Gdx.files.internal("blocs.atlas"));
		administation = atlas_blocs.findRegion("administratif");
		production = atlas_blocs.findRegion("production");
		habitation = atlas_blocs.findRegion("habitation");
		decoration = atlas_blocs.findRegion("decoration");
		vague = atlas_blocs.findRegion("vague");
		loisir = atlas_blocs.findRegion("loisir"); 
		commerce = atlas_blocs.findRegion("commerce");
	}
	
	/** Charge l'atlas des objets animables ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadAnimates(){
		atlas_animate = new TextureAtlas(Gdx.files.internal("animates.atlas"));
		joueur = atlas_animate.findRegion("perso");
		maglev = atlas_animate.findRegion("maglev");
	}
}
