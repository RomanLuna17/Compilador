package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;


//Retorna el token sin contar el ultimo caracter. Nose si esta bien, no tendria que eliminar el caracter que devuelvo????
public class AS8 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		return token_act.charAt(0);//NOSE SI ESTA BIEN ESTO
	}

}
