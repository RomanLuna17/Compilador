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
		//System.out.println("ACCION SEMANTICA 7");
		try {
            char caracter = (char) lector.read(); //Leo el caracter
            
            token_act.append(caracter);//agrego al token
            
            if (caracter == Constantes.SALTO_DE_LINEA) { //Si es un salto de linea actualizo LineaActual
                analizadorLexico.setLineaActual(analizadorLexico.getLineaActual() + 1);
            }
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		
		String lexemaSimbolo = token_act.toString();
		Simbolo simbolo = TablaDeSimbolo.obtenerSimbolo(lexemaSimbolo); //Se encarga de agregar el simbolo en casod e no existir
		 														//o retornar el simbolo si ya existe
		
		token_act.delete(0, token_act.length()); //elimino todos los caracteres
		
		/*
        System.out.println("TOKEN ACTUAL: " + token_act.toString());
        System.out.println("###########################################");
		*/
		
		return simbolo.getId(); //TODAS LAS ACCIONES SEMANTICAS TIENEN ESTA DUDA :)


	}

}
