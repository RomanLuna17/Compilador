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
		token_act.append(caracter);  
		String lexemaSimbolo = token_act.toString(); //Obtengo el numero
		boolean fuerarango = false;
		
		
		try {
		//verifico de que tipo de entero se trata, si un entero largo, con o sin signo y si su rango es correcto
		if((lexemaSimbolo.substring(lexemaSimbolo.length() - 3)).equals("_ui")){
			//Es un entero sin signo.
			int valor = Integer.parseInt(lexemaSimbolo.substring(0, lexemaSimbolo.length() - 3)); //obtengo el valor numerico. Elimino el sufijo
			if(valor > Constantes.MAXIMO_VALOR_INT_SIN_SIGNO) {
				//me fui de rango
				String err ="Linea " + AnalizadorLexico.getLineaActual() + ". ERROR LEXICO. NUMERO ENTERO SIN SIGNO '" + valor +"' FUERA DE RANGO.";
				AnalizadorLexico.erroresLexicos.add(err);
				fuerarango = true;
			}
		}else if((lexemaSimbolo.substring(lexemaSimbolo.length() - 2)).equals("_l")) {
			//es un numero entero largo
			long valor = Long.parseLong(lexemaSimbolo.substring(0, lexemaSimbolo.length() - 2)); //obtengo el valor numerico
			if(valor > Constantes.MAXIMO_VALOR_INT_LARGO) {//le sumo uno porque en esta etapa nose si es positivo o negativo
											 //y el rango de los enteros negativos tiene 1 mas que los positivos
				//Me fui de rango
				String err ="Linea " + AnalizadorLexico.getLineaActual() + ". ERROR LEXICO. NUMERO ENTERO LARGO '" + valor + "' FUERA DE RANGO.";
				AnalizadorLexico.erroresLexicos.add(err);
				fuerarango = true;
			}
		}
		} catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }
		

		
		if(!fuerarango) {
			Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.CTE); //Se encarga de agregar el simbolo en casod e no existir	
			token_act.delete(0, token_act.length()); //elimino todos los caracteres 
			
			AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
			
			//Agrego el tipo a la constante
			if(lexemaSimbolo.substring(lexemaSimbolo.length() - 3).equals("_ui")){
				simbolo.setTipo("UINT"); //Esta bien que como tipo le coloque UINT
			}else {
				simbolo.setTipo("LONG");
			}
			simbolo.setUso("constante");
			return simbolo.getId(); 
	
		}else {
			AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
			
			token_act.delete(0, token_act.length()); //elimino todos los caracteres 
			return 0;
		}
	}

}
