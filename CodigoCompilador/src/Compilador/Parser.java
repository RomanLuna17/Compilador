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
package compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

//#line 27 "Parser.java"




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
public final static short OUT=264;
public final static short FUN=265;
public final static short RETURN=266;
public final static short BREAK=267;
public final static short DISCARD=268;
public final static short CONST=269;
public final static short DEFER=270;
public final static short I16=271;
public final static short F32=272;
public final static short IGUAL=273;
public final static short MAYOR_IGUAL=274;
public final static short MENOR_IGUAL=275;
public final static short EXCLAMACION_EXCLAMACION=276;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    2,    3,    3,    4,    4,    5,    6,    6,
    7,   10,   10,   10,   11,   11,    8,   12,   13,    9,
   14,   14,   14,   15,   15,   15,   16,   16,   17,
};
final static short yylen[] = {                            2,
    1,    4,    1,    2,    1,    1,    1,    2,    2,    2,
    2,    1,    1,    1,    3,    1,    3,    1,    1,    4,
    3,    3,    1,    3,    3,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    3,    0,    1,    0,    0,    0,   14,    0,   12,   13,
    0,    5,    6,    7,    0,    0,    0,    0,   18,    0,
    0,    2,    4,    8,    9,   10,   16,    0,   27,   29,
   17,    0,    0,   26,   28,    0,    0,    0,    0,    0,
    0,   20,   15,    0,    0,   24,   25,
};
final static short yydgoto[] = {                          2,
    3,    4,   11,   12,   13,   14,   15,   16,   17,   18,
   28,   20,   31,   32,   33,   34,   35,
};
final static short yysindex[] = {                      -239,
    0,    0,    0,  -99, -256, -246,    0,  -12,    0,    0,
 -125,    0,    0,    0,  -14,  -13,  -11, -228,    0, -237,
 -227,    0,    0,    0,    0,    0,    0,  -10,    0,    0,
    0,  -26,  -40,    0,    0,   -6, -221, -237, -237, -237,
 -237,    0,    0,  -40,  -40,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   -7,  -39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -34,  -31,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,   27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -16,  -15,    0,
};
final static int YYTABLESIZE=147;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
    6,   40,    7,   23,   23,   23,   41,    8,   21,   21,
   21,   22,   22,   22,    9,   10,   38,    1,   39,   29,
   30,   44,   45,    5,   46,   47,   19,   21,   27,   24,
   25,   36,   26,   37,   42,   43,   19,   23,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    6,    0,    7,    0,    0,    0,    0,    8,    0,
    0,    0,    0,    0,    0,    9,   10,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        125,
  257,   42,  259,   43,   44,   45,   47,  264,   43,   44,
   45,   43,   44,   45,  271,  272,   43,  257,   45,  257,
  258,   38,   39,  123,   40,   41,  273,   40,  257,   44,
   44,  259,   44,   44,   41,  257,   44,   11,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,   -1,  259,   -1,   -1,   -1,   -1,  264,   -1,
   -1,   -1,   -1,   -1,   -1,  271,  272,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=276;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CADENA","IF","THEN","ELSE",
"END_IF","OUT","FUN","RETURN","BREAK","DISCARD","CONST","DEFER","I16","F32",
"IGUAL","MAYOR_IGUAL","MENOR_IGUAL","EXCLAMACION_EXCLAMACION",
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
"sentencias_ejecutables : sentencia_asignacion ','",
"sentencias_ejecutables : sentencia_out ','",
"declaracion_variables : tipo list_var",
"tipo : I16",
"tipo : F32",
"tipo : CADENA",
"list_var : list_var ',' ID",
"list_var : ID",
"sentencia_asignacion : ID igual valor_asignacion",
"igual : IGUAL",
"valor_asignacion : expr_aritmetic",
"sentencia_out : OUT '(' CADENA ')'",
"expr_aritmetic : expr_aritmetic '+' termino",
"expr_aritmetic : expr_aritmetic '-' termino",
"expr_aritmetic : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : const",
"const : CTE",
};

//#line 104 "gramatica.y"
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
              return identificador_token
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
//#line 322 "Parser.java"
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
int yyparse()
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
case 2:
//#line 28 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa " +  val_peek(3).sval);}
break;
case 11:
//#line 48 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una declaracion de variable");}
break;
case 12:
//#line 51 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el tipo I16");
	   yyval.sval = "i16";
	   aux = "i16";}
break;
case 13:
//#line 54 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el tipo F32");
    	   yyval.sval = "f32";
    	   aux = "f32";}
break;
case 14:
//#line 57 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio una cadena");
    	      aux = "CADENA";}
break;
case 15:
//#line 61 "gramatica.y"
{System.out.println("G86: se le va a asignar tipo: " + aux + " a la variable " + val_peek(0).sval + ambito);}
break;
case 16:
//#line 64 "gramatica.y"
{System.out.println("G90: se le va a asignar tipo: " + aux + " a la variable " + val_peek(0).sval + ambito);}
break;
case 17:
//#line 69 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la asignacion a la variable " + val_peek(2).sval);}
break;
case 20:
//#line 78 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio la impresion de una cadena: ");}
break;
case 21:
//#line 81 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una suma");}
break;
case 22:
//#line 82 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una resta");}
break;
case 24:
//#line 86 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una multiplicacion");}
break;
case 25:
//#line 87 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + "Se detecto una division");}
break;
case 27:
//#line 90 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + val_peek(0).sval + " como ID" );}
break;
case 28:
//#line 91 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ", Se reconocio " + val_peek(0).sval + " como constante " );}
break;
case 29:
//#line 94 "gramatica.y"
{ if ((val_peek(0).sval).contains(".")){val_peek(0).sval = String.valueOf(PasarAFloatJava(val_peek(0).sval));}
	     ConstantePositiva(val_peek(0).sval);
 	     yyval.sval = val_peek(0).sval;}
break;
//#line 542 "Parser.java"
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
 */
public void run()
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
