package Compilador;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class TablaDeSimbolos {
		
		
		private static Hashtable<String, Simbolo> tabla = new Hashtable<>();
		
		
		public static void imprimirTabla() {
			
			
			for (String key : tabla.keySet()) {
	            Simbolo value = tabla.get(key);
	            System.out.println("Clave del hashtable: [" + key + "] ---------------> " + value.ToString());
	            if(!value.getUsada() && value.getUso().equals("identificador") && value.getLexema().contains("#global") )
	            	Constantes.varsNoUsadas.add(value.getLexema());
			}
		}
		
		public static void setTipo(String tipo, String lexema) {
			if(tabla.containsKey(lexema)) {
				tabla.get(lexema).setTipo(tipo);				
			}
		}
		
		
		// Devuelve el primer simbolo con igual lexema
	    public static Simbolo obtenerSimbolo(String lexema) {
	    	//System.out.println("lexema: " + lexema);
	        if (tabla.containsKey(lexema)) {
	        	return tabla.get(lexema);
	        }
	        if (lexema.contains("global#")) {
	        	return obtenerSimbolo(lexema.substring(0, lexema.lastIndexOf("#")));
	        }
	        return Constantes.SIMBOLO_NO_ENCONTRADO;
	    }
	    
	    public static boolean existeSimboloAmbitoActual(String lexema, String lexemaParametro) {
	    	System.out.println("LEXEMA: " + lexema + " LEXEMA PARAMETRO: " + lexemaParametro);
	    	 if(tabla.containsKey(lexema)) { 
	    		 if(tabla.get(lexema).getParametro() != null) {
	    			 if(tabla.get(lexema).getParametro().getLexema().equals(lexemaParametro)) {
	    				 return true;
	    			 }
	    		 }else {
	    			 return true;
	    		 }
	    	 }
	    	 return false;
	    }
	    
	    public static Simbolo obtenerSimboloSinAmbito(String lexema) {
	    	if(tabla.containsKey(lexema)) {
	    		return tabla.get(lexema);	
	    	}else {
	    		return Constantes.SIMBOLO_NO_ENCONTRADO;
	    	}
	    }
	    
	    public static void modificarLexema(String llaveActual, String nuevaLlave) {
	    	if(tabla.containsKey(llaveActual)) {
	    		Simbolo simbol = tabla.get(llaveActual);
	    		simbol.setLexema(nuevaLlave);
	    		tabla.remove(llaveActual);
	    		tabla.put(nuevaLlave, simbol);
	    	}
	    }
	    
	    
		
	  //A patir del nombre de la funcion, devuelve el nombre de sus parametros
	    public static Simbolo obtenerParametros(String nombreFuncion) {
	        if (tabla.containsKey(nombreFuncion)) {//Si la ultima parte del ambito es la funcion
	        	Simbolo simbolo = tabla.get(nombreFuncion);
	        	if (simbolo.getTipo().equals(Constantes.FUNCION)) {
	        		//System.out.println(" nombre parametro: "+ simbolo.getParametro().getLexema()); //Y si el uso del simbolo es parametro
	                return simbolo.getParametro();
	        	}
	        }
	        return Constantes.SIMBOLO_NO_ENCONTRADO;
	    }
	    
		public static Simbolo obtenerSimbolo (String lexema, int id_simbolo) {
			Simbolo buscado = tabla.get(lexema);
			if (buscado == null) {
				Simbolo nuevo = new Simbolo(lexema,id_simbolo);
				tabla.put(lexema, nuevo);
				return nuevo;
			}
			else {
				return buscado;
			}
		}
		
		public static void agregarSimbolo(String lexema, int id_simbolo) {
			Simbolo buscado = tabla.get(lexema);
			if (buscado == null) {
				Simbolo nuevo = new Simbolo(lexema,id_simbolo);
				tabla.put(lexema, nuevo);
			}
		}
		
	
		
		public static void borrarSimbolo (String lexema) {
			System.out.println("BORRAR: " + lexema);
			tabla.remove(lexema);
					
		}
		
		public static Hashtable<String, Simbolo>  obtenerTablaDeSimbolos() {
	        return tabla;
	    }
		
		public static Simbolo buscarPorId(int id) {
			for (Map.Entry<String, Simbolo> entrada: tabla.entrySet()) {
	            Simbolo simbolo = entrada.getValue();
	            if(simbolo.getId() == id) {
	            	return simbolo;
	            }
			}
			return Constantes.SIMBOLO_NO_ENCONTRADO;           
		}
		
		public static void vaciarTabla() {
			tabla.clear();
		}
		
}
