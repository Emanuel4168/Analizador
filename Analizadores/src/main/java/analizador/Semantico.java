package main.java.analizador;

import java.util.ArrayList;

public class Semantico {
	private ArrayList<ValoresTabla> tablaSimbolos;
	private ArrayList<String> errors;
	private String code;
	
	public Semantico(ArrayList<ValoresTabla> tablaSimbolos, String code) {
		this.tablaSimbolos = tablaSimbolos;
		this.code = code;
		errors = new ArrayList<String>();
	}

	public ArrayList<ValoresTabla> getTablaSimbolos() {
		return tablaSimbolos;
	}

	public void setTablaSimbolos(ArrayList<ValoresTabla> tablaSimbolos) {
		this.tablaSimbolos = tablaSimbolos;
	}
	
	public ArrayList<String> checkSemantic(){
		CheckAssignments();
		return this.errors;
	}
	
	public boolean CheckAssignments() {
		for(int i = 0; i < tablaSimbolos.size(); i++ ) {
			ValoresTabla currentIdentifier = tablaSimbolos.get(i);
			if(currentIdentifier.tipo.equals("int") && this.isBooleanValue(currentIdentifier.valor)) {
				errors.add("Error semantico en linea " +currentIdentifier.renglon +", identificador "+ currentIdentifier.nombre +" Se intenta asignar un valor boolean a un entero");
			}
			if(currentIdentifier.tipo.equals("boolean") && this.isIntValue(currentIdentifier.valor)) {
				errors.add("Error semantico en linea " +currentIdentifier.renglon +", identificador "+ currentIdentifier.nombre +" Se intenta asignar un valor entero a un boolean");
			}
		}
		return true;
	}
	
	public boolean isBooleanValue(String value) {
		return value.equals("false") || value.equals("true");
	}
	
	public boolean isIntValue(String value) {
		char[] chars = value.toCharArray();

	    for (char c : chars) {
	        if(Character.isLetter(c)) {
	            return false;
	        }
	    }

	    return true;
	}

}
