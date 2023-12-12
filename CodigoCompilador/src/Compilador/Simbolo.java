package Compilador;

import java.util.ArrayList;

public class Simbolo {
	
	private String lexema;
	private int id;
	private String tipo = "";
	private String uso = "";
	private String hereda = "";
	private String valor = "";
	private boolean valorAsignado = false;
	private boolean usada = false;
	
	private boolean claseAPosterior = false;
	private Simbolo parametro = null;
	
	private ArrayList<String> nombreMetodo = new ArrayList<String>(); 
	
	public void agregarNombreMetodo(String nombre) {
		nombreMetodo.add(nombre);
	}
	
	public ArrayList<String> getNombreMetodo() {
		return nombreMetodo;
	}
	
	public Simbolo(String lexema, int id) {
		super();
		this.lexema = lexema;
		this.id = id;
	}

	public Simbolo(String lexema) {
		super();
		this.lexema = lexema;
	}
	
	public boolean getClaseAPosterior() {
		return claseAPosterior;
	}
	
	public void setClasePosterior(boolean estado) {
		claseAPosterior = estado;
	}
	
	public boolean getUsada() {
		return usada;
	}
	public void setUsada(boolean us) {
		this.usada = us;
	}
	
	public boolean getValorAsignado() {
		return valorAsignado;
	}
	
	public void setValorAsignado(boolean asig) {
		this.valorAsignado = asig;
	}
	
	public String getValor()
	{
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public Simbolo getParametro() {
		return parametro;
	}

	public void setParametro(Simbolo parametro) {
		this.parametro = parametro;
	}
	
	public String getHereda() {
		return hereda;
	}

	public void setHereda(String hereda) {
		this.hereda = hereda;
	}

	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}


	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String ToString() {
		if(parametro != null) {
			String simbolo = " SIMBOLO: Lexema: [" + lexema + "] ID: [" + id + "] TIPO: [" + tipo + "] USO: [" + uso +"] HEREDA: ["+ hereda +"] PARAMETRO: [" + parametro.ToString() +"] VALOR: ["+valor+"]";
			return simbolo;
		}else {
			String simbolo = " SIMBOLO: Lexema: [" + lexema + "] ID: [" + id + "] TIPO: [" + tipo + "] USO: [" + uso +"] HEREDA: ["+ hereda +"] VALOR: ["+valor+"]";
			return simbolo;
		}
	}
	
	/*
	 * TO STRING VIEJO
	public String ToString() {
		String simbolo = " SIMBOLO: Lexema: [" + lexema + "] ID: [" + id + "]";
		return simbolo;
	}
	*/
	
	
}
