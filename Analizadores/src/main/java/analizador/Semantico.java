package main.java.analizador;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		checkUndeclared();
		checkDuplicity();
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
	
	public boolean checkUndeclared() {
		String[] lines = code.split("\\r?\\n"); 
		for(int i = 0; i < lines.length; i++) { 
			if(lines[i].contains(" class ")) {
				continue;
			}
			List<String> identifiers = this.getAllIdentifiers(lines[i]);
			for(int j = 0; j < identifiers.size(); j++) {
				String currentIdentifier = identifiers.get(j);
				if(!this.existIdentifier(currentIdentifier,i)) {
					errors.add("Error semantico en linea "+(i+1)+", el identificador "+ currentIdentifier + " no ha sido declarado");
				}
			}
		}
		
		return true;
	}
	
	public boolean checkDuplicity() {
			
			for(int i = 0; i < tablaSimbolos.size(); i++ ) {
				ValoresTabla currentIdentifier = tablaSimbolos.get(i);
				int identifierLine = Integer.parseInt(currentIdentifier.renglon);
				ValoresTabla original = this.searchDuplicityByName(currentIdentifier.nombre,identifierLine);
				if(original != null) {
					errors.add("Error semantico en linea "+identifierLine+", el identificador "+ currentIdentifier.nombre + " ya ha sido declarado en la linea "+original.renglon);
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
	
	public List<String> getAllIdentifiers(String input) {
	    final String regex = "[a-z]{1,40}[0-9]{1,40}";

	    final Matcher m = Pattern.compile(regex).matcher(input);

	    final List<String> matches = new ArrayList<>();
	    while (m.find()) {
	        matches.add(m.group(0));
	    }

	    return matches;
	}
	
	private boolean existIdentifier(String name, int linea) {
		for(ValoresTabla simbolo: this.tablaSimbolos) {
			if(simbolo.nombre.equals(name) && Integer.parseInt(simbolo.renglon) <= (linea + 1)) {
				return true;
			}
		}
		return false;
	}
	
	private ValoresTabla searchDuplicityByName(String name,int linea) {
		for(ValoresTabla simbolo: this.tablaSimbolos) {
			if(simbolo.nombre.equals(name) && Integer.parseInt(simbolo.renglon) < linea ) {
				/*if(this.sameScope(simbolo)) {
					return simbolo;
				}
				return null;*/
				return simbolo;
			}
		}
		return null;
	}

}
