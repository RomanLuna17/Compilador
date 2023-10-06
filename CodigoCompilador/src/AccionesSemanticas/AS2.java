package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;

//ENCARGADA DE LEER EL SIGUIENTE CARACTER Y DEVOLVER EL TOKEN DEL LITERAL
public class AS2 implements AccionSemantica{
	
	
	@Override
    public int run(Reader lector, StringBuilder token_act) {
        //System.out.println("ACCION SEMANTICA 2");
		try {
			char caracter = (char) lector.read(); //Leo el caracter
            
            token_act.append(caracter);//agrego al token
        	
            
            String lexemaSimbolo = token_act.toString(); //Obtengo el numero
            //System.out.println("LEXEMA: " + lexemaSimbolo + " ID: " + Constantes.ARCHIVO_CARACTERES_ASCII.get(lexemaSimbolo));
            Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.ARCHIVO_CARACTERES_ASCII.get(lexemaSimbolo)); //Se encarga de agregar el simbolo en casod e no existir
																	//o retornar el simbolo si ya existe
            
            token_act.delete(0, token_act.length()); //elimino todos los caracteres del token
        	
            
           
          /*  
            System.out.println("TOKEN ACTUAL: " + token_act.toString());
            System.out.println("###########################################");
           */
            //System.out.println("SIMBOLO: " + simbolo.getLexema() + " ID: " + simbolo.getId());
            AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
            return simbolo.getId();// Retorno token
            
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		

        return Constantes.ERROR_EN_TOKEN; //retorno -1 en caso de error
    }

	
}
