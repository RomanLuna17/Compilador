package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.analizadorLexico;
import Compilador.Constantes;

//SE ENCARGA DE LEER EL SIGUIENTE TOKEN EN EL PROGRAMA
public class AS0 implements AccionSemantica {
	
	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		try {
            char caracter = (char) lector.read(); // Lee el siguiente caracter

            if (caracter == Constantes.SALTO_DE_LINEA) {
                analizadorLexico.setLineaActual(analizadorLexico.getLineaActual() + 1);
            }
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }

        return Constantes.SEGUIR_LEYENDO; //sigue leyendo porque el token todavia no esta completo
	}

}
