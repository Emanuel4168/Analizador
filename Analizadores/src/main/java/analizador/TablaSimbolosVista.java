package main.java.analizador;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TablaSimbolosVista extends JFrame{
	
	private JTable tablaSimbolos;
	private Vector<String> headers = new Vector<String>(Arrays.asList("Nombre","Tipo","Valor","Linea","Columna"))  ;
	private Vector<Vector<String>> data;
	public TablaSimbolosVista(ArrayList<ValoresTabla> tablaSimbolosList) {
		data = new Vector<Vector<String>>();
		fillVector(tablaSimbolosList);
		this.tablaSimbolos = new JTable(data,headers);
		this.add(new JScrollPane(tablaSimbolos),BorderLayout.CENTER);
	}
	
	private void fillVector(ArrayList<ValoresTabla> tablaSimbolosList) {
		Vector<String> row = new Vector<String>();
		for(ValoresTabla symbol : tablaSimbolosList) {
			row.add(0, symbol.nombre);
			row.add(1, symbol.tipo);
			row.add(2, symbol.valor);
			row.add(3, symbol.renglon);
			row.add(4, symbol.columna);
			data.add(row);
			row = new Vector<String>();
		}
	}

}