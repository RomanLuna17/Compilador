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
import java.util.Stack;

//#line 31 "Parser.java"




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
   28,   28,   33,   33,   33,   33,   33,   33,   34,   34,
   34,   35,   35,   36,   36,
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
    3,    1,    3,    6,    3,    6,    1,    4,    2,    1,
    1,    3,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   35,   36,   37,    0,   53,
    0,    0,    0,    1,    0,    7,    8,    9,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  114,   66,    0,    0,  113,   59,    0,
   32,    0,    0,    0,    0,  133,  134,    0,    0,    0,
    0,    0,  127,    0,  131,    0,    6,   14,   10,   15,
   11,   16,   12,   17,   13,   20,   39,    0,   19,    0,
    0,    0,    0,    0,   73,    0,    0,    0,    0,    0,
    0,    0,   62,    0,    0,   74,   68,   75,   69,   76,
   70,   77,   71,   78,   72,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    5,    3,    2,
    0,  135,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  129,    0,    4,   38,    0,    0,    0,
    0,   31,    0,    0,    0,   63,   49,   44,   50,   45,
   48,   51,   46,   47,    0,    0,   61,   80,    0,   82,
    0,   81,   83,   67,   65,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  123,    0,  125,  132,   43,    0,
   41,   42,    0,    0,    0,    0,    0,    0,   58,   56,
   54,   57,   79,   84,  112,    0,    0,   60,    0,    0,
    0,    0,    0,  128,    0,    0,    0,    0,    0,   40,
    0,    0,    0,    0,    0,    0,    0,    0,  111,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   29,   27,   22,   28,    0,    0,    0,   30,    0,    0,
    0,    0,    0,    0,    0,   34,    0,    0,  116,    0,
    0,    0,  101,    0,    0,    0,  124,  126,   25,   23,
   21,   24,   26,    0,  103,    0,  102,    0,   99,    0,
   87,    0,    0,  119,  115,  117,  118,    0,  100,    0,
    0,    0,    0,    0,    0,    0,   33,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   94,    0,    0,
   97,    0,    0,    0,    0,   93,    0,   92,    0,   91,
   98,   96,  104,   95,   88,   86,   89,   90,
};
final static short yydgoto[] = {                         13,
   14,   15,   16,   17,  179,   79,   80,   81,   82,   23,
   24,   25,   74,  180,  181,  182,   84,   26,   27,   85,
   36,   28,   29,   30,   31,   32,   33,   50,  152,   51,
  196,   34,   52,   53,   54,   55,
};
final static short yysindex[] = {                        91,
 -218,   35, -177,  -96,  -33,    0,    0,    0,   49,    0,
  104,  441,    0,    0,  120,    0,    0,    0,  -14,   -4,
   -1,    3, -191,  -37,  -27,   62,  133,   27,   48,   50,
   59,   63,   15,    0,    0,  -49,  366,    0,    0,  325,
    0,   10,  409, -134,   45,    0,    0,   86, -122,   24,
   99,   26,    0,  -46,    0, -119,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   89,    0, -114,
    4,   30, -102,  121,    0, -218,  149,  -93,   66,   75,
  122,   77,    0,  127,  -73,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  596,  349,  -82,  -75,  -72,
   60,  155,  365,   67,  -57,  -69,  168,    0,    0,    0,
  -25,    0,  441,  441,  441,  441,  441,  441,  441,  441,
 -112,  458,  481,    0,  -47,    0,    0,  325,  165,   -6,
  325,    0,   88,  -44,  -88,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -218,  -43,    0,    0,  111,    0,
   11,    0,    0,    0,    0,  399,   92,  -40,  325,  179,
  123,  -98,  182,   11,   11,   11,   11,   26,   26,   11,
   11,  135,  399,  203,    0,  220,    0,    0,    0,  178,
    0,    0,  325,  198,  325,  213,  228,  325,    0,    0,
    0,    0,    0,    0,    0,  400,  416,    0,  251,  143,
  399,  157,  399,    0,  399,  417,  -25,  -25,  110,    0,
   61,  118,  267,  325,  280,  125,  299, -148,    0, -127,
  379,  126,  325,  432,  449,  462,  472,  -97,  337,  338,
    0,    0,    0,    0,  139,   78,  147,    0,  152,  287,
  167,  298,  170,  304,  -74,    0,  312,  192,    0,  488,
  195,  196,    0,  330,  204,  399,    0,    0,    0,    0,
    0,    0,    0,  399,    0,  489,    0,  399,    0,  336,
    0,  399,  212,    0,    0,    0,    0,  399,    0,  510,
  520,  209,  533,  558,  559,  575,    0,  579, -179,  221,
  218,  225,  236,  238,  580, -166, -133,    0,  219,  245,
    0,  249,  250,  253, -132,    0,  254,    0,  257,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
   34,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   81,    0,    0,    0,
    0,    0,    0,    0,    0,   84,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -26,    0,  -39,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   94,    0,    0,
    0,    0,    0,    0,    0,   -2,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -35,    0,    0,    0,    0,    0,
  107,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  466,  480,  482,  498,  -12,   19,  511,
  512,    0,    0,    0,    0,    0,    0,    0,    0,    0,
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
    0,  543,   43,    0,    1,   87,   97,  100,  105,   28,
  532,    0,  487,  726,  687,   64,    0,    0,    0,   32,
    0,    0,    0,    0,    0,    0,    0,  811,    0,   96,
  609,    0,   29,  652,    0,    0,
};
final static int YYTABLESIZE=949;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        125,
   18,  130,  130,  130,  130,  130,   42,  130,   52,  100,
  173,   18,   71,   72,  122,   18,  122,  122,  122,   49,
  130,   70,  130,   39,  203,  256,   40,   78,  120,   59,
  120,  120,  120,  122,  186,  122,  191,   84,   35,   61,
   78,   52,   63,   84,  129,   18,   65,  120,  272,  120,
  104,  146,   73,  117,   96,  118,   39,   57,   84,  121,
   98,  121,  121,  121,   66,   67,  117,  122,  118,  105,
   87,  103,  123,   84,   37,   97,  298,   78,  121,   84,
  121,   38,  299,  120,   12,  119,   19,   57,   43,  306,
   83,   89,   39,   91,   84,  307,   20,   19,   73,   21,
   12,   19,   93,   83,   22,   75,   95,   20,  135,  138,
   21,   20,  240,  241,   21,   22,  185,   12,  140,   22,
  143,  108,  308,  315,   55,  111,  128,   64,  309,  316,
   12,   19,  102,  242,  243,  112,  126,   18,  107,  121,
   83,   20,  127,   12,   21,  168,  169,   70,  147,   22,
   85,  193,  131,  117,  132,  118,  195,  172,  253,   12,
   39,  133,  136,  254,  255,  141,  147,  190,  145,  110,
  144,  202,   12,  195,  153,    4,    5,    6,    7,    8,
  154,  269,  156,  145,  155,  233,  270,  271,   12,  159,
    4,    5,    6,    7,    8,  157,  219,  195,  147,  160,
  161,  195,  261,  195,   12,  195,  219,   99,  162,  178,
  188,  189,  192,   11,  197,  198,  130,   12,   69,  200,
   39,  219,  204,   41,  219,  195,  219,  219,   44,  122,
  124,   46,   47,  130,  130,  130,  130,   12,    6,    7,
    8,   58,  207,  120,   56,  201,  122,  122,  122,  122,
  219,   60,   12,   39,   62,   77,  195,  205,   64,  208,
  120,  120,  120,  120,  195,  223,  195,   12,  195,    6,
    7,    8,  195,  134,  121,    6,    7,    8,  195,  225,
  219,  219,   86,  219,  219,  195,  219,  183,  219,   39,
   12,  121,  121,  121,  121,  219,  113,  114,  115,  116,
  109,    1,  209,   88,    2,   90,   12,    3,    4,    5,
    6,    7,    8,    9,   92,   10,  232,   76,   94,   12,
    2,  137,  212,    3,    4,    5,    6,    7,    8,    9,
  139,   10,  142,  260,   76,  214,   55,    2,   12,   64,
    3,    4,    5,    6,    7,    8,    9,    1,   10,   18,
    2,   12,  216,    3,    4,    5,    6,    7,    8,    9,
    1,   10,   85,    2,   12,  231,    3,    4,    5,    6,
    7,    8,    9,  234,   10,  222,    1,  257,  258,    2,
  238,  246,    3,    4,    5,    6,    7,    8,    9,   76,
   10,  235,    2,   49,  259,    3,    4,    5,    6,    7,
    8,    9,  262,   10,  237,   76,  101,  263,    2,  264,
   49,    3,    4,    5,    6,    7,    8,    9,   12,   10,
  266,   76,  265,  239,    2,  267,  268,    3,    4,    5,
    6,    7,    8,    9,   76,   10,  273,    2,   12,   12,
    3,    4,    5,    6,    7,    8,    9,  274,   10,  106,
  276,  277,  278,   49,   76,   12,   12,    2,  285,  279,
    3,    4,    5,    6,    7,    8,    9,  287,   10,   76,
  291,   12,    2,  301,  310,    3,    4,    5,    6,    7,
    8,    9,  300,   10,   76,   49,  302,    2,   12,  158,
    3,    4,    5,    6,    7,    8,    9,  303,   10,  304,
  311,   12,   49,  245,  312,  313,  107,   76,  314,  317,
    2,   12,  318,    3,    4,    5,    6,    7,    8,    9,
  108,   10,  109,   76,  218,   49,    2,   12,   12,    3,
    4,    5,    6,    7,    8,    9,   76,   10,  110,    2,
  220,  228,    3,    4,    5,    6,    7,    8,    9,   12,
   10,  105,  106,   45,   68,   76,  248,  130,    2,   12,
    0,    3,    4,    5,    6,    7,    8,    9,   76,   10,
    0,    2,   12,  249,    3,    4,    5,    6,    7,    8,
    9,   76,   10,    0,    2,    0,  251,    3,    4,    5,
    6,    7,    8,    9,    0,   10,  252,   12,   12,    0,
    0,    0,    0,    0,  150,   46,   47,    0,    0,    0,
    0,    0,  275,  282,   12,    0,    0,    0,   12,   12,
   48,  145,   46,   47,    0,    0,    0,    0,    4,    5,
    6,    7,    8,    0,  289,  194,  148,   48,    2,  244,
   49,    3,    0,    0,  290,    0,    0,    9,    0,   10,
    0,    0,    0,    0,    0,  194,  194,  292,    2,    2,
    0,    3,    3,    0,    0,   46,   47,    9,    9,   10,
   10,    0,  194,  194,    0,    2,    2,    0,    3,    3,
   48,    0,  293,  294,    9,    9,   10,   10,  194,    0,
    0,    2,    0,    0,    3,    0,    0,   46,   47,  296,
    9,    0,   10,  297,  305,  194,    0,    0,    2,    0,
    0,    3,   48,    0,   46,   47,    0,    9,  194,   10,
    0,    2,    0,    0,    3,    0,    0,    0,  194,  174,
    9,    2,   10,    0,    3,    0,    0,   46,   47,    0,
    9,    0,   10,    0,  194,  194,    0,    2,    2,    0,
    3,    3,  176,    0,    0,    0,    9,    9,   10,   10,
    0,    0,  163,    0,    0,    0,  194,    0,    0,    2,
    0,    0,    3,  175,  177,    0,  194,    0,    9,    2,
   10,  206,    3,    0,    0,    0,    0,    0,    9,  194,
   10,    0,    2,    0,    0,    3,    0,    0,    0,    0,
    0,    9,    0,   10,    0,  221,    0,    0,    0,  224,
    0,  226,    0,  227,  194,  194,    0,    2,    2,    0,
    3,    3,    0,    0,    0,    0,    9,    9,   10,   10,
    0,  194,    0,  250,    2,  194,  194,    3,    2,    2,
    0,    3,    3,    9,    0,   10,    0,    9,    9,   10,
   10,    0,   46,   47,  184,    0,  187,    0,  229,  230,
    0,    0,    0,    0,  280,    0,  210,   48,    0,    0,
  210,    0,  281,  210,  283,    0,  284,    0,    0,    0,
  286,    0,    0,    0,  199,  210,  288,    0,    0,    0,
    0,    0,    0,  295,    0,    0,    0,  210,    0,  210,
    0,  210,    0,  210,    0,    0,  149,  151,  211,    0,
  213,  215,    0,  217,    0,    0,    0,    0,    0,    0,
    0,    0,  210,  164,  165,  166,  167,    0,    0,  170,
  171,    0,    0,  210,    0,    0,    0,    0,    0,  236,
    0,    0,    0,    0,    0,    0,    0,    0,  247,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         46,
    0,   41,   42,   43,   44,   45,   40,   47,   44,   59,
  123,   11,   40,   41,   41,   15,   43,   44,   45,   45,
   60,   59,   62,   59,  123,  123,  123,   27,   41,   44,
   43,   44,   45,   60,   41,   62,  125,   40,  257,   44,
   40,   44,   44,   46,   41,   45,   44,   60,  123,   62,
   41,  125,   25,   43,   40,   45,   59,   15,   61,   41,
   46,   43,   44,   45,  256,  257,   43,   42,   45,   42,
   44,   40,   47,   40,   40,   61,  256,   77,   60,   46,
   62,  259,  262,   60,   40,   62,    0,   45,   40,  256,
   27,   44,   59,   44,   61,  262,    0,   11,   71,    0,
   40,   15,   44,   40,    0,   44,   44,   11,   77,   44,
   11,   15,  261,  262,   15,   11,  123,   40,   44,   15,
   44,  256,  256,  256,   44,   40,  123,   44,  262,  262,
   40,   45,   37,  261,  262,  258,  256,   44,   43,   41,
   77,   45,  257,   40,   45,  117,  118,   59,   85,   45,
   44,   41,  123,   43,  257,   45,  156,  270,  256,   40,
  257,   41,  256,  261,  262,   44,  103,  256,  257,  125,
   44,  270,   40,  173,  257,  264,  265,  266,  267,  268,
  256,  256,  123,  257,  257,  125,  261,  262,   40,  123,
  264,  265,  266,  267,  268,   41,  196,  197,  135,  257,
  270,  201,  125,  203,   40,  205,  206,  257,   41,  257,
  123,  256,  256,  123,  123,  256,  256,   40,  256,   41,
  256,  221,   41,  257,  224,  225,  226,  227,  125,  256,
  277,  257,  258,  273,  274,  275,  276,   40,  266,  267,
  268,  256,   40,  256,  125,  123,  273,  274,  275,  276,
  250,  256,   40,  256,  256,  123,  256,  123,  256,   40,
  273,  274,  275,  276,  264,  123,  266,   40,  268,  266,
  267,  268,  272,  125,  256,  266,  267,  268,  278,  123,
  280,  281,  256,  283,  284,  285,  286,  123,  288,  256,
   40,  273,  274,  275,  276,  295,  273,  274,  275,  276,
  256,  257,  125,  256,  260,  256,   40,  263,  264,  265,
  266,  267,  268,  269,  256,  271,  256,  257,  256,   40,
  260,  256,  125,  263,  264,  265,  266,  267,  268,  269,
  256,  271,  256,  256,  257,  123,  256,  260,   40,  256,
  263,  264,  265,  266,  267,  268,  269,  257,  271,  256,
  260,   40,  125,  263,  264,  265,  266,  267,  268,  269,
  257,  271,  256,  260,   40,  256,  263,  264,  265,  266,
  267,  268,  269,  256,  271,  125,  257,   41,   41,  260,
  256,  256,  263,  264,  265,  266,  267,  268,  269,  257,
  271,  125,  260,   45,  256,  263,  264,  265,  266,  267,
  268,  269,  256,  271,  125,  257,   41,  256,  260,  123,
   45,  263,  264,  265,  266,  267,  268,  269,   40,  271,
  123,  257,  256,  125,  260,  256,  123,  263,  264,  265,
  266,  267,  268,  269,  257,  271,  125,  260,   40,   40,
  263,  264,  265,  266,  267,  268,  269,  256,  271,   41,
  256,  256,  123,   45,  257,   40,   40,  260,  123,  256,
  263,  264,  265,  266,  267,  268,  269,  256,  271,  257,
  262,   40,  260,  256,  256,  263,  264,  265,  266,  267,
  268,  269,  262,  271,  257,   45,  262,  260,   40,  125,
  263,  264,  265,  266,  267,  268,  269,  262,  271,  262,
  256,   40,   45,  125,  256,  256,   41,  257,  256,  256,
  260,   40,  256,  263,  264,  265,  266,  267,  268,  269,
   41,  271,   41,  257,  125,   45,  260,   40,   40,  263,
  264,  265,  266,  267,  268,  269,  257,  271,   41,  260,
  125,  125,  263,  264,  265,  266,  267,  268,  269,   40,
  271,   41,   41,   11,   23,  257,  125,   71,  260,   40,
   -1,  263,  264,  265,  266,  267,  268,  269,  257,  271,
   -1,  260,   40,  125,  263,  264,  265,  266,  267,  268,
  269,  257,  271,   -1,  260,   -1,  125,  263,  264,  265,
  266,  267,  268,  269,   -1,  271,  125,   40,   40,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,
   -1,   -1,  125,  125,   40,   -1,   -1,   -1,   40,   40,
  272,  257,  257,  258,   -1,   -1,   -1,   -1,  264,  265,
  266,  267,  268,   -1,  125,  257,   41,  272,  260,  261,
   45,  263,   -1,   -1,  125,   -1,   -1,  269,   -1,  271,
   -1,   -1,   -1,   -1,   -1,  257,  257,  125,  260,  260,
   -1,  263,  263,   -1,   -1,  257,  258,  269,  269,  271,
  271,   -1,  257,  257,   -1,  260,  260,   -1,  263,  263,
  272,   -1,  125,  125,  269,  269,  271,  271,  257,   -1,
   -1,  260,   -1,   -1,  263,   -1,   -1,  257,  258,  125,
  269,   -1,  271,  125,  125,  257,   -1,   -1,  260,   -1,
   -1,  263,  272,   -1,  257,  258,   -1,  269,  257,  271,
   -1,  260,   -1,   -1,  263,   -1,   -1,   -1,  257,  272,
  269,  260,  271,   -1,  263,   -1,   -1,  257,  258,   -1,
  269,   -1,  271,   -1,  257,  257,   -1,  260,  260,   -1,
  263,  263,  272,   -1,   -1,   -1,  269,  269,  271,  271,
   -1,   -1,  111,   -1,   -1,   -1,  257,   -1,   -1,  260,
   -1,   -1,  263,  122,  123,   -1,  257,   -1,  269,  260,
  271,  173,  263,   -1,   -1,   -1,   -1,   -1,  269,  257,
  271,   -1,  260,   -1,   -1,  263,   -1,   -1,   -1,   -1,
   -1,  269,   -1,  271,   -1,  197,   -1,   -1,   -1,  201,
   -1,  203,   -1,  205,  257,  257,   -1,  260,  260,   -1,
  263,  263,   -1,   -1,   -1,   -1,  269,  269,  271,  271,
   -1,  257,   -1,  225,  260,  257,  257,  263,  260,  260,
   -1,  263,  263,  269,   -1,  271,   -1,  269,  269,  271,
  271,   -1,  257,  258,  129,   -1,  131,   -1,  207,  208,
   -1,   -1,   -1,   -1,  256,   -1,  180,  272,   -1,   -1,
  184,   -1,  264,  187,  266,   -1,  268,   -1,   -1,   -1,
  272,   -1,   -1,   -1,  159,  199,  278,   -1,   -1,   -1,
   -1,   -1,   -1,  285,   -1,   -1,   -1,  211,   -1,  213,
   -1,  215,   -1,  217,   -1,   -1,   96,   97,  183,   -1,
  185,  186,   -1,  188,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  236,  113,  114,  115,  116,   -1,   -1,  119,
  120,   -1,   -1,  247,   -1,   -1,   -1,   -1,   -1,  214,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  223,
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
"termino : termino '*' TOF '(' factor ')'",
"termino : termino '/' factor",
"termino : termino '/' TOF '(' factor ')'",
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

