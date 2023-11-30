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
   13,   12,   12,   12,   12,   12,   10,   10,   10,   11,
   11,   14,   14,   15,   15,   16,   16,   16,   16,   16,
   16,   16,   16,   17,   18,    8,    8,    8,    8,    8,
   19,   19,   20,   20,   20,   20,    9,   21,   21,   21,
    5,    5,    5,    5,    5,    5,    5,    5,    5,    5,
    5,   26,   26,   26,   26,   26,   26,   22,   22,   28,
   28,   29,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   30,   30,   30,   30,   30,   30,   31,   31,   24,
   24,   25,   32,   32,   32,   32,   32,   27,   27,   27,
   33,   33,   33,   33,   34,   34,   34,   35,   35,   36,
   36,
};
final static short yylen[] = {                            2,
    1,    3,    3,    3,    3,    2,    1,    1,    1,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    7,    6,    7,    7,    7,    7,    6,    6,    6,    6,
    2,    2,    9,    9,    7,    7,    1,    1,    1,    3,
    1,    2,    1,    1,    1,    2,    2,    2,    2,    2,
    2,    2,    2,    1,    1,    4,    1,    4,    4,    4,
    2,    5,    2,    1,    2,    2,    2,    3,    1,    3,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    4,    3,    4,    3,    3,    4,    3,    3,    3,
    1,    1,   12,    8,   12,   12,   12,   11,   11,   11,
   10,   12,   12,   11,   12,    8,    8,    7,    8,    8,
   12,    3,    3,    3,    3,    3,    3,    2,    1,    2,
    2,    1,    8,    7,    8,    8,    8,    3,    3,    1,
    3,    3,    1,    4,    2,    1,    1,    3,    1,    1,
    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,   37,   38,   39,    0,   55,
    0,    0,    0,    1,    0,    7,    8,    9,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  122,    0,  140,    0,    0,    0,    0,
    0,    0,    0,  133,    0,  137,    0,  121,  120,   61,
    0,   32,    0,    0,    0,    0,  139,    0,    0,    0,
    6,   14,   10,   15,   11,   16,   12,   17,   13,   20,
   41,    0,   19,    0,    0,    0,    0,    0,   76,    0,
    0,    0,    0,    0,    0,    0,   64,    0,    0,   77,
   71,   78,   72,   79,   73,   80,   74,   81,   75,    0,
    0,    0,  141,   85,    0,    0,    0,   86,    0,    0,
    0,    0,    0,    0,    0,  135,    0,    0,    0,    0,
    0,    0,    0,    0,    5,    3,    2,    0,    0,    0,
    0,    0,    0,    0,    4,   40,    0,    0,    0,    0,
   31,    0,    0,    0,   65,   51,   46,   52,   47,   66,
   50,   53,   48,   49,    0,    0,    0,   63,   89,    0,
   88,   90,    0,    0,    0,   84,   82,   70,   68,    0,
    0,   87,  131,  132,  138,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   45,    0,   43,   44,    0,    0,    0,    0,    0,    0,
   60,   58,   56,   69,    0,   59,  134,    0,    0,    0,
  119,    0,    0,   62,    0,    0,    0,    0,    0,    0,
    0,    0,   42,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  118,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   29,   27,   22,   28,    0,
    0,    0,   30,    0,   36,    0,   83,    0,    0,    0,
    0,    0,    0,   35,    0,    0,  124,    0,    0,    0,
  108,    0,    0,    0,   25,   23,   21,   24,   26,    0,
    0,  110,    0,  109,    0,  106,    0,   94,    0,    0,
  127,  123,  125,  126,    0,  107,    0,   34,    0,    0,
    0,    0,    0,    0,   33,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  101,    0,    0,  104,    0,
    0,    0,    0,  100,    0,   99,    0,   98,  105,  103,
  111,  102,   95,   93,   96,   97,
};
final static short yydgoto[] = {                         13,
   14,   15,   16,   17,  191,   83,   84,  157,   86,   23,
   24,   25,   78,  192,  193,  194,   88,   26,   27,   89,
   41,   28,   29,   30,   31,   32,   42,   33,  161,   59,
  212,   34,   43,   44,   45,   46,
};
final static short yysindex[] = {                       118,
  -34,  -25, -106,  -94,  -38,    0,    0,    0,   19,    0,
  131,  159,    0,    0,  155,    0,    0,    0,   -8,   12,
   27,   29, -121,  -45,  142,   20,  170,   33,   45,   50,
   67,   83,   47,    0,    0,    0,   32, -167,  460, -154,
  -54,  116,    8,    0,  -43,    0,   84,    0,    0,    0,
  298,    0,  256,  135, -149,   64,    0,  -11,   77, -133,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   69,    0, -107,  -31,    9,  -91,  133,    0,  -34,
  188,  -96,   94,   97,  105,  108,    0,  137,  679,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  529,
  -78,   68,    0,    0,   65,  -67,   25,    0,  -58,  -56,
  159,  159,  -49,   68,   68,    0,  -55,   86,  171,  685,
  106,  -27,  -18,  205,    0,    0,    0,  159,  159,  159,
  159,  159,  159,  -98,    0,    0,  298,  211,  -17,  298,
    0,  132,    2,  667,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -10,    4,  213,    0,    0,  111,
    0,    0,  220,  298,  225,    0,    0,    0,    0,    8,
    8,    0,    0,    0,    0,  503,  165,   14,  298,  253,
  189,  -97,  111,  111,  111,  111,  111,  111,  192,  503,
    0,  227,    0,    0,  298,  247,  298,  282,  315,  298,
    0,    0,    0,    0,  322,    0,    0,  336,  231,  -32,
    0,  481,  485,    0,  361,  234,  503,  237,  503,  503,
  486,  150,    0,   80,  157,  374,  298,  392,  169,  407,
   65,  172,  298,  -41,  -77,    0,  -75,  462,  173,  298,
  519,  537,  552,  557,  -92,    0,    0,    0,    0,  186,
  102,  187,    0,  190,    0,  427,    0,  251,  193,  288,
  194,  321,  -62,    0,  443,  202,    0,  572,  204,  206,
    0,  340,  208,  503,    0,    0,    0,    0,    0,  209,
  503,    0,  576,    0,  503,    0,  343,    0,  503,  214,
    0,    0,    0,    0,  503,    0,  577,    0,  595,  207,
  596,  611,  626,  639,    0,  643, -181,  210,  217,  219,
  223,  226,  659, -166, -125,    0,  233,  241,    0,  244,
  250,  252, -123,    0,  263,    0,  271,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
  147,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  121,    0,    0,    0,
    0,    0,    0,    0,  -24,    0,    0,    0,    0,    0,
  123,    0,    3,    0,   -2,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  124,    0,    0,    0,    0,    0,    0,    0,   39,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  134,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -6,    0,    0,    0,    0,  148,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   35,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  468,  487,  488,  489,  492,  493,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   55,
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
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  525,   18,    0,    1,   99,  119,  955,  520,   87,
  480,    0,  463,  799,  792,  678,    0,    0,    0,  -14,
    0,    0,    0,    0,    0,    0,   15,    0,    0,   70,
  698,    0,   85,    7,    0,    0,
};
final static int YYTABLESIZE=1057;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        257,
   18,   53,  117,   38,  110,   39,   40,  234,   40,  138,
   38,   18,   38,   74,   47,   18,  139,  139,  139,   69,
  139,  139,  139,  198,  190,  219,   58,   82,   51,  205,
  274,  111,   61,  112,   69,   63,  120,   54,  136,  136,
  136,  136,  136,  130,  136,  130,  130,  130,  133,  114,
  132,   82,   41,  107,  115,   65,   18,  136,   54,  136,
  289,   58,  130,   79,  130,  167,  144,  111,   58,  112,
   67,  102,   69,   61,  316,  128,   91,  128,  128,  128,
  317,   82,   54,  129,   91,  129,  129,  129,   93,  324,
  103,  137,  101,   95,  128,  325,  128,   41,   19,   91,
   91,  108,  129,   12,  129,  197,  125,  100,  163,   19,
   97,   77,   38,   19,  160,   91,  119,  134,   20,   12,
  173,  174,  135,  124,  118,  106,   99,   74,   38,   20,
  326,  140,  333,   20,   70,   71,  327,  147,  334,  122,
  149,   12,  183,  184,  185,  186,  187,  188,  151,  136,
   48,  153,   49,  111,   19,  112,  113,   12,  111,  145,
  112,   77,   50,  271,   57,  141,   67,   18,  272,  273,
   12,  189,  218,  142,   20,  123,  211,   83,  162,   38,
  154,   75,   76,  258,  259,  260,  261,  164,  127,  165,
  211,   92,   91,  286,   12,  170,  171,  168,  287,  288,
  169,  175,  109,   38,  248,   41,  172,   91,  176,   12,
   73,  177,  236,  211,  104,   57,   36,  211,   52,  211,
  211,  236,   35,   36,   57,   36,  277,   12,  179,  180,
   37,   69,   69,  116,    6,    7,    8,   37,  236,   37,
   11,  236,  211,  236,  236,  182,  204,   62,  107,   41,
   12,  181,  139,  136,  200,   55,  151,  201,  130,  206,
  207,  128,  129,  130,  131,  209,   12,   64,  236,  214,
  136,  136,  136,  136,  211,  130,  130,  130,  130,   60,
  166,  211,   66,  211,   68,  211,   12,  213,   90,  211,
  128,  106,   81,  216,   41,  211,  121,  236,  129,  236,
   92,  236,  236,  211,  236,   94,  236,  128,  128,  128,
  128,  217,  143,  236,  220,  129,  129,  129,  129,  126,
    1,   12,   96,    2,   57,   36,    3,    4,    5,    6,
    7,    8,    9,  195,   10,  247,   80,   12,   98,    2,
   57,   36,    3,    4,    5,    6,    7,    8,    9,  146,
   10,  222,  148,  233,   12,   37,  240,  276,   80,  242,
  150,    2,  231,  152,    3,    4,    5,    6,    7,    8,
    9,  225,   10,  281,    1,   12,   57,    2,   67,   18,
    3,    4,    5,    6,    7,    8,    9,    1,   10,   83,
    2,   57,   36,    3,    4,    5,    6,    7,    8,    9,
   12,   10,   41,   92,  227,  246,   37,    6,    7,    8,
  283,    1,  249,   12,    2,   57,   36,    3,    4,    5,
    6,    7,    8,    9,  253,   10,   80,  255,  264,    2,
   37,   12,    3,    4,    5,    6,    7,    8,    9,  229,
   10,  275,  278,  285,   80,  279,   12,    2,  282,  284,
    3,    4,    5,    6,    7,    8,    9,  291,   10,  293,
  232,  294,  295,  296,  298,  303,   12,   80,  309,  305,
    2,  318,  319,    3,    4,    5,    6,    7,    8,    9,
  320,   10,   12,   80,  321,  239,    2,  322,  328,    3,
    4,    5,    6,    7,    8,    9,  329,   10,  250,  330,
  105,   12,   72,   80,   38,  331,    2,  332,  114,    3,
    4,    5,    6,    7,    8,    9,  252,   10,  335,   22,
   12,    6,    7,    8,   12,   12,  336,  115,  116,  117,
   22,  254,  112,  113,   22,   56,    0,  139,   80,    0,
    0,    2,   12,    0,    3,    4,    5,    6,    7,    8,
    9,  280,   10,    0,   80,    0,    0,    2,   12,    0,
    3,    4,    5,    6,    7,    8,    9,  290,   10,    0,
    0,   80,    0,   38,    2,   22,   12,    3,    4,    5,
    6,    7,    8,    9,    0,   10,  263,    6,    7,    8,
    0,   12,   80,    0,    0,    2,   12,    0,    3,    4,
    5,    6,    7,    8,    9,  235,   10,    0,    0,  237,
  245,   12,    0,    0,    0,   12,   12,   80,    0,    0,
    2,    0,    0,    3,    4,    5,    6,    7,    8,    9,
   80,   10,    0,    2,   12,   12,    3,    4,    5,    6,
    7,    8,    9,  266,   10,    0,    0,    0,   80,    0,
   12,    2,    0,    0,    3,    4,    5,    6,    7,    8,
    9,  267,   10,   80,    0,   12,    2,    0,    0,    3,
    4,    5,    6,    7,    8,    9,  269,   10,   12,    0,
    0,  270,   12,   80,    0,    0,    2,    0,    0,    3,
    4,    5,    6,    7,    8,    9,  292,   10,   12,   80,
  300,  307,    2,    0,   87,    3,    4,    5,    6,    7,
    8,    9,    0,   10,    0,  104,   57,   36,  210,  308,
  310,    2,  262,    0,    3,    6,    7,    8,   87,    0,
    9,   37,   10,    0,    0,  311,    0,  210,    0,    0,
    2,  210,  210,    3,    2,    2,    0,    3,    3,    9,
  312,   10,    0,    9,    9,   10,   10,    0,   87,  210,
    0,    0,    2,  314,    0,    3,  158,  315,    0,    0,
    0,    9,    0,   10,    0,  210,    0,    0,    2,    0,
    0,    3,    0,  323,  159,   57,   36,    9,    0,   10,
    0,  203,    0,  210,    0,    0,    2,  158,    0,    3,
   37,    0,    0,  156,    0,    9,    0,   10,  210,  178,
    0,    2,    0,  210,    3,    0,    2,    0,    0,    3,
    9,  158,   10,    0,    0,    9,    0,   10,  210,    0,
    0,    2,  210,  210,    3,    2,    2,    0,    3,    3,
    9,    0,   10,    0,    9,    9,   10,   10,    0,    0,
    0,  210,  210,    0,    2,    2,    0,    3,    3,    0,
    0,    0,    0,    9,    9,   10,   10,  210,    0,    0,
    2,    0,    0,    3,    0,    0,    0,    0,    0,    9,
    0,   10,  210,    0,    0,    2,    0,  221,    3,    0,
    0,    0,    0,    0,    9,  210,   10,    0,    2,  210,
    0,    3,    2,    0,    0,    3,    0,    9,    0,   10,
  238,    9,    0,   10,  241,  210,  243,  244,    2,    0,
    0,    3,  202,  155,    0,    0,    0,    9,    0,   10,
    4,    5,    6,    7,    8,  155,  196,    0,  199,  268,
    0,  155,    4,    5,    6,    7,    8,    0,    4,    5,
    6,    7,    8,    0,   21,    0,    0,    0,    0,    0,
    0,    0,  208,    0,    0,   21,    0,    0,    0,   21,
    0,  297,    0,    0,    0,    0,    0,  215,  299,    0,
  301,   85,  302,  223,    0,    0,  304,  223,    0,    0,
  223,    0,  306,  224,    0,  226,  228,    0,  230,  223,
  313,    0,    0,    0,    0,   85,  223,    0,    0,    0,
   21,    0,    0,    0,    0,  223,    0,  223,    0,  223,
    0,  223,    0,    0,    0,  251,    0,    0,    0,    0,
    0,  256,    0,    0,    0,   85,    0,    0,  265,    0,
    0,    0,  223,    0,    0,    0,    0,  223,    0,    0,
    0,    0,    0,    0,    0,    0,  223,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,   46,   45,   59,   40,   41,   40,   41,   41,
   45,   11,   45,   59,   40,   15,   41,   42,   43,   44,
   45,   46,   47,   41,  123,  123,   12,   27,  123,   40,
  123,   43,   15,   45,   59,   44,   51,   44,   41,   42,
   43,   44,   45,   41,   47,   43,   44,   45,   60,   42,
   62,   51,   59,   39,   47,   44,   56,   60,   40,   62,
  123,   47,   60,   44,   62,   41,   81,   43,   54,   45,
   44,   40,   44,   56,  256,   41,   44,   43,   44,   45,
  262,   81,   44,   41,   46,   43,   44,   45,   44,  256,
  258,  123,   46,   44,   60,  262,   62,   59,    0,   61,
   46,  256,   60,   40,   62,  123,  256,   61,  102,   11,
   44,   25,   45,   15,  100,   61,   47,   41,    0,   40,
  114,  115,  256,   54,   41,   39,   44,   59,   45,   11,
  256,  123,  256,   15,  256,  257,  262,   44,  262,   53,
   44,   40,  128,  129,  130,  131,  132,  133,   44,  257,
  257,   44,  259,   43,   56,   45,   41,   40,   43,  256,
   45,   75,  257,  256,   44,  257,   44,   44,  261,  262,
   40,  270,  270,   41,   56,   41,  176,   44,  257,   45,
   44,   40,   41,  261,  262,  261,  262,  123,  125,  257,
  190,   44,   46,  256,   40,  111,  112,  256,  261,  262,
  257,  257,  257,   45,  125,   59,  256,   61,  123,   40,
  256,   41,  212,  213,  256,  257,  258,  217,  257,  219,
  220,  221,  257,  258,  257,  258,  125,   40,  123,  257,
  272,  256,  257,  277,  266,  267,  268,  272,  238,  272,
  123,  241,  242,  243,  244,   41,  257,  256,  234,  256,
   40,  270,  277,  256,  123,  125,   44,  256,  256,  256,
   41,  273,  274,  275,  276,   41,   40,  256,  268,  256,
  273,  274,  275,  276,  274,  273,  274,  275,  276,  125,
  256,  281,  256,  283,  256,  285,   40,  123,  256,  289,
  256,  205,  123,   41,  256,  295,   41,  297,  256,  299,
  256,  301,  302,  303,  304,  256,  306,  273,  274,  275,
  276,  123,  125,  313,  123,  273,  274,  275,  276,  256,
  257,   40,  256,  260,  257,  258,  263,  264,  265,  266,
  267,  268,  269,  123,  271,  256,  257,   40,  256,  260,
  257,  258,  263,  264,  265,  266,  267,  268,  269,  256,
  271,  125,  256,  123,   40,  272,  123,  256,  257,  123,
  256,  260,   41,  256,  263,  264,  265,  266,  267,  268,
  269,  125,  271,  123,  257,   40,  256,  260,  256,  256,
  263,  264,  265,  266,  267,  268,  269,  257,  271,  256,
  260,  257,  258,  263,  264,  265,  266,  267,  268,  269,
   40,  271,  256,  256,  123,  256,  272,  266,  267,  268,
  123,  257,  256,   40,  260,  257,  258,  263,  264,  265,
  266,  267,  268,  269,  256,  271,  257,  256,  256,  260,
  272,   40,  263,  264,  265,  266,  267,  268,  269,  125,
  271,  256,  256,  123,  257,  256,   40,  260,  256,  256,
  263,  264,  265,  266,  267,  268,  269,  256,  271,  256,
  125,  256,  123,  256,  256,  123,   40,  257,  262,  256,
  260,  262,  256,  263,  264,  265,  266,  267,  268,  269,
  262,  271,   40,  257,  262,  125,  260,  262,  256,  263,
  264,  265,  266,  267,  268,  269,  256,  271,  125,  256,
   41,   40,   23,  257,   45,  256,  260,  256,   41,  263,
  264,  265,  266,  267,  268,  269,  125,  271,  256,    0,
   40,  266,  267,  268,   40,   40,  256,   41,   41,   41,
   11,  125,   41,   41,   15,   11,   -1,   75,  257,   -1,
   -1,  260,   40,   -1,  263,  264,  265,  266,  267,  268,
  269,  125,  271,   -1,  257,   -1,   -1,  260,   40,   -1,
  263,  264,  265,  266,  267,  268,  269,  125,  271,   -1,
   -1,  257,   -1,   45,  260,   56,   40,  263,  264,  265,
  266,  267,  268,  269,   -1,  271,  125,  266,  267,  268,
   -1,   40,  257,   -1,   -1,  260,   40,   -1,  263,  264,
  265,  266,  267,  268,  269,  125,  271,   -1,   -1,  125,
  125,   40,   -1,   -1,   -1,   40,   40,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,  266,  267,  268,  269,
  257,  271,   -1,  260,   40,   40,  263,  264,  265,  266,
  267,  268,  269,  125,  271,   -1,   -1,   -1,  257,   -1,
   40,  260,   -1,   -1,  263,  264,  265,  266,  267,  268,
  269,  125,  271,  257,   -1,   40,  260,   -1,   -1,  263,
  264,  265,  266,  267,  268,  269,  125,  271,   40,   -1,
   -1,  125,   40,  257,   -1,   -1,  260,   -1,   -1,  263,
  264,  265,  266,  267,  268,  269,  125,  271,   40,  257,
  125,  125,  260,   -1,   27,  263,  264,  265,  266,  267,
  268,  269,   -1,  271,   -1,  256,  257,  258,  257,  125,
  125,  260,  261,   -1,  263,  266,  267,  268,   51,   -1,
  269,  272,  271,   -1,   -1,  125,   -1,  257,   -1,   -1,
  260,  257,  257,  263,  260,  260,   -1,  263,  263,  269,
  125,  271,   -1,  269,  269,  271,  271,   -1,   81,  257,
   -1,   -1,  260,  125,   -1,  263,   89,  125,   -1,   -1,
   -1,  269,   -1,  271,   -1,  257,   -1,   -1,  260,   -1,
   -1,  263,   -1,  125,  256,  257,  258,  269,   -1,  271,
   -1,  125,   -1,  257,   -1,   -1,  260,  120,   -1,  263,
  272,   -1,   -1,  125,   -1,  269,   -1,  271,  257,  125,
   -1,  260,   -1,  257,  263,   -1,  260,   -1,   -1,  263,
  269,  144,  271,   -1,   -1,  269,   -1,  271,  257,   -1,
   -1,  260,  257,  257,  263,  260,  260,   -1,  263,  263,
  269,   -1,  271,   -1,  269,  269,  271,  271,   -1,   -1,
   -1,  257,  257,   -1,  260,  260,   -1,  263,  263,   -1,
   -1,   -1,   -1,  269,  269,  271,  271,  257,   -1,   -1,
  260,   -1,   -1,  263,   -1,   -1,   -1,   -1,   -1,  269,
   -1,  271,  257,   -1,   -1,  260,   -1,  190,  263,   -1,
   -1,   -1,   -1,   -1,  269,  257,  271,   -1,  260,  257,
   -1,  263,  260,   -1,   -1,  263,   -1,  269,   -1,  271,
  213,  269,   -1,  271,  217,  257,  219,  220,  260,   -1,
   -1,  263,  256,  257,   -1,   -1,   -1,  269,   -1,  271,
  264,  265,  266,  267,  268,  257,  138,   -1,  140,  242,
   -1,  257,  264,  265,  266,  267,  268,   -1,  264,  265,
  266,  267,  268,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  164,   -1,   -1,   11,   -1,   -1,   -1,   15,
   -1,  274,   -1,   -1,   -1,   -1,   -1,  179,  281,   -1,
  283,   27,  285,  192,   -1,   -1,  289,  196,   -1,   -1,
  199,   -1,  295,  195,   -1,  197,  198,   -1,  200,  208,
  303,   -1,   -1,   -1,   -1,   51,  215,   -1,   -1,   -1,
   56,   -1,   -1,   -1,   -1,  224,   -1,  226,   -1,  228,
   -1,  230,   -1,   -1,   -1,  227,   -1,   -1,   -1,   -1,
   -1,  233,   -1,   -1,   -1,   81,   -1,   -1,  240,   -1,
   -1,   -1,  251,   -1,   -1,   -1,   -1,  256,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  265,
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
"encabezado_funcion : ID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' error",
"encabezado_funcion : VOID '(' ')' '{' cuerpo_de_la_funcion '}' error",
"encabezado_funcion : ID '(' ')' '{' cuerpo_de_la_funcion '}' error",
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
"cuerpo_clase : declaracion_clases error",
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
"sentencias_ejecucion_funcion : ID '(' expr_aritmetic ')'",
"sentencias_ejecucion_funcion : ID '(' ')'",
"sentencias_ejecucion_funcion : ID '(' expr_aritmetic error",
"sentencias_ejecucion_funcion : ID '(' error",
"sentencias_ejecucion_funcion : ID ')' error",
"sentencias_ejecucion_funcion : ID expr_aritmetic ')' error",
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
"sentencias_salida : PRINT ID",
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

