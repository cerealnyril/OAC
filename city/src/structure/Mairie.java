package structure;

import java.util.ArrayList;
import java.util.Iterator;

import tools.Identifiants;
import tools.Utils;
import topographie.Cell;
import topographie.Zone;


/** Mairie d'arrondissement. Gére les routes vers les autres quartiers, les limites vers le monde
 * exterieur et la politique de la ville */
public class Mairie extends Batiment{
/*-----------------------------VARIABLES-----------------------------*/
	private Zone zone_quartier;
	private ArrayList<Rocade> rocades;
	private ArrayList<Frontiere> frontieres;
	private int jour;
	private boolean haut, bas, gauche, droite;
/*-----------------------------INITIALISATION------------------------*/
	public Mairie(int id_b, int id_q) {
		super(id_b, id_q);
		setTaille();
		super.type = Identifiants.mairieBat;
		rocades = new ArrayList<Rocade>();
		frontieres = new ArrayList<Frontiere>();
		this.jour = 0;
	}
	private void setTaille(){
		super.profondeur = 2;
		super.facade = 2;
	}
	public void setCurrentZones(Zone z, Zone zone_haut, Zone zone_bas, Zone zone_gauche, Zone zone_droite){
		this.zone_quartier = z;
		zone_quartier.setStation(zone.getCell(Utils.floatToInt(zone.getCentreX()), Utils.floatToInt(zone.getCentreY())));
		if(zone_haut != null && zone_haut.getIdQuartier() != -1){
			this.haut = true;
		}
		if(zone_bas != null && zone_bas.getIdQuartier() != -1){
			this.bas = true;
		}
		if(zone_gauche != null && zone_gauche.getIdQuartier() != -1){
			this.gauche = true;
		}
		if(zone_droite != null && zone_droite.getIdQuartier() != -1){
			this.droite = true;
		}
	}
	/** dit si il y'a une zone courante */
	public boolean asZone(){
		if(this.zone_quartier != null){
			return true;
		}
		return false;
	}
	@Override
	public int getConsommationEnergie() {
		return 0;
	}
	/** supprime toutes les structures liées une fois */
	public void clearStructures(){
		//supprime toutes les limites
		frontieres.clear();
		//supprime toutes les rocades
		rocades.clear();
	}
	/** met en place les structures externes de la ville que sont les frontieres */
	public void updateFrontieres(ArrayList<Cell> non_contigue, boolean to_x){
		//test si il n'existe pas de cellules non contigue alors il n'y aura pas de frontiere
		if(non_contigue.size() > 0){	
			setFrontieres(non_contigue, to_x);
		}
	}
/*-----------------------PARTIE GESTION DES FRONTIERES DU QUARTIER------------------*/
	private void setFrontieres(ArrayList<Cell> non_contigue, boolean on_x){
		Iterator<Cell> iter = non_contigue.iterator();
		ArrayList<Cell> tmp = new ArrayList<Cell>();
		int pos_x = 0;
		int pos_y = 0;
		int to_x = 0;
		int to_y = 0;
		while(iter.hasNext()){
			Cell cell = iter.next();
			//si on garde les memes X ou les memes Y
			if((pos_x != cell.getX() && pos_y == cell.getY()) && to_x == 0){
				to_y++;
				tmp.add(cell);
			}
			else if((pos_y != cell.getY() && pos_x == cell.getX()) && to_y == 0){
				to_x++;
				tmp.add(cell);
			}
			else{
				if(tmp.size() > 0){
					boolean clos = false;
					if((on_x && (to_x > to_y)) || (!on_x && (to_x < to_y))){
						clos = true;
					}
					Frontiere frontiere = new Frontiere(tmp, to_y, to_x, this.id_quartier, this.jour, clos);
					frontieres.add(frontiere);
					tmp.clear();
				}
				pos_x = cell.getX();
				pos_y = cell.getY();
				to_x = 0;
				to_y = 0;
			}
		}
		if(tmp.size() > 0){
			boolean clos = false;
			if((on_x && (to_x > to_y)) || (!on_x && (to_x < to_y))){
				clos = true;
			}
			Frontiere frontiere = new Frontiere(tmp, to_y, to_x, this.id_quartier, this.jour, clos);
			frontieres.add(frontiere);
		}
	}
	/** Met à jour le jour pour les RLE */
	public void upJour(){
		this.jour++;
	}
	/** Retourne les RLE du quartier */
	public ArrayList<RLE> getRLEs(){
		ArrayList<RLE> result = new ArrayList<RLE>();
		result.addAll(this.frontieres);
		result.addAll(this.rocades);
		return result;
	}
/*-----------------------PARTIE GESTION DES ROCADES ENTRE QUARTIERS-----------------*/
	/** Met en place les rocades */
	public void updateRocades(ArrayList<Cell> contigues, boolean to_x){
		if(haut || bas || gauche || droite){
			Trapeze trap = setTrapeze(contigues, to_x);
			//on initialise pour les points de depart
			trap.getCornerCells(zone_quartier.getNoeudsDispos());
			Cell tmp = null, step_hg = null, step_hd = null, step_bg = null, step_bd = null;
			//calcul du point 2 qui est une cellule contigue sur le même axe
			Iterator<Cell> iter = contigues.iterator();
			while(iter.hasNext()){
				tmp = iter.next();
				if((gauche) && (step_hg == null) && (trap.getHG() != null)){
					if((tmp.getY() == trap.getHG().getY()) && (tmp.getX() < trap.getMidX_h())){
						step_hg = tmp;
					}
				}
				if((haut) && (step_hd == null) && (trap.getHD() != null)){
					if((tmp.getX() == trap.getHD().getX()) && (tmp.getY() > trap.getMidY_d())){
						step_hd = tmp;
					}
				}
				if((bas) && (step_bg == null) && (trap.getBG() != null)){
					if((tmp.getX() == trap.getBG().getX()) && (tmp.getY() < trap.getMidY_g())){
						step_bg = tmp;
					}
				}
				if((droite) && (step_bd == null) && (trap.getBD() != null)){
					if((tmp.getY() == trap.getBD().getY()) && (tmp.getX() > trap.getMidX_b())){
						step_bd = tmp;
					}
				}
			}
			//creation des rocades
			//hg = vas du coté haut gauche vers le milieu de la gauche 
			if((gauche) && (step_hg != null) && (trap.getHG() != null)){
				ArrayList<Cell> path_1 = zone_quartier.getCellLine(trap.getHG(), step_hg);
				Rocade ins1 = new Rocade(path_1, path_1.size(), 1, this.id_quartier, jour);
				rocades.add(ins1);
				Cell close = zone_quartier.getCellRelief(step_hg.getX(), Utils.floatToInt(trap.getMidY_g()));
				ArrayList<Cell> path_2 = zone_quartier.getCellLine(step_hg, close);
				Rocade ins2 = new Rocade(path_2, 1, path_2.size(), this.id_quartier, jour);
				rocades.add(ins2);
				Rocade ins3 = new Rocade(step_hg, this.id_quartier, this.jour, 1);
				rocades.add(ins3);
			}
			//hd = vas du coté haut droit vers le milieu haut 
			if((haut) && (step_hd != null) && (trap.getHD() != null)){
				ArrayList<Cell> path_1 = zone_quartier.getCellLine(trap.getHD(), step_hd);
				Rocade ins1 = new Rocade(path_1, 1, path_1.size(), this.id_quartier, jour);
				rocades.add(ins1);
				Cell close = zone_quartier.getCellRelief(Utils.floatToInt(trap.getMidX_h()), step_hd.getY());
				ArrayList<Cell> path_2 = zone_quartier.getCellLine(step_hd, close);
				Rocade ins2 = new Rocade(path_2, path_2.size(), 1, this.id_quartier, jour);
				rocades.add(ins2);
				//et on insere la rocade d'angle
				Rocade ins3 = new Rocade(step_hd, this.id_quartier, this.jour, 0);
				rocades.add(ins3);
			}
			//bg = vas du coté bas gauche vers le milieu bas
			if((bas) && (step_bg != null) && (trap.getBG() != null)){
				ArrayList<Cell> path_1 = zone_quartier.getCellLine(trap.getBG(), step_bg);
				Rocade ins1 = new Rocade(path_1, 1, path_1.size(), this.id_quartier, jour);
				rocades.add(ins1);
				Cell close = zone_quartier.getCellRelief(Utils.floatToInt(trap.getMidX_b()), step_bg.getY());
				ArrayList<Cell> path_2 = zone_quartier.getCellLine(step_bg, close);
				Rocade ins2 = new Rocade(path_2, path_2.size()+1, 1, this.id_quartier, jour);
				rocades.add(ins2);
				//et on insere la rocade d'angle
				Rocade ins3 = new Rocade(step_bg, this.id_quartier, this.jour, 2);
				rocades.add(ins3);
			}
			//bd
			if((droite) && (step_bd != null) && (trap.getBD() != null)){
				ArrayList<Cell> path_1 = zone_quartier.getCellLine(trap.getBD(), step_bd);
				Rocade ins1 = new Rocade(path_1, path_1.size(), 1, this.id_quartier, jour);
				rocades.add(ins1);
				Cell close = zone_quartier.getCellRelief(step_bd.getX(), Utils.floatToInt(trap.getMidY_d()));
				ArrayList<Cell> path_2 = zone_quartier.getCellLine(step_bd, close);
				Rocade ins2 = new Rocade(path_2, 1, path_2.size()+1, this.id_quartier, jour);
				rocades.add(ins2);
				//et on insere la rocade d'angle
				Rocade ins3 = new Rocade(step_bd, this.id_quartier, this.jour, 3);
				rocades.add(ins3);
			}
		}
	}
	/** permet d'initialiser les coins du trapèze que représente les cellules contigues */
	private Trapeze setTrapeze(ArrayList<Cell> contigues, boolean to_x){
		Trapeze trap = new Trapeze();
		//si la croissance est le long de l'axe y cela signifie que les coordonées sur x sont constantes
		if(!to_x){
			int min_x = Utils.floatToInt(zone_quartier.getCentreX() - (zone_quartier.getTailleX()/2));
			int max_x = Utils.floatToInt(zone_quartier.getCentreX() + (zone_quartier.getTailleX()/2));
			trap.setMax_x_h(max_x);
			trap.setMin_x_h(min_x);
			trap.setMax_x_b(max_x);
			trap.setMin_x_b(min_x);
			
			Cell tmp;
			Iterator<Cell> iter = contigues.iterator();
			int min_y_g = 100000000;
			int max_y_g = -100000000;
			int min_y_d = 100000000;
			int max_y_d = -100000000;
			while(iter.hasNext()){
				tmp = iter.next();
				//si on est de la bonne moitiée de X
				if((tmp.getX() > trap.getMidX_h())){
					//on choppe le min et le max de y
					if(tmp.getY() > max_y_d){
						max_y_d = tmp.getY();
					}
					if(tmp.getY() < min_y_d){
						min_y_d = tmp.getY();
					}
				}
				else{
					//on choppe le min et le max de y
					if(tmp.getY() > max_y_g){
						max_y_g = tmp.getY();
					}
					if(tmp.getY() < min_y_g){
						min_y_g = tmp.getY();
					}
				}
			}
			trap.setMax_y_d(max_y_d);
			trap.setMax_y_g(max_y_g);
			trap.setMin_y_d(min_y_d);
			trap.setMin_y_g(min_y_g);
		}
		else{
			int min_y = Utils.floatToInt(zone_quartier.getCentreY() - (zone_quartier.getTailleY()/2));
			int max_y = Utils.floatToInt(zone_quartier.getCentreY() + (zone_quartier.getTailleY()/2));
			trap.setMax_y_g(max_y);
			trap.setMin_y_g(min_y);
			trap.setMax_y_d(max_y);
			trap.setMin_y_d(min_y);
			
			Cell tmp;
			Iterator<Cell> iter = contigues.iterator();
			int min_x_h = 100000000;
			int max_x_h = -100000000;
			int min_x_b = 100000000;
			int max_x_b = -100000000;
			while(iter.hasNext()){
				tmp = iter.next();
				//si on est de la bonne moitiée de y
				if((tmp.getY() > trap.getMidY_d())){
					//on choppe le min et le max de x
					if(tmp.getX() > max_x_h){
						max_x_h = tmp.getX();
					}
					if(tmp.getX() < min_x_h){
						min_x_h = tmp.getX();
					}
				}
				else{
					//on choppe le min et le max de x
					if(tmp.getX() > max_x_b){
						max_x_b = tmp.getX();
					}
					if(tmp.getX() < min_x_b){
						min_x_b = tmp.getX();
					}
				}
			}
			trap.setMax_x_b(max_x_b);
			trap.setMin_x_b(min_x_b);
			trap.setMax_x_h(max_x_h);
			trap.setMin_x_h(min_x_h);
		}
		return trap;
		
	}
	
