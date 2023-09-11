package Compilador;


import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		analizadorLexico analizador = new analizadorLexico();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		
		Reader lector;
		try {
		lector = new BufferedReader(new FileReader("\\Compilador\\CodigoCompilador\\src\\archivosTxt"));
		}catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }
		
		while(lector.read() != -10) {
			char caracter = (char) lector.read();
			ids.add(analizador.proximoEstado(lector, caracter));
		}
		
	}

}
