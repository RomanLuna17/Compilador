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

import GeneracionCodigoIntermedio.ArbolSintactico;
import GeneracionCodigoIntermedio.NodoComun;
import GeneracionCodigoIntermedio.NodoHoja;
import GeneracionCodigoIntermedio.NodoControl;
import java.util.Hashtable;

//#line 30 "Parser.java"




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
public final static short ELSE=261;
public final static short END_IF=262;
public final static short PRINT=263;
public final static short CLASS=264;
public final static short VOID=265;
public final static short LONG=266;
public final static short UINT=267;
public final static short FLOAT=268;
public final static short WHILE=269;
public final static short DO=270;
public final static short RETURN=271;
public final static short TOF=272;
public final static short MAYOR_IGUAL=273;
public final static short MENOR_IGUAL=274;
public final static short IGUAL_IGUAL=275;
public final static short EXCLAMACION_EXCLAMACION=276;
public final static short MENOS_MENOS=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    2,    3,    3,    4,
    4,    4,    4,    4,    4,    4,    4,    6,    6,    6,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
   13,   12,   12,   12,   10,   10,   10,   11,   11,   14,
   14,   15,   15,   16,   16,   16,   16,   16,   16,   16,
   16,   17,   18,    8,    8,    8,    8,    8,   19,   19,
   20,   20,   20,    9,   21,   21,   21,    5,    5,    5,
    5,    5,    5,    5,    5,    5,    5,    5,   26,   26,
   22,   22,   27,   27,   29,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   30,   30,   30,   30,   30,   30,
   31,   31,   24,   25,   32,   32,   32,   32,   32,   28,
   28,   28,   33,   33,   33,   33,   34,   34,   34,   35,
   35,   36,   36,
};
final static short yylen[] = {                            2,
    1,    3,    3,    3,    3,    2,    1,    1,    1,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    7,    6,    7,    7,    7,    7,    6,    6,    6,    6,
    2,    2,    9,    7,    1,    1,    1,    3,    1,    2,
    1,    1,    1,    2,    2,    2,    2,    2,    2,    2,
    2,    1,    1,    4,    1,    4,    4,    4,    2,    5,
    2,    1,    2,    2,    3,    1,    3,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    4,    3,
    3,    3,    3,    1,    1,   12,    8,   12,   12,   12,
   11,   11,   11,   10,   12,   12,   11,   12,    8,    8,
    7,    8,    8,   12,    3,    3,    3,    3,    3,    3,
    2,    1,    2,    1,    8,    7,    8,    8,    8,    3,
    3,    1,    3,    3,    1,    4,    2,    1,    1,    3,
    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   35,   36,   37,    0,   53,
    0,    0,    0,    1,    0,    7,    8,    9,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  114,   66,    0,    0,  113,   59,    0,
   32,    0,    0,    0,    0,  131,  132,    0,    0,    0,
    0,    0,  125,    0,  129,    0,    6,   14,   10,   15,
   11,   16,   12,   17,   13,   20,   39,    0,   19,    0,
    0,    0,    0,    0,   73,    0,    0,    0,    0,    0,
    0,    0,   62,    0,    0,   74,   68,   75,   69,   76,
   70,   77,   71,   78,   72,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    5,    3,    2,
    0,  133,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  127,    0,    4,   38,    0,    0,    0,
    0,   31,    0,    0,    0,   63,   49,   44,   50,   45,
   48,   51,   46,   47,    0,    0,   61,   80,    0,   82,
    0,   81,   83,   67,   65,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  123,  124,  130,   43,    0,   41,   42,
    0,    0,    0,    0,    0,    0,   58,   56,   54,   57,
   79,   84,  112,    0,    0,   60,    0,    0,    0,    0,
    0,  126,    0,    0,    0,   40,    0,    0,    0,    0,
    0,    0,    0,    0,  111,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   29,   27,   22,   28,    0,    0,
    0,   30,    0,    0,    0,    0,    0,    0,    0,   34,
    0,    0,  116,    0,    0,    0,  101,    0,    0,    0,
   25,   23,   21,   24,   26,    0,  103,    0,  102,    0,
   99,    0,   87,    0,    0,  119,  115,  117,  118,    0,
  100,    0,    0,    0,    0,    0,    0,    0,   33,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   94,
    0,    0,   97,    0,    0,    0,    0,   93,    0,   92,
    0,   91,   98,   96,  104,   95,   88,   86,   89,   90,
};
final static short yydgoto[] = {                         13,
   14,   15,   16,   17,  177,   79,   80,   81,   82,   23,
   24,   25,   74,  178,  179,  180,   84,   26,   27,   85,
   36,   28,   29,   30,   31,   32,   33,   50,  152,   51,
  194,   34,   52,   53,   54,   55,
};
final static short yysindex[] = {                        89,
 -217,    5, -210,  -99,  -31,    0,    0,    0,   16,    0,
  102,  163,    0,    0,  115,    0,    0,    0,  -24,   -1,
    4,   14, -151,  -49,  444,   17,  130,   47,   58,   65,
   66,   74,   34,    0,    0,  -48,  -16,    0,    0,  229,
    0,  293,  -14, -183,   44,    0,    0,   73, -137,   23,
   86,   56,    0,  -46,    0, -122,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   85,    0, -121,
   -4,   27, -117,  127,    0, -217,  143,  -83,   81,   82,
  131,   94,    0,  133,  441,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  422,  306, -111,  -72,  -66,
   69,  153,  524,   75,  -60,  -67,  170,    0,    0,    0,
  -19,    0,  163,  163,  163,  163,  163,  163,  163,  163,
 -110,  -19,  -19,    0,  -51,    0,    0,  229,  159,  -11,
  229,    0,   90,  -42,  -78,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -217,  -41,    0,    0,  100,    0,
   79,    0,    0,    0,    0,  515,   93,  -28,  229,  184,
  106, -109,  205,   79,   79,   79,   79,   56,   56,   79,
   79,  144,  515,    0,    0,    0,    0,  180,    0,    0,
  229,  193,  229,  212,  249,  229,    0,    0,    0,    0,
    0,    0,    0,  394,  399,    0,  266,  149,  515,  157,
  515,    0,  515,  415,   35,    0,   60,   46,  279,  229,
  308,  121,  345, -131,    0, -108,  377,  132,  229,  428,
  433,  459,  463,  -90,    0,    0,    0,    0,  136,   76,
  162,    0,  173,  167,  175,  282,  176,  290,  329,    0,
  362,  182,    0,  464,  185,  186,    0,  292,  198,  515,
    0,    0,    0,    0,    0,  515,    0,  481,    0,  515,
    0,  313,    0,  515,  209,    0,    0,    0,    0,  515,
    0,  482,  498,  204,  511,  516,  541,  542,    0,  546,
 -180,  220,  215,  226,  228,  239,  564, -168, -163,    0,
  218,  235,    0,  251,  252,  254, -155,    0,  255,    0,
  269,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
   31,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  105,    0,    0,    0,
    0,    0,    0,    0,    0,  107,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -26,    0,  -39,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  108,    0,    0,
    0,    0,    0,    0,    0,   -2,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -37,    0,    0,    0,    0,    0,
  120,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  348,  486,  487,  500,   10,   19,  508,
  513,    0,    0,    0,    0,    0,    0,    0,    0,    0,
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
};
final static short yygindex[] = {                         0,
    0,  494,   20,    0,    1,  117,  148,  165,  624,   25,
  529,    0,  496,  709,  -74,   12,    0,    0,    0,   -8,
    0,    0,    0,    0,    0,    0,    0,  740,    0,   77,
  627,    0,   64,  -36,    0,    0,
};
final static int YYTABLESIZE=928;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        125,
   18,  128,  128,  128,  128,  128,   52,  128,   42,   70,
  100,   18,  173,  201,  122,   18,  122,  122,  122,   59,
  128,   39,  128,   40,  101,   49,  106,   78,   49,  184,
   49,  103,  250,  122,   57,  122,  129,   84,   83,   35,
   78,   52,   61,   84,   37,   18,  189,   63,   38,   73,
  120,   83,  120,  120,  120,   43,   39,   65,   84,  121,
   75,  121,  121,  121,   57,  117,  105,  118,  135,  120,
   84,  120,  108,   96,  163,  290,   84,   78,  121,   98,
  121,  291,  120,   12,  119,  174,  175,  298,   83,   39,
   87,   84,  300,  299,   97,   73,  147,  122,  301,   12,
  307,   89,  123,  206,   66,   67,  308,  206,   91,   93,
  206,  183,  111,  102,  147,   12,   19,   95,  128,  107,
  112,  117,  206,  118,  138,  140,  121,   19,   12,  234,
  235,   19,  206,  126,  206,  127,  206,  143,  206,  132,
  191,   12,  117,   70,  118,  153,  147,   20,   55,  131,
   64,   18,  236,  237,   12,  206,  193,   39,   20,  172,
  200,   19,   20,   85,   21,  247,  206,  133,  110,   12,
  248,  249,  136,  193,  141,   21,  144,  188,  145,   21,
  168,  169,   12,  154,  227,    4,    5,    6,    7,    8,
  155,  156,   20,  157,  215,  193,  160,  159,   12,  193,
  253,  193,  161,  193,  215,  176,   69,   49,   99,   21,
  162,   11,  186,  187,  190,  195,  128,  215,   39,   12,
  215,  193,  215,  215,  198,   41,   44,  196,  199,  122,
  124,   58,   12,  128,  128,  128,  128,   46,   47,   56,
   46,   47,   46,   47,  215,  202,  122,  122,  122,  122,
  193,   12,   77,   39,   60,   48,  193,   48,  193,   62,
  193,    6,    7,    8,  193,  120,  203,  134,   12,   64,
  193,  219,  215,  215,  121,  215,  215,  193,  215,  221,
  215,  181,  120,  120,  120,  120,   39,  215,   12,  256,
  225,  121,  121,  121,  121,  113,  114,  115,  116,  109,
    1,  228,   86,    2,  205,   12,    3,    4,    5,    6,
    7,    8,    9,   88,   10,  226,   76,  208,   12,    2,
   90,   92,    3,    4,    5,    6,    7,    8,    9,   94,
   10,  252,   76,  104,  210,    2,  137,  139,    3,    4,
    5,    6,    7,    8,    9,    1,   10,   12,    2,  142,
   49,    3,    4,    5,    6,    7,    8,    9,    1,   10,
   55,    2,   64,   18,    3,    4,    5,    6,    7,    8,
    9,    1,   10,  212,    2,   85,  232,    3,    4,    5,
    6,    7,    8,    9,   12,   10,   76,  240,  107,    2,
  218,  251,    3,    4,    5,    6,    7,    8,    9,   76,
   10,   12,    2,  229,  258,    3,    4,    5,    6,    7,
    8,    9,  260,   10,  270,   76,   12,  254,    2,   46,
   47,    3,    4,    5,    6,    7,    8,    9,  255,   10,
  257,  259,  231,   12,   48,  277,   76,  266,   12,    2,
  268,  269,    3,    4,    5,    6,    7,    8,    9,   76,
   10,  264,    2,  271,   12,    3,    4,    5,    6,    7,
    8,    9,  148,   10,  279,  283,   49,   12,   76,  233,
  293,    2,   12,  302,    3,    4,    5,    6,    7,    8,
    9,  292,   10,   71,   72,   76,  265,  294,    2,  295,
  303,    3,    4,    5,    6,    7,    8,    9,   12,   10,
  296,  239,   12,   12,   45,   76,  304,  305,    2,  306,
  309,    3,    4,    5,    6,    7,    8,    9,  214,   10,
   12,   12,   76,  216,  310,    2,  108,  109,    3,    4,
    5,    6,    7,    8,    9,   76,   10,   12,    2,  224,
  110,    3,    4,    5,    6,    7,    8,    9,  105,   10,
   12,   68,  242,  106,   12,   12,    0,  243,    6,    7,
    8,  150,   46,   47,   76,  146,  130,    2,    0,    0,
    3,    4,    5,    6,    7,    8,    9,   48,   10,    0,
   12,   12,    0,  245,  261,   12,    0,  246,  267,  262,
  263,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   76,    0,   12,    2,  274,  281,    3,    4,    5,
    6,    7,    8,    9,    0,   10,    0,    0,   76,    0,
    0,    2,  282,   22,    3,    4,    5,    6,    7,    8,
    9,    0,   10,  192,   22,  284,    2,  238,   22,    3,
  285,    0,    0,    0,    0,    9,    0,   10,  158,    0,
  192,    0,    0,    2,    0,  192,    3,    0,    2,    0,
    0,    3,    9,    0,   10,  286,  288,    9,   22,   10,
  289,  192,    0,    0,    2,    0,    0,    3,   46,   47,
    0,    0,    0,    9,  192,   10,    0,    2,  297,  192,
    3,    0,    2,   48,    0,    3,    9,  145,   10,    0,
    0,    9,    0,   10,    4,    5,    6,    7,    8,    6,
    7,    8,    0,    0,    0,  192,    0,    0,    2,  192,
  192,    3,    2,    2,    0,    3,    3,    9,    0,   10,
    0,    9,    9,   10,   10,    0,    0,  192,  192,    0,
    2,    2,    0,    3,    3,    0,    0,    0,    0,    9,
    9,   10,   10,    0,  192,    0,    0,    2,    0,    0,
    3,    0,    0,    0,    0,    0,    9,  192,   10,    0,
    2,  192,  192,    3,    2,    2,    0,    3,    3,    9,
  145,   10,    0,    9,    9,   10,   10,    4,    5,    6,
    7,    8,    0,    0,    0,    0,    0,  192,  192,  204,
    2,    2,  192,    3,    3,    2,    0,    0,    3,    9,
    9,   10,   10,    0,    9,    0,   10,    0,    0,    0,
  192,  217,    0,    2,    0,  220,    3,  222,    0,  223,
    0,    0,    9,    0,   10,  149,  151,  182,    0,  185,
    0,    0,    0,    0,    0,    0,    0,  244,    0,    0,
    0,    0,  164,  165,  166,  167,    0,    0,  170,  171,
    0,    0,    0,    0,    0,    0,    0,  197,    0,    0,
    0,    0,    0,    0,    0,    0,  272,    0,    0,    0,
    0,    0,  273,    0,  275,    0,  276,    0,    0,  207,
  278,  209,  211,    0,  213,    0,  280,    0,    0,    0,
    0,    0,    0,  287,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  230,    0,
    0,    0,    0,    0,    0,    0,    0,  241,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         46,
    0,   41,   42,   43,   44,   45,   44,   47,   40,   59,
   59,   11,  123,  123,   41,   15,   43,   44,   45,   44,
   60,   59,   62,  123,   41,   45,   41,   27,   45,   41,
   45,   40,  123,   60,   15,   62,   41,   40,   27,  257,
   40,   44,   44,   46,   40,   45,  125,   44,  259,   25,
   41,   40,   43,   44,   45,   40,   59,   44,   61,   41,
   44,   43,   44,   45,   45,   43,   42,   45,   77,   60,
   40,   62,  256,   40,  111,  256,   46,   77,   60,   46,
   62,  262,   60,   40,   62,  122,  123,  256,   77,   59,
   44,   61,  256,  262,   61,   71,   85,   42,  262,   40,
  256,   44,   47,  178,  256,  257,  262,  182,   44,   44,
  185,  123,   40,   37,  103,   40,    0,   44,  123,   43,
  258,   43,  197,   45,   44,   44,   41,   11,   40,  261,
  262,   15,  207,  256,  209,  257,  211,   44,  213,  257,
   41,   40,   43,   59,   45,  257,  135,    0,   44,  123,
   44,   44,  261,  262,   40,  230,  156,  257,   11,  270,
  270,   45,   15,   44,    0,  256,  241,   41,  125,   40,
  261,  262,  256,  173,   44,   11,   44,  256,  257,   15,
  117,  118,   40,  256,  125,  264,  265,  266,  267,  268,
  257,  123,   45,   41,  194,  195,  257,  123,   40,  199,
  125,  201,  270,  203,  204,  257,  256,   45,  257,   45,
   41,  123,  123,  256,  256,  123,  256,  217,  256,   40,
  220,  221,  222,  223,   41,  257,  125,  256,  123,  256,
  277,  256,   40,  273,  274,  275,  276,  257,  258,  125,
  257,  258,  257,  258,  244,   41,  273,  274,  275,  276,
  250,   40,  123,  256,  256,  272,  256,  272,  258,  256,
  260,  266,  267,  268,  264,  256,  123,  125,   40,  256,
  270,  123,  272,  273,  256,  275,  276,  277,  278,  123,
  280,  123,  273,  274,  275,  276,  256,  287,   40,  123,
  256,  273,  274,  275,  276,  273,  274,  275,  276,  256,
  257,  256,  256,  260,  125,   40,  263,  264,  265,  266,
  267,  268,  269,  256,  271,  256,  257,  125,   40,  260,
  256,  256,  263,  264,  265,  266,  267,  268,  269,  256,
  271,  256,  257,   41,  123,  260,  256,  256,  263,  264,
  265,  266,  267,  268,  269,  257,  271,   40,  260,  256,
   45,  263,  264,  265,  266,  267,  268,  269,  257,  271,
  256,  260,  256,  256,  263,  264,  265,  266,  267,  268,
  269,  257,  271,  125,  260,  256,  256,  263,  264,  265,
  266,  267,  268,  269,   40,  271,  257,  256,   41,  260,
  125,  256,  263,  264,  265,  266,  267,  268,  269,  257,
  271,   40,  260,  125,  123,  263,  264,  265,  266,  267,
  268,  269,  123,  271,  123,  257,   40,  256,  260,  257,
  258,  263,  264,  265,  266,  267,  268,  269,  256,  271,
  256,  256,  125,   40,  272,  123,  257,  256,   40,  260,
  256,  256,  263,  264,  265,  266,  267,  268,  269,  257,
  271,  123,  260,  256,   40,  263,  264,  265,  266,  267,
  268,  269,   41,  271,  256,  262,   45,   40,  257,  125,
  256,  260,   40,  256,  263,  264,  265,  266,  267,  268,
  269,  262,  271,   40,   41,  257,  125,  262,  260,  262,
  256,  263,  264,  265,  266,  267,  268,  269,   40,  271,
  262,  125,   40,   40,   11,  257,  256,  256,  260,  256,
  256,  263,  264,  265,  266,  267,  268,  269,  125,  271,
   40,   40,  257,  125,  256,  260,   41,   41,  263,  264,
  265,  266,  267,  268,  269,  257,  271,   40,  260,  125,
   41,  263,  264,  265,  266,  267,  268,  269,   41,  271,
   40,   23,  125,   41,   40,   40,   -1,  125,  266,  267,
  268,  256,  257,  258,  257,  125,   71,  260,   -1,   -1,
  263,  264,  265,  266,  267,  268,  269,  272,  271,   -1,
   40,   40,   -1,  125,  256,   40,   -1,  125,  125,  261,
  262,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,   -1,   40,  260,  125,  125,  263,  264,  265,
  266,  267,  268,  269,   -1,  271,   -1,   -1,  257,   -1,
   -1,  260,  125,    0,  263,  264,  265,  266,  267,  268,
  269,   -1,  271,  257,   11,  125,  260,  261,   15,  263,
  125,   -1,   -1,   -1,   -1,  269,   -1,  271,  125,   -1,
  257,   -1,   -1,  260,   -1,  257,  263,   -1,  260,   -1,
   -1,  263,  269,   -1,  271,  125,  125,  269,   45,  271,
  125,  257,   -1,   -1,  260,   -1,   -1,  263,  257,  258,
   -1,   -1,   -1,  269,  257,  271,   -1,  260,  125,  257,
  263,   -1,  260,  272,   -1,  263,  269,  257,  271,   -1,
   -1,  269,   -1,  271,  264,  265,  266,  267,  268,  266,
  267,  268,   -1,   -1,   -1,  257,   -1,   -1,  260,  257,
  257,  263,  260,  260,   -1,  263,  263,  269,   -1,  271,
   -1,  269,  269,  271,  271,   -1,   -1,  257,  257,   -1,
  260,  260,   -1,  263,  263,   -1,   -1,   -1,   -1,  269,
  269,  271,  271,   -1,  257,   -1,   -1,  260,   -1,   -1,
  263,   -1,   -1,   -1,   -1,   -1,  269,  257,  271,   -1,
  260,  257,  257,  263,  260,  260,   -1,  263,  263,  269,
  257,  271,   -1,  269,  269,  271,  271,  264,  265,  266,
  267,  268,   -1,   -1,   -1,   -1,   -1,  257,  257,  173,
  260,  260,  257,  263,  263,  260,   -1,   -1,  263,  269,
  269,  271,  271,   -1,  269,   -1,  271,   -1,   -1,   -1,
  257,  195,   -1,  260,   -1,  199,  263,  201,   -1,  203,
   -1,   -1,  269,   -1,  271,   96,   97,  129,   -1,  131,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  221,   -1,   -1,
   -1,   -1,  113,  114,  115,  116,   -1,   -1,  119,  120,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  159,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  250,   -1,   -1,   -1,
   -1,   -1,  256,   -1,  258,   -1,  260,   -1,   -1,  181,
  264,  183,  184,   -1,  186,   -1,  270,   -1,   -1,   -1,
   -1,   -1,   -1,  277,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  210,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  219,
};
}
final static short YYFINAL=13;
final static short YYMAXTOKEN=277;
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
null,null,null,null,null,null,null,"ID","CTE","CADENA","IF","ELSE","END_IF",
"PRINT","CLASS","VOID","LONG","UINT","FLOAT","WHILE","DO","RETURN","TOF",
"MAYOR_IGUAL","MENOR_IGUAL","IGUAL_IGUAL","EXCLAMACION_EXCLAMACION",
"MENOS_MENOS",
};
final static String yyrule[] = {
"$accept : program",
"program : programa",
"programa : '{' bloque_sentencias_programa '}'",
"programa : '{' bloque_sentencias_programa error",
"programa : bloque_sentencias_programa '}' error",
"programa : '{' '}' error",
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
"declaracion_funciones : encabezado_funcion '(' parametro_fun ')' '{' cuerpo_de_la_funcion '}'",
"declaracion_funciones : encabezado_funcion '(' ')' '{' cuerpo_de_la_funcion '}'",
"declaracion_funciones : encabezado_funcion '(' parametro_fun ')' '{' cuerpo_de_la_funcion error",
"declaracion_funciones : encabezado_funcion '(' parametro_fun ')' cuerpo_de_la_funcion '}' error",
"declaracion_funciones : encabezado_funcion '(' parametro_fun '{' cuerpo_de_la_funcion '}' error",
"declaracion_funciones : encabezado_funcion parametro_fun ')' '{' cuerpo_de_la_funcion '}' error",
"declaracion_funciones : encabezado_funcion '(' ')' '{' cuerpo_de_la_funcion error",
"declaracion_funciones : encabezado_funcion '(' ')' cuerpo_de_la_funcion '}' error",
"declaracion_funciones : encabezado_funcion '(' '{' cuerpo_de_la_funcion '}' error",
"declaracion_funciones : encabezado_funcion ')' '{' cuerpo_de_la_funcion '}' error",
"parametro_fun : tipo ID",
"encabezado_funcion : VOID ID",
"encabezado_funcion : VOID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' error",
"encabezado_funcion : VOID '(' ')' '{' cuerpo_de_la_funcion '}' error",
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
"sentencia_declatariva_especificas : declaracion_clase_hereda ','",
"sentencia_declatariva_especificas : declaracion_clases ','",
"sentencia_declatariva_especificas : declaracion_variables error",
"sentencia_declatariva_especificas : declaracion_funciones error",
"sentencia_declatariva_especificas : declaracion_objetos_clase error",
"declaracion_clase_hereda : ID",
"sentencias_retorno : RETURN",
"declaracion_clases : encabezado_clase '{' cuerpo_clase '}'",
"declaracion_clases : encabezado_clase",
"declaracion_clases : encabezado_clase '{' cuerpo_clase error",
"declaracion_clases : encabezado_clase cuerpo_clase '}' error",
"declaracion_clases : encabezado_clase '{' '}' error",
"encabezado_clase : CLASS ID",
"encabezado_clase : CLASS '{' cuerpo_clase '}' error",
"cuerpo_clase : cuerpo_clase sentencia_declatariva_especificas",
"cuerpo_clase : sentencia_declatariva_especificas",
"cuerpo_clase : sentencias_ejecutables error",
"declaracion_objetos_clase : ID list_objts_clase",
"list_objts_clase : list_objts_clase ';' ID",
"list_objts_clase : ID",
"list_objts_clase : list_objts_clase ID error",
"sentencias_ejecutables : sentencia_asignacion ','",
"sentencias_ejecutables : sentencias_IF ','",
"sentencias_ejecutables : sentencias_salida ','",
"sentencias_ejecutables : sentencias_control ','",
"sentencias_ejecutables : sentencias_ejecucion_funcion ','",
"sentencias_ejecutables : sentencias_retorno ','",
"sentencias_ejecutables : sentencia_asignacion error",
"sentencias_ejecutables : sentencias_IF error",
"sentencias_ejecutables : sentencias_salida error",
"sentencias_ejecutables : sentencias_control error",
"sentencias_ejecutables : sentencias_ejecucion_funcion error",
"sentencias_ejecucion_funcion : id_asig '(' expr_aritmetic ')'",
"sentencias_ejecucion_funcion : id_asig '(' ')'",
"sentencia_asignacion : id_asig '=' valor_asignacion",
"sentencia_asignacion : id_asig '=' error",
"id_asig : id_asig '.' ID",
"id_asig : ID",
"valor_asignacion : expr_aritmetic",
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
"sentencias_IF : IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables ELSE '{' bloque_sentencias_ejecutables '}' END_IF error",
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
"termino : TOF '(' factor ')'",
"factor : id_asig_der MENOS_MENOS",
"factor : id_asig_der",
"factor : const",
"id_asig_der : id_asig_der '.' ID",
"id_asig_der : ID",
"const : CTE",
"const : '-' CTE",
};

