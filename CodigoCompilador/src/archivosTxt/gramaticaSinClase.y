%{
package Compilador;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import GeneracionCodigoIntermedio.ArbolSintactico;
import GeneracionCodigoIntermedio.NodoComun;
import GeneracionCodigoIntermedio.NodoHoja;
import GeneracionCodigoIntermedio.NodoControl;
import java.util.Hashtable;

%}

%token ID CTE CADENA IF ELSE END_IF PRINT CLASS VOID LONG UINT FLOAT WHILE DO RETURN TOF 
        MAYOR_IGUAL MENOR_IGUAL IGUAL_IGUAL 
        EXCLAMACION_EXCLAMACION MENOS_MENOS

%left '+' '-'
%left '*' '/'

%start program

%%
//gramatica

program: programa {System.out.println("ARRANCO EL PROGRAMA");}
       ;

programa: '{' bloque_sentencias_programa '}' {System.out.println("Linea "+ AnalizadorLexico.getLineaActual() + ", Se reconocio el programa ");}
        | '{' bloque_sentencias_programa error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta } al Final");}
        | bloque_sentencias_programa '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta { al Inicio");}
        | '{' '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico al compilar no permite terminar de leer el programa de forma correcta. Falta bloque de sentencias");}
        ;

bloque_sentencias_programa: bloque_sentencias_programa sentencia_programa
        		          | sentencia_programa 
        		          ;

