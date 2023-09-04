package Compilador;

import AccionesSemanticas.AccionSemantica;

public class analizadorLexico {
	
	private int lineaActual = 1;
	private AccionSemantica [][] accionesSemanticas; // Hacerlo constante
	
	
	
	public int getLineaActual() {
		return lineaActual;
	}

	public void setLineaActual(int lineaActual) {
		this.lineaActual = lineaActual;
	}
	
}