//#line 1646 "gramatica.y"

//funciones

public static ArrayList<String> auxDerAsig = new ArrayList<String>();

public static boolean tieneErrores = false;

public static ArrayList<String> errores = new ArrayList<String>();
private static NodoControl raiz;
private static NodoComun ptrRaizPrograma;

private static NodoComun ptrRaizFuncion;
private static NodoComun aux_raiz_func;

private static final NodoComun nodoError = new NodoComun("ERROR", null,null);

private static ArrayList<NodoControl> lista_func = new ArrayList<NodoControl>();

private static ArrayList<String> lista_identificadores = new ArrayList<String>();

private static String ambitoActual = "global";

private static String variable_menosMenos = "";

private static ArrayList<String> lista_tipos = new ArrayList<String>();

public static ArrayList<String> erroresSemanticos = new ArrayList<String>();
public static String auxTipoAsig = "";

public static String auxVarClases = "";

public static boolean tieneReturn = false; //variable utilizada para saber si las declaracion de funciones tiene la sentencia return
public static String auxConversion = "";
public static Simbolo simboloAuxConversion = null;

public static void buscarErroresEnNodo(ArbolSintactico arb){
    if(!tieneErrores){
        if(arb == nodoError){
            tieneErrores = true;
        }if(arb.getDer() != null) {
            buscarErroresEnNodo(arb.getDer());
        }
        if(arb.getIzq() != null) {
            buscarErroresEnNodo(arb.getIzq());
        }
    }
}