//#line 2728 "gramatica.y"

//funciones

public static boolean auxTof = false;
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

public static Stack<NodoComun> pilaAuxArbol = new Stack<>();
public static String nombre_clase = "";

public static String auxiliar_clase = "";
public static ArrayList<String> auxiliar_lista_clase = new ArrayList<String>();
public static int auxiliar_profundidad = 0;
public static Stack<ContenedorClase> pilaAuxiliarClases = new Stack<>();

public static void CreacionVariablesPosteriori (String lexema, ArrayList<String> listaVarACrear){
    System.out.println("CLASE u OBJETO: " + lexema + " LISTA VAR CREAR: " + listaVarACrear);
    // lexema: nombre de mi clase u objeto a posteriori
    // listaVarACrear: son las variables que le voy a generar a las clases y objetos que les debo variables
    Simbolo simboloLexema = TablaDeSimbolos.obtenerSimbolo(lexema);
    String principioLexema = lexema.substring(0, lexema.indexOf('#'));
    for (String lexemaLeDebo : simboloLexema.getListaAQuienLeDebo()){
        Simbolo simboloLeDebo = TablaDeSimbolos.obtenerSimbolo(lexemaLeDebo);
        String ambito = lexemaLeDebo.substring(lexemaLeDebo.indexOf('#')+1, lexemaLeDebo.length());
        ArrayList<String> listaVarClase = new ArrayList<String>();
        for (String var : listaVarACrear) {
            if (simboloLeDebo.getUso().equals("clase")) {
                System.out.println("CLASE ----> MEDIO: " + var + " PRINCIPIO: " + principioLexema +  " ambito: "+ ambito+"#"+lexemaLeDebo.substring(0, lexemaLeDebo.indexOf('#')));
                String aux = CrearCopiaVariable(var, principioLexema, ambito+"#"+lexemaLeDebo.substring(0, lexemaLeDebo.indexOf('#')));
                System.out.println("CREE CLASE------------------------------------------------------------------------------> " + aux);
                listaVarClase.add(aux);
            } else {
                // simboloLeDebo.getLexema().substring(0, simboloLeDebo.getLexema().indexOf('#'))
                System.out.println("OBJTEO----> MEDIO: " + var + " PRINCIPIO: " +lexemaLeDebo.substring(0,lexemaLeDebo.indexOf("#"))+"."+principioLexema +  " ambito: "+ambito);
                if(simboloLexema.getUso().equals("clase")){
                    String aux = CrearCopiaVariable(var, lexemaLeDebo.substring(0,lexemaLeDebo.indexOf("#")), ambito);
                    System.out.println("CREE  OBJT CLASE------------------------------------------------------------------------------> " + aux);
                    listaVarClase.add(aux);
                }else{
                    String aux = CrearCopiaVariable(var, lexemaLeDebo.substring(0,lexemaLeDebo.indexOf("#"))+"."+principioLexema, ambito);
                    System.out.println("CREE OBJT - OBJT ------------------------------------------------------------------------------> " + aux);
                    listaVarClase.add(aux);
                }
            }
        }
        simboloLeDebo.agregarAtributosTotalesClase(listaVarClase);
        if (!simboloLeDebo.getListaAQuienLeDebo().isEmpty()) {
            CreacionVariablesPosteriori(lexemaLeDebo, listaVarClase);
        }
        listaVarClase.clear();
    }
}


