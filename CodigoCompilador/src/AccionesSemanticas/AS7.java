package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.AnalizadorLexico;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;



//Lee el siguiente caracter, lo concatena con el caracter actual y devuelve el token
public class AS7 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA 7");
		
		try {
            char caracter = (char) lector.read(); //Leo el caracter
            
            token_act.append(caracter);//agrego al token
            
            if (caracter == Constantes.SALTO_DE_LINEA) { //Si es un salto de linea actualizo LineaActual
                AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
            }
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		
		String lexemaSimbolo = token_act.toString();
		
		//Simbolo simbolo;
		int id_simbolo = -1;
		if(Constantes.ARCHIVO_CARACTERES_ASCII.containsKey(lexemaSimbolo)) {
			//simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.ARCHIVO_CARACTERES_ASCII.get(lexemaSimbolo)); //Se encarga de agregar el simbolo en caso de no existir
			id_simbolo = Constantes.ARCHIVO_CARACTERES_ASCII.get(lexemaSimbolo);
			
			token_act.delete(0, token_act.length()); //elimino todos los caracteres
	        AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
			//return simbolo.getId(); 
	        return id_simbolo;
		
		}else {
			
			int id_token = Constantes.ARCHIVO_PALABRAS_RESERVADAS.getOrDefault(lexemaSimbolo, Constantes.ERROR_EN_TOKEN);
			token_act.delete(0, token_act.length()); //elimino todos los caracteres
	        AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
			return id_token; 
		}
		

	}

}
