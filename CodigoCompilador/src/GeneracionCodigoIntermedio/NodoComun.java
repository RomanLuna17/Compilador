package GeneracionCodigoIntermedio;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.GeneradorAssembler;
import Compilador.TablaDeSimbolos;

public class NodoComun extends ArbolSintactico {
	
	private String valorAssembler = "";
	
	public NodoComun(String lex,ArbolSintactico izq,ArbolSintactico der) {
		super(lex);
		setDer(der);
		setIzq(izq);
		valorAssembler = (lex); //seteo el valor del nodo
	}
	
	@Override
	public String getValorAssembler() {
		// TODO Auto-generated method stub
		return valorAssembler;
	}
	
	@Override
	public void recorrerArbol(String s) {
		System.out.print(s+"Lexama Nodo: " + super.getLex() + "\n");
        if (!(super.getIzq() == null)){
            System.out.print(s+"Hijo Izquierdo: " + "\n");
        	super.getIzq().recorrerArbol(s+"    ");
        }
        if (!(super.getDer() == null)){
            System.out.print(s+"Hijo Derecho: "+ "\n");
            super.getDer().recorrerArbol(s+"    ");
        }
	}
	
	public void setHijoIzq(ArbolSintactico arbl) {
		setIzq(arbl);
	}
	
	public void setHijoDer(ArbolSintactico arbl) {
		setDer(arbl);
	}
	
	public ArbolSintactico getHijoIzq(){
		return super.getIzq();
	}
	
	public ArbolSintactico getHijoDer() {
		return super.getDer();
	}
	
	
	