//#line 1773 "gramatica.y"

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


public static void copiarIdentificadoresDeClaseHeredada(String nombreClase, String nombreClasePropia){
    if(!TablaDeSimbolos.obtenerSimbolo(nombreClase).getHereda().equals("nadie")){
        String claseHereda = TablaDeSimbolos.obtenerSimbolo(nombreClase).getHereda();
        
        int indiceClase = claseHereda.lastIndexOf('#');
        String aux = claseHereda.substring(0, indiceClase);
        nombreClase = claseHereda.substring(indiceClase+1, claseHereda.length())+"#"+aux;
        Hashtable<String, Simbolo> tabla = TablaDeSimbolos.obtenerTablaDeSimbolos();
        System.out.println("NOMBRECLASE---------------------------------------------------------: " + nombreClase);
        for (String key : tabla.keySet()) {
            Simbolo simbolo = tabla.get(key);
            if(simbolo.getLexema().contains(nombreClase)){
                System.out.println("LEXEMA: " + simbolo.getLexema());

            }
        }
    }
}


public static String nivelDeClaseCorrecto(String nombreClase){
    System.out.println("ENTRE A LA FUNCION CON: " + nombreClase);
    int nivel = 1;
    String claseHereda;
    Simbolo simboloClase = TablaDeSimbolos.obtenerSimboloSinAmbito(nombreClase);
    System.out.println("SIMBOLO: " + simboloClase.ToString());
    claseHereda = simboloClase.getHereda();
    String claseAnt = claseHereda;
    while(!claseHereda.equals("nadie") &&  nivel < 3){
        System.out.println("ENTRE EN EL WHILE");
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
    }else{
        String lexemanegativo = "-" + lexemaSimbolo;
        TablaDeSimbolos.agregarSimbolo(lexemanegativo, Constantes.CTE);
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
//#line 923 "Parser.java"
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
                                            /*ArbolSintactico arbolNuevo = (ArbolSintactico) $1; */
                                             /*System.out.println("La sentencia ejecutable, su nodo padre es: " + arbolNuevo.getLex()+ ". FUERA DEL IF");*/
                                               /* System.out.println("La sentencia ejecutable, su nodo padre es: " + arbolNuevo.getLex() + "DENTRO DEL IF");*/
                                             ArbolSintactico arbAux = (ArbolSintactico) val_peek(0);
                                             buscarErroresEnNodo(arbAux);  
                                             generarArbol((ArbolSintactico) val_peek(0));
                                             /*}*/
                                            }
break;
case 14:
//#line 59 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de variable");}
break;
case 15:
//#line 60 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de funcion");}
break;
case 16:
//#line 61 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de la clase");}
break;
case 17:
//#line 62 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion del objeto de clase");}
break;
case 18:
//#line 65 "gramatica.y"
{
                                                    /*Recorro la lista de identificadores que encontre*/
                                                    for(String s : lista_identificadores){
                                                        /*Si no se encuentra en la tabla agregado con el lexema + ambito entonces lo agrego*/
                                                        if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                            TablaDeSimbolos.setTipo(val_peek(1).sval, s);
                                                            TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                            simbolo.setUso("identificador");
                                                        }else{
                                                            /*doy error por re declaracion del identificador*/
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                    }
                                                    /*borro la lista una vez analizados todos los identificadores*/
                                                    lista_identificadores.clear();
                                                }
break;
case 19:
//#line 83 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el TIPO en la DECLARACION de la variable");}
break;
case 20:
//#line 84 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el identificador en la DECLARACION de la variable");}
break;
case 21:
//#line 87 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION CON PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    String ambitoParametro = ambitoActual;
                                                    ambitoActual = ambitoActual.substring(0, indice);

                                                    System.out.println("EL SIMBOLO DEL PARAMETrO ES: " + val_peek(4).sval + " EL AMBITO: "+ambitoParametro);
                                                    Simbolo simbolParametro = TablaDeSimbolos.obtenerSimbolo(val_peek(4).sval+"#"+ambitoParametro);
                                                    TablaDeSimbolos.obtenerSimbolo(val_peek(6).sval).setParametro(simbolParametro);

                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual(val_peek(6).sval+"#"+ambitoActual, simbolParametro.getLexema())){
                                                        System.out.println("ENTRE AL IF DE EXISTE SIMBOLO"); 
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema(val_peek(6).sval, val_peek(6).sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(6).sval + "#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            /*Simbolo simbolParametro = TablaDeSimbolos.obtenerSimboloSinAmbito($4.sval);*/
                                                            
                                                            /*pruebo guardando el parametro en la tabla de simbolos*/
                                                            /*TablaDeSimbolos.modificarLexema($4.sval,$4.sval+"#"+ambitoParametro);*/
                                                            /*simbolParametro.setUso("identificador");*/
                                                            /*simbolParametro.setTipo($3.sval);*/
                                                            simbolo.setParametro(simbolParametro);
                                                            /*TablaDeSimbolos.borrarSimbolo($4.sval);*/
                                                            agregarArbol(val_peek(6).sval+"#"+ambitoActual);
                                                            /*
                                                            TablaDeSimbolos.modificarLexema($4.sval,$4.sval+"#"+ambitoActual);
                                                            TablaDeSimbolos.obtenerSimbolo($4.sval+"#"+ambitoActual).setUso("identificador");
                                                            TablaDeSimbolos.obtenerSimbolo($4.sval+"#"+ambitoActual).setTipo($3.sval);
                                                            agregarArbol($1.sval+"#"+ambitoActual);
                                                            */
                                                            tieneReturn = false; 
                                                        }else{
                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Falta la sentencia return en la declaracion de funcion";
                                                            erroresSemanticos.add(err);    
                                                        }
                                                    }else{
                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Funcion re declarada en el mismo ambito";
                                                        erroresSemanticos.add(err);
                                                    }
                                                    System.out.println("AMBITO UNA VEZ FINALIZADA LA DECLARACIOND DE FUNCION: " + ambitoActual);   
                                                }