public static String nivelDeClaseCorrecto(String nombreClase){
    int nivel = 1;
    String claseHereda;
    Simbolo simboloClase = TablaDeSimbolos.obtenerSimboloSinAmbito(nombreClase);
    claseHereda = simboloClase.getHereda();
    String claseAnt = claseHereda;
    while(!claseHereda.equals("nadie") &&  nivel < 3){
        nivel = nivel+1;
        simboloClase = TablaDeSimbolos.obtenerSimbolo(claseHereda);
        claseAnt = claseHereda;
        claseHereda = simboloClase.getHereda();
    }
    if(nivel < 3){
        return "";
    }else{
        //String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: El nivel de la clase " + claseHereda+" supera el maximo";
        //erroresSemanticos.add(err);
        return claseAnt;
    }


}

public static boolean chequearTipos(Simbolo s1){
    if(s1.getId() != -1){
        if(!auxTipoAsig.equals("")){
            //es  una asignacion
            if(s1.getTipo().equals(auxTipoAsig)){
                return true;
            }else{
                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable " + s1.getLexema() + " con tipo incorrecto";
                erroresSemanticos.add(err);
                return false; 
            }
        }else{
            //es una expr_aritmetic suelta, por ejemplo en una COMPARACION
            auxTipoAsig = s1.getTipo();
            return true;
        }
    }else{
        return true;
    }
}
                                     
public static boolean agregarTipoALista(String lexema){
    Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexema + ambitoActual);
    if(simbolo != null){
        String tipo = simbolo.getTipo();
        if(lista_tipos.isEmpty()){
            lista_tipos.add(tipo);
            
        }else if(!lista_tipos.contains(tipo)){
            lista_tipos.add(tipo);
        }
        return true;
    }
    String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable " + lexema + " no declarada";
    erroresSemanticos.add(err);
    return false;
}


public static void generarArbol(ArbolSintactico arbl){
    if(raiz == null){
        //Si la raiz no existe la creo
        NodoComun sentencia = new NodoComun("Sentencia",null,null);
        NodoControl program = new NodoControl("Programa", sentencia);
        raiz = program;
        ptrRaizPrograma = sentencia;
    }
    if(ptrRaizPrograma.getHijoIzq() == null){
        //Si el nodo izquierdo es null, agrego la nueva sentencia ahi
        ptrRaizPrograma.setHijoIzq(arbl);

    }else{
        //si el nodo izq ya esta ocupado, creo un nuevo NodoControl "Sentencia" y añado la sentencia nueva en el nodo izq
        NodoComun nuevo = new NodoComun("Sentencia", arbl,null);
        ptrRaizPrograma.setHijoDer(nuevo);
        ptrRaizPrograma = nuevo; //seteo puntero;
    }
}