sentencia_programa: sentencias_declarativas {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia DECLARATIVA");}
        	      | sentencias_ejecutables	{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio sentencia EJECUTABLE");
                                            ArbolSintactico arbolNuevo = (ArbolSintactico) $1; 
                                             //System.out.println("La sentencia ejecutable, su nodo padre es: " + arbolNuevo.getLex()+ ". FUERA DEL IF");
                                             //if($1 != nodoError){
                                               // System.out.println("La sentencia ejecutable, su nodo padre es: " + arbolNuevo.getLex() + "DENTRO DEL IF");
                                                generarArbol((ArbolSintactico) $1);
                                             //}
                                            }
				  ;

//declarativas

sentencias_declarativas: declaracion_variables ','
		       		   | declaracion_funciones ','   
				       | declaracion_clases	   ','	   
					   | declaracion_objetos_clase ','
                       | declaracion_variables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de variable");}  
		       		   | declaracion_funciones error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de funcion");}     
				       | declaracion_clases error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion de la clase");}	   	   
					   | declaracion_objetos_clase error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la declaracion del objeto de clase");}   
					   ;

declaracion_variables: tipo lista_de_variables {
                                                    //Recorro la lista de identificadores que encontre
                                                    for(String s : lista_identificadores){
                                                        //Si no se encuentra en la tabla agregado con el lexema + ambito entonces lo agrego
                                                        if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                            TablaDeSimbolos.setTipo($1.sval, s);
                                                            TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                            simbolo.setUso("identificador");
                                                        }else{
                                                            //doy error por re declaracion del identificador
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                    }
                                                    //borro la lista una vez analizados todos los identificadores
                                                    lista_identificadores.clear();
                                                }
				     | lista_de_variables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el TIPO en la DECLARACION de la variable");}
                     | tipo error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el identificador en la DECLARACION de la variable");}
                     ;

declaracion_funciones: encabezado_funcion '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION CON PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    ambitoActual = ambitoActual.substring(0, indice);
                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual($1.sval+"#"+ambitoActual, $4.sval)){ 
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema($1.sval, $1.sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval + "#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            Simbolo simbolParametro = TablaDeSimbolos.obtenerSimboloSinAmbito($4.sval);
                                                            simbolParametro.setUso("identificador");
                                                            simbolParametro.setTipo($3.sval);
                                                            simbolo.setParametro(simbolParametro);
                                                            TablaDeSimbolos.borrarSimbolo($4.sval);
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
                                                }
                     | encabezado_funcion '(' ')' '{' cuerpo_de_la_funcion '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION SIN PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    ambitoActual = ambitoActual.substring(0, indice);
                                                   
                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual($1.sval+"#"+ambitoActual, "nulo")){ 
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema($1.sval, $1.sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval + "#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            agregarArbol($1.sval+"#"+ambitoActual); 
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
                     
		     		 | encabezado_funcion '(' tipo ID ')' '{' cuerpo_de_la_funcion error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' al final de la funcion");}
                     | encabezado_funcion '(' tipo ID ')' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
                     | encabezado_funcion '(' tipo ID '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
                     | encabezado_funcion tipo ID ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
		     		 | encabezado_funcion '(' ')' '{' cuerpo_de_la_funcion error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' en la declaracion de la funcion");}
                     | encabezado_funcion '(' ')' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
                     | encabezado_funcion '(' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
                     | encabezado_funcion ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
                     
                     ;
                     

encabezado_funcion: VOID ID{ 
                                ambitoActual = ambitoActual+"#"+$2.sval;
                                $$ = $2;
                            }
                  | VOID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
                  | ID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la palabra VOID en la declaracion de la funcion");}
                  | VOID '(' ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
                  | ID '(' ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la palabra VOID en la declaracion de la funcion");}
                  ;

tipo: LONG {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo LONG");
            $$ = $1;
            }
	| UINT {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo UINT");
            $$ = $1;
            }
	| FLOAT {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un tipo FLOAT");
            $$ = $1;
            }
	;

lista_de_variables: lista_de_variables ';' ID { 
                                                lista_identificadores.add($3.sval); 
                                              }
				  | ID { 
                         lista_identificadores.add($1.sval);
                        }
                  ;

cuerpo_de_la_funcion: cuerpo_de_la_funcion sentencias_de_funcion 
                    | sentencias_de_funcion
                    ;

sentencias_de_funcion: sentencia_declatariva_especificas 
                     | sentencias_ejecutables { generarArbolFunc((ArbolSintactico) $1);}
                     ;

sentencia_declatariva_especificas: declaracion_variables ','               
                                 | declaracion_funciones ','
                                 | declaracion_objetos_clase ','
                                 | declaracion_clase_hereda
                                 | declaracion_variables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de varaible");}                     
                                 | declaracion_funciones error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de funcion");}
                                 | declaracion_objetos_clase error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de objeto");}
                                 ;

declaracion_clase_hereda: ID ',' {  if(auxVarClases.equals("")){
                                        if(TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).getId() != -1){
                                            auxVarClases = $1.sval;
                                        }else{
                                            String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: No se declaro la clase";
                                            erroresSemanticos.add(err);    
                                            auxVarClases = "";
                                        }
                                    }else{
                                        auxVarClases = auxVarClases+"#"+$1.sval; // concateno las dos clases para demostrar que hay un error
                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: No se permite heredar de mas de una clase.";
                                        erroresSemanticos.add(err); 
                                    }
                                }
                        ;
sentencias_retorno: RETURN {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio RETURN"); 
                            if(!ambitoActual.equals("global")){
                                $$ = (ArbolSintactico) new NodoHoja($1.sval);
                                tieneReturn = true;
                            }else{
                                String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Return fuera del ambito de una funcion";
                                erroresSemanticos.add(err);
                                $$ = nodoError;
                            }
                            }
                  ;



declaracion_clases: encabezado_clase '{' cuerpo_clase '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE");
                                                            //int indice = ambitoActual.lastIndexOf('#');
                                                            //ambitoActual = ambitoActual.substring(0, indice);

                                                            if((TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).getUso().equals("")) || (TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).getClaseAPosterior())){
                                                                //Si el uso es vacio y/o declaracionAPosterior es true entonces no estoy redeclarando
                                                                if(!auxVarClases.equals("")){
                                                                    if(nivelDeClaseCorrecto(auxVarClases)){
                                                                        TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setHereda(auxVarClases);
                                                                        TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setUso("clase");
                                                                    }else{
                                                                        TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setHereda(auxVarClases); //agrego igual de la clase que hereda por si luego hay mas clases
                                                                        String err = "Linea: "+AnalizadorLexico.getLineaActual()+". Error Semantico: El nivel de la clase es superior a 3.";
                                                                        erroresSemanticos.add(err);
                                                                    }
                                                                }else{
                                                                        TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setHereda("nadie");
                                                                        TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setUso("clase");
                                                                }
                                                                TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setClasePosterior(false);
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: Clase " + $1.sval+" redeclarada";
                                                                erroresSemanticos.add(err);
                                                            }
                                                            auxVarClases = "";
                                                            ambitoActual = "global";
                                                          }
					//tema  21
				  | encabezado_clase {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE A POSTERIOR");
                                        TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setClasePosterior(true);
                                        TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setUso("clase");
                                    }
                  | encabezado_clase '{' cuerpo_clase error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() + "Error sintactico.Falta } al final de la clase");}
                  | encabezado_clase cuerpo_clase '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta '{' al inicio de la clase");}
                  | encabezado_clase '{' '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta cuerpo de la clase");}
				  ;

encabezado_clase: CLASS ID {
                        $$ = $2;
                        ambitoActual = $2.sval;
                }
                | CLASS '{' cuerpo_clase '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta el identificador de la clase");}
                ;


cuerpo_clase: cuerpo_clase sentencia_declatariva_especificas
            | sentencia_declatariva_especificas
            | sentencias_ejecutables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico al compilar no permite declarar sentencia ejecutables dentro de una funcion");}
            | declaracion_clases error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintectico al compilar no permite declarar una clase dentro de otra");}
            ;

declaracion_objetos_clase: ID list_objts_clase  {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de OBJETO DE CLASE");
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
                                                }
						 ;

list_objts_clase: list_objts_clase ';' ID {lista_identificadores.add($3.sval);}
				| ID {lista_identificadores.add($1.sval);}
                | list_objts_clase '.' ID error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico. Al compilar no permite objetos de clase separados por .");}
                | list_objts_clase ID error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico. Al compilar no permite objetos de clase separados por ,");}
				;

//ejecutables 
sentencias_ejecutables: sentencia_asignacion ',' {$$ = $1; }
                      | sentencias_IF ',' {$$ = $1;}
                      | sentencias_salida ',' {$$ = $1;}
                      | sentencias_control ',' { $$ = $1;}
                      | sentencias_ejecucion_funcion ',' {$$ = $1;}
                      | sentencias_retorno ',' {$$ = $1;}
                      | sentencia_asignacion error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la ASIGNACION");
                                                $$ = (ArbolSintactico) nodoError;
                                                }
                      | sentencias_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia IF");
                                                $$ = (ArbolSintactico) nodoError;
                                                }
                      | sentencias_salida error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia de SALIDA"); 
                                                $$ = (ArbolSintactico) nodoError;
                                                }
                      | sentencias_control error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la sentencia de CONTROL");
                                                $$ = (ArbolSintactico) nodoError;
                                                }
                      | sentencias_ejecucion_funcion error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la ejecucion de FUNCION");
                                                $$ = (ArbolSintactico) nodoError;
                                                }
                      ;

