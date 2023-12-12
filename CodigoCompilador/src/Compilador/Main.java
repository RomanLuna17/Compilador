package Compilador;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import GeneracionCodigoIntermedio.ArbolSintactico;
import GeneracionCodigoIntermedio.NodoControl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.security.KeyStore.Entry;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub	
		Parser parser = new Parser();
		AnalizadorLexico analizador = new AnalizadorLexico();
		
		

		String datoIngresado = "compatibilidad_funciones_REVISAR.txt";
		
		AnalizadorLexico.setLector(Paths.get("").normalize().toAbsolutePath()+"\\src\\Testeos\\Aritmetica\\"+ datoIngresado);	
		
		
		parser.run();		
			
		System.out.println("");
		System.out.println("");
		System.out.println("Tabla de Simbolos: ");
		TablaDeSimbolos.imprimirTabla();
		
		
      	
      	System.out.println("");
      	System.out.println("");
      	
      	ArrayList<NodoControl> clases_funcs = Parser.get_arboles();
		
		for(NodoControl a : clases_funcs) {
			System.out.println("");
			System.out.println("");
			System.out.println("");
			a.recorrerArbol("-");
		}
		System.out.println("");
      	System.out.println("");
      	System.out.println("ARBOL: ");
		NodoControl raiz = parser.getRaiz();
		if(raiz != null) {
			raiz.recorrerArbol("-");
		}else {
			System.out.println("EL arbol esta vacio porque no hay sentencias ejecutables");
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("WARNINGS: ");
		if(!Constantes.varsNoUsadas.isEmpty()) {
			for(String s : Constantes.varsNoUsadas) {
				System.out.println("Variable " + s + " no usada");
			}
		}else {
			System.out.println("No hay warnings");	
		}
			
		System.out.println("");
		System.out.println("");
      	
      	//ERRORES LEXICOS
      	System.out.println("Errores Lexicos: ");
      	if(AnalizadorLexico.erroresLexicos.isEmpty()) {
			System.out.println("No hubo ningun error lexico");
		}else {
			for(String e : AnalizadorLexico.erroresLexicos) {
				System.out.println(e);
			}
		}
		

		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		
		//ERRORES SINTACTICOS
		if(!AnalizadorLexico.erroresLexicos.isEmpty())
			System.out.println("AL HABER ERRORES LEXICO, LOS ERRORES SINTACTICOS Y SEMANTICOS PUEDEN NO CORRESPONDERSE");
		System.out.println("");
		System.out.println("Errores Sintacticos: ");
		if(Parser.getErrores().isEmpty()) {
			System.out.println("NO HUBO ERRORES SINTACTICOS");
		}else {
			for( String s : Parser.getErrores()) {
				System.out.println(s);
			}
		}

		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		//ERRORES SEMANTICOS
      	if(!Parser.getErrores().isEmpty())
      		System.out.println("AL HABER ERRORES SINTACTICOS, LOS ERRORES SEMANTICOS PUEDEN NO CORRESPONDERSE");
		System.out.println("");
		System.out.println("Errores Semanticos: ");
      	if(Parser.erroresSemanticos.isEmpty()) {
			System.out.println("No hubo ningun error Semantico");
		}else {
			for(String e : Parser.erroresSemanticos) {
				System.out.println(e);
			}
		}
		
      	System.out.println("");
		System.out.println("");
      	
		//Assembler
		if(!Parser.tieneErrores && Parser.errores.isEmpty() && Parser.erroresSemanticos.isEmpty()) {
			if(raiz != null) {
				GeneradorAssembler generador = new GeneradorAssembler(raiz);
				System.out.println("");
				System.out.println("");
				generador.generar();
			}else {
				System.out.println("No hay sentencias ejecutables a las que generar el assembler");
			}
		}else {
			System.out.println("EL ASSEMBLER NO SE PUDO GENERAR PORQUE HAY ERRORES");
		}
		
		
		/* MAIN A ENTREGAR
		Scanner scanner = new Scanner(System.in);
		
		
			System.out.println("");
	        System.out.println("");
	        System.out.println("###########################################################################");
	        System.out.println("");
	
			 
	         
			
	        Parser parser = new Parser();
			AnalizadorLexico analizador = new AnalizadorLexico();
			System.out.println("La carpeta a partir de la cual el compilador toma el valor ingresado es \\CodigoCompilador\\src\\Testeos\\");
			System.out.println("Ejemplo de dato a ingresar: [CPP_general.txt]   o     Sintactico\\CPP_general.txt");
			System.out.println(" ");
			System.out.print("Ingrese la direccion del archivo a compilar: ");
			String datoIngresado = scanner.next();
			AnalizadorLexico.setLector(Paths.get("").normalize().toAbsolutePath()+"\\CodigoCompilador\\src\\Testeos\\"+datoIngresado);	
			System.out.println("OPCION ELEGIDA: " + datoIngresado);
		       
		    System.out.println("");
		    System.out.println("");
		    System.out.println("###########################################################################");
		    System.out.println("");
			parser.run();		
			
			
			
			
			
			System.out.println("");
			System.out.println("");
			System.out.println("Tabla de Simbolos: ");
			TablaDeSimbolos.imprimirTabla();
			
			
	      	
	      	System.out.println("");
	      	System.out.println("");
	      	
	      	ArrayList<NodoControl> clases_funcs = Parser.get_arboles();
			
			for(NodoControl a : clases_funcs) {
				System.out.println("");
				System.out.println("");
				System.out.println("");
				a.recorrerArbol("-");
			}
			System.out.println("");
	      	System.out.println("");
	      	System.out.println("ARBOL: ");
			NodoControl raiz = parser.getRaiz();
			if(raiz != null) {
				raiz.recorrerArbol("-");
			}else {
				System.out.println("EL arbol esta vacio porque no hay sentencias ejecutables");
			}
			
			System.out.println("");
			System.out.println("");
			System.out.println("WARNINGS: ");
			if(!Constantes.varsNoUsadas.isEmpty()) {
				for(String s : Constantes.varsNoUsadas) {
					System.out.println("Variable " + s + " no usada");
				}
			}else {
				System.out.println("No hay warnings");	
			}
				
			System.out.println("");
			System.out.println("");
	      	
	      	//ERRORES LEXICOS
	      	System.out.println("Errores Lexicos: ");
	      	if(AnalizadorLexico.erroresLexicos.isEmpty()) {
				System.out.println("No hubo ningun error lexico");
			}else {
				for(String e : AnalizadorLexico.erroresLexicos) {
					System.out.println(e);
				}
			}
			
	
			System.out.println("");
			System.out.println("");
			System.out.println("");
			
			
			//ERRORES SINTACTICOS
			if(!AnalizadorLexico.erroresLexicos.isEmpty())
				System.out.println("AL HABER ERRORES LEXICO, LOS ERRORES SINTACTICOS Y SEMANTICOS PUEDEN NO CORRESPONDERSE");
			System.out.println("");
			System.out.println("Errores Sintacticos: ");
			if(Parser.getErrores().isEmpty()) {
				System.out.println("NO HUBO ERRORES SINTACTICOS");
			}else {
				for( String s : Parser.getErrores()) {
					System.out.println(s);
				}
			}
	
			System.out.println("");
			System.out.println("");
			System.out.println("");
			
			//ERRORES SEMANTICOS
	      	if(!Parser.getErrores().isEmpty())
	      		System.out.println("AL HABER ERRORES SINTACTICOS, LOS ERRORES SEMANTICOS PUEDEN NO CORRESPONDERSE");
			System.out.println("");
			System.out.println("Errores Semanticos: ");
	      	if(Parser.erroresSemanticos.isEmpty()) {
				System.out.println("No hubo ningun error Semantico");
			}else {
				for(String e : Parser.erroresSemanticos) {
					System.out.println(e);
				}
			}
			
	      	System.out.println("");
			System.out.println("");
	      	
			//Assembler
			if(!Parser.tieneErrores && Parser.errores.isEmpty() && Parser.erroresSemanticos.isEmpty()) {
				if(raiz != null) {
					GeneradorAssembler generador = new GeneradorAssembler(raiz);
					System.out.println("");
					System.out.println("");
					generador.generar();
				}else {
					System.out.println("No hay sentencias ejecutables a las que generar el assembler");
				}
			}else {
				System.out.println("EL ASSEMBLER NO SE PUDO GENERAR PORQUE HAY ERRORES");
			}
			
			
	        
	        System.out.println("");
	        System.out.println("");
	        System.out.println("");
	        
	        
	        //TOKENS DETECTADOS
	        System.out.println("TOKENS DETECTADOS POR EL LEXICO: ");
	        Iterator<Map.Entry<String, Integer>> iterator = Constantes.tokens.entrySet().iterator();
	    	
	      	while (iterator.hasNext()) {
	      		Map.Entry<String, Integer> entry = iterator.next();
	      		String key = entry.getKey();
	      		Integer value = entry.getValue();
	      		System.out.println("[ " + key + " , " + value + " ]");
	      	} 	
	      	System.out.println("");
	        System.out.println("");
	        System.out.println("");
	      	//CODIGO PROPORCIONADO
	      	Reader codigo = new BufferedReader(new FileReader(Paths.get("").normalize().toAbsolutePath()+"\\CodigoCompilador\\src\\Testeos\\"+datoIngresado));
	      	BufferedReader bufferedReader = new BufferedReader(codigo);
	      	String linea;
	      	int nro_linea = 1;
	      	System.out.println("Codigo Analizado: ");
	      	while ((linea = bufferedReader.readLine()) != null) {
                System.out.println("LINEA " + nro_linea + ". " + linea);
                nro_linea++;
            }
	      	
	      	bufferedReader.close();

		 
		scanner.close();
        */

    }	
	
}
