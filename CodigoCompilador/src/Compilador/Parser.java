//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package Compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
//#line 23 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short IF=260;
public final static short THEN=261;
public final static short ELSE=262;
public final static short END_IF=263;
public final static short PRINT=264;
public final static short CLASS=265;
public final static short VOID=266;
public final static short LONG=267;
public final static short UINT=268;
public final static short FLOAT=269;
public final static short WHILE=270;
public final static short DO=271;
public final static short RETURN=272;
public final static short TOF=273;
public final static short MAYOR_IGUAL=274;
public final static short MENOR_IGUAl=275;
public final static short IGUAL_IGUAL=276;
public final static short EXCLAMACION_EXCLAMACION=277;
public final static short MENOS_MENOS=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    2,    3,    3,    4,    4,    5,    5,    5,
    5,    7,    8,    8,   11,   11,   11,   12,   12,   13,
   13,   14,    9,    9,   15,   10,   16,   16,    6,    6,
    6,    6,   17,   17,   21,   21,   23,   23,   18,   18,
   24,   24,   24,   24,   24,   24,   25,   25,   19,   20,
   26,   22,   22,   22,   27,   27,   27,   28,   28,   28,
   29,
};
final static short yylen[] = {                            2,
    1,    4,    1,    2,    1,    1,    1,    2,    1,    1,
    1,    2,    9,    7,    1,    1,    1,    3,    1,    1,
    1,    2,    5,    3,    1,    3,    3,    1,    2,    2,
    2,    2,    3,    2,    1,    1,    5,    4,   13,    8,
    3,    3,    3,    3,    3,    3,    2,    1,    2,    1,
    8,    3,    3,    1,    3,    3,    1,    2,    1,    1,
    1,
};
final static short yydefred[] = {                         0,
    3,    0,    1,    0,    0,    0,    0,    0,    0,    0,
   15,   16,   17,    0,    0,    5,    6,    7,    0,    9,
   10,   11,    0,    0,    0,    0,    0,   50,   28,    0,
   34,    0,    0,   49,    0,    0,    0,    2,    4,    8,
   19,    0,   29,   30,   31,   32,    0,   61,   33,    0,
   36,    0,   57,   60,   26,    0,    0,    0,    0,    0,
   24,    0,    0,    0,    0,   58,    0,    0,    0,    0,
   27,    0,    0,    0,    0,    0,    0,    0,   25,    0,
    0,    0,    0,   18,    0,    0,    0,    0,   55,   56,
    0,    0,    0,    0,    0,    0,    0,   23,    0,    0,
    0,   38,    0,    0,   48,    0,    0,   20,    0,   21,
    0,    0,   37,    0,   47,   22,   14,    0,    0,    0,
   40,    0,   51,    0,   13,    0,    0,    0,   39,
};
final static short yydgoto[] = {                          2,
    3,    4,   15,  108,   17,   18,   19,   20,   21,   22,
   23,   42,  109,  110,   80,   32,   24,   25,   26,   27,
   49,   58,   51,   59,  106,   28,   52,   53,   54,
};
final static short yysindex[] = {                      -252,
    0,    0,    0,  -81, -147,  -17,   16, -213, -191, -181,
    0,    0,    0,   49, -122,    0,    0,    0,   50,    0,
    0,    0, -164,   54,   57,   58,   68,    0,    0, -196,
    0,  -10, -172,    0,  -23,   74, -172,    0,    0,    0,
    0,   56,    0,    0,    0,    0,  -40,    0,    0,    7,
    0,    6,    0,    0,    0, -141, -159,  -19,   84, -147,
    0,  -39,   85, -130,  -37,    0, -172, -172, -172, -172,
    0, -172, -172, -172, -172, -172, -172,    5,    0,    4,
    8, -127, -139,    0,   89,   24,    6,    6,    0,    0,
    7,    7,    7,    7,    7,    7, -206,    0, -161,   93,
   13,    0,   95,   -1,    0, -108,   96,    0,   12,    0,
   27, -206,    0,  -43,    0,    0,    0, -161, -103,   28,
    0,   30,    0, -206,    0,  -94, -110,   97,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  114,    0,    0,    0,    0,   37,    0,    0,  115,
    0,  -30,    0,    0,    0,    0,  -35,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -25,   -5,    0,    0,
  119,  123,  124,  127,  128,  130,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,   18,    0,  -29,    0,    0,    0,    0,
  110,    0,   55,    0,    0,    0,    0,    0,    0,    0,
    0,   -2,    0,  137,  -65,    0,   20,   22,    0,
};
final static int YYTABLESIZE=277;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         65,
  121,   81,   38,   85,    1,   59,   59,   59,   59,   59,
   54,   59,   54,   54,   54,   52,  114,   52,   52,   52,
   61,  123,   16,   67,   59,   68,   59,   50,   31,   54,
  127,   54,   39,   55,   52,   53,   52,   53,   53,   53,
   73,    5,   72,   30,   31,   34,  119,   69,   56,   67,
  104,   68,   70,    7,   53,   33,   53,    8,  126,   30,
   47,   48,   86,   14,  103,   35,   67,  105,   68,   91,
   92,   93,   94,   95,   96,   36,  115,   79,   59,   59,
   59,   59,  105,   59,   57,   48,   87,   88,   37,  115,
   89,   90,   41,   40,  105,    6,  115,   43,    7,   60,
   44,   45,    8,    9,   10,   11,   12,   13,   14,    6,
  107,   46,    7,   62,   64,   71,    8,    9,   10,   11,
   12,   13,   14,   66,   78,   83,   84,   97,   98,  100,
   99,  101,  102,  111,    6,  112,  117,    7,  113,  116,
  129,    8,    9,   10,   11,   12,   13,   14,  104,  118,
  124,    7,  128,  104,  125,    8,    7,   12,   35,   41,
    8,   14,  104,   42,   43,    7,   14,   44,   45,    8,
   46,   82,  122,   63,    0,   14,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  120,   57,
   48,    0,    0,    0,    0,    0,    0,   11,   12,   13,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   29,
    0,    0,   66,   59,   59,   59,   59,    0,   54,   54,
   54,   54,    0,   52,   52,   52,   52,    0,    0,   74,
   75,   76,   77,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   53,   53,   53,   53,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   44,   41,  125,   41,  257,   41,   42,   43,   44,   45,
   41,   47,   43,   44,   45,   41,  125,   43,   44,   45,
   44,  125,    5,   43,   60,   45,   62,   30,   46,   60,
  125,   62,   15,   44,   60,   41,   62,   43,   44,   45,
   60,  123,   62,   61,   46,  259,  112,   42,   59,   43,
  257,   45,   47,  260,   60,   40,   62,  264,  124,   61,
  257,  258,   65,  270,   41,  257,   43,   97,   45,   72,
   73,   74,   75,   76,   77,  257,  106,   60,   42,   43,
   44,   45,  112,   47,  257,  258,   67,   68,   40,  119,
   69,   70,  257,   44,  124,  257,  126,   44,  260,  123,
   44,   44,  264,  265,  266,  267,  268,  269,  270,  257,
  272,   44,  260,   40,   59,  257,  264,  265,  266,  267,
  268,  269,  270,  283,   41,   41,  257,  123,  125,  257,
  123,  271,   44,   41,  257,  123,  125,  260,   44,   44,
   44,  264,  265,  266,  267,  268,  269,  270,  257,  123,
  123,  260,  263,  257,  125,  264,  260,   44,   44,   41,
  264,  270,  257,   41,   41,  260,  270,   41,   41,  264,
   41,   62,  118,   37,   -1,  270,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  262,  257,
  258,   -1,   -1,   -1,   -1,   -1,   -1,  267,  268,  269,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
   -1,   -1,  283,  279,  280,  281,  282,   -1,  279,  280,
  281,  282,   -1,  279,  280,  281,  282,   -1,   -1,  279,
  280,  281,  282,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  279,  280,  281,  282,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=283;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CADENA","IF","THEN","ELSE",
