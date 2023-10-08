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
public final static short MENOR_IGUAL=275;
public final static short IGUAL_IGUAL=276;
public final static short EXCLAMACION_EXCLAMACION=277;
public final static short MENOS_MENOS=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    3,    3,    4,    4,    5,    5,
    5,    5,    7,    8,    8,    8,    8,   11,   11,   11,
   12,   12,   13,   13,   15,   15,   16,   16,   16,   14,
    9,    9,    9,    9,    9,    9,    9,   17,   17,   17,
   17,   10,   18,   18,   18,   18,   18,    6,    6,    6,
    6,    6,   23,   23,   19,   19,   25,   25,   26,   26,
   20,   20,   27,   27,   27,   27,   27,   27,   28,   28,
   21,   22,   29,   29,   24,   24,   24,   30,   30,   30,
   30,   31,   31,   31,   32,   32,
};
final static short yylen[] = {                            2,
    1,    4,    4,    1,    2,    1,    1,    1,    2,    2,
    2,    2,    2,   10,    9,    8,    7,    1,    1,    1,
    3,    1,    2,    1,    1,    1,    2,    2,    2,    2,
    5,    2,    5,    5,    5,    5,    2,    2,    1,    2,
    2,    2,    3,    1,    4,    4,    3,    2,    2,    2,
    2,    2,    4,    3,    3,    3,    4,    1,    5,    4,
   12,    8,    3,    3,    3,    3,    3,    3,    2,    1,
    2,    1,    8,    7,    3,    3,    1,    3,    3,    1,
    1,    2,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    4,    0,    1,    0,    0,    0,    0,    0,    0,    0,
   18,   19,   20,    0,    0,    6,    7,    8,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   72,   44,
    0,    0,    0,    0,    0,   71,   37,    0,    0,    0,
    0,    3,    2,    5,    9,   10,   11,   12,   22,    0,
   48,   49,   50,   51,   52,    0,   85,    0,   54,    0,
   81,    0,   80,   84,    0,   56,    0,    0,   55,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   39,    0,    0,    0,    0,    0,   82,    0,   86,
    0,    0,   53,    0,    0,    0,   47,    0,   43,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   40,
   27,   28,   41,   29,    0,    0,   38,    0,    0,    0,
    0,   21,    0,    0,    0,    0,    0,   78,   79,    0,
   45,   46,    0,    0,    0,    0,    0,    0,    0,   35,
   33,   31,   34,   36,    0,    0,    0,   60,    0,   57,
    0,   70,    0,    0,   26,    0,    0,   24,   25,    0,
    0,   59,    0,   69,   30,    0,   23,   17,    0,   74,
    0,    0,   62,   16,    0,    0,   73,    0,    0,   15,
    0,   14,    0,   61,
};
final static short yydgoto[] = {                          2,
    3,    4,   15,   16,   17,  155,   78,   79,   80,   81,
   23,   50,  156,  157,  158,  159,   83,   34,   24,   25,
   26,   27,   28,   74,   69,   61,   75,  153,   29,   62,
   63,   64,
};
final static short yysindex[] = {                      -201,
    0,    0,    0,  -10,   44,    2,   53, -176,  -77, -171,
    0,    0,    0,   75, -103,    0,    0,    0,   80,   84,
   90,   96, -140,  108,  111,  114,  129,  130,    0,    0,
  -24, -138,  -44,    6,  -42,    0,    0, -121,   44,  135,
  -42,    0,    0,    0,    0,    0,    0,    0,    0,  117,
    0,    0,    0,    0,    0,  -40,    0,  -81,    0,   65,
    0,   49,    0,    0,   23,    0,  138,   47,    0,  -75,
  -74,  -73,  -71,  -19,  146,  -72,  -67,  147,  155,  -55,
  158,    0,  -46,   31,  -37,  162,  -53,    0,    4,    0,
  -42,  -42,    0,  -22,  -22,  -42,    0,  -50,    0,  -47,
  -42,  -42,  -42,  -42,  -42,  -42,  101,  -31,  -97,    0,
    0,    0,    0,    0,  -38,  -29,    0,  -28,  103,  -20,
  -23,    0,  199,   66,   49,   49,  -18,    0,    0,   71,
    0,    0,   47,   47,   47,   47,   47,   47, -175,    0,
    0,    0,    0,    0, -137,  212,  131,    0,  220,    0,
   14,    0,  -52,  221,    0, -137,  141,    0,    0,  144,
   16,    0, -165,    0,    0,  143,    0,    0, -137,    0,
   17,  152,    0,    0, -137,  153,    0, -175,  154,    0,
   25,    0,   20,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -39,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  240,
    0,    0,    0,    0,    0,  -35,    0,    0,    0,    0,
    0,  -30,    0,    0,    0,    0,    0,  246,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -25,   -5,  -35,    0,    0,    0,
    0,    0,  250,  251,  252,  253,  255,  261,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  288,    0,   29,   46,   57,   59,   73,
  222,    0,  136,  -98, -109,   42,   -6,    0,  274,    0,
    0,    0,    0,   -2,    0,    0,  275, -102,    0,   30,
   43,    0,
};
final static int YYTABLESIZE=316;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         89,
   58,   76,   58,  119,   32,   83,   83,   83,   83,   83,
   77,   83,   77,   77,   77,   75,   59,   75,   75,   75,
   58,   43,   58,   91,   83,   92,   83,  142,   60,   77,
   68,   77,   84,   18,   75,   76,   75,   76,   76,   76,
  106,   31,  105,   18,  123,   39,  167,   32,   58,   71,
   19,   73,  108,   31,   76,    1,   76,  166,  171,   32,
   19,   20,   33,   21,   72,  167,   77,   77,   32,  109,
  176,   20,  163,   21,   33,  181,  179,   22,  116,   82,
   82,  151,   36,   33,    7,   40,  124,   22,    8,   91,
   94,   92,   35,  130,   14,   95,  172,  173,  133,  134,
  135,  136,  137,  138,   77,   93,  149,   91,   91,   92,
   92,  150,    5,   91,   41,   92,   49,   82,   65,    6,
  125,  126,    7,   45,  117,  117,    8,   46,   10,   11,
   12,   13,   14,   47,  154,    6,  128,  129,    7,   48,
  170,  177,    8,    9,   10,   11,   12,   13,   14,  183,
  117,   51,   42,    6,   52,  118,    7,   53,  141,  115,
    8,    9,   10,   11,   12,   13,   14,  152,   10,   11,
   12,   13,   54,   55,   85,   87,   90,   96,   37,   38,
   97,  164,   98,   99,    6,  100,  107,    7,  110,  152,
  111,    8,    9,   10,   11,   12,   13,   14,  112,  164,
  113,  114,  121,  122,  151,  131,  152,    7,  132,  164,
  115,    8,   56,   57,   56,   57,   32,   14,   30,   10,
   11,   12,   13,  139,  140,  145,  143,  144,   67,   11,
   12,   13,   56,   57,  127,   57,  146,   88,   83,   83,
   83,   83,  148,   77,   77,   77,   77,  147,   75,   75,
   75,   75,  160,  161,  101,  102,  103,  104,   30,   88,
   56,   57,   70,  162,  165,  168,  169,  174,   76,   76,
   76,   76,  151,  151,  178,    7,    7,  180,  182,    8,
    8,  151,  184,   13,    7,   14,   14,  115,    8,   58,
   65,   66,   67,   68,   14,   63,   10,   11,   12,   13,
    6,   64,   44,    7,  175,   66,  120,    8,    9,   10,
   11,   12,   13,   14,    0,   86,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   45,  123,   45,   41,   44,   41,   42,   43,   44,   45,
   41,   47,   43,   44,   45,   41,   41,   43,   44,   45,
   45,  125,   45,   43,   60,   45,   62,  125,   31,   60,
   33,   62,   39,    5,   60,   41,   62,   43,   44,   45,
   60,   40,   62,   15,   41,  123,  156,   46,   45,   44,
    5,   46,  125,   40,   60,  257,   62,  156,  161,   46,
   15,    5,   61,    5,   59,  175,   38,   39,   46,   76,
  169,   15,  125,   15,   61,  178,  175,    5,  125,   38,
   39,  257,  259,   61,  260,  257,   89,   15,  264,   43,
   42,   45,   40,   96,  270,   47,  262,  263,  101,  102,
  103,  104,  105,  106,   76,   41,   41,   43,   43,   45,
   45,   41,  123,   43,   40,   45,  257,   76,  257,  257,
   91,   92,  260,   44,   83,   84,  264,   44,  266,  267,
  268,  269,  270,   44,  272,  257,   94,   95,  260,   44,
  125,  125,  264,  265,  266,  267,  268,  269,  270,  125,
  109,   44,  256,  257,   44,  125,  260,   44,  256,  257,
  264,  265,  266,  267,  268,  269,  270,  139,  266,  267,
  268,  269,   44,   44,   40,   59,  258,   40,  256,  257,
  256,  153,  257,  257,  257,  257,   41,  260,  256,  161,
   44,  264,  265,  266,  267,  268,  269,  270,   44,  171,
  256,   44,   41,  257,  257,  256,  178,  260,  256,  181,
  257,  264,  257,  258,  257,  258,  256,  270,  257,  266,
  267,  268,  269,  123,  256,  123,  256,  256,  273,  267,
  268,  269,  257,  258,  257,  258,  257,  278,  274,  275,
  276,  277,   44,  274,  275,  276,  277,  271,  274,  275,
  276,  277,   41,  123,  274,  275,  276,  277,  257,  278,
  257,  258,  257,   44,   44,  125,  123,  125,  274,  275,
  276,  277,  257,  257,  123,  260,  260,  125,  125,  264,
  264,  257,  263,   44,  260,  270,  270,  257,  264,   44,
   41,   41,   41,   41,  270,   41,  266,  267,  268,  269,
  257,   41,   15,  260,  169,   32,   85,  264,  265,  266,
  267,  268,  269,  270,   -1,   41,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=278;
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
"TOF","MAYOR_IGUAL","MENOR_IGUAL","IGUAL_IGUAL","EXCLAMACION_EXCLAMACION",
"MENOS_MENOS",
};
final static String yyrule[] = {
"$accept : program",
"program : programa",
"programa : nombre_programa '{' bloque_sentencias_programa '}'",
"programa : nombre_programa '{' bloque_sentencias_programa error",
"nombre_programa : ID",
"bloque_sentencias_programa : bloque_sentencias_programa sentencia_programa",
"bloque_sentencias_programa : sentencia_programa",
"sentencia_programa : sentencias_declarativas",
"sentencia_programa : sentencias_ejecutables",
"sentencias_declarativas : declaracion_variables ','",
"sentencias_declarativas : declaracion_funciones ','",
"sentencias_declarativas : declaracion_clases ','",
"sentencias_declarativas : declaracion_objetos_clase ','",
"declaracion_variables : tipo lista_de_variables",
"declaracion_funciones : VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}'",
"declaracion_funciones : VOID ID '(' tipo ID ')' '{' sentencias_retorno '}'",
"declaracion_funciones : VOID ID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno '}'",
"declaracion_funciones : VOID ID '(' ')' '{' sentencias_retorno '}'",
"tipo : LONG",
"tipo : UINT",
"tipo : FLOAT",
"lista_de_variables : lista_de_variables ';' ID",
"lista_de_variables : ID",
"cuerpo_de_la_funcion : cuerpo_de_la_funcion sentencias_de_funcion",
"cuerpo_de_la_funcion : sentencias_de_funcion",
"sentencias_de_funcion : sentencia_declatariva_especificas",
"sentencias_de_funcion : sentencias_ejecutables",
"sentencia_declatariva_especificas : declaracion_variables ','",
"sentencia_declatariva_especificas : declaracion_funciones ','",
"sentencia_declatariva_especificas : declaracion_objetos_clase ','",
"sentencias_retorno : RETURN ','",
"declaracion_clases : CLASS ID '{' cuerpo_clase '}'",
"declaracion_clases : CLASS ID",
"declaracion_clases : CLASS ID '{' cuerpo_clase error",
"declaracion_clases : CLASS ID cuerpo_clase '}' error",
"declaracion_clases : CLASS ID '{' '}' error",
"declaracion_clases : CLASS '{' cuerpo_clase '}' error",
"declaracion_clases : CLASS error",
"cuerpo_clase : cuerpo_clase sentencia_declatariva_especificas",
"cuerpo_clase : sentencia_declatariva_especificas",
"cuerpo_clase : sentencias_ejecutables error",
"cuerpo_clase : declaracion_clases error",
"declaracion_objetos_clase : ID list_objts_clase",
"list_objts_clase : list_objts_clase ';' ID",
"list_objts_clase : ID",
"list_objts_clase : list_objts_clase ',' ID error",
"list_objts_clase : list_objts_clase '.' ID error",
"list_objts_clase : list_objts_clase ID error",
"sentencias_ejecutables : sentencia_asignacion ','",
"sentencias_ejecutables : sentencias_IF ','",
"sentencias_ejecutables : sentencias_salida ','",
"sentencias_ejecutables : sentencias_control ','",
"sentencias_ejecutables : sentencias_ejecucion_funcion ','",
"sentencias_ejecucion_funcion : ID '(' expr_aritmetic ')'",
"sentencias_ejecucion_funcion : ID '(' ')'",
"sentencia_asignacion : ID '=' valor_asignacion",
"sentencia_asignacion : ID '.' sentencia_asignacion",
"valor_asignacion : TOF '(' expr_aritmetic ')'",
"valor_asignacion : expr_aritmetic",
"invocacion_funcion : ID '(' expr_aritmetic ')' ','",
"invocacion_funcion : ID '(' ')' ','",
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF",
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' END_IF",
"condicion_if_while : expr_aritmetic '>' expr_aritmetic",
"condicion_if_while : expr_aritmetic '<' expr_aritmetic",
"condicion_if_while : expr_aritmetic MAYOR_IGUAL expr_aritmetic",
"condicion_if_while : expr_aritmetic MENOR_IGUAL expr_aritmetic",
"condicion_if_while : expr_aritmetic IGUAL_IGUAL expr_aritmetic",
"condicion_if_while : expr_aritmetic EXCLAMACION_EXCLAMACION expr_aritmetic",
"bloque_sentencias_ejecutables : bloque_sentencias_ejecutables sentencias_ejecutables",
"bloque_sentencias_ejecutables : sentencias_ejecutables",
"sentencias_salida : PRINT CADENA",
"sentencias_control : sentencia_while_do",
"sentencia_while_do : WHILE '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}'",
"sentencia_while_do : WHILE '(' condicion_if_while ')' DO '{' '}'",
"expr_aritmetic : expr_aritmetic '+' termino",
"expr_aritmetic : expr_aritmetic '-' termino",
"expr_aritmetic : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino : invocacion_funcion",
"factor : ID MENOS_MENOS",
"factor : ID",
"factor : const",
"const : CTE",
"const : '-' CTE",
};