break;
case 22:
//#line 128 "gramatica.y"
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
                                                     System.out.println("AMBITO UNA VEZ FINALIZADA LA DECLARACIOND DE FUNCION: " + ambitoActual);
                                        }
break;
case 23:
//#line 150 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' al final de la funcion");}
break;
case 24:
//#line 151 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
break;
case 25:
//#line 152 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
break;
case 26:
//#line 153 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
break;
case 27:
//#line 155 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' en la declaracion de la funcion");}
break;
case 28:
//#line 156 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
break;
case 29:
//#line 157 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
break;
case 30:
//#line 158 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
break;
case 31:
//#line 162 "gramatica.y"
{
                        Simbolo simbolParametro = TablaDeSimbolos.obtenerSimbolo(val_peek(0).sval);
                                                            
                        /*pruebo guardando el parametro en la tabla de simbolos*/
                        TablaDeSimbolos.modificarLexema(val_peek(0).sval,val_peek(0).sval+"#"+ambitoActual);
                        simbolParametro.setUso("identificador");
                        simbolParametro.setTipo(val_peek(1).sval);
                        simbolParametro.setValorAsignado(true);
                        yyval = val_peek(0);
                        }
break;
case 32:
//#line 173 "gramatica.y"
{ 
                                ambitoActual = ambitoActual+"#"+val_peek(0).sval;
                                yyval = val_peek(0);
                            }