public static void generarArbolFunc(ArbolSintactico arbl){
    if(aux_raiz_func == null){
        //Si la raiz no existe la creo
        NodoComun sentencia = new NodoComun("Sentencia",null,null);
        aux_raiz_func = sentencia;
        ptrRaizFuncion = sentencia;
    }
    if(ptrRaizFuncion.getHijoIzq() == null){
        //Si el nodo izquierdo es null, agrego la nueva sentencia ahi
        ptrRaizFuncion.setHijoIzq(arbl);
    }else{
        //si el nodo izq ya esta ocupado, creo un nuevo NodoControl "Sentencia" y añado la sentencia nueva en el nodo izq
        NodoComun nuevo = new NodoComun("Sentencia", arbl,null);
        ptrRaizFuncion.setHijoDer(nuevo);
        ptrRaizFuncion = nuevo; //seteo puntero;
    }
}

public static void agregarArbol(String nombreFunc){
    NodoControl func = new NodoControl(nombreFunc,aux_raiz_func); 
    aux_raiz_func = null;
    ptrRaizFuncion = null;
    lista_func.add(func);
}

public static ArrayList<NodoControl> get_arboles(){
    ArrayList<NodoControl> aux = new ArrayList<NodoControl>();
    for(NodoControl n : lista_func){
        aux.add(n);
    }
    //lista_func.clear();
    return aux;
}


public NodoControl getRaiz(){
	return raiz;
}


public static void agregarError(String nuevoError){
    errores.add(nuevoError);
}

public static ArrayList<String> getErrores(){
    return errores;
}

public static void constanteNegativa(String lexemaSimbolo){
    if(lexemaSimbolo.substring(lexemaSimbolo.length() - 3).equals("_ui")){
        String err ="Linea " + AnalizadorLexico.getLineaActual() + ". ERROR LEXICO. NUMERO ENTERO SIN SIGNO NEGATIVO NO PERMITIDO.";				
        AnalizadorLexico.erroresLexicos.add(err);
    }else if(!lexemaSimbolo.contains(".")){
        String lexemanegativo = "-" + lexemaSimbolo;
        TablaDeSimbolos.agregarSimbolo(lexemanegativo, Constantes.CTE);
        TablaDeSimbolos.obtenerSimbolo(lexemanegativo).setTipo("LONG");
        TablaDeSimbolos.obtenerSimbolo(lexemanegativo).setUso("constante");
        
        Constantes.tokens.put(lexemanegativo, Constantes.CTE);
    }else{
        String lexemanegativo = "-" + lexemaSimbolo;
        TablaDeSimbolos.agregarSimbolo(lexemanegativo, Constantes.CTE);
        TablaDeSimbolos.obtenerSimbolo(lexemanegativo).setTipo("FLOAT");
        TablaDeSimbolos.obtenerSimbolo(lexemanegativo).setUso("constante");
        Constantes.tokens.put(lexemanegativo, Constantes.CTE);
        
    }
}

public static void constantePositiva(String lexemaSimbolo){
    if((lexemaSimbolo.substring(lexemaSimbolo.length() - 2)).equals("_l")) {
        //es un numero entero largo
        long valor = Long.parseLong(lexemaSimbolo.substring(0, lexemaSimbolo.length() - 2)); //obtengo el valor numerico
        if(valor > Constantes.MAXIMO_VALOR_INT_LARGO-1) {
            String err ="Linea " + AnalizadorLexico.getLineaActual() + ". ERROR LEXICO. NUMERO ENTERO LARGO '" + valor +"' FUERA DE RANGO.";
            AnalizadorLexico.erroresLexicos.add(err);
        }
    } 
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
    //System.out.println("Yacc reporto error: " + error);
}
//#line 865 "Parser.java"
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
//#line 28 "gramatica.y"
{System.out.println("ARRANCO EL PROGRAMA");}
break;
case 2:
//#line 31 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa ");}
break;
case 3:
//#line 32 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta } al Final");}
break;
case 4:
//#line 33 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta { al Inicio");}
break;
case 5:
//#line 34 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta bloque de sentencias");}
break;
case 8:
//#line 41 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia DECLARATIVA");}
break;
case 9:
//#line 42 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia EJECUTABLE");
                                             ArbolSintactico arbAux = (ArbolSintactico) val_peek(0);
                                             buscarErroresEnNodo(arbAux);  
                                             generarArbol((ArbolSintactico) val_peek(0));
                                            }
break;
case 14:
//#line 55 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de variable");}
break;
case 15:
//#line 56 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de funcion");}
break;
case 16:
//#line 57 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de la clase");}
break;
case 17:
//#line 58 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion del objeto de clase");}
break;
case 18:
//#line 61 "gramatica.y"
{
                                                    for(String s : lista_identificadores){
                                                        if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                            TablaDeSimbolos.setTipo(val_peek(1).sval, s);
                                                            TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                            simbolo.setUso("identificador");
                                                        }else{
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                    }
                                                    lista_identificadores.clear();
                                                }
break;
case 19:
//#line 75 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el TIPO en la DECLARACION de la variable");}
break;
case 20:
//#line 76 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el identificador en la DECLARACION de la variable");}
break;
case 21:
//#line 79 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION CON PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    String ambitoParametro = ambitoActual;
                                                    ambitoActual = ambitoActual.substring(0, indice);

                                                    Simbolo simbolParametro = TablaDeSimbolos.obtenerSimbolo(val_peek(4).sval+"#"+ambitoParametro);
                                                    TablaDeSimbolos.obtenerSimbolo(val_peek(6).sval).setParametro(simbolParametro);

                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(6).sval+"#"+ambitoActual, simbolParametro.getLexema())){
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema(val_peek(6).sval, val_peek(6).sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(6).sval + "#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            simbolo.setParametro(simbolParametro);
                                                            agregarArbol(val_peek(6).sval+"#"+ambitoActual);
                                                            tieneReturn = false; 
                                                        }else{
                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Falta la sentencia return en la declaracion de funcion";
                                                            erroresSemanticos.add(err);    
                                                        }
                                                    }else{
                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Funcion re declarada en el mismo ambito";
                                                        erroresSemanticos.add(err);
                                                    }
                                                     
                                                }
break;
case 22:
//#line 105 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION SIN PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    ambitoActual = ambitoActual.substring(0, indice);
                                                   
                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(5).sval+"#"+ambitoActual, "nulo")){ 
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema(val_peek(5).sval, val_peek(5).sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(5).sval + "#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            agregarArbol(val_peek(5).sval+"#"+ambitoActual); 
                                                            tieneReturn = false; 
                                                        }else{
                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Falta la sentencia return en la declaracion de funcion";
                                                            erroresSemanticos.add(err);    
                                                        }
                                                    }else{
                                                        String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Funcion re declarada en el mismo ambito";
                                                        erroresSemanticos.add(err);
                                                    }   
                                        }
break;
case 23:
//#line 125 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' al final de la funcion");}
break;
case 24:
//#line 126 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
break;
case 25:
//#line 127 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
break;
case 26:
//#line 128 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
break;
case 27:
//#line 130 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' en la declaracion de la funcion");}
break;
case 28:
//#line 131 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
break;
case 29:
//#line 132 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
break;
case 30:
//#line 133 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
break;
case 31:
//#line 136 "gramatica.y"
{
                        
                        Simbolo simbolParametro = TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval);
                        TablaDeSimbolos.modificarLexema(val_peek(0).sval,val_peek(0).sval+"#"+ambitoActual);
                        simbolParametro.setUso("identificador");
                        simbolParametro.setTipo(val_peek(1).sval);
                        simbolParametro.setValorAsignado(true);
                        yyval = val_peek(0);
                        }
break;
case 32:
//#line 147 "gramatica.y"
{ 
                                ambitoActual = ambitoActual+"#"+val_peek(0).sval;
                                yyval = val_peek(0);
                            }
break;
case 33:
//#line 152 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
break;
case 34:
//#line 154 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
break;
case 35:
//#line 158 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");
            yyval = val_peek(0);
            }
break;
case 36:
//#line 161 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");
            yyval = val_peek(0);
            }
break;
case 37:
//#line 164 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");
            yyval = val_peek(0);
            }
break;
case 38:
//#line 169 "gramatica.y"
{ 
                                                lista_identificadores.add(val_peek(0).sval); 
                                              }
break;
case 39:
//#line 172 "gramatica.y"
{ 
                         lista_identificadores.add(val_peek(0).sval);
                        }
break;
case 43:
//#line 182 "gramatica.y"
{ generarArbolFunc((ArbolSintactico) val_peek(0));}
break;
case 49:
//#line 190 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de varaible");}
break;
case 50:
//#line 191 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de funcion");}
break;
case 51:
//#line 192 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de objeto");}
break;
case 52:
//#line 195 "gramatica.y"
{  if(auxVarClases.equals("")){

                                        int indice = ambitoActual.lastIndexOf('#');
                                        String lexema = val_peek(0).sval+"#"+ambitoActual.substring(0, indice);
                                        if(TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval).getId() != -1){
                                            auxVarClases = lexema;
                                        }else{
                                            String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: No se declaro la clase " + lexema;
                                            erroresSemanticos.add(err);    
                                            auxVarClases = "";
                                        }
                                }else{
                                        auxVarClases = auxVarClases+"#"+val_peek(0).sval; /* concateno las dos clases para demostrar que hay un error*/
                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: No se permite heredar de mas de una clase.";
                                        erroresSemanticos.add(err); 
                                }
                            }
break;
case 53:
//#line 213 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio RETURN"); 
                            if(!ambitoActual.equals("global")){
                                yyval = (ArbolSintactico) new NodoHoja(val_peek(0).sval);
                                tieneReturn = true;
                            }else{
                                String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Return fuera del ambito de una funcion";
                                erroresSemanticos.add(err);
                                yyval = nodoError;
                            }
                            }
