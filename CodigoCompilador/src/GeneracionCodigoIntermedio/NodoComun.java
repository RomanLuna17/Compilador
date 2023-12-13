package GeneracionCodigoIntermedio;

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
        /* 
        System.out.println("Nodo Comun: " + getLex());
        if(getDer() != null) {
        	System.out.println("NodoComun: '" + getLex()+ "'. HijoIzq: '" + getIzq().getLex() + "'. HijoDer: '" +getDer().getLex() +"'");
        }else {
        	System.out.println("NodoComun: '" + getLex()+ "'. HijoIzq: '" + getIzq().getLex() + "'. HijoDer: 'NULO '");
            	
        }*/
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
                /*
                if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")){
                	//System.out.println("ENTRE AL IF: " + arbIzq.getLex());
                	//System.out.println("DER: " + arbDer.getLex());
                	//System.out.println("IZQ: " + arbIzq.getLex());
                	//System.out.println("NOMBRE DEL TIPO: "+getDer().getClass().getName());
                	if(getDer().getClass().getName().equals("GeneracionCodigoIntermedio.NodoHoja")){
	                    salida+= "MOV EAX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n"; 
	                    salida+= "MOV $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", EAX"+ "\n";
                	}else {    
                		salida += "MOV EAX , @aux" + ArbolSintactico.indiceAux + "\n"; 
                		salida += "MOV $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" , EAX \n";
                	}
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")) {
                	if(getDer().getClass().getName().equals("GeneracionCodigoIntermedio.NodoHoja")){
	                    salida+= "MOV AX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n"; 
	                    salida+= "MOV $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", AX"+ "\n";
                	}else {    
                		salida += "MOV AX , @aux" + ArbolSintactico.indiceAux + "\n"; 
                		salida += "MOV $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" , AX \n";
                	}
                }else {
                	//FALTA LOS NUMEROS FLOTANTES
                	if(getDer().getClass().getName().equals("GeneracionCodigoIntermedio.NodoHoja")){
                		salida += "FLD $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + "\n";
                        salida += "FST $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") + "\n";
                    }else {    
                		salida += "FLD @aux" + ArbolSintactico.indiceAux + "\n"; 
                		salida += "FST $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +"\n";
                	}
                }*/
                
                
                break;
            case "+":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//pregunto el simbolo existe, le quito el primer $
            	System.out.println("LO QUE BUSCO PARA IZQ: " + getIzq().getValorAssembler());
            	System.out.println("LO QUE BUSCO PARA DER: " + getDer().getValorAssembler());
            	
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
            		
            		System.out.println("hijoIzq: " + hijoIzq + " hijoDer: " + hijoDer);
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
            		  
            		
            		//verifico overflow
            		salida += "FNSTSW AX \n";   
            		salida += "SAHF \n";       
            		salida += "TEST AH, 1 \n";         
            		salida += "JNZ errorOverflowSumaFlotantes \n";
            		//salida += "JO errorOverflowSumaFlotantes \n";
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
            case "-":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//pregunto el simbolo existe, le quito el primer $
            	System.out.println("LO QUE BUSCO PARA IZQ: " + getIzq().getValorAssembler());
            	System.out.println("LO QUE BUSCO PARA DER: " + getDer().getValorAssembler());
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
            	System.out.println("RESTA: ARBIZQ: "+ hijoIzq + " ARBDER: " + hijoDer +"######################################");
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
            	
            	System.out.println("MULTIPLICACION: PADRE: "+ getValorAssembler()+" ARBIZQ: "+ hijoIzq + " ARBDER: " + hijoDer+"######################################");
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
            	System.out.println("LO QUE BUSCO PARA IZQ: " + getIzq().getValorAssembler());
            	System.out.println("LO QUE BUSCO PARA DER: " + getDer().getValorAssembler());
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
            	System.out.println("DIVISION: PADRE: "+ getValorAssembler()+" ARBIZQ: "+ hijoIzq + " ARBDER: " + hijoDer+"######################################");
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
            	System.out.println("LUEGO DE REALIZAR LA DIVISION EL VALOR DE ESTE NODO ES: "+ valorAssembler);
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
            	System.out.println("COMPARACION IGUAL FLOTANTES HIJO IZQ: " + hijoIzq +  " HIJODER: " + hijoDer);
            	
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
                 	salida += "CMP EAX , " +hijoDer+"\n";
                 	
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
                 	
                 	salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
                 }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "MOV AX , " +hijoIzq+"\n";
                 	salida += "CMP AX , " +hijoDer+"\n";
                 	
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
                 	
                 	salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
                 }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                 	//SI SE COMPARAN 2 FLOTANTES. NOSE SI ESTA BIEN
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		
             		System.out.println(hijoIzq + " == " + hijoDer);
             		
             		salida += "FLD " +hijoIzq+"\n";
                 	salida += "FCOM " +hijoDer+"\n";
                 	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
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
                	salida += "CMP EAX , " +hijoDer+"\n";
                	
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
                	
                	salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}	
                	salida += "MOV AX , " + hijoIzq+"\n";
                    salida += "CMP AX , " +hijoDer+"\n";
                    
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
                    
                    salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "FLD " +hijoIzq+"\n";
                	salida += "FCOM " + hijoDer+"\n";
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
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
                	salida += "CMP EAX , " +hijoDer+"\n";
                	
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
                	
                	salida+= "JLE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "MOV AX , " + hijoIzq+"\n";
                	salida += "CMP AX , " +hijoDer+"\n";
                	
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
                	
                	salida+= "JLE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "FLD " +hijoIzq+"\n";
                	salida += "FCOM " + hijoDer+"\n";
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
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
                	salida += "CMP EAX , " +hijoDer+"\n";
                	
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
                	
                	salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT") && !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "MOV AX , " + hijoIzq+"\n";
                    salida += "CMP AX , " +hijoDer+"\n";
                    
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
                    
                    salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "FLD " +hijoIzq+"\n";
                	salida += "FCOM " + hijoDer+"\n";
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
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
                    
                	salida+="JB "+ArbolSintactico.apilarLabel()+"\n";
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
                	salida += "CMP EAX , " +hijoDer+"\n";
                	
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
                	
                	salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT")&& !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "MOV AX , " +hijoIzq+"\n";
                    salida += "CMP AX , " +hijoDer+"\n";
                    
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
                    
                    salida+= "JAE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "FLD " +hijoIzq+"\n";
                	salida += "FCOM " + hijoDer+"\n";
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
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
                	salida += "CMP EAX , " +hijoDer+"\n";
                	
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
                	
                	salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("UINT")&& !getDer().getLex().equals("TOF") && !getIzq().getLex().equals("TOF")) {
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "MOV AX , " + hijoIzq+"\n";
                    salida += "CMP AX , " +hijoDer+"\n";
                    
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
                    
                    salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(hijoIzq).getTipo().equals("FLOAT") || getDer().getLex().equals("TOF") || getIzq().getLex().equals("TOF")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	if(!hijoIzq.contains("@")) {
             			hijoIzq ="$"+hijoIzq.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
             		if(!hijoDer.contains("@")) {	
             			hijoDer ="$"+hijoDer.replace("#", "$").replace(".","_").replace("+","$").replace("-","$");
             		}
                	salida += "FLD " +hijoIzq+"\n";
                	salida += "FCOM " +hijoDer+"\n";
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                    
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
                    
                	salida+="JG "+ArbolSintactico.apilarLabel()+"\n";
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
            			System.out.println("Simbolo: " + TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).ToString());
            			System.out.println("Parametro: " +TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getParametro().getLexema());
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
            		
            		salida += "call $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " \n";
            		
            		//salida += getIzq().getAssembler()+getDer().getAssembler();
            	}else {
            		//salida += "HIJO DERECHO ES NULL";
            		salida += "call $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
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



	
}

/*
 case "+":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//System.out.println("ARBIZQ: "+ getIzq().getLex() + " ARBDER: " + getDer().getLex()+"######################################");
            	
            	if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV EAX , $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "ADD EAX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
            	}else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")){
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "ADD AX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
            	}else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")){
            		//FALTA LOS NUMEROS FLOTANTES
            		ArbolSintactico.indiceAux++;
            		salida+= "FLD $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
            		salida+= "FADD $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
            		
            		//verifico overflow
            		salida += "FNSTSW ax \n";   
            		salida += "SAHF \n";       
            		salida += "TEST ah, 2 \n";         

            		salida += "JNZ errorOverflowSumaFlotantes \n";
            		
            		
            		salida+= "FST @aux" +ArbolSintactico.indiceAux +"\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
            	}else {
            		//quiere decir que es un +, -, /, *
            		if(salida.contains("EAX")) {
            			//si es un entero largo
	            		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "ADD EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			ArbolSintactico.pilaAuxs.pop();
	            			salida+= "ADD EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
	            		}
            		}else if(salida.contains("AX")) {
            			//si es un entero sin signo
            			if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "ADD AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "ADD AX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
	            		}
            		}else if(salida.contains("FLD")){
            			//flotantes
            			if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	salida+= "FLD @aux" + ArbolSintactico.indiceAux+"\n"; 
                			salida+= "FADD $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") +"\n";
                			ArbolSintactico.indiceAux++;
	            			salida+= "FST @aux"+ArbolSintactico.indiceAux+"\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}else {
	            			salida+= "FLD @aux" + ArbolSintactico.indiceAux+"\n"; 
                			salida+= "FADD @aux" + ArbolSintactico.pilaAuxs.pop() +"\n";
                			ArbolSintactico.indiceAux++;
	            			salida+= "FST @aux"+ArbolSintactico.indiceAux+"\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}
            		}
            	}
            	//super.aumentarVarAux();
            	
            	
                break;
            case "-":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//System.out.println("ARBIZQ: "+ getIzq().getLex() + " ARBDER: " + getDer().getLex()+"######################################");
            	
            	if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV EAX , $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "SUB EAX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
            	}else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")){
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "SUB AX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
            	}else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")){
            		//FALTA LOS NUMEROS FLOTANTES
            		ArbolSintactico.indiceAux++;
            		salida+= "FLD $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
            		salida+= "FSUB $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
            		salida+= "FST @aux" +ArbolSintactico.indiceAux +"\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
            	}else{
            		//quiere decir que es un +, -, /, * y el derecho es un identificador
            		if(salida.contains("EAX")) {
            			//si es un entero largo
	            		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "SUB EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "SUB EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
	            		}
            		}else if(salida.contains("AX")) {
            			//si es un entero sin signo
            			if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "SUB AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "SUB AX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
	            		}
            		}else if(salida.contains("FLD")){
            			//flotantes
            			if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	salida+= "FLD @aux" + ArbolSintactico.indiceAux+"\n"; 
                			salida+= "FSUB $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") +"\n";
                			ArbolSintactico.indiceAux++;
	            			salida+= "FST @aux"+ArbolSintactico.indiceAux+"\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}else {
	            			salida+= "FLD @aux" + ArbolSintactico.indiceAux+"\n"; 
                			salida+= "FSUB @aux" + ArbolSintactico.pilaAuxs.pop() +"\n";
                			ArbolSintactico.indiceAux++;
	            			salida+= "FST @aux"+ArbolSintactico.indiceAux+"\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}
            		}
            	}
            	//super.aumentarVarAux();
                break;
            case "*":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//System.out.println("ARBIZQ: "+ getIzq().getLex() + " ARBDER: " + getDer().getLex()+"######################################");
            	
            	if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV EAX , $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "IMUL EAX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		
            		//verifico si no hubo overflow, en caso de haber salto a la etiqueta
            		salida += "JO errorOverflowMultEntero \n";
            		
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
            	}else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")){
            		ArbolSintactico.indiceAux++;
            		salida+= "MOV AX , $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		//salida+= "MUL AX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MOV BX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MUL BX \n";
            		
            		//verifico si no hubo overflow, en caso de haber salto a la etiqueta
            		salida += "JO errorOverflowMultEntero \n";
            		
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
            	}else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")){
            		//FALTA LOS NUMEROS FLOTANTES
            		ArbolSintactico.indiceAux++;
            		salida+= "FLD $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "FMUL $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		
            		salida+= "FST @aux" +ArbolSintactico.indiceAux +"\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
            	}else {
            		//quiere decir que es un +, -, /, *
            		if(salida.contains("EAX")) {
            			//si es un entero largo
	            		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "IMUL EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			
	                		//verifico si no hubo overflow, en caso de haber salto a la etiqueta
	                		salida += "JO errorOverflowMultEntero \n";

	            			
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "IMUL EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			
	                		//verifico si no hubo overflow, en caso de haber salto a la etiqueta
	                		salida += "JO errorOverflowMultEntero \n";
	            			
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
	            		}
            		}else if(salida.contains("AX")) {
            			//si es un entero sin signo
            			if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "MOV BX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	                		salida+= "MUL BX \n";
	                		//verifico si no hubo overflow, en caso de haber salto a la etiqueta
	                		salida += "JO errorOverflowMultEntero \n";
	                		
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "MOV BX , $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	                		salida+= "MUL BX \n";
	                		//verifico si no hubo overflow, en caso de haber salto a la etiqueta
	                		salida += "JO errorOverflowMultEntero \n";
	                		
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
	            		}
            		}else if(salida.contains("FLD")){
            			//flotantes
            			if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                    	salida+= "FLD @aux" + ArbolSintactico.indiceAux+"\n"; 
                			salida+= "FMUL $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") +"\n";
                			ArbolSintactico.indiceAux++;
	            			salida+= "FST @aux"+ArbolSintactico.indiceAux+"\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}else {
	            			salida+= "FLD @aux" + ArbolSintactico.indiceAux+"\n"; 
                			salida+= "FMUL @aux" + ArbolSintactico.pilaAuxs.pop() +"\n";
                			
                			ArbolSintactico.indiceAux++;
	            			salida+= "FST @aux"+ArbolSintactico.indiceAux+"\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}
            		}
            	}
            	//super.aumentarVarAux();
                break;
            case "/":
            	salida += getIzq().getAssembler() +  getDer().getAssembler();
            	//System.out.println("ARBIZQ: "+ getIzq().getLex() + " ARBDER: " + getDer().getLex()+"######################################");
            	
            	if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
            		ArbolSintactico.indiceAux++;
            		salida += "CMP $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", 0 \n";
                    salida += "JE errorDivisionPorCero\n";
            		salida+= "MOV EAX , $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "DIV $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
            	}else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")){
            		ArbolSintactico.indiceAux++;
            		//tema particular, division por cero
            		salida += "CMP $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", 0 \n";
                    salida += "JE errorDivisionPorCero\n";
            		salida+= "MOV AX , $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "DIV $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
            		ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
            	}else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")){
            		//verifico que el divisor no sea 0
              		 ArbolSintactico.indiceAux++;
            		 System.out.println("EL primer valor de la pila: " + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " El segundo valor: "+ getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$"));
              		 
              		 salida += "FLD $"+ getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
            		 salida += "FLD $"+ getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+" \n";
            		 salida += "FTST \n";
            		 
            		 salida += "FSTSW AX \n"; //Almacena el estado de la palabra de estado de la FPU en AX
            		 salida += "SAHF \n";     //Transfiere los flags de la FPU al registro de flags del procesador
            		 
                     salida += "JE errorDivisionPorCero \n";
                     salida += "FDIV \n";
            		 salida+= "FST @aux" +ArbolSintactico.indiceAux +"\n";
            		 ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
            		 TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
            		 TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
            	}else {
            		//quiere decir que es un +, -, /, *
            		if(salida.contains("EAX")) {
            			//si es un entero largo
	            		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	            			salida += "CMP $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", 0 \n";
	                        salida += "JE errorDivisionPorCero\n";
	            			
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "DIV $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
	            		}else {
	            			salida += "CMP $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", 0 \n";
	                        salida += "JE errorDivisionPorCero\n";
	            			
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "DIV @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , EAX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("LONG");
	            		}
            		}else if(salida.contains("AX")) {
            			//si es un entero sin signo
            			if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
            				salida += "CMP $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", 0 \n";
                            salida += "JE errorDivisionPorCero\n";
            				
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "DIV $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
	            		}else {
	            			salida += "CMP $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + ", 0 \n";
	                        salida += "JE errorDivisionPorCero\n";
	            			
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "DIV @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			ArbolSintactico.indiceAux++;
	            			salida+= "MOV @aux"+ArbolSintactico.indiceAux+ " , AX " + "\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("UINT");
	            		}
            		}else if(salida.contains("FLD")){
            			//flotantes
            			if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	            			//ArbolSintactico.indiceAux++;
	               		    //salida += "FLD @aux" + ArbolSintactico.indiceAux +" \n";
            				System.out.println("EL primer valor de la pila: " +ArbolSintactico.pilaAuxs.pop() + " El segundo valor: $"+ getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$"));
                     		 
            				//ArbolSintactico.pilaAuxs.pop();
            				//salida += "FLD @aux"+ArbolSintactico.pilaAuxs.pop() +" \n";
            				
            				salida += "FLD $"+ getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+" \n";
	               		    salida += "FTST \n";
	               		    
	               		    salida += "FSTSW AX \n";
	               		 	salida += "SAHF \n";
	               		    
	                        salida += "JE errorDivisionPorCero \n";
	                        salida += "FDIV \n";
	                        ArbolSintactico.indiceAux++;
	               		    salida+= "FST @aux" +ArbolSintactico.indiceAux +"\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}else {
	            			//salida += "FLD @aux" + ArbolSintactico.pilaAuxs.pop() +" \n";
	               		    //salida += "FLD @aux"+ ArbolSintactico.pilaAuxs.pop() +" \n";
	            			System.out.println("EL primer valor de la pila: " + ArbolSintactico.pilaAuxs.pop() + " El segundo valor: "+ ArbolSintactico.pilaAuxs.pop());
	                 		 
	            			//ArbolSintactico.pilaAuxs.pop();
	               		    //ArbolSintactico.pilaAuxs.pop();
	            			salida += "FTST \n";
	            			
	            			salida += "FSTSW AX \n";
	               		 	salida += "SAHF \n";
	            			
	               		 	salida += "JE errorDivisionPorCero \n";
	                        salida += "FDIV \n";
	                        ArbolSintactico.indiceAux++;
	               		    salida+= "FST @aux" +ArbolSintactico.indiceAux +"\n";
	            			
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}
            		}
            	}
 */