break;
case 33:
//#line 177 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
break;
case 34:
//#line 178 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la palabra VOID en la declaracion de la funcion");}
break;
case 35:
//#line 179 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
break;
case 36:
//#line 180 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la palabra VOID en la declaracion de la funcion");}
break;
case 37:
//#line 183 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");
            yyval = val_peek(0);
            }
break;
case 38:
//#line 186 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");
            yyval = val_peek(0);
            }
break;
case 39:
//#line 189 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");
            yyval = val_peek(0);
            }
break;
case 40:
//#line 194 "gramatica.y"
{ 
                                                lista_identificadores.add(val_peek(0).sval); 
                                              }
break;
case 41:
//#line 197 "gramatica.y"
{ 
                         lista_identificadores.add(val_peek(0).sval);
                        }
break;
case 45:
//#line 207 "gramatica.y"
{ generarArbolFunc((ArbolSintactico) val_peek(0));}
break;
case 51:
//#line 215 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de varaible");}
break;
case 52:
//#line 216 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de funcion");}
break;
case 53:
//#line 217 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de objeto");}
break;
case 54:
//#line 220 "gramatica.y"
{  if(auxVarClases.equals("")){
                                        /*String lexema = $1.sval; //+"#"+ambitoActual;*/

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
case 55:
//#line 239 "gramatica.y"
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
case 56:
//#line 253 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE");
                                                            /*int indice = ambitoActual.lastIndexOf('#');*/
                                                            /*ambitoActual = ambitoActual.substring(0, indice);*/
                                                            
                                                            int indiceLexClase = ambitoActual.lastIndexOf('#');
                                                            String identClase = val_peek(3).sval +"#"+ ambitoActual.substring(0, indiceLexClase);
                                                            
                                                            if((TablaDeSimbolos.obtenerSimbolo(identClase).getUso().equals("")) || (TablaDeSimbolos.obtenerSimbolo(identClase).getClaseAPosterior())){
                                                                /*System.out.println("NOMBRE: " + identClase + " USO: " + TablaDeSimbolos.obtenerSimbolo(identClase).getUso());*/
                                                                /*Si el uso es vacio y/o declaracionAPosterior es true entonces no estoy redeclarando*/
                                                                System.out.println("AUXVARCLASES: " + auxVarClases);
                                                                
                                                                
                                                                if(!auxVarClases.equals("")){
                                                                    String claseHoja = nivelDeClaseCorrecto(auxVarClases); 
                                                                    if(claseHoja.equals("")){
                                                                        /*Quiere decir que no supera en nivel de clase, en cambio retorna el nombre de la clase que lo supera*/
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases);
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                        /*copiarIdentificadoresDeClaseHeredada(auxVarClases);*/
                                                                    }else{
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases); /*agrego igual de la clase que hereda por si luego hay mas clases*/
                                                                        String err = "Linea: "+AnalizadorLexico.getLineaActual()+". Error Semantico: El nivel de herencia es superior a 3 por: " + claseHoja;
                                                                        erroresSemanticos.add(err);
                                                                    }
                                                                }else{
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setHereda("nadie");
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                }

                                                                TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);
                                                            }else{
                                                                System.out.println("ENTRE EN ESTE ELSE: " + identClase + " USO------- " + TablaDeSimbolos.obtenerSimbolo(identClase).getUso());
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: Clase " + val_peek(3).sval+" redeclarada";
                                                                erroresSemanticos.add(err);
                                                            }

                                                            System.out.println("FINAL DE DECLARACION. CLASE: " + identClase + " USO: " + TablaDeSimbolos.obtenerSimbolo(identClase).getUso() + " HEREDA: " + TablaDeSimbolos.obtenerSimbolo(identClase).getHereda());

                                                            auxVarClases = "";
                                                            /*ambitoActual = "global";*/
                                                            int indice = ambitoActual.lastIndexOf('#');
                                                            ambitoActual = ambitoActual.substring(0, indice);
                                                        }
