package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.analizadorLexico;

//notifico de que ocurrio un error lexico
public class ASE implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA ERROR");
		try {
			char caracter = (char) lector.read(); //Leo el caracter. TENGO QUE AGREGARLO AL TOKEN_ACT, SINO PIERDO UN CARACTER ME PARECE
													//DUDA POST REALIZACION DE LA ACCION
			
			System.out.println("ERROR: TOKEN ACTUAL: " + token_act);
			token_act.delete(0,token_act.length()); //elimino el token con el error
			
			
            token_act.append(caracter);
            if (caracter == Constantes.SALTO_DE_LINEA){ //Si es un salto de linea actualizo LineaActual
                analizadorLexico.setLineaActual(analizadorLexico.getLineaActual() + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

		/*
        System.out.println("TOKEN ACTUAL: " + token_act.toString());
        System.out.println("###########################################");
		*/
		
        return Constantes.ERROR_EN_TOKEN;
	}

}
