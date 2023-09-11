package Compilador;

public class Constantes {
	public final static Simbolo SIMBOLO_NO_ENCONTRADO = new Simbolo("Simbolo no encotrado", -1);
	public final static String ARCHIVO_MATRIZ_ESTADO = "";
	public final static String ARCHIVO_MATRIZ_ACCIONES = "\\Compilador\\CodigoCompilador\\src\\archivosTxt";
	public final static int CANT_ESTADOS = 20;
	public final static int CANT_CARACTERES = 20;
	
	public final static int SEGUIR_LEYENDO = 0;
	public final static int ERROR_EN_TOKEN = -1;
	
	
// CARACTERES
	public final static char MINISCULA = 'a';
	public final static char MAYUSCULA = 'A';
	public final static char DIGITO = '0';
	public static final char SALTO_DE_LINEA = '\n';
	public static final char TAB = '\t';
    public static final char BLANCO = ' ';
	
}
