package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.LectorArchivo;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;


//elimina el ultimo caracter y retorna el token
public class AS4 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA 4");
	
		//###################################################################################################################
		
		//forma2
		String lexemaSimbolo = token_act.toString().substring(0, token_act.length()); //saco el ultimo caracter del token
		Simbolo simbolo;
		if(Constantes.ARCHIVO_CARACTERES_ASCII.containsKey(lexemaSimbolo)) {
			simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.ARCHIVO_CARACTERES_ASCII.get(lexemaSimbolo)); //Se encarga de agregar el simbolo en caso de no existir
		}else {
			simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.ARCHIVO_PALABRAS_RESERVADAS.get(lexemaSimbolo)); //Se encarga de agregar el simbolo en caso de no existir
		}
		//Map<String, Integer> palabrasReservadas = LectorArchivo.readMapFile("src\\archivosTxt\\TablaPalabrasReservadas.txt");
		/*
		if(palabrasReservadas.containsKey(lexemaSimbolo)) {
			token_act.delete(0, token_act.length()); //elimino todos los caracteres menos el utlimo porque lo voy a usar para el proximo token
			return palabrasReservadas.get(lexemaSimbolo);
		}else {
			token_act.delete(0, token_act.length()); //elimino todos los caracteres menos el utlimo porque lo voy a usar para el proximo token
			
			return -1; 
		
		token_act.delete(0, token_act.length()); //elimino todos los caracteres menos el utlimo porque lo voy a usar para el proximo token
		
		/*
        System.out.println("TOKEN ACTUAL: " + token_act.toString());
        System.out.println("###########################################");
		*/
		
        AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
		return simbolo.getId(); 
	}

}