public static String CrearCopiaVariable (String lexema, String principioLexema, String ambito) {
    //System.out.println("MEDIO: "+lexema + " PRINCIPIO: "+ principioLexema + " AMBITO: " + ambito + " GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGg");
    String lexemaCopia = principioLexema +"."+lexema.substring(0, lexema.indexOf('#'))+"#"+ambito;
    Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(lexema);
    Simbolo nuevoSimbolo = new Simbolo(lexemaCopia, simbolo.getId());
    nuevoSimbolo.setTipo(simbolo.getTipo());
    nuevoSimbolo.setUso(simbolo.getUso());
    nuevoSimbolo.setParametro(simbolo.getParametro());
    nuevoSimbolo.setDireccionMetodo(simbolo.getDireccionMetodo());
    TablaDeSimbolos.agregarSimbolo(lexemaCopia, nuevoSimbolo); // agregue una nueva funcion a la tabla
    return lexemaCopia;
}


public static ArbolSintactico crearMetodoDeClase(String objtClase, ArbolSintactico arbolFun){
   return null;
    /*
    Simbolo simboloNodo = Constantes.SIMBOLO_NO_ENCONTRADO;
    String nuevoNombreNodo ="";
    if(arbolFun != null){
        simboloNodo = TablaDeSimbolos.obtenerSimbolo(arbolFun.getLex());
        System.out.println("ARBOL: " + arbolFun.getLex() + " SIMBOLO: " + simboloNodo.getLexema() + " nombreEnMETODO: " + simboloNodo.getNombreMetodo() + " nombreObjt: " + objtClase);
        ArrayList<String> nombreClases = simboloNodo.getNombreMetodo();
        for(String s : nombreClases){
            //System.out.println("nodo a procesar: "+simboloNodo.getLexema() +" nombreMetodo: " + s + " objtClase: " + objtClase);
            if(s.contains(objtClase)){
                nuevoNombreNodo = s;
                break;
            }
        }
        if(nuevoNombreNodo.equals(""))
            nuevoNombreNodo = simboloNodo.getLexema();
    }
    if (arbolFun instanceof NodoComun) {
            if(simboloNodo.getId() != -1){
                NodoComun copia = new NodoComun(nuevoNombreNodo,crearMetodoDeClase(objtClase, arbolFun.getIzq()) ,crearMetodoDeClase(objtClase, arbolFun.getDer()));
                return copia;
            }else{
                System.out.println("ENTRE ACA NODO COMUN");
                NodoComun copia = new NodoComun(arbolFun.getLex(),crearMetodoDeClase(objtClase, arbolFun.getIzq()) ,crearMetodoDeClase(objtClase, arbolFun.getDer()));
                return copia;
            }
        } else if (arbolFun instanceof NodoControl) {
            if(simboloNodo.getId() != -1){            
                NodoControl copia = new NodoControl(nuevoNombreNodo,crearMetodoDeClase(objtClase, arbolFun.getIzq()) );
                return copia;
            }else{
                System.out.println("ENTRE ACA NODO CONTROL");
                NodoControl copia = new NodoControl(arbolFun.getLex(),crearMetodoDeClase(objtClase, arbolFun.getIzq()) );
                return copia;
            }
        } else if (arbolFun instanceof NodoHoja) {
            if(simboloNodo.getId() != -1){
                NodoHoja copia = new NodoHoja(nuevoNombreNodo);
                return copia;
            }else{
                System.out.println("ENTRE ACA NODO HOJA");
                NodoHoja copia = new NodoHoja(arbolFun.getLex());
                return copia;
            }
        }
        return null;
    */
}

public static ArrayList<String> obtenerSimbolosFuncion(ArbolSintactico arbol){
    ArrayList<String> aux = new ArrayList<String>();
    if(arbol.getIzq() != null){
        aux.addAll(obtenerSimbolosFuncion(arbol.getIzq()));
    }
    
    if(arbol.getDer() != null){
        aux.addAll(obtenerSimbolosFuncion(arbol.getDer()));
    }
    
    if(arbol.getLex().contains("#") && TablaDeSimbolos.obtenerSimbolo(arbol.getLex()).getUso().equals("identificador")){
        aux.add(arbol.getLex());
    }


    return aux;
}

void crearAtributosDeObjetoClase(String nombreObjeto,String nombreObjetoNuevo, String tipoClase){
    Hashtable<String, Simbolo> tabla = TablaDeSimbolos.obtenerTablaDeSimbolos();
    Hashtable<Simbolo, String> simbolosACrear = new Hashtable<Simbolo,String>();
    for (String key : tabla.keySet()) {
        Simbolo simbolo = tabla.get(key);
        //System.out.println("claseHeredada: " + claseHeredada + " nombreClaseAct: " + nombreClaseAct);
        if(simbolo.getLexema().contains(nombreObjeto+".") && !simbolo.getLexema().contains("."+nombreObjeto)){
            // con este if estoy diciendo que simbolo tiene que tener por ejemplo #c6 al final y no en el medio
            System.out.println("Adelante agrego: " + nombreObjetoNuevo +" en el medio: "+simbolo.getLexema() + " al final: #"+tipoClase);
            String nuevoLexema = nombreObjetoNuevo+"."+simbolo.getLexema();
            simbolosACrear.put(simbolo,nuevoLexema);
        }
    }

    //Una vez que consigo todos los simbolos los agergo a la tabla
    for (Simbolo simbolo : simbolosACrear.keySet()) {
        Simbolo simboloNuevo = TablaDeSimbolos.obtenerSimbolo(simbolosACrear.get(simbolo), simbolo.getId());
        simboloNuevo.setTipo(simbolo.getTipo());
        simboloNuevo.setUso(simbolo.getUso());
        simboloNuevo.setUsada(simbolo.getUsada());
        simboloNuevo.setValor(simbolo.getValor());
        //simboloNuevo.setParametro(Simbolo.getParametro());
        simboloNuevo.setDireccionMetodo(simbolo.getDireccionMetodo());
    }
}
public static void crearAtributoClaseHeredada(String claseHeredada, String nombreClaseAct){
    Hashtable<String, Simbolo> tabla = TablaDeSimbolos.obtenerTablaDeSimbolos();
    Hashtable<Simbolo, String> simbolosACrear = new Hashtable<Simbolo,String>();
    for (String key : tabla.keySet()) {
        Simbolo simbolo = tabla.get(key);
        System.out.println("claseHeredada: " + claseHeredada + " nombreClaseAct: " + nombreClaseAct);
        if(simbolo.getLexema().contains("#"+claseHeredada) && !simbolo.getLexema().contains(claseHeredada+"#")){
            // con este if estoy diciendo que simbolo tiene que tener por ejemplo #c6 al final y no en el medio
            String nuevoLexema = simbolo.getLexema().substring(0, simbolo.getLexema().lastIndexOf("#")); //Quita el ultimo ambito, ej, #c6
            nuevoLexema = claseHeredada+"."+nuevoLexema; // agrego c6. al principio
            nuevoLexema = nuevoLexema +"#"+nombreClaseAct; // agrego #c7 al final
            simbolosACrear.put(simbolo,nuevoLexema);
        }
    }

    //Una vez que consigo todos los simbolos los agergo a la tabla
    for (Simbolo simbolo : simbolosACrear.keySet()) {
        Simbolo simboloNuevo = TablaDeSimbolos.obtenerSimbolo(simbolosACrear.get(simbolo), simbolo.getId());
        simboloNuevo.setTipo(simbolo.getTipo());
        simboloNuevo.setUso(simbolo.getUso());
        simboloNuevo.setUsada(simbolo.getUsada());
        simboloNuevo.setValor(simbolo.getValor());
        //simboloNuevo.setParametro(Simbolo.getParametro());
        simboloNuevo.setDireccionMetodo(simbolo.getDireccionMetodo());
    }

}

public static Hashtable<Simbolo, String> crearAtributosClase(String nombreClase, String nombreObjtClase){
    
    //nombre clase = c3
    //nombreObjtClase = objt3

    Hashtable<String, Simbolo> tabla = TablaDeSimbolos.obtenerTablaDeSimbolos();
    Hashtable<Simbolo, String> simbolosDeClase = new Hashtable<Simbolo,String>();
    Simbolo simboloClase = TablaDeSimbolos.obtenerSimbolo(nombreClase+"#"+ambitoActual);
    String claseHereda = simboloClase.getHereda();
    String[] partes = simboloClase.getHereda().split("#");
    claseHereda = partes[0];
    // System.out.println("Clase Hereda es: " + claseHereda +  " nombreClase: " + nombreClase + " nombreObjtClase: " + nombreObjtClase);
    for (String key : tabla.keySet()) {
	    Simbolo simbol = tabla.get(key);
        
        if(simbol.getLexema().contains("#"+nombreClase) && !simbol.getLexema().contains(nombreClase+"#")){
            int ultimoIndice = simbol.getLexema().indexOf("#");
            System.out.println("EL LEXEMA ES: " +simbol.getLexema() +" LO QUE YO LE SACO ES: " +simbol.getLexema().substring(0,ultimoIndice));
            simbolosDeClase.put(simbol,nombreObjtClase+"."+simbol.getLexema().substring(0,ultimoIndice)+"#"+ambitoActual);
            //System.out.println("#####################################: " +nombreObjtClase+"."+simbol.getLexema().substring(0,ultimoIndice));
            //simbolosDeClase.put(simbol,nombreObjtClase+"."+simbol.getLexema());
        }
    }
    if(!claseHereda.equals("") && !claseHereda.equals("nadie")){
        Hashtable<Simbolo, String> aux = crearAtributosClase(claseHereda, nombreObjtClase+"."+claseHereda);
        simbolosDeClase.putAll(aux);
    }
    return simbolosDeClase;
}


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

public static void agregarMetodoLista(NodoControl raiz){
    lista_func.add(raiz);
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
//#line 1094 "Parser.java"
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
//#line 29 "gramatica.y"
{System.out.println("ARRANCO EL PROGRAMA");}
break;
case 2:
//#line 32 "gramatica.y"
{System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa ");}
break;
case 3:
//#line 33 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta } al Final");}
break;
case 4:
//#line 34 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta { al Inicio");}
break;
case 5:
//#line 35 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta bloque de sentencias");}
break;
case 8:
//#line 42 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia DECLARATIVA");}
break;
case 9:
//#line 43 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia EJECUTABLE");
                                             ArbolSintactico arbAux = (ArbolSintactico) val_peek(0);
                                             buscarErroresEnNodo(arbAux);  
                                             generarArbol((ArbolSintactico) val_peek(0));
                                            }
break;
case 14:
//#line 56 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de variable");}
break;
case 15:
//#line 57 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de funcion");}
break;
case 16:
//#line 58 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de la clase");}
break;
case 17:
//#line 59 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion del objeto de clase");}
break;
case 18:
//#line 62 "gramatica.y"
{
                                                    for(String s : lista_identificadores){
                                                        System.out.println("LEXEMA: " + s);
                                                        if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                            TablaDeSimbolos.setTipo(val_peek(1).sval, s);
                                                            TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                            simbolo.setUso("identificador");
                                                            simbolo.setValorAsignado(true);
                                                            simbolo.setUsada(true);
                                                            System.out.println("Estoy agergando una variable: " + simbolo.getLexema() + " auxiliar_clase vale: "+auxiliar_clase + " la profundidad es: " + auxiliar_profundidad);
                                                            if(!auxiliar_clase.equals("") && auxiliar_profundidad == 0){
                                                                System.out.println("cumpli las condiciones entonces entre al if y me agregue a la lista");
                                                                auxiliar_lista_clase.add(simbolo.getLexema());
                                                                
                                                            }
                                                        }else{
                                                           String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                    }
                                                    lista_identificadores.clear();
                                                }
break;
case 19:
//#line 85 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el TIPO en la DECLARACION de la variable");}
break;
case 20:
//#line 86 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el identificador en la DECLARACION de la variable");}
break;
case 21:
//#line 89 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION CON PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    String ambitoParametro = ambitoActual;
                                                    ambitoActual = ambitoActual.substring(0, indice);

                                                    Simbolo simbolParametro = TablaDeSimbolos.obtenerSimbolo(val_peek(4).sval+"#"+ambitoParametro);
                                                    TablaDeSimbolos.obtenerSimbolo(val_peek(6).sval).setParametro(simbolParametro);

                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(6).sval+"#"+ambitoActual, simbolParametro.getLexema())){
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema(val_peek(6).sval, val_peek(6).sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(val_peek(6).sval+"#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            simbolo.setParametro(simbolParametro);
                                                            simbolo.setDireccionMetodo(val_peek(6).sval+"#"+ambitoActual);
                                                            agregarArbol(val_peek(6).sval+"#"+ambitoActual);
                                                            tieneReturn = false; 

                                                            auxiliar_profundidad--;
                                                            if(!auxiliar_clase.equals("") && auxiliar_profundidad == 0){
                                                                simbolo.agergarListaVariablesMetodo(auxiliar_lista_clase);
                                                                auxiliar_lista_clase.add(simbolo.getLexema());
                                                            }
                                                        }else{
                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Falta la sentencia return en la declaracion de funcion";
                                                            erroresSemanticos.add(err);    
                                                        }
                                                    }else{
                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Funcion re declarada en el mismo ambito";
                                                        erroresSemanticos.add(err);
                                                    }
                                                    
                                                    if(!pilaAuxArbol.empty()){
                                                        ptrRaizFuncion = pilaAuxArbol.pop();
                                                        aux_raiz_func = pilaAuxArbol.pop();
                                                    }
                                                }
