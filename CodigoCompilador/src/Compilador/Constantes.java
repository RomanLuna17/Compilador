package Compilador;


public class Constantes { // esta es una clase donde estaran las constates utilizadas en el resto de clases
	
	public static final int NO_ENCONTRADO = -1;
    public static final char BLANCO = ' ';
    public static final char TAB = '\t';
    public static final char SALTO_LINEA = '\n';
    public static final int DIGITO = 0;
    public static final int CANTIDAD_ESTADOS = 21;
    public static final int CANTIDAD_CARACTERES = 26; // revisar cantidad total
    public static final String FUNC_TYPE = "funcion";
    public static final int LONGITUD_MAXIMA_ID = 20;
    public static final int EN_LECTURA = 0;
    public static final simbolo SIMBOLO_NO_ENCONTRADO= new simbolo("no_encontrado.no",-1);
}
