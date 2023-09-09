package Compilador;

import java.util.Hashtable;

public class tablaDeSimbolo {

		private static Hashtable<String, simbolo> tabla = new Hashtable<>();
		
		public static void agregarSimbolo (String lexema) {
			simbolo nuevo = new simbolo(lexema);
			tabla.put(lexema, nuevo);
		}
		
		public static simbolo obtenerSimbolo (String lexema) {
			simbolo buscado = tabla.get(lexema);
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
