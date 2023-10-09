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
import java.util.ArrayList;
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
    0,    1,    1,    1,    1,    2,    3,    3,    4,    4,
    5,    5,    5,    5,    5,    5,    5,    5,    7,    7,
    7,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,   11,
   11,   11,   12,   12,   13,   13,   15,   15,   16,   16,
   16,   16,   16,   16,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   14,   14,    9,    9,   23,   23,
   10,   24,   24,    6,    6,    6,    6,    6,   22,   22,
   18,   18,   26,   26,   27,   27,   19,   19,   28,   28,
   28,   28,   28,   28,   29,   29,   20,   21,   30,   30,
   25,   25,   25,   31,   31,   31,   31,   32,   32,   32,
   33,   33,
};
final static short yylen[] = {                            2,
    1,    4,    4,    4,    4,    1,    2,    1,    1,    1,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,   10,    9,    8,    7,   10,   10,   10,   10,   10,
   10,   10,    8,    8,    8,    8,    8,    8,    8,    1,
    1,    1,    3,    1,    2,    1,    1,    1,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    5,    2,    2,    1,
    2,    3,    1,    2,    2,    2,    2,    2,    4,    3,
    3,    3,    4,    1,    5,    4,   12,    8,    3,    3,
    3,    3,    3,    3,    2,    1,    2,    1,    8,    7,
    3,    3,    1,    3,    3,    1,    1,    2,    1,    1,
    1,    2,
};
final static short yydefred[] = {                         0,
    6,    0,    1,    0,    0,    0,    0,    0,    0,   40,
   41,   42,    0,    0,    0,    8,    9,   10,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   98,
   73,    0,    0,    0,    0,    0,   97,    0,    0,    0,
    0,    0,    0,    0,    7,   15,   11,   16,   12,   17,
   13,   18,   14,   21,   44,    0,   20,    0,   74,   75,
   76,   77,   78,    0,  111,    0,    0,    0,    0,  107,
    0,  106,  110,    0,    0,   81,    0,   82,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    5,    3,
    2,    4,   43,  108,    0,  112,    0,    0,    0,    0,
   79,    0,    0,    0,   72,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   70,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   46,
   47,   48,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  104,  105,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   52,   49,   53,   50,   54,   51,   67,   69,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   86,
    0,    0,    0,   45,   60,   55,   61,   56,   62,   57,
   63,   58,   64,   59,    0,   83,    0,   96,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   85,   66,   65,    0,    0,    0,    0,   95,    0,
    0,    0,   25,    0,    0,    0,    0,    0,    0,    0,
    0,  100,    0,   39,    0,   80,    0,   88,   36,   33,
   34,   24,   35,    0,    0,    0,    0,   37,    0,   38,
    0,   99,    0,    0,    0,    0,    0,   23,    0,    0,
    0,   32,    0,   29,   26,   27,   22,   28,   30,   31,
    0,   87,
};
final static short yydgoto[] = {                          2,
    3,    4,   15,   16,   17,   18,  114,  115,   21,  116,
   23,   24,  129,  173,  130,  131,  132,  133,  134,  135,
  136,  137,  118,   35,   69,   76,   70,   81,  189,   30,
   71,   72,   73,
};
final static short yysindex[] = {                      -234,
    0,    0,    0, -104,  -19,   -8, -197, -187,  -34,    0,
    0,    0,   54,  138,  152,    0,    0,    0,  -15,  -13,
  -10,   -6, -140,  -50,   79,   89,   97,  101,  102,    0,
    0,   39,   -5, -109,   99,   13,    0,   72,  -37,   31,
   13,  -81,  123,  -73,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  125,    0,  -69,    0,    0,
    0,    0,    0,  -39,    0,  -64,   77,  -59,   46,    0,
   85,    0,    0,  161,   58,    0,   73,    0,  -54,  -17,
  166,  -52,  -33,   86,  -49,   95,  -32,  179,    0,    0,
    0,    0,    0,    0,   47,    0,  295,  187,   13,   13,
    0,   18,   18,   13,    0,   13,   13,   13,   13,   13,
   13,  106,  -20,   23,   33,   55,    0,  221,  295,  -90,
  -24,  295,  220,  295,  223,    2,  222,  128,  265,    0,
    0,    0,   60,   61,   62,   63,   64,  146,   85,   85,
   56,    0,    0,  148,   58,   58,   58,   58,   58,   58,
 -120,   34,    0,    0,    0,    0,    0,    0,    0,    0,
  265,  265,  265,   28,  265,  155,  265,  207,  209,    0,
  240,   65,  208,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  295,    0,   75,    0,   88,   77,
  211,   71,  217,  224,  295,  104,  225,  295,  235,  295,
  170,    0,    0,    0,  109,  265,   52, -137,    0,  110,
  111, -101,    0,  113,  265,  265,  265,  119,  265,  120,
  265,    0,  202,    0,  237,    0,  254,    0,    0,    0,
    0,    0,    0,  253,   87,  256,  257,    0,  259,    0,
  260,    0,  130, -120,  140,  141,  -88,    0,  143,  144,
  154,    0,  204,    0,    0,    0,    0,    0,    0,    0,
  131,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  -45,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   66,    0,    0,   67,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   68,    0,    0,    0,    0,
    0,    0,    0,    6,    0,    0,   69,    0,    0,    0,
   11,    0,    0,    0,   70,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -45,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   16,   38,
    6,    0,    0,    0,  370,  374,  382,  383,  384,  385,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  414,   59,    0,  212,   81,  114,    0,  116,
  103,  406,  323,  332,  285,    4,    0,   -4,   -2,    1,
    3,   21,    0,    0,  403,    0,    0,  390, -179,    0,
   82,   36,    0,
};
final static int YYTABLESIZE=567;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         25,
   95,   26,   83,   84,   27,   40,   28,  120,   58,   25,
   25,   26,   26,   44,   27,   27,   28,   28,   14,  152,
   32,  223,    1,  232,   29,   99,   34,  100,   47,   78,
   49,   36,  162,   51,   29,   29,  257,   53,   25,   66,
   26,   33,  111,   27,  110,   28,  109,  109,  109,  109,
  109,  103,  109,  103,  103,  103,  101,   66,  101,  101,
  101,   37,   66,   29,  253,  109,  154,  109,  196,   38,
  103,   86,  103,   45,  190,  101,  156,  101,  102,   67,
  102,  102,  102,   66,   19,  117,  101,  127,   99,  119,
  100,   66,  226,   41,   19,   19,   66,  102,  158,  102,
   99,   45,  100,  176,  178,  180,  182,  184,  204,   71,
   68,   19,   80,   84,  207,   54,   55,   20,   34,   22,
   34,  160,   59,   19,  227,  228,  102,   20,   20,   22,
   22,  103,   60,   33,   68,   33,  187,  142,  143,    6,
   61,   85,   87,    7,   62,   63,   25,   77,   26,   13,
  195,   27,    5,   28,  231,    6,   20,   79,   22,    7,
    8,    9,   10,   11,   12,   13,    5,  256,  171,    6,
   99,   29,  100,    7,   89,    9,   10,   11,   12,   13,
  139,  140,   92,   58,   25,  121,   26,   93,  186,   27,
   99,   28,  100,   96,   82,  211,   25,   98,   26,   97,
  104,   27,  105,   28,  113,   57,  112,  123,  122,   29,
   44,  246,  208,    9,   10,   11,   12,  124,   25,  126,
   26,   29,   39,   27,  125,   28,  216,  138,  151,   10,
   11,   12,  164,   10,   11,   12,   31,   31,   94,   25,
   46,   26,   48,   29,   27,   50,   28,   91,   25,   52,
   26,   64,   65,   27,   68,   28,  106,  107,  108,  109,
  166,  109,   42,  168,   29,  170,  103,   74,  185,   64,
   65,  101,  169,   29,  141,   65,   44,  198,  153,  109,
  109,  109,  109,  202,  103,  103,  103,  103,  155,  101,
  101,  101,  101,  102,  222,   64,   65,   10,   11,   12,
   10,   11,   12,   64,   65,   10,   11,   12,   64,   65,
  157,  102,  102,  102,  102,  175,  177,  179,  181,  183,
  203,   71,   68,   19,   80,   84,  242,    5,  261,  200,
    6,  201,  205,   94,    7,  210,    9,   10,   11,   12,
   13,  213,  172,    5,  187,  159,    6,    6,  214,  218,
    7,    7,    9,   10,   11,   12,   13,   13,  172,  220,
    5,  243,  188,    6,  224,  229,  230,    7,  233,    9,
   10,   11,   12,   13,  238,  240,  244,  245,   90,    5,
  248,  249,    6,  250,  251,  252,    7,    8,    9,   10,
   11,   12,   13,  262,    5,  254,  255,    6,  258,  259,
  209,    7,    8,    9,   10,   11,   12,   13,    5,  260,
   91,    6,  188,  174,   92,    7,    8,    9,   10,   11,
   12,   13,   93,   94,   89,   90,  187,   43,   56,    6,
   88,    0,    0,    7,  209,   75,    0,    0,   80,   13,
    0,  161,  163,   80,  165,  174,  167,  174,    0,  174,
    0,  174,    0,    0,    0,  188,    0,    0,  187,    0,
  187,    6,    0,    6,  209,    7,    0,    7,    0,    0,
    0,   13,    0,   13,    0,    0,  174,  113,    0,    0,
    0,    0,    0,    0,  192,    0,    9,   10,   11,   12,
  174,    0,  191,  193,  194,    0,  197,  128,  199,  174,
    0,  174,    0,  174,    0,  174,  144,  206,  145,  146,
  147,  148,  149,  150,    0,    0,    0,  215,  217,  174,
  219,    5,  221,  212,    6,    0,    0,    0,    7,    0,
    9,   10,   11,   12,   13,    0,  172,  225,  235,    0,
    0,    0,    0,    0,    0,    0,  234,  236,  237,    0,
  239,    5,  241,    0,    6,    0,    0,    0,    7,    0,
    9,   10,   11,   12,   13,    0,  247,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
   40,    4,   40,   41,    4,   40,    4,   41,   59,   14,
   15,   14,   15,   59,   14,   15,   14,   15,  123,   40,
   40,  201,  257,  125,    4,   43,   46,   45,   44,   34,
   44,   40,  123,   44,   14,   15,  125,   44,   43,   45,
   43,   61,   60,   43,   62,   43,   41,   42,   43,   44,
   45,   41,   47,   43,   44,   45,   41,   45,   43,   44,
   45,  259,   45,   43,  244,   60,   44,   62,   41,  257,
   60,   41,   62,   15,   41,   60,   44,   62,   41,   41,
   43,   44,   45,   45,    4,   82,   41,   41,   43,  123,
   45,   45,   41,   40,   14,   15,   45,   60,   44,   62,
   43,   43,   45,   44,   44,   44,   44,   44,   44,   44,
   44,   44,   44,   44,   40,  256,  257,    4,   46,    4,
   46,  118,   44,   43,  262,  263,   42,   14,   15,   14,
   15,   47,   44,   61,   32,   61,  257,  102,  103,  260,
   44,   39,   40,  264,   44,   44,  151,  257,  151,  270,
  123,  151,  257,  151,  256,  260,   43,   59,   43,  264,
  265,  266,  267,  268,  269,  270,  257,  256,   41,  260,
   43,  151,   45,  264,  256,  266,  267,  268,  269,  270,
   99,  100,  256,   59,  189,   83,  189,  257,   41,  189,
   43,  189,   45,  258,  123,  125,  201,  257,  201,  123,
   40,  201,  257,  201,  257,  256,   41,  257,  123,  189,
  256,  125,  125,  266,  267,  268,  269,  123,  223,   41,
  223,  201,  257,  223,  257,  223,  123,   41,  123,  267,
  268,  269,  257,  267,  268,  269,  257,  257,  278,  244,
  256,  244,  256,  223,  244,  256,  244,  125,  253,  256,
  253,  257,  258,  253,  152,  253,  274,  275,  276,  277,
   41,  256,  125,   41,  244,   44,  256,  273,  123,  257,
  258,  256,  271,  253,  257,  258,  125,  123,  256,  274,
  275,  276,  277,   44,  274,  275,  276,  277,  256,  274,
  275,  276,  277,  256,  125,  257,  258,  267,  268,  269,
  267,  268,  269,  257,  258,  267,  268,  269,  257,  258,
  256,  274,  275,  276,  277,  256,  256,  256,  256,  256,
  256,  256,  256,  256,  256,  256,  125,  257,  125,  123,
  260,  123,  125,  278,  264,  125,  266,  267,  268,  269,
  270,  125,  272,  257,  257,  125,  260,  260,  125,  125,
  264,  264,  266,  267,  268,  269,  270,  270,  272,  125,
  257,  125,  151,  260,  256,  256,  256,  264,  256,  266,
  267,  268,  269,  270,  256,  256,  123,  125,  256,  257,
  125,  125,  260,  125,  125,  256,  264,  265,  266,  267,
  268,  269,  270,  263,  257,  256,  256,  260,  256,  256,
  189,  264,  265,  266,  267,  268,  269,  270,  257,  256,
   41,  260,  201,  129,   41,  264,  265,  266,  267,  268,
  269,  270,   41,   41,   41,   41,  257,   14,   23,  260,
   41,   -1,   -1,  264,  223,   33,   -1,   -1,   36,  270,
   -1,  119,  120,   41,  122,  161,  124,  163,   -1,  165,
   -1,  167,   -1,   -1,   -1,  244,   -1,   -1,  257,   -1,
  257,  260,   -1,  260,  253,  264,   -1,  264,   -1,   -1,
   -1,  270,   -1,  270,   -1,   -1,  192,  257,   -1,   -1,
   -1,   -1,   -1,   -1,  162,   -1,  266,  267,  268,  269,
  206,   -1,  161,  162,  163,   -1,  165,   95,  167,  215,
   -1,  217,   -1,  219,   -1,  221,  104,  185,  106,  107,
  108,  109,  110,  111,   -1,   -1,   -1,  195,  196,  235,
  198,  257,  200,  192,  260,   -1,   -1,   -1,  264,   -1,
  266,  267,  268,  269,  270,   -1,  272,  206,  216,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  215,  216,  217,   -1,
  219,  257,  221,   -1,  260,   -1,   -1,   -1,  264,   -1,
  266,  267,  268,  269,  270,   -1,  235,
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
"programa : nombre_programa bloque_sentencias_programa '}' error",
"programa : nombre_programa '{' '}' error",
"nombre_programa : ID",
"bloque_sentencias_programa : bloque_sentencias_programa sentencia_programa",
"bloque_sentencias_programa : sentencia_programa",
"sentencia_programa : sentencias_declarativas",
"sentencia_programa : sentencias_ejecutables",
"sentencias_declarativas : declaracion_variables ','",
"sentencias_declarativas : declaracion_funciones ','",
"sentencias_declarativas : declaracion_clases ','",
"sentencias_declarativas : declaracion_objetos_clase ','",
"sentencias_declarativas : declaracion_variables error",
"sentencias_declarativas : declaracion_funciones error",
"sentencias_declarativas : declaracion_clases error",
"sentencias_declarativas : declaracion_objetos_clase error",
"declaracion_variables : tipo lista_de_variables",
"declaracion_variables : lista_de_variables error",
"declaracion_variables : tipo error",
"declaracion_funciones : VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}'",
"declaracion_funciones : VOID ID '(' tipo ID ')' '{' sentencias_retorno '}'",
"declaracion_funciones : VOID ID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno '}'",
"declaracion_funciones : VOID ID '(' ')' '{' sentencias_retorno '}'",
"declaracion_funciones : VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' error",
"declaracion_funciones : VOID ID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno error",
"declaracion_funciones : VOID ID '(' tipo ID ')' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : VOID ID '(' tipo ID '{' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : VOID ID tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : VOID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : ID '(' tipo ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : VOID ID '(' ')' '{' cuerpo_de_la_funcion '}' error",
"declaracion_funciones : VOID ID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno error",
"declaracion_funciones : VOID ID '(' ')' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : VOID ID '(' '{' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : VOID ID ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : VOID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error",
"declaracion_funciones : ID '(' ')' '{' cuerpo_de_la_funcion sentencias_retorno '}' error",
"tipo : LONG",
"tipo : UINT",
"tipo : FLOAT",
"lista_de_variables : lista_de_variables ';' ID",
"lista_de_variables : ID",
"cuerpo_de_la_funcion : cuerpo_de_la_funcion sentencias_de_funcion",
"cuerpo_de_la_funcion : sentencias_de_funcion",
"sentencias_de_funcion : sentencia_declatariva_especificas",
"sentencias_de_funcion : sentencia_funcion_ejecutable",
"sentencia_declatariva_especificas : declaracion_variables ','",
"sentencia_declatariva_especificas : declaracion_funciones ','",
"sentencia_declatariva_especificas : declaracion_objetos_clase ','",
"sentencia_declatariva_especificas : declaracion_variables error",
"sentencia_declatariva_especificas : declaracion_funciones error",
"sentencia_declatariva_especificas : declaracion_objetos_clase error",
"sentencia_funcion_ejecutable : sentencia_asignacion ','",
"sentencia_funcion_ejecutable : sentencias_IF ','",
"sentencia_funcion_ejecutable : sentencias_salida ','",
"sentencia_funcion_ejecutable : sentencias_control ','",
"sentencia_funcion_ejecutable : sentencias_ejecucion_funcion ','",
"sentencia_funcion_ejecutable : sentencia_asignacion error",
"sentencia_funcion_ejecutable : sentencias_IF error",
"sentencia_funcion_ejecutable : sentencias_salida error",
"sentencia_funcion_ejecutable : sentencias_control error",
"sentencia_funcion_ejecutable : sentencias_ejecucion_funcion error",
"sentencias_retorno : RETURN ','",
"sentencias_retorno : RETURN error",
"declaracion_clases : CLASS ID '{' cuerpo_clase '}'",
"declaracion_clases : CLASS ID",
"cuerpo_clase : cuerpo_clase sentencia_declatariva_especificas",
"cuerpo_clase : sentencia_declatariva_especificas",
"declaracion_objetos_clase : ID list_objts_clase",
"list_objts_clase : list_objts_clase ';' ID",
"list_objts_clase : ID",
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

