package Compilador;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
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
		/*
		AnalizadorLexico analizador = new AnalizadorLexico();
		AnalizadorLexico.setLector("src\\Testeos\\CPP_WHILEDO.txt"); //COLOCAR ACA LA DIRECCION EN CASO DE QUERER HACER PRUEBAS DE UN UNICO ARCHIVO TXT
		
		Parser parser = new Parser();
		parser.run();		
        
        for( String s : Parser.getErrores()) {
       	 System.out.println(s);
        }
        
        Iterator<Map.Entry<String, Integer>> iterator = Constantes.tokens.entrySet().iterator();

        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("TOKENS DETECTADOS POR EL LEXICO: ");
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("[ " + key + " , " + value + " ]");
        }
		
		*/
		
		
		Scanner scanner = new Scanner(System.in);
		menu(scanner);
        scanner.close();

    }
	
	public static void menu(Scanner scanner) throws IOException{
		System.out.println("");
        System.out.println("");
        System.out.println("###########################################################################");
        System.out.println("");
		Parser parser = new Parser();
		 
         //System.out.println("LEXEMA: " + Constantes.ARCHIVO_CARACTERES_ASCII.get(";"));
 		AnalizadorLexico analizador = new AnalizadorLexico();
		
		 	System.out.println("Codigos disponibles para compilar: ");
		 	System.out.println("1- Codigo de prueba basico");
		 	System.out.println("2- Codigo Operaciones aritmeticas");
		 	System.out.println("3- Codigo de prueba con funcion");
		 	System.out.println("4- Codigo de prueba clases");
		 	System.out.println("5- Codigo de prueba declaraciond de funciones");
		 	System.out.println("6- Codigo de prueba IF");
		 	System.out.println("7- Codigo de prueba MENOS_MENOS");
		 	System.out.println("8- Codigo de prueba Print CADENA");
		 	System.out.println("9- Codigo de prueba TOF");
		 	System.out.println("10- Codigo de prueba WHILE_DO");
		 	System.out.println("ERRORES: ");
		 	System.out.println("11- Errores asignacion");
		 	System.out.println("12- Errores de clases");
		 	System.out.println("13- Errores de funcion");
		 	System.out.println("14- Errores_IF");
		 	System.out.println("15- Errores WHILE_DO");
		 	System.out.println("16- Errores extras");
		 	System.out.println("17- Errores Lexicos");
		 	
	        // Solicitar al usuario que ingrese un dato
	        System.out.print("Ingrese una opcion: ");

	        // Leer el dato ingresado por el usuario
	        int datoIngresado = scanner.nextInt();
	       
	        
	       switch (datoIngresado) {
	       		case 1:
	       			AnalizadorLexico.setLector("src\\Testeos\\CodigoPruebaParser.txt");	
	       			break;
	       		case 2:
	       			AnalizadorLexico.setLector("src\\Testeos\\CodigoPruebaParserOPArit.txt");	
	       			break;
	       		case 3:
	       			AnalizadorLexico.setLector("src\\Testeos\\CodigoPruebaParserParte.txt");	
	       			break;
	       		case 4:
	       			AnalizadorLexico.setLector("src\\Testeos\\CPP_CLASES.txt");	
	       			break;
	       		case 5:
	       			AnalizadorLexico.setLector("src\\Testeos\\CPP_DecFunc.txt");	
	       			break;
	       		case 6:
	       			AnalizadorLexico.setLector("src\\Testeos\\CPP_IF.txt");	
	       			break;
	       		case 7:
	       			AnalizadorLexico.setLector("src\\Testeos\\CPP_MENOSMENOS.txt");	
	       			break;
	       		case 8:
	       			AnalizadorLexico.setLector("src\\Testeos\\CPP_PRINTCADENA.txt");	
	       			break;
	       		case 9:
	       			AnalizadorLexico.setLector("src\\Testeos\\CPP_TOF.txt");	
	       			break;
	       		case 10:
	       			AnalizadorLexico.setLector("src\\Testeos\\CPP_WHILEDO.txt");	
	       			break;
	       		case 11:
	       			AnalizadorLexico.setLector("src\\Testeos\\errores\\CPP_ERRORASIGNACIONES.txt");	
	       			break;
	       		case 12:
	       			AnalizadorLexico.setLector("src\\Testeos\\errores\\CPP_ERRORESCLASES.txt");	
	       			break;
	       		case 13:
	       			AnalizadorLexico.setLector("src\\Testeos\\errores\\CPP_ERRORESFUNCIONES.txt");	
	       			break;
	       		case 14:
	       			AnalizadorLexico.setLector("src\\Testeos\\errores\\CPP_ERRORESIF.txt");	
	       			break;
	       		case 15:
	       			AnalizadorLexico.setLector("src\\Testeos\\errores\\CPP_ERRORESWHILE.txt");	
	       			break;
	       		case 16:
	       			AnalizadorLexico.setLector("src\\Testeos\\errores\\CPP_ERRORESVARIOS.txt");	
	       			break;
	       		case 17:
	       			AnalizadorLexico.setLector("src\\Testeos\\CPP_CodigoDePruebaErroresLexico.txt");	
	       			break;
	       		default:
	       			System.out.println("Numero incorrecto");
	       			break;
	     }


	       System.out.println("");
	        System.out.println("");
	        System.out.println("###########################################################################");
	        System.out.println("");
		 //AnalizadorLexico.setLector("src\\Testeos\\errores\\CPP_ERRORESFUNCIONES.txt");
         parser.run();		
         
         for( String s : Parser.getErrores()) {
        	 System.out.println(s);
         }
         
         Iterator<Map.Entry<String, Integer>> iterator = Constantes.tokens.entrySet().iterator();

         System.out.println("");
         System.out.println("");
         System.out.println("");
         System.out.println("TOKENS DETECTADOS POR EL LEXICO: ");
         while (iterator.hasNext()) {
             Map.Entry<String, Integer> entry = iterator.next();
             String key = entry.getKey();
             Integer value = entry.getValue();
             System.out.println("[ " + key + " , " + value + " ]");
         }
         
         System.out.println("");
         System.out.println("");
         System.out.println("###########################################################################");
         System.out.println("");
         
         AnalizadorLexico.setLineaActual(1);
         Parser.errores.clear();
         menu(scanner);
	}
	
	
}
