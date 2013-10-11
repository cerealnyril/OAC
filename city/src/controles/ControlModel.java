package controles;

import java.util.TreeMap;

public class ControlModel {

	private TreeMap<Integer, Binding> id_control, touche_control;
	
	public ControlModel(){
		this.id_control = new TreeMap<Integer, Binding>();
		this.touche_control = new TreeMap<Integer, Binding>();
		load();
	}
	
/*------------------------------FONCTIONS---------------------------*/
	/** Appel le chargement initial des controles depuis le fichier de controle */
	private void load(){
		ControlLoad charge = new ControlLoad();
		
	}
	
	/** Modifie le fichier de configuration des controles avec la nouvelle configuration */
	private void save(){
		ControlSave sauvegarde = new ControlSave();
	}
	
/*------------------------------CLASSE INTERNE---------------------------*/
	/** Classe interne pour lier un controle à une touche */
	private class Binding{
		
		private int id, touche;
		private String desc;
		
		/** Constructeur d'un objet Binding qui contient 
		 * @param id : l'identifiant du controle pour le retrouver facilement
		 * @param touche : la clef de la touche associée 
		 * @param desc : la description textuelle du controle considéré pour les labels dans le menu */
		public Binding(int id, int touche, String desc){
			this.id = id;
			this.touche = touche;
			this.desc = desc;
		}
/*------------------------------GETTEURS---------------------------*/
		/** Retourne l'identifiant du control*/
		public int getId() {
			return id;
		}
		/** Retourne la touche associée au controle*/
		public int getTouche() {
			return touche;
		}
		/** Retourne la description associée au controle */
		public String getDesc() {
			return desc;
		}
/*------------------------------SETTEURS---------------------------*/		
		/** Réassigne la touche au controle */
		public void setTouche(int touche) {
			this.touche = touche;
		}
		/** Réassigne la description au controle */
		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
}
