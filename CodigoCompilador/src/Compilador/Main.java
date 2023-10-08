package Compilador;


import java.util.ArrayList;
import java.util.Map;

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
		AnalizadorLexico analizador = new AnalizadorLexico();
		//ArrayList<Integer> ids = new ArrayList<Integer>();
		
		//analizador.setLector("src\\archivosTxt\\CodigoPrueba.txt");
		 Parser parser = new Parser();
		 
         //System.out.println("LEXEMA: " + Constantes.ARCHIVO_CARACTERES_ASCII.get(";"));

		 
		 AnalizadorLexico.setLector("C:\\Users\\rolus\\OneDrive\\Escritorio\\git\\Compilador\\CodigoCompilador\\src\\Testeos\\CPP_TOF.txt");
         parser.run();		
		
        }      
	
}
