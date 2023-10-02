package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

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
		Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo); //Se encarga de agregar el simbolo en caso de no existir
		 														//o retornar el simbolo si ya existe
		
		
		token_act.delete(0, token_act.length()); //elimino todos los caracteres menos el utlimo porque lo voy a usar para el proximo token
		
		/*
        System.out.println("TOKEN ACTUAL: " + token_act.toString());
        System.out.println("###########################################");
		*/
		
		return simbolo.getId(); //Lo mismo, nose si tengo que retornar esto
	}

}