sentencias_ejecucion_funcion: ID '(' expr_aritmetic ')' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION CON PARAMETRO");
                                                            Simbolo simbol = TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual);
                                                            if(simbol != null && simbol.getParametro() != null){
                                                                simbol.setUsada(true); //SETEO en USADA LA VARIABLE 
                                                               //System.out.println("AUX: " + auxTipoAsig + " param: " + simbol.getParametro().getTipo()); 
                                                               if(simbol.getParametro().getTipo().equals(auxTipoAsig)){
                                                                    //En este caso auxTipoAsig tiene el tipo de la expr_aritmetic, entonces hago la comparacion pero al reves
                                                                    ArbolSintactico arb = (ArbolSintactico) $3;
                                                                    NodoHoja id_func = new NodoHoja($1.sval);
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
                                                            }
                                                            auxTipoAsig ="";
                                                        }
                            | ID '(' ')' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION SIN PARAMETRO");
                                          Simbolo simbol = TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual);
                                          
                                          if(simbol.getId() != -1 && simbol.getParametro() == null){
                                                simbol.setUsada(true);
                                                NodoHoja id_func = new NodoHoja($1.sval);
                                                $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func, null);
                                          }else{
                                                if(simbol.getParametro() != null){
                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Falta el parametro en la funcion";
                                                    erroresSemanticos.add(err);
                                                    $$ = (ArbolSintactico) nodoError;  
                                                }else{
                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: La funcion " + $1.sval+" no fue declarada";
                                                    erroresSemanticos.add(err);
                                                    $$ = (ArbolSintactico) nodoError;
                                                }

                                          }
                                                auxTipoAsig ="";
                                         }
                            | ID '(' expr_aritmetic error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico .Falta ')' al final de la invocacion");
                                        $$ = (ArbolSintactico) nodoError;
                                        auxTipoAsig ="";
                                        }
                            | ID '(' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' al final de la invocacion");
                                        $$ = (ArbolSintactico) nodoError;
                                        auxTipoAsig ="";
                                        }
                            | ID ')' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' al principio de la invocacion");
                                            $$ = (ArbolSintactico) nodoError;
                                            auxTipoAsig ="";
                                            }
                            | ID expr_aritmetic ')' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' al principio de la invocacion");
                                                            $$ = (ArbolSintactico) nodoError;
                                                            auxTipoAsig ="";
                                                          }
                            ;