"END_IF","PRINT","CLASS","VOID","LONG","UINT","FLOAT","WHILE","DO","RETURN",
"TOF","MAYOR_IGUAL","MENOR_IGUAl","IGUAL_IGUAL","EXCLAMACION_EXCLAMACION",
"MENOS_MENOS","\"MAYOR_IGUAL\"","\"MENOR_IGUAL\"","\"IGUAL_IGUAL\"",
"\"EXCLAMACION_EXCLAMACION\"","\"MENOS_MENOS\"",
};
final static String yyrule[] = {
"$accept : program",
"program : programa",
"programa : nombre_programa '{' bloque_sentencias_programa '}'",
"nombre_programa : ID",
"bloque_sentencias_programa : bloque_sentencias_programa sentencia_programa",
"bloque_sentencias_programa : sentencia_programa",
"sentencia_programa : sentencias_declarativas",
"sentencia_programa : sentencias_ejecutables",
"sentencias_declarativas : declaracion_variables ','",
"sentencias_declarativas : declaracion_funciones",
"sentencias_declarativas : declaracion_clases",
"sentencias_declarativas : declaracion_objetos_clase",
"declaracion_variables : tipo lista_de_variables",
"declaracion_funciones : VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}'",
"declaracion_funciones : VOID ID '(' ')' '{' cuerpo_de_la_funcion '}'",
"tipo : LONG",
"tipo : UINT",
"tipo : FLOAT",
"lista_de_variables : lista_de_variables ';' ID",
"lista_de_variables : ID",
"cuerpo_de_la_funcion : sentencia_programa",
"cuerpo_de_la_funcion : sentencias_retorno",
"sentencias_retorno : RETURN ','",
"declaracion_clases : CLASS ID '{' cuerpo_clase '}'",
"declaracion_clases : CLASS ID ','",
"cuerpo_clase : sentencia_programa",
"declaracion_objetos_clase : ID list_objts_clase ','",
"list_objts_clase : list_objts_clase ';' ID",
"list_objts_clase : ID",
"sentencias_ejecutables : sentencia_asignacion ','",
"sentencias_ejecutables : sentencias_IF ','",
"sentencias_ejecutables : sentencias_salida ','",
"sentencias_ejecutables : sentencias_control ','",
"sentencia_asignacion : ID '=' valor_asignacion",
"sentencia_asignacion : ID '.'",
"valor_asignacion : expr_aritmetic",
"valor_asignacion : invocacion_funcion",
"invocacion_funcion : ID '(' expr_aritmetic ')' ','",
"invocacion_funcion : ID '(' ')' ','",
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF ','",
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ','",
"condicion_if_while : expr_aritmetic '>' expr_aritmetic",
"condicion_if_while : expr_aritmetic '<' expr_aritmetic",
"condicion_if_while : expr_aritmetic \"MAYOR_IGUAL\" expr_aritmetic",
"condicion_if_while : expr_aritmetic \"MENOR_IGUAL\" expr_aritmetic",
"condicion_if_while : expr_aritmetic \"IGUAL_IGUAL\" expr_aritmetic",
"condicion_if_while : expr_aritmetic \"EXCLAMACION_EXCLAMACION\" expr_aritmetic",
"bloque_sentencias_ejecutables : bloque_sentencias_ejecutables sentencias_ejecutables",
"bloque_sentencias_ejecutables : sentencias_ejecutables",
"sentencias_salida : PRINT CADENA",
"sentencias_control : sentencia_while_do",
"sentencia_while_do : WHILE '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}'",
"expr_aritmetic : expr_aritmetic '+' termino",
"expr_aritmetic : expr_aritmetic '-' termino",
"expr_aritmetic : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID \"MENOS_MENOS\"",
"factor : ID",
"factor : const",
"const : CTE",
};

