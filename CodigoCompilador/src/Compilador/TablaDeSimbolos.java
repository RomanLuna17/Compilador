package Compilador;

import java.util.Hashtable;
import java.util.Map;

public class TablaDeSimbolos {
		
		
		private static Hashtable<String, Simbolo> tabla = new Hashtable<>();
		private static int id = Constantes.PRIMER_IDENTIFICADOR;

		
		public static void agregarSimbolo (String lexema) {
			Simbolo nuevo = new Simbolo(lexema,id);
			tabla.put(lexema, nuevo);
			id++;
		}
		
		public static Simbolo obtenerSimbolo (String lexema) {
			Simbolo buscado = tabla.get(lexema);
			if (buscado == null) {
				//agregarSimbolo(lexema); // PREGUNTAR si es correcto
				Simbolo nuevo = new Simbolo(lexema,id);
				tabla.put(lexema, nuevo);
				id++;
				//System.out.println("AGREGO EL SIMBOLO A LA TABLA: " + lexema);
				//return Constantes.SIMBOLO_NO_ENCONTRADO;
				return nuevo;
			}
			else {
				return buscado;
			}
		}
		
		public static void borrarSimbolo (String lexema) {
			tabla.remove(lexema);
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
		
		
		
}