break;
case 22:
//#line 126 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION SIN PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    ambitoActual = ambitoActual.substring(0, indice);
                                                   
                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(5).sval+"#"+ambitoActual, "nulo")){ 
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema(val_peek(5).sval, val_peek(5).sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(5).sval + "#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            simbolo.setDireccionMetodo(val_peek(5).sval+"#"+ambitoActual);
                                                            agregarArbol(val_peek(5).sval+"#"+ambitoActual); 
                                                            tieneReturn = false; 

                                                            auxiliar_profundidad--;
                                                            if(!auxiliar_clase.equals("") && auxiliar_profundidad == 0){
                                                                simbolo.agergarListaVariablesMetodo(auxiliar_lista_clase);
                                                                auxiliar_lista_clase.add(simbolo.getLexema());
                                                            }   

                                                        }else{
                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Falta la sentencia return en la declaracion de funcion";
                                                            erroresSemanticos.add(err);    
                                                        }
                                                    }else{
                                                        String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Funcion re declarada en el mismo ambito";
                                                        erroresSemanticos.add(err);
                                                    }
                                                    if(!pilaAuxArbol.empty()){
                                                        ptrRaizFuncion = pilaAuxArbol.pop();
                                                        aux_raiz_func = pilaAuxArbol.pop();
                                                    }

                                                    
                                        }
break;
case 23:
//#line 160 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' al final de la funcion");}
break;
case 24:
//#line 161 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
break;
case 25:
//#line 162 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
break;
case 26:
//#line 163 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
break;
case 27:
//#line 164 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' en la declaracion de la funcion");}
break;
case 28:
//#line 165 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
break;
case 29:
//#line 166 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
break;
case 30:
//#line 167 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
break;
case 31:
//#line 170 "gramatica.y"
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
//#line 181 "gramatica.y"
{ 
                                if(aux_raiz_func != null){
                                    NodoComun auxRaiz = aux_raiz_func;
                                    NodoComun auxPtrFunc = ptrRaizFuncion;
                                    pilaAuxArbol.push(auxRaiz); /* guardo primero la raiz*/
                                    pilaAuxArbol.push(auxPtrFunc); /* guardo segundo el ptro*/
                                }
                                NodoComun arbolFun = new NodoComun("Sentencia", null, null);
                                aux_raiz_func = arbolFun;
                                ptrRaizFuncion = arbolFun;

                                if(!auxiliar_clase.equals("")){
                                    auxiliar_profundidad++;
                                }

                                ambitoActual = ambitoActual+"#"+val_peek(0).sval;
                                yyval = val_peek(0);
                            }
break;
case 33:
//#line 200 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
break;
case 34:
//#line 202 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
break;
case 35:
//#line 206 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");
            yyval = val_peek(0);
            }
break;
case 36:
//#line 209 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");
            yyval = val_peek(0);
            }
break;
case 37:
//#line 212 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");
            yyval = val_peek(0);
            }
break;
case 38:
//#line 217 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 39:
//#line 218 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 43:
//#line 226 "gramatica.y"
{ generarArbolFunc((ArbolSintactico) val_peek(0));
                                                System.out.println("El subArbol que estoy creando es: ");
                                                ArbolSintactico arb = (ArbolSintactico) val_peek(0);
                                                arb.recorrerArbol("-");
                                              }
break;
case 49:
//#line 238 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de varaible");}
break;
case 50:
//#line 239 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de funcion");}
break;
case 51:
//#line 240 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de objeto");}
break;
case 52:
//#line 243 "gramatica.y"
{  if(auxVarClases.equals("")){

                                    int indice = ambitoActual.lastIndexOf('#');
                                    String lexema = val_peek(0).sval+"#"+ambitoActual.substring(0, indice);
                                    if(TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval).getId() != -1){
                                        auxVarClases = lexema;
                                        /* primero creo los atributos con el objt adelante*/
                                        
                                        Simbolo simboloTipo = TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval+"#"+ambitoActual);
                                        if(!simboloTipo.getClaseAPosterior()){
                                            ArrayList<String> atributosClase = simboloTipo.getAtributosTotalesClase();
                                            ArrayList<String> lexemasListaObjt = new ArrayList<>();
                                            for(String lexemaAtributo : atributosClase){
                                                lexemasListaObjt.add(CrearCopiaVariable(lexemaAtributo,val_peek(0).sval, ambitoActual)); /* s es el principio del lexema, el nombre del objeto*/
                                                
                                            }
                                            
                                            /* Todos los atributos*/
                                            simboloTipo.agregarAtributosTotalesClase(lexemasListaObjt);
                                            

                                           
                                            

                                            
                                            
                                            lexemasListaObjt.add(simboloTipo.getLexema());




                                            if(!auxiliar_clase.equals("") && auxiliar_profundidad == 0){
                                                auxiliar_lista_clase.addAll(lexemasListaObjt);
                                            }
                                            if(!simboloTipo.getListaQuienMeDebe().isEmpty()){
                                                simboloTipo.agregarAQuienLeDebo(auxiliar_clase);
                                                TablaDeSimbolos.obtenerSimbolo(auxiliar_clase).agregarQuienMeDebe(simboloTipo.getLexema());
                                            }

                                        }else{
                                            System.out.println("HERENCIA: " + simboloTipo.getLexema() + " posterior: " + simboloTipo.getClaseAPosterior() + " auxiliar_clase: " + auxiliar_clase);
                                            simboloTipo.agregarAQuienLeDebo(auxiliar_clase);
                                            TablaDeSimbolos.obtenerSimbolo(auxiliar_clase).agregarQuienMeDebe(simboloTipo.getLexema());
                                            System.out.println("LISTA HASTA AHORA: " + simboloTipo.getListaAQuienLeDebo());
                                        }

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
//#line 301 "gramatica.y"
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
//#line 315 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE");
                                                            int indiceLexClase = ambitoActual.lastIndexOf('#');
                                                            System.out.println("SIMBOLO: " + TablaDeSimbolos.obtenerSimbolo(val_peek(3).sval +"#"+ ambitoActual.substring(0, indiceLexClase)).ToString() + " LISTA DEBO: " +TablaDeSimbolos.obtenerSimbolo(val_peek(3).sval +"#"+ ambitoActual.substring(0, indiceLexClase)).getListaAQuienLeDebo());
                                                            
                                                            String identClase = val_peek(3).sval +"#"+ ambitoActual.substring(0, indiceLexClase);
                                                            
                                                            if((TablaDeSimbolos.obtenerSimbolo(identClase).getUso().equals("")) || (TablaDeSimbolos.obtenerSimbolo(identClase).getClaseAPosterior())){
                                                               if(!auxVarClases.equals("")){
                                                                    String claseHereda = nivelDeClaseCorrecto(auxVarClases); 
                                                                    if(claseHereda.equals("")){
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases);
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                        /* TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);*/

                                                                        /*creo todos los identificadores o metodos de la clase que heredo en esta clase*/
                                                                        /* String[] claseHeredada = auxVarClases.split("#");*/
                                                                        /* crearAtributoClaseHeredada(claseHeredada[0], $1.sval); //paso solo el nombre de la clase que heredo, no el ambito*/

                                                                        
                                                                        
                                                                    }else{
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases); /*agrego igual de la clase que hereda por si luego hay mas clases*/
                                                                        String err = "Linea: "+AnalizadorLexico.getLineaActual()+". Error Semantico: El nivel de herencia es superior a 3 por: " + claseHereda;
                                                                        erroresSemanticos.add(err);
                                                                    }
                                                                }else{
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setHereda("nadie");
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                    /*TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);*/
                                                                }

                                                                

                                                                Simbolo simboloClase = TablaDeSimbolos.obtenerSimbolo(identClase);
                                                                simboloClase.agregarAtributosTotalesClase(auxiliar_lista_clase);
                                                                
                                                              
                                                                

                                                                if(!pilaAuxiliarClases.isEmpty()){
                                                                    System.out.println("TERMINE LA DECLARACION DE LA CLASE QUE ESTABA ADENTRO DE OTRA. AHORA PUEDO SEGUIR CON LA CLASE PADRE: " + pilaAuxiliarClases);
                                                                    ContenedorClase cont = pilaAuxiliarClases.pop();
                                                                    auxiliar_clase = cont.getLexema();
                                                                    auxiliar_lista_clase.clear();
                                                                    auxiliar_lista_clase.addAll(cont.getListaAtributos());
                                                                    auxiliar_profundidad = cont.getContador();
                                                                }else{
                                                                    System.out.println("TERMINE CON LA DECLARACION DE CLASES Y LA PILA DE AUXILIARES ESTABA VACIA");
                                                                    auxiliar_clase ="";
                                                                    auxiliar_lista_clase.clear();
                                                                    auxiliar_profundidad = 0;
                                                                }

                                                                Simbolo simbolClase = TablaDeSimbolos.obtenerSimbolo(identClase);
                                                                System.out.println("LA CLASE: " + simboloClase.getLexema() + " POSTERIOR: " +simboloClase.getClaseAPosterior()+" LE DEBO: " + simboloClase.getListaAQuienLeDebo() +" ME DEBE: " + simboloClase.getListaQuienMeDebe());

                                                                if(simboloClase.getClaseAPosterior()){
                                                                    if(!simboloClase.getListaAQuienLeDebo().isEmpty()){
                                                                        CreacionVariablesPosteriori(simboloClase.getLexema(), simboloClase.getAtributosTotalesClase());
                                                                    }
                                                                }

                                                                TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: Clase " + val_peek(3).sval+" redeclarada";
                                                                erroresSemanticos.add(err);
                                                            }

                                                            nombre_clase = "";
                                                            auxVarClases = "";
                                                            int indice = ambitoActual.lastIndexOf('#');
                                                            ambitoActual = ambitoActual.substring(0, indice);
                                                        }