//#line 180 "gramatica.y"

//funciones


public static void ConstanteNegativa(String lexema){
  	Simbolo s = TablaDeSimbolos.obtenerSimbolo(lexema, Constantes.CTE);
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
            //System.out.println("GR176. IDENTIFICADOR: " + identificador_token + " Lexema: " + AnalizadorLexico.getLexemaActual());
            yylval = new ParserVal(AnalizadorLexico.getLexemaActual()); 
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
//#line 444 "Parser.java"
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
case 3:
//#line 25 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite terminar de leer el programa de forma correcta");}
break;
case 7:
//#line 35 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia declarativa");}
break;
case 8:
//#line 36 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia ejecutable");}
break;
case 13:
//#line 47 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de variables");}
break;
case 14:
//#line 50 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");}
break;
case 15:
//#line 51 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");}
break;
case 16:
//#line 52 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
break;
case 17:
//#line 53 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
break;
case 18:
//#line 57 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");}
break;
case 19:
//#line 58 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");}
break;
case 20:
//#line 59 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");}
break;
case 30:
//#line 79 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio RETURN");}
break;
case 31:
//#line 82 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase");}
break;
case 32:
//#line 84 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase a posterior");}
break;
case 33:
//#line 85 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite clase con cuerpos que no esten entre {} ");}
break;
case 34:
//#line 86 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite clase con cuerpos que no esten entre {} ");}
break;
case 35:
//#line 87 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite declarar clases vacias");}
break;
case 36:
//#line 88 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite clase sin ID");}
break;
case 37:
//#line 89 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite clase a posterior sin ID");}
break;
case 40:
//#line 94 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite declarar sentencia ejecutables");}
break;
case 41:
//#line 95 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite declarar una clase dentro de otra");}
break;
case 42:
//#line 98 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de objeto de clase");}
break;
case 45:
//#line 103 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite objetos de clase separados por ,");}
break;
case 46:
//#line 104 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite objetos de clase separados por .");}
break;
case 47:
//#line 105 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite objetos de clase separados por ,");}
break;
case 48:
//#line 109 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia Ejecutables");}
break;
case 53:
//#line 116 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
break;
case 54:
//#line 117 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion sin parametro");}
break;
case 55:
//#line 120 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion");}
break;
case 57:
//#line 124 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion con conversion de tipo");}
break;
case 59:
//#line 128 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
break;
case 60:
//#line 129 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a una funcion sin parametro");}
break;
case 61:
//#line 132 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF_ELSE");}
break;
case 62:
//#line 133 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF");}
break;
case 63:
//#line 136 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor");}
break;
case 64:
//#line 137 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor");}
break;
case 65:
//#line 138 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor o igual");}
break;
case 66:
//#line 139 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor o igual");}
break;
case 67:
//#line 140 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion de igualdad");}
break;
case 68:
//#line 141 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por distinto?");}
break;
case 71:
//#line 148 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una cadena");}
break;
case 72:
//#line 151 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia While");}
break;
case 75:
//#line 158 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una suma");}
break;
case 76:
//#line 159 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una resta");}
break;
case 78:
//#line 163 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una multiplicacion");}
break;
case 79:
//#line 164 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una division");}
break;
case 82:
//#line 169 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador con MENOS_MENOS");}
break;
case 83:
//#line 170 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador");}
break;
case 85:
//#line 174 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una constante");}
break;
case 86:
//#line 175 "gramatica.y"
{ConstanteNegativa(val_peek(0).sval);}
break;
//#line 801 "Parser.java"
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
