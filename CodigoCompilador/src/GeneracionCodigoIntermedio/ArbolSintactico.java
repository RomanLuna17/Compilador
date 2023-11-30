package GeneracionCodigoIntermedio;

import java.util.ArrayList;
import java.util.Stack;

import Compilador.ParserVal;

public abstract class ArbolSintactico extends ParserVal{
	
	private ArbolSintactico izq;
    private ArbolSintactico der; 
    private String lex = "";
    
    private String aux = "@aux";
    public static int indiceAux = 0;
    public static Stack<Integer> pilaAuxs = new Stack<>();
    public static Stack<String> pilaLabels = new Stack<>();
    public static int indiceLabel = 0;
    
    public static String apilarLabel() {
    	indiceLabel++;
    	pilaLabels.push("label"+indiceLabel);
    	return "label"+indiceLabel;
    }
    
    public static String desapilarLabel() {
    	if(!pilaLabels.isEmpty()) {
    		return pilaLabels.pop();
    	}else {
    		indiceLabel = 0;
    		return "";
    	}
    }
    
    public String getAux() {
    	return aux+indiceAux;
    	
    	//return auxiliar;
    }
    
    public void aumentarVarAux() {
    	indiceAux++;
    }
    
    public void resetearIndiceAux() {
    	indiceAux = 1;
    }
    
    public ArbolSintactico(String lex){
        izq = null;
        der = null;
        this.lex  = lex;
    }
    
    public String getLex() {
    	return lex;
    }
    
    public void setLexema(String lexema) {
    	this.lex = lexema;
    }
    
    public ArbolSintactico getIzq(){
    	return this.izq;
    }
    public ArbolSintactico getDer(){
    	return this.der;
    }
    
    
    public void setIzq(ArbolSintactico izq) { 
    	this.izq = izq; 
    }
	
    public void setDer(ArbolSintactico der) { 
    	this.der = der; 
    }
    
    public abstract void recorrerArbol(String identado);
    public abstract String getAssembler();
    
   
}