break;
case 55:
//#line 388 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE A POSTERIOR");
                                        int indiceLexClase = ambitoActual.lastIndexOf('#');
                                        
                                                          
                                        String identClase = val_peek(0).sval +"#"+ ambitoActual.substring(0, indiceLexClase);
                                        if((TablaDeSimbolos.obtenerSimbolo(identClase).getUso().equals(""))){                    
                                            TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(true);
                                            TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                 
                                            Simbolo simboloClase = TablaDeSimbolos.obtenerSimbolo(identClase);
                                            simboloClase.agregarAtributosTotalesClase(auxiliar_lista_clase);
                                            if(!pilaAuxiliarClases.isEmpty()){
                                                ContenedorClase cont = pilaAuxiliarClases.pop();
                                                auxiliar_clase = cont.getLexema();
                                                auxiliar_lista_clase.clear();
                                                auxiliar_lista_clase.addAll(cont.getListaAtributos());
                                                auxiliar_profundidad = cont.getContador();
                                            }else{
                                                auxiliar_clase = "";
                                                auxiliar_lista_clase.clear();
                                                auxiliar_profundidad = 0; 
                                            }
                                        }
                                        auxVarClases = "";
                                        nombre_clase = "";
                                        int indice = ambitoActual.lastIndexOf('#');
                                        ambitoActual = ambitoActual.substring(0, indice);
                                        /*TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setClasePosterior(true);*/
                                        /*TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setUso("clase");*/
                                         System.out.println("SIMBOLO FINAL DEC POSTERIOR: " + TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval +"#"+ ambitoActual.substring(0, indiceLexClase)).ToString() + " LISTA DEBO: " +TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval +"#"+ ambitoActual.substring(0, indiceLexClase)).getListaAQuienLeDebo() );
                                         
                                    }
break;
case 56:
//#line 420 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() + ". Error sintactico.Falta } al final de la clase");}
break;
case 57:
//#line 421 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta '{' al inicio de la clase");}
break;
case 58:
//#line 422 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta cuerpo de la clase");}
break;
case 59:
//#line 425 "gramatica.y"
{
                        yyval = val_peek(0);
                        nombre_clase = val_peek(0).sval;
                        String lexema = val_peek(0).sval +"#"+ ambitoActual;
                        
                        if(!auxiliar_clase.equals("")){
                            System.out.println("####################### ESTOY AL PRINCIPIO DE LA DECLARACION DE UNA CLASE, LA LISTA DE AUXILIAR_CLASE HASTA AHORA ES: " + auxiliar_lista_clase);
                            ContenedorClase nuevoCont = new ContenedorClase(auxiliar_clase, auxiliar_lista_clase, auxiliar_profundidad);
                            pilaAuxiliarClases.push(nuevoCont);    
                            auxiliar_clase = "";
                            auxiliar_lista_clase.clear();
                            auxiliar_profundidad = 0;   
                        } 

                        auxiliar_clase = lexema; /* auxiliar_clase = c2#global*/
                        if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() == -1)
                            TablaDeSimbolos.modificarLexema(val_peek(0).sval, lexema);
                        
                        ambitoActual = ambitoActual + "#" + val_peek(0).sval;
                }
break;
case 60:
//#line 445 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta el identificador de la clase");}
break;
case 63:
//#line 451 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico al compilar no permite declarar sentencia ejecutables dentro de una funcion");}
break;
case 64:
//#line 454 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de OBJETO DE CLASE");
                                                    if(!val_peek(1).sval.equals("")){
                                                        for(String s : lista_identificadores){
                                                            if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                                TablaDeSimbolos.setTipo(val_peek(1).sval, s);
                                                                TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                                Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                                simbolo.setUso("identificador");
                                                                TablaDeSimbolos.borrarSimbolo(s);
                                                                simbolo.setEsObjetoClase(true);

                                                                /* primero creo los atributos con el objt adelante*/
                                                                Simbolo simboloTipo = TablaDeSimbolos.obtenerSimbolo(val_peek(1).sval+"#"+ambitoActual);
                                                                ArrayList<String> atributosClase = simboloTipo.getAtributosTotalesClase();
                                                                ArrayList<String> lexemasListaObjt = new ArrayList<>();
                                                                for(String lexemaAtributo : atributosClase){
                                                                   lexemasListaObjt.add(CrearCopiaVariable(lexemaAtributo, s, ambitoActual)); /* s es el principio del lexema, el nombre del objeto*/
                                                                }
                                                                
                                                                
                                                                for(String s1 : lexemasListaObjt){
                                                                    TablaDeSimbolos.obtenerSimbolo(s1).agregarAtributosTotalesClase(lexemasListaObjt);

                                                                }
                                                                System.out.println("LEXEMA LISTA OBJTEOS: " + lexemasListaObjt);
                                                                simbolo.agregarAtributosTotalesClase(lexemasListaObjt);
                                                                lexemasListaObjt.add(simbolo.getLexema());

                                                                if(!auxiliar_clase.equals("") && auxiliar_profundidad == 0){
                                                                    auxiliar_lista_clase.addAll(lexemasListaObjt);
                                                                }

                                                                /*
                                                                if(!simboloTipo.getListaQuienMeDebe().isEmpty()){
                                                                    ArrayList<String> aux = simboloTipo.getListaQuienMeDebe();
                                                                    for(String x : aux){
                                                                        TablaDeSimbolos.obtenerSimbolo(x).agregarAQuienLeDebo(s + "#"+ambitoActual);
                                                                    }
                                                                }
                                                                */

                                                                if(!simboloTipo.getListaQuienMeDebe().isEmpty()){
                                                                    simboloTipo.agregarAQuienLeDebo(simbolo.getLexema());
                                                                    simbolo.agregarQuienMeDebe(simboloTipo.getLexema());
                                                                }

                                                            }else{
                                                                /*doy error por re declaracion del identificador*/
                                                                String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                                erroresSemanticos.add(err);
                                                            }
                                                        }
                                                    }else{
                                                        String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: clase no declarada";
                                                        erroresSemanticos.add(err);    
                                                    }
                                                    lista_identificadores.clear();   
                                                }
break;
case 65:
//#line 514 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 66:
//#line 515 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval); System.out.println("objts");}
break;
case 67:
//#line 516 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico. Al compilar no permite objetos de clase separados por ,");}
break;
case 68:
//#line 520 "gramatica.y"
{yyval = val_peek(1); }
break;
case 69:
//#line 521 "gramatica.y"
{yyval = val_peek(1);}
break;
case 70:
//#line 522 "gramatica.y"
{yyval = val_peek(1);}
break;
case 71:
//#line 523 "gramatica.y"
{ yyval = val_peek(1);}
break;
case 72:
//#line 524 "gramatica.y"
{ yyval = val_peek(1);
                                                           System.out.println("mando el subArbol para arribla: sentencias_ejecucion_funcion");
                                                        }
break;
case 73:
//#line 527 "gramatica.y"
{yyval = val_peek(1);}
break;
case 74:
//#line 528 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la ASIGNACION");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 75:
//#line 531 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia IF");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 76:
//#line 534 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia de SALIDA"); 
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 77:
//#line 537 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia de CONTROL");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 78:
//#line 540 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la ejecucion de FUNCION");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 79:
//#line 545 "gramatica.y"
{
                                                                System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION CON PARAMETRO");
                                                                String lexemaIdentificadorFun = "";
                                                                String idAnterior = "";
                                                                String nombreObjtClase = "";
                                                                
                                                                if(lista_identificadores.size() == 1){
                                                                    System.out.println("ES UNA FUNCION CON UN PARAMETRO");
                                                                    lexemaIdentificadorFun = lista_identificadores.get(0)+"#"+ambitoActual;
                                                                    Simbolo simboloFun = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificadorFun);
                                                                    if(simboloFun.getId() != -1 & simboloFun.getParametro() != null){
                                                                       
                                                                        if(simboloFun.getParametro().getTipo().equals(auxTipoAsig)){
                                                                            simboloFun.setUsada(true);
                                                                            NodoHoja id_func = new NodoHoja(simboloFun.getLexema());
                                                                            System.out.println("creo el arbol y lo retorno");
                                                                            yyval = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,(ArbolSintactico) val_peek(1));
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                                            erroresSemanticos.add(err);
                                                                            yyval = (ArbolSintactico) nodoError;
                                                                        }
                                                                    }else{
                                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lista_identificadores.get(0) +" no fue declarada";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = (ArbolSintactico) nodoError;
                                                                    }
                                                                }else{
                                                                    for(String s : lista_identificadores){
                                                                        if( lexemaIdentificadorFun.equals("")){
                                                                                lexemaIdentificadorFun = s;
                                                                        }else{
                                                                            lexemaIdentificadorFun = lexemaIdentificadorFun + "."+s;
                                                                        }
                                                                    }
                                                                    lexemaIdentificadorFun = lexemaIdentificadorFun+"#"+ambitoActual;
                                                                    Simbolo simboloFun = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificadorFun);
                                                                    if(simboloFun.getId() != -1 & simboloFun.getParametro() != null){
                                                                       
                                                                        if(simboloFun.getParametro().getTipo().equals(auxTipoAsig)){
                                                                            simboloFun.setUsada(true);
                                                                            NodoHoja id_func = new NodoHoja(simboloFun.getLexema());
                                                                            System.out.println("creo el arbol y lo retorno");
                                                                            yyval = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,(ArbolSintactico) val_peek(1));
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                                            erroresSemanticos.add(err);
                                                                            yyval = (ArbolSintactico) nodoError;
                                                                        }
                                                                    }else{
                                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lista_identificadores.get(0) +" no fue declarada";
                                                                        erroresSemanticos.add(err);
                                                                        yyval = (ArbolSintactico) nodoError;
                                                                    }

                                                                }
                                                                lista_identificadores.clear();
                                                                
                                                            }
break;
case 80:
//#line 604 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION SIN PARAMETRO");
                                                String lexemaIdentificadorFun = "";
                                                String idAnterior = "";
                                                String nombreObjtClase = "";
                                                
                                                if(lista_identificadores.size() == 1){
                                                    lexemaIdentificadorFun = lista_identificadores.get(0)+"#"+ambitoActual;
                                                    Simbolo simboloFun = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificadorFun);
                                                    if(simboloFun.getId() != -1 & simboloFun.getParametro() == null){
                                                        simboloFun.setUsada(true);
                                                        NodoHoja id_func = new NodoHoja(simboloFun.getLexema());
                                                        yyval = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,null);
                                                    }else{
                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lista_identificadores.get(0) +" no fue declarada";
                                                        erroresSemanticos.add(err);
                                                        yyval = (ArbolSintactico) nodoError;
                                                    }
                                                }else{
                                                    for(String s : lista_identificadores){
                                                        if( lexemaIdentificadorFun.equals("")){
                                                                lexemaIdentificadorFun = s;
                                                        }else{
                                                            lexemaIdentificadorFun = lexemaIdentificadorFun + "."+s;
                                                        }
                                                    }
                                                    lexemaIdentificadorFun = lexemaIdentificadorFun+"#"+ambitoActual;
                                                    Simbolo simboloFun = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificadorFun);
                                                    if(simboloFun.getId() != -1 & simboloFun.getParametro() == null){
                                                        simboloFun.setUsada(true);
                                                        NodoHoja id_func = new NodoHoja(simboloFun.getLexema());
                                                        yyval = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,null);
                                                    }else{
                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lista_identificadores.get(0) +" no fue declarada";
                                                        erroresSemanticos.add(err);
                                                        yyval = (ArbolSintactico) nodoError;
                                                    }
                                                }
                                                lista_identificadores.clear();
                                                
                                                }