	public String getAssembler() {
		//System.out.println("Entre aca");
        String salida = "";

        String hijoIzq = "";
    	String hijoDer = "";
    	boolean menosmenosIzq = false;
    	boolean menosmenosDer = false;
    	
        switch(super.getLex()){
       
            //Asignacion
        	case "Sentencia":
        		//System.out.println("ENTRE EN EL CASE DE SENTENCIA");
        		if(getDer() != null) {
        			salida += getIzq().getAssembler() + getDer().getAssembler();
        		}else {
        			salida += getIzq().getAssembler();
        		}
        		break;
            case "=":
            	//System.out.println("ENTRE EN EL CASE DEL IGUAL");
                salida += getDer().getAssembler() +getIzq().getAssembler();
                hijoIzq = getIzq().getValorAssembler();
                hijoDer = getDer().getValorAssembler();
                if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG")){
                	if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
                	salida+= "MOV EAX , " + hijoDer + "\n"; 
                    salida+= "MOV " + hijoIzq + ", EAX"+ "\n";
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT")) {
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		salida+= "MOV AX , " + hijoDer + "\n"; 
                    salida+= "MOV " + hijoIzq + ", AX"+ "\n";
            	}else {
            		
            		if(hijoDer.equals("TOF")) {
            			//hijoDer = getDer().getIzq().getValorAssembler();
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			if(!hijoIzq.contains("@")) {
                			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
                		}
            			salida += "FSTP " + hijoIzq + " \n";
            		}else {
            		
	            		if(!hijoIzq.contains("@")) {
	            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
	            		}
	            		if(!hijoDer.contains("@")) {	
	            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
	            		}
	            		salida += "FLD " + hijoDer + "\n";
	                    salida += "FSTP " + hijoIzq + "\n";
            		}
                }
                
                
                
                
                break;
            case "+":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//pregunto el simbolo existe, le quito el primer $
            	
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	
            	//pregunto si el simbolo existe, le quito el primer $ que le agregue cuando cree el nodo
            	//System.out.println("SUMA: ARBIZQ: "+ hijoIzq + " ARBDER: " + hijoDer+"######################################");
            	
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF") ) {
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV EAX , "+hijoIzq+ "\n";
            		salida+= "ADD EAX , " + hijoDer+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		//la operacion tiene un menos menos
            		if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
            		
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT")&& !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")){
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , "+hijoIzq+ "\n";
            		salida+= "ADD AX , " +hijoDer+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		//la operacion tiene un menos menos
            		if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
                	
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")){
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		
            		if(hijoIzq.equals("$TOF") && !hijoDer.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FADD " + hijoDer + " \n";
            		}else if(hijoDer.equals("$TOF") && !hijoIzq.equals("$TOF")) {
            			//hijoDer = getDer().getIzq().getValorAssembler();
            			salida += "FLD "+hijoIzq+" \n";
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FADD \n";
            		}else if(hijoDer.equals("$TOF") && hijoIzq.equals("$TOF")) {
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FADD \n";
            		}else {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FADD " +hijoDer+ "\n";
            		}
            		//comparar con constante mas grande de float y menor
            		ArbolSintactico.indiceAux++;
            		salida+= "FSTP @aux" +ArbolSintactico.indiceAux +"\n";
            		  
            		
            		//verifico overflow
            		salida += "FLD @aux"+ArbolSintactico.indiceAux +" \n";
            		salida += "FLD $constMaximoFloat \n";
            		salida += "FCOM \n";
            		salida += "FSTSW AX \n";
            		salida += "SAHF \n";
            		//salida += "JNLE errorOverflowSumaFlotantes \n";
            		
            		/*
            		salida += "FNSTSW AX \n";   
            		salida += "SAHF \n";       
            		salida += "TEST AH, 1 \n";         
            		salida += "JNZ errorOverflowSumaFlotantes \n";
            		*/
            		
            		//salida += "JO errorOverflowSumaFlotantes \n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
            	}
            	
            	break;
            case "-":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//pregunto el simbolo existe, le quito el primer $
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	
            	//para menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV EAX , "+hijoIzq+ "\n";
            		salida+= "SUB EAX , " +hijoDer+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
            		
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")){
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , "+hijoIzq+ "\n";
            		salida+= "SUB AX , " +hijoDer+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")){
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(hijoIzq.equals("$TOF") && !hijoDer.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSUB " + hijoDer + " \n";
            		}else if(hijoDer.equals("$TOF") && !hijoIzq.equals("$TOF")) {
            			//hijoDer = getDer().getIzq().getValorAssembler();
            			salida += "FLD "+hijoIzq+" \n";
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FSUB \n";
            		}else if(hijoDer.equals("$TOF") && hijoIzq.equals("$TOF")) {
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FSUB \n";
            		}else {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FSUB " +hijoDer+ "\n";
            		}
            		
            		
            		//FALTA LOS NUMEROS FLOTANTES
            		//salida+= "FLD " +hijoIzq+ "\n";
            		//salida+= "FSUB " +hijoDer+ "\n";
            		ArbolSintactico.indiceAux++;
            		
            		salida+= "FSTP @aux" +ArbolSintactico.indiceAux +"\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
            	}
            	break;
            case "*":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//pregunto el simbolo existe, le quito el primer $
            	//System.out.println("LO QUE BUSCO PARA IZQ: " + getIzq().getValorAssembler());
            	//System.out.println("LO QUE BUSCO PARA DER: " + getDer().getValorAssembler());
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	//para menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV EAX , "+hijoIzq+ "\n";
            		salida+= "IMUL EAX , " + hijoDer+ "\n";
            		//verifico si no hubo overflow, en caso de haber salto a la etiqueta
            		salida += "JO errorOverflowMultEntero \n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		//la operacion tiene un menos menos
            		if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")){
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , "+hijoIzq+ "\n";
            		//salida+= "MUL AX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MOV BX , " +hijoDer+ "\n";
            		salida+= "MUL BX \n";
            		//verifico si no hubo overflow, en caso de haber salto a la etiqueta
            		salida += "JO errorOverflowMultEntero \n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		//la operacion tiene un menos menos
            		if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")){
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(hijoIzq.equals("$TOF") && !hijoDer.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FMUL " + hijoDer + " \n";
            		}else if(hijoDer.equals("$TOF") && !hijoIzq.equals("$TOF")) {
            			//hijoDer = getDer().getIzq().getValorAssembler();
            			salida += "FLD "+hijoIzq+" \n";
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FMUL \n";
            		}else if(hijoDer.equals("$TOF") && hijoIzq.equals("$TOF")) {
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FMUL \n";
            		}else {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FMUL " +hijoDer+ "\n";
            		}
            		
            		
            		//FALTA LOS NUMEROS FLOTANTES
            		ArbolSintactico.indiceAux++;
            		//salida+= "FLD " + hijoIzq+ "\n";
            		//salida+= "FMUL " + hijoDer+ "\n";
            		salida+= "FSTP @aux" +ArbolSintactico.indiceAux +"\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
            	}
            	break;
            case "/":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//pregunto el simbolo existe, le quito el primer $
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	//para menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		//System.out.println("ES LONG");
            		ArbolSintactico.indiceAux++;
            		salida += "CMP " + hijoDer + ", 0 \n";
                    salida += "JE errorDivisionPorCero\n";
                    
                    salida += "MOV EAX, " + hijoIzq +" \n";
                    salida += "CDQ \n"; // extiendo el signo de EAX a EAX y EDX
                    salida += "MOV EBX, "+hijoDer +" \n";
                    salida += "IDIV EBX \n"; // divide el registro EAX:EDX por EBX
                    
                
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
            		//seteo el valor del nodo padre igual al resultado de la operacion entre los hijos
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		//la operacion tiene un menos menos
            		if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")){
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		//System.out.println("ES UINT");
            		ArbolSintactico.indiceAux++;
            		//tema particular, division por cero
            		salida += "CMP " + hijoDer + ", 0 \n";
                    salida += "JE errorDivisionPorCero\n";
                    salida+= "MOV AX , "+hijoIzq+ "\n";
            		salida+="XOR DX,DX \n";
            		salida+="MOV BX , "+hijoDer+" \n";
                    salida+= "DIV BX \n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
            		//seteo el valor del nodo padre igual al resultado de la operacion entre los hijos
            		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
            		
            		//la operacion tiene un menos menos
            		if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
            	}else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")){
            		if(!hijoIzq.contains("@")) {
            			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		if(!hijoDer.contains("@")) {	
            			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
            		}
            		
            		if(hijoIzq.equals("$TOF") && !hijoDer.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			salida += "FLD "+ hijoDer +" \n";
               		 	salida += "FTST \n";
               		 	salida += "FSTSW AX \n"; //Almacena el estado de la palabra de estado de la FPU en AX
               		 	salida += "SAHF \n";     //Transfiere los flags de la FPU al registro de flags del procesador
               		 	salida += "JE errorDivisionPorCero \n";
               		 	salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
         			 	salida += "FDIV "+hijoDer+"\n";
            		}else if(hijoDer.equals("$TOF") && !hijoIzq.equals("$TOF")) {
            			//hijoDer = getDer().getIzq().getValorAssembler();
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FTST \n";
               		 	salida += "FSTSW AX \n"; //Almacena el estado de la palabra de estado de la FPU en AX
               		 	salida += "SAHF \n";     //Transfiere los flags de la FPU al registro de flags del procesador
               		 	salida += "JE errorDivisionPorCero \n";
               		 	salida += "FLD "+hijoIzq+" \n";
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FDIV \n";
            		}else if(hijoDer.equals("$TOF") && hijoIzq.equals("$TOF")) {
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FTST \n";
               		 	salida += "FSTSW AX \n"; //Almacena el estado de la palabra de estado de la FPU en AX
               		 	salida += "SAHF \n";     //Transfiere los flags de la FPU al registro de flags del procesador
               		 	salida += "JE errorDivisionPorCero \n";
               		 	salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
            			salida += "FDIV \n";
            		}else {
            			salida += "FLD "+ hijoDer +" \n";
               		 	salida += "FTST \n";
               		 	salida += "FSTSW AX \n"; //Almacena el estado de la palabra de estado de la FPU en AX
               		 	salida += "SAHF \n";     //Transfiere los flags de la FPU al registro de flags del procesador
               		 	salida += "JE errorDivisionPorCero \n";
                        salida += "FLD " +hijoIzq+" \n";
               		 	salida += "FDIV "+hijoDer+"\n";
               		}
            		//System.out.println("ES FLOAT");
            		//verifico que el divisor no sea 0
              		 ArbolSintactico.indiceAux++;
            		 //System.out.println("EL primer valor de la pila: " + hijoIzq + " El segundo valor: "+ hijoDer);
              		 salida+= "FSTP @aux" +ArbolSintactico.indiceAux +"\n";
            		 ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		 TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		 TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
             		//seteo el valor del nodo padre igual al resultado de la operacion entre los hijos
             		valorAssembler = "@aux"+ArbolSintactico.indiceAux;
             		
             		if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
            	}
            	break;
            case "IF":
            	//System.out.println("ENTRE ENEL IF");
				salida += getIzq().getAssembler() + getDer().getAssembler();
				salida+=ArbolSintactico.desapilarLabel()+": \n";
				break;
            case "cuerpo":
            	//System.out.println("ENTRE EN EL CUERPO");
            	if(getDer() != null) {
        			salida += getIzq().getAssembler() + getDer().getAssembler();
        		}else {
        			salida += getIzq().getAssembler();
        		}
            	break;
            case "Sentencia_Dentro_IF":
            	//System.out.println("ENTRE EN SENTENCIA DENTRO DE IF");
        		//System.out.println("ENTRE EN EL CASE DE SENTENCIA");
        		if(getIzq() != null) {
        			salida += getIzq().getAssembler() + getDer().getAssembler();
        		}else {
        			salida += getDer().getAssembler();
        		}
        		break;
            //COMPARACIONES  
            case "==":
                salida += getIzq().getAssembler() + getDer().getAssembler();
                hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	
            	//por si hubo menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	
            	 if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		
            		salida += "MOV EAX , " + hijoIzq+"\n";
            		salida += "MOV $auxLongCompIzq , EAX \n";  
            		salida += "MOV EAX , " +hijoDer+"\n";
            		salida += "MOV $auxLongCompDer , EAX \n";
                 	
                 	//menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
                 	
                	salida += "MOV EAX , $auxLongCompIzq \n";
                	salida += "CMP EAX , $auxLongCompDer \n";
                 	salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
                 }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxUintCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxUintCompDer , AX \n";
                 	
                 	//menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
                 	salida +="MOV AX , $auxUintCompIzq \n";
                 	salida +="CMP AX , $auxUintCompDer \n";
                 	
                	
                 	salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
                 }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                 	//SI SE COMPARAN 2 FLOTANTES. NOSE SI ESTA BIEN
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		
             		
             		if(hijoIzq.equals("$TOF")) {
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompIzq \n";
            			
             		}else {
             			salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxFloatCompIzq \n";
             		}

             		if(hijoDer.equals("$TOF")) {
             			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompDer \n";
             		}else {
             			salida += "FLD " + hijoDer+" \n";
             			salida += "FSTP $auxFloatCompDer \n";
             		}
             		
             		
                    //menos menos
                    if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
                	
                	salida += "FLD $auxFloatCompIzq \n";
                 	salida += "FCOM $auxFloatCompDer \n";
                 	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
                    
                 	salida+="JNE "+ArbolSintactico.apilarLabel()+"\n";//salta por distinto al else
                 }
                
            	
                break;
            case "!!":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	
            	//por si hubo menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV EAX , " + hijoIzq+"\n";
            		salida += "MOV $auxLongCompIzq , EAX \n";  
            		salida += "MOV EAX , " +hijoDer+"\n";
            		salida += "MOV $auxLongCompDer , EAX \n";
                	
                	//menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
                	salida+= "MOV EAX , $auxLongCompIzq \n";
                	salida+= "CMP EAX , $auxLongCompDer \n";
                	salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}	
             		salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxUintCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxUintCompDer , AX \n";
                    
                  //menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
                    
                	salida += "MOV AX , auxUintCompIzq \n";
                	salida += "CMP AX , aucUintCompDer \n";
                	
                    salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(hijoIzq.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompIzq \n";
            			
             		}else {
             			salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxFloatCompIzq \n";
             		}

             		if(hijoDer.equals("$TOF")) {
             			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompDer \n";
             		}else {
             			salida += "FLD " + hijoDer+" \n";
             			salida += "FSTP $auxFloatCompDer \n";
             		}
             		
             		
                    //menos menos
                    if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
                	
                	salida += "FLD $auxFloatCompIzq \n";
                 	salida += "FCOM $auxFloatCompDer \n";
                 	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                	salida+="JE "+ArbolSintactico.apilarLabel()+"\n";
                }
            	
                break;
            case ">":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	//para menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV EAX , " + hijoIzq+"\n";
            		salida += "MOV $auxLongCompIzq , EAX \n";  
            		salida += "MOV EAX , " +hijoDer+"\n";
            		salida += "MOV $auxLongCompDer , EAX \n";
                	
                	//menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
                	salida += "MOV EAX , $auxLongCompIzq \n";
                	salida += "CMP EAX , $auxLongCompDer \n";
                	salida+= "JLE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxUintCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxUintCompDer , AX \n";
                	
                	//menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
                	salida += "MOV AX , $aucUintCompIzq \n";
                	salida += "CMP AX , $auxUintCompDer \n";
                	
                	salida+= "JLE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(hijoIzq.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompIzq \n";
            			
             		}else {
             			salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxFloatCompIzq \n";
             		}

             		if(hijoDer.equals("$TOF")) {
             			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompDer \n";
             		}else {
             			salida += "FLD " + hijoDer+" \n";
             			salida += "FSTP $auxFloatCompDer \n";
             		}
             		
             		
                    //menos menos
                    if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
                	
                	salida += "FLD $auxFloatCompIzq \n";
                 	salida += "FCOM $auxFloatCompDer \n";
                 	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
                	salida+="JLE "+ArbolSintactico.apilarLabel()+"\n";
                }
            	
                break;
            case ">=":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	//para menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV EAX , " + hijoIzq+"\n";
            		salida += "MOV $auxLongCompIzq , EAX \n";  
            		salida += "MOV EAX , " +hijoDer+"\n";
            		salida += "MOV $auxLongCompDer , EAX \n";
                	//menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
                	salida += "MOV EAX , $auxLongCompIzq \n";
                	salida += "CMP EAX , $auxLongCompDer \n";
                	
                	salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxUintCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxUintCompDer , AX \n";
                    
                  //menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
                	salida +="MOV AX , $auxUintCompIzq \n";
                	salida +="CMP AX , $auxUintCompDer \n";
                    
                    salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(hijoIzq.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompIzq \n";
            			
             		}else {
             			salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxFloatCompIzq \n";
             		}

             		if(hijoDer.equals("$TOF")) {
             			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompDer \n";
             		}else {
             			salida += "FLD " + hijoDer+" \n";
             			salida += "FSTP $auxFloatCompDer \n";
             		}
             		
             		
                    //menos menos
                    if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
                	
                	salida += "FLD $auxFloatCompIzq \n";
                 	salida += "FCOM $auxFloatCompDer \n";
                 	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                	salida+="JNGE "+ArbolSintactico.apilarLabel()+"\n";
                }
            	
                break; 
            case "<":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	//para menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG")&& !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV EAX , " + hijoIzq+"\n";
            		salida += "MOV $auxLongCompIzq , EAX \n";  
            		salida += "MOV EAX , " +hijoDer+"\n";
            		salida += "MOV $auxLongCompDer , EAX \n";
                	
                	//menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
                	salida += "MOV EAX , $auxLongCompIzq \n";
                	salida += "CMP EAX , $auxLongCompDer \n";
                	salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT")&& !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxUintCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxUintCompDer , AX \n";
                    
                  //menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
                    
                	salida += "MOV EAX , $auxUintCompIzq \n";
                	salida += "CMP EAX , $auxUintCompDer \n";
                	
                    salida+= "JAE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(hijoIzq.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompIzq \n";
            			
             		}else {
             			salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxFloatCompIzq \n";
             		}

             		if(hijoDer.equals("$TOF")) {
             			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompDer \n";
             		}else {
             			salida += "FLD " + hijoDer+" \n";
             			salida += "FSTP $auxFloatCompDer \n";
             		}
             		
             		
                    //menos menos
                    if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
                	
                	salida += "FLD $auxFloatCompIzq \n";
                 	salida += "FCOM $auxFloatCompDer \n";
                 	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
                	salida+="JNL "+ArbolSintactico.apilarLabel()+"\n";
                }
            	
            	break;
            case "<=":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
            	hijoIzq = getIzq().getValorAssembler();
            	hijoDer = getDer().getValorAssembler();
            	//para menos menos
            	if(hijoIzq.contains("--")) {
            		menosmenosIzq = true;
            		hijoIzq= hijoIzq.substring(0, hijoIzq.length()-2);
            	}
            	if(hijoDer.contains("--")) {
            		menosmenosDer = true;
            		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
            	}
            	if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("LONG")&& !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
            		if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV EAX , " + hijoIzq+"\n";
            		salida += "MOV $auxLongCompIzq , EAX \n";  
            		salida += "MOV EAX , " +hijoDer+"\n";
            		salida += "MOV $auxLongCompDer , EAX \n";
                	
                	//menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV EAX , "+hijoIzq+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , EAX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV EAX , "+hijoDer+" \n";
                		salida+= "SUB EAX , 1 \n";
                		salida += "MOV "+ hijoDer+" , EAX \n";
                	}
                	salida += "MOV EAX , $auxLongCompIzq \n";
                	salida += "CMP EAX , $auxLongCompDer \n";
                	salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT")&& !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		salida += "MOV AX , " + hijoIzq+"\n";
            		salida += "MOV $auxUintCompIzq , AX \n";  
            		salida += "MOV AX , " +hijoDer+"\n";
            		salida += "MOV $auxUintCompDer , AX \n";
                    
                  //menos menos
                 	if(menosmenosIzq) {
                		salida+= "MOV AX , "+hijoIzq+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoIzq+" , AX \n";
                	}
                	
                	if(menosmenosDer) {
                		salida+= "MOV AX , "+hijoDer+" \n";
                		salida+= "SUB AX , 1 \n";
                		salida += "MOV "+ hijoDer+" , AX \n";
                	}
                    
                	salida += "MOV AX ,$auxUintCompIzq \n";
                	salida += "CMP AX , $auxUintCompDer \n";
                	
                    salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(hijoIzq.equals("$TOF")) {
            			//hijoIzq = getIzq().getIzq().getValorAssembler();
            			
             			
                		salida += "FILD $"+getIzq().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
                		salida += "FSTP $auxFloatCompIzq \n";
            			
             		}else {
             			salida += "FLD " +hijoIzq+"\n";
             			salida += "FSTP $auxFloatCompIzq \n";
             		}

             		if(hijoDer.equals("$TOF")) {
             			salida += "FILD $"+getDer().getIzq().getValorAssembler().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            			salida += "FSTP $auxFloatCompDer \n";
             		}else {
             			salida += "FLD " + hijoDer+" \n";
             			salida += "FSTP $auxFloatCompDer \n";
             		}
             		
             		
                    //menos menos
                    if(menosmenosIzq) {
            			salida+= "FLD " +hijoIzq+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoIzq+" \n";
                	}
                	
                	if(menosmenosDer) {
            			salida+= "FLD " +hijoDer+ "\n";
            			salida+= "FLD1 \n";
            			salida+= "FSUB \n";
            			salida+= "FSTP "+hijoDer+" \n";
                	}
                	
                	salida += "FLD $auxFloatCompIzq \n";
                 	salida += "FCOM $auxFloatCompDer \n";
                 	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
                	salida+="JNLE "+ArbolSintactico.apilarLabel()+"\n";
                }
            	
                break;
            //FIN CONDICIONES
            case "WHILE":
				salida += getIzq().getAssembler() + getDer().getAssembler();
				salida+=ArbolSintactico.desapilarLabel()+": \n";
				break;
            case "Ejecucion_func":
            	if(getDer() != null) {
            		salida += getDer().getAssembler();
        			hijoDer = getDer().getValorAssembler();
        			if(hijoDer.contains("--")) {
                		menosmenosDer = true;
                		hijoDer = hijoDer.substring(0,hijoDer.length()-2);
                	}
        			if(TablaDeSimbolos.obtenerSimbolo(hijoDer).getTipo().equals("LONG") && !hijoDer.equals("TOF")) {
            			//quiere decir que es un identificador o una constante y no una operacion
            			if(!hijoIzq.contains("@")) {
                 			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
                 		}
                 		if(!hijoDer.contains("@")) {	
                 			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
                 		}
            			salida += "MOV EAX , "+hijoDer+ " \n";
            			salida += "MOV $"+TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getParametro().getLexema().replace("#", "$").replace(".","_") +" , EAX \n";
            		
            			//menos menos
                     	if(menosmenosIzq) {
                    		salida+= "MOV EAX , "+hijoIzq+" \n";
                    		salida+= "SUB EAX , 1 \n";
                    		salida += "MOV "+ hijoIzq+" , EAX \n";
                    	}
                    	
                    	if(menosmenosDer) {
                    		salida+= "MOV EAX , "+hijoDer+" \n";
                    		salida+= "SUB EAX , 1 \n";
                    		salida += "MOV "+ hijoDer+" , EAX \n";
                    	}
            		}else if(TablaDeSimbolos.obtenerSimbolo(hijoDer).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")){
            			if(!hijoIzq.contains("@")) {
                 			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
                 		}
                 		if(!hijoDer.contains("@")) {	
                 			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
                 		}
            			//salida += getDer().getAssembler();
            			salida += "MOV AX , @aux"+ hijoDer + " \n";
            			salida += "MOV $"+TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getParametro().getLexema().replace("#", "$").replace(".","_") +" , AX \n";
            		
            			//menos menos
                     	if(menosmenosIzq) {
                    		salida+= "MOV AX , "+hijoIzq+" \n";
                    		salida+= "SUB AX , 1 \n";
                    		salida += "MOV "+ hijoIzq+" , AX \n";
                    	}
                    	
                    	if(menosmenosDer) {
                    		salida+= "MOV AX , "+hijoDer+" \n";
                    		salida+= "SUB AX , 1 \n";
                    		salida += "MOV "+ hijoDer+" , AX \n";
                    	}
            		}else if(TablaDeSimbolos.obtenerSimbolo(hijoDer).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF")) {
            			if(!hijoIzq.contains("@")) {
                 			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
                 		}
                 		if(!hijoDer.contains("@")) {	
                 			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
                 		}
            			salida += "FLD " + hijoDer + "\n";
                        salida += "FST $" + TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getParametro().getLexema().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") + "\n";
                        if(menosmenosIzq) {
                			salida+= "FLD " +hijoIzq+ "\n";
                			salida+= "FLD1 \n";
                			salida+= "FSUB \n";
                			salida+= "FSTP "+hijoIzq+" \n";
                    	}
                    	
                    	if(menosmenosDer) {
                			salida+= "FLD " +hijoDer+ "\n";
                			salida+= "FLD1 \n";
                			salida+= "FSUB \n";
                			salida+= "FSTP "+hijoDer+" \n";
                    	}
            		}
            		
            		if(!ArbolSintactico.pilaAuxs.isEmpty()) {
            			int i = ArbolSintactico.pilaAuxs.pop();
            			ArbolSintactico.indiceAux = i;
            			ArbolSintactico.pilaAuxs.push(i);
            		}
            		ArrayList<String> listaObjeto = TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getAtributosTotalesClase();
            		
            		
            		String lexMetodo = TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getDireccionMetodo();
            		ArrayList<String> listaClase = TablaDeSimbolos.obtenerSimbolo(lexMetodo).getVariablesMetodo();
            		
            		salida += PasarValoresVariables(listaObjeto, listaClase,getIzq().getLex(), 0);
            		if(!lexMetodo.equals("")) {
            			salida += "call $"+lexMetodo.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " \n";
            		}else {
            			salida += "call $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " \n";
                	}
            		
            		salida += PasarValoresVariables(listaObjeto, listaClase,getIzq().getLex(), 1); 
            		
            		//salida += getIzq().getAssembler()+getDer().getAssembler();
            	}else {
            		//salida += "HIJO DERECHO ES NULL";
            		
            		ArrayList<String> listaObjeto = TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getAtributosTotalesClase();
            		
            		
            		String lexMetodo = TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getDireccionMetodo();
            		ArrayList<String> listaClase = TablaDeSimbolos.obtenerSimbolo(lexMetodo).getVariablesMetodo();
            		
            		salida += PasarValoresVariables(listaObjeto, listaClase, getIzq().getLex(), 0);
            		
            		if(!lexMetodo.equals("")) {
            			salida += "call $"+lexMetodo.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " \n";
            		}else {
            			salida += "call $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " \n";
                	}
            	
            		salida += PasarValoresVariables(listaObjeto, listaClase,getIzq().getLex(), 1); 
            		//salida += getIzq().getAssembler();

                    		
            	
            	}
            	break;
           
                
        }
        
        //salida +="\n";
        //System.out.println("ASSEMBLER HASTA AHORA: ");
        //System.out.println(salida);
        //System.out.println("ASSEMBLER GENERADO: ");
        //System.out.println(salida);
        
        return salida;
	}

	public static String PasarValoresVariables(ArrayList<String> listaVarObj, ArrayList<String> listaVarClase, String lexemaMetObj, int simboloDistinguido) {
		String auxC = "";
		String auxO = "";
		String salida = "";
		String principioMet = "";
		Pattern patternPrincipioMet = Pattern.compile("^(.*?)\\.[^.]+$");
		Matcher matcherPrincipioMet = patternPrincipioMet.matcher(lexemaMetObj);
		if (matcherPrincipioMet.find()) {
			principioMet = matcherPrincipioMet.group(1);
		}
		for (String lexC : listaVarClase) {
            Pattern pattern2 = Pattern.compile("^(.*?#global)");
            Matcher matcher2 = pattern2.matcher(lexC);
            auxO = "";
            if (matcher2.find()) {
            	auxO = principioMet+ "." + matcher2.group();
            }
            System.out.println("lexema clase: "+auxO);
			for (String lexO : listaVarObj) {
		        Pattern pattern1 = Pattern.compile("^(.*?#global)");
		        Matcher matcher = pattern1.matcher(lexO);
		        auxC = "";
		        if (matcher.find()) {
		        	auxC = matcher.group();

		        }
		        System.out.println("lexema obj: "+auxC);

                if (auxC.equals(auxO)) {
                    System.out.println("El lexema "+lexC+" y el lexema "+lexO+" refieren al mismo objeto de la clase");
                    if (simboloDistinguido == 0) {
                    	if (TablaDeSimbolos.obtenerSimbolo(lexO).getTipo().equals("LONG")) {
                    		salida += "MOV EAX, $"+lexO.replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                    		salida +=  "MOV $" + lexC.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", EAX"+ "\n";
                    	} else if ((TablaDeSimbolos.obtenerSimbolo(lexO).getTipo().equals("UINT"))) {
                    		salida+= "MOV AX , $" + lexO.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n";
                            salida+= "MOV $" + lexC.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", AX"+ "\n";
                    	} else {
                    		salida += "FLD $" + lexO.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n";
                    		salida += "FSTP $" + lexC.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n";
                    	}

                    } else {
                    	if (TablaDeSimbolos.obtenerSimbolo(lexO).getTipo().equals("LONG")) {
                    		salida += "MOV EAX, $"+lexC.replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                    		salida +=  "MOV $" + lexO.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", EAX"+ "\n";
                    	} else if ((TablaDeSimbolos.obtenerSimbolo(lexO).getTipo().equals("UINT"))) {
                    		salida+= "MOV AX , $" + lexC.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n";
                            salida+= "MOV $" + lexO.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", AX"+ "\n";
                    	} else {
                    		salida += "FLD $" + lexC.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n";
                    		salida += "FSTP $" + lexO.replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n";
                    	}
                    }
                    break;
                }
            }

        }
		return salida;
    }

	
}


