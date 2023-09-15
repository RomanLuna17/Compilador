package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.analizadorLexico;
import Compilador.Constantes;

//LEE EL SIGUIENTE CARACTER Y LO CONCATENA AL TOKEN ACTUAL
public class AS1 implements AccionSemantica {

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA 1");
		try {
            char caracter = (char) lector.read(); //Leo el caracter
			
			
            token_act.append(caracter);//agrego al token
            

            
            if (caracter == Constantes.SALTO_DE_LINEA) { //Si es un salto de linea actualizo LineaActual
                analizadorLexico.setLineaActual(analizadorLexico.getLineaActual() + 1);
            }
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		/*
        System.out.println("TOKEN ACTUAL: " + token_act.toString());
        System.out.println("###########################################");
		*/
		
		return Constantes.SEGUIR_LEYENDO; //RETORNA UN '0' QUE SIGNIFICA QUE EL TOKEN NO ESTA COMPLETO
	}

}
