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
    0,    1,    2,    3,    3,    4,    4,    5,    7,    8,
    8,    8,    9,    9,    6,    6,    6,    6,   10,   10,
   14,   14,   14,   16,   16,   11,   11,   17,   17,   17,
   17,   17,   17,   18,   18,   12,   13,   19,   15,   15,
   15,   20,   20,   20,   21,   21,   21,   22,
};
final static short yylen[] = {                            2,
    1,    4,    1,    2,    1,    1,    1,    2,    2,    1,
    1,    1,    3,    1,    2,    2,    2,    2,    3,    3,
    4,    1,    1,    5,    4,   13,    8,    3,    3,    3,
    3,    3,    3,    2,    1,    2,    1,    8,    3,    3,
    1,    3,    3,    1,    2,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    3,    0,    1,    0,    0,    0,    0,    0,   10,   11,
   12,    0,    0,    5,    6,    7,    0,    0,    0,    0,
    0,    0,   37,    0,    0,    0,   36,    0,    2,    4,
    8,   14,    0,   15,   16,   17,   18,    0,   48,    0,
   19,    0,   23,    0,   44,   47,   20,    0,    0,    0,
    0,    0,    0,   45,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   13,    0,    0,
    0,    0,    0,   42,   43,    0,    0,    0,    0,    0,
    0,    0,    0,   25,    0,   21,   35,    0,    0,   24,
    0,   34,    0,    0,   27,   38,    0,    0,    0,    0,
   26,
};
final static short yydgoto[] = {                          2,
    3,    4,   13,   14,   15,   87,   17,   18,   33,   19,
   20,   21,   22,   41,   49,   43,   50,   88,   23,   44,
   45,   46,
};
final static short yysindex[] = {                      -252,
    0,    0,    0,  -95, -192,  -15,    4, -208,    0,    0,
    0,   18, -123,    0,    0,    0,   12, -190,   30,   50,
   53,   54,    0, -224, -167, -187,    0, -187,    0,    0,
    0,    0,   40,    0,    0,    0,    0,  -40,    0,   60,
    0,    2,    0,    6,    0,    0,    0, -182,  -19,   61,
   62, -152,  -37,    0, -187, -187, -187, -187, -187, -187,
 -187, -187, -187, -187, -187,  -17, -164,    0,   64,   43,
   46,    6,    6,    0,    0,    2,    2,    2,    2,    2,
    2, -191,  -13,    0,   67,    0,    0, -122, -191,    0,
  -43,    0, -108,  -11,    0,    0, -191, -103, -150,   71,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   72,    0,    0,    0,    0,   38,    0,    0,
    0,   73,    0,  -30,    0,    0,    0,  -35,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -25,   -5,    0,    0,   77,   78,   79,   80,   81,
   82,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  111,    0,   16,    0,    0,    0,  100,
    0,    0,    0,    0,   -1,    0,   98,  -47,    0,   36,
   37,    0,
};
final static int YYTABLESIZE=277;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         53,
   95,   29,   91,   69,    1,   46,   46,   46,   46,   46,
   41,   46,   41,   41,   41,   39,   96,   39,   39,   39,
   16,   99,   42,   56,   46,   57,   46,    5,   16,   41,
   25,   41,   38,   39,   39,   40,   39,   40,   40,   40,
   61,   93,   60,   26,   56,   24,   57,   58,   40,   98,
   27,   70,   59,   71,   40,   31,   40,   28,   76,   77,
   78,   79,   80,   81,    6,    6,   32,    7,    7,   48,
   39,    8,    8,   34,    9,   10,   11,   12,   12,   46,
   46,   46,   46,   85,   46,   56,   86,   57,   56,    6,
   57,   72,   73,   35,   74,   75,   36,   37,   52,   55,
   54,   66,   67,   92,   68,   82,   83,   84,   92,   89,
   90,   97,  100,   92,  101,    9,   22,   28,   29,   30,
   31,   32,   33,   30,   47,   51,    0,    0,    0,    0,
    0,    0,    0,    6,    6,    0,    7,    7,    0,    0,
    8,    8,    0,    9,   10,   11,   12,   12,    6,    0,
    0,    7,    0,    6,    0,    8,    7,    0,    0,    0,
    8,   12,    0,    0,    0,    0,   12,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   94,   48,
   39,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   54,   46,   46,   46,   46,    0,   41,   41,
   41,   41,    0,   39,   39,   39,   39,    0,    0,   62,
   63,   64,   65,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   40,   40,   40,   40,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   44,  125,  125,   41,  257,   41,   42,   43,   44,   45,
   41,   47,   43,   44,   45,   41,  125,   43,   44,   45,
    5,  125,   24,   43,   60,   45,   62,  123,   13,   60,
   46,   62,  257,  258,   60,   41,   62,   43,   44,   45,
   60,   89,   62,   40,   43,   61,   45,   42,  273,   97,
  259,   53,   47,   55,   60,   44,   62,   40,   60,   61,
   62,   63,   64,   65,  257,  257,  257,  260,  260,  257,
  258,  264,  264,   44,  267,  268,  269,  270,  270,   42,
   43,   44,   45,   41,   47,   43,   41,   45,   43,  257,
   45,   56,   57,   44,   58,   59,   44,   44,   59,   40,
  283,   41,   41,   88,  257,  123,  271,   44,   93,  123,
   44,  123,  263,   98,   44,   44,   44,   41,   41,   41,
   41,   41,   41,   13,   25,   28,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  257,   -1,  260,  260,   -1,   -1,
  264,  264,   -1,  267,  268,  269,  270,  270,  257,   -1,
   -1,  260,   -1,  257,   -1,  264,  260,   -1,   -1,   -1,
  264,  270,   -1,   -1,   -1,   -1,  270,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  262,  257,
  258,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