break;
case 54:
//#line 227 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE");
                                                            
                                                            int indiceLexClase = ambitoActual.lastIndexOf('#');
                                                            String identClase = val_peek(3).sval +"#"+ ambitoActual.substring(0, indiceLexClase);
                                                            
                                                            if((TablaDeSimbolos.obtenerSimbolo(identClase).getUso().equals("")) || (TablaDeSimbolos.obtenerSimbolo(identClase).getClaseAPosterior())){
                                                               if(!auxVarClases.equals("")){
                                                                    String claseHereda = nivelDeClaseCorrecto(auxVarClases); 
                                                                    if(claseHereda.equals("")){
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases);
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);
                                                                    }else{
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases); /*agrego igual de la clase que hereda por si luego hay mas clases*/
                                                                        String err = "Linea: "+AnalizadorLexico.getLineaActual()+". Error Semantico: El nivel de herencia es superior a 3 por: " + claseHereda;
                                                                        erroresSemanticos.add(err);
                                                                    }
                                                                }else{
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setHereda("nadie");
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);
                                                                }

                                                                TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: Clase " + val_peek(3).sval+" redeclarada";
                                                                erroresSemanticos.add(err);
                                                            }

                                                            
                                                            auxVarClases = "";
                                                            int indice = ambitoActual.lastIndexOf('#');
                                                            ambitoActual = ambitoActual.substring(0, indice);
                                                        }
break;
case 55:
//#line 261 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE A POSTERIOR");
                                        int indiceLexClase = ambitoActual.lastIndexOf('#');
                                        String identClase = val_peek(0).sval +"#"+ ambitoActual.substring(0, indiceLexClase);
                                        if((TablaDeSimbolos.obtenerSimbolo(identClase).getUso().equals(""))){                    
                                            TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(true);
                                            TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                        }
                                        auxVarClases = "";
                                        int indice = ambitoActual.lastIndexOf('#');
                                        ambitoActual = ambitoActual.substring(0, indice);
                                        /*TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setClasePosterior(true);*/
                                        /*TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setUso("clase");*/
                                    }
break;
case 56:
//#line 274 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() + "Error sintactico.Falta } al final de la clase");}
break;
case 57:
//#line 275 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta '{' al inicio de la clase");}
break;
case 58:
//#line 276 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta cuerpo de la clase");}
break;
case 59:
//#line 279 "gramatica.y"
{
                        yyval = val_peek(0);

                        String lexema = val_peek(0).sval +"#"+ ambitoActual;
                       
                        TablaDeSimbolos.modificarLexema(val_peek(0).sval, lexema);
                        ambitoActual = ambitoActual + "#" + val_peek(0).sval;
                }
break;
case 60:
//#line 287 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta el identificador de la clase");}
break;
case 63:
//#line 293 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico al compilar no permite declarar sentencia ejecutables dentro de una funcion");}
break;
case 64:
//#line 296 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de OBJETO DE CLASE");
                                                    for(String s : lista_identificadores){
                                                        if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                            TablaDeSimbolos.setTipo(val_peek(1).sval, s);
                                                            TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                            simbolo.setUso("identificador");

                                                            TablaDeSimbolos.borrarSimbolo(s);
                                                        }else{
                                                            /*doy error por re declaracion del identificador*/
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                    }
                                                    lista_identificadores.clear();   
                                                }
break;
case 65:
//#line 315 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 66:
//#line 316 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 67:
//#line 317 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico. Al compilar no permite objetos de clase separados por ,");}
break;
case 68:
//#line 321 "gramatica.y"
{yyval = val_peek(1); }
break;
case 69:
//#line 322 "gramatica.y"
{yyval = val_peek(1);}
break;
case 70:
//#line 323 "gramatica.y"
{yyval = val_peek(1);}
break;
case 71:
//#line 324 "gramatica.y"
{ yyval = val_peek(1);}
break;
case 72:
//#line 325 "gramatica.y"
{yyval = val_peek(1);}
break;
case 73:
//#line 326 "gramatica.y"
{yyval = val_peek(1);}
break;
case 74:
//#line 327 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la ASIGNACION");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 75:
//#line 330 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia IF");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 76:
//#line 333 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia de SALIDA"); 
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 77:
//#line 336 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia de CONTROL");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 78:
//#line 339 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la ejecucion de FUNCION");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 79:
//#line 344 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION CON PARAMETRO");
                                                            String lexema = "";
                                                            String idAnterior = "";
                                                            for(int i = lista_identificadores.size()-1; i >= 0;i--){
                                                                System.out.println("EL LEXEMA DE LA FUNCION HASTA AHORA ES: " + lexema);
                                                                /* System.out.println("nombre: " + lista_identificadores.get(i));*/
                                                                if(lexema.equals("")){
                                                                    lexema = lista_identificadores.get(i)+"#"+ambitoActual;
                                                                    idAnterior = lista_identificadores.get(i);
                                                                }else{
                                                                    Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual);
                                                                    /* System.out.println("EL SIMBOLO DE LA CLASE "+lista_identificadores.get(i)+"#"+ambitoActual +"ES: " + simbol.getLexema());*/
                                                                    System.out.println("El simbolo anterior es: " + idAnterior + " el simbolo nuevo es: " + simbol.getLexema());
                                                                    if(simbol.getId() != -1){
                                                                        if(TablaDeSimbolos.obtenerSimbolo(idAnterior+"#"+ambitoActual+"#"+lista_identificadores.get(i)).getUso().equals("funcion")){
                                                                            if(TablaDeSimbolos.obtenerSimbolo(simbol.getTipo()+"#"+ambitoActual).getUso().equals("clase")){
                                                                                /*quiere decir que el simbolo es el primer ident, es decir el objeto de clase. Por eso uso el tipo de ese objeto*/
                                                                                lexema = lexema + "#" + simbol.getTipo();
                                                                                idAnterior = simbol.getTipo();
                                                                            }else{
                                                                                /*quiere decir que tengo mas clases en el identificador*/
                                                                                lexema = lexema +"#"+ lista_identificadores.get(i);
                                                                                idAnterior = lista_identificadores.get(i);
                                                                            }

                                                                        }else{
                                                                            if(TablaDeSimbolos.obtenerSimbolo(simbol.getLexema()).getHereda().equals(idAnterior+"#"+ambitoActual) || TablaDeSimbolos.obtenerSimbolo(simbol.getTipo()+"#"+ambitoActual).getHereda().equals(idAnterior+"#"+ambitoActual)){
                                                                                /* quiere decir que la clase hereda de la otra y que ambos lex son el lexema*/
                                                                                /* o que el lexema actual es el nombre de un objeto clase y tengo que usar su tipo para comprobar si hereda*/
                                                                                idAnterior = lista_identificadores.get(i);
                                                                            }else{
                                                                                /* AGREGADO PERO PARA HACE MAS KILOMBO*/
                                                                                if(idAnterior.equals(TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo())){
                                                                                    idAnterior = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo();
                                                                                }else if(idAnterior.equals(lista_identificadores.get(i))){
                                                                                    idAnterior = lista_identificadores.get(i);
                                                                                }else{
                                                                                    lexema = "";
                                                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase " + idAnterior + " no hereda de "+ lista_identificadores.get(i);
                                                                                    erroresSemanticos.add(err);
                                                                                    yyval = nodoError;
                                                                                    break;
                                                                                } 
                                                                                /*
                                                                                lexema = "";
                                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase " + idAnterior + " no hereda de "+ lista_identificadores.get(i);
                                                                                erroresSemanticos.add(err);
                                                                                $$ = nodoError;
                                                                                break;
                                                                                */
                                                                            }
                                                                        }
                                                                        
                                                                        
                                                                    }else{
                                                                        /* Hago el intento de ver si el identificador actual es un objeto de clase dentro de una clase*/
                                                                        /* pruebo agregandole al ambito el tipo o nombre del siguiente identificador del for*/
                                                                        if(idAnterior.equals(lista_identificadores.get(i)+"#"+ambitoActual)){
                                                                            idAnterior = lista_identificadores.get(i);
                                                                        }else{
                                                                            simbol = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+lista_identificadores.get(i-1));
                                                                            if(simbol.getId() != -1){
                                                                                idAnterior = lista_identificadores.get(i-1);
                                                                            }else{
                                                                                /* pregunto si el tipo del siguiente id es una clase*/
                                                                                simbol = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+ TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i-1)+"#"+ambitoActual).getTipo());
                                                                                if(simbol.getId() != -1){
                                                                                    /* NO TENGO NI IDEA DE SI ESTO FUNCIONA. YA ESTOY PROGRAMANDO EN MODO AUTOMATICO*/
                                                                                    idAnterior = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i-1)+"#"+ambitoActual).getTipo();
                                                                                }else{
                                                                                    lexema = "";
                                                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase no existe";
                                                                                    erroresSemanticos.add(err);
                                                                                    yyval = nodoError;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        /*
                                                                        lexema = "";
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase no existe";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                        break;
                                                                        */
                                                                    }
                                                                }
                                                            }

                                                            System.out.println("EL LEXEMA DE LA FUNCION ES: " + lexema + " auxTipoAsig: "+auxTipoAsig);

                                                            Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexema);
                                                            /*System.out.println("La funcion encontrada es: " + simbol.getLexema());*/
                                                            if(simbol != null && simbol.getParametro() != null){
                                                                simbol.setUsada(true); /*SETEO en USADA LA VARIABLE */
                                                               if(simbol.getParametro().getTipo().equals(auxTipoAsig)){
                                                                    /*NodoHoja id_func = new NodoHoja(lexema);*/
                                                                    NodoHoja id_func = new NodoHoja(simbol.getLexema());
                                                                    
                                                                   /* System.out.println("AGREGO AL ARBOL LA FUNCION CON EL NOMBRE: " + id_func.getLex());*/
                                                                    /*System.out.println("EL simbolo: " + simbol.getLexema());*/
                                                                    yyval = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,(ArbolSintactico)val_peek(1));
                                                               }else{
                                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                                    erroresSemanticos.add(err);
                                                                    yyval = (ArbolSintactico) nodoError;
                                                               }
                                                            }else{
                                                                String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lexema+" no fue declarada";
                                                                erroresSemanticos.add(err);
                                                                yyval = (ArbolSintactico) nodoError;
                                                            }
                                                            /*
                                                            Simbolo simbol = TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual);
                                                            //System.out.println("La funcion encontrada es: " + simbol.getLexema());
                                                            if(simbol != null && simbol.getParametro() != null){
                                                                simbol.setUsada(true); //SETEO en USADA LA VARIABLE 
                                                               if(simbol.getParametro().getTipo().equals(auxTipoAsig)){
                                                                    //NodoHoja id_func = new NodoHoja($1.sval+"#"+ambitoActual);
                                                                    NodoHoja id_func = new NodoHoja(simbol.getLexema());
                                                                    
                                                                   // System.out.println("AGREGO AL ARBOL LA FUNCION CON EL NOMBRE: " + id_func.getLex());
                                                                    //System.out.println("EL simbolo: " + simbol.getLexema());
                                                                    $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,(ArbolSintactico)$3);
                                                               }else{
                                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                                    erroresSemanticos.add(err);
                                                                    $$ = (ArbolSintactico) nodoError;
                                                               }
                                                            }else{
                                                                String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + $1.sval+" no fue declarada";
                                                                erroresSemanticos.add(err);
                                                                $$ = (ArbolSintactico) nodoError;
                                                            }*/
                                                            lista_identificadores.clear();
                                                            auxTipoAsig ="";
                                                            
                                                        }
