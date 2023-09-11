package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;


//Se encarga de eliminar el token del comentario y leer el siguiente carater
public class AS9 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
	    token_act.delete(0, token_act.length()); // Reinicia el token

        try {
            lector.read(); // Lee el siguiente caracter
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }

        return Constantes.SEGUIR_LEYENDO;
	}
	
}
