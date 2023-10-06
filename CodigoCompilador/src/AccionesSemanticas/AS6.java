package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;

//Agregar el valor constante a la tabla de simbolos en caso de no estar y devuelve el ID del token constante. 
//Verifica que el numero flotante este en el rango correcto
public class AS6 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA 6");
		String lexemaSimbolo = token_act.toString();
		
		try {
			float valor = Float.parseFloat(lexemaSimbolo+'f'); //tengo que concatenar el f al final porque sino float no toma el valor
			
			
			if(valor > Float.MAX_VALUE) {
				//me fui de rango
				//MENSAJE WARNING Y RETORNO EL MAX_VALUE
				
			}
			
		} catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }
		
		
		Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo,Constantes.CTE); //Se encarga de agregar el simbolo en caso de no existir
		 														//o retornar el simbolo si ya existe

		token_act.delete(0, token_act.length()); //elimino todos los caracteres //NOSE SI TENGO QUE DEJAR EL ULTIMO O NO
		
		/*
        System.out.println("TOKEN ACTUAL: " + token_act.toString());
        System.out.println("###########################################");
		*/
        AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
		return simbolo.getId(); //SEGUIMOS CON LA MISMA DUDA
		
		
		

	}

}