break;
case 80:
//#line 482 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION SIN PARAMETRO");
                                          Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(val_peek(2).sval+"#"+ambitoActual);
                                          if(simbol.getId() != -1 && simbol.getParametro() == null){
                                                simbol.setUsada(true);
                                                /*NodoHoja id_func = new NodoHoja($1.sval+"#"+ambitoActual);*/
                                                NodoHoja id_func = new NodoHoja(simbol.getLexema());
                                                yyval = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func, null);
                                          }else{
                                                if(simbol.getParametro() != null){
                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Falta el parametro en la funcion";
                                                    erroresSemanticos.add(err);
                                                    yyval = (ArbolSintactico) nodoError;  
                                                }else{
                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: La funcion " + val_peek(2).sval+" no fue declarada";
                                                    erroresSemanticos.add(err);
                                                    yyval = (ArbolSintactico) nodoError;
                                                }

                                          }
                                                auxTipoAsig ="";
                                         }
break;
case 81:
//#line 525 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una ASIGNACION");
                                                        String lexema = "";
                                                        String primer = "";
                                                        String ambitoAnt = "";
                                                        String ambitoVariable = ambitoActual;
                                                        /*VERIFICO SI LA LISTA ES DISTINTO DE 0. SI ES DISTINTO DE 0 QUIERE DECIR QUE DEL LADO IZQUIERDO ESTA EL ID*/
                                                        if(lista_identificadores.size() > 0){
                                                            /*VERIFICO SI LA LISTA ES MAYOR A 1. SI ES MAYOR A 1 QUIERE DECIR QUE ES UN IDENTIFICADOR COMPUESTO, POR EJ, CLASE1.VAR1*/
                                                            if(lista_identificadores.size() > 1){
                                                                String lexemaAuxFinAmbito =""; /*aux usado para el ambito*/
                                                                Simbolo simboloAnt = null; /*este simbolo es el anterior, por ejemplo en clase1.a.num1 puede ser simboloAnt: clase1*/
                                                                /*RECORRO LA LISTA DE IDENTIFICADORES*/
                                                                for(int i = 0; i< lista_identificadores.size();i++){
                                                                    System.out.println("LISTA ID TIENE: " + lista_identificadores.get(i));
                                                                    if(lexema.equals("")){
                                                                        /*ES EL PRIMER ELEMENTO DEL LEXEMA*/
                                                                        lexema = lista_identificadores.get(i); 
                                                                        simboloAnt = TablaDeSimbolos.obtenerSimbolo(lexema+"#"+ambitoActual);
                                                                        lexemaAuxFinAmbito = TablaDeSimbolos.obtenerSimbolo(lexema+"#"+ambitoActual).getTipo();
                                                                    }else{
                                                                        /*LEXEMA YA TIENE ALGUN VALOR*/
                                                                        Simbolo simboloNuevo = null;
                                                                        System.out.println("ESTO BUSCO COMO NUEVOSIMBOLO EN ASIG: " + lista_identificadores.get(i)+"#"+ambitoActual+"#"+lexemaAuxFinAmbito);
                                                                        simboloNuevo = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+lexemaAuxFinAmbito);   /*lexemaAuxFinAmbito es a*/
                                                                        /*SI EL SIMBOLO EXISTE EN LA TABLA DE SIMBOLOS*/
                                                                        if(simboloNuevo.getId() != -1){   
                                                                            /*SI EL SIMBOLO ES UNA CLASE*/
                                                                            if(simboloNuevo.getUso().equals("clase")){ /*si es una clase*/
                                                                                /*simboloTipoAnt SE REFIERE AL TIPO DEL SIMBOLO ANTERIOR, POR EJ, EL SIMBOLO ANT ES CLASE1 Y SU TIPO ES C1, ENTONCES OBTENGO EL SIMBOLO DE C1*/
                                                                                Simbolo simboloTipoAnt = null;
                                                                                /*SI EL SIMBOLO ANTERIOR ES UN OBJETO DE UNA CLASE, POR EJEMPLO, CLASE1*/
                                                                                if(simboloAnt.getUso().equals("identificador")){
                                                                                    simboloTipoAnt = TablaDeSimbolos.obtenerSimbolo(simboloAnt.getTipo()+"#"+ambitoActual); /*aca lo que hago es obtener el identificador de la tabla de la clase que es el identificador anterior*/
                                                                                }else{
                                                                                    /*SINO QUIERE DECIR QUE ES C1*/
                                                                                    simboloTipoAnt = simboloAnt;
                                                                                }
                                                                                /*SI EL SIMBOLO ANTERIOR HEREDA DEL NUEVO SIMBOLO*/
                                                                                if(simboloTipoAnt.getHereda().equals(simboloNuevo.getLexema()) && !simboloNuevo.getClaseAPosterior()){
                                                                                    /*AGREGO EL NOMBRE AL LEXEMA Y SIGO PROCESANDO*/
                                                                                     lexema = lexema+"."+lista_identificadores.get(i);
                                                                                     lexemaAuxFinAmbito = lista_identificadores.get(i);
                                                                                     simboloAnt = simboloNuevo;
                                                                                }else{
                                                                                    if(!simboloNuevo.getClaseAPosterior()){
                                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase no hereda de "+simboloNuevo.getLexema();
                                                                                        erroresSemanticos.add(err);
                                                                                        yyval = nodoError;
                                                                                        break;
                                                                                    }else{
                                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: Error de Forward declaration en la clase "+simboloNuevo.getLexema();
                                                                                        erroresSemanticos.add(err);
                                                                                        yyval = nodoError;
                                                                                        break;
                                                                                    }
                                                                                }

                                                                            }else{ 
                                                                                /*SI EL SIMBOLO NO ES UNA CLASE, ENTONCES QUIERE DECIR QUE ES EL ULTIMO IDENTIFICADOR, POR EJEMPLO, SI EL IDENTIFICADOR*/
                                                                                /*ORIGINAL ERA CLASE1.CLASE1.VARIABLE1, EN ESTE ESLE LO QUE HAGO ES AGREGAR VARIABLE1 AL LEXEMA                                                                     */
                                                                                lexema = lexema+"."+lista_identificadores.get(i)+"#"+ambitoActual; /*Aca hago clase1.num1#global. Asi la voy a guardar en la Tabla de simbolos*/
                                                                                TablaDeSimbolos.agregarSimbolo(lexema,Constantes.ID);
                                                                                /*CREO EL NUEVO SIMBOLO Y SETEO SUS ATRIBUTOS*/
                                                                                Simbolo nuevo = TablaDeSimbolos.obtenerSimbolo(lexema);
                                                                                nuevo.setTipo(simboloNuevo.getTipo());
                                                                                nuevo.setUso(simboloNuevo.getUso());
                                                                                nuevo.setValor(simboloNuevo.getValor());
                                                                                
                                                                             }
                                                                        }else{
                                                                            String err = "Linea: "  + AnalizadorLexico.getLineaActual()+". Error Semantico: variable no declarada";
                                                                            erroresSemanticos.add(err);
                                                                            yyval = nodoError;
                                                                            break;
                                                                        }
                                                                    } 
                                                                }
                                                                
                                                              


                                                            }else{
                                                                
                                                                lexema = lista_identificadores.get(0)+"#"+ambitoActual;
                                                            }
                                                        }
                                                        
                                                /*SI EL LEXEMA QUE FORME EXISTE EN LA TABLA DE SIMBOLOS*/
                                                if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() != -1){
                                                    Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexema); /*Obtengo el simbolo del lado izquierdo que acabo de crear o que ya existia*/
                                                    
                                                    ArbolSintactico arb = (ArbolSintactico) val_peek(0);
                                                    
                                                    String aux ="";
                                                    ArbolSintactico arb_aux = arb;
                                                    /*CON ESTE WHILE OBTENGO EL TIPO DEL LADO IZQUIERDO DE LA ASIGNACION, EN CASO DE QUE EL NODO PADRE NO SEA UN NUMERO O VARIABLE, SINO UN +,-,ETC*/
                                                    while(aux.equals("")){
                                                        if(TablaDeSimbolos.obtenerSimbolo(arb_aux.getLex()).getTipo().equals("")){
                                                            if(arb_aux.getIzq() != null){
                                                                arb_aux = arb_aux.getIzq();
                                                            }else{
                                                                aux = "nada";
                                                            }
                                                        }else{
                                                                aux = TablaDeSimbolos.obtenerSimbolo(arb_aux.getLex()).getTipo();
                                                        }
                                                    }

                                                    /*SI LOS TIPOS DE AMBOS LADOS DE LA ASIGNACION COINCIDEN*/
                                                    if(simbol.getTipo().equals(aux)){ 
                                                        if(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getId() != -1 && TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getUso().equals("constante")){ 
                                                            /*SI EL VALOR DEL LADO DERECHO DE UNA ASIGNACION ES UNA CONSTANTE LA ASIGNO*/
                                                            simbol.setValor(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getLexema());
                                                        }
                                                        /*CREO EL NODO HOJA CON EL LEXEMA DEL LADO IZQUIERDO*/
                                                        NodoHoja hoja = new NodoHoja(simbol.getLexema());
                                                        simbol.setValorAsignado(true);
                                                        /*CREO EL SUBARBOL DE LA ASIGNACION*/
                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, hoja , (ArbolSintactico) val_peek(0));  /*Aca hago nodo --- = ---- lo que genere*/
                                                    }else{
                                                        String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo de la asignacion es incorrecto";
                                                        erroresSemanticos.add(err);
                                                        yyval = (ArbolSintactico) nodoError;    
                                                    }

                                                }else{
                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: La variable de la asignacion no existe";
                                                    erroresSemanticos.add(err);
                                                    yyval = nodoError;
                                                }
                                                        
                                                /*VUELVO A COLOCAR LAS VARIABLES AUXILIARES EN VALORES NULOS*/
                                                if(simboloAuxConversion != null)
                                                    simboloAuxConversion.setTipo(auxConversion);
                                                simboloAuxConversion = null;
                                                auxConversion = "";      
                                                lista_identificadores.clear();
                                                auxTipoAsig = "";
                                            }
