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
   16,   16,   16,   16,   14,   14,    9,    9,    9,    9,
    9,    9,   17,   17,   17,   17,   10,   18,   18,   18,
   18,    6,    6,    6,    6,    6,   23,   23,   19,   19,
   25,   25,   26,   26,   20,   20,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   20,   27,   27,   27,   27,   27,   27,   28,   28,
   21,   22,   29,   29,   29,   29,   29,   24,   24,   24,
   30,   30,   30,   30,   31,   31,   31,   32,   32,
};
final static short yylen[] = {                            2,
    1,    4,    4,    4,    4,    1,    2,    1,    1,    1,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,   10,    9,    8,    7,   10,   10,   10,   10,   10,
   10,   10,    8,    8,    8,    8,    8,    8,    8,    1,
    1,    1,    3,    1,    2,    1,    1,    1,    2,    2,
    2,    2,    2,    2,    2,    2,    5,    2,    5,    5,
    5,    5,    2,    1,    2,    2,    2,    3,    1,    4,
    3,    2,    2,    2,    2,    2,    4,    3,    3,    3,
    4,    1,    5,    4,   12,    8,   12,   12,   12,   11,
   11,   11,   10,   12,   12,   11,   12,    8,    8,    7,
    8,    8,    3,    3,    3,    3,    3,    3,    2,    1,
    2,    1,    8,    7,    8,    8,    8,    3,    3,    1,
    3,    3,    1,    1,    2,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    6,    0,    1,    0,    0,    0,    0,    0,    0,   40,
   41,   42,    0,    0,    0,    0,    8,    9,   10,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  112,   69,    0,    0,    0,    0,    0,  111,    0,    0,
    0,    0,    0,    0,    0,    0,  128,    0,    0,  124,
    0,    0,  123,  127,    0,    7,   15,   11,   16,   12,
   17,   13,   18,   14,   21,   44,    0,   20,    0,   72,
   73,   74,   75,   76,    0,    0,    0,    0,   80,    0,
    0,   79,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    5,    3,    2,  125,    0,  129,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    4,   43,    0,    0,   77,    0,   71,   68,    0,    0,
    0,    0,    0,   65,   52,   49,   53,   50,   66,   54,
   51,    0,    0,   63,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  121,  122,   48,
    0,   46,   47,    0,    0,   70,    0,  110,    0,    0,
   61,   59,   57,    0,   60,   62,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   84,    0,    0,
    0,    0,    0,   45,    0,   81,    0,    0,  109,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   83,    0,    0,   56,   55,
    0,    0,   78,    0,    0,    0,    0,    0,    0,    0,
    0,   25,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  114,    0,    0,    0,  100,    0,    0,    0,   39,
    0,    0,  102,    0,  101,   98,    0,   86,    0,   36,
   33,   34,   24,   35,    0,    0,    0,    0,   37,    0,
   38,    0,  117,  113,  115,  116,    0,   99,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   23,    0,
    0,    0,    0,    0,   32,    0,    0,    0,    0,    0,
    0,   29,   26,   27,   22,   28,   30,   31,    0,   93,
    0,    0,   96,    0,    0,    0,   92,    0,   91,    0,
   90,   97,   95,   94,   87,   85,   88,   89,
};
final static short yydgoto[] = {                          2,
    3,    4,   16,   17,   18,  170,   90,   91,   22,   93,
   24,   25,  171,  203,  172,  173,   95,   36,   26,   27,
   28,   29,   30,   49,   82,   50,   51,  179,   31,   52,
   53,   54,
};
final static short yysindex[] = {                      -223,
    0,    0,    0,   87,  -15,   -2, -191, -106,  -33,    0,
    0,    0,   43,  101,  196,  115,    0,    0,    0,  -40,
  -28,    8,   26, -112,  -56,   48,   70,   84,  103,  109,
    0,    0,  221, -140,  -43,  106,   10,    0,  133,  161,
   81,  304,   12, -124,   73,  -39,    0, -122,   20,    0,
  126,   78,    0,    0,  -82,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  119,    0, -107,    0,
    0,    0,    0,    0,   65,  -75,   15,   27,    0,  146,
   94,    0,  -64,  -63,  -62,   76,  163,  147,  -29,   28,
   58,   -8,   62,    0,  311,  350,  127,  138,  -44,  140,
    2,   21,  333,    0,    0,    0,    0,   59,    0,  196,
  196,  196,  196,  196,  196,  196,  196, -115,  241,  241,
    0,    0,  217,  345,    0,  196,    0,    0,  121,  198,
  265,  153,  -97,    0,    0,    0,    0,    0,    0,    0,
    0,   14,  154,    0,  166,  217,  177,  135,  217,  382,
  217,  391,  310, -114,  395,   88,   94,   94,   94,   94,
   78,   78,   94,   94,  327,  198,  174,    0,    0,    0,
   55,    0,    0,  334,  136,    0,  100,    0,  178,  237,
    0,    0,    0,  318,    0,    0,   55,   55,   55,  -12,
   55,  341,   55,  342,  198,  343,  198,    0,  412,  198,
  248,   72,  355,    0,  217,    0,   74,  -99,    0,  -78,
  253,   65,  357,  -35,  367,  370,  217,  203,  371,  217,
  375,  217,  262,  264,  280,    0,  294,  -73,    0,    0,
  246,   55,    0,  380,  250,  381,  255,  -54,  258,  259,
 -102,    0,  260,   55,   55,   55,  271,   55,  273,   55,
  274,    0,  295,  275,  277,    0,  397,  279,  198,    0,
  413,  198,    0,  324,    0,    0,  416,    0,  198,    0,
    0,    0,    0,    0,  417,   41,  418,  420,    0,  421,
    0,  422,    0,    0,    0,    0,  198,    0,  336,  285,
  351,  286,  366,  368,  384,  292,  297,  -98,    0,  300,
  301,  305,  400, -230,    0,  299,  307,  303,  306,  411,
 -185,    0,    0,    0,    0,    0,    0,    0, -165,    0,
  314,  319,    0,  320,  326, -160,    0,  335,    0,  339,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  -53,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   80,    0,    0,   90,    0,
    0,    0,    0,    0,    0,  -23,    0,    0,    0,    0,
    0,  -30,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  104,    0,    0,    0,
    0,    0,    0,    0,  415,    0,    0,    0,    0,    0,
  516,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -53,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  526,  533,  542,  548,
   -1,    4,  549,  551,    0,    0,  -23,    0,    0,    0,
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
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  583,   60,    0,   -4,   63,   85,   35,   93,
   52,  574,  547,  536,  458,   47,   22,    0,  568,    0,
    0,    0,    0,  566,    0,    0,   75,  489,    0,   91,
   92,    0,
};
final static int YYTABLESIZE=812;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         19,
  108,   48,   69,   58,   15,   44,   42,  166,  197,   19,
  120,   19,  120,  120,  120,   60,   40,  126,  126,  126,
  126,  126,  273,  126,   33,  320,  315,  183,  218,  120,
   34,  120,  321,    1,   89,   89,  126,   37,  126,  118,
   19,  118,  118,  118,  119,   35,  119,  119,  119,  259,
   86,   62,  102,  184,   48,  125,   48,  114,  118,  115,
  118,   96,  114,  119,  115,  119,   20,   38,  269,   64,
  327,  136,   34,   92,   92,   56,   20,  328,   20,  117,
   15,  116,   43,   89,   76,   94,   94,   35,   21,  240,
  329,   70,   99,  101,   15,  335,   23,  330,   21,  155,
   21,  138,  336,   48,   56,  141,   23,   20,   23,  133,
  217,   87,   15,   71,  233,  230,   78,  103,   48,  119,
   97,   98,   92,   67,  120,  178,   15,   72,  199,   21,
  114,  104,  115,   58,   94,  109,  114,   23,  115,  207,
   15,  144,  144,   65,   66,   34,   73,   19,  148,  122,
   39,   85,   74,  272,   15,  165,  196,  314,  182,  142,
   35,  178,  234,  235,   84,  297,  118,  147,    9,   10,
   11,   12,   15,  121,  209,  178,  206,   69,  114,  144,
  115,  124,  256,  236,  237,  126,   15,  123,  257,  258,
  178,  127,  178,  128,  129,  178,  209,  106,  130,   68,
   15,  266,   44,  131,  161,  162,  209,  267,  268,   14,
  168,  169,  150,   46,   47,   57,   15,   15,  209,  178,
  209,    5,  209,   41,    6,   44,  134,   59,    7,   80,
    9,   10,   11,   12,   13,   76,  202,   15,  107,   55,
   48,   32,   15,  120,  120,  120,  120,  139,  209,  146,
  126,  126,  126,  126,  178,   88,   15,  178,  152,  178,
  149,   75,  151,   61,  178,   48,   46,   47,   46,   47,
   32,  132,  118,  118,  118,  118,   15,  119,  119,  119,
  119,   63,  178,  135,  209,   48,  209,   15,  209,  178,
  209,  153,   15,  110,  111,  112,  113,    5,  209,  188,
    6,   15,  208,   15,    7,  209,    9,   10,   11,   12,
   13,    5,  202,  137,    6,   46,   47,  140,    7,   15,
    9,   10,   11,   12,   13,  245,  202,  229,  105,    5,
   46,   47,    6,   15,   15,   67,    7,    8,    9,   10,
   11,   12,   13,    5,  100,   58,    6,   10,   11,   12,
    7,    8,    9,   10,   11,   12,   13,    5,  212,   19,
    6,  210,   83,   15,    7,    8,    9,   10,   11,   12,
   13,    5,  228,  154,    6,   15,  176,  238,    7,    8,
    9,   10,   11,   12,   13,  174,  251,  180,  252,    5,
   15,  190,    6,   10,   11,   12,    7,    8,    9,   10,
   11,   12,   13,    5,  254,   15,    6,   15,  181,  185,
    7,    8,    9,   10,   11,   12,   13,    5,  255,  284,
    6,  186,  192,   15,    7,    8,    9,   10,   11,   12,
   13,  194,  195,    5,  177,  143,    6,    6,  198,   15,
    7,    7,    9,   10,   11,   12,   13,   13,  292,  200,
   15,  107,   46,   47,  177,  226,  205,    6,   78,    5,
  304,    7,    6,  220,  222,  224,    7,   13,    9,   10,
   11,   12,   13,    5,  145,  306,    6,   46,   47,  231,
    7,  239,    9,   10,   11,   12,   13,   10,   11,   12,
  308,  242,  309,  177,  243,  247,    6,  167,   47,  249,
    7,  260,  262,  264,  177,  263,   13,    6,  311,  177,
  265,    7,    6,  270,  271,  274,    7,   13,  177,  287,
  177,    6,   13,    6,  319,    7,  279,    7,  281,  283,
  285,   13,  286,   13,  288,  326,  177,  290,  294,    6,
  305,  296,  299,    7,  300,  301,  302,  312,  307,   13,
  177,  177,  313,    6,    6,  316,  317,    7,    7,   82,
  318,  322,  323,   13,   13,  324,  105,  142,  325,  331,
   10,   11,   12,  106,  332,  333,    9,   10,   11,   12,
  177,  334,  107,    6,   10,   11,   12,    7,  108,  103,
  337,  104,  177,   13,  338,    6,   45,   67,   77,    7,
   81,   79,    0,    0,    0,   13,  142,  177,    0,    0,
    6,    0,    0,    0,    7,    9,   10,   11,   12,    0,
   13,    0,  177,    0,  177,    6,    0,    6,  204,    7,
    0,    7,    0,    0,    0,   13,    0,   13,    0,    0,
  177,    0,    0,    6,  204,    0,  204,    7,  204,    0,
  204,    0,    0,   13,  201,    0,  177,    0,    0,    6,
    0,    0,    0,    7,    0,    0,    0,  177,  211,   13,
    6,  204,    0,  156,    7,  157,  158,  159,  160,    0,
   13,  163,  164,  223,    0,  225,    0,    0,  227,  204,
    0,  175,  187,  189,    0,  191,    0,  193,    0,    0,
    0,  204,    0,  204,    0,  204,    0,  204,    0,    0,
    0,    0,  253,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  213,  215,  216,    0,  219,    0,  221,    0,
    0,    0,    0,  204,  214,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  289,    0,  241,
  291,  232,  293,    0,    0,    0,    0,  295,    0,    0,
    0,    0,    0,  244,  246,    0,  248,  261,  250,    0,
    0,    0,   77,    0,    0,  303,    0,    0,    0,  275,
  277,  278,  310,  280,    0,  282,    0,    0,    0,    0,
    0,  276,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  298,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
   40,   45,   59,   44,   40,   59,   40,  123,  123,   14,
   41,   16,   43,   44,   45,   44,  123,   41,   42,   43,
   44,   45,  125,   47,   40,  256,  125,  125,   41,   60,
   46,   62,  263,  257,   39,   40,   60,   40,   62,   41,
   45,   43,   44,   45,   41,   61,   43,   44,   45,  123,
   41,   44,   41,   40,   45,   41,   45,   43,   60,   45,
   62,   40,   43,   60,   45,   62,    4,  259,  123,   44,
  256,   44,   46,   39,   40,   16,   14,  263,   16,   60,
   40,   62,   40,   88,   33,   39,   40,   61,    4,  125,
  256,   44,   41,   42,   40,  256,    4,  263,   14,   41,
   16,   44,  263,   45,   45,   44,   14,   45,   16,   88,
  123,   37,   40,   44,   41,   44,  257,   43,   45,   42,
   40,   41,   88,   44,   47,  130,   40,   44,   41,   45,
   43,  256,   45,   44,   88,  258,   43,   45,   45,   40,
   40,   95,   96,  256,  257,   46,   44,   44,   97,  257,
  257,   46,   44,  256,   40,  271,  271,  256,  256,  257,
   61,  166,  262,  263,   59,  125,   41,   41,  266,  267,
  268,  269,   40,  256,  179,  180,   41,   59,   43,  133,
   45,  257,  256,  262,  263,   40,   40,  123,  262,  263,
  195,  256,  197,  257,  257,  200,  201,  125,  123,  256,
   40,  256,  256,   41,  114,  115,  211,  262,  263,  123,
  119,  120,  257,  257,  258,  256,   40,   40,  223,  224,
  225,  257,  227,  257,  260,  125,  256,  256,  264,  273,
  266,  267,  268,  269,  270,  184,  272,   40,  278,  125,
   45,  257,   40,  274,  275,  276,  277,  256,  253,  123,
  274,  275,  276,  277,  259,  123,   40,  262,  257,  264,
  123,   41,  123,  256,  269,   45,  257,  258,  257,  258,
  257,  125,  274,  275,  276,  277,   40,  274,  275,  276,
  277,  256,  287,  256,  289,   45,  291,   40,  293,  294,
  295,  271,   40,  274,  275,  276,  277,  257,  303,  123,
  260,   40,  125,   40,  264,  310,  266,  267,  268,  269,
  270,  257,  272,  256,  260,  257,  258,  256,  264,   40,
  266,  267,  268,  269,  270,  123,  272,  256,  256,  257,
  257,  258,  260,   40,   40,  256,  264,  265,  266,  267,
  268,  269,  270,  257,   41,  256,  260,  267,  268,  269,
  264,  265,  266,  267,  268,  269,  270,  257,   41,  256,
  260,  125,  257,   40,  264,  265,  266,  267,  268,  269,
  270,  257,  125,   41,  260,   40,  256,  125,  264,  265,
  266,  267,  268,  269,  270,   41,  125,  123,  125,  257,
   40,  257,  260,  267,  268,  269,  264,  265,  266,  267,
  268,  269,  270,  257,  125,   40,  260,   40,  256,  256,
  264,  265,  266,  267,  268,  269,  270,  257,  125,  125,
  260,  256,   41,   40,  264,  265,  266,  267,  268,  269,
  270,   41,  123,  257,  257,  125,  260,  260,   44,   40,
  264,  264,  266,  267,  268,  269,  270,  270,  125,  123,
   40,  278,  257,  258,  257,   44,  123,  260,   44,  257,
  125,  264,  260,  123,  123,  123,  264,  270,  266,  267,
  268,  269,  270,  257,  125,  125,  260,  257,  258,  125,
  264,  125,  266,  267,  268,  269,  270,  267,  268,  269,
  125,  125,  125,  257,  125,  125,  260,  257,  258,  125,
  264,  256,  123,  123,  257,  256,  270,  260,  125,  257,
  256,  264,  260,  256,  256,  256,  264,  270,  257,  123,
  257,  260,  270,  260,  125,  264,  256,  264,  256,  256,
  256,  270,  256,  270,  256,  125,  257,  125,  123,  260,
  256,  125,  125,  264,  125,  125,  125,  256,  263,  270,
  257,  257,  256,  260,  260,  256,  256,  264,  264,   44,
  256,  263,  256,  270,  270,  263,   41,  257,  263,  256,
  267,  268,  269,   41,  256,  256,  266,  267,  268,  269,
  257,  256,   41,  260,  267,  268,  269,  264,   41,   41,
  256,   41,  257,  270,  256,  260,   14,   24,   33,  264,
   35,   34,   -1,   -1,   -1,  270,  257,  257,   -1,   -1,
  260,   -1,   -1,   -1,  264,  266,  267,  268,  269,   -1,
  270,   -1,  257,   -1,  257,  260,   -1,  260,  171,  264,
   -1,  264,   -1,   -1,   -1,  270,   -1,  270,   -1,   -1,
  257,   -1,   -1,  260,  187,   -1,  189,  264,  191,   -1,
  193,   -1,   -1,  270,  166,   -1,  257,   -1,   -1,  260,
   -1,   -1,   -1,  264,   -1,   -1,   -1,  257,  180,  270,
  260,  214,   -1,  108,  264,  110,  111,  112,  113,   -1,
  270,  116,  117,  195,   -1,  197,   -1,   -1,  200,  232,
   -1,  126,  146,  147,   -1,  149,   -1,  151,   -1,   -1,
   -1,  244,   -1,  246,   -1,  248,   -1,  250,   -1,   -1,
   -1,   -1,  224,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  187,  188,  189,   -1,  191,   -1,  193,   -1,
   -1,   -1,   -1,  276,  188,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  259,   -1,  214,
  262,  205,  264,   -1,   -1,   -1,   -1,  269,   -1,   -1,
   -1,   -1,   -1,  217,  218,   -1,  220,  232,  222,   -1,
   -1,   -1,  207,   -1,   -1,  287,   -1,   -1,   -1,  244,
  245,  246,  294,  248,   -1,  250,   -1,   -1,   -1,   -1,
   -1,  245,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  276,
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
"sentencias_de_funcion : sentencias_ejecutables",
"sentencia_declatariva_especificas : declaracion_variables ','",
"sentencia_declatariva_especificas : declaracion_funciones ','",
"sentencia_declatariva_especificas : declaracion_objetos_clase ','",
"sentencia_declatariva_especificas : declaracion_variables error",
"sentencia_declatariva_especificas : declaracion_funciones error",
"sentencia_declatariva_especificas : declaracion_objetos_clase error",
"sentencias_retorno : RETURN ','",
"sentencias_retorno : RETURN error",
"declaracion_clases : CLASS ID '{' cuerpo_clase '}'",
"declaracion_clases : CLASS ID",
"declaracion_clases : CLASS ID '{' cuerpo_clase error",
"declaracion_clases : CLASS ID cuerpo_clase '}' error",
"declaracion_clases : CLASS ID '{' '}' error",
"declaracion_clases : CLASS '{' cuerpo_clase '}' error",
"cuerpo_clase : cuerpo_clase sentencia_declatariva_especificas",
"cuerpo_clase : sentencia_declatariva_especificas",
"cuerpo_clase : sentencias_ejecutables error",
"cuerpo_clase : declaracion_clases error",
"declaracion_objetos_clase : ID list_objts_clase",
"list_objts_clase : list_objts_clase ';' ID",
"list_objts_clase : ID",
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
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' error",
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' END_IF error",
"sentencias_IF : '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error",
"sentencias_IF : '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' END_IF error",
"sentencias_IF : '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' error",
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' error",
"sentencias_IF : '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' error",
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' '}' END_IF error",
"sentencias_IF : IF '(' condicion_if_while ')' '{' '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error",
"sentencias_IF : IF '(' condicion_if_while ')' '{' '}' ELSE '{' '}' END_IF error",
"sentencias_IF : IF '(' ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error",
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' error",
"sentencias_IF : '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' END_IF error",
"sentencias_IF : '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' error",
"sentencias_IF : IF '(' condicion_if_while ')' '{' '}' END_IF error",
"sentencias_IF : IF '(' ')' '{' bloque_sentencias_ejecutables '}' END_IF error",
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
"sentencia_while_do : WHILE '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' error",
"sentencia_while_do : '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}' error",
"sentencia_while_do : WHILE '(' ')' DO '{' bloque_sentencias_ejecutables '}' error",
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

