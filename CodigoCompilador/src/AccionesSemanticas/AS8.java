package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.LectorArchivo;


//Accion semantica encargada de leer las palabras reservadas
public class AS8 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("Accion Semantica 8");
		String lexemaSimbolo = token_act.toString().substring(0, token_act.length()); 
		
		if(Constantes.ARCHIVO_PALABRAS_RESERVADAS.containsKey(lexemaSimbolo)) {
			token_act.delete(0, token_act.length()); 
            AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
            if(!lexemaSimbolo.equals("CTE") && !lexemaSimbolo.equals("ID") && !lexemaSimbolo.equals("CADENA") && !lexemaSimbolo.equals(">=") && !lexemaSimbolo.equals("<=") && !lexemaSimbolo.equals("==") && !lexemaSimbolo.equals("!!"))
            	return Constantes.ARCHIVO_PALABRAS_RESERVADAS.get(lexemaSimbolo);
		}
		String err ="Linea " + AnalizadorLexico.getLineaActual() + ". ERROR LEXICO. NO EXISTE LA PALABRA RESERVADA '" + lexemaSimbolo + "'";
		AnalizadorLexico.erroresLexicos.add(err);
		token_act.delete(0, token_act.length());
		char caracter = '1';
		while(caracter != ',') {
			//avanzo el lector hasta encontrar una coma para pasar el error
				
		if (caracter == Constantes.SALTO_DE_LINEA){ //Si es un salto de linea actualizo LineaActual
			AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
	    }
				
		caracter = (char) lector.read();
				
		}
			
		AnalizadorLexico.setLexemaActual(lexemaSimbolo);
		return 0; 
	}
		
}
