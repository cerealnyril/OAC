package tools;

import grafx.Model;

import java.util.logging.Level;
import managers.DBObjectManager;
import managers.MetaManager;
import managers.Old_MinimapManager;
import monde.Ville;

/**
 * Contient les variables globales
 * @Author Cereal , Syno
 */
public class ParamsGlobals {
////////////////////////////////////////////////////////////////////////////////
/////////////////////////////PARAMETRES GLOGAUX/////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public static final Level LOGLVL = Level.INFO;
////////////////////////////////////////////////////////////////////////////////
/////////////////////////////PARAMETRES GENERAUX////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public static final String VERSION = "OrdoAbChao v0.1a";
    public static final String DEFAULT_SERVER = "127.0.0.1";
    public static final int FPS = 60;
    public static final int DEFAULT_PORT_TCP = 4242;
    public static final int DEFAULT_PORT_UDP = 4242;
    public static int RES_WIDTH = 800;//1680;//640;
    public static int RES_HEIGHT = 600;//945;//480;
    public static long MAP_SEED = 0;
//mode d'affichage, 0 pour aucun, 1 pour 2D vue de dessus, 2 pour iso et 3 pour 3D. Dans l'etat actuel des choses on ne s'interesse qu'à 1 et 3
    public static int DISPLAY = 1;
	public static float DISTANCE_VIEW = 100f;
	public static float FOV = 60f;
	public static boolean LOWGRAFX = false;
	public static boolean VBO = true;
	public static boolean GLINFO = false;
////////////////////////////////////////////////////////////////////////////////
/////////////////////////////PARAMETRES MAP/////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	public static boolean MAP_RENDER_CANAL = true;
    public static boolean MAP_RENDER_ROADS = true;    
    public static boolean MAP_RENDER_BLOCS = true;
    public static boolean MAP_RENDER_RAILS = true;
    public static boolean MAP_RENDER_BATS = true;
    public static boolean MAP_RENDER_HEIGHT = true;
	public static boolean MAP_RENDER_MAP = true;
////////////////////////////////////////////////////////////////////////////////
/////////////////////////////PARAMETRES GENESIS/////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public static Ville VILLE;
    private static String base_fichier = "../city/fichiers/";
    public static String XML_COM_DATAS = base_fichier+"commerce.xml";
    public static String XML_JOB_DATAS = base_fichier+"metiers.xml";
    public static String XML_FACTIONS = base_fichier+"faction.xml";
    public static String NOMS_FILLES = base_fichier+"femme.txt";
    public static String NOMS_HOMMES = base_fichier+"homme.txt";
    public static String NOMS_QUARTIERS = base_fichier+"quartiers.txt";
    public static MetaManager MANAGER;
    public static void registerManager(MetaManager manager) {
    		MANAGER = manager;
    }
    // variables permettant l'initialisation 
    public static int NB_QUARTIERS = 8;
    public static int BASE_POP = 400;

    public static int TAILLE_PLATEAU = 129;
    public static int GAP = 40;
    public static int HEIGHT = 10;
    //pourcentage d'elevation du canal
    public static double LVL_CANAUX = 0.5;
    public static int MULT_TAILLE = 2;
////////////////////////////////////////////////////////////////////////////////
/////////////////////////////PARAMETRES TEMPORELS///////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public static int PERIODES = 3;
    public static int MINUTES = 10;
    public static int HEURES = MINUTES*PERIODES;
    public static int HEURES_PERIODES = PERIODES*PERIODES;
    public static int HEURES_JOUR = PERIODES*HEURES_PERIODES;
    public static int JOURNEE = HEURES_JOUR*HEURES;
    // paramètre de sauvegarde sur la base de donnée
    public static boolean DBSAVE = false;

}