%{
package Compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

%}

%token ID CTE CADENA IF THEN ELSE END_IF OUT FUN RETURN BREAK DISCARD
       CONST DEFER I16 F32 IGUAL MAYOR_IGUAL
       MENOR_IGUAL EXCLAMACION_EXCLAMACION

%left '+' '-'
%left '*' '/'

%start program

%%
//gramatica

program: programa
       ;

programa: nombre_programa '{' bloque_sentencias_programa '}' {System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa " +  $1.sval);}
	      ;

nombre_programa: ID
	       ;

bloque_sentencias_programa: bloque_sentencias_programa sentencia_programa
        		  | sentencia_programa
        		  ;

sentencia_programa: sentencias_declarativas
        	        | sentencias_ejecutables
		              ;

sentencias_declarativas:  declaracion_variables ','
		                   ;

sentencias_ejecutables: sentencia_asignacion ','
		                  //| sentencia_out ','

declaracion_variables: tipo list_var {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion de variable");}
            		     ;

tipo: I16 {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el tipo I16");
	   $$.sval = "i16";
	   aux = "i16";}
    | F32 {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el tipo F32");
    	   $$.sval = "f32";
    	   aux = "f32";}
    | CADENA {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una cadena");
    	      aux = "CADENA";}
    ;

list_var: list_var ',' ID {System.out.println("G86: se le va a asignar tipo: " + aux + " a la variable " + $3.sval + ambito);}
          //TablaDeSimbolos.obtenerSimbolo($3.sval, ambito.toString()).setTipo(aux);
          //TablaDeSimbolos.obtenerSimbolo($3.sval, ambito.toString()).setUso("variable");}
        | ID {System.out.println("G90: se le va a asignar tipo: " + aux + " a la variable " + $1.sval + ambito);}
          //TablaDeSimbolos.obtenerSimbolo($1.sval, ambito.toString()).setTipo(aux);
          //TablaDeSimbolos.obtenerSimbolo($1.sval, ambito.toString()).setUso("variable");}
	      ;

sentencia_asignacion: ID igual valor_asignacion {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion a la variable " + $1.sval);}
                    ;

igual: IGUAL //{ultimaAsignacion = polaca.size();}
		 ;            

valor_asignacion: expr_aritmetic
                ;

//sentencia_out: OUT '(' CADENA ')' {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la impresion de una cadena: ");}
//             ;

expr_aritmetic: expr_aritmetic '+' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una suma");}
					    | expr_aritmetic '-' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una resta");}
              | termino
              ;

termino: termino '*' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una multiplicacion");}
       | termino '/' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una division");}
       | factor;

factor: ID {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + " como ID" );}
      | const {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + $1.sval + " como constante " );}
      ;

const: CTE { if (($1.sval).contains(".")){$1.sval = String.valueOf(PasarAFloatJava($1.sval));}
	     ConstantePositiva($1.sval);
 	     $$.sval = $1.sval;}
     //| '-'CTE { if (($2.sval).contains(".")){$2.sval = String.valueOf(PasarAFloatJava($2.sval));}
     //		ConstanteNegativa($2.sval);
     // 	$$.sval = ('-'+$2.sval);}
     ;


%%
//funciones
      public static void ConstanteNegativa(String lexema){
  	    Simbolo s = TablaDeSimbolos.obtenerSimbolo(lexema);
      }

      public static void ConstantePositiva(String lexema){
      }

      int yylex() throws IOException {
        /*
        int identificador_token = 0;
        Reader lector = compilador.AnalizadorLexico.lector;
        compilador.AnalizadorLexico.estado_actual = 0;
        while (! EOF(lector)) {
          char caracter = nextChar(lector);
          identificador_token = AnalizadorLexico.proximoEstado(lector, caracter);
          if (identificador_token != ConstantesCompilador.EN_LECTURA) {
            yylval = new ParserVal(AnalizadorLexico.getTokenActual());
            AnalizadorLexico.token_actual.delete(0, AnalizadorLexico.token_actual.length());
            return identificador_token;
          }
        }
        return identificador_token;
        */

        Reader lector = null;
        lector = new BufferedReader(new FileReader("C:\\Users\\rolus\\OneDrive\\Escritorio\\git\\Compilador\\CodigoCompilador\\src\\archivosTxt\\CodigoPruebaParser.txt"));
  
        lector.mark(1);
        int value = lector.read();
        lector.reset();
        
        while(!(value == -1)){
          lector.mark(1);
            char next_char = (char) lector.read();
            lector.reset();
            
            int identificador_token = AnalizadorLexico.proximoEstado(lector, next_char);
            if (identificador_token != 0) {
              //AnalizadorLexico.token_actual.delete(0, AnalizadorLexico.token_actual.length());
              //ids.add(identificador_token);
              //System.out.println("EL TOKEN ENCONTRADO ES MAIN: " + identificador_token);
              return identificador_token;
            }           
            
            lector.mark(1);
            value = lector.read();
            lector.reset();
      
        }
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