break;
case 81:
//#line 650 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una ASIGNACION");
                                                        String lexema = "";
                                                        String primer = "";
                                                        String ambitoAnt = "";
                                                        String ambitoVariable = ambitoActual;
                                                        System.out.println("AMBITO: " + ambitoActual);
                                                        /*
                                                        for(int i = 0; i < lista_identificadores.size();i++){
                                                            System.out.println("IDENTIFICADOR: " + lista_identificadores.get(i));
                                                        }
                                                        //VERIFICO SI LA LISTA ES DISTINTO DE 0. SI ES DISTINTO DE 0 QUIERE DECIR QUE DEL LADO IZQUIERDO ESTA EL ID
                                                        if(lista_identificadores.size() > 0){
                                                            //VERIFICO SI LA LISTA ES MAYOR A 1. SI ES MAYOR A 1 QUIERE DECIR QUE ES UN IDENTIFICADOR COMPUESTO, POR EJ, CLASE1.VAR1
                                                            if(lista_identificadores.size() > 1){
                                                                String lexemaAuxFinAmbito =""; //aux usado para el ambito
                                                                Simbolo simboloAnt = null; //este simbolo es el anterior, por ejemplo en clase1.a.num1 puede ser simboloAnt: clase1
                                                                //RECORRO LA LISTA DE IDENTIFICADORES
                                                                for(int i = 0; i< lista_identificadores.size();i++){
                                                                    System.out.println("LISTA ID TIENE: " + lista_identificadores.get(i));
                                                                    if(lexema.equals("")){
                                                                        //ES EL PRIMER ELEMENTO DEL LEXEMA
                                                                        lexema = lista_identificadores.get(i); 
                                                                        simboloAnt = TablaDeSimbolos.obtenerSimbolo(lexema+"#"+ambitoActual);
                                                                        lexemaAuxFinAmbito = TablaDeSimbolos.obtenerSimbolo(lexema+"#"+ambitoActual).getTipo();
                                                                    }else{
                                                                        //LEXEMA YA TIENE ALGUN VALOR
                                                                        Simbolo simboloNuevo = null;
                                                                        System.out.println("ESTO BUSCO COMO NUEVOSIMBOLO EN ASIG: " + lista_identificadores.get(i)+"#"+ambitoActual+"#"+lexemaAuxFinAmbito);
                                                                        simboloNuevo = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+lexemaAuxFinAmbito);   //lexemaAuxFinAmbito es a
                                                                        //SI EL SIMBOLO EXISTE EN LA TABLA DE SIMBOLOS
                                                                        if(simboloNuevo.getId() != -1){   
                                                                            //SI EL SIMBOLO ES UNA CLASE
                                                                            if(simboloNuevo.getUso().equals("clase")){ //si es una clase
                                                                                //simboloTipoAnt SE REFIERE AL TIPO DEL SIMBOLO ANTERIOR, POR EJ, EL SIMBOLO ANT ES CLASE1 Y SU TIPO ES C1, ENTONCES OBTENGO EL SIMBOLO DE C1
                                                                                Simbolo simboloTipoAnt = null;
                                                                                //SI EL SIMBOLO ANTERIOR ES UN OBJETO DE UNA CLASE, POR EJEMPLO, CLASE1
                                                                                if(simboloAnt.getUso().equals("identificador")){
                                                                                    simboloTipoAnt = TablaDeSimbolos.obtenerSimbolo(simboloAnt.getTipo()+"#"+ambitoActual); //aca lo que hago es obtener el identificador de la tabla de la clase que es el identificador anterior
                                                                                }else{
                                                                                    //SINO QUIERE DECIR QUE ES C1
                                                                                    simboloTipoAnt = simboloAnt;
                                                                                }
                                                                                //SI EL SIMBOLO ANTERIOR HEREDA DEL NUEVO SIMBOLO
                                                                                if(simboloTipoAnt.getHereda().equals(simboloNuevo.getLexema()) && !simboloNuevo.getClaseAPosterior()){
                                                                                    //AGREGO EL NOMBRE AL LEXEMA Y SIGO PROCESANDO
                                                                                     lexema = lexema+"."+lista_identificadores.get(i);
                                                                                     lexemaAuxFinAmbito = lista_identificadores.get(i);
                                                                                     simboloAnt = simboloNuevo;
                                                                                }else{
                                                                                    if(!simboloNuevo.getClaseAPosterior()){
                                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase no hereda de "+simboloNuevo.getLexema();
                                                                                        erroresSemanticos.add(err);
                                                                                        $$ = nodoError;
                                                                                        break;
                                                                                    }else{
                                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: Error de Forward declaration en la clase "+simboloNuevo.getLexema();
                                                                                        erroresSemanticos.add(err);
                                                                                        $$ = nodoError;
                                                                                        break;
                                                                                    }
                                                                                }

                                                                            }else{ 
                                                                                //SI EL SIMBOLO NO ES UNA CLASE, ENTONCES QUIERE DECIR QUE ES EL ULTIMO IDENTIFICADOR, POR EJEMPLO, SI EL IDENTIFICADOR
                                                                                //ORIGINAL ERA CLASE1.CLASE1.VARIABLE1, EN ESTE ESLE LO QUE HAGO ES AGREGAR VARIABLE1 AL LEXEMA                                                                     
                                                                                lexema = lexema+"."+lista_identificadores.get(i)+"#"+ambitoActual; //Aca hago clase1.num1#global. Asi la voy a guardar en la Tabla de simbolos
                                                                                TablaDeSimbolos.agregarSimbolo(lexema,Constantes.ID);
                                                                                //CREO EL NUEVO SIMBOLO Y SETEO SUS ATRIBUTOS
                                                                                Simbolo nuevo = TablaDeSimbolos.obtenerSimbolo(lexema);
                                                                                nuevo.setTipo(simboloNuevo.getTipo());
                                                                                nuevo.setUso(simboloNuevo.getUso());
                                                                                nuevo.setValor(simboloNuevo.getValor());
                                                                                
                                                                             }
                                                                        }else{
                                                                            String err = "Linea: "  + AnalizadorLexico.getLineaActual()+". Error Semantico: variable no declarada";
                                                                            erroresSemanticos.add(err);
                                                                            $$ = nodoError;
                                                                            break;
                                                                        }
                                                                    } 
                                                                }
                                                                
                                                              


                                                            }else{
                                                                
                                                                lexema = lista_identificadores.get(0)+"#"+ambitoActual;
                                                            }
                                                        }*/
                                                for(String s : lista_identificadores){
                                                    if(lexema.equals("")){
                                                        lexema = s;
                                                    }else{
                                                        lexema = lexema + "."+s;
                                                    }
                                                }

                                                lexema = lexema +"#"+ambitoActual;

                                                /*SI EL LEXEMA QUE FORME EXISTE EN LA TABLA DE SIMBOLOS*/
                                                if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() != -1){
                                                    Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexema); /*Obtengo el simbolo del lado izquierdo que acabo de crear o que ya existia*/
                                                    
                                                    ArbolSintactico arb = (ArbolSintactico) val_peek(0);
                                                    

                                                    String aux ="";
                                                    ArbolSintactico arb_aux = arb;
                                                    /*CON ESTE WHILE OBTENGO EL TIPO DEL LADO IZQUIERDO DE LA ASIGNACION, EN CASO DE QUE EL NODO PADRE NO SEA UN NUMERO O VARIABLE, SINO UN +,-,ETC*/
                                                    while(aux.equals("")){
                                                        if(arb_aux.getLex().contains("--")){
                                                            String lexArb = arb_aux.getLex().substring(0,arb_aux.getLex().length()-2);    
                                                            if(TablaDeSimbolos.obtenerSimbolo(lexArb).getTipo().equals("")){
                                                                if(arb_aux.getIzq() != null){
                                                                    arb_aux = arb_aux.getIzq();
                                                                }else{
                                                                    aux = "nada";
                                                                }
                                                            }else{
                                                                    aux = TablaDeSimbolos.obtenerSimbolo(lexArb).getTipo();
                                                            }
                                                        }else{
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
                                                    }

                                                    /*SI LOS TIPOS DE AMBOS LADOS DE LA ASIGNACION COINCIDEN*/
                                                    /*System.out.println("simbol: " + simbol.getTipo + " auxTipoAsig "+ auxTipoAsig + " tipoIzq: " + aux);*/
                                                    if(simbol.getTipo().equals(aux) || auxTipoAsig.equals(aux) || auxTof){ 
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
                                                auxTof = false;
                                            }
break;
case 82:
//#line 819 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la expresion despues del '='");
                                    yyval = (ArbolSintactico) nodoError;
                                    }
break;
case 83:
//#line 824 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 84:
//#line 825 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 85:
//#line 828 "gramatica.y"
{yyval = val_peek(0);
                                   
                                    }
