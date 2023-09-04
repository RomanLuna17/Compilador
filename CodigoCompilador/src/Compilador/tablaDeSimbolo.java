package Compilador;

import java.util.Hashtable;

public class tablaDeSimbolo {

		private static int identificador = 200;
		private static Hashtable<Integer, simbolo> tabla = new Hashtable<>();
		
		public static void agregarSimbolo (String lexema) {
			simbolo nuevo = new simbolo(lexema, identificador);
			tabla.put(identificador, nuevo);
			identificador++;
		}
		
		public static simbolo obtenerSimbolo (int ident) {
			simbolo buscado = tabla.get(ident);
			if (buscado == null) {
				return Constantes.SIMBOLO_NO_ENCONTRADO;
			}
			else {
				return buscado;
			}
		}
		
		public static void borrarSimbolo (int ident) {
			tabla.remove(ident);
		}
		
		
}