	class Trapeze{
		private float max_x_h, min_x_h, max_x_b, min_x_b;
		private float max_y_g, min_y_g, max_y_d, min_y_d;
		private Cell cell_hg, cell_hd, cell_bg, cell_bd;

		/*------------------------------FONCTIONS--------------------------------*/
		/** Fonction qui extrait d'une liste de cellules candidates celles qui sont les plus proches des coins */
		public void getCornerCells(ArrayList<Cell> cells){
			int best_dist_hg = 10000000, best_dist_hd = 10000000, best_dist_bg = 10000000, best_dist_bd = 10000000;
			Cell tmp;
			Iterator<Cell> iter = cells.iterator();
			while(iter.hasNext()){
				tmp = iter.next();
				//calcul de la distance haut gauche
				if(gauche){
					int dist_hg = Utils.floatToInt(Math.abs(tmp.getX()-min_x_h) + Math.abs(max_y_g-tmp.getY()));
					if(dist_hg < best_dist_hg){
						best_dist_hg = dist_hg;
						cell_hg = tmp;
					}
				}
				//calcul de la distance haut droit
				if(haut){
					int dist_hd = Utils.floatToInt(Math.abs(max_x_h-tmp.getX()) + Math.abs(max_y_d-tmp.getY()));
					if(dist_hd < best_dist_hd){
						best_dist_hd = dist_hd;
						cell_hd = tmp;
					}
				}
				//calcul de la distance bas gauche
				if(bas){
					int dist_bg = Utils.floatToInt(Math.abs(tmp.getX()-min_x_b) + Math.abs(tmp.getY()-min_y_g));
					if(dist_bg < best_dist_bg){
						best_dist_bg = dist_bg;
						cell_bg = tmp;
					}
				}
				//calcul de la distance bas droit
				if(droite){
					int dist_bd = Utils.floatToInt(Math.abs(max_x_b-tmp.getX()) + Math.abs(tmp.getY()-min_y_d));
					if(dist_bd < best_dist_bd){
						best_dist_bd = dist_bd;
						cell_bd = tmp;
					}
				}
			}
		}
/*------------------------------SETTEURS---------------------------------*/
		public Cell getHG(){
			return this.cell_hg;
		}
		public Cell getHD(){
			return this.cell_hd;
		}
		public Cell getBG(){
			return this.cell_bg;
		}
		public Cell getBD(){
			return this.cell_bd;
		}
		
