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
                                             ArbolSintactico arbAux = (ArbolSintactico) $1;
                                             buscarErroresEnNodo(arbAux);  
                                             generarArbol((ArbolSintactico) $1);
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
                                                    for(String s : lista_identificadores){
                                                        System.out.println("LEXEMA: " + s);
                                                        if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                            TablaDeSimbolos.setTipo($1.sval, s);
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
				     | lista_de_variables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el TIPO en la DECLARACION de la variable");}
                     | tipo error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta el identificador en la DECLARACION de la variable");}
                     ;

declaracion_funciones: encabezado_funcion '(' parametro_fun ')' '{' cuerpo_de_la_funcion '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION CON PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    String ambitoParametro = ambitoActual;
                                                    ambitoActual = ambitoActual.substring(0, indice);

                                                    Simbolo simbolParametro = TablaDeSimbolos.obtenerSimbolo($3.sval+"#"+ambitoParametro);
                                                    TablaDeSimbolos.obtenerSimbolo($1.sval).setParametro(simbolParametro);

                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual($1.sval+"#"+ambitoActual, simbolParametro.getLexema())){
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema($1.sval, $1.sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval + "#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            simbolo.setParametro(simbolParametro);
                                                            agregarArbol($1.sval+"#"+ambitoActual);
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
                     | encabezado_funcion '(' parametro_fun ')' '{' cuerpo_de_la_funcion error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' al final de la funcion");}
                     | encabezado_funcion '(' parametro_fun ')' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
                     | encabezado_funcion '(' parametro_fun '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
                     | encabezado_funcion parametro_fun ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
		     		 
                     | encabezado_funcion '(' ')' '{' cuerpo_de_la_funcion error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '}' en la declaracion de la funcion");}
                     | encabezado_funcion '(' ')' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '{' en la declaracion de la funcion");}
                     | encabezado_funcion '(' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ')' en la declaracion de la funcion");}
                     | encabezado_funcion ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta '(' en la declaracion de la funcion");}
                     ;
                     
parametro_fun: tipo ID {
                        
                        Simbolo simbolParametro = TablaDeSimbolos.obtenerSimbolo($2.sval);
                        TablaDeSimbolos.modificarLexema($2.sval,$2.sval+"#"+ambitoActual);
                        simbolParametro.setUso("identificador");
                        simbolParametro.setTipo($1.sval);
                        simbolParametro.setValorAsignado(true);
                        $$ = $2;
                        }
            ;                     

encabezado_funcion: VOID ID{ 
                                ambitoActual = ambitoActual+"#"+$2.sval;
                                $$ = $2;
                            }
                  
                  | VOID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
                  //| ID '(' tipo ID ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la palabra VOID en la declaracion de la funcion");}
                  | VOID '(' ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta nombre en la declaracion de la funcion");}
                  //| ID '(' ')' '{' cuerpo_de_la_funcion '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la palabra VOID en la declaracion de la funcion");}
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
                                 | declaracion_clase_hereda ','
                                 | declaracion_clases ','
                                 | declaracion_variables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de varaible");}                     
                                 | declaracion_funciones error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de funcion");}
                                 | declaracion_objetos_clase error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta ',' al final de la DECLARACION de objeto");}
                                 ;

declaracion_clase_hereda: ID {  if(auxVarClases.equals("")){

                                        int indice = ambitoActual.lastIndexOf('#');
                                        String lexema = $1.sval+"#"+ambitoActual.substring(0, indice);
                                        if(TablaDeSimbolos.obtenerSimbolo($1.sval).getId() != -1){
                                            auxVarClases = lexema;
                                        }else{
                                            String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: No se declaro la clase " + lexema;
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
                                                            
                                                            int indiceLexClase = ambitoActual.lastIndexOf('#');
                                                            String identClase = $1.sval +"#"+ ambitoActual.substring(0, indiceLexClase);
                                                            
                                                            if((TablaDeSimbolos.obtenerSimbolo(identClase).getUso().equals("")) || (TablaDeSimbolos.obtenerSimbolo(identClase).getClaseAPosterior())){
                                                               if(!auxVarClases.equals("")){
                                                                    String claseHereda = nivelDeClaseCorrecto(auxVarClases); 
                                                                    if(claseHereda.equals("")){
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases);
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);
                                                                    }else{
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases); //agrego igual de la clase que hereda por si luego hay mas clases
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
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: Clase " + $1.sval+" redeclarada";
                                                                erroresSemanticos.add(err);
                                                            }

                                                            
                                                            auxVarClases = "";
                                                            int indice = ambitoActual.lastIndexOf('#');
                                                            ambitoActual = ambitoActual.substring(0, indice);
                                                        }
				  | encabezado_clase {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de CLASE A POSTERIOR");
                                        int indiceLexClase = ambitoActual.lastIndexOf('#');
                                        String identClase = $1.sval +"#"+ ambitoActual.substring(0, indiceLexClase);
                                        if((TablaDeSimbolos.obtenerSimbolo(identClase).getUso().equals(""))){                    
                                            TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(true);
                                            TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                        }
                                        auxVarClases = "";
                                        int indice = ambitoActual.lastIndexOf('#');
                                        ambitoActual = ambitoActual.substring(0, indice);
                                        //TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setClasePosterior(true);
                                        //TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setUso("clase");
                                    }
                  | encabezado_clase '{' cuerpo_clase error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() + ". Error sintactico.Falta } al final de la clase");}
                  | encabezado_clase cuerpo_clase '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta '{' al inicio de la clase");}
                  | encabezado_clase '{' '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta cuerpo de la clase");}
				  ;

encabezado_clase: CLASS ID {
                        $$ = $2;

                        String lexema = $2.sval +"#"+ ambitoActual;
                       
                        TablaDeSimbolos.modificarLexema($2.sval, lexema);
                        ambitoActual = ambitoActual + "#" + $2.sval;
                }
                | CLASS '{' cuerpo_clase '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta el identificador de la clase");}
                ;


cuerpo_clase: cuerpo_clase sentencia_declatariva_especificas
            | sentencia_declatariva_especificas
            | sentencias_ejecutables error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico al compilar no permite declarar sentencia ejecutables dentro de una funcion");}
            ;

declaracion_objetos_clase: ID list_objts_clase  {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DECLARACION de OBJETO DE CLASE");
                                                    for(String s : lista_identificadores){
                                                        if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                            TablaDeSimbolos.setTipo($1.sval, s);
                                                            TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                            simbolo.setUso("identificador");
                                                            TablaDeSimbolos.borrarSimbolo(s);
                                                            System.out.println("LOS OBJETOS QUE VOY A CREAR SON: ");
                                                            Hashtable<Simbolo, String> atributos = crearAtributosClase($1.sval, s);
                                                            for (Simbolo simbol : atributos.keySet()){
                                                                //int ultimoIndice = simbol.getLexema().lastIndexOf("#");
                                                                String nombreObjt = atributos.get(simbol);
                                                                TablaDeSimbolos.agregarSimbolo(nombreObjt, simbol.getId()); // Creo le nuevo simbolo en la tabla
                                                                Simbolo simboloNuevo = TablaDeSimbolos.obtenerSimbolo(nombreObjt); // Obtengo el simbolo que acabo de crear
                                                                
                                                                //seteo los atributos igual que el simbolo original
                                                                simboloNuevo.setTipo(simbol.getTipo());
                                                                simboloNuevo.setValor(simbol.getValor());
                                                                simboloNuevo.setHereda(simbol.getHereda());
                                                                simboloNuevo.setUso(simbol.getUso());
                                                                simboloNuevo.setUsada(true);
                                                                simboloNuevo.setValorAsignado(true);
                                                                if(simbol.getUso().equals("funcion")){
                                                                    simboloNuevo.setParametro(simbol.getParametro());
                                                                }
                                                                //System.out.println(simboloNuevo.ToString());
                                                                
                                                                //Le agrego al simbolo original el nombre que va a tener el atributode la clase
                                                                System.out.println("El simbolo viejo es: "+ simbol.getLexema() + " le voy a agregar: " + simboloNuevo.getLexema());
                                                                simbol.agregarNombreMetodo(simboloNuevo.getLexema());
                                                            }
                                                        }else{
                                                            //doy error por re declaracion del identificador
                                                            String err = "Linea " + AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable re declarada en el mismo ambito";
                                                            erroresSemanticos.add(err);
                                                        }
                                                    }
                                                    lista_identificadores.clear();   
                                                }
						 ;

list_objts_clase: list_objts_clase ';' ID {lista_identificadores.add($3.sval);}
				| ID {lista_identificadores.add($1.sval); System.out.println("objts");}
                | list_objts_clase ID error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +"Error sintactico. Al compilar no permite objetos de clase separados por ,");}
				;

