package Compilador;

import java.util.ArrayList;

public class ContenedorClase {
    private String lexema;
    private ArrayList<String> listaAtributos = new ArrayList<>();
    private int contador;
	public ContenedorClase(String lexema, ArrayList<String> listaAtributos, int contador) {
		super();
		this.lexema = lexema;
		this.listaAtributos.addAll(listaAtributos);
		this.contador = contador;
	}
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public ArrayList<String> getListaAtributos() {
		return listaAtributos;
	}
	public void setListaAtributos(ArrayList<String> listaAtributos) {
		this.listaAtributos = listaAtributos;
	}
	public int getContador() {
		return contador;
	}
	public void setContador(int contador) {
		this.contador = contador;
	}
}
