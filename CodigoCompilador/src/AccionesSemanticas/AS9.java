package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.analizadorLexico;


//Se encarga de eliminar el token del comentario y leer el siguiente carater
public class AS9 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
	    //System.out.println("ACCION SEMANTICA 9");
		token_act.delete(0, token_act.length()); // Reinicia el token
		
		
        try {
            char caracter = (char) lector.read(); // Lee el siguiente caracter. AHORA QUE LO PIENSO ESTO NOSE PORQUE LO PUSE EN UN PRINCIPIO, NO TIENE MUCHO SENTIDO
            				//CREO QUE ESTOY PERDIENDO UN CARACTER POR BOLUDO
            token_act.append(caracter);
            
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
        
        return Constantes.SEGUIR_LEYENDO;
	}
	
}