break;
case 86:
//#line 833 "gramatica.y"
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
//#line 848 "gramatica.y"
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
//#line 860 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta END_IF para finalizar la sentencia IF");
                    System.out.println("ENTRE EN EL ERROR DE QUE FALTA ELSE_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 89:
//#line 864 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 90:
//#line 867 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 91:
//#line 870 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF y ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 92:
//#line 873 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 93:
//#line 876 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras ELSE Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 94:
//#line 879 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF, ELSE Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 95:
//#line 882 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 96:
//#line 885 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 97:
//#line 888 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloques sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 98:
//#line 891 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 99:
//#line 894 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 100:
//#line 897 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 101:
//#line 900 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 102:
//#line 903 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 103:
//#line 906 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 104:
//#line 909 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' antes del ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 105:
//#line 916 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR");
                                                      ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));    
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblIzqOp.getLex().contains("--")){
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex().substring(0,arblIzqOp.getLex().length()-2));
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                                }
                                                            }
                                                            
                                                            if(arblDerOp.getLex().equals("TOF")){
                                                                if(arblDerOp.getIzq().getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0, arblDerOp.getIzq().getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblDerOp.getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex().substring(0,arblDerOp.getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                                }
                                                            }
                                                            
                                                            /*Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());*/
                                                            /*Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());*/
                                                            
                                                            
                                                            
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    
                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }
                                                                    
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
                                                                /*sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex().substring(0,arblIzqOp.getIzq().getIzq().getLex().length()-2));    
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex());
                                                                    }
                                                                }else{
                                                                    if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                    }
                                                                }
                                                                
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }

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
//#line 1060 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR");
                                                 ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));    
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblIzqOp.getLex().contains("--")){
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex().substring(0,arblIzqOp.getLex().length()-2));
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                                }
                                                            }
                                                            
                                                            if(arblDerOp.getLex().equals("TOF")){
                                                                if(arblDerOp.getIzq().getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0, arblDerOp.getIzq().getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblDerOp.getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex().substring(0,arblDerOp.getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                                }
                                                            }
                                                            
                                                            /*Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());*/
                                                            /*Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());*/
                                                            
                                                            
                                                            
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    
                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }
                                                                    
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
                                                                /*sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex().substring(0,arblIzqOp.getIzq().getIzq().getLex().length()-2));    
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex());
                                                                    }
                                                                }else{
                                                                    if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                    }
                                                                }
                                                                
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }

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
case 107:
//#line 1202 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR O IGUAL");
                                                        ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));    
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblIzqOp.getLex().contains("--")){
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex().substring(0,arblIzqOp.getLex().length()-2));
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                                }
                                                            }
                                                            
                                                            if(arblDerOp.getLex().equals("TOF")){
                                                                if(arblDerOp.getIzq().getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0, arblDerOp.getIzq().getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblDerOp.getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex().substring(0,arblDerOp.getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                                }
                                                            }
                                                            
                                                            /*Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());*/
                                                            /*Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());*/
                                                            
                                                            
                                                            
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    
                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }
                                                                    
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
                                                                /*sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex().substring(0,arblIzqOp.getIzq().getIzq().getLex().length()-2));    
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex());
                                                                    }
                                                                }else{
                                                                    if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                    }
                                                                }
                                                                
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }

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
case 108:
//#line 1345 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR O IGUAL");
                                                        ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));    
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblIzqOp.getLex().contains("--")){
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex().substring(0,arblIzqOp.getLex().length()-2));
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                                }
                                                            }
                                                            
                                                            if(arblDerOp.getLex().equals("TOF")){
                                                                if(arblDerOp.getIzq().getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0, arblDerOp.getIzq().getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblDerOp.getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex().substring(0,arblDerOp.getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                                }
                                                            }
                                                            
                                                            /*Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());*/
                                                            /*Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());*/
                                                            
                                                            
                                                            
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    
                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }
                                                                    
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
                                                                /*sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex().substring(0,arblIzqOp.getIzq().getIzq().getLex().length()-2));    
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex());
                                                                    }
                                                                }else{
                                                                    if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                    }
                                                                }
                                                                
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }

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
case 109:
//#line 1488 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR IGUAL");
                                                            ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));    
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblIzqOp.getLex().contains("--")){
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex().substring(0,arblIzqOp.getLex().length()-2));
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                                }
                                                            }
                                                            
                                                            if(arblDerOp.getLex().equals("TOF")){
                                                                if(arblDerOp.getIzq().getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0, arblDerOp.getIzq().getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblDerOp.getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex().substring(0,arblDerOp.getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                                }
                                                            }
                                                            
                                                            /*Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());*/
                                                            /*Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());*/
                                                            
                                                            
                                                            
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    
                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }
                                                                    
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
                                                                /*sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex().substring(0,arblIzqOp.getIzq().getIzq().getLex().length()-2));    
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex());
                                                                    }
                                                                }else{
                                                                    if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                    }
                                                                }
                                                                
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }

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
case 110:
//#line 1631 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR DISTINTO");
                                                                     ArbolSintactico arb1 = (ArbolSintactico) val_peek(2);
                                                        ArbolSintactico arb2 = (ArbolSintactico) val_peek(0);

                                                        if((ArbolSintactico) val_peek(2) != nodoError && (ArbolSintactico) val_peek(0) != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) val_peek(2);
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) val_peek(0); 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));    
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblIzqOp.getLex().contains("--")){
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex().substring(0,arblIzqOp.getLex().length()-2));
                                                                }else{
                                                                    sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                                }
                                                            }
                                                            
                                                            if(arblDerOp.getLex().equals("TOF")){
                                                                if(arblDerOp.getIzq().getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0, arblDerOp.getIzq().getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                }
                                                            }else{
                                                                if(arblDerOp.getLex().contains("--")){
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex().substring(0,arblDerOp.getLex().length()-2));
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                                }
                                                            }
                                                            
                                                            /*Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());*/
                                                            /*Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());*/
                                                            
                                                            
                                                            
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/
                                                                    
                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }
                                                                    
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
                                                                /*sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si*/
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex().substring(0,arblIzqOp.getIzq().getIzq().getLex().length()-2));    
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getIzq().getLex());
                                                                    }
                                                                }else{
                                                                    if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex().substring(0,arblIzqOp.getIzq().getLex().length()-2));
                                                                    }else{
                                                                        sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());
                                                                    }
                                                                }
                                                                
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
                                                                    /*sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo*/

                                                                    if(arblDerOp.getIzq().getLex().equals("TOF")){
                                                                        if(arblDerOp.getIzq().getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex().substring(0, arblDerOp.getIzq().getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getIzq().getLex());
                                                                        }
                                                                    }else{
                                                                        if(arblDerOp.getIzq().getLex().contains("--")){
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex().substring(0,arblDerOp.getIzq().getLex().length()-2));
                                                                        }else{
                                                                            sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex());
                                                                        }
                                                                    }

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
case 111:
//#line 1776 "gramatica.y"
{
                                                                        yyval=new NodoComun("Sentencia_Dentro_IF", (ArbolSintactico) val_peek(1), (ArbolSintactico) val_peek(0));}
break;
case 112:
//#line 1778 "gramatica.y"
{yyval=val_peek(0);}
break;
case 113:
//#line 1781 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia SALIDA con CADENA");
                                 NodoHoja cadena = new NodoHoja(val_peek(0).sval);
                                 NodoControl nodo = new NodoControl(val_peek(1).sval, (ArbolSintactico) cadena);
                                 yyval = (ArbolSintactico) nodo; 
                                }
break;
case 114:
//#line 1788 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una SENTENCIA WHILE");
                                        yyval = val_peek(0);
                                       }
break;
case 115:
//#line 1793 "gramatica.y"
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
//#line 1802 "gramatica.y"
{
                                                                NodoControl condicion = new NodoControl("condicion_while", (ArbolSintactico) val_peek(4));
                                                                NodoControl cuerpo_while = new NodoControl("cuerpo_while", null);
                                                                yyval = (ArbolSintactico) new NodoComun(val_peek(6).sval, condicion, cuerpo_while);
                                                                }
break;
case 117:
//#line 1808 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra DO");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 118:
//#line 1811 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra WHILE");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 119:
//#line 1814 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 120:
//#line 1820 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una SUMA");
                                            
                                             ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                                             ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;


                                            if(arbIzq.getLex().equals("TOF")){
                                            	if(arbIzq.getIzq().getLex().contains("--")){
                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex().substring(0,arbIzq.getIzq().getLex().length()-2));    
                                                }else{
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                                }
                                            }else{
                                                if(arbIzq.getLex().contains("--")){
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex().substring(0,arbIzq.getLex().length()-2));
                                                }else{
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                                }
                                             }
                                             
                                             if(arbDer.getLex().equals("TOF")){
                                                if(arbDer.getIzq().getLex().contains("--")){
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex().substring(0, arbDer.getIzq().getLex().length()-2));
                                                }else{
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                                }
                                             }else{
                                                if(arbDer.getLex().contains("--")){
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex().substring(0,arbDer.getLex().length()-2));
                                                }else{
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                                }
                                             }

                                            

                                             if(arbIzq != nodoError && arbDer != nodoError){
                                               if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
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
                                                    }else{
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
                                               }
                                             }else{
                                                yyval = nodoError;
                                             } 
                                            }
break;
case 121:
//#line 1946 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una RESTA");

                                             ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                                             ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;

                                            if(arbIzq.getLex().equals("TOF")){
                                            	if(arbIzq.getIzq().getLex().contains("--")){
                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex().substring(0,arbIzq.getIzq().getLex().length()-2));    
                                                }else{
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                                }
                                            }else{
                                                if(arbIzq.getLex().contains("--")){
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex().substring(0,arbIzq.getLex().length()-2));
                                                }else{
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                                }
                                             }
                                             
                                             if(arbDer.getLex().equals("TOF")){
                                                if(arbDer.getIzq().getLex().contains("--")){
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex().substring(0, arbDer.getIzq().getLex().length()-2));
                                                }else{
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                                }
                                             }else{
                                                if(arbDer.getLex().contains("--")){
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex().substring(0,arbDer.getLex().length()-2));
                                                }else{
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                                }
                                             }

                                            /*
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
                                            */


                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                /*System.out.println("IZq: " + arbIzq.getLex().equals("TOF")+ " der: " + arbDer.getLex().equals("TOF"));*/
                                                if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
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
                                                }else{    
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
                                                }
                                             }else{
                                                yyval = nodoError;
                                             }
                                            }
break;
case 122:
//#line 2084 "gramatica.y"
{yyval = val_peek(0); 

                        }
break;
case 123:
//#line 2089 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             

                                             ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                                             ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;

                                             System.out.println("EL ARBOL IZQ: " + arbIzq.getLex() + " EL ARBOL DER: "+ arbDer.getLex());


                                            if(arbIzq.getLex().equals("TOF")){
                                            	if(arbIzq.getIzq().getLex().contains("--")){
                                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex().substring(0,arbIzq.getIzq().getLex().length()-2));    
                                                }else{
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                                }
                                            }else{
                                                if(arbIzq.getLex().contains("--")){
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex().substring(0,arbIzq.getLex().length()-2));
                                                }else{
                                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                                }
                                             }
                                             
                                             if(arbDer.getLex().equals("TOF")){
                                                if(arbDer.getIzq().getLex().contains("--")){
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex().substring(0, arbDer.getIzq().getLex().length()-2));
                                                }else{
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                                }
                                             }else{
                                                if(arbDer.getLex().contains("--")){
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex().substring(0,arbDer.getLex().length()-2));
                                                }else{
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                                }
                                             }

                                            /*
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
                                            */


                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                /*System.out.println("IZq: " + arbIzq.getLex().equals("TOF")+ " der: " + arbDer.getLex().equals("TOF"));*/
                                                if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
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
                                                }else{    
                                                    if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                        /*Si son dos constantes hago la operacion y retorno la suma de ambos*/
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
                                                }
                                             }else{
                                                yyval = nodoError;
                                             }
                            }
break;
case 124:
//#line 2231 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             
                                            NodoHoja factor = (NodoHoja) val_peek(1);
                                            if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                                auxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo();
                                                simboloAuxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()); 
                                                /*TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); //Cambio el tipo de la variable*/
                                                auxTipoAsig = "FLOAT";
                                                auxTof = true;
                                                NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) val_peek(1));
                                                
                                                ArbolSintactico arbIzq = (ArbolSintactico) val_peek(5);
                                                ArbolSintactico arbDer = nodoTof;
                                                Simbolo simbolo1;
                                                Simbolo simbolo2;

                                                System.out.println("EL ARBOL IZQ: " + arbIzq.getLex() + " EL ARBOL DER: "+ arbDer.getLex());


                                                if(arbIzq.getLex().equals("TOF")){
                                                    if(arbIzq.getIzq().getLex().contains("--")){
                                                        /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex().substring(0,arbIzq.getIzq().getLex().length()-2));    
                                                    }else{
                                                        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                                    }
                                                }else{
                                                    if(arbIzq.getLex().contains("--")){
                                                        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex().substring(0,arbIzq.getLex().length()-2));
                                                    }else{
                                                        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                                    }
                                                }
                                                
                                                
                                                if(arbDer.getIzq().getLex().contains("--")){
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex().substring(0, arbDer.getIzq().getLex().length()-2));
                                                }else{
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                                }
                                                


                                                if(arbIzq != nodoError && arbDer != nodoError){
                                                    /*System.out.println("IZq: " + arbIzq.getLex().equals("TOF")+ " der: " + arbDer.getLex().equals("TOF"));*/
                                                    if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                                        if(simbolo1.getValorAsignado()){ 
                                                                            if(simbolo2.getUso().equals("identificador")){ 
                                                                                if(simbolo2.getValorAsignado()){                                                
                                                                                    yyval = (ArbolSintactico) new NodoComun(val_peek(4).sval,(ArbolSintactico)val_peek(5),nodoTof); /*Hago $1 ------- * ------- $2*/
                                                                                }else{
                                                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                                    erroresSemanticos.add(err);
                                                                                    yyval = (ArbolSintactico) nodoError;
                                                                                }
                                                                            }else{
                                                                                yyval = (ArbolSintactico) new NodoComun(val_peek(4).sval,(ArbolSintactico)val_peek(5),nodoTof);
                                                                                

                                                                            }
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                            erroresSemanticos.add(err);
                                                                            yyval = (ArbolSintactico) nodoError;
                                                                        }
                                                                    }else{
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(4).sval,(ArbolSintactico)val_peek(5),nodoTof);
                                                                    }
                                                    }
                                                }else{
                                                    yyval = nodoError;
                                                }
                                            }else{
                                                String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                                erroresSemanticos.add(err);
                                                yyval = (ArbolSintactico) nodoError;
                                            }
                            }
