package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.analizadorLexico;

//notifico de que ocurrio un error lexico
public class ASE implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		try {
			char caracter = (char) lector.read(); //Leo el caracter

            
            if (caracter == Constantes.SALTO_DE_LINEA) { //Si es un salto de linea actualizo LineaActual
                analizadorLexico.setLineaActual(analizadorLexico.getLineaActual() + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Constantes.ERROR_EN_TOKEN;
	}

}