//ejecutables 
sentencias_ejecutables: sentencia_asignacion ',' {$$ = $1; }
                      | sentencias_IF ',' {$$ = $1;}
                      | sentencias_salida ',' {$$ = $1;}
                      | sentencias_control ',' { $$ = $1;}
                      | sentencias_ejecucion_funcion ','{$$ = $1;}
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

sentencias_ejecucion_funcion: id_asig '(' expr_aritmetic ')' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION CON PARAMETRO");
                                    String lexemaIdentificador = "";
                                    String idAnterior = "";
                                    String nombreObjtClase = "";
                                    if(lista_identificadores.size() == 1){
                                        // es una funcion
                                        lexemaIdentificador = lista_identificadores.get(0)+"#"+ambitoActual;
                                    }else if(lista_identificadores.size() == 2){
                                        // es un metodo de la misma clase
                                        // El nombre del objto de la clase esta en la posicion 0 de la lista 
                                        nombreObjtClase = lista_identificadores.get(0);
                                        // Armo el lexema de la funcion
                                        for(int i = lista_identificadores.size()-1; i >= 0;i--){
                                            if(lexemaIdentificador.equals("")){
                                                lexemaIdentificador = lista_identificadores.get(i) + "#"+ambitoActual;
                                            }else{
                                                String tipoObjtClase = TablaDeSimbolos.obtenerSimbolo(nombreObjtClase+"#"+ambitoActual).getTipo();
                                                lexemaIdentificador = lexemaIdentificador +"#"+tipoObjtClase;
                                            }
                                        }
                                    }else{
                                        // es un metodo de una clase que hereda
                                        for(int i = lista_identificadores.size()-1; i >= 0;i--){
                                            System.out.println("EL LEXEMA DE LA FUNCION HASTA AHORA ES: " + lista_identificadores.get(i));    
                                            if(lexemaIdentificador.equals("")){
                                                lexemaIdentificador = lista_identificadores.get(i)+"#"+ambitoActual;
                                            }else{
                                                if(i == (lista_identificadores.size()-2)){
                                                    lexemaIdentificador = lexemaIdentificador+"#"+lista_identificadores.get(i); // agrego el ambito final
                                                    idAnterior = lista_identificadores.get(i)+"#"+ambitoActual;
                                                }else{
                                                    String aux ="";
                                                    if(TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getUso().equals("clase")){
                                                        // es una clase
                                                        aux  = lista_identificadores.get(i)+"#"+ambitoActual;
                                                    }else{
                                                        // es el nombre de un objeto de clase
                                                        nombreObjtClase = lista_identificadores.get(i);
                                                        aux = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo()+"#"+ambitoActual;
                                                    }
                                                    if(TablaDeSimbolos.obtenerSimbolo(aux).getHereda().equals(idAnterior)){
                                                        idAnterior = aux;
                                                    }else{
                                                        String err = "Linea: "+AnalizadorLexico.getLineaActual()+". Error Semantico: la clase " + aux + " no hereda de " + idAnterior;
                                                        erroresSemanticos.add(err);
                                                        lexemaIdentificador = "";
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    
                                    if(lista_identificadores.size() > 1){
                                        Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificador);
                                        NodoControl arbolFuncion = null;
                                        // obtengo el arbol de la funcion
                                        for(NodoControl a : lista_func){
                                            if(a.getLex().equals(lexemaIdentificador)){
                                                arbolFuncion = a;
                                            }
                                        }

                                        /*
                                        if(arbolFuncion != null){
                                            System.out.println("EL ARBOL DE LA FUNCION: " + arbolFuncion.getLex());
                                        } */

                                    
                                        NodoControl nuevoArbol= (NodoControl) crearMetodoDeClase(nombreObjtClase, arbolFuncion);
                                        // System.out.println("El arbol de la funcion que copie es: " + nuevoArbol.getLex()); 
                                        // nuevoArbol.recorrerArbol("-");
                                        agregarMetodoLista(nuevoArbol);
                                        
                                        if(simbol != null && simbol.getParametro() != null){
                                            simbol.setUsada(true); //SETEO en USADA LA VARIABLE 
                                            
                                            ArbolSintactico param = (ArbolSintactico) $3;
                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                            boolean tieneTof = false;
                                            if(param.getLex().equals("TOF")){
                                                tieneTofd = true;
                                                if(param.getIzq().getLex().contains("--")){
                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
                                                    sI = TablaDeSimbolos.obtenerSimbolo(param.getIzq().getLex().substring(0,param.getIzq().getLex().length()-2));    
                                                }else{
                                                    sI = TablaDeSimbolos.obtenerSimbolo(param.getIzq().getLex());
                                                }
                                            }else{
                                                if(param.getLex().contains("--")){
                                                    sI = TablaDeSimbolos.obtenerSimbolo(param.getLex().substring(0,param.getLex().length()-2));
                                                }else{
                                                    sI = TablaDeSimbolos.obtenerSimbolo(param.getLex());
                                                }
                                            }
                                            
                                            if(simbol.getParametro().getTipo().equals(sI.getTipo()) || tieneTof){
                                                NodoHoja id_func = new NodoHoja(nuevoArbol.getLex());
                                                $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,param);
                                            }else{
                                                String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                erroresSemanticos.add(err);
                                                $$ = (ArbolSintactico) nodoError;
                                            }
                                        }else{
                                            String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + nuevoArbol.getLex() +" no fue declarada";
                                            erroresSemanticos.add(err);
                                            $$ = (ArbolSintactico) nodoError;
                                        }
                                    }else{
                                        Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificador);
                                        if(simbol != null && simbol.getParametro() != null){
                                            simbol.setUsada(true); //SETEO en USADA LA VARIABLE 
                                            
                                            ArbolSintactico param = (ArbolSintactico) $3;
                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                            boolean tieneToF = false;
                                            if(param.getLex().equals("TOF")){
                                                tieneToF = true;
                                                if(param.getIzq().getLex().contains("--")){
                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
                                                    sI = TablaDeSimbolos.obtenerSimbolo(param.getIzq().getLex().substring(0,param.getIzq().getLex().length()-2));    
                                                }else{
                                                    sI = TablaDeSimbolos.obtenerSimbolo(param.getIzq().getLex());
                                                }
                                            }else{
                                                if(param.getLex().contains("--")){
                                                    sI = TablaDeSimbolos.obtenerSimbolo(param.getLex().substring(0,param.getLex().length()-2));
                                                }else{
                                                    sI = TablaDeSimbolos.obtenerSimbolo(param.getLex());
                                                }
                                            }
                                            
                                            if(simbol.getParametro().getTipo().equals(sI.getTipo()) || tieneToF){
                                                NodoHoja id_func = new NodoHoja(lexemaIdentificador);
                                                $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,param);
                                            }else{
                                                String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                erroresSemanticos.add(err);
                                                $$ = (ArbolSintactico) nodoError;
                                            }
                                        }else{
                                            String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lexemaIdentificador+" no fue declarada";
                                            erroresSemanticos.add(err);
                                            $$ = (ArbolSintactico) nodoError;
                                        }
                                    }
                                    auxTipoAsig ="";
                                    // System.out.println("el objeto de clase es: " + nombreObjtClase);
                                    // System.out.println("la funcion que encontre es: "+TablaDeSimbolos.obtenerSimbolo(lexemaIdentificador).ToString());
                                    lista_identificadores.clear();
                            }
                            
                            | id_asig '(' ')' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION SIN PARAMETRO");
                                                String lexemaIdentificador = "";
                                                String idAnterior = "";
                                                String nombreObjtClase = "";
                                                if(lista_identificadores.size() == 1){
                                                    // es una funcion
                                                    lexemaIdentificador = lista_identificadores.get(0)+"#"+ambitoActual;
                                                }else if(lista_identificadores.size() == 2){
                                                    // es un metodo de la misma clase
                                                    // El nombre del objto de la clase esta en la posicion 0 de la lista 
                                                    nombreObjtClase = lista_identificadores.get(0);
                                                    // Armo el lexema de la funcion
                                                    for(int i = lista_identificadores.size()-1; i >= 0;i--){
                                                        if(lexemaIdentificador.equals("")){
                                                            lexemaIdentificador = lista_identificadores.get(i) + "#"+ambitoActual;
                                                        }else{
                                                            String tipoObjtClase = TablaDeSimbolos.obtenerSimbolo(nombreObjtClase+"#"+ambitoActual).getTipo();
                                                            lexemaIdentificador = lexemaIdentificador +"#"+tipoObjtClase;
                                                        }
                                                    }
                                                }else{
                                                    // es un metodo de una clase que hereda
                                                    /*
                                                    for(int i = lista_identificadores.size()-1; i >= 0;i--){
                                                        System.out.println("EL LEXEMA DE LA FUNCION HASTA AHORA ES: " + lista_identificadores.get(i));    
                                                        if(lexemaIdentificador.equals("")){
                                                            lexemaIdentificador = lista_identificadores.get(i)+"#"+ambitoActual;
                                                        }else{
                                                            if(i == (lista_identificadores.size()-2)){
                                                                lexemaIdentificador = lexemaIdentificador+"#"+lista_identificadores.get(i); // agrego el ambito final
                                                                idAnterior = lista_identificadores.get(i)+"#"+ambitoActual;
                                                            }else{
                                                                String aux ="";
                                                                if(TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getUso().equals("clase")){
                                                                    // es una clase
                                                                    aux  = lista_identificadores.get(i)+"#"+ambitoActual;
                                                                }else{
                                                                    // es el nombre de un objeto de clase
                                                                    nombreObjtClase = lista_identificadores.get(i);
                                                                    aux = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo()+"#"+ambitoActual;
                                                                }
                                                                if(TablaDeSimbolos.obtenerSimbolo(aux).getHereda().equals(idAnterior)){
                                                                    idAnterior = aux;
                                                                }else{
                                                                    String err = "Linea: "+AnalizadorLexico.getLineaActual()+". Error Semantico: la clase " + aux + " no hereda de " + idAnterior;
                                                                    erroresSemanticos.add(err);
                                                                    lexemaIdentificador = "";
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    } */
                                                    for(int i = lista_identificadores.size()-1; i >= 0;i--){
                                                        if(lexemaIdentificador.equals("")){
                                                            lexemaIdentificador = lista_identificadores.get(i)+"#"+ambitoActual;
                                                            idAnterior = lista_identificadores.get(i);
                                                        }else{
                                                            Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual);
                                                            // System.out.println("EL SIMBOLO DE LA CLASE "+lista_identificadores.get(i)+"#"+ambitoActual +"ES: " + simbol.getLexema());
                                                            System.out.println("El simbolo anterior es: " + idAnterior + " el simbolo nuevo es: " + simbol.getLexema());
                                                            if(simbol.getId() != -1){
                                                                if(TablaDeSimbolos.obtenerSimbolo(idAnterior+"#"+ambitoActual+"#"+lista_identificadores.get(i)).getUso().equals("funcion")){
                                                                    if(TablaDeSimbolos.obtenerSimbolo(simbol.getTipo()+"#"+ambitoActual).getUso().equals("clase")){
                                                                        //quiere decir que el simbolo es el primer ident, es decir el objeto de clase. Por eso uso el tipo de ese objeto
                                                                        lexemaIdentificador = lexemaIdentificador + "#" + simbol.getTipo();
                                                                        idAnterior = simbol.getTipo();
                                                                    }else{
                                                                        //quiere decir que tengo mas clases en el identificador
                                                                        lexemaIdentificador = lexemaIdentificador +"#"+ lista_identificadores.get(i);
                                                                        idAnterior = lista_identificadores.get(i);
                                                                    }

                                                                }else{
                                                                    if(TablaDeSimbolos.obtenerSimbolo(simbol.getLexema()).getHereda().equals(idAnterior+"#"+ambitoActual) || TablaDeSimbolos.obtenerSimbolo(simbol.getTipo()+"#"+ambitoActual).getHereda().equals(idAnterior+"#"+ambitoActual)){
                                                                        // quiere decir que la clase hereda de la otra y que ambos lex son el lexema
                                                                        // o que el lexema actual es el nombre de un objeto clase y tengo que usar su tipo para comprobar si hereda

                                                                        idAnterior = lista_identificadores.get(i);
                                                                    }else{
                                                                        // AGREGADO PERO PARA HACE MAS KILOMBO
                                                                        System.out.println("IDANTERIOR: " + idAnterior + " busco: " +TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo());
                                                                        if(idAnterior.equals(TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo()) || TablaDeSimbolos.obtenerSimbolo(lexemaIdentificador+"#"+TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo()).getId() != -1 ){
                                                                            // QUIERE DECIR QUE EL IdAnterior ES IGUAL AL TIPO DEL ID ACTUAL caso: clase1.objt_c3.c2.c1.fun1(var1)
                                                                            lexemaIdentificador = lexemaIdentificador+"#"+TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo();
                                                                            idAnterior = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual).getTipo();
                                                                        }else if(idAnterior.equals(lista_identificadores.get(i))){
                                                                            // QUIERE DECIR QUE EL ID ANTERIOR ES IGUAL AL ID NUEVO. CASO clase1.c5.c4.objt_c3.c2.c1.fun1(var1)
                                                                            idAnterior = lista_identificadores.get(i);
                                                                        }else{
                                                                            lexemaIdentificador = "";
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase " + idAnterior + " no hereda de "+ lista_identificadores.get(i);
                                                                            erroresSemanticos.add(err);
                                                                            $$ = nodoError;
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
                                                                // Hago el intento de ver si el identificador actual es un objeto de clase dentro de una clase
                                                                // pruebo agregandole al ambito el tipo o nombre del siguiente identificador del for
                                                                if(idAnterior.equals(lista_identificadores.get(i)+"#"+ambitoActual)){
                                                                    // caso que el id anterior sea igual al id actual mas ambito
                                                                    idAnterior = lista_identificadores.get(i);
                                                                }else{
                                                                    simbol = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+lista_identificadores.get(i-1));
                                                                    if(simbol.getId() != -1){
                                                                        // porque es un objteto de tipo clase declarado dentro de una clase
                                                                        idAnterior = lista_identificadores.get(i-1);
                                                                    }else{
                                                                        // pregunto si el tipo del siguiente id es una clase
                                                                        simbol = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i)+"#"+ambitoActual+"#"+ TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i-1)+"#"+ambitoActual).getTipo());
                                                                        if(simbol.getId() != -1){
                                                                            // NO TENGO NI IDEA DE SI ESTO FUNCIONA. YA ESTOY PROGRAMANDO EN MODO AUTOMATICO
                                                                            idAnterior = TablaDeSimbolos.obtenerSimbolo(lista_identificadores.get(i-1)+"#"+ambitoActual).getTipo();
                                                                        }else{
                                                                            lexemaIdentificador = "";
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: la clase no existe";
                                                                            erroresSemanticos.add(err);
                                                                            $$ = nodoError;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                
                                                if(lista_identificadores.size() > 1){
                                                    Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificador);
                                                    NodoControl arbolFuncion = null;
                                                    // obtengo el arbol de la funcion
                                                    for(NodoControl a : lista_func){
                                                        if(a.getLex().equals(lexemaIdentificador)){
                                                            arbolFuncion = a;
                                                        }
                                                    }

                                                    /*
                                                    if(arbolFuncion != null){
                                                        System.out.println("EL ARBOL DE LA FUNCION: " + arbolFuncion.getLex());
                                                    } */

                                                
                                                    NodoControl nuevoArbol= (NodoControl) crearMetodoDeClase(nombreObjtClase, arbolFuncion);
                                                    // System.out.println("El arbol de la funcion que copie es: " + nuevoArbol.getLex()); 
                                                    // nuevoArbol.recorrerArbol("-");
                                                    agregarMetodoLista(nuevoArbol);
                                                    
                                                    if(simbol != null && simbol.getParametro() == null){
                                                        simbol.setUsada(true); //SETEO en USADA LA VARIABLE 
                                                        
                                                        NodoHoja id_func = new NodoHoja(nuevoArbol.getLex());
                                                        $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,null);
                                                        
                                                    }else{
                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + nuevoArbol.getLex() +" no fue declarada";
                                                        erroresSemanticos.add(err);
                                                        $$ = (ArbolSintactico) nodoError;
                                                    }
                                                }else{
                                                    Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificador);
                                                    
                                                    if(simbol != null && simbol.getParametro() == null){
                                                        simbol.setUsada(true); //SETEO en USADA LA VARIABLE 
                                                       
                                                        NodoHoja id_func = new NodoHoja(lexemaIdentificador);
                                                        $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,null);
                                                        
                                                    }else{
                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lexemaIdentificador+" no fue declarada";
                                                        erroresSemanticos.add(err);
                                                        $$ = (ArbolSintactico) nodoError;
                                                    }
                                                }
                                                auxTipoAsig ="";
                                                // System.out.println("el objeto de clase es: " + nombreObjtClase);
                                                // System.out.println("la funcion que encontre es: "+TablaDeSimbolos.obtenerSimbolo(lexemaIdentificador).ToString());
                                                lista_identificadores.clear();        
                                                               
                                    
                                        
                                        
                        }
                        ;

                                   
                                               


