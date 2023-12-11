package Compilador;

import java.util.ArrayList;
import java.util.Hashtable;

import GeneracionCodigoIntermedio.ArbolSintactico;
import GeneracionCodigoIntermedio.NodoControl;

public class GeneradorAssembler {
	ArbolSintactico arbol;
	String contenidoTabla, assemblerMain, codigo, AssemblerFuncs;

	
	public GeneradorAssembler(ArbolSintactico arb) {
		this.arbol = arb;
		contenidoTabla = "";
		assemblerMain = arbol.getAssembler();
		codigo = "";
		AssemblerFuncs = generarAssemblerFuncs();
		generarDataTabla();
	}
	
	private void generarDataTabla(){
		Hashtable<String, Simbolo> tabla = TablaDeSimbolos.obtenerTablaDeSimbolos();
		
		contenidoTabla += "$errorDivisionPorCero db \" Error Assembler:No se puede realizar la division por cero\" , 0 \n";  
		contenidoTabla += "$errorOverflowMultEntero db \" Error Assembler: overflow en producto de enteros \", 0 \n";
		contenidoTabla += "$errorOverflowSumaFlotantes db \" Error Assembler: overflow en la suma de flotantes \", 0 \n";
		
		
		for (String llave : TablaDeSimbolos.obtenerTablaDeSimbolos().keySet()) {
            Simbolo simbolo = tabla.get(llave);
            String valor = "";
            switch(simbolo.getUso()) {
            	case "constante": 
            		switch(simbolo.getTipo()) {
            		case "LONG":
            			valor = simbolo.getLexema().substring(0, simbolo.getLexema().length() - 2); //me quedo con el simbolo sin el sufijo
            			contenidoTabla += "$"+simbolo.getLexema().replace(".","_").replace("+","$").replace("-","$") + " dd " + valor + "\n";
    	            	break;
            		case "UINT":
            			valor = simbolo.getLexema().substring(0, simbolo.getLexema().length() - 3);
            			contenidoTabla += "$"+simbolo.getLexema().replace(".","_").replace("+","$").replace("-","$") + " dw " + valor + "\n";
    	            	break;
            		case "FLOAT":
            			valor = simbolo.getLexema();
            			contenidoTabla += "$"+simbolo.getLexema().replace(".","_").replace("+","$").replace("-","$") + " dd " + valor + "\n";
    	            	break;
	            	}
	            	break;
            	case "identificador":
            		switch(simbolo.getTipo()) {
            		case "LONG":
            			if(simbolo.getValor().contains("_l")) {
	            			valor = simbolo.getValor().substring(0, simbolo.getValor().length() - 2); //me quedo con el simbolo sin el sufijo
	            			contenidoTabla += "$"+simbolo.getLexema().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " dd "+ valor+ "\n";
            			}else {
            				contenidoTabla += "$"+simbolo.getLexema().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " dd ? \n";
                		}
	            		break;
            		case "UINT":
            			if(simbolo.getValor().contains("_ui")) {
	    	            	valor = simbolo.getValor().substring(0, simbolo.getValor().length() - 3);
	            			contenidoTabla += "$"+simbolo.getLexema().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " dw "+valor +" \n";
            			}else {
            				contenidoTabla += "$"+simbolo.getLexema().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " dw ? \n";
                		}
	            		break;
            		case "FLOAT":
            			if(simbolo.getValor().contains(".")) {
            				valor = simbolo.getValor();
            				contenidoTabla += "$"+simbolo.getLexema().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " dd "+valor +" \n";
            			}else {
            				contenidoTabla += "$"+simbolo.getLexema().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") + " dd ? \n";
                		}
            			break;
	            	}
            		break;
            	case "cadena":
            		contenidoTabla += "$"+simbolo.getLexema().replace(" ", "$").replace(".", "_").replace("+","$").replace("-","$") + " db \"" + simbolo.getLexema() +"\" , 0 \n";
            		break;
            	default:
            		if(simbolo.getLexema().contains("@aux")) {
            			switch(simbolo.getTipo()) {
            			case "LONG":
            				contenidoTabla += simbolo.getLexema() + " dd ? \n";
                    		break;
            			case "UINT":
            				contenidoTabla += simbolo.getLexema() + " dw ? \n";
                    		break;
            			case "FLOAT":
                    		contenidoTabla += simbolo.getLexema() + " dd ? \n";
                    		break;
            			}
            		}
            		break;
            }
		}
		//System.out.println("TABLA PASADA A ASSEMBLER: ");
		//System.out.println(contenidoTabla);
		
    }
	
	public void generar() {
				codigo+= ".386 \n";
				codigo+=".model flat, stdcall \n";
				codigo+= "option casemap :none \n";
				codigo+= "include \\masm32\\include\\windows.inc\r\n"
						+ "include \\masm32\\include\\kernel32.inc\r\n"
						+ "include \\masm32\\include\\user32.inc\r\n"
						+ "includelib \\masm32\\lib\\kernel32.lib\r\n"
						+ "includelib \\masm32\\lib\\user32.lib \n";
				
				codigo+=".data \n";
				
				codigo+= contenidoTabla;
				
				codigo += ".code\n";
		        
		        //codigo += codigoFunciones;
		        
				codigo += AssemblerFuncs;
				
		        codigo += "main:\n";
		        codigo += assemblerMain;
		        codigo += "invoke ExitProcess, 0 \n";
		        
		        //etiquetas de errores
		        //division por cero
		        codigo += "errorDivisionPorCero: \n";
		        codigo += "invoke MessageBox, NULL, addr $errorDivisionPorCero , addr $errorDivisionPorCero , MB_OK \n";
		        codigo += "invoke ExitProcess, 0 \n";
		        
		        //overflow entero
		        codigo += "errorOverflowMultEntero: \n";
		        codigo += "invoke MessageBox, NULL, addr $errorOverflowMultEntero , addr $errorOverflowMultEntero , MB_OK \n";
		        codigo += "invoke ExitProcess, 0 \n";
		        
		        //overflow entero
		        codigo += "errorOverflowSumaFlotantes: \n";
		        codigo += "invoke MessageBox, NULL, addr $errorOverflowSumaFlotantes , addr $errorOverflowSumaFlotantes , MB_OK \n";
		        codigo += "invoke ExitProcess, 0 \n";
		        
		        
		        codigo += "end main";
		        
		        System.out.println("\n ASSEMBLER: \n" + codigo);
		        
		        //generarAssemblerFuncs();
		    
	}
	
	public String generarAssemblerFuncs() {
		String salida ="";
		ArrayList<NodoControl> listaFuncs = Parser.get_arboles();
		//System.out.println("FUNCS: ");
		for(NodoControl x : listaFuncs) {
			//System.out.println("ES: " + x.getLex());
			salida += "$" + x.getLex().replace("#", "$")+": \n";
			salida += x.getIzq().getAssembler();
			salida += "ret \n";
		}
		return salida;
	}
}