sentencia_asignacion: id_asig '=' valor_asignacion {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una ASIGNACION");
                                                        String lexema = "";
                                                        String primer = "";
                                                        String ambitoAnt = "";
                                                        //pruebo poniendo mayor a 0
                                                        if(lista_identificadores.size() > 0){
                                                            for(int i = lista_identificadores.size()-1; i>=0;i--){
                                                                if(lexema.equals("")){
                                                                    primer = lista_identificadores.get(i);
                                                                    lexema = lista_identificadores.get(i)+"#"+ambitoActual;
                                                                
                                                                    ambitoAnt = lexema;
                                                                    if(TablaDeSimbolos.obtenerSimbolo(primer).getClaseAPosterior()){
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: La clase fue declarada con posterioridad";
                                                                        erroresSemanticos.add(err);
                                                                        break;
                                                                    }
                                                                }else{
                                                                                                                                
                                                                    if(TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)).getUso().equals("clase")){
                                                                        //System.out.println("identificador act: " + TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)).getLexema());
                                                                        //System.out.println("AMBITO ANTERIOR: " + ambitoAnt);
                                                                        
                                                                        
                                                                        if(!TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)).getClaseAPosterior()){
                                                                            if(TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getUso().equals("clase")){
                                                                                
                                                                                if(TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getHereda().equals(TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)).getLexema())){
                                                                                    //System.out.println("Entre en if porque " + TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getLexema() + " hereda de "+TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getHereda() + " y la de ahora es "+TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)).getLexema());
                                                                                    lexema = lexema + "."+lista_identificadores.get(i);
                                                                                    ambitoAnt = lista_identificadores.get(i);
                                                                                
                                                                                }else{
                                                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual()+". Error Semantico: la clase '"+TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getLexema()+"' no hereda de '"+TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)).getLexema()+"'";
                                                                                    erroresSemanticos.add(err);
                                                                                    break;
                                                                                }
                                                                            }else{
                                                                                    System.out.println("Entre en if porque " + TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getLexema() + " hereda de "+TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getHereda() + " y la de ahora es "+TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)).getLexema());
                                                                                    lexema = lexema + "."+lista_identificadores.get(i);
                                                                                    ambitoAnt = lista_identificadores.get(i);
                                                                                
                                                                            }
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: La clase fue declarada con posterioridad";
                                                                            erroresSemanticos.add(err);
                                                                            break;
                                                                        }
                                                                    }else{
                                                                        if(TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getTipo()).getUso().equals("identificador")){
                                                                            lexema = lexema + "." +lista_identificadores.get(i)+"#"+TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getTipo();
                                                                            ambitoAnt = lista_identificadores.get(i)+"#"+TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getTipo(); 
                                                                        }else{
                                                                            int indiceUltimoPunto = lexema.lastIndexOf('.'); // obtengo el indice del ultimo '.'
                                                                            String tipoClase = lexema.substring(indiceUltimoPunto + 1); //obtengo el nombre de la clase que es igual que el ambito en el que se encuentra la variable
                                                                            lexema = lexema + "."+lista_identificadores.get(i)+"#"+tipoClase;
                                                                            System.out.println("ambitoAnt: "+ambitoAnt);
                                                                            ambitoAnt = lista_identificadores.get(i)+"#"+ambitoAnt;
                                                                        }
                                                                    }
                                                                    
                                                                }
                                                            }
                                                                //existe el simbolo en la clase
                                                                TablaDeSimbolos.agregarSimbolo(lexema,TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getId()); // agrego el simbolo por ejemplo c1.b y le agrego la misma id que b
                                                                TablaDeSimbolos.obtenerSimbolo(lexema).setTipo(TablaDeSimbolos.obtenerSimbolo(ambitoAnt).getTipo()); //Le asigno el tipo de la ultima variable despues del ultimo puntero
                                                                TablaDeSimbolos.obtenerSimbolo(lexema).setUso("identificador"); //Le seteo el uso IDentificador
                                                                ArbolSintactico arb = (ArbolSintactico) $3;
                                                                
                                                                if(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getId() != -1 && TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getUso().equals("constante")){ 
                                                                    
                                                                    TablaDeSimbolos.obtenerSimbolo(lexema).setValor(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getLexema());
                                                                }
                                                        }else{
                                                          primer = lista_identificadores.get(0);
                                                          lexema = primer+ "#" + ambitoActual;;  
                                                        }
                                                        TablaDeSimbolos.borrarSimbolo(primer); // $1.sval
                                                        Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexema);


                                                        if(simbol.getId() != -1){

                                                            ArbolSintactico arb = (ArbolSintactico) $3;
                                                            System.out.println("IZQ: " + simbol.getTipo()+" DER: " + TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getTipo()); 
                                                            String aux ="";
                                                            ArbolSintactico arb_aux = arb;
                                                            while(aux.equals("")){
                                                                //Con este while obtengo el tipo del lado derecho en caso de que el nodo padre sea un+,-,*,/, etc
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
                                                                if(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getId() != -1&&TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getUso().equals("constante")){ 
                                                                simbol.setValor(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getLexema());
                                                                }
                                                                NodoHoja hoja = new NodoHoja(simbol.getLexema());

                                                                //Valor asignado Correctamente
                                                                simbol.setValorAsignado(true);

                                                                $$ = (ArbolSintactico) new NodoComun($2.sval, hoja , (ArbolSintactico) $3);  //Aca hago nodo --- = ---- lo que genere
                                                            }else{
                                                                String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo de la asignacion es incorrecto";
                                                                erroresSemanticos.add(err);
                                                                $$ = (ArbolSintactico) nodoError;    
                                                            }
                                                        }else{
                                                            
                                                            String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+$1.sval+ " no declarada en ambito "+ambitoActual;
                                                            erroresSemanticos.add(err);
                                                            $$ = (ArbolSintactico) nodoError;
                                                        }
                                                lista_identificadores.clear();
                                                auxTipoAsig = "";
                                               }
                    | id_asig '=' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la expresion despues del '='");
                                    $$ = (ArbolSintactico) nodoError;
                                    }
                    ;

id_asig: ID {lista_identificadores.add($1.sval);}
       | ID '.' id_asig {lista_identificadores.add($1.sval);}
       ;

valor_asignacion: expr_aritmetic {$$ = $1;} //Hago que valor_asignacion valga lo que vale expr_aritmetic                            
                ;

