package structure;

import java.util.ArrayList;

import tools.Identifiants;
import topographie.Cell;

public class Rocade extends RLE{

	public Rocade(ArrayList<Cell> cells, int x, int y, int id_q, int jour) {
		super(cells, x, y, id_q, jour);
		super.type = Identifiants.roadBloc;
		super.setCells();
	}

}
