%{
package Compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
%}

%token ID CTE CADENA IF THEN ELSE END_IF PRINT CLASS VOID LONG UINT FLOAT WHILE DO IGUAL
       
       //Borrar antes de consultar
       //OUT FUN RETURN BREAK DISCARD
       //CONST DEFER I16 F32 IGUAL MAYOR_IGUAL
       //MENOR_IGUAL EXCLAMACION_EXCLAMACION

%left '+' '-'
%left '*' '/'

%start program

%%
//gramatica

program: programa {System.out.println("ARRANCO EL PROGRAMA");}
       ;

programa: nombre_programa '{' bloque_sentencias_programa '}' {System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa " +  $1.sval);}
	      ;

nombre_programa: ID
	       ;

bloque_sentencias_programa: bloque_sentencias_programa sentencia_programa
        		              | sentencia_programa
        		              ;

sentencia_programa: sentencias_ejecutables
		              ;

sentencias_ejecutables: sentencia_asignacion ','
                      ;

sentencia_asignacion: ID igual valor_asignacion {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion a la variable " + $1.sval);}
                    ;

igual: IGUAL {System.out.println("IGUAL AHRE");}
		 ;            

valor_asignacion: expr_aritmetic
                ;

expr_aritmetic: expr_aritmetic '+' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una suma");}
					    | expr_aritmetic '-' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una resta");}
              | termino
              ;

termino: termino '*' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una multiplicacion");}
       | termino '/' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una division");}
       | factor;

factor: ID {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio UN ID");}
      | const {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + " como constante " );}
      ;

const: CTE {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + "como constante");}
     ;


%%
//funciones
      public static void ConstanteNegativa(String lexema){

      }

      public static void ConstantePositiva(String lexema){
      }


      int yylex() throws IOException {
        int identificador_token = 0;
        //Reader lector = new BufferedReader(new FileReader("C:\\Users\\rolus\\OneDrive\\Escritorio\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\CodigoPrueba.txt"));
        Reader lector = Compilador.AnalizadorLexico.lector;
        
        lector.mark(1);
        int value = lector.read();
        lector.reset();
            
        while(!(value == -1)){
         	lector.mark(1);
          char next_char = (char) lector.read();
          lector.reset();
          identificador_token = AnalizadorLexico.proximoEstado(lector, next_char);      
          
          if (identificador_token != 0) {
          String lexema_entregar = "nose"; //NOSE CON QUE INICIALIZAR
        	if(identificador_token > 311) {
        		  //no es palabra reservada
          		String lexema = TablaDeSimbolos.buscarPorId(identificador_token).getLexema();
          		lexema_entregar = lexema;
          	}else {
          		//System.out.println(ids.get(i));
          		
          		Map<String, Integer> palabrasReservadas = LectorArchivo.readMapFile("src\\archivosTxt\\TablaPalabrasReservadas.txt");
          		
          		for (java.util.Map.Entry<String, Integer> entry : palabrasReservadas.entrySet()) {
          			if(entry.getValue().equals(identificador_token)) {
                      	//Si encontre e lexema no es necesario seguir recorriendo el FOR
          				lexema_entregar = entry.getKey();
                      	 yylval = new ParserVal(lexema_entregar);
                         return identificador_token;
                      }
                  }
          	}
            yylval = new ParserVal(lexema_entregar);
            return identificador_token;
          }

          lector.mark(1);
          value = lector.read();
          lector.reset();
        }
        return identificador_token;
      }

      public static char nextChar(Reader reader) throws IOException {
        reader.mark(1);
        char next_char = (char) reader.read();
        reader.reset();
        return next_char;
      }

      
      private boolean EOF(Reader lector) throws IOException {
        lector.mark(1);
        int value = lector.read();
        lector.reset();
        return value == -1;
      }

    void yyerror(String error) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Yacc reporto un error: " + error); //Falta mostrar cual es el error
    }