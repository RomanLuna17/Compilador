package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.AnalizadorLexico;


//Se encarga de eliminar el token del comentario
public class AS9 implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
	    //System.out.println("ACCION SEMANTICA 9");
		
		try {
        	
            char caracter = (char) lector.read();
            token_act.append(caracter);    
            if (caracter == Constantes.SALTO_DE_LINEA) { //Si es un salto de linea actualizo LineaActual
                AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
            }
            
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		token_act.delete(0, token_act.length()); // Reinicia el token
        
        return Constantes.SEGUIR_LEYENDO;
	}
	
}