		public void setMax_x_h(float max_x_h) {
			this.max_x_h = max_x_h;
		}
		
		public void setMin_x_h(float min_x_h) {
			this.min_x_h = min_x_h;
		}
		
		public void setMax_x_b(float max_x_b) {
			this.max_x_b = max_x_b;
		}
		
		public void setMin_x_b(float min_x_b) {
			this.min_x_b = min_x_b;
		}
		
		public void setMax_y_g(float max_y_g) {
			this.max_y_g = max_y_g;
		}
		
		public void setMin_y_g(float min_y_g) {
			this.min_y_g = min_y_g;
		}
		
		public void setMax_y_d(float max_y_d) {
			this.max_y_d = max_y_d;
		}
		
		public void setMin_y_d(float min_y_d) {
			this.min_y_d = min_y_d;
		}
/*---------------------------------ACCESSEURS--------------------------------*/
		public float getMax_x_h() {
			return max_x_h;
		}
		public float getMin_y_d() {
			return min_y_d;
		}
		public float getMax_y_d() {
			return max_y_d;
		}
		public float getMin_y_g() {
			return min_y_g;
		}
		public float getMax_y_g() {
			return max_y_g;
		}
		public float getMin_x_b() {
			return min_x_b;
		}
		public float getMax_x_b() {
			return max_x_b;
		}
		public float getMin_x_h() {
			return min_x_h;
		}
/*----------------------------ACCESSEURS CALCULES----------------------------*/
		/** renvois la coordonée de la moitée haute sur x*/
		public float getMidX_h(){
			return this.min_x_h+((this.max_x_h-this.min_x_h)/2);
		}
		/** renvois la coordonée de la moitée basse sur x*/
		public float getMidX_b(){
			return this.min_x_b+((this.max_x_b-this.min_x_b)/2);
		}
		/** renvois la coordonée de la moitée y à gauche */
		public float getMidY_g(){
			return this.min_y_g+((this.max_y_g-this.min_y_g)/2);
		}
		/** renvois la coordonée de la moitée y à gauche */
		public float getMidY_d(){
			return this.min_y_d+((this.max_y_d-this.min_y_d)/2);
		}
	}
}
