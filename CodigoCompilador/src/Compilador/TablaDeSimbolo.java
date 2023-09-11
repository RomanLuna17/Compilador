package Compilador;

import java.util.Hashtable;

public class TablaDeSimbolo {

		private static Hashtable<String, Simbolo> tabla = new Hashtable<>();
		
		public static void agregarSimbolo (String lexema) {
			Simbolo nuevo = new Simbolo(lexema);
			tabla.put(lexema, nuevo);
		}
		
		public static Simbolo obtenerSimbolo (String lexema) {
			Simbolo buscado = tabla.get(lexema);
			if (buscado == null) {
				agregarSimbolo(lexema); // PREGUNTAR si es correcto
				return Constantes.SIMBOLO_NO_ENCONTRADO;
			}
			else {
				return buscado;
			}
		}
		
		public static void borrarSimbolo (String lexema) {
			tabla.remove(lexema);
		}
		
		
}
