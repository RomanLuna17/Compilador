package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;


import Compilador.Simbolo;
import Compilador.TablaDeSimbolo;

//Agregar el valor constante a la tabla de simbolos en caso de no estar y devuelve el ID del Token. 
//Verifica que tipo de entero es(Long o unsigned), Tambien verifica si los rangos son correctos
public class AS5 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		
		//DEBATIR SI ESTA BIEN ESTA PARTE
		char caracter = (char) lector.read();
		token_act.append(caracter);  //En este caso creo que agrego la i o la u. pero no estoy seguro
		
		String simbolo = token_act.toString(); //Obtengo el numero
		
		try {
		//verifico de que tipo de entero se trata, si un entero largo, con o sin signo y si su rango es correcto
		if((simbolo.substring(simbolo.length() - 3)).equals("_ui")){
			//Es un entero sin signo.
			short valor = Short.parseShort(simbolo.substring(0, simbolo.length() - 3)); //obtengo el valor numerico. Elimino el sufijo
			if(valor > Short.MAX_VALUE) {
				//me fui de rango
				//MENSAJE WARNING Y RETORNO EL MAX_VALUE			}
			}
		}else if((simbolo.substring(simbolo.length() - 2)).equals("_l")) {
			//es un numero entero largo
			int valor = Integer.parseInt(simbolo.substring(0, simbolo.length() - 2)); //obtengo el valor numerico
			if(valor > Integer.MAX_VALUE+1) {//le sumo uno porque en esta etapa nose si es positivo o negativo
											 //y el rango de los enteros negativos tiene 1 mas que los positivos
				//Me fui de rango
				//MENSAJE WARNING Y RETORNO EL MAX_VALUE
			}
		}
		} catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }
		
		
		Simbolo simbol = TablaDeSimbolo.obtenerSimbolo(simbolo); //Se encarga de agregar el simbolo en casod e no existir
		 //o retornar el simbolo si ya existe

		return simbol.getId(); //ESTA BIEN ESTO???

		//TENGO QUE RETORNAR EL ID del TOKEN ME PARECE?
		
		
		/* DESCOMENTAR CUANDO CREE LA TABLA DE SIMBOLOS
		if (TablaSimbolos.obtenerSimbolo(simbolo) == TablaSimbolos.NO_ENCONTRADO) {
            TablaSimbolos.agregarSimbolo(simbolo); //Agrego el simbolo
            
            int ptr_id = TablaSimbolos.obtenerSimbolo(simbolo); //obtengo el puntero(ID)
            TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaTipos.ULONG_TYPE); // NOSE QUE ES ESTO!!
        }
        
        TENGO QUE RETORNAR EL ID del TOKEN ME PARECE
		*/
	}

}