"declaracion_variables : tipo lista_de_variables",
"tipo : LONG",
"tipo : UINT",
"tipo : FLOAT",
"lista_de_variables : lista_de_variables ';' ID",
"lista_de_variables : ID",
"sentencias_ejecutables : sentencia_asignacion ','",
"sentencias_ejecutables : sentencias_IF ','",
"sentencias_ejecutables : sentencias_salida ','",
"sentencias_ejecutables : sentencias_control ','",
"sentencia_asignacion : ID '=' valor_asignacion",
"sentencia_asignacion : ID '.' sentencia_asignacion",
"valor_asignacion : TOF '(' expr_aritmetic ')'",
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

//#line 119 "gramatica.y"

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


//#line 366 "Parser.java"
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
//#line 22 "gramatica.y"
{System.out.println("ARRANCO EL PROGRAMA");}
break;
case 2:
//#line 25 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa ");}
break;
case 6:
//#line 35 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia declarativa");}
break;
case 7:
//#line 36 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia ejecutable");}
break;
case 10:
//#line 45 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");}
break;
case 11:
//#line 46 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");}
break;
case 12:
//#line 47 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");}
break;
case 13:
//#line 50 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio lista VAR");}
break;
case 14:
//#line 51 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio UNA SOLA VARIABLE");}
break;
case 19:
//#line 62 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion");}
break;
case 24:
//#line 71 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
break;
case 25:
//#line 72 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a una funcion sin parametro");}
break;
case 28:
//#line 79 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor");}
break;
case 29:
//#line 80 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor");}
break;
case 30:
//#line 81 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor o igual");}
break;
case 31:
//#line 82 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor o igual");}
break;
case 32:
//#line 83 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion de igualdad");}
break;
case 33:
//#line 84 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por distinto?");}
break;
case 36:
//#line 91 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una cadena");}
break;
case 37:
//#line 94 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia While");}
break;
case 39:
//#line 100 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una suma");}
break;
case 40:
//#line 101 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una resta");}
break;
case 42:
//#line 105 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una multiplicacion");}
break;
case 43:
//#line 106 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una division");}
break;
case 46:
//#line 111 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador");}
break;
case 48:
//#line 115 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una constante");}
break;
//#line 619 "Parser.java"
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