sentencias_IF: IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF_ELSE");
                        if((ArbolSintactico)$3 != nodoError){
                            NodoControl then = new NodoControl("then",(ArbolSintactico) $6);
                            NodoControl _else = new NodoControl("else", (ArbolSintactico) $10);
                            NodoComun cuerpo = new NodoComun("cuerpo", then, _else);
                        
                            NodoControl condicion = new NodoControl("Condicion",(ArbolSintactico) $3);
                            $$ = (ArbolSintactico) new NodoComun($1.sval, condicion , cuerpo); 
                        }else{
                            $$ = $3;
                        }
                  
                                                                                                                                    
                    }
                                                                                                                                   
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' END_IF {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IF");
                        if((ArbolSintactico)$3 != nodoError){                                                                     
                            NodoControl then = new NodoControl("then",(ArbolSintactico) $6);
                            NodoControl condicion = new NodoControl("Condicion",(ArbolSintactico) $3);
                            $$ = (ArbolSintactico) new NodoComun($1.sval, condicion , then);                                                                                                                  
                        }else{
                            $$ = $3;
                        }
                    }
             
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta END_IF para finalizar la sentencia IF");
                    System.out.println("ENTRE EN EL ERROR DE QUE FALTA ELSE_IF");
                    //$$ = (ArbolSintactico) null;
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra ELSE");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra IF");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF y ELSE");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF Y END_IF");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras ELSE Y END_IF");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF, ELSE Y END_IF");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '(' condicion_if_while ')' '{'  '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '(' condicion_if_while ')' '{'  '}' ELSE '{' '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloques sentencias ejecutables");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '('  ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}'  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra END_IF");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' END_IF  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra IF");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}'  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabras IF y END_IF");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '(' condicion_if_while ')' '{'  '}' END_IF  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta bloque sentencias ejecutables");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '('  ')' '{' bloque_sentencias_ejecutables '}' END_IF  error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                    $$ = (ArbolSintactico) nodoError;
                    }
             | IF '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables ELSE '{' bloque_sentencias_ejecutables '}' END_IF error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' antes del ELSE");
                    $$ = (ArbolSintactico) nodoError;
                    }
             ;


condicion_if_while: expr_aritmetic '>' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR");
                                                        
                                                        ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            $$ = nodoError;
                                                        }
                                                        
                                                        auxTipoAsig = "";
                                                        //$$ = nodoError;
                                                        //$$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                        //auxTipoAsig = "";
                                                        }
            | expr_aritmetic '<' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR");
                                                    /*
                                                    ArbolSintactico nodoIzq = (ArbolSintactico) $1;
                                                    ArbolSintactico nodoDer = (ArbolSintactico) $3;
                                                    System.out.println("IZQ: " + nodoIzq.getLex() + " DER: " + nodoDer.getLex());
                                                    $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                    auxTipoAsig = "";
                                                    */
                                                    ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            $$ = nodoError;
                                                        }
                                                }
            | expr_aritmetic MAYOR_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR O IGUAL");
                                                        /*
                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                        auxTipoAsig = "";
                                                        */
                                                        ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            $$ = nodoError;
                                                        }
                                                        }
            | expr_aritmetic MENOR_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR O IGUAL");
                                                        /*
                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                        auxTipoAsig = "";
                                                        */
                                                        ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            $$ = nodoError;
                                                        }
                                                        }
            | expr_aritmetic IGUAL_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR IGUAL");
                                                            /*
                                                            $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                            auxTipoAsig = "";
                                                            */
                                                        }
            | expr_aritmetic EXCLAMACION_EXCLAMACION expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR DISTINTO");
                                                                    /*
                                                                    $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);
                                                                    auxTipoAsig = "";
                                                                    */
                                                                    ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    System.out.println("VALOR IZQ: " + sI.getTipo() + " VALOR DER: " + sD.getTipo());
                                                                    if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                         System.out.println("entre al if porque los dos tipos son entenros");
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

                                                                    if(chequearTipos(sD) || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }
                                                        }else{
                                                            
                                                            
                                                            $$ = nodoError;
                                                        }
                                                                    }
            ;

bloque_sentencias_ejecutables: bloque_sentencias_ejecutables sentencias_ejecutables  {
                                                                        $$=new NodoComun("Sentencia_Dentro_IF", (ArbolSintactico) $1, (ArbolSintactico) $2);}
                             | sentencias_ejecutables {$$=$1;}
                             ;

sentencias_salida: PRINT CADENA {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia SALIDA con CADENA");
                                 NodoHoja cadena = new NodoHoja($2.sval);
                                 NodoControl nodo = new NodoControl($1.sval, (ArbolSintactico) cadena);
                                 $$ = (ArbolSintactico) nodo; 
                                }

                  |  PRINT ID {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una sentencia SALIDA con ID");
                                 NodoHoja id = new NodoHoja($2.sval);
                                 NodoControl nodo = new NodoControl($1.sval, (ArbolSintactico) id);
                                 $$ = (ArbolSintactico) nodo; 
                            }
                 ;

sentencias_control: sentencia_while_do {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una SENTENCIA WHILE");
                                        $$ = $1;
                                       }
                  ;

