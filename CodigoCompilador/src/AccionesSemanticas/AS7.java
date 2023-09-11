package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.analizadorLexico;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolo;


//Lee el siguiente caracter, lo concatena con el caracter actual y devuelve el token
public class AS7 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		try {
            char caracter = (char) lector.read(); //Leo el caracter
            
            token_act.append(caracter);//agrego al token
            
            if (caracter == Constantes.SALTO_DE_LINEA) { //Si es un salto de linea actualizo LineaActual
                analizadorLexico.setLineaActual(analizadorLexico.getLineaActual() + 1);
            }
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		
		//TENGO QUE DEVOLVER EL TOKEN O EL IDENTIFICADOR DEL TOKEN, NOSE BIEN
		//ESPERO A CREAR LA ESTRUCTURA DE LA TABLA DE SIMBOLOS PARA HACER ESTO
		
		String simbolo = token_act.toString();
		Simbolo simbol = TablaDeSimbolo.obtenerSimbolo(simbolo); //Se encarga de agregar el simbolo en casod e no existir
		 //o retornar el simbolo si ya existe

		return simbol.getId(); //ESTA BIEN ESTO???

		//TENGO QUE RETORNAR EL ID del TOKEN ME PARECE?

	}

}