sentencia_asignacion: id_asig '=' valor_asignacion {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una ASIGNACION");
                                                        String lexema = "";
                                                        String primer = "";
                                                        String ambitoAnt = "";
                                                        String ambitoVariable = ambitoActual;
                                                        System.out.println("AMBITO: " + ambitoActual);
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
                                                        }
                                                        
                                                //SI EL LEXEMA QUE FORME EXISTE EN LA TABLA DE SIMBOLOS
                                                if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() != -1){
                                                    Simbolo simbol = TablaDeSimbolos.obtenerSimbolo(lexema); //Obtengo el simbolo del lado izquierdo que acabo de crear o que ya existia
                                                    
                                                    ArbolSintactico arb = (ArbolSintactico) $3;
                                                    

                                                    String aux ="";
                                                    ArbolSintactico arb_aux = arb;
                                                    //CON ESTE WHILE OBTENGO EL TIPO DEL LADO IZQUIERDO DE LA ASIGNACION, EN CASO DE QUE EL NODO PADRE NO SEA UN NUMERO O VARIABLE, SINO UN +,-,ETC
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

                                                    //SI LOS TIPOS DE AMBOS LADOS DE LA ASIGNACION COINCIDEN
                                                    //System.out.println("simbol: " + simbol.getTipo + " auxTipoAsig "+ auxTipoAsig + " tipoIzq: " + aux);
                                                    if(simbol.getTipo().equals(aux) || auxTipoAsig.equals(aux) || auxTof){ 
                                                        if(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getId() != -1 && TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getUso().equals("constante")){ 
                                                            //SI EL VALOR DEL LADO DERECHO DE UNA ASIGNACION ES UNA CONSTANTE LA ASIGNO
                                                            simbol.setValor(TablaDeSimbolos.obtenerSimbolo(arb.getLex()).getLexema());
                                                        }
                                                        //CREO EL NODO HOJA CON EL LEXEMA DEL LADO IZQUIERDO
                                                        NodoHoja hoja = new NodoHoja(simbol.getLexema());
                                                        simbol.setValorAsignado(true);
                                                        //CREO EL SUBARBOL DE LA ASIGNACION
                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, hoja , (ArbolSintactico) $3);  //Aca hago nodo --- = ---- lo que genere
                                                    }else{
                                                        String err = "Linea: "+AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo de la asignacion es incorrecto";
                                                        erroresSemanticos.add(err);
                                                        $$ = (ArbolSintactico) nodoError;    
                                                    }

                                                }else{
                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: La variable de la asignacion no existe";
                                                    erroresSemanticos.add(err);
                                                    $$ = nodoError;
                                                }
                                                        
                                                //VUELVO A COLOCAR LAS VARIABLES AUXILIARES EN VALORES NULOS
                                                if(simboloAuxConversion != null)
                                                    simboloAuxConversion.setTipo(auxConversion);
                                                simboloAuxConversion = null;
                                                auxConversion = "";      
                                                lista_identificadores.clear();
                                                auxTipoAsig = "";
                                                auxTof = false;
                                            }
                    | id_asig '=' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico. Falta la expresion despues del '='");
                                    $$ = (ArbolSintactico) nodoError;
                                    }
                    ;

