package Compilador;

public class simbolo {
	
	private String lexema;
	private int id;
	private String tipo = "";
	private String ambito = "";
	
	public simbolo(String lexema, int id) {
		super();
		this.lexema = lexema;
		this.id = id;
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

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}
	
	
}
