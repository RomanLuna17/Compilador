package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import Compilador.AnalizadorLexico;
import Compilador.LectorArchivo;


//Accion semantica encargada de leer las palabras reservadas
public class AS8 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		String lexemaSimbolo = token_act.toString().substring(0, token_act.length()); //saco el ultimo caracter del token
		Map<String, Integer> palabrasReservadas = LectorArchivo.readMapFile("src\\archivosTxt\\TablaPalabrasReservadas.txt");
		
		if(palabrasReservadas.containsKey(lexemaSimbolo)) {
			token_act.delete(0, token_act.length()); //elimino todos los caracteres menos el utlimo porque lo voy a usar para el proximo token
            AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
			return palabrasReservadas.get(lexemaSimbolo);
		}else {
			token_act.delete(0, token_act.length()); //elimino todos los caracteres menos el utlimo porque lo voy a usar para el proximo token
			
			return -1; 
		}
		
	
	}

}
