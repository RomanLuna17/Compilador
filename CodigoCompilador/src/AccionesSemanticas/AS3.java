package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;

//Se encarga de verificar que el identificador cumple con el largo, que esta en la tabla de simbolos(sino agregarlo) y retornar la ID del token
public class AS3 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		String simbolo = token_act.toString();
		
		if(simbolo.length() > Constantes.CANT_CARACTERES) {
			simbolo = token_act.substring(0, Constantes.CANT_CARACTERES); //Recorto el String
			//CUANDO TENGA EL PARCER INFORMO EL WARNING. POR AHORA CREO QUE LO TENGO QUE AGREGAR A UN ARCHIVO O MOSTRAR POR PANTALLA
		}
		
		//Tengo que verificar que el identificador no se encuentre en la tabla de simbolos
		//Si no se encuentra lo agrego, y si se encuentra entonces retorno el ID?
		
		
		return 0;
	}

}