//#line 208 "gramatica.y"

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
    //System.out.println("Yacc reporto un error: " + error);
}
//#line 559 "Parser.java"
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
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta } al final");}
break;
case 4:
//#line 26 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta { al final");}
break;
case 5:
//#line 27 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta bloque de sentencias al final");}
break;
case 9:
//#line 37 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia declarativa");}
break;
case 10:
//#line 38 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia ejecutable");}
break;
case 15:
//#line 47 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 16:
//#line 48 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 17:
//#line 49 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 18:
//#line 50 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 19:
//#line 53 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de variables");}
break;
case 20:
//#line 54 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta el TIPO");}
break;
case 21:
//#line 55 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta el identificador");}
break;
case 22:
//#line 58 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");}
break;
case 23:
//#line 59 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion con parametro");}
break;
case 24:
//#line 60 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
break;
case 25:
//#line 61 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una funcion sin parametro");}
break;
case 26:
//#line 62 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta RETURN en la funcion");}
break;
case 27:
//#line 63 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '}' en la funcion");}
break;
case 28:
//#line 64 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '{' en la funcion");}
break;
case 29:
//#line 65 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ')' en la funcion");}
break;
case 30:
//#line 66 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '(' en la funcion");}
break;
case 31:
//#line 67 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta nombre en la funcion");}
break;
case 32:
//#line 68 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta la palabra VOID en la funcion");}
break;
case 33:
//#line 69 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta RETURN en la funcion");}
break;
case 34:
//#line 70 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '}' en la funcion");}
break;
case 35:
//#line 71 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '{' en la funcion");}
break;
case 36:
//#line 72 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ')' en la funcion");}
break;
case 37:
//#line 73 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta '(' en la funcion");}
break;
case 38:
//#line 74 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta nombre en la funcion");}
break;
case 39:
//#line 75 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta la palabra VOID en la funcion");}
break;
case 40:
//#line 78 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");}
break;
case 41:
//#line 79 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");}
break;
case 42:
//#line 80 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");}
break;
case 52:
//#line 98 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 53:
//#line 99 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 54:
//#line 100 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 60:
//#line 108 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 61:
//#line 109 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 62:
//#line 110 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 63:
//#line 111 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 64:
//#line 112 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 65:
//#line 116 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio RETURN");}
break;
case 66:
//#line 117 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 67:
//#line 120 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase");}
break;
case 68:
//#line 122 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase a posterior");}
break;
case 71:
//#line 129 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de objeto de clase");}
break;
case 74:
//#line 137 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia Ejecutables");}
break;
case 79:
//#line 144 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
break;
case 80:
//#line 145 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion sin parametro");}
break;
case 81:
//#line 148 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion");}
break;
case 83:
//#line 152 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion con conversion de tipo");}
break;
case 85:
//#line 156 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
break;
case 86:
//#line 157 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a una funcion sin parametro");}
break;
case 87:
//#line 160 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF_ELSE");}
break;
case 88:
//#line 161 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF");}
break;
case 89:
//#line 164 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor");}
break;
case 90:
//#line 165 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor");}
break;
case 91:
//#line 166 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor o igual");}
break;
case 92:
//#line 167 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor o igual");}
break;
case 93:
//#line 168 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion de igualdad");}
break;
case 94:
//#line 169 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por distinto?");}
break;
case 97:
//#line 176 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una cadena");}
break;
case 98:
//#line 179 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia While");}
break;
case 101:
//#line 186 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una suma");}
break;
case 102:
//#line 187 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una resta");}
break;
case 104:
//#line 191 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una multiplicacion");}
break;
case 105:
//#line 192 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una division");}
break;
case 108:
//#line 197 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador con MENOS_MENOS");}
break;
case 109:
//#line 198 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador");}
break;
case 111:
//#line 202 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una constante");}
break;
case 112:
//#line 203 "gramatica.y"
{ConstanteNegativa(val_peek(0).sval);}
break;
//#line 1000 "Parser.java"
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