break;
case 125:
//#line 2311 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una DIVISION");
                            ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                            ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                            Simbolo simbolo1;
                            Simbolo simbolo2;

                            if(arbIzq.getLex().equals("TOF")){
                                if(arbIzq.getIzq().getLex().contains("--")){
                                    /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex().substring(0,arbIzq.getIzq().getLex().length()-2));    
                                }else{
                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                }
                            }else{
                                if(arbIzq.getLex().contains("--")){
                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex().substring(0,arbIzq.getLex().length()-2));
                                }else{
                                    simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                }
                            }
                                
                            if(arbDer.getLex().equals("TOF")){
                                if(arbDer.getIzq().getLex().contains("--")){
                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex().substring(0, arbDer.getIzq().getLex().length()-2));
                                }else{
                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                }
                            }else{
                                if(arbDer.getLex().contains("--")){
                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex().substring(0,arbDer.getLex().length()-2));
                                }else{
                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                }
                            }

                            if(arbIzq != nodoError && arbDer != nodoError){
                                if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
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
                                    }else{        
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
                                }
                            }else{
                            yyval = nodoError;
                            }
                    }
break;
case 126:
//#line 2430 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             
                                            NodoHoja factor = (NodoHoja) val_peek(1);
                                            if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                                auxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo();
                                                simboloAuxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()); 
                                                /*TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); //Cambio el tipo de la variable*/
                                                auxTipoAsig = "FLOAT";
                                                auxTof = true;
                                                NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) val_peek(1));
                                                
                                                ArbolSintactico arbIzq = (ArbolSintactico) val_peek(5);
                                                ArbolSintactico arbDer = nodoTof;
                                                Simbolo simbolo1;
                                                Simbolo simbolo2;

                                                System.out.println("EL ARBOL IZQ: " + arbIzq.getLex() + " EL ARBOL DER: "+ arbDer.getLex());


                                                if(arbIzq.getLex().equals("TOF")){
                                                    if(arbIzq.getIzq().getLex().contains("--")){
                                                        /*el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve*/
                                                        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex().substring(0,arbIzq.getIzq().getLex().length()-2));    
                                                    }else{
                                                        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                                    }
                                                }else{
                                                    if(arbIzq.getLex().contains("--")){
                                                        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex().substring(0,arbIzq.getLex().length()-2));
                                                    }else{
                                                        simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                                    }
                                                }
                                                
                                                
                                                if(arbDer.getIzq().getLex().contains("--")){
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex().substring(0, arbDer.getIzq().getLex().length()-2));
                                                }else{
                                                    simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                                }
                                                


                                                if(arbIzq != nodoError && arbDer != nodoError){
                                                    /*System.out.println("IZq: " + arbIzq.getLex().equals("TOF")+ " der: " + arbDer.getLex().equals("TOF"));*/
                                                    if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                                        if(simbolo1.getValorAsignado()){ 
                                                                            if(simbolo2.getUso().equals("identificador")){ 
                                                                                if(simbolo2.getValorAsignado()){                                                
                                                                                    yyval = (ArbolSintactico) new NodoComun(val_peek(4).sval,(ArbolSintactico)val_peek(5),nodoTof); /*Hago $1 ------- * ------- $2*/
                                                                                }else{
                                                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                                    erroresSemanticos.add(err);
                                                                                    yyval = (ArbolSintactico) nodoError;
                                                                                }
                                                                            }else{
                                                                                yyval = (ArbolSintactico) new NodoComun(val_peek(4).sval,(ArbolSintactico)val_peek(5),nodoTof);
                                                                                

                                                                            }
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                            erroresSemanticos.add(err);
                                                                            yyval = (ArbolSintactico) nodoError;
                                                                        }
                                                                    }else{
                                                                        yyval = (ArbolSintactico) new NodoComun(val_peek(4).sval,(ArbolSintactico)val_peek(5),nodoTof);
                                                                    }
                                                    }
                                                }else{
                                                    yyval = nodoError;
                                                }
                                            }else{
                                                String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                                erroresSemanticos.add(err);
                                                yyval = (ArbolSintactico) nodoError;
                                            }
            }
break;
case 127:
//#line 2509 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 128:
//#line 2510 "gramatica.y"
{    System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un FACTOR con CONVERSION DE TIPO");
                                    
                                NodoHoja factor = (NodoHoja) val_peek(1);
                                if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                    auxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo();
                                    simboloAuxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()); 
                                    /*TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); //Cambio el tipo de la variable*/
                                    auxTipoAsig = "FLOAT";
                                    auxTof = true;
                                    NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) val_peek(1));
                                    yyval = (ArbolSintactico) nodoTof;
                                }else{
                                    String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                    erroresSemanticos.add(err);
                                    yyval = (ArbolSintactico) nodoError;
                                }

                                
                            }
break;
case 129:
//#line 2531 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador con MENOS_MENOS");
                        String auxIdentificador = "";
                        for(String s: auxDerAsig){
                            if( auxIdentificador.equals("")){
                                auxIdentificador = s;
                            }else{
                                auxIdentificador = auxIdentificador +"."+s;
                            }
                        }
                        auxIdentificador = auxIdentificador+"#"+ambitoActual;
                        Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(auxIdentificador);
                        if(simbolo.getId() != -1){
                            simbolo.setUsada(true);
                            simbolo.setValorAsignado(true);
                            NodoHoja hoja = new NodoHoja(simbolo.getLexema()+"--");
                            yyval = (ArbolSintactico) hoja;
                        }else{
                            String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+auxIdentificador+ " no declarada";
                            erroresSemanticos.add(err);
                            yyval = (ArbolSintactico) nodoError;
                        }
                        auxDerAsig.clear();
                        if(auxTipoAsig.equals("")){
                            /*Esto me sirve para resolver la comparacion del parametro de una funcion*/
                            auxTipoAsig = simbolo.getTipo();
                        }
                        
                        /*
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
                            NodoHoja hoja = new NodoHoja(lexema+"--");
                            $$ = hoja;
                           
                        }else{
                            String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+lexema+ " no declarada";
                            erroresSemanticos.add(err);
                            $$ = (ArbolSintactico) nodoError;
                        }
                        if(auxTipoAsig.equals("")){
                            auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(lexema).getTipo();
                        }*/

                        }
break;
case 130:
//#line 2586 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IDENTIFICADOR ");
            String lexema = "";
            /* for(String s : auxDerAsig){*/
              
              String auxAmbito = "";
              String auxIdentificador = "";
              String restoAmbito = "";
              for(String s: auxDerAsig){
                if( auxIdentificador.equals("")){
                    auxIdentificador = s;
                }else{
                    auxIdentificador = auxIdentificador +"."+s;
                }
              }
              auxIdentificador = auxIdentificador+"#"+ambitoActual;
              Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(auxIdentificador);
              if(simbolo.getId() != -1){
                simbolo.setUsada(true);
                simbolo.setValorAsignado(true);
                NodoHoja hoja = new NodoHoja(simbolo.getLexema());
                yyval = (ArbolSintactico) hoja;
              }else{
                String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+auxIdentificador+ " no declarada";
                erroresSemanticos.add(err);
                yyval = (ArbolSintactico) nodoError;
              }
              auxDerAsig.clear();
              if(auxTipoAsig.equals("")){
                /*Esto me sirve para resolver la comparacion del parametro de una funcion*/
                auxTipoAsig = simbolo.getTipo();
              }

              
                /* if(TablaDeSimbolos.obtenerSimbolo(auxDerAsig.get(0)+"#"+ambitoActual).getUso().equals("clase")){
                    //QUIERE DECIR QUE ES UN ATRIBUTO DE UNA CLASE QUE HEREDA Y NO UN OBJETO DE CLASE
                    for(int i = auxDerAsig.size()-1;i>=0;i--){
                        System.out.println("VALOR EN i= " + i + " es: "+ auxDerAsig.get(i));
                        //System.out.println("EL LEXEMA DEL DERECHO DE LA ASIGNACION HASTA AHORA ES: " + lexema + " El identificador que puedo agregar es (s+'#'+ambitoActual): " + auxDerAsig.get(i)+"#"+ambitoActual);
                        if(lexema.equals("")){
                            //lexema = auxDerAsig.get(i);
                            lexema = "nada";
                            auxIdentificador = auxDerAsig.get(i);
                        }else{
                            if(i == auxDerAsig.size()-2){
                                auxAmbito = auxDerAsig.get(i);
                            }else{
                                if(restoAmbito.equals("")){
                                    restoAmbito = "";
                                }else{
                                    restoAmbito = restoAmbito+"."+auxDerAsig.get(i);
                                }
                            }
                        }
                    }
                    String ambitoMedio = ambitoActual;
                    Simbolo simboloAux = Constantes.SIMBOLO_NO_ENCONTRADO;
                    if(!auxAmbito.equals("")){
                        System.out.println("ENTRE EN EL IF. auxId: " + auxIdentificador + " auxAmbito: "+ auxAmbito);
                        while(simboloAux.getId() == -1 && !ambitoMedio.equals("global") && !simboloAux.getLexema().contains(auxAmbito)){
                            int lastIndex = ambitoMedio.lastIndexOf("#");
                            ambitoMedio = ambitoMedio.substring(0,lastIndex);
                            simboloAux = TablaDeSimbolos.obtenerSimbolo(auxIdentificador+"#"+ambitoMedio+"#"+auxAmbito);
                        }            
                    }else{
                        System.out.println("ENTRE AL ELSE");
                        simboloAux = TablaDeSimbolos.obtenerSimbolo(auxIdentificador+"#"+ambitoMedio);
                    }
                    simboloAux.setUsada(true);
                    simboloAux.setValorAsignado(true);
                    lexema = simboloAux.getLexema();
                }else{
                    if(TablaDeSimbolos.obtenerSimbolo(auxDerAsig.get(0)+"#"+ambitoActual).getUso().equals("identificador")){
                        // QUIERE DECIR QUE EL PRIMER OBJETO ES EL NOMBRE DE UN OBJETO DE CLASE, POR EJEMPLO, c1 objt1,
                        for(String s : auxDerAsig){
                            if(lexema.equals("")){
                                lexema = s;
                            }else{
                                lexema = lexema+"."+s;
                            }
                        }
                        lexema = lexema +"#"+ambitoActual;
                    }
                } 
            // auxIdentificador: primer id
            // auxAmbito: final del ambito del identificador
            // ambitoMedio: El ambito actual
            // restoAmbito: resto de identificadores
            

            auxDerAsig.clear();
            //lexema = lexema + "#"+ambitoActual;
           
            System.out.println("El lexema final es: " + lexema);
            Simbolo aux = TablaDeSimbolos.obtenerSimbolo(lexema);
            if(aux.getId() != -1){
                aux.setUsada(true);
                NodoHoja hoja = new NodoHoja(aux.getLexema());
                $$ = (ArbolSintactico) hoja;
            }else{
                String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+$1.sval+ " no declarada";
                erroresSemanticos.add(err);
                $$ = (ArbolSintactico) nodoError;
            }
            if(auxTipoAsig.equals("")){
                //Esto me sirve para resolver la comparacion del parametro de una funcion
                auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(lexema).getTipo();
            }*/
        }
break;
case 131:
//#line 2694 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 132:
//#line 2697 "gramatica.y"
{ 
                                    auxDerAsig.add(val_peek(0).sval);
                                    yyval = val_peek(0);
                                }
break;
case 133:
//#line 2701 "gramatica.y"
{ 
                auxDerAsig.add(val_peek(0).sval);
                yyval = val_peek(0);
                }
break;
case 134:
//#line 2708 "gramatica.y"
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
case 135:
//#line 2717 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una CONSTANTE NEGATIVA");
               constanteNegativa(val_peek(0).sval);
               yyval = (ArbolSintactico) new NodoHoja("-" + val_peek(0).sval); /* padre ------- $1*/
                if(auxTipoAsig.equals("")){
                    auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(1).sval).getTipo();
                }
             }
break;
//#line 4185 "Parser.java"
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
