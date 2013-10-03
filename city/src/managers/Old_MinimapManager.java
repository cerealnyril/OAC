package managers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


import tools.ParamsGlobals;
import topographie.Cell;


/** fait la conversion pour */
public class Old_MinimapManager {
	//private CreateImage image;
	private Map<Integer, ArrayList<Cell>> urban_cells, height_cells, rails;
	private String alphamap;
	public Old_MinimapManager(){
		alphamap = "assets/Textures/Terrain/alphamap.png";
		/** lancement de l'affichage de la minimap */
        //image = new CreateImage(alphamap);
        urban_cells = Collections.synchronizedMap(new TreeMap<Integer, ArrayList<Cell>>());
        height_cells = Collections.synchronizedMap(new TreeMap<Integer, ArrayList<Cell>>());
        rails = Collections.synchronizedMap(new TreeMap<Integer, ArrayList<Cell>>());
	}
/*----------------------GESTION FICHIER IMAGE---------------------*/
	/** renvois le chemin vers l'alphamap*/
	public String getAlphaMap(){
		return this.alphamap;
	}
/*----------------------CONVERSION VERS MINIMAP-------------------*/
	/** rajoute de nouvelles cellules 
	 * @param i */
	public void placeStructures(int i, ArrayList<Cell> filledCells) {
		urban_cells.put(i, filledCells);
	}
	public void placeHeight(int i, ArrayList<Cell> filledCells) {
		height_cells.put(i, filledCells);
	}
	public void placeRails(int i, ArrayList<Cell> filledCells){
		rails.put(i, filledCells);
	}
	
	/*-----------------------------------OPENGL------------------------------*/

	

	public void setupVboMap(){
		
/*		synchronized(urban_cells){
			Iterator<Integer> iter_s = urban_cells.keySet().iterator();
			while(iter_s.hasNext()){
				int id = iter_s.next();
				ArrayList<Cell> cel = urban_cells.get(id);
				Iterator<Cell> iter_c = cel.iterator();
				while(iter_c.hasNext()){
					Cell cell = iter_c.next();
						if (cell.isBat())ParamsGlobals.MANAGER.addBatToVboMap(cell, cell.getCellColor4f());
						if (cell.isBloc())ParamsGlobals.MANAGER.addBlocToVboMap(cell, cell.getCellColor4f());
						if (cell.isRoad())ParamsGlobals.MANAGER.addRoadToVboMap(cell, cell.getCellColor4f());
					}
				}
			}
		synchronized(rails){	
			Iterator<Integer> iter_q = rails.keySet().iterator();
			while(iter_q.hasNext()){
				int id_quartier = iter_q.next();
				ArrayList<Cell> rails_quart = rails.get(id_quartier);
				Iterator<Cell> iter_r = rails_quart.iterator();
				while(iter_r.hasNext()){
					Cell cell = iter_r.next();
					ParamsGlobals.MANAGER.addRailToVboMap(cell, new float[]{0.54f,0.27f,0.07f,0.5f});
				}
			}
		}
		synchronized(height_cells){
			Iterator<Integer> iter_s = height_cells.keySet().iterator();
			while(iter_s.hasNext()){
				int id = iter_s.next();
				ArrayList<Cell> cel = height_cells.get(id);
				Iterator<Cell> iter_c = cel.iterator();
				while(iter_c.hasNext()){
					Cell cell = iter_c.next();
					ParamsGlobals.MANAGER.addHeightToVboMap(cell, cell.getHeightCellColor4f());
				}
			}
		}
		ParamsGlobals.MANAGER.bufferVboMap();

*/
	}
}