break;
case 57:
//#line 298 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE A POSTERIOR");
                                        TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(0).sval).setClasePosterior(true);
                                        TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(0).sval).setUso("clase");
                                    }
break;
case 58:
//#line 302 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() + "Error sintactico.Falta } al final de la clase");}
break;
case 59:
//#line 303 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta '{' al inicio de la clase");}
break;
case 60:
//#line 304 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta cuerpo de la clase");}
break;
case 61:
//#line 307 "gramatica.y"
{
                        yyval = val_peek(0);
                        /*agrego ambito a la clase*/

                        String lexema = val_peek(0).sval +"#"+ ambitoActual;
                       
                        TablaDeSimbolos.modificarLexema(val_peek(0).sval, lexema);
                        /*System.out.println("LEXEMA A BORRAR: " + $2.sval);*/
                        /*TablaDeSimbolos.borrarSimbolo($2.sval);*/
                        ambitoActual = ambitoActual + "#" + val_peek(0).sval;
                }
break;
case 62:
//#line 318 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta el identificador de la clase");}
break;
case 65:
//#line 324 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico al compilar no permite declarar sentencia ejecutables dentro de una funcion");}
break;
case 66:
//#line 325 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintectico al compilar no permite declarar una clase dentro de otra");}
break;
case 67:
//#line 328 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de OBJETO DE CLASE");
                                                    for(String s : lista_identificadores){
                                                        /*Si no se encuentra en la tabla agregado con el lexema + ambito entonces lo agrego*/
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
                                                    /*
                                                    System.out.println("NOMBRE tIPO CLASE: " + $1.sval);
                                                    if(TablaDeSimbolos.existeSimboloAmbitoActual($1.sval,"nulo") && TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).getUso().equals("clase")){
                                                        for(String s : lista_identificadores){
                                                            //Si no se encuentra en la tabla agregado con el lexema + ambito entonces lo agrego
                                                            if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                                //System.out.println("CLASE: "+ s+"#"+ambitoActual);
                                                                TablaDeSimbolos.setTipo($1.sval, s);
                                                                TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                                Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                                simbolo.setUso("identificador");
                                                            }else{
                                                                //doy error por re declaracion del identificador
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                                erroresSemanticos.add(err);
                                                            }
                                                        }
                                                    }else{
                                                                TablaDeSimbolos.borrarSimbolo($1.sval);
                                                                for(String s : lista_identificadores){
                                                                    TablaDeSimbolos.borrarSimbolo(s);
                                                                }
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: clase no declarada en el mismo ambito";
                                                                erroresSemanticos.add(err);
                                                        
                                                    }
                                                    //borro la lista una vez analizados todos los identificadores
                                                    lista_identificadores.clear(); 
                                                    */      
                                                }
break;
case 68:
//#line 377 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 69:
//#line 378 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 70:
//#line 380 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico. Al compilar no permite objetos de clase separados por ,");}
break;
case 71:
//#line 384 "gramatica.y"
{yyval = val_peek(1); }
break;
case 72:
//#line 385 "gramatica.y"
{yyval = val_peek(1);}
break;
case 73:
//#line 386 "gramatica.y"
{yyval = val_peek(1);}
break;
case 74:
//#line 387 "gramatica.y"
{ yyval = val_peek(1);}
break;
case 75:
//#line 388 "gramatica.y"
{
                                                            System.out.println("POR LO MENOS ENCONTRE ESTO");
                                                            yyval = val_peek(1);}
break;
case 76:
//#line 391 "gramatica.y"
{yyval = val_peek(1);}
break;
case 77:
//#line 392 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la ASIGNACION");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 78:
//#line 395 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia IF");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 79:
//#line 398 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia de SALIDA"); 
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 80:
//#line 401 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia de CONTROL");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 81:
//#line 404 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la ejecucion de FUNCION");
                                                yyval = (ArbolSintactico) nodoError;
                                                }
break;
case 82:
//#line 409 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION CON PARAMETRO");
                                                            
                                                            Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(val_peek(3).sval+"#"+ambitoActual);
                                                            if(simbol != null && simbol.getParametro() != null){
                                                                simbol.setUsada(true); /*SETEO en USADA LA VARIABLE */
                                                               /*System.out.println("AUX: " + auxTipoAsig + " param: " + simbol.getParametro().getTipo()); */
                                                               if(simbol.getParametro().getTipo().equals(auxTipoAsig)){
                                                                    /*En este caso auxTipoAsig tiene el tipo de la expr_aritmetic, entonces hago la comparacion pero al reves*/
                                                                    /*ArbolSintactico arb = (ArbolSintactico) $3;*/
                                                                    /*System.out.println("PARAMETRO: " + parametroDec.ToString());*/
                                                                    
                                                                    
                                                                    
                                                                    NodoHoja id_func = new NodoHoja(val_peek(3).sval+"#"+ambitoActual);
                                                                    yyval = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,(ArbolSintactico)val_peek(1));
                                                               }else{
                                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                                    erroresSemanticos.add(err);
                                                                    yyval = (ArbolSintactico) nodoError;
                                                               }
                                                            }else{
                                                                String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + val_peek(3).sval+" no fue declarada";
                                                                erroresSemanticos.add(err);
                                                                yyval = (ArbolSintactico) nodoError;
                                                            }
                                                            auxTipoAsig ="";
                                                        }