break;
case 82:
//#line 664 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la expresion despues del '='");
                                    yyval = (ArbolSintactico) nodoError;
                                    }
break;
case 83:
//#line 669 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 84:
//#line 670 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 85:
//#line 673 "gramatica.y"
{yyval = val_peek(0);
                                   
                                    }
break;
case 86:
//#line 678 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF_ELSE");
                        if((ArbolSintactico)val_peek(9) != nodoError){
                            NodoControl then = new NodoControl("then",(ArbolSintactico) val_peek(6));
                            NodoControl _else = new NodoControl("else", (ArbolSintactico) val_peek(2));
                            NodoComun cuerpo = new NodoComun("cuerpo", then, _else);
                        
                            NodoControl condicion = new NodoControl("Condicion",(ArbolSintactico) val_peek(9));
                            yyval = (ArbolSintactico) new NodoComun(val_peek(11).sval, condicion , cuerpo); 
                        }else{
                            yyval = val_peek(9);
                        }
                  
                                                                                                                                    
                    }
break;
case 87:
//#line 693 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF");
                        if((ArbolSintactico)val_peek(5) != nodoError){                                                                     
                            NodoControl then = new NodoControl("then",(ArbolSintactico) val_peek(2));
                            NodoControl condicion = new NodoControl("Condicion",(ArbolSintactico) val_peek(5));
                            yyval = (ArbolSintactico) new NodoComun(val_peek(7).sval, condicion , then);                                                                                                                  
                        }else{
                            yyval = val_peek(5);
                        }
                    }
break;
case 88:
//#line 705 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta END_IF para finalizar la sentencia IF");
                    System.out.println("ENTRE EN EL ERROR DE QUE FALTA ELSE_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 89:
//#line 709 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 90:
//#line 712 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 91:
//#line 715 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF y ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 92:
//#line 718 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 93:
//#line 721 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras ELSE Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 94:
//#line 724 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF, ELSE Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 95:
//#line 727 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 96:
//#line 730 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 97:
//#line 733 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloques sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 98:
//#line 736 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 99:
//#line 739 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 100:
//#line 742 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 101:
//#line 745 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 102:
//#line 748 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 103:
//#line 751 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 104:
//#line 754 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' antes del ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 105:
//#line 761 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR");
                                                        
                                                        

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());/*Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            yyval = nodoError;
                                                        }
                                                        
                                                        auxTipoAsig = "";
                                                        }
break;
case 106:
//#line 825 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR");
                                                    ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());/*Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            yyval = nodoError;
                                                        }
                                                }
break;
case 107:
//#line 887 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR O IGUAL");
                                                        ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());/*Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            yyval = nodoError;
                                                        }
                                                        }
break;
case 108:
//#line 949 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR O IGUAL");
                                                        ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());/*Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            yyval = nodoError;
                                                        }
                                                        }
break;
case 109:
//#line 1011 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR IGUAL");
                                                            ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                   if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());/*Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            yyval = nodoError;
                                                        }
                                                        }
break;
case 110:
//#line 1073 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR DISTINTO");
                                                                    ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                   if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());/*Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); /*Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval, (ArbolSintactico) val_peek(2) , (ArbolSintactico) val_peek(0));                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            yyval = nodoError;
                                                        }
                                                                    }
break;
case 111:
//#line 1137 "gramatica.y"
{
                                                                        yyval=new NodoComun("Sentencia_Dentro_IF", (ArbolSintactico) val_peek(1), (ArbolSintactico) val_peek(0));}
break;
case 112:
//#line 1139 "gramatica.y"
{yyval=val_peek(0);}
break;
case 113:
//#line 1142 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia SALIDA con CADENA");
                                 NodoHoja cadena = new NodoHoja(val_peek(0).sval);
                                 NodoControl nodo = new NodoControl(val_peek(1).sval, (ArbolSintactico) cadena);
                                 yyval = (ArbolSintactico) nodo; 
                                }
break;
case 114:
//#line 1149 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una SENTENCIA WHILE");
                                        yyval = val_peek(0);
                                       }
break;
case 115:
//#line 1154 "gramatica.y"
{
                                                            if((ArbolSintactico) val_peek(5) != nodoError){
                                                                NodoControl condicion = new NodoControl("condicion_while", (ArbolSintactico) val_peek(5));
                                                                NodoControl cuerpo_while = new NodoControl("cuerpo_while", (ArbolSintactico) val_peek(1));
                                                                yyval = (ArbolSintactico) new NodoComun(val_peek(7).sval, condicion, cuerpo_while);
                                                            }else{
                                                                yyval = val_peek(5);
                                                            }
                                                            }
break;
case 116:
//#line 1163 "gramatica.y"
{
                                                                NodoControl condicion = new NodoControl("condicion_while", (ArbolSintactico) val_peek(4));
                                                                NodoControl cuerpo_while = new NodoControl("cuerpo_while", null);
                                                                yyval = (ArbolSintactico) new NodoComun(val_peek(6).sval, condicion, cuerpo_while);
                                                                }
break;
case 117:
//#line 1169 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra DO");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 118:
//#line 1172 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra WHILE");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 119:
//#line 1175 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 120:
//#line 1181 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una SUMA");
                                            
                                             ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                                             ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;


                                            if(arbIzq.getLex().equals("TOF")){
                                            	simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                             }else{
                                                
                                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             }
                                             
                                             if(arbDer.getLex().equals("TOF")){
                                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                             }else{
                                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             }
                                            

                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                    if(simbolo1.getUso().equals("constante") && simbolo2.getUso().equals("constante")){
                                                            if(simbolo1.getTipo().equals("UINT")){
                                                                int resultado = Integer.parseInt(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 3)) + Integer.parseInt(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 3)); 
                                                                TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_ui",Constantes.CTE);
                                                                Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_ui");
                                                                s.setTipo("UINT");
                                                                s.setUso("constante");
                                                                NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_ui");
                                                                yyval = (ArbolSintactico) resul;
                                                            }else if(simbolo1.getTipo().equals("LONG")){
                                                                long resultado = Long.parseLong(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 2)) + Long.parseLong(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 2)); 
                                                                TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_l",Constantes.CTE);
                                                                Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_l");
                                                                s.setTipo("LONG");
                                                                s.setUso("constante");
                                                                NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_l");
                                                                yyval = (ArbolSintactico) resul;
                                                            }else{
                                                                float resultado = Float.parseFloat(simbolo1.getLexema()) + Float.parseFloat(simbolo2.getLexema()); 
                                                                TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado),Constantes.CTE);
                                                                Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado));
                                                                s.setTipo("FLOAT");
                                                                s.setUso("constante");
                                                                NodoHoja resul = new NodoHoja(String.valueOf(resultado));
                                                                yyval = (ArbolSintactico) resul;
                                                            }
                                                    }else{
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                            if(simbolo1.getValorAsignado()){ 
                                                                if(simbolo2.getUso().equals("identificador")){ 
                                                                    if(simbolo2.getValorAsignado()){                                                
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0)); /*Hago $1 ------- * ------- $2*/
                                                                        
                                                                

                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = (ArbolSintactico) nodoError;
                                                                    }
                                                                }else{
                                                                    yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0));
                                                                    

                                                                }
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                erroresSemanticos.add(err);
                                                                yyval = (ArbolSintactico) nodoError;
                                                            }
                                                        }else{
                                                            yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0));

                                                        

                                                        }
                                                        
                                                    }
                                                }else{
                                                    yyval = nodoError;
                                                }
                                             }else{
                                                yyval = nodoError;
                                             } 
                                            }
break;
case 121:
//#line 1270 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una RESTA");

                                             ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                                             ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;

                                             if(arbIzq.getLex().equals("TOF")){
                                            	simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                             }else{
                                                
                                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             }
                                             
                                             if(arbDer.getLex().equals("TOF")){
                                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                             }else{
                                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             }

                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                    /*Si son dos constantes hago la operacion y retorno la suma de ambos*/
                                                if(simbolo1.getUso().equals("constante") && simbolo2.getUso().equals("constante")){
                                                        if(simbolo1.getTipo().equals("UINT")){
                                                            int resultado = Integer.parseInt(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 3)) - Integer.parseInt(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 3)); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_ui",Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_ui");
                                                            s.setTipo("UINT");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_ui");
                                                            yyval = (ArbolSintactico) resul;
                                                        }else if(simbolo1.getTipo().equals("LONG")){
                                                            long resultado = Long.parseLong(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 2)) - Long.parseLong(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 2)); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_l",Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_l");
                                                            s.setTipo("LONG");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_l");
                                                            yyval = (ArbolSintactico) resul;
                                                        }else{
                                                            float resultado = Float.parseFloat(simbolo1.getLexema()) - Float.parseFloat(simbolo2.getLexema()); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado),Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado));
                                                            s.setTipo("FLOAT");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado));
                                                            yyval = (ArbolSintactico) resul;
                                                        }
                                                    }else{
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                            if(simbolo1.getValorAsignado()){ 
                                                                if(simbolo2.getUso().equals("identificador")){ 
                                                                    if(simbolo2.getValorAsignado()){                                                
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0)); /*Hago $1 ------- * ------- $2*/
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = (ArbolSintactico) nodoError;
                                                                    }
                                                                }else{
                                                                    yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0));
                                                                }
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                erroresSemanticos.add(err);
                                                                yyval = (ArbolSintactico) nodoError;
                                                            }
                                                        }else{
                                                            yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0));
                                                        }   
                                                    }
                                                }else{
                                                    yyval = nodoError;
                                                }
                                             }else{
                                                yyval = nodoError;
                                             }
                                            }
break;
case 122:
//#line 1349 "gramatica.y"
{yyval = val_peek(0); 

                        }
