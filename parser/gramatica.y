%{
package Compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
%}

%token ID CTE CADENA IF THEN ELSE END_IF PRINT CLASS VOID LONG UINT FLOAT WHILE DO RETURN TOF 
        MAYOR_IGUAL MENOR_IGUAL IGUAL_IGUAL 
        EXCLAMACION_EXCLAMACION MENOS_MENOS

%left '+' '-'
%left '*' '/'

%start program

%%
//gramatica

program: programa {System.out.println("ARRANCO EL PROGRAMA");}
       ;

programa: nombre_programa '{' bloque_sentencias_programa '}' {System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa ");}
        | nombre_programa '{' bloque_sentencias_programa error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta } al final");}
        | nombre_programa bloque_sentencias_programa '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta { al final");}
        | nombre_programa '{' '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta bloque de sentencias al final");}
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
                       | declaracion_variables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}  
		       		   | declaracion_funciones error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}     
				       | declaracion_clases error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}	   	   
					   | declaracion_objetos_clase error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}   
					   ;

declaracion_variables: tipo lista_de_variables {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de variables");}
				     | lista_de_variables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta el TIPO");}
                     | tipo error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta el identificador");}
                     ;

declaracion_funciones: VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno'}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");}
                     | VOID ID '(' tipo ID ')' '{' sentencias_retorno'}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");} 
                     | VOID ID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
                     | VOID ID '(' ')' '{' sentencias_retorno '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
                     | VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta RETURN en la funcion");}
		     		 | VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '}' en la funcion");}
                     | VOID ID '(' tipo ID ')' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '{' en la funcion");}
                     | VOID ID '(' tipo ID '{' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ')' en la funcion");}
                     | VOID ID tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '(' en la funcion");}
                     | VOID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta nombre en la funcion");}
                     | ID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta la palabra VOID en la funcion");}
                     | VOID ID '(' ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta RETURN en la funcion");}
		     		 | VOID ID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '}' en la funcion");}
                     | VOID ID '(' ')' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '{' en la funcion");}
                     | VOID ID '(' '{' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ')' en la funcion");}
                     | VOID ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '(' en la funcion");}
                     | VOID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta nombre en la funcion");}
                     | ID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta la palabra VOID en la funcion");}
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

sentencias_de_funcion: sentencia_declatariva_especificas
                     | sentencias_ejecutables
                     ;

sentencia_declatariva_especificas: declaracion_variables ','                    
                             | declaracion_funciones ','
                             | declaracion_objetos_clase ','
                             | declaracion_variables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}                     
                             | declaracion_funciones error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
                             | declaracion_objetos_clase error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
                             ;

/*                             
sentencia_funcion_ejecutable: sentencia_asignacion ','
                            | sentencias_IF ','
                            | sentencias_salida ','
                            | sentencias_control ','
                            | sentencias_ejecucion_funcion ','
                            | sentencia_asignacion error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
                            | sentencias_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
                            | sentencias_salida error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
                            | sentencias_control error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
                            | sentencias_ejecucion_funcion error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
                            ;
*/

sentencias_retorno: RETURN ',' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio RETURN");}
                  | RETURN error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
				  ;