break;
case 83:
//#line 436 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION SIN PARAMETRO");
                                          System.out.println("EL LEXEMA DE LA FUNCION ES: " + val_peek(2).sval+"#"+ambitoActual);
                                          Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(val_peek(2).sval+"#"+ambitoActual);
                                          if(simbol.getId() != -1 && simbol.getParametro() == null){
                                                simbol.setUsada(true);
                                                NodoHoja id_func = new NodoHoja(val_peek(2).sval+"#"+ambitoActual);
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
case 84:
//#line 461 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico .Falta ')' al final de la invocacion");
                                        yyval = (ArbolSintactico) nodoError;
                                        auxTipoAsig ="";
                                        }
break;
case 85:
//#line 465 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' al final de la invocacion");
                                        yyval = (ArbolSintactico) nodoError;
                                        auxTipoAsig ="";
                                        }
break;
case 86:
//#line 469 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' al principio de la invocacion");
                                            yyval = (ArbolSintactico) nodoError;
                                            auxTipoAsig ="";
                                            }
break;
case 87:
//#line 473 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' al principio de la invocacion");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            auxTipoAsig ="";
                                                          }
break;
case 88:
//#line 482 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una ASIGNACION");
                                                        System.out.println("El ambito es: " + ambitoActual);
                                                        String lexema = "";
                                                        String primer = "";
                                                        String ambitoAnt = "";
                                                        /*pruebo poniendo mayor a 0*/
                                                        String ambitoVariable = ambitoActual;
                                                        if(lista_identificadores.size() > 0){
                                                            /*quiere decir que de el lado izquierdo no hubo error y por lo menos tengo un identificador*/
                                                            if(lista_identificadores.size() > 1){
                                                                /*Quiere decir que es un identificador compuesto, por ejemplo clase1.a.num1*/
                                                                /*System.out.print("IDENTIFICADOR COMPUESTO: ---------------------------------------------------- ");*/
                                                                String lexemaAuxFinAmbito =""; /*aux usado para el ambito*/
                                                                Simbolo simboloAnt = null; /*este simbolo es el anterior, por ejemplo en clase1.a.num1 puede ser simboloAnt: clase1*/
                                                                for(int i = 0; i< lista_identificadores.size();i++){
                                                                    /*recorro todos los identidicadores que encontre*/
                                                                    /*System.out.print(lista_identificadores.get(i) + " ");  */
                                                                    if(lexema.equals("")){
                                                                        /*quiere decir que es el primer identificador, por ejemplo, clase 1*/
                                                                        lexema = lista_identificadores.get(i); 
                                                                        simboloAnt = TablaDeSimbolos.obtenerSimbolo(lexema+"#"+ambitoActual);
                                                                        lexemaAuxFinAmbito = TablaDeSimbolos.obtenerSimbolo(lexema+"#"+ambitoActual).getTipo();
                                                                        /*System.out.println("EL PRIMER IF: " + lexemaAuxFinAmbito);*/
                                                                    }else{
                                                                        /*En este simbolo el lexema que le paso es por ejemplo "num1#global#b"*/
                                                                        Simbolo simboloNuevo = null;
                                                                        /*System.out.println("El lexema que uso para buscar el nuevo simbolo: " + lista_identificadores.get(i)+"#"+ambitoActual+"#"+lexemaAuxFinAmbito);*/
                                                                        /*simboloNuevo es el nuevo valor del for*/
                                                                        simboloNuevo = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+lexemaAuxFinAmbito);   /*lexemaAuxFinAmbito es a*/
                                                                        
                                                                        /*if(simboloAnt == null){
                                                                            //quiere decir que es la primera vez o que el nuevo simbolo va a ser un identificador
                                                                            simboloNuevo = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+TablaDeSimbolos.obtenerSimbolo(lexema+"#"+ambitoActual).getTipo());  
                                                                        }else{
                                                                            //aca hago num1#global#el nombre de la clase anterior
                                                                            simboloNuevo = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+lexemaAuxFinAmbito);   //lexemaAuxFinAmbito es a
                                                                        }*/
                                                                        
                                                                        /*System.out.println("EL NUEVO SIMBOLO ES: "+ simboloNuevo.ToString());*/
                                                                        if(simboloNuevo.getId() != -1){   
                                                                            if(simboloNuevo.getUso().equals("clase")){ /*si es una clase*/
                                                                                /*System.out.println("HASTA ACA OBTUVE QUE EL SIMBOLO ANTERIOR: " + simboloAnt.getLexema() + " SIMBOLO ACTUAL: " + simboloNuevo.getLexema());                                                                                */
                                                                                Simbolo simboloTipoAnt = null;
                                                                                if(simboloAnt.getUso().equals("identificador")){
                                                                                    /*quiere decir que el simbolo es un identificador del tipo de una clase*/
                                                                                    simboloTipoAnt = TablaDeSimbolos.obtenerSimbolo(simboloAnt.getTipo()+"#"+ambitoActual); /*aca lo que hago es obtener el identificador de la tabla de la clase que es el identificador anterior*/
                                                                                }else{
                                                                                    /*el propio simbolo es la clase*/
                                                                                    simboloTipoAnt = simboloAnt;
                                                                                }
                                                                                /*System.out.print("EL LEXEMA QUE USO PARA BUSCAR SIMBOLO TIPO ANT: "+ simboloAnt.getTipo()+"#"+ambitoActual);*/
                                                                                /*System.out.println(" SIMBOLOTIPOANT: "+ simboloTipoAnt.ToString());*/
                                                                                if(simboloTipoAnt.getHereda().equals(simboloNuevo.getLexema())){
                                                                                     /*System.out.println("HEREDA CORRECTAMETNE");*/
                                                                                     /*si b#global.hereda() es igual a la clase que sigue*/
                                                                                     lexema = lexema+"."+lista_identificadores.get(i);
                                                                                     lexemaAuxFinAmbito = lista_identificadores.get(i);
                                                                                     simboloAnt = simboloNuevo;
                                                                                }else{
                                                                                    /*si entre en este else quiere decir que el simbolo anterior no heredaba del simbolo actual*/
                                                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase no hereda de "+simboloNuevo.getLexema();
                                                                                    erroresSemanticos.add(err);
                                                                                    yyval = nodoError;
                                                                                    break;
                                                                                }

                                                                            }else{
                                                                                /*quiere decir que el nuevo simbolo es un identidicador*/
                                                                                /*System.out.println("ENTRE ACA PORQUE ES EL FINAL Y ES UN IDENTIFICADOR");*/
                                                                                
                                                                                lexema = lexema+"."+lista_identificadores.get(i)+"#"+ambitoActual; /*Aca hago clase1.num1#global. Asi la voy a guardar en la Tabla de simbolos*/
                                                                                /*System.out.println("EL LEXEMA QUE VOY A AGREGAR ES: " + lexema + " EL TIPO QUE LE VOY A PONER ES EL MISMO QUE: SIMBOLOANT: " +simboloAnt.getLexema() + " O EL DE: "+simboloNuevo.getLexema());*/
                                                                                /*en este punto ya tengo el lexema que va a tener el nuevo simbolo y las caracteristicas que va a tener(Las del simbolo simboloNuevo)*/
                                                                                TablaDeSimbolos.agregarSimbolo(lexema,Constantes.ID);
                                                                                Simbolo nuevo = TablaDeSimbolos.obtenerSimbolo(lexema);
                                                                                nuevo.setTipo(simboloNuevo.getTipo());
                                                                                nuevo.setUso(simboloNuevo.getUso());
                                                                                nuevo.setValor(simboloNuevo.getValor());
                                                                                
                                                                                System.out.println("El nuevo simbolo que agregue es: " + nuevo.ToString());
                                                                            }
                                                                        }else{
                                                                            /*el simbolo no existe*/
                                                                            String err = "Linea: "  + AnalizadorLexico.getLineaActual()+". Error Semantico: variable no declarada";
                                                                            erroresSemanticos.add(err);
                                                                            yyval = nodoError;
                                                                            break;
                                                                        }
                                                                    } 
                                                                }
                                                                
                                                               System.out.println("EL LEXEMA QUE VOY A AGREGAR EN LA TABLA ES: " + lexema);
                                                                


                                                            }else{
                                                                /*Quiere decir que no es un identificador compuesto*/
                                                                System.out.println("ES UN IDENTIFICADOR UNARIO: " + lista_identificadores.get(0)+"#"+ambitoActual);

                                                                lexema = lista_identificadores.get(0)+"#"+ambitoActual;
                                                            }
                                                        }
                                                        /*System.out.print(lexema + " EL AMBITO EN EL QUE ESTA LA VARIABLE: "+ ambitoActual);*/
                                                        System.out.println("");
                                                        
                                                
                                                if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() != -1){
                                                
                                                    /*si existe entonces verifico el lado derecho*/
                                                    Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexema); /*Obtengo el simbolo del lado izquierdo que acabo de crear o que ya existia*/
                                                    
                                                    ArbolSintactico arb = (ArbolSintactico) val_peek(0);
                                                    System.out.println("IZQ: " + simbol.getTipo()+" DER: " + TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getTipo()); 
                                                    
                                                    String aux ="";
                                                    ArbolSintactico arb_aux = arb;
                                                    while(aux.equals("")){
                                                        /*Con este while obtengo el tipo del lado derecho en caso de que el nodo padre sea un+,-,*,/, etc*/
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
                                                    if(simbol.getTipo().equals(aux)){ 
                                                        if(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getId() != -1 && TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getUso().equals("constante")){ 
                                                            simbol.setValor(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getLexema());
                                                        }
                                                        NodoHoja hoja = new NodoHoja(simbol.getLexema());

                                                        /*Valor asignado Correctamente*/
                                                        simbol.setValorAsignado(true);

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
                                                        

                                                if(simboloAuxConversion != null)
                                                    simboloAuxConversion.setTipo(auxConversion);
                                                simboloAuxConversion = null;
                                                auxConversion = "";      
                                                lista_identificadores.clear();
                                                auxTipoAsig = "";
                                            }
break;
case 89:
//#line 640 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la expresion despues del '='");
                                    yyval = (ArbolSintactico) nodoError;
                                    }
