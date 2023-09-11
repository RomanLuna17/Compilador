package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.TablaDeSimbolo;
import Compilador.Simbolo;


//Se encarga de verificar que el identificador cumple con el largo, que esta en la tabla de simbolos(sino agregarlo) y retornar la ID del token
public class AS3 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		String simbolo = token_act.toString();
		
		if(simbolo.length() > Constantes.CANT_CARACTERES) {
			simbolo = token_act.substring(0, Constantes.CANT_CARACTERES); //Recorto el String
			//CUANDO TENGA EL PARCER INFORMO EL WARNING. POR AHORA CREO QUE LO TENGO QUE AGREGAR A UN ARCHIVO O MOSTRAR POR PANTALLA
		}
		
		
		
		Simbolo simbol = TablaDeSimbolo.obtenerSimbolo(simbolo); //Se encarga de agregar el simbolo en casod e no existir
																 //o retornar el simbolo si ya existe
		
        return simbol.getId(); //ESTA BIEN ESTO???
        
        //TENGO QUE RETORNAR EL ID del TOKEN ME PARECE?
		
	}

}
