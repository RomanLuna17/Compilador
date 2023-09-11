package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

import Compilador.Constantes;

//ENCARGADA DE LEER EL SIGUIENTE CARACTER Y DEVOLVER EL TOKEN DEL LITERAL
public class AS2 implements AccionSemantica{
    //PROBAR IMPRIMIENDO POR PANTALLA QUE RETORNA PARA TERMINAR DE ENTENDERLO!!
	
	
	@Override
    public int run(Reader lector, StringBuilder token) {
        try {
        	char literal = (char) lector.read(); //Leo el siguiente caracter
            return (int) literal; //retorno el token
        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }

        return Constantes.ERROR_EN_TOKEN; //retorno -1 en caso de error
    }

	
}