break;
case 90:
//#line 645 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 91:
//#line 646 "gramatica.y"
{lista_identificadores.add(val_peek(0).sval);}
break;
case 92:
//#line 649 "gramatica.y"
{yyval = val_peek(0);
                                   
                                    }
break;
case 93:
//#line 654 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF_ELSE");
                        if((ArbolSintactico)val_peek(9) != nodoError){
                            System.out.println("ENTRE AL IF"); 
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
case 94:
//#line 670 "gramatica.y"
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
case 95:
//#line 680 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta END_IF para finalizar la sentencia IF");
                    System.out.println("ENTRE EN EL ERROR DE QUE FALTA ELSE_IF");
                    /*$$ = (ArbolSintactico) null;*/
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 96:
//#line 685 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 97:
//#line 688 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 98:
//#line 691 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF y ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 99:
//#line 694 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 100:
//#line 697 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras ELSE Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 101:
//#line 700 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF, ELSE Y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 102:
//#line 703 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 103:
//#line 706 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 104:
//#line 709 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloques sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 105:
//#line 712 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 106:
//#line 715 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 107:
//#line 718 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 108:
//#line 721 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF y END_IF");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 109:
//#line 724 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 110:
//#line 727 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 111:
//#line 730 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' antes del ELSE");
                    yyval = (ArbolSintactico) nodoError;
                    }
break;
case 112:
//#line 736 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR");
                                                        
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
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
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
                                                        /*$$ = nodoError;*/
                                                        /*$$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  */
                                                        /*auxTipoAsig = "";*/
                                                        }
break;
case 113:
//#line 806 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR");
                                                    /*
                                                    ArbolSintactico nodoIzq = (ArbolSintactico) $1;
                                                    ArbolSintactico nodoDer = (ArbolSintactico) $3;
                                                    System.out.println("IZQ: " + nodoIzq.getLex() + " DER: " + nodoDer.getLex());
                                                    $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                    auxTipoAsig = "";
                                                    */
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
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
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
case 114:
//#line 877 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR O IGUAL");
                                                        /*
                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                        auxTipoAsig = "";
                                                        */
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
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
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
case 115:
//#line 945 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR O IGUAL");
                                                        /*
                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                        auxTipoAsig = "";
                                                        */
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
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
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
case 116:
//#line 1013 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR IGUAL");
                                                            /*
                                                            $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                            auxTipoAsig = "";
                                                            */
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
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
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
case 117:
//#line 1081 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR DISTINTO");
                                                                    /*
                                                                    $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                                    auxTipoAsig = "";
                                                                    */
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
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
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
case 118:
//#line 1151 "gramatica.y"
{
                                                                        yyval=new NodoComun("Sentencia_Dentro_IF", (ArbolSintactico) val_peek(1), (ArbolSintactico) val_peek(0));}
break;
case 119:
//#line 1153 "gramatica.y"
{yyval=val_peek(0);}
break;
case 120:
//#line 1156 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia SALIDA con CADENA");
                                 NodoHoja cadena = new NodoHoja(val_peek(0).sval);
                                 NodoControl nodo = new NodoControl(val_peek(1).sval, (ArbolSintactico) cadena);
                                 yyval = (ArbolSintactico) nodo; 
                                }
break;
case 121:
//#line 1162 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia SALIDA con ID");
                                 NodoHoja id = new NodoHoja(val_peek(0).sval+"#"+ambitoActual);
                                 NodoControl nodo = new NodoControl(val_peek(1).sval, (ArbolSintactico) id);
                                 yyval = (ArbolSintactico) nodo; 
                            }
break;
case 122:
//#line 1169 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una SENTENCIA WHILE");
                                        yyval = val_peek(0);
                                       }
break;
case 123:
//#line 1174 "gramatica.y"
{
                                                            if((ArbolSintactico) val_peek(5) != nodoError){
                                                                /*System.out.println("antes de condicion");*/
                                                                NodoControl condicion = new NodoControl("condicion_while", (ArbolSintactico) val_peek(5));
                                                                /*System.out.println("condicion");*/
                                                                NodoControl cuerpo_while = new NodoControl("cuerpo_while", (ArbolSintactico) val_peek(1));
                                                                /*System.out.println("cuerpo");*/
                                                                yyval = (ArbolSintactico) new NodoComun(val_peek(7).sval, condicion, cuerpo_while);
                                                            }else{
                                                                yyval = val_peek(5);
                                                                /*System.out.println("ERROR");*/
                                                            }
                                                            }
break;
case 124:
//#line 1187 "gramatica.y"
{
                                                                /*ESTO ESTA MAL ME PARECE PORQUE SI EL WHILE NO TIENE CUERPO NO PUEDO CUMPLIR NUNCA LA Condicion*/
                                                                /*Y QUEDA EN BUCLE*/
                                                                NodoControl condicion = new NodoControl("condicion_while", (ArbolSintactico) val_peek(4));
                                                                NodoControl cuerpo_while = new NodoControl("cuerpo_while", null);
                                                                yyval = (ArbolSintactico) new NodoComun(val_peek(6).sval, condicion, cuerpo_while);
                                                                }
break;
case 125:
//#line 1194 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra DO");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 126:
//#line 1197 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra WHILE");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 127:
//#line 1200 "gramatica.y"
{agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                                                            yyval = (ArbolSintactico) nodoError;
                                                            }
