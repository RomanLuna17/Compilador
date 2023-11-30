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
		boolean fuerarango = false;
		try {
			float valor = Float.parseFloat(lexemaSimbolo+'f'); //tengo que concatenar el f al final porque sino float no toma el valor
			
			
			if(valor > Float.MAX_VALUE) {
				//me fui de rango
				String err ="Linea " + AnalizadorLexico.getLineaActual() + ". ERROR LEXICO. NUMERO FLOTANTE '" + valor + "' FUERA DE RANGO.";
				AnalizadorLexico.erroresLexicos.add(err);
				fuerarango = true;
			}
			
		} catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }
		
		if(!fuerarango) {
			Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo,Constantes.CTE);
	
			//System.out.println("idToken: " + simbolo.getId() + " Lexema: " + simbolo.getLexema());
			
			token_act.delete(0, token_act.length()); //elimino todos los caracteres
			
	        AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
	        
	        //Seteo el tipo a la constante
	        simbolo.setTipo("FLOAT");
	        simbolo.setUso("constante");
			return simbolo.getId(); 
		}else {
			token_act.delete(0, token_act.length()); //elimino todos los caracteres
			return 0;
		}
		
		

	}

}
