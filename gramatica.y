%{
package Compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
%}

%token ID CTE CADENA IF THEN ELSE END_IF PRINT CLASS VOID LONG UINT FLOAT WHILE DO RETURN TOF 
        MAYOR_IGUAL MENOR_IGUAl IGUAL_IGUAL 
        EXCLAMACION_EXCLAMACION MENOS_MENOS

%left '+' '-'
%left '*' '/'

%start program

%%
//gramatica

program: programa {System.out.println("ARRANCO EL PROGRAMA");}
       ;

programa: nombre_programa '{' bloque_sentencias_programa '}' {System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa ");}
        ;

nombre_programa: ID
	       ;

bloque_sentencias_programa: bloque_sentencias_programa sentencia_programa
        		          | sentencia_programa
        		          ;

sentencia_programa: sentencias_declarativas {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia declarativa");}
        	      | sentencias_ejecutables	{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia ejecutable");}
				  ;

//declarativas

sentencias_declarativas: declaracion_variables ',' 
		       		   | declaracion_funciones ','    
				       | declaracion_clases	   ','	   
					   | declaracion_objetos_clase ',' 
					   ;

declaracion_variables: tipo lista_de_variables {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de variables");}
				     ;

declaracion_funciones: VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno'}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");}
                     | VOID ID '(' tipo ID ')' '{' sentencias_retorno'}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");} 
                     | VOID ID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
                     | VOID ID '(' ')' '{' sentencias_retorno '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
                     
		     		 ;

tipo: LONG {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");}
	| UINT {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");}
	| FLOAT {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");}
	;

lista_de_variables: lista_de_variables ';' ID
				  | ID
				  ;

cuerpo_de_la_funcion: cuerpo_de_la_funcion sentencias_de_funcion 
					| sentencias_de_funcion
					;

sentencias_de_funcion: sentencia_funcion_declarativa
                     | sentencia_funcion_ejecutable
                     ;

sentencia_funcion_declarativa: declaracion_variables ','                    
                             | declaracion_funciones ','
                             | declaracion_objetos_clase ','
                             ;
                             
sentencia_funcion_ejecutable: sentencia_asignacion ','
                            | sentencias_IF ','
                            | sentencias_salida ','
                            | sentencias_control ','
                            | sentencias_ejecucion_funcion ','
                            ;


sentencias_retorno: RETURN ',' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio RETURN");}
				  ;

declaracion_clases: CLASS ID '{' cuerpo_clase '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase");}
					//tema 21
				  | CLASS ID ',' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase a posterior");}
				  ;

cuerpo_clase: sentencia_programa
			;

declaracion_objetos_clase: ID list_objts_clase ',' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de objeto de clase");}
						 ;

list_objts_clase: list_objts_clase ';' ID
				| ID
				;

//ejecutables 
sentencias_ejecutables: sentencia_asignacion ',' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia Ejecutables");}
                      | sentencias_IF ','
                      | sentencias_salida ','
                      | sentencias_control ','
                      | sentencias_ejecucion_funcion ','
                      ;

sentencias_ejecucion_funcion: ID '(' expr_aritmetic ')' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
                            | ID '(' ')' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion sin parametro");}
                            ;

sentencia_asignacion: ID '=' valor_asignacion {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion");}
                    | ID '.' //sentencia_asignacion //???
                    ;

valor_asignacion: expr_aritmetic                            //TOF '(' expr_aritmetic ')' //tema 29. nose si esta bien
                | invocacion_funcion  //PONER A NIVEL DE LOS TERMINOS
                ;

invocacion_funcion: ID '(' expr_aritmetic ')' ',' {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}      
                  | ID '('')' ',' {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a una funcion sin parametro");}
                  ;

sentencias_IF: IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF ','
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ','
             ;

condicion_if_while: expr_aritmetic '>' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor");}
            | expr_aritmetic '<' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor");}
            | expr_aritmetic 'MAYOR_IGUAL' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor o igual");}
            | expr_aritmetic 'MENOR_IGUAL' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor o igual");}
            | expr_aritmetic 'IGUAL_IGUAL' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion de igualdad");}
            | expr_aritmetic 'EXCLAMACION_EXCLAMACION' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por distinto?");}
            ;

bloque_sentencias_ejecutables: bloque_sentencias_ejecutables sentencias_ejecutables  
                             | sentencias_ejecutables
                             ;

sentencias_salida: PRINT CADENA {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una cadena");}
                 ;

sentencias_control: sentencia_while_do {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia While");}
                  ;

sentencia_while_do: WHILE '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}' 
                  ;

expr_aritmetic: expr_aritmetic '+' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una suma");}
	    	  | expr_aritmetic '-' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una resta");}
              | termino
              ;

termino: termino '*' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una multiplicacion");}
       | termino '/' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una division");}
       | factor
       ;

factor: ID 'MENOS_MENOS' //NOSE SI ESTA BIEN
      | ID {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador");}
      | const
      ;

const: CTE {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una constante");}
     ;                  

//fin gramatica
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
            //yylval = new ParserVal(AnalizadorLexico.getLexemaActual());
            //System.out.println("GR176. IDENTIFICADOR: " + identificador_token + " Lexema: " + AnalizadorLexico.getLexemaActual()); 
            return identificador_token;
        }

        lector.mark(1);
        value = lector.read();
        lector.reset();
    }
    return identificador_token;
}

void yyerror(String error) {
    // funcion utilizada para imprimir errores que produce yacc. Sin esto me sale error en yacc
    System.out.println("Yacc reporto un error: " + error);
}