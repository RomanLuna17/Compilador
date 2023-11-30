package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.LectorArchivo;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;


//Accion encargada de verificar si el token pertenece a una palabra reservada o a un caracter ASCII
public class AS4 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA 4");
	
		//###################################################################################################################
		
		//forma2
		String lexemaSimbolo = token_act.toString().substring(0, token_act.length());
		
		int id_simbolo = -1;
		if(Constantes.ARCHIVO_CARACTERES_ASCII.containsKey(lexemaSimbolo)) {
			id_simbolo = Constantes.ARCHIVO_CARACTERES_ASCII.get(lexemaSimbolo);
		}else{
			id_simbolo = Constantes.ARCHIVO_PALABRAS_RESERVADAS.get(lexemaSimbolo);
		}
		
		token_act.delete(0, token_act.length()); 
		AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
		return id_simbolo;
	}

}