sentencia_while_do: WHILE '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}' {
                                                            if((ArbolSintactico) $3 != nodoError){
                                                                //System.out.println("antes de condicion");
                                                                NodoControl condicion = new NodoControl("condicion_while", (ArbolSintactico) $3);
                                                                //System.out.println("condicion");
                                                                NodoControl cuerpo_while = new NodoControl("cuerpo_while", (ArbolSintactico) $7);
                                                                //System.out.println("cuerpo");
                                                                $$ = (ArbolSintactico) new NodoComun($1.sval, condicion, cuerpo_while);
                                                            }else{
                                                                $$ = $3;
                                                                //System.out.println("ERROR");
                                                            }
                                                            }
                  | WHILE '(' condicion_if_while ')' DO '{' '}'  {
                                                                //ESTO ESTA MAL ME PARECE PORQUE SI EL WHILE NO TIENE CUERPO NO PUEDO CUMPLIR NUNCA LA Condicion
                                                                //Y QUEDA EN BUCLE
                                                                NodoControl condicion = new NodoControl("condicion_while", (ArbolSintactico) $3);
                                                                NodoControl cuerpo_while = new NodoControl("cuerpo_while", null);
                                                                $$ = (ArbolSintactico) new NodoComun($1.sval, condicion, cuerpo_while);
                                                                }
                  | WHILE '(' condicion_if_while ')' '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra DO");
                                                            $$ = (ArbolSintactico) nodoError;
                                                            }
                  | '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta palabra WHILE");
                                                            $$ = (ArbolSintactico) nodoError;
                                                            }
                  | WHILE '('  ')' DO '{' bloque_sentencias_ejecutables '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta condicion");
                                                            $$ = (ArbolSintactico) nodoError;
                                                            }
                  ;

expr_aritmetic: expr_aritmetic '+' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una SUMA");
                                            
                                             ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                                             Simbolo simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             Simbolo simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                    //Si son dos constantes hago la operacion y retorno la suma de ambos
                                                    if(simbolo1.getUso().equals("constante") && simbolo2.getUso().equals("constante")){
                                                            if(simbolo1.getTipo().equals("UINT")){
                                                                int resultado = Integer.parseInt(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 3)) + Integer.parseInt(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 3)); 
                                                                TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_ui",Constantes.CTE);
                                                                Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_ui");
                                                                s.setTipo("UINT");
                                                                s.setUso("constante");
                                                                NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_ui");
                                                                $$ = (ArbolSintactico) resul;
                                                            }else if(simbolo1.getTipo().equals("LONG")){
                                                                long resultado = Long.parseLong(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 2)) + Long.parseLong(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 2)); 
                                                                TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_l",Constantes.CTE);
                                                                Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_l");
                                                                s.setTipo("LONG");
                                                                s.setUso("constante");
                                                                NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_l");
                                                                $$ = (ArbolSintactico) resul;
                                                            }else{
                                                                float resultado = Float.parseFloat(simbolo1.getLexema()) + Float.parseFloat(simbolo2.getLexema()); 
                                                                TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado),Constantes.CTE);
                                                                Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado));
                                                                s.setTipo("FLOAT");
                                                                s.setUso("constante");
                                                                NodoHoja resul = new NodoHoja(String.valueOf(resultado));
                                                                $$ = (ArbolSintactico) resul;
                                                            }
                                                    }else{

                                                        //Si ambos lados de la operacion estan asignados
                                                        //System.out.println("SIMBOLO1: " + simbolo1.getLexema() + " USO: " +simbolo1.getUso());
                                                        //System.out.println("SIMBOLO2: " + simbolo2.getLexema() + " USO: " +simbolo2.getUso());


                                                        if(simbolo1.getUso().equals("identificador")) {
                                                            if(simbolo1.getValorAsignado()){ 
                                                                if(simbolo2.getUso().equals("identificador")){ 
                                                                    if(simbolo2.getValorAsignado()){                                                
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3); //Hago $1 ------- * ------- $2
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = (ArbolSintactico) nodoError;
                                                                    }
                                                                }else{
                                                                    $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3);
                                                                }
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                erroresSemanticos.add(err);
                                                                $$ = (ArbolSintactico) nodoError;
                                                            }
                                                        }else{
                                                            $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3);
                                                        }
                                                        
                                                    }
                                                }else{
                                                    $$ = nodoError;
                                                }
                                             }else{
                                                $$ = nodoError;
                                             } 
                                            }
	    	  | expr_aritmetic '-' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una RESTA");
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

                                             ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                                             Simbolo simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             Simbolo simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                    //Si son dos constantes hago la operacion y retorno la suma de ambos
                                                if(simbolo1.getUso().equals("constante") && simbolo2.getUso().equals("constante")){
                                                        if(simbolo1.getTipo().equals("UINT")){
                                                            int resultado = Integer.parseInt(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 3)) - Integer.parseInt(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 3)); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_ui",Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_ui");
                                                            s.setTipo("UINT");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_ui");
                                                            $$ = (ArbolSintactico) resul;
                                                        }else if(simbolo1.getTipo().equals("LONG")){
                                                            long resultado = Long.parseLong(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 2)) - Long.parseLong(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 2)); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_l",Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_l");
                                                            s.setTipo("LONG");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_l");
                                                            $$ = (ArbolSintactico) resul;
                                                        }else{
                                                            float resultado = Float.parseFloat(simbolo1.getLexema()) - Float.parseFloat(simbolo2.getLexema()); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado),Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado));
                                                            s.setTipo("FLOAT");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado));
                                                            $$ = (ArbolSintactico) resul;
                                                        }
                                                    }else{
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                            if(simbolo1.getValorAsignado()){ 
                                                                if(simbolo2.getUso().equals("identificador")){ 
                                                                    if(simbolo2.getValorAsignado()){                                                
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3); //Hago $1 ------- * ------- $2
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = (ArbolSintactico) nodoError;
                                                                    }
                                                                }else{
                                                                    $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3);
                                                                }
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                erroresSemanticos.add(err);
                                                                $$ = (ArbolSintactico) nodoError;
                                                            }
                                                        }else{
                                                            $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3);
                                                        }   
                                                    }
                                                }else{
                                                    $$ = nodoError;
                                                }
                                             }else{
                                                $$ = nodoError;
                                             }
                                            }
              | termino  {$$ = $1; 
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

                        } //Hago que expr_aritmetic valga lo que vale termino
              ;

