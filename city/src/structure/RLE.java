package structure;

import java.util.ArrayList;
import java.util.Iterator;

import tools.Identifiants;
import tools.ParamsGlobals;
import topographie.Cell;

/** Structures encodées selon un Run length encoding maison */
public class RLE {
	protected int tailleX, tailleY;
	protected int id_quartier;
	protected int type;
	protected ArrayList<Cell> cells;
	protected float start_x, start_y;
	protected int jour;
	
	public RLE(ArrayList<Cell> cells, int x, int y, int id_q, int jour){
		this.cells = new ArrayList<Cell>();
		this.cells.addAll(cells);
		this.jour = jour;
		if(x == 0){
			x = 1;
		}
		if(y == 0){
			y = 1;
		}
		this.tailleX = x;
		this.tailleY = y;
	}
/*-------------------------ACCESSEURS-------------------------*/	
	public int getType(){
		return this.type;
	}
	
	public int getIDQuartier(){
		return this.id_quartier;
	}
	
	public ArrayList<Cell> getCells(){
		return this.cells;
	}
	
	public float getStartX(){
		return this.start_x;
	}
	
	public float getStartY(){
		return this.start_y;
	}
	
	public int getTailleX(){
		return this.tailleX;
	}
	
	public int getTailleY(){
		return this.tailleY;
	}
	
	public float getCentreX(){
		return this.start_x+(this.tailleX/2);
	}
	
	public float getCentreY(){
		return this.start_y+(this.tailleY/2);
	}
	public int getJour(){
		return this.jour;
	}
/*---------------------------------SETTEURS------------------------------*/	
	/** fonction de la classe abstraite qui vas mettre tout les bon types et extraire les points de depars et les centres */
	protected void setCells(){
		this.start_x = 1000000000;
		this.start_y = 1000000000;
		Iterator<Cell> iter = this.cells.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
	/*		if(this.type == Identifiants.roadBloc){
				cell.resetBatType(this.type);
			}
			else{
				cell.resetUpperLayer(this.type);
			}*/
			if(start_x > cell.getX()){
				start_x = cell.getX();
			}
			if(start_y > cell.getY()){
				start_y = cell.getY();
			}
		}
		ParamsGlobals.MANAGER.updateObject(this);
	}
/*	public void releaseCells(){
		Iterator<Cell> iter = this.cells.iterator();
		while(iter.hasNext()){
			Cell cell = iter.next();
			if(this.type == Identifiants.roadBloc){
				cell.resetBatType(Identifiants.vide);
			}
			else{
				cell.resetUpperLayer(Identifiants.vide);
			}
			
		}
	}*/
}
