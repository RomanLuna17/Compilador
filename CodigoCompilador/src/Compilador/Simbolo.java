package Compilador;

import java.lang.reflect.Array;
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
	private String direccionMetodo = "";
	private boolean esObjetoClase = false;
	private ArrayList<String> listaAQuienLeDebo = new ArrayList<>();  // A
	private ArrayList<String> listaQuienMeDebe = new ArrayList<>(); // B
	
	private boolean claseAPosterior = false;
	private Simbolo parametro = null;
	
	private ArrayList<String> variablesMetodo = new ArrayList<String>();  //atributos que estan dentro del metodo
	
	private ArrayList<String> atributosTotalesClase = new ArrayList<String>(); //lista con todos los atributos de la clase
	

	
	
	
	
	public void mostrarListaAtributos() {
		System.out.println("Lista De Simbolo "+lexema+": ");
		System.out.println(atributosTotalesClase);
	}
	
	
	
	
	public ArrayList<String> getListaAQuienLeDebo() {
		return listaAQuienLeDebo;
	}


	public void agregarAQuienLeDebo(ArrayList<String> lis ) {
		
		this.listaAQuienLeDebo.addAll(lis);
	}

	public void agregarAQuienLeDebo(String clas) {
		System.out.println("------------------------------------------------- A SIMBOLO " + lexema + " LE VOY A AGREGAR: " + clas);
		this.listaAQuienLeDebo.add(clas);
	}




	public ArrayList<String> getListaQuienMeDebe() {
		return listaQuienMeDebe;
	}




	public void agregarQuienMeDebe(String clas) {
		this.listaQuienMeDebe.add(clas);
	}

	public void agregarQuienMeDebe(ArrayList<String> lis) {
		this.listaQuienMeDebe.addAll(lis);
	}


	public ArrayList<String> getVariablesMetodo() {
		return variablesMetodo;
	}

	public void setVariablesMetodo(ArrayList<String> variablesMetodo) {
		this.variablesMetodo = variablesMetodo;
	}
	
	public void agergarListaVariablesMetodo(ArrayList<String> variablesMetodo) {
		this.variablesMetodo.addAll(variablesMetodo);
	}

	public ArrayList<String> getAtributosTotalesClase() {
		return atributosTotalesClase;
	}

	public void setAtributosTotalesClase(ArrayList<String> atributosTotalesClase) {
		this.atributosTotalesClase = atributosTotalesClase;
	}
	
	public void agregarAtributosTotalesClase(ArrayList<String> atributosTotalesClase) {
		this.atributosTotalesClase.addAll(atributosTotalesClase);
	}

	public boolean getEsObjetoClase() {
		return esObjetoClase;
	}

	public void setEsObjetoClase(boolean esObjetoClase) {
		this.esObjetoClase = esObjetoClase;
	}

	
	
	public String getDireccionMetodo() {
		return direccionMetodo;
	}
	
	public void setDireccionMetodo(String dir) {
		direccionMetodo = dir;
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
			String simbolo = " SIMBOLO: Lexema: [" + lexema + "] ID: [" + id + "] TIPO: [" + tipo + "] USO: [" + uso +"] HEREDA: ["+ hereda +"] PARAMETRO: [" + parametro.ToString() +"] VALOR: ["+valor+"] DIRECCIONMETODO: ["+direccionMetodo+"]";
			return simbolo;
		}else {
			String simbolo = " SIMBOLO: Lexema: [" + lexema + "] ID: [" + id + "] TIPO: [" + tipo + "] USO: [" + uso +"] HEREDA: ["+ hereda +"] VALOR: ["+valor+"] DIRECCIONMETODO: ["+direccionMetodo +"]";
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