break;
case 128:
//#line 1205 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una SUMA");
                                            
                                             ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                                             ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;


                                            if(arbIzq.getLex().equals("TOF")){
                                                /*QUIERE DECIR QUE ES UN NODO TOF*/
                                            	 System.out.println("TOY: "+TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex()).ToString());
                                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                             }else{
                                                
                                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             }
                                             
                                             if(arbDer.getLex().equals("TOF")){
                                                /*QUIERE DECIR QUE ES UN NODO TOF*/
                                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                             }else{
                                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             }
                                            /*
                                             if(TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex()).getId() == -1){
                                                //QUIERE DECIR QUE ES UN NODO TOF
                                                
                                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                             }else{
                                                
                                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             }
                                             
                                             if(TablaDeSimbolos.obtenerSimbolo(arbDer.getLex()).getId() == -1){
                                                //QUIERE DECIR QUE ES UN NODO TOF
                                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                                             }else{
                                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             }
                                             */
                                             System.out.println("EL DERECHO DE LA SUMA ES: " + simbolo1.ToString()+" EL IZQUIERDO DE LA SUMA ES: " + simbolo2.ToString());


                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                    /*Si son dos constantes hago la operacion y retorno la suma de ambos*/
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

                                                        /*Si ambos lados de la operacion estan asignados*/
                                                        /*System.out.println("SIMBOLO1: " + simbolo1.getLexema() + " USO: " +simbolo1.getUso());*/
                                                        /*System.out.println("SIMBOLO2: " + simbolo2.getLexema() + " USO: " +simbolo2.getUso());*/


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
case 129:
//#line 1322 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una RESTA");
                                             /*
                                             ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                                             Simbolo simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             Simbolo simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                 $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3); //Hago $1 ------- * ------- $2
                                             }else{
                                                 $$ = nodoError;
                                             } */
                                            
                                             

                                             ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                                             ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;

                                             if(arbIzq.getLex().equals("TOF")){
                                                /*QUIERE DECIR QUE ES UN NODO TOF*/
                                            	 System.out.println("TOY: "+TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex()).ToString());
                                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                                             }else{
                                                
                                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             }
                                             
                                             if(arbDer.getLex().equals("TOF")){
                                                /*QUIERE DECIR QUE ES UN NODO TOF*/
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
case 130:
//#line 1416 "gramatica.y"
{yyval = val_peek(0); 
                         /*ArbolSintactico arb = (ArbolSintactico) $1;
                         if(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getId() != -1){
                            if(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getValor() != ""){
                                $$ = $1;
                            }else{
                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable " + arb.getLex() + " no inicializada";
                                erroresSemanticos.add(err);
                                $$ = (ArbolSintactico) nodoError;
                            }
                         }*/

                        }
break;
case 131:
//#line 1431 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             

                            ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                            ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                            Simbolo simbolo1;
                            Simbolo simbolo2;
                            if(arbIzq.getLex().equals("TOF")){
                            /*QUIERE DECIR QUE ES UN NODO TOF*/
                                System.out.println("TOY: "+TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex()).ToString());
                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                            }else{
                            
                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                            }
                            
                            if(arbDer.getLex().equals("TOF")){
                            /*QUIERE DECIR QUE ES UN NODO TOF*/
                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                            }else{
                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                            }


                            if(arbIzq != nodoError && arbDer != nodoError){
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
                            }else{
                            yyval = nodoError;
                            }
                            }
break;
case 132:
//#line 1514 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una DIVISION");
                             /*
                             ArbolSintactico arbIzq = (ArbolSintactico) $1;
                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                             Simbolo simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                             Simbolo simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                            if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3); //Hago $1 ------- * ------- $2
                            }else{
                                $$ = nodoError;
                            }
                            */
                            ArbolSintactico arbIzq = (ArbolSintactico) val_peek(2);
                            ArbolSintactico arbDer = (ArbolSintactico) val_peek(0);
                            Simbolo simbolo1;
                            Simbolo simbolo2;

                            if(arbIzq.getLex().equals("TOF")){
                            /*QUIERE DECIR QUE ES UN NODO TOF*/
                                System.out.println("TOY: "+TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex()).ToString());
                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getIzq().getLex());
                            }else{
                            
                                simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                            }
                            
                            if(arbDer.getLex().equals("TOF")){
                            /*QUIERE DECIR QUE ES UN NODO TOF*/
                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getIzq().getLex());
                            }else{
                                simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                            }

                            if(arbIzq != nodoError && arbDer != nodoError){
                            if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                /*Si son dos constantes hago la operacion y retorno la suma de ambos*/
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
case 133:
//#line 1610 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 134:
//#line 1611 "gramatica.y"
{    System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un FACTOR con CONVERSION DE TIPO");
                                    
                                NodoHoja factor = (NodoHoja) val_peek(1);
                                System.out.println("EL LEXEMA DE TOF ES: " + factor.getLex());
                                if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                    auxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo();
                                    simboloAuxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()); 
                                    TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); /*Cambio el tipo de la variable*/
                                    auxTipoAsig = "FLOAT";
                                    NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) val_peek(1));
                                    yyval = (ArbolSintactico) nodoTof;
                                    System.out.println("EL TIPO DEL LEXEMA ES: " + TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo());
                                }else{
                                    String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                    erroresSemanticos.add(err);
                                    yyval = (ArbolSintactico) nodoError;
                                }

                                
                            }
break;
case 135:
//#line 1633 "gramatica.y"
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
                            /*la variable esta siendo usada*/

                            TablaDeSimbolos.obtenerSimbolo(lexema).setUsada(true);

                            /*NodoHoja hoja = new NodoHoja(lexema);*/
                            System.out.println("ANTES DEL MENOS MENOS: " + TablaDeSimbolos.obtenerSimbolo(lexema).getLexema() + " vale: " + TablaDeSimbolos.obtenerSimbolo(lexema).getValor());

                            /*creo la constante con el valor actual del Identificador*/
                            Simbolo simboloConst = new Simbolo(TablaDeSimbolos.obtenerSimbolo(lexema).getValor(), Constantes.CTE);
                            simboloConst.setUso("constante");
                            simboloConst.setTipo(TablaDeSimbolos.obtenerSimbolo(lexema).getTipo());
                            yyval = (ArbolSintactico) new NodoHoja(simboloConst.getLexema());

                            /*realizo la resta de menos menos al identificador*/
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
                            /*agrego el nuevo valor al simbolo*/
                            TablaDeSimbolos.obtenerSimbolo(lexema).setValor(resultadoNuevoValor);
                            /*System.out.println("La variable: " + TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getLexema()+ " tiene el valor: " + TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getValor());*/
                            /*System.out.println("EL nodo Hoja tiene: " + simboloConst.getLexema());*/

                        }else{
                            String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+lexema+ " no declarada";
                            erroresSemanticos.add(err);
                            yyval = (ArbolSintactico) nodoError;
                        }
                        if(auxTipoAsig.equals("")){
                            /*Esto me sirve para resolver la comparacion del parametro de una funcion*/
                            auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(lexema).getTipo();
                        }
                        
                        /*$$ = $1;   */
                        /*TablaDeSimbolos.accionMenosMenos($1.sval);*/

                        }
break;
case 136:
//#line 1696 "gramatica.y"
{System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IDENTIFICADOR ");
            /*System.out.println("EL IDENTIFICADOR ES: " + $1.sval + "#"+ambitoActual);*/
            
            

            /*String lexema = $1.sval + "#" + ambitoActual;*/
            /*IBA ACA*/
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

            /*System.out.println("-----------------------------------------------------------EL AMBITO DESDE EL QUE LLAMO EL ID ES: " + ambitoActual);*/
            /*System.out.println("EL SIMBOLO ES: " + lexema);*/
            /*TablaDeSimbolos.borrarSimbolo($1.sval);*/
            Simbolo aux = TablaDeSimbolos.obtenerSimbolo(lexema);
            if(aux.getId() != -1){
                /*System.out.println("LA VARIABLE QUE ENCONTRE FUE: " + */
                /*la variable fue usada*/
                aux.setUsada(true);
                NodoHoja hoja = new NodoHoja(aux.getLexema());
                /*$$ = (ArbolSintactico) new NodoHoja($1.sval+"#"+ambitoActual);*/
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
            /*$$ = (ArbolSintactico) new NodoHoja($1.sval); //Hago  padre ------- $1*/
            }
break;
case 137:
//#line 1736 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 138:
//#line 1739 "gramatica.y"
{ /*auxDerAsig = auxDerAsig + "." + $3.sval; */
                                  /*lista_identificadores.add($3.sval);*/
                                    auxDerAsig.add(val_peek(0).sval);
                                    yyval = val_peek(0);
                                }
break;
case 139:
//#line 1744 "gramatica.y"
{ /*auxDerAsig = auxDerAsig + "." + $1.sval; */
                /*lista_identificadores.add($1.sval);*/
                auxDerAsig.add(val_peek(0).sval);
                yyval = val_peek(0);
                
                
                }
break;
case 140:
//#line 1753 "gramatica.y"
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
case 141:
//#line 1762 "gramatica.y"
{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una CONSTANTE NEGATIVA");
               constanteNegativa(val_peek(0).sval);
               yyval = (ArbolSintactico) new NodoHoja("-" + val_peek(0).sval); /* padre ------- $1*/
                if(auxTipoAsig.equals("")){
                    auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(val_peek(1).sval).getTipo();
                }
             }
break;
//#line 3080 "Parser.java"
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
