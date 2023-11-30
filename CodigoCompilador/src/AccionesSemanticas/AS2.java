package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.AnalizadorLexico;
import Compilador.Constantes;
import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;

//ENCARGADA DE LEER EL SIGUIENTE CARACTER Y DEVOLVER EL TOKEN DEL LITERAL
public class AS2 implements AccionSemantica{
	
	
	@Override
    public int run(Reader lector, StringBuilder token_act) {
        //System.out.println("ACCION SEMANTICA 2");
		try {
			char caracter = (char) lector.read(); //Leo el caracter
			
			
            token_act.append(caracter);//agrego al token
            
            String lexemaSimbolo = token_act.toString(); 
            
            //System.out.println("Lexema: " + lexemaSimbolo);
            
            int id_simbolo = Constantes.ARCHIVO_CARACTERES_ASCII.get(lexemaSimbolo);     
            
            token_act.delete(0, token_act.length()); //elimino todos los caracteres del token
         
            AnalizadorLexico.setLexemaActual(lexemaSimbolo); //seteo el lexema actual para el parser
            
            return id_simbolo;
           
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }
		

        return 0;
	}

	
}
