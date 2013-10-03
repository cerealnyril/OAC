package structure;

import java.util.ArrayList;

import tools.Identifiants;
import topographie.Cell;


/** la classe rail stock les objets rails pour l'ensemble de la ville. Ces objets sont encodés selon le principe du RLE etendu à la direction */
public class Rail extends RLE{

	public Rail(ArrayList<Cell> cells, int x, int y, int id_q, int jour) {
		super(cells, x, y, id_q, jour);
		super.type = Identifiants.rails;
		super.setCells();
	}

}
