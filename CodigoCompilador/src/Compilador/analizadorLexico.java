package Compilador;

import java.io.IOException;
import java.io.Reader;

import AccionesSemanticas.AccionSemantica;

public class analizadorLexico {
	
	//HICE LineaActual Y getLineaActual Y setLineaActual ESTATICAS!!
	
	private static int lineaActual = 1;
	public static final StringBuilder tokenActual = new StringBuilder();
	public static int estadoActual = 0;
	private static AccionSemantica [][] accionesSemanticas = LectorArchivo.readActionMatrixFile(Constantes.ARCHIVO_MATRIZ_ACCIONES, Constantes.CANT_ESTADOS, Constantes.CANT_CARACTERES);
	private static int [][] transicionDeEstados = LectorArchivo.readIntMatrixFile(Constantes.ARCHIVO_MATRIZ_ESTADO, Constantes.CANT_ESTADOS, Constantes.CANT_CARACTERES);
	
	public static int getLineaActual() {
		return lineaActual;
	}

	public static void setLineaActual(int lineaActual1) {
		lineaActual = lineaActual1;//this.lineaActual = lineaActual; TUVE QUE SACAR EL THIS PORQUE AHORA ES ESTATICA
	}
	
	private static char obtenerTipoCar (char caracter){
        if (Character.isDigit(caracter)){
            return Constantes.DIGITO;
        } else if (Character.isLowerCase(caracter)){
            return Constantes.MINISCULA;
        }else if (caracter != 'E' && caracter != 'e' && caracter != 'l' && caracter != 'u' && caracter != 'i' && Character.isUpperCase(caracter)){
            //FALTO AGREGAR LA i EN EL IF DE ARRIBA
        	return Constantes.MAYUSCULA;
        } else {
            return caracter;
        }
    }
	
	public static int proximoEstado(Reader lector, char caracter) throws IOException {
        int carActual;
        switch (obtenerTipoCar(caracter)){
            case Constantes.BLANCO:
                carActual = 19;
                break;
            case Constantes.TAB:
                carActual = 20;
                break;
            case Constantes.SALTO_DE_LINEA:
                carActual = 23;
                break;
            case Constantes.MINISCULA:
                carActual = 4;
                break;
            case Constantes.MAYUSCULA:
                carActual = 5;
                break;
            case Constantes.DIGITO:
                carActual = 1;
                break;
            case '.':
                carActual = 0;
                break;
            case '+':
                carActual = 6;
                break;
            case '-':
                carActual = 7;
                break;
            case '/':
                carActual = 9;
                break;
            case '(':
                carActual = 10;
                break;
            case ')':
                carActual = 11;
                break;
            case '{':
                carActual = 12;
                break;
            case '}':
                carActual = 13;
                break;
            case ',':
                carActual = 14;
                break;
            case ';':
                carActual = 15;
                break;
            case '=':
                carActual = 3;
                break;
            case '>':
                carActual = 2;
                break;
            case '<':
                carActual = 21;
                break;
            case '!':
                carActual = 17;
                break;
            case '\'':
                carActual = 16;
                break;
            case '*':
                carActual = 8;
                break;
            case 'E':
            case 'e':	
                carActual = 16;
                break;
            case 'u':	
                carActual = 23;
                break;
            case 'i':	
                carActual = 24;
                break;
            case 'l':	
                carActual = 25;
                break;
            case '_':
                carActual = 26;
                break;
            default: 
                carActual = 27; //Si es un caracter no reconocido, lo manda a ASE
                break;
        }
        AccionSemantica accSemantica = accionesSemanticas[estadoActual][carActual];
        int idToken = accSemantica.run(lector, tokenActual);
        estadoActual = transicionDeEstados[estadoActual][carActual];
        //System.out.println("Debug AL178: as: " + accion_semantica + " car: " + car + " ea: " + estado_actual + " id: " + id_token);
        return idToken;
       
	}
}