package Compilador;

import java.util.Hashtable;
import java.util.Map;

public class TablaDeSimbolos {
		
		
		private static Hashtable<String, Simbolo> tabla = new Hashtable<>();


		
		public static Simbolo obtenerSimbolo (String lexema, int id_simbolo) {
			Simbolo buscado = tabla.get(lexema);
			if (buscado == null) {
				//agregarSimbolo(lexema); // PREGUNTAR si es correcto
				Simbolo nuevo = new Simbolo(lexema,id_simbolo);
				//System.out.println("TS24. CREE UN NUEVO SIMBOLO CON ID: " + nuevo.getId() + " SIMBOLO: " + nuevo.getLexema());
				tabla.put(lexema, nuevo);
				//id++;
				//System.out.println("AGREGO EL SIMBOLO A LA TABLA: " + lexema);
				//return Constantes.SIMBOLO_NO_ENCONTRADO;
				return nuevo;
			}
			else {
				//System.out.println("TS24. YA EXISTIA SIMBOLO CON ID: " + buscado.getId() + " SIMBOLO: " + buscado.getLexema());
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