termino: termino '*' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             /*ArbolSintactico arbIzq = (ArbolSintactico) $1;
                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                             Simbolo simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                             Simbolo simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                            if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3); //Hago $1 ------- * ------- $2
                            }else{
                                $$ = nodoError;
                            } */

                            ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                                             Simbolo simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             Simbolo simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                    //Si son dos constantes hago la operacion y retorno la suma de ambos
                                                if(simbolo1.getUso().equals("constante") && simbolo2.getUso().equals("constante")){
                                                        if(simbolo1.getTipo().equals("UINT")){
                                                            int resultado = Integer.parseInt(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 3)) * Integer.parseInt(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 3)); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_ui",Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_ui");
                                                            s.setTipo("UINT");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_ui");
                                                            $$ = (ArbolSintactico) resul;
                                                        }else if(simbolo1.getTipo().equals("LONG")){
                                                            long resultado = Long.parseLong(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 2)) * Long.parseLong(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 2)); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_l",Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_l");
                                                            s.setTipo("LONG");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_l");
                                                            $$ = (ArbolSintactico) resul;
                                                        }else{
                                                            float resultado = Float.parseFloat(simbolo1.getLexema()) * Float.parseFloat(simbolo2.getLexema()); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado),Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado));
                                                            s.setTipo("FLOAT");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado));
                                                            $$ = (ArbolSintactico) resul;
                                                        }
                                                    }else{
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                            if(simbolo1.getValorAsignado()){ 
                                                                if(simbolo2.getUso().equals("identificador")){ 
                                                                    if(simbolo2.getValorAsignado()){                                                
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3); //Hago $1 ------- * ------- $2
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = (ArbolSintactico) nodoError;
                                                                    }
                                                                }else{
                                                                    $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3);
                                                                }
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                erroresSemanticos.add(err);
                                                                $$ = (ArbolSintactico) nodoError;
                                                            }
                                                        }else{
                                                            $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3);
                                                        }                                                          
                                                    }
                                                }else{
                                                    $$ = nodoError;
                                                }
                                             }else{
                                                $$ = nodoError;
                                             }
                            }
       | termino '/' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una DIVISION");
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
                            ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                                             Simbolo simbolo1 = TablaDeSimbolos.obtenerSimbolo(arbIzq.getLex());
                                             Simbolo simbolo2 = TablaDeSimbolos.obtenerSimbolo(arbDer.getLex());
                                             if(arbIzq != nodoError && arbDer != nodoError){
                                                if(chequearTipos(simbolo1) && chequearTipos(simbolo2)){
                                                    //Si son dos constantes hago la operacion y retorno la suma de ambos
                                                if(simbolo1.getUso().equals("constante") && simbolo2.getUso().equals("constante")){
                                                        if(simbolo1.getTipo().equals("UINT")){
                                                            int resultado = Integer.parseInt(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 3)) / Integer.parseInt(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 3)); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_ui",Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_ui");
                                                            s.setTipo("UINT");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_ui");
                                                            $$ = (ArbolSintactico) resul;
                                                        }else if(simbolo1.getTipo().equals("LONG")){
                                                            long resultado = Long.parseLong(simbolo1.getLexema().substring(0, simbolo1.getLexema().length() - 2)) / Long.parseLong(simbolo2.getLexema().substring(0, simbolo2.getLexema().length() - 2)); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado)+"_l",Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado)+"_l");
                                                            s.setTipo("LONG");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado)+"_l");
                                                            $$ = (ArbolSintactico) resul;
                                                        }else{
                                                            float resultado = Float.parseFloat(simbolo1.getLexema()) / Float.parseFloat(simbolo2.getLexema()); 
                                                            TablaDeSimbolos.agregarSimbolo(String.valueOf(resultado),Constantes.CTE);
                                                            Simbolo s = TablaDeSimbolos.obtenerSimboloSinAmbito(String.valueOf(resultado));
                                                            s.setTipo("FLOAT");
                                                            s.setUso("constante");
                                                            NodoHoja resul = new NodoHoja(String.valueOf(resultado));
                                                            $$ = (ArbolSintactico) resul;
                                                        }
                                                    }else{
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                            if(simbolo1.getValorAsignado()){ 
                                                                if(simbolo2.getUso().equals("identificador")){ 
                                                                    if(simbolo2.getValorAsignado()){                                                
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3); //Hago $1 ------- * ------- $2
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = (ArbolSintactico) nodoError;
                                                                    }
                                                                }else{
                                                                    $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3);
                                                                }
                                                            }else{
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                erroresSemanticos.add(err);
                                                                $$ = (ArbolSintactico) nodoError;
                                                            }
                                                        }else{
                                                            $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,(ArbolSintactico)$3);
                                                        }                                                            
                                                    }
                                                }else{
                                                    $$ = nodoError;
                                                }
                                             }else{
                                                $$ = nodoError;
                                             }
                            


                             
                        }
       | factor  { $$ = $1; }  //Aca hago que termino valga lo que venga de factor
       | TOF '(' factor ')' {
                                NodoHoja factor = (NodoHoja) $3;
                                if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                    TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); //Cambio el tipo de la variable
                                    System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un FACTOR con CONVERSION DE TIPO");
                                    NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) $3);
                                    $$ = (ArbolSintactico) nodoTof;
                                }else{
                                    String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                    erroresSemanticos.add(err);
                                    $$ = (ArbolSintactico) nodoError;
                                }
                                
                            }
       ;

