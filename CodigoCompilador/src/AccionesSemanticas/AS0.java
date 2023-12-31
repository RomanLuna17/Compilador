package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;

//SE ENCARGA DE LEER EL SIGUIENTE CARACTER Y SEGUIR LEYENDO SIN AGREGARLO AL TOKEN
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
		
		
        return Constantes.SEGUIR_LEYENDO; //sigue leyendo
	}

}
