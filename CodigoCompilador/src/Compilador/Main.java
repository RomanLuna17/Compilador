package Compilador;


import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		analizadorLexico analizador = new analizadorLexico();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		analizador.setLector("C:\\Users\\usuario\\Desktop\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\CodigoPrueba.txt");
		
		
		
		
		Reader lector = null;
        try {
            lector = new BufferedReader(new FileReader("C:\\Users\\usuario\\Desktop\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\CodigoPrueba.txt"));
            
            int caracter;
            while ((caracter = lector.read()) != -1) {
                char c = (char) caracter;
                System.out.println(c);
                ids.add(analizadorLexico.proximoEstado(lector, c));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (lector != null) {
                try {
                    lector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        for(int i = 0;i<ids.size();i++) {
        	System.out.print(ids.get(i));
        	System.out.print(" ");
        }
		
	}

}
