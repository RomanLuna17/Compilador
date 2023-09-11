package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;


//elimina el ultimo caracter y retorna el token
public class AS4 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		token_act.deleteCharAt(token_act.length()); //Elimino el ultimo caracter del token actual. ¿nose si lo tengo que guardar?
		
		
		//Retorno el token de la tabla de simbolos. Si no esta lo tengo que agregar me parece y retornarlo
		return 0;
	}

}
