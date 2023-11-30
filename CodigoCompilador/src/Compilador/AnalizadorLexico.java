package Compilador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;

import AccionesSemanticas.AccionSemantica;

public class AnalizadorLexico {
	
	//HICE LineaActual Y getLineaActual Y setLineaActual ESTATICAS!!
	
	private static int lineaActual = 1;
	public static final StringBuilder tokenActual = new StringBuilder();
	public static int estadoActual = 0;
	//private static AccionSemantica [][] accionesSemanticas = LectorArchivo.readActionMatrixFile(Constantes.ARCHIVO_MATRIZ_ACCIONES, Constantes.CANT_ESTADOS, Constantes.CANT_CARACTERES);
	private static AccionSemantica [][] accionesSemanticas = LectorArchivo.readActionMatrixFile(Paths.get("").normalize().toAbsolutePath()+Constantes.ARCHIVO_MATRIZ_ACCIONES, Constantes.CANT_ESTADOS, Constantes.CANT_CARACTERES);
	
	//private static int [][] transicionDeEstados = LectorArchivo.readIntMatrixFile(Constantes.ARCHIVO_MATRIZ_ESTADO, Constantes.CANT_ESTADOS, Constantes.CANT_CARACTERES);
	private static int [][] transicionDeEstados = LectorArchivo.readIntMatrixFile(Paths.get("").normalize().toAbsolutePath()+Constantes.ARCHIVO_MATRIZ_ESTADO, Constantes.CANT_ESTADOS, Constantes.CANT_CARACTERES);
	
	public static ArrayList<String> erroresLexicos = new ArrayList<String>();
	
	private static String lexemaActual = "";
	
	public static void setLexemaActual(String nuevo_lexema) {
		lexemaActual = nuevo_lexema;
	}
	
	public static String getLexemaActual() {
		return lexemaActual;
	}
	
	public static int getLineaActual() {
		return lineaActual;
	}

	public static void setLineaActual(int lineaActual1) {
		lineaActual = lineaActual1;
	}
	
	private static char obtenerTipoCar (char caracter){
        if (Character.isDigit(caracter)){
            return Constantes.DIGITO;
        } else if ((Character.isLowerCase(caracter)) && (caracter != 'e') && (caracter != 'l') && (caracter != 'u') && (caracter != 'i')){
            return Constantes.MINISCULA;
        }else if ((caracter != 'E') && (Character.isUpperCase(caracter))){
        	return Constantes.MAYUSCULA;
        } else {
            return caracter;
        }
    }
	
	public static Reader lector;

    public static void setLector(String archivo_a_leer) throws FileNotFoundException {
        lector = new BufferedReader(new FileReader(archivo_a_leer));
    }
	
	public static int proximoEstado(Reader lector, char caracter) throws IOException {
        int carActual;
        switch (obtenerTipoCar(caracter)){
            case Constantes.BLANCO:
                carActual = 0;
                break;
            case Constantes.TAB:
                carActual = 1;
                break;
            case Constantes.SALTO_DE_LINEA:
                carActual = 2;
                break;
            case Constantes.MINISCULA:
                carActual = 3;
                break;
            case '_':
                carActual = 4;
                break;
            case Constantes.DIGITO:
                carActual = 5;
                break;
            case 'E':
            	carActual = 7;
                break;
            case 'e':	
                carActual = 6;
                break;
            case '+':
                carActual = 8;
                break;
            case '-':
                carActual = 9;
                break;
            case '*':
                carActual = 10;
                break;
            case '/':
                carActual = 11;
                break;
            case '=':
                carActual = 12;
                break;
            case '!':
                carActual = 13;
                break;
            case '<':
                carActual = 14;
                break;
            case '>':
                carActual = 15;
                break;
            case '.':
                carActual = 16;
                break;
            case ';':
                carActual = 17;
                break;
            case ',':
                carActual = 18;
                break;
            case Constantes.MAYUSCULA:
                carActual = 19;
                break;
            case '{':
                carActual = 20;
                break;
            case '}':
                carActual = 21;
                break;
            case '%':
            	carActual = 22;
            	break;
            case '(':
                carActual = 23;
                break;
            case ')':
                carActual = 24;
                break;
            case 'l':	
                carActual = 25;
                break;
            case 'u':	
                carActual = 26;
                break;
            case 'i':	
                carActual = 27;
                break;
            default: 
                carActual = 28; //Si es un caracter no reconocido, lo manda a ASE
                break;
        }
        
        if(estadoActual == -1) {
        	estadoActual = 0; //Seteo el estado en 0 porque ya pase el error	
        }
        AccionSemantica accSemantica = accionesSemanticas[estadoActual][carActual];
        //System.out.println("ESTADO ACTUAL: " + estadoActual + " CARACTER ACTUAL: " + carActual + " VALOR CARACTER: " + caracter);
        
        int idToken = accSemantica.run(lector, tokenActual);
        
        //System.out.println("ESTADO ACTUAL: " + estadoActual + " CARACTER ACTUAL: " + carActual + " VALOR CARACTER: " + caracter +" ID_TOKEN: " + idToken);
        
        
        estadoActual = transicionDeEstados[estadoActual][carActual];
        
        
        
        if(idToken != 0 && idToken != -1) {
        	Constantes.tokens.put(lexemaActual, idToken);
        }
        
      
        
        
        return idToken;
	}
}