package GeneracionCodigoIntermedio;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.GeneradorAssembler;
import Compilador.TablaDeSimbolos;

public class NodoComun extends ArbolSintactico {
	
	
	public NodoComun(String lex,ArbolSintactico izq,ArbolSintactico der) {
		super(lex);
		setDer(der);
		setIzq(izq);
	}

	private NodoHoja hojaPropia = null;

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
                }
                
                
                break;
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
            		 salida += "FLD $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") + "\n"; 
                     salida += "FSUB $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") + "\n";
                     salida += "FTST ";  // Comparo ST y 0
                     salida += "JE errorDivisionPorCero \n";
            		
            		
            		ArbolSintactico.indiceAux++;
            		salida+= "FLD $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
            		salida+= "FDIV $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
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
            				//verifico que el divisor no sea 0
                   		 	salida += "FLD $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") + "\n"; 
                            salida += "FSUB $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") + "\n";
                            salida += "FTST ";  // Comparo ST y 0
                            salida += "JE errorDivisionPorCero\n";
            				
            				salida+= "FLD @aux" + ArbolSintactico.indiceAux+"\n"; 
                			salida+= "FDIV $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") +"\n";
                			ArbolSintactico.indiceAux++;
	            			salida+= "FST @aux"+ArbolSintactico.indiceAux+"\n";
	            			ArbolSintactico.pilaAuxs.push(ArbolSintactico.indiceAux);
	            			TablaDeSimbolos.agregarSimbolo("@aux"+ArbolSintactico.indiceAux, Constantes.ID);
	                		TablaDeSimbolos.obtenerSimbolo("@aux"+ArbolSintactico.indiceAux).setTipo("FLOAT");
	            		}else {
	            			//verifico que el divisor no sea 0
	               		 	salida += "FLD $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") + "\n"; 
	                        salida += "FSUB $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$") + "\n";
	                        salida += "FTST ";  // Comparo ST y 0
	                        salida += "JE errorDivisionPorCero\n";
	            			
	            			salida+= "FLD @aux" + ArbolSintactico.indiceAux+"\n"; 
                			salida+= "FDIV @aux" + ArbolSintactico.pilaAuxs.pop() +"\n";
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
        			System.out.println("ENTRE AL ELSE");
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
                if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
                	salida += "MOV EAX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida += "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")) {
                	salida += "MOV AX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida += "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")) {
                	//SI SE COMPARAN 2 FLOTANTES. NOSE SI ESTA BIEN
                	salida += "FLD $" +getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	salida += "FCOM $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	
                	
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                	
                    salida+="JNE "+ArbolSintactico.apilarLabel()+"\n";//salta por distinto al else
                
                
                
                }else {
                	//quiere decir que es un +, -, /, *
                	if(salida.contains("EAX")) {
	                	if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else if(salida.contains("AX")) {
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else {
                		//ES UN FLOTANTE
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JNE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}
                }
                break;
            case "!!":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
                if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
                	salida += "MOV EAX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida += "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")) {
                     	salida += "MOV AX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                     	salida += "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                     	salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	salida += "FLD $" +getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	salida += "FCOM $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	
                	
                	
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                	
                	salida+="JE "+ArbolSintactico.apilarLabel()+"\n";
                }else {
                	//quiere decir que es un +, -, /, *
                	if(salida.contains("EAX")) {
	                	if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else if(salida.contains("AX")) {
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , " +getDer().getLex()+ "\n";
	            			salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else {
                		//ES UN FLOTANTE
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}
                }
                break;
            case ">":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
                if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
                	salida += "MOV EAX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida += "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida+= "JLE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")) {
                	salida += "MOV AX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida += "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida+= "JLE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	salida += "FLD $" +getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	salida += "FCOM $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                	
                	salida+="JLE "+ArbolSintactico.apilarLabel()+"\n";
                }else {
                	//quiere decir que es un +, -, /, *
                	if(salida.contains("EAX")) {
	                	if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JLE " + ArbolSintactico.apilarLabel() + "\n";
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JLE " + ArbolSintactico.apilarLabel() +"\n";
	            		}
                	}else if(salida.contains("AX")) {
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JLE " + ArbolSintactico.apilarLabel() +"\n";
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JLE " + ArbolSintactico.apilarLabel() +"\n";
	            		}
                	}else {
                		//ES UN FLOTANTE
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JLE " + ArbolSintactico.apilarLabel() +"\n";
	            		}else {
	            			salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JLE " + ArbolSintactico.apilarLabel() +"\n";
	            		}
                	}
                }
                break;
            case ">=":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
                if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
                	salida += "MOV EAX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida += "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")) {
                    salida += "MOV AX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                    salida += "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                    salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	salida += "FLD $" +getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	salida += "FCOM $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                	
                	salida+="JL "+ArbolSintactico.apilarLabel()+"\n";
                }else {
                	//quiere decir que es un +, -, /, *
                	if(salida.contains("EAX")) {
	                	if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JL " + ArbolSintactico.apilarLabel() +"\n";
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JL " + ArbolSintactico.apilarLabel() +"\n";
	            		}
                	}else if(salida.contains("AX")) {
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JL " + ArbolSintactico.apilarLabel() +"\n";
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else {
                		//ES UN FLOTANTE
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	                		salida+= "FCOM $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
	            			
	                		salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	                		
	                		salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JL " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}
                }
                break; 
            case "<":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
                if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
                	salida += "MOV EAX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida += "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")) {
                    salida += "MOV AX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                    salida += "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                    salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	salida += "FLD $" +getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	salida += "FCOM $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                	
                	salida+="JGE "+ArbolSintactico.apilarLabel()+"\n";
                }else {
                	//quiere decir que es un +, -, /, *
                	if(salida.contains("EAX")) {
	                	if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else if(salida.contains("AX")) {
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else {
                		//ES UN FLOTANTE
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JGE " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}
                }
            	break;
            case "<=":
            	salida += getIzq().getAssembler() + getDer().getAssembler();
                if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("LONG")) {
                	salida += "MOV EAX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida += "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                	salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("UINT")) {
                    	salida += "MOV AX , $" + getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                    	salida += "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+"\n";
                    	salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
                }else if(TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getTipo().equals("FLOAT")) {
                	//SI SE COMPARAN 2 FLOTANTES
                	salida += "FLD $" +getIzq().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	salida += "FCOM $" + getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+"\n";
                	
                	salida += "FSTSW AX \n";
                    salida += "SAHF \n";
                	
                	salida+="JG "+ArbolSintactico.apilarLabel()+"\n";
                }else {
                	//quiere decir que es un +, -, /, *
                	if(salida.contains("EAX")) {
	                	if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "MOV EAX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP EAX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else if(salida.contains("AX")) {
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+ "\n";
	            			salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "MOV AX , @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "CMP AX , @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}else {
                		//ES UN FLOTANTE
                		if(!getDer().getLex().contains("+") &&  !getDer().getLex().contains("-") && !getDer().getLex().contains("*") && !getDer().getLex().contains("/")) {
	                		salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM $" +getDer().getLex().replace("#", "$").replace(".","_").replace("+", "$").replace("-","$")+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
	            		}else {
	            			salida+= "FLD @aux"+ArbolSintactico.indiceAux+ "\n";
	            			salida+= "FCOM @aux" +ArbolSintactico.pilaAuxs.pop()+ "\n";
	            			
	            			salida += "FSTSW AX \n";
	                        salida += "SAHF \n";
	            			
	            			salida+= "JG " + ArbolSintactico.apilarLabel()+"\n";
	            		}
                	}
                }
                break;
            //FIN CONDICIONES
            case "WHILE":
				salida += getIzq().getAssembler() + getDer().getAssembler();
				salida+=ArbolSintactico.desapilarLabel()+": \n";
				break;
            case "Ejecucion_func":
            	if(getDer() != null) {
            		
            		if(TablaDeSimbolos.obtenerSimbolo(getDer().getLex()).getId() != -1) {
            			//quiere decir que es un identificador o una constante y no una operacion
            			salida += "MOV EAX , $"+getDer().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " \n";
            			salida += "MOV $"+TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getParametro().getLexema().replace("#", "$") +" , EAX \n";
            		}else {
            			salida += getDer().getAssembler();
            			salida += "MOV EAX , @aux"+ ArbolSintactico.pilaAuxs.pop() + " \n";
            			salida += "MOV $"+TablaDeSimbolos.obtenerSimbolo(getIzq().getLex()).getParametro().getLexema().replace("#", "$") +" , EAX \n";
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
