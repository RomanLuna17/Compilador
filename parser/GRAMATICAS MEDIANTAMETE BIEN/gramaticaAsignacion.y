%{
package Compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
%}

%token ID CTE CADENA IF THEN ELSE END_IF PRINT CLASS VOID LONG UINT FLOAT WHILE DO RETURN TOF

%left '+' '-'
%left '*' '/'

%start sentencias_declarativas

%%
//gramatica


sentencias_declarativas: declaracion_variables ',' 
                       ;

declaracion_variables: tipo lista_de_variables
				     ;

tipo: LONG {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");}
	| UINT {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");}
	| FLOAT {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");}
	;

lista_de_variables: lista_de_variables ';' ID {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio lista VAR");}
				  | ID {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio UNA SOLA VARIABLE");}
				  ;

%%

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
        
            yylval = new ParserVal(AnalizadorLexico.getLexemaActual());
            //System.out.println("GR176. TOKEN: " + identificador_token + " Lexema: " + AnalizadorLexico.getLexemaActual()); 
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



