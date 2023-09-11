package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Simbolo;
import Compilador.TablaDeSimbolo;


//elimina el ultimo caracter y retorna el token
public class AS4 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		token_act.deleteCharAt(token_act.length()); //Elimino el ultimo caracter del token actual. ¿nose si lo tengo que guardar?
		
		String simbolo = token_act.toString();
		
		Simbolo simbol = TablaDeSimbolo.obtenerSimbolo(simbolo); //Se encarga de agregar el simbolo en casod e no existir
		 //o retornar el simbolo si ya existe

		return simbol.getId(); //ESTA BIEN ESTO???

		//TENGO QUE RETORNAR EL ID del TOKEN ME PARECE?
		//Retorno el token de la tabla de simbolos. Si no esta lo tengo que agregar me parece y retornarlo
	}

}
