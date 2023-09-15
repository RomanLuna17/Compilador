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
		
		//analizador.setLector("C:\\Users\\usuario\\Desktop\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\CodigoPrueba.txt");
		
		
		
		
		Reader lector = null;
        try {
            lector = new BufferedReader(new FileReader("C:\\Users\\usuario\\Desktop\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\CodigoPrueba.txt"));
            /*int caracter;
            while ((lector.read()) != -1) {
            	char caracter = (char) lector.read();
                System.out.println(caracter);
                ids.add(analizadorLexico.proximoEstado(lector, caracter));
            }
            char caracter = (char) lector.read();*/
            
            
            //ids.add(analizadorLexico.proximoEstado(lector, caracter));
            
            
            lector.mark(1);
            int value = lector.read();
            lector.reset();
            
            while(!(value == -1)){
            	lector.mark(1);
                char next_char = (char) lector.read();
                lector.reset();
                
                int identificador_token = analizadorLexico.proximoEstado(lector, next_char);
                if (identificador_token != 0) {
                  //AnalizadorLexico.token_actual.delete(0, AnalizadorLexico.token_actual.length());
                  ids.add(identificador_token);
                  //System.out.println("EL TOKEN ENCONTRADO ES MAIN: " + identificador_token);
                }       
                
                
                lector.mark(1);
                value = lector.read();
                lector.reset();
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
        
        
        System.out.println("TOKENS ENCONTRADOS: ");
        for(int i = 0;i<ids.size();i++) {
        	String lexema = TablaDeSimbolo.buscarPorId(ids.get(i)).getLexema();
        	if(lexema.equals(";")) {
        		System.out.println(lexema);
        	}else {
        		System.out.print(lexema);
        	}
        }
		
        
        
	}

	
	
}