id_asig: id_asig '.' ID {lista_identificadores.add($3.sval);}
       | ID {lista_identificadores.add($1.sval);}
       ;

valor_asignacion: expr_aritmetic {$$ = $1;
                                   
                                    } //Hago que valor_asignacion valga lo que vale expr_aritmetic                            
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
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                            
                                                            //Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            //Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            
                                                            
                                                            
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                   if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                //sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

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
                                                        }
            | expr_aritmetic '<' expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR");
                                                 ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                            
                                                            //Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            //Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            
                                                            
                                                            
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                   if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                //sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

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
                                                }
            | expr_aritmetic MAYOR_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MAYOR O IGUAL");
                                                        ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                            
                                                            //Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            //Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            
                                                            
                                                            
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                   if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                //sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

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
                                                        }
            | expr_aritmetic MENOR_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR MENOR O IGUAL");
                                                        ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                            
                                                            //Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            //Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            
                                                            
                                                            
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                   if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                //sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

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
                                                        }
            | expr_aritmetic IGUAL_IGUAL expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR IGUAL");
                                                            ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                            
                                                            //Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            //Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            
                                                            
                                                            
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                   if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                //sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

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
                                                        }
            | expr_aritmetic EXCLAMACION_EXCLAMACION expr_aritmetic {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una COMPARACION POR DISTINTO");
                                                                     ArbolSintactico arb1 = (ArbolSintactico) $1;
                                                        ArbolSintactico arb2 = (ArbolSintactico) $3;

                                                        if((ArbolSintactico) $1 != nodoError && (ArbolSintactico) $3 != nodoError){
                                                            ArbolSintactico arblIzqOp = (ArbolSintactico) $1;
                                                            ArbolSintactico arblDerOp = (ArbolSintactico) $3; 
                                                            Simbolo sI = Constantes.SIMBOLO_NO_ENCONTRADO;
                                                            Simbolo sD = Constantes.SIMBOLO_NO_ENCONTRADO;

                                                            if(arblIzqOp.getLex().equals("TOF")){
                                                                if(arblIzqOp.getIzq().getLex().contains("--")){
                                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                            
                                                            //Simbolo sI= TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getLex());
                                                            //Simbolo sD= TablaDeSimbolos.obtenerSimbolo(arblDerOp.getLex());
                                                            
                                                            
                                                            
                                                            if(sI.getId() != -1){
                                                                auxTipoAsig = sI.getTipo();
                                                                if(sD.getId() != -1){
                                                                   if(sI.getTipo().equals("FLOAT") && sD.getTipo().equals("FLOAT") || sI.getTipo().equals("LONG") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("UINT") && sD.getTipo().equals("UINT") || sI.getTipo().equals("UINT") && sD.getTipo().equals("LONG") ||sI.getTipo().equals("LONG") && sD.getTipo().equals("UINT")){    
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo
                                                                    
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                        auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }
                                                            }else{
                                                                //sI = TablaDeSimbolos.obtenerSimbolo(arblIzqOp.getIzq().getLex());//Si el nodo padre no es un numero enotnces el hijo derecho si
                                                                
                                                                if(arblIzqOp.getIzq().getLex().equals("TOF")){
                                                                    if(arblIzqOp.getIzq().getIzq().getLex().contains("--")){
                                                                        //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval, (ArbolSintactico) $1 , (ArbolSintactico) $3);                                                                                                                  
                                                                    auxTipoAsig = "";           
                                                                    }else{
                                                                        String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo de la comparacion es distinto.";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = nodoError;
                                                                    }
                                                                }else{
                                                                    //sD = TablaDeSimbolos.obtenerSimbolo(arblDerOp.getIzq().getLex()); //Si el nodo padre no es un numero, entonces el hijo izq tiene que serlo

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
                 ;

sentencias_control: sentencia_while_do {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio una SENTENCIA WHILE");
                                        $$ = $1;
                                       }
                  ;

sentencia_while_do: WHILE '(' condicion_if_while ')' DO '{' bloque_sentencias_ejecutables '}' {
                                                            if((ArbolSintactico) $3 != nodoError){
                                                                NodoControl condicion = new NodoControl("condicion_while", (ArbolSintactico) $3);
                                                                NodoControl cuerpo_while = new NodoControl("cuerpo_while", (ArbolSintactico) $7);
                                                                $$ = (ArbolSintactico) new NodoComun($1.sval, condicion, cuerpo_while);
                                                            }else{
                                                                $$ = $3;
                                                            }
                                                            }
                  | WHILE '(' condicion_if_while ')' DO '{' '}'  {
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
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;


                                            if(arbIzq.getLex().equals("TOF")){
                                            	if(arbIzq.getIzq().getLex().contains("--")){
                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                               }
                                             }else{
                                                $$ = nodoError;
                                             } 
                                            }
	    	  | expr_aritmetic '-' termino {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una RESTA");

                                             ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;

                                            if(arbIzq.getLex().equals("TOF")){
                                            	if(arbIzq.getIzq().getLex().contains("--")){
                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                //System.out.println("IZq: " + arbIzq.getLex().equals("TOF")+ " der: " + arbDer.getLex().equals("TOF"));
                                                if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
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
                                                }else{    
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
                                                }
                                             }else{
                                                $$ = nodoError;
                                             }
                                            }
              | termino  {$$ = $1; 

                        } //Hago que expr_aritmetic valga lo que vale termino
              ;

termino: termino '*' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             

                                             ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                             ArbolSintactico arbDer = (ArbolSintactico) $3;
                                             Simbolo simbolo1;
                                             Simbolo simbolo2;

                                             System.out.println("EL ARBOL IZQ: " + arbIzq.getLex() + " EL ARBOL DER: "+ arbDer.getLex());


                                            if(arbIzq.getLex().equals("TOF")){
                                            	if(arbIzq.getIzq().getLex().contains("--")){
                                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                //System.out.println("IZq: " + arbIzq.getLex().equals("TOF")+ " der: " + arbDer.getLex().equals("TOF"));
                                                if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
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
                                                }else{    
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
                                                }
                                             }else{
                                                $$ = nodoError;
                                             }
                            }
       | termino '*' TOF '(' factor ')' {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             
                                            NodoHoja factor = (NodoHoja) $5;
                                            if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                                auxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo();
                                                simboloAuxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()); 
                                                //TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); //Cambio el tipo de la variable
                                                auxTipoAsig = "FLOAT";
                                                auxTof = true;
                                                NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) $5);
                                                
                                                ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                                ArbolSintactico arbDer = nodoTof;
                                                Simbolo simbolo1;
                                                Simbolo simbolo2;

                                                System.out.println("EL ARBOL IZQ: " + arbIzq.getLex() + " EL ARBOL DER: "+ arbDer.getLex());


                                                if(arbIzq.getLex().equals("TOF")){
                                                    if(arbIzq.getIzq().getLex().contains("--")){
                                                        //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                    //System.out.println("IZq: " + arbIzq.getLex().equals("TOF")+ " der: " + arbDer.getLex().equals("TOF"));
                                                    if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                                        if(simbolo1.getValorAsignado()){ 
                                                                            if(simbolo2.getUso().equals("identificador")){ 
                                                                                if(simbolo2.getValorAsignado()){                                                
                                                                                    $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,nodoTof); //Hago $1 ------- * ------- $2
                                                                                }else{
                                                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                                    erroresSemanticos.add(err);
                                                                                    $$ = (ArbolSintactico) nodoError;
                                                                                }
                                                                            }else{
                                                                                $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,nodoTof);
                                                                                

                                                                            }
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                            erroresSemanticos.add(err);
                                                                            $$ = (ArbolSintactico) nodoError;
                                                                        }
                                                                    }else{
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,nodoTof);
                                                                    }
                                                    }
                                                }else{
                                                    $$ = nodoError;
                                                }
                                            }else{
                                                String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                                erroresSemanticos.add(err);
                                                $$ = (ArbolSintactico) nodoError;
                                            }
                            }

       | termino '/' factor {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una DIVISION");
                            ArbolSintactico arbIzq = (ArbolSintactico) $1;
                            ArbolSintactico arbDer = (ArbolSintactico) $3;
                            Simbolo simbolo1;
                            Simbolo simbolo2;

                            if(arbIzq.getLex().equals("TOF")){
                                if(arbIzq.getIzq().getLex().contains("--")){
                                    //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                }
                            }else{
                            $$ = nodoError;
                            }
                    }
       | termino '/' TOF '(' factor ')' {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se detecto una MULTIPLICACION");
                             
                                            NodoHoja factor = (NodoHoja) $5;
                                            if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                                auxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo();
                                                simboloAuxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()); 
                                                //TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); //Cambio el tipo de la variable
                                                auxTipoAsig = "FLOAT";
                                                auxTof = true;
                                                NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) $5);
                                                
                                                ArbolSintactico arbIzq = (ArbolSintactico) $1;
                                                ArbolSintactico arbDer = nodoTof;
                                                Simbolo simbolo1;
                                                Simbolo simbolo2;

                                                System.out.println("EL ARBOL IZQ: " + arbIzq.getLex() + " EL ARBOL DER: "+ arbDer.getLex());


                                                if(arbIzq.getLex().equals("TOF")){
                                                    if(arbIzq.getIzq().getLex().contains("--")){
                                                        //el menos menos lo hago para luego usarlo en el assembler y hacer la resta. Pero ahora no me sirve
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
                                                    //System.out.println("IZq: " + arbIzq.getLex().equals("TOF")+ " der: " + arbDer.getLex().equals("TOF"));
                                                    if(arbIzq.getLex().equals("TOF") || arbDer.getLex().equals("TOF")){
                                                        if(simbolo1.getUso().equals("identificador")) {
                                                                        if(simbolo1.getValorAsignado()){ 
                                                                            if(simbolo2.getUso().equals("identificador")){ 
                                                                                if(simbolo2.getValorAsignado()){                                                
                                                                                    $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,nodoTof); //Hago $1 ------- * ------- $2
                                                                                }else{
                                                                                    String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo2.getLexema()+"' no inicializada";
                                                                                    erroresSemanticos.add(err);
                                                                                    $$ = (ArbolSintactico) nodoError;
                                                                                }
                                                                            }else{
                                                                                $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,nodoTof);
                                                                                

                                                                            }
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: variable '" + simbolo1.getLexema()+"' no inicializada";
                                                                            erroresSemanticos.add(err);
                                                                            $$ = (ArbolSintactico) nodoError;
                                                                        }
                                                                    }else{
                                                                        $$ = (ArbolSintactico) new NodoComun($2.sval,(ArbolSintactico)$1,nodoTof);
                                                                    }
                                                    }
                                                }else{
                                                    $$ = nodoError;
                                                }
                                            }else{
                                                String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                                erroresSemanticos.add(err);
                                                $$ = (ArbolSintactico) nodoError;
                                            }
            }
       | factor  { $$ = $1; } 
       | TOF '(' factor ')' {    System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio un FACTOR con CONVERSION DE TIPO");
                                    
                                NodoHoja factor = (NodoHoja) $3;
                                if(TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("UINT") || TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo().equals("LONG") ){
                                    auxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()).getTipo();
                                    simboloAuxConversion = TablaDeSimbolos.obtenerSimbolo(factor.getLex()); 
                                    //TablaDeSimbolos.obtenerSimbolo(factor.getLex()).setTipo("FLOAT"); //Cambio el tipo de la variable
                                    auxTipoAsig = "FLOAT";
                                    auxTof = true;
                                    NodoControl nodoTof = new NodoControl("TOF", (ArbolSintactico) $3);
                                    $$ = (ArbolSintactico) nodoTof;
                                }else{
                                    String err = "Linea: " +AnalizadorLexico.getLineaActual()+". Error Semantico: El tipo del parametro TOF ya es un flotante";
                                    erroresSemanticos.add(err);
                                    $$ = (ArbolSintactico) nodoError;
                                }

                                
                            }
       ;

