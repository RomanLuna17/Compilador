package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

//Agregar el valor constante a la tabla de simbolos en caso de no estar y devuelve el ID del token constante. 
//Verifica que el numero flotante este en el rango correcto
public class AS6 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		String simbolo = token_act.toString();
		
		try {
			float valor = Float.parseFloat(simbolo+'f'); //tengo que concatenar el f al final porque sino float no toma el valor
			
			
			if(valor > Float.MAX_VALUE) {
				//me fui de rango
				//Nose que tengo que hacer, retornar error, warning y enviar el MAX_VALUE o nose
				
			}
			
		} catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }
		
		/* DESCOMENTAR CUANDO CREE LA TABLA DE SIMBOLOS
		if (TablaSimbolos.obtenerSimbolo(simbolo) == TablaSimbolos.NO_ENCONTRADO) {
            TablaSimbolos.agregarSimbolo(simbolo); //Agrego el simbolo
            
            int ptr_id = TablaSimbolos.obtenerSimbolo(simbolo); //obtengo el puntero(ID)
            TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaTipos.ULONG_TYPE); // NOSE QUE ES ESTO!!
        }
        
        TENGO QUE RETORNAR EL TOKEN ME PARECE
		*/
		
		return 0;
	}

}
