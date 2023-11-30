package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;
import Compilador.AnalizadorLexico;

//notifico de que ocurrio un error lexico
public class ASE implements AccionSemantica{

	@Override
	public int run(Reader lector, StringBuilder token_act) throws IOException {
		//System.out.println("ACCION SEMANTICA ERROR");
		
		try {
			char caracter = (char) lector.read(); 
			
			//System.out.println("Entre a error por: " + token_act + " me llego [" + caracter + "] ");
			String err = "";
			if (caracter == Constantes.SALTO_DE_LINEA){ //Si es un salto de linea actualizo LineaActual
                AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
                err = "Linea " +AnalizadorLexico.getLineaActual() + ". ERROR LEXICO. HUBO UN ERROR: '" + token_act + " SALTO DE LINEA'.NO SE RECONOCE COMO UN TOKEN VALIDO.";
    			
			}else {
				err = "Linea " +AnalizadorLexico.getLineaActual() + ". ERROR LEXICO. HUBO UN ERROR: '" + token_act + caracter + "'.NO SE RECONOCE COMO UN TOKEN VALIDO.";
			}
			
			AnalizadorLexico.erroresLexicos.add(err);
			
			token_act.delete(0,token_act.length()); //elimino el token con el error
			
			
			
			while(caracter != ',') {
				//avanzo el lector hasta encontrar una coma para pasar el error
				
				if (caracter == Constantes.SALTO_DE_LINEA){ //Si es un salto de linea actualizo LineaActual
	                AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
	            }
				
				caracter = (char) lector.read();
				
			}
			//lector.reset();
			AnalizadorLexico.setLexemaActual(","); //seteo una coma en lexema actual
           
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
        return 0;  
	}

}
