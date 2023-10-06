package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;
import Compilador.AnalizadorLexico;

//Agregar el valor constante a la tabla de simbolos en caso de no estar y devuelve el ID del Token. 
//Verifica que tipo de entero es(Long o unsigned), Tambien verifica si los rangos son correctos
public class AS5 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		
		//System.out.println("ACCIONS SEMANTICA 5");
		
		
		char caracter = (char) lector.read();
		token_act.append(caracter);  //En este caso creo que agrego la i o la u. pero no estoy seguro
		
		String lexemaSimbolo = token_act.toString(); //Obtengo el numero
		
		try {
		//verifico de que tipo de entero se trata, si un entero largo, con o sin signo y si su rango es correcto
		if((lexemaSimbolo.substring(lexemaSimbolo.length() - 3)).equals("_ui")){
			//Es un entero sin signo.
			short valor = Short.parseShort(lexemaSimbolo.substring(0, lexemaSimbolo.length() - 3)); //obtengo el valor numerico. Elimino el sufijo
			if(valor > Constantes.MAXIMO_VALOR_INT_SIN_SIGNO) {
				//me fui de rango
				//MENSAJE WARNING Y RETORNO EL MAX_VALUE
				System.err.println("ERROR EN NUMERO ENTERO SIN SIGNO FUERA DE RANGO. LINEA " + AnalizadorLexico.getLineaActual());
			}
		}else if((lexemaSimbolo.substring(lexemaSimbolo.length() - 2)).equals("_l")) {
			//es un numero entero largo
			int valor = Integer.parseInt(lexemaSimbolo.substring(0, lexemaSimbolo.length() - 2)); //obtengo el valor numerico
			if(valor > Constantes.MAXIMO_VALOR_INT_LARGO) {//le sumo uno porque en esta etapa nose si es positivo o negativo
											 //y el rango de los enteros negativos tiene 1 mas que los positivos
				//Me fui de rango
				//MENSAJE WARNING Y RETORNO EL MAX_VALUE
				System.err.println("ERROR EN NUMERO ENTERO LARGO FUERA DE RANGO. LINEA " + AnalizadorLexico.getLineaActual());
			}
		}
		} catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }
		
		
		Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.CTE); //Se encarga de agregar el simbolo en casod e no existir
		 														//o retornar el simbolo si ya existe

		token_act.delete(0, token_act.length()); //elimino todos los caracteres //-1??
		
		/*
        System.out.println("TOKEN ACTUAL: " + token_act.toString());
        System.out.println("###########################################");
		*/
        AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
		return simbolo.getId(); //MISMO CASO QUE ANTERIORES
	}

}
