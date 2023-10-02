package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;

//SE ENCARGA DE LEER EL SIGUIENTE TOKEN EN EL PROGRAMA
public class AS0 implements AccionSemantica {
	
	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA 0");
		try {
            char caracter = (char) lector.read(); // Lee el siguiente caracter

            if (caracter == Constantes.SALTO_DE_LINEA) {
                AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
            }
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		
		/*
        System.out.println("TOKEN ACTUAL: " + token_act.toString());
        System.out.println("###########################################");
		*/
        return Constantes.SEGUIR_LEYENDO; //sigue leyendo
	}

}
