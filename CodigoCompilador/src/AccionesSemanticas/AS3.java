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
		
		if(lexemaSimbolo.length() > Constantes.CANT_CARACTERES) {
			lexemaSimbolo = token_act.substring(0, Constantes.CANT_CARACTERES); //Recorto el String
			//TENGO QUE INFORMAR WARNING. NOSE SI MOSTRAR POR PANTALLA O DE ESTO SE ENCARGA EL PARSER
			System.err.println("WARNING: EL NOMBRE DEL IDENTIFICADOR SUPERA EL LARGO " );
		}
		
		
		
		Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.ID); //Se encarga de agregar el simbolo en caso de no existir
																 //o retornar el simbolo si ya existe		
        
		token_act.delete(0, token_act.length()); //elimino todos los caracteres

		
        AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
		return simbolo.getId();
 
		
	}

}
