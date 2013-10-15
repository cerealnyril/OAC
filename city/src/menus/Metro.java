package menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import elements2D.AssetsLoader;

/** Classe qui gère le menu du metro */
public class Metro {
	
	private Table table;
	public Metro(int id_q){
		table = new Table();
//		populateTable(id_q);
	}
	
	private void populateTable(int id_q){
		
		BitmapFont white = AssetsLoader.whiteFont;
		Label.LabelStyle style = new Label.LabelStyle();
		style.font = white;
		white.scale(-0.96f);
		
		//pour le debug
		
		table.debug(); // turn on all debug lines (table, cell, and widget)
		table.debugTable();
		
		table.setFillParent(true);
		table.left().top();
		table.setSize(500, 500);
		table.setPosition(0, 0);
		table.setColor(Color.RED);

		table.add(new Label("Metro "+id_q, style));
		table.row();
		table.add(new Label("Pouet", style));
	}
	
	public Table getUI(){
		return table;
	}
	
}
