package elements2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	private static TextureAtlas atlas_evt;
	private static TextureAtlas atlas_test;
	//pour les menus
	private static TextureAtlas atlas_menu;
	
	//-- les identifications de localisation dans les atlas
	//pour les routes
	public static TextureRegion route_I, route_L, route_T, route_X;
	//- pour les rocades
	public static TextureRegion rocade_I, rocade_, rocade_L, rocade_T, rocade_X;
	//- pour les rails
	public static TextureRegion rail_I, rail_, rail_L, rail_Y;
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
	public static TextureRegion frontiereI, frontiere_;
	//- pour les blocs
	public static TextureRegion administation, production, habitation, decoration, loisir, commerce, vague;
	//- pour les objets animables
	public static TextureRegion joueur, maglev;
	//- pour les tests
	public static TextureRegion test, railItest, rail_test, railLtest, railYtest;
	
	//pour l'environement
	public static TextureRegion evt_bleu, evt_rouge, evt_gris;
	
	//les fonts
	public static BitmapFont whiteFont, blackFont, redFont;
	//les menus
	public static TextureRegion loading_cache, loading_cache_back, loading_fill, main;
	
	public static void load() {
		loadRoutes();
		loadCanaux();
		loadRails();
		loadBatiments();
		loadBlocs();
		loadAnimates();
		loadEvt();
		//pour les tests
		loadTest();
		//pour le menu
		loadMenus();
	}
	
	public static void dispose(){
		atlas_routes.dispose();
		atlas_canaux.dispose();
		atlas_rails.dispose();
		atlas_evt.dispose();
		atlas_test.dispose();
		whiteFont.dispose();
		redFont.dispose();
		atlas_menu.dispose();
	}
	/** Charge les fonts */
	private static void loadMenus(){
		//les fonts
		whiteFont = new BitmapFont(Gdx.files.internal("fonts/whitefont.fnt"), false);
		redFont = new BitmapFont(Gdx.files.internal("fonts/redfont.fnt"), false);
		//les elements visuels
		atlas_menu = new TextureAtlas(Gdx.files.internal("menus/menus.atlas"));
		loading_cache = atlas_menu.findRegion("loading_cache");
		loading_cache_back = atlas_menu.findRegion("loading_cache_back");
		loading_fill = atlas_menu.findRegion("loading_fill");
		main = atlas_menu.findRegion("main");
	}
	
	/** Charge les éléments de test graphique */
	private static void loadTest(){
		atlas_test = new TextureAtlas(Gdx.files.internal("graphics2D/test.atlas"));
		test = atlas_test.findRegion("test");
		railItest = atlas_test.findRegion("railItest");
		rail_test = atlas_test.findRegion("rail_test");
		railLtest = atlas_test.findRegion("railLtest");
		railYtest = atlas_test.findRegion("railYtest");
	}
	
	/** Charge l'atlas des environements ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadEvt(){
		atlas_evt = new TextureAtlas(Gdx.files.internal("graphics2D/environements.atlas"));
		evt_bleu = atlas_evt.findRegion("bleu");
		evt_rouge = atlas_evt.findRegion("rouge");
		evt_gris = atlas_evt.findRegion("gris");
	}
	
	/** Charge l'atlas des route ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadRoutes(){
		atlas_routes = new TextureAtlas(Gdx.files.internal("graphics2D/routes.atlas"));
		route_I = atlas_routes.findRegion("routeI");
		route_L = atlas_routes.findRegion("routeL");
		route_T = atlas_routes.findRegion("routeT");
		route_X = atlas_routes.findRegion("routeX");
		rocade_I = atlas_routes.findRegion("rocadeI");
		rocade_ = atlas_routes.findRegion("rocade_");
		rocade_L = atlas_routes.findRegion("rocadeL");
		rocade_T = atlas_routes.findRegion("rocadeT");
		rocade_X = atlas_routes.findRegion("rocadeX");
	}
	
	/** Charge l'atlas des canaux ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadCanaux(){
		atlas_canaux = new TextureAtlas(Gdx.files.internal("graphics2D/canaux.atlas"));
		canal_I = atlas_canaux.findRegion("canalI");
		canal_L = atlas_canaux.findRegion("canalL");
		canal_T = atlas_canaux.findRegion("canalT");
		canal_X = atlas_canaux.findRegion("canalX");
		
	}
	
	/** Charge l'atlas des rails ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadRails(){
		atlas_rails = new TextureAtlas(Gdx.files.internal("graphics2D/rails.atlas"));
		rail_I = atlas_rails.findRegion("railI");
		rail_ = atlas_rails.findRegion("rail_");
		rail_L = atlas_rails.findRegion("railL");
		rail_Y = atlas_rails.findRegion("railY");
	}	
	
	/** Charge l'atlas des batiments ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadBatiments(){
		atlas_batiments = new TextureAtlas(Gdx.files.internal("graphics2D/batiments.atlas"));
		//d'habitation
		immeuble = atlas_batiments.findRegion("immeuble");
		//d'administration
		banque = atlas_batiments.findRegion("banque");
		//commissariat = atlas_batiments.findRegion("commissariat");
		commissariat = atlas_batiments.findRegion("admin");
		//ecole = atlas_batiments.findRegion("ecole");
		ecole = atlas_batiments.findRegion("admin");
		horloge = atlas_batiments.findRegion("horloge");
		interim = atlas_batiments.findRegion("interim");
		morgue = atlas_batiments.findRegion("morgue");
		//pensionnat = atlas_batiments.findRegion("pensionnat");
		pensionnat = atlas_batiments.findRegion("admin");
		//refuge = atlas_batiments.findRegion("refuge");
		refuge = atlas_batiments.findRegion("admin");
		sanitarium = atlas_batiments.findRegion("sanitarium");
		tribunal = atlas_batiments.findRegion("tribunal");
		station = atlas_batiments.findRegion("station");
		//mairie = atlas_batiments.findRegion("mairie");
		mairie = atlas_batiments.findRegion("admin");
		//stase = atlas_batiments.findRegion("stase");
		stase = atlas_batiments.findRegion("admin");
		//de loisirs
		//restaurant = atlas_batiments.findRegion("restaurant");
		restaurant = atlas_batiments.findRegion("batiment");
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
		frontiere_ = atlas_batiments.findRegion("frontiere_");
	}
	
	/** Charge l'atlas des blocs ainsi que les référence à toutes les regions qui concernent ses
	 * textures */
	private static void loadBlocs(){
		atlas_blocs = new TextureAtlas(Gdx.files.internal("graphics2D/blocs.atlas"));
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
		atlas_animate = new TextureAtlas(Gdx.files.internal("graphics2D/animates.atlas"));
		joueur = atlas_animate.findRegion("perso");
		maglev = atlas_animate.findRegion("maglev");
	}
}