//#line 236 "gramatica.y"

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
//#line 649 "Parser.java"
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
case 55:
//#line 117 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio RETURN");}
break;
case 56:
//#line 118 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta ',' al final");}
break;
case 57:
//#line 121 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase");}
break;
case 58:
//#line 123 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de clase a posterior");}
break;
case 59:
//#line 124 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite clase con cuerpos que no esten entre {} ");}
break;
case 60:
//#line 125 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite clase con cuerpos que no esten entre {} ");}
break;
case 61:
//#line 126 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite declarar clases vacias");}
break;
case 62:
//#line 127 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite clase sin ID");}
break;
case 65:
//#line 132 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite declarar sentencia ejecutables");}
break;
case 66:
//#line 133 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite declarar una clase dentro de otra");}
break;
case 67:
//#line 136 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una declaracion de objeto de clase");}
break;
case 70:
//#line 141 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite objetos de clase separados por .");}
break;
case 71:
//#line 142 "gramatica.y"
{System.out.println("Error sintectico al compilar no permite objetos de clase separados por ,");}
break;
case 72:
//#line 146 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia Ejecutables");}
break;
case 77:
//#line 153 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
break;
case 78:
//#line 154 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion sin parametro");}
break;
case 79:
//#line 157 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion");}
break;
case 81:
//#line 161 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una asignacion con conversion de tipo");}
break;
case 83:
//#line 165 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a funcion con parametro");}
break;
case 84:
//#line 166 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una invocacion a una funcion sin parametro");}
break;
case 85:
//#line 169 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF_ELSE");}
break;
case 86:
//#line 170 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF");}
break;
case 87:
//#line 171 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta END_IF para finalizar la sentencia IF");}
break;
case 88:
//#line 172 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra ELSE");}
break;
case 89:
//#line 173 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra IF");}
break;
case 90:
//#line 174 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabras IF y ELSE");}
break;
case 91:
//#line 175 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabras IF Y END_IF");}
break;
case 92:
//#line 176 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabras ELSE Y END_IF");}
break;
case 93:
//#line 177 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabras IF, ELSE Y END_IF");}
break;
case 94:
//#line 178 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta bloque sentencias ejecutables");}
break;
case 95:
//#line 179 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta bloque sentencias ejecutables");}
break;
case 96:
//#line 180 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta bloques sentencias ejecutables");}
break;
case 97:
//#line 181 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta condicion");}
break;
case 98:
//#line 182 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra END_IF");}
break;
case 99:
//#line 183 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico .Falta palabra IF");}
break;
case 100:
//#line 184 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico .Falta palabras IF y END_IF");}
break;
case 101:
//#line 185 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico .Falta bloque sentencias ejecutables");}
break;
case 102:
//#line 186 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta condicion");}
break;
case 103:
//#line 189 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor");}
break;
case 104:
//#line 190 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor");}
break;
case 105:
//#line 191 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por mayor o igual");}
break;
case 106:
//#line 192 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por menor o igual");}
break;
case 107:
//#line 193 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion de igualdad");}
break;
case 108:
//#line 194 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una comparacion por distinto?");}
break;
case 111:
//#line 201 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una cadena");}
break;
case 112:
//#line 204 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia While");}
break;
case 115:
//#line 209 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra DO");}
break;
case 116:
//#line 210 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta palabra WHILE");}
break;
case 117:
//#line 211 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico . Falta condicion");}
break;
case 118:
//#line 214 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una suma");}
break;
case 119:
//#line 215 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una resta");}
break;
case 121:
//#line 219 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una multiplicacion");}
break;
case 122:
//#line 220 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una division");}
break;
case 125:
//#line 225 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador con MENOS_MENOS");}
break;
case 126:
//#line 226 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador");}
break;
case 128:
//#line 230 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una constante");}
break;
case 129:
//#line 231 "gramatica.y"
{ConstanteNegativa(val_peek(0).sval);}
break;
//#line 1178 "Parser.java"
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