break;
case 123:
//#line 1354 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             

                            ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                            ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                            Simbolo simbolo1;
                            Simbolo simbolo2;
                            if(arbIzq.getLex().equals("TOF")){
                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                            }else{
                            
                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                            }
                            
                            if(arbDer.getLex().equals("TOF")){
                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                            }else{
                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                            }


                            if(arbIzq != nodoError && arbDer != nodoError){
                            if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                            if(simbolo1.getUso().equals("constante") && simbolo2.getUso().equals("constante")){
                                    if(simbolo1.getTipo().equals("UINT")){
                                        int resultado = Integer.parseInt(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 3)) * Integer.parseInt(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 3)); 
                                        TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_ui",Constantes.CTE);
                                        Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_ui");
                                        s.setTipo("UINT");
                                        s.setUso("constante");
                                        NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_ui");
                                        yyval = (ArbolSintactico) resul;
                                    }else if(simbolo1.getTipo().equals("LONG")){
                                        long resultado = Long.parseLong(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 2)) * Long.parseLong(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 2)); 
                                        TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_l",Constantes.CTE);
                                        Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_l");
                                        s.setTipo("LONG");
                                        s.setUso("constante");
                                        NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_l");
                                        yyval = (ArbolSintactico) resul;
                                    }else{
                                        float resultado = Float.parseFloat(simbolo1.getLexema()) * Float.parseFloat(simbolo2.getLexema()); 
                                        TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado),Constantes.CTE);
                                        Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado));
                                        s.setTipo("FLOAT");
                                        s.setUso("constante");
                                        NodoHoja resul = new NodoHoja(String.valueOf(resultado));
                                        yyval = (ArbolSintactico) resul;
                                    }
                                }else{
                                    if(simbolo1.getUso().equals("identificador")) {
                                        if(simbolo1.getValorAsignado()){ 
                                            if(simbolo2.getUso().equals("identificador")){ 
                                                if(simbolo2.getValorAsignado()){                                                
                                                    yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0)); /*Hago $1 ------- * ------- $2*/
                                                }else{
                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                    erroresSemanticos.add(err);
                                                    yyval = (ArbolSintactico) nodoError;
                                                }
                                            }else{
                                                yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0));
                                            }
                                        }else{
                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                            erroresSemanticos.add(err);
                                            yyval = (ArbolSintactico) nodoError;
                                        }
                                    }else{
                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0));
                                    }                                                          
                                }
                            }else{
                                yyval = nodoError;
                            }
                            }else{
                            yyval = nodoError;
                            }
                            }
break;
case 124:
//#line 1433 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una DIVISION");
                            ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                            ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                            Simbolo simbolo1;
                            Simbolo simbolo2;

                            if(arbIzq.getLex().equals("TOF")){
                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                            }else{
                            
                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                            }
                            
                            if(arbDer.getLex().equals("TOF")){
                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                            }else{
                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                            }

                            if(arbIzq != nodoError && arbDer != nodoError){
                            if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                            if(simbolo1.getUso().equals("constante") && simbolo2.getUso().equals("constante")){
                                    if(simbolo1.getTipo().equals("UINT")){
                                        int resultado = Integer.parseInt(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 3)) / Integer.parseInt(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 3)); 
                                        TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_ui",Constantes.CTE);
                                        Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_ui");
                                        s.setTipo("UINT");
                                        s.setUso("constante");
                                        NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_ui");
                                        yyval = (ArbolSintactico) resul;
                                    }else if(simbolo1.getTipo().equals("LONG")){
                                        long resultado = Long.parseLong(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 2)) / Long.parseLong(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 2)); 
                                        TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_l",Constantes.CTE);
                                        Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_l");
                                        s.setTipo("LONG");
                                        s.setUso("constante");
                                        NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_l");
                                        yyval = (ArbolSintactico) resul;
                                    }else{
                                        float resultado = Float.parseFloat(simbolo1.getLexema()) / Float.parseFloat(simbolo2.getLexema()); 
                                        TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado),Constantes.CTE);
                                        Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado));
                                        s.setTipo("FLOAT");
                                        s.setUso("constante");
                                        NodoHoja resul = new NodoHoja(String.valueOf(resultado));
                                        yyval = (ArbolSintactico) resul;
                                    }
                                }else{
                                    if(simbolo1.getUso().equals("identificador")) {
                                        if(simbolo1.getValorAsignado()){ 
                                            if(simbolo2.getUso().equals("identificador")){ 
                                                if(simbolo2.getValorAsignado()){                                                
                                                    yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0)); /*Hago $1 ------- * ------- $2*/
                                                }else{
                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                    erroresSemanticos.add(err);
                                                    yyval = (ArbolSintactico) nodoError;
                                                }
                                            }else{
                                                yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0));
                                            }
                                        }else{
                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                            erroresSemanticos.add(err);
                                            yyval = (ArbolSintactico) nodoError;
                                        }
                                    }else{
                                        yyval = (ArbolSintactico) new NodoComun(val_peek(1).sval,(ArbolSintactico)val_peek(2),(ArbolSintactico)val_peek(0));
                                    }                                                            
                                }
                            }else{
                                yyval = nodoError;
                            }
                            }else{
                            yyval = nodoError;
                            }
                            


                             
                        }
break;
case 125:
//#line 1514 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 126:
//#line 1515 "gramatica.y"
{    System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un FACTOR con CONVERSION DE TIPO");
                                    
                                NodoHoja factor = (NodoHoja) val_peek(1);
                                if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                    auxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo();
                                    simboloAuxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()); 
                                    TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); /*Cambio el tipo de la variable*/
                                    auxTipoAsig = "FLOAT";
                                    NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) val_peek(1));
                                    yyval = (ArbolSintactico) nodoTof;
                                }else{
                                    String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                    erroresSemanticos.add(err);
                                    yyval = (ArbolSintactico) nodoError;
                                }

                                
                            }
break;
case 127:
//#line 1535 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador con MENOS_MENOS");
                        String lexema = "";
                        for(String s : auxDerAsig){
                            if(lexema.equals("")){
                                lexema = s;
                            }else{
                                lexema = lexema+"."+s;
                            }
                        } 
                        auxDerAsig.clear();
                        lexema = lexema + "#"+ambitoActual;
                        
                        if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() != -1){

                            TablaDeSimbolos.obtenerSimbolo(lexema).setUsada(true);

                            System.out.println("ANTES DEL MENOS MENOS: " + TablaDeSimbolos.obtenerSimbolo(lexema).getLexema() + " vale: " + TablaDeSimbolos.obtenerSimbolo(lexema).getValor());

                            Simbolo simboloConst = new Simbolo(TablaDeSimbolos.obtenerSimbolo(lexema).getValor(), Constantes.CTE);
                            simboloConst.setUso("constante");
                            simboloConst.setTipo(TablaDeSimbolos.obtenerSimbolo(lexema).getTipo());
                            yyval = (ArbolSintactico) new NodoHoja(simboloConst.getLexema());

                            String resultadoNuevoValor = "";
                            String lexema2 = TablaDeSimbolos.obtenerSimbolo(lexema).getValor();
                                
                            switch(TablaDeSimbolos.obtenerSimbolo(lexema).getTipo()){
                                case "LONG":
                                    long resultadoL = Long.parseLong(lexema2.substring(0, lexema2.length() - 2))-1; 
                                    resultadoNuevoValor = String.valueOf(resultadoL+"_l");
                                    break;
                                case "UINT":
                                    int resultadoUI = Integer.parseInt(lexema2.substring(0, lexema2.length() - 3))-1;
                                    resultadoNuevoValor = String.valueOf(resultadoUI+"_ui");
                                    break;
                                case "FLOAT":
                                    float resultadoF = Float.parseFloat(lexema2) - 1.0f;
                                    resultadoNuevoValor = String.valueOf(resultadoF);
                                    break;
                            }
                            TablaDeSimbolos.obtenerSimbolo(lexema).setValor(resultadoNuevoValor);
                        }else{
                            String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+lexema+ " no declarada";
                            erroresSemanticos.add(err);
                            yyval = (ArbolSintactico) nodoError;
                        }
                        if(auxTipoAsig.equals("")){
                            auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(lexema).getTipo();
                        }

                        }
break;
case 128:
//#line 1586 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IDENTIFICADOR ");
            String lexema = "";
            for(String s : auxDerAsig){
                if(lexema.equals("")){
                    lexema = s;
                }else{
                    lexema = lexema+"."+s;
                }
            } 
            auxDerAsig.clear();
            lexema = lexema + "#"+ambitoActual;
            Simbolo aux = TablaDeSimbolos.obtenerSimbolo(lexema);
            if(aux.getId() != -1){
                aux.setUsada(true);
                NodoHoja hoja = new NodoHoja(aux.getLexema());
                yyval = (ArbolSintactico) hoja;
            }else{
                String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+val_peek(0).sval+ " no declarada";
                erroresSemanticos.add(err);
                yyval = (ArbolSintactico) nodoError;
            }
            if(auxTipoAsig.equals("")){
                /*Esto me sirve para resolver la comparacion del parametro de una funcion*/
                auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(lexema).getTipo();
            }
            }
break;
case 129:
//#line 1612 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 130:
//#line 1615 "gramatica.y"
{ 
                                    auxDerAsig.add(val_peek(0).sval);
                                    yyval = val_peek(0);
                                }
break;
case 131:
//#line 1619 "gramatica.y"
{ 
                auxDerAsig.add(val_peek(0).sval);
                yyval = val_peek(0);
                }
break;
case 132:
//#line 1626 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una CONSTANTE POSITIVA");
            if (!val_peek(0).sval.contains(String.valueOf(".")))
                constantePositiva(val_peek(0).sval);
            yyval = (ArbolSintactico) new NodoHoja(val_peek(0).sval); /* padre ------- $1*/
            if(auxTipoAsig.equals("")){
                /*Esto me sirve para resolver la comparacion del parametro de una funcion*/
                auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(0).sval).getTipo();
            }
            }
break;
case 133:
//#line 1635 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una CONSTANTE NEGATIVA");
               constanteNegativa(val_peek(0).sval);
               yyval = (ArbolSintactico) new NodoHoja("-" + val_peek(0).sval); /* padre ------- $1*/
                if(auxTipoAsig.equals("")){
                    auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(1).sval).getTipo();
                }
             }
break;
//#line 2853 "Parser.java"
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
