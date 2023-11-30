package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.TablaDeSimbolos;
import Compilador.Simbolo;


//Se encarga de verificar que el identificador cumple con el largo, que esta en la tabla de simbolos(sino agregarlo) y retornar la ID del token
public class AS3 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA 3");
		String lexemaSimbolo = token_act.substring(0, token_act.length());
		
		//System.out.println("Entre aca porque el token es: " + token_act);
		
		if(lexemaSimbolo.length() > 20) {
			lexemaSimbolo = token_act.substring(0, 20); //Recorto el String
			String err = "WARNING: Linea "+ AnalizadorLexico.getLineaActual() +". EL NOMBRE DEL IDENTIFICADOR: " + token_act.substring(0, token_act.length()) +" SUPERA EL LARGO. SERA REEMPLAZADO POR: " + lexemaSimbolo;
			AnalizadorLexico.erroresLexicos.add(err);
		}
		
		
		
		Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.ID); 	
        
		token_act.delete(0, token_act.length()); //elimino todos los caracteres

		
        AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
		return simbolo.getId();
 
		
	}

}