//#line 149 "gramatica.y"

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
//#line 395 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse() throws IOException
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 21 "gramatica.y"
{System.out.println("ARRANCO EL PROGRAMA");}
break;
case 2:
//#line 24 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa ");}
break;
case 6:
//#line 34 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia declarativa");}
break;
case 7:
//#line 35 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia ejecutable");}
break;
case 8:
//#line 40 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de variables");}
break;
case 9:
//#line 41 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de funcion");}
break;
case 10:
//#line 42 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase");}
break;
case 11:
//#line 43 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de objeto de clase");}
break;
case 13:
//#line 49 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");}
break;
case 14:
//#line 50 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
break;
case 15:
//#line 53 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");}
break;
case 16:
//#line 54 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");}
break;
case 17:
//#line 55 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");}
break;
case 22:
//#line 66 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio RETURN");}
break;
case 33:
//#line 92 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion");}
break;
case 37:
//#line 100 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
break;
case 38:
//#line 101 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a una funcion sin parametro");}
break;
case 41:
//#line 108 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor");}
break;
case 42:
//#line 109 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor");}
break;
case 43:
//#line 110 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor o igual");}
break;
case 44:
//#line 111 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor o igual");}
break;
case 45:
//#line 112 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion de igualdad");}
break;
case 46:
//#line 113 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por distinto?");}
break;
case 49:
//#line 120 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una cadena");}
break;
case 50:
//#line 123 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia While");}
break;
case 52:
//#line 129 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una suma");}
break;
case 53:
//#line 130 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una resta");}
break;
case 55:
//#line 134 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una multiplicacion");}
break;
case 56:
//#line 135 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una division");}
break;
case 59:
//#line 140 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador");}
break;
case 61:
//#line 144 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una constante");}
break;
//#line 668 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 * @throws IOException 
 */
public void run() throws IOException
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