factor: ID MENOS_MENOS {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador con MENOS_MENOS");
                        String lexema = $1.sval + "#" + ambitoActual;
                        TablaDeSimbolos.borrarSimbolo($1.sval);
                        if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() != -1){
                            //la variable esta siendo usada

                            TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).setUsada(true);

                            //NodoHoja hoja = new NodoHoja($1.sval+"#"+ambitoActual);
                            System.out.println("ANTES DEL MENOS MENOS: " + TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getLexema() + " vale: " + TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getValor());

                            //creo la constante con el valor actual del Identificador
                            Simbolo simboloConst = new Simbolo(TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getValor(), Constantes.CTE);
                            simboloConst.setUso("constante");
                            simboloConst.setTipo(TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getTipo());
                            $$ = (ArbolSintactico) new NodoHoja(simboloConst.getLexema());

                            //realizo la resta de menos menos al identificador
                            String resultadoNuevoValor = "";
                            String lexema2 = TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getValor();
                                
                            switch(TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getTipo()){
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
                            //agrego el nuevo valor al simbolo
                            TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).setValor(resultadoNuevoValor);
                            System.out.println("La variable: " + TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getLexema()+ " tiene el valor: " + TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).getValor());
                            System.out.println("EL nodo Hoja tiene: " + simboloConst.getLexema());

                        }else{
                            String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+$1.sval+ " no declarada";
                            erroresSemanticos.add(err);
                            $$ = (ArbolSintactico) nodoError;
                        }
                        if(auxTipoAsig.equals("")){
                            //Esto me sirve para resolver la comparacion del parametro de una funcion
                            auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval+"#"+ambitoActual).getTipo();
                        }
                        
                        //$$ = $1;   
                        //TablaDeSimbolos.accionMenosMenos($1.sval);

                        } 
      | ID {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IDENTIFICADOR ");
            String lexema = $1.sval + "#" + ambitoActual;
            TablaDeSimbolos.borrarSimbolo($1.sval);
            if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() != -1){
                //la variable fue usada
                TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual).setUsada(true);
                NodoHoja hoja = new NodoHoja($1.sval+"#"+ambitoActual);
                $$ = (ArbolSintactico) new NodoHoja($1.sval+"#"+ambitoActual);
            }else{
                String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+$1.sval+ " no declarada";
                erroresSemanticos.add(err);
                $$ = (ArbolSintactico) nodoError;
            }
            if(auxTipoAsig.equals("")){
                //Esto me sirve para resolver la comparacion del parametro de una funcion
                auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval+"#"+ambitoActual).getTipo();
            }
            //$$ = (ArbolSintactico) new NodoHoja($1.sval); //Hago  padre ------- $1
            }
      | const { $$ = $1;} // Aca hago que factor valga lo que vale const
      ;

const: CTE {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una CONSTANTE POSITIVA");
            if (!$1.sval.contains(String.valueOf(".")))
                constantePositiva($1.sval);
            $$ = (ArbolSintactico) new NodoHoja($1.sval); // padre ------- $1
            if(auxTipoAsig.equals("")){
                //Esto me sirve para resolver la comparacion del parametro de una funcion
                auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).getTipo();
            }
            }
     | '-'CTE{System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una CONSTANTE NEGATIVA");
               constanteNegativa($2.sval);
               $$ = (ArbolSintactico) new NodoHoja("-" + $2.sval); // padre ------- $1
                if(auxTipoAsig.equals("")){
                    auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).getTipo();
                }
             }
     ;                   

//fin gramatica
%%

//funciones

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

public static boolean nivelDeClaseCorrecto(String nombreClase){
    int nivel = 1;
    String claseHereda;
    Simbolo simboloClase = TablaDeSimbolos.obtenerSimboloSinAmbito(nombreClase);
    claseHereda = simboloClase.getHereda();
    while(!claseHereda.equals("nadie")){
        nivel = nivel+1;
        simboloClase = TablaDeSimbolos.obtenerSimboloSinAmbito(claseHereda);
        claseHereda = simboloClase.getHereda();
    }
    if(nivel < 3){
        return true;
    }else{
        return false;
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
    lista_func.clear();
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