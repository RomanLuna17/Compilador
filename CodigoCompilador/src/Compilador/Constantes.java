package Compilador;

import java.util.Map;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


public class Constantes {
	public final static Simbolo SIMBOLO_NO_ENCONTRADO = new Simbolo("Simbolo no encotrado", -1);
	
	//"\src\archivosTxt\MatrizTransicionEstados.txt"  PARA PROBAR EN ECLIPSE
	//"\CodigoCompilador\src\archivosTxt\MatrizTransicionEstados.txt" PARA PROBAR EN EL JAR
	public final static String ARCHIVO_MATRIZ_ESTADO = "\\CodigoCompilador\\src\\archivosTxt\\MatrizTransicionEstados.txt";
	//"\src\archivosTxt\MatrizAccionesSemanticas.txt" PARA PROBAR EN ECLIPSE
	//"\CodigoCompilador\src\archivosTxt\MatrizAccionesSemanticas.txt" PARA PROBAR EN EL JAR
	public final static String ARCHIVO_MATRIZ_ACCIONES = "\\CodigoCompilador\\src\\archivosTxt\\MatrizAccionesSemanticas.txt";
	public final static String ARCHIVO_ASCII = "\\src\\archivosTxt\\caracteres_ASCII.txt";
	
	//PARA PROBAR EN EL ECLIPSE
	//public final static Map<String, Integer> ARCHIVO_PALABRAS_RESERVADAS= LectorArchivo.readMapFile("src\\archivosTxt\\TablaPalabrasReservadas.txt");
	
	//PARA EL JAR
	public final static Map<String, Integer> ARCHIVO_PALABRAS_RESERVADAS= LectorArchivo.readMapFile(Paths.get("").normalize().toAbsolutePath()+"\\CodigoCompilador\\src\\archivosTxt\\TablaPalabrasReservadas.txt");
	
	
	public final static int CANT_ESTADOS = 19;
	public final static int CANT_CARACTERES = 29;
	
	//PARA PROBAR EN EL ECLIPSE
	//public final static Map<String, Integer> ARCHIVO_CARACTERES_ASCII= LectorArchivo.readMapFile("src\\archivosTxt\\caracteres_ASCII.txt");
	
	//PARA PROBAR EN JAR
	public final static Map<String, Integer> ARCHIVO_CARACTERES_ASCII= LectorArchivo.readMapFile(Paths.get("").normalize().toAbsolutePath()+"\\CodigoCompilador\\src\\archivosTxt\\caracteres_ASCII.txt");
	
	
	public static Map<String, Integer> tokens = new HashMap<>();
	public static ArrayList<String> varsNoUsadas = new ArrayList<String>();

	
	
	public final static int SEGUIR_LEYENDO = 0;
	public final static int ERROR_EN_TOKEN = -1;
	public final static int ID = 257;
	public final static int CTE = 258;
	public final static int CADENA = 259;
	
	public final static long MAXIMO_VALOR_INT_LARGO = 2147483648L;
	public final static int MAXIMO_VALOR_INT_SIN_SIGNO = 65535;
	public final static double MAXIMO_VALOR_FLOTANTE =  3.40282347E+38;
	
	
// CARACTERES
	public final static char MINISCULA = 'a';
	public final static char MAYUSCULA = 'A';
	public final static char DIGITO = '0';
	public static final char SALTO_DE_LINEA = '\n';
	public static final char TAB = '\t';
    public static final char BLANCO = ' ';
    
  //TIPO Identificador
    public final static String FUNCION = "Funcion";
	
	
}
