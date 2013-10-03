package structure;

import java.util.ArrayList;

import tools.Identifiants;
import topographie.Cell;

/** Cette classe stock les limites exterieurs de la ville pour positionner des remparts et des murailles de frontieres */
public class Frontiere extends RLE{

public Frontiere(ArrayList<Cell> cells, int x, int y, int id_q, int j) {
		super(cells, x, y, id_q, j);
		super.type = Identifiants.frontiereBat;
		super.setCells();
	}

}