declaracion_clases: CLASS ID '{' cuerpo_clase '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase");}
					//tema  21
				  | CLASS ID {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase a posterior");}
                  | CLASS ID '{' cuerpo_clase error {System.out.println("Error sintectico al compilar no permite clase con cuerpos que no esten entre {} ");}
                  | CLASS ID cuerpo_clase '}' error {System.out.println("Error sintectico al compilar no permite clase con cuerpos que no esten entre {} ");}
                  | CLASS ID '{' '}' error {System.out.println("Error sintectico al compilar no permite declarar clases vacias");}
                  | CLASS '{' cuerpo_clase '}' error {System.out.println("Error sintectico al compilar no permite clase sin ID");}
				  ;

cuerpo_clase: cuerpo_clase sentencia_declatariva_especificas
            | sentencia_declatariva_especificas
            | sentencias_ejecutables error {System.out.println("Error sintectico al compilar no permite declarar sentencia ejecutables");}
            | declaracion_clases error {System.out.println("Error sintectico al compilar no permite declarar una clase dentro de otra");}
            ;

declaracion_objetos_clase: ID list_objts_clase  {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de objeto de clase");}
						 ;

list_objts_clase: list_objts_clase ';' ID
				| ID
                | list_objts_clase '.' ID error {System.out.println("Error sintectico al compilar no permite objetos de clase separados por .");}
                | list_objts_clase ID error {System.out.println("Error sintectico al compilar no permite objetos de clase separados por ,");}
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
                    | ID '.' sentencia_asignacion
                    ;

valor_asignacion: TOF '(' expr_aritmetic ')' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion con conversion de tipo");}
                | expr_aritmetic                            
                ;

invocacion_funcion: ID '(' expr_aritmetic ')' ',' {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}      
                  | ID '('')' ',' {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a una funcion sin parametro");}
                  ;

sentencias_IF: IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF_ELSE");}
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' END_IF {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF");}
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta END_IF para finalizar la sentencia IF");}
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra ELSE");}
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra IF");}
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabras IF y ELSE");}
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabras IF Y END_IF");}
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabras ELSE Y END_IF");}
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabras IF, ELSE Y END_IF");}
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta bloque sentencias ejecutables");}
             | IF '(' condicion_if_while ')' '{'  '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta bloque sentencias ejecutables");}
             | IF '(' condicion_if_while ')' '{'  '}' ELSE '{' '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta bloques sentencias ejecutables");}
             | IF '('  ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta condicion");}
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}'  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra END_IF");}
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' END_IF  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico .Falta palabra IF");}
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}'  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico .Falta palabras IF y END_IF");}
             | IF '(' condicion_if_while ')' '{'  '}' END_IF  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico .Falta bloque sentencias ejecutables");}
             | IF '('  ')' '{' bloque_sentencias_ejecutables '}' END_IF  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta condicion");}
             ;

condicion_if_while: expr_aritmetic '>' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor");}
            | expr_aritmetic '<' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor");}
            | expr_aritmetic MAYOR_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor o igual");}
            | expr_aritmetic MENOR_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor o igual");}
            | expr_aritmetic IGUAL_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion de igualdad");}
            | expr_aritmetic EXCLAMACION_EXCLAMACION expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por distinto?");}
            ;

bloque_sentencias_ejecutables: bloque_sentencias_ejecutables sentencias_ejecutables  
                             | sentencias_ejecutables
                             ;

sentencias_salida: PRINT CADENA {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una cadena");}
                 ;

sentencias_control: sentencia_while_do {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia While");}
                  ;

sentencia_while_do: WHILE '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}' 
                  | WHILE '(' condicion_if_while ')' DO '{' '}' 
                  | WHILE '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra DO");}
                  | '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra WHILE");}
                  | WHILE '('  ')' DO '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta condicion");}
                  ;

expr_aritmetic: expr_aritmetic '+' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una suma");}
	    	  | expr_aritmetic '-' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una resta");}
              | termino
              ;

termino: termino '*' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una multiplicacion");}
       | termino '/' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una division");}
       | factor
       | invocacion_funcion  
       ;

factor: ID MENOS_MENOS {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador con MENOS_MENOS");} 
      | ID {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador");}
      | const
      ;

const: CTE {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una constante");}
     | '-'CTE {ConstanteNegativa($2.sval);}
     ;                   

//fin gramatica
%%

//funciones

public static ArrayList<String> errores = new ArrayList<String>();

public static void agregarError(String nuevoError){
    errores.add(nuevoError);
}

public static ArrayList<String> getErrores(){
    return errores;
}

public static void ConstanteNegativa(String lexema){
  	TablaDeSimbolos.agregarSimbolo(lexema, Constantes.CTE);
}

int yylex() throws IOException {
    int identificador_token = 0;
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
            return identificador_token;
        }

        lector.mark(1);
        value = lector.read();
        lector.reset();
    }
    return identificador_token;
}

void yyerror(String error) {
    //System.out.println("Yacc reporto un error: " + error);
}