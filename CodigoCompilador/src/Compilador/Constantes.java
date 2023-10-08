package Compilador;

import java.util.Map;

public class Constantes {
	public final static Simbolo SIMBOLO_NO_ENCONTRADO = new Simbolo("Simbolo no encotrado", -1);
	
	public final static String ARCHIVO_MATRIZ_ESTADO = "C:\\Users\\rolus\\OneDrive\\Escritorio\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\MatrizTransicionEstados.txt";
	public final static String ARCHIVO_MATRIZ_ACCIONES = "C:\\Users\\rolus\\OneDrive\\Escritorio\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\MatrizAccionesSemanticas.txt";
	public final static String ARCHIVO_ASCII = "../archivosTxt/caracteres_ASCII.txt";
	public final static Map<String, Integer> ARCHIVO_PALABRAS_RESERVADAS= LectorArchivo.readMapFile("src\\archivosTxt\\TablaPalabrasReservadas.txt");
	public final static int CANT_ESTADOS = 19;
	public final static int CANT_CARACTERES = 29;
	public final static Map<String, Integer> ARCHIVO_CARACTERES_ASCII= LectorArchivo.readMapFile("C:\\Users\\rolus\\OneDrive\\Escritorio\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\caracteres_ASCII.txt");
	
	public final static int SEGUIR_LEYENDO = 0;
	public final static int ERROR_EN_TOKEN = -1;
	public final static int ID = 257;
	public final static int CTE = 258;
	public final static int CADENA = 259;
	
	public final static int MAXIMO_VALOR_INT_LARGO = 32000;
	public final static int MAXIMO_VALOR_INT_SIN_SIGNO = 16000; 
	
	
// CARACTERES
	public final static char MINISCULA = 'a';
	public final static char MAYUSCULA = 'A';
	public final static char DIGITO = '0';
	public static final char SALTO_DE_LINEA = '\n';
	public static final char TAB = '\t';
    public static final char BLANCO = ' ';
	
}
