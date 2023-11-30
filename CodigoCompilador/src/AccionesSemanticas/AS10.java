package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;


//Accion encargada de leer cadenas
public class AS10 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("Accion Semantica 10");
		try {
            char caracter = (char) lector.read();
            token_act.append(caracter);
            if (caracter == Constantes.SALTO_DE_LINEA) { //Si es un salto de linea actualizo LineaActual
                AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
            }
            String lexemaSimbolo = token_act.toString();
    		lexemaSimbolo = lexemaSimbolo.substring(1, lexemaSimbolo.length()-1); // QUITO % %
            
    		Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexemaSimbolo, Constantes.CADENA);
    		simbolo.setUso("cadena");
    		simbolo.setUsada(true);
    		AnalizadorLexico.setLexemaActual(lexemaSimbolo);
    		
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		//System.out.println("CADENA: " + token_act);
		token_act.delete(0, token_act.length()); //elimino todos los caracteres

        return Constantes.CADENA;
	}

}