factor: id_asig_der MENOS_MENOS {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un Identificador con MENOS_MENOS");
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
                            NodoHoja hoja = new NodoHoja(lexema+"--");
                            $$ = hoja;
                           
                        }else{
                            String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+lexema+ " no declarada";
                            erroresSemanticos.add(err);
                            $$ = (ArbolSintactico) nodoError;
                        }
                        if(auxTipoAsig.equals("")){
                            auxTipoAsig = TablaDeSimbolos.obtenerSimboloSinAmbito(lexema).getTipo();
                        }

                        } 
      | id_asig_der {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IDENTIFICADOR ");
            String lexema = "";
            // for(String s : auxDerAsig){
              
              String auxAmbito = "";
              String auxIdentificador = "";
              String restoAmbito = "";
                if(TablaDeSimbolos.obtenerSimbolo(auxDerAsig.get(0)+"#"+ambitoActual).getUso().equals("clase")){
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
            }
        }
      | const { $$ = $1;}
      ;

id_asig_der: id_asig_der '.' ID { 
                                    auxDerAsig.add($3.sval);
                                    $$ = $3;
                                }
           | ID { 
                auxDerAsig.add($1.sval);
                $$ = $1;
                }
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


public static ArbolSintactico crearMetodoDeClase(String objtClase, ArbolSintactico arbolFun){
   
    
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

public static Hashtable<Simbolo, String> crearAtributosClase(String nombreClase, String nombreObjtClase){
    Hashtable<String, Simbolo> tabla = TablaDeSimbolos.obtenerTablaDeSimbolos();
    Hashtable<Simbolo, String> simbolosDeClase = new Hashtable<Simbolo,String>();
    Simbolo simboloClase = TablaDeSimbolos.obtenerSimbolo(nombreClase+"#"+ambitoActual);
    String claseHereda = simboloClase.getHereda();
    String[] partes = simboloClase.getHereda().split("#");
    claseHereda = partes[0];
    for (String key : tabla.keySet()) {
	    Simbolo simbol = tabla.get(key);
        if(simbol.getLexema().contains("#"+nombreClase) && !simbol.getLexema().contains(nombreClase+"#")){
            int ultimoIndice = simbol.getLexema().lastIndexOf("#");
            simbolosDeClase.put(simbol,nombreObjtClase+"."+simbol.getLexema().substring(0,ultimoIndice));
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