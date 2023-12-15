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
import java.util.Stack;

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
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            simbolo.setParametro(simbolParametro);
                                                            simbolo.setDireccionMetodo($1.sval+"#"+ambitoActual);
                                                            agregarArbol($1.sval+"#"+ambitoActual);
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
                     | encabezado_funcion '(' ')' '{' cuerpo_de_la_funcion '}' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una DELCARACION de FUNCION SIN PARAMETRO");
                                                    int indice = ambitoActual.lastIndexOf('#');
                                                    ambitoActual = ambitoActual.substring(0, indice);
                                                   
                                                    if(!TablaDeSimbolos.existeSimboloAmbitoActual($1.sval+"#"+ambitoActual, "nulo")){ 
                                                        if(tieneReturn){
                                                            TablaDeSimbolos.modificarLexema($1.sval, $1.sval +"#"+ambitoActual);
                                                            Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval + "#"+ambitoActual);
                                                            simbolo.setUso("funcion");
                                                            simbolo.setDireccionMetodo($1.sval+"#"+ambitoActual);
                                                            agregarArbol($1.sval+"#"+ambitoActual); 
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
                                if(aux_raiz_func != null){
                                    NodoComun auxRaiz = aux_raiz_func;
                                    NodoComun auxPtrFunc = ptrRaizFuncion;
                                    pilaAuxArbol.push(auxRaiz); // guardo primero la raiz
                                    pilaAuxArbol.push(auxPtrFunc); // guardo segundo el ptro
                                }
                                NodoComun arbolFun = new NodoComun("Sentencia", null, null);
                                aux_raiz_func = arbolFun;
                                ptrRaizFuncion = arbolFun;

                                if(!auxiliar_clase.equals("")){
                                    auxiliar_profundidad++;
                                }

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

lista_de_variables: lista_de_variables ';' ID {lista_identificadores.add($3.sval);}
				  | ID {lista_identificadores.add($1.sval);}
                  ;

cuerpo_de_la_funcion: cuerpo_de_la_funcion sentencias_de_funcion 
                    | sentencias_de_funcion 
                    ;

sentencias_de_funcion: sentencia_declatariva_especificas 
                     | sentencias_ejecutables { generarArbolFunc((ArbolSintactico) $1);
                                                System.out.println("El subArbol que estoy creando es: ");
                                                ArbolSintactico arb = (ArbolSintactico) $1;
                                                arb.recorrerArbol("-");
                                              }
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
                                        // primero creo los atributos con el objt adelante
                                        
                                        Simbolo simboloTipo = TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual);
                                        if(!simboloTipo.getClaseAPosterior()){
                                            ArrayList<String> atributosClase = simboloTipo.getAtributosTotalesClase();
                                            ArrayList<String> lexemasListaObjt = new ArrayList<>();
                                            for(String lexemaAtributo : atributosClase){
                                                lexemasListaObjt.add(CrearCopiaVariable(lexemaAtributo,$1.sval, ambitoActual)); // s es el principio del lexema, el nombre del objeto
                                                
                                            }
                                            
                                            // Todos los atributos
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
                                                            System.out.println("SIMBOLO: " + TablaDeSimbolos.obtenerSimbolo($1.sval +"#"+ ambitoActual.substring(0, indiceLexClase)).ToString() + " LISTA DEBO: " +TablaDeSimbolos.obtenerSimbolo($1.sval +"#"+ ambitoActual.substring(0, indiceLexClase)).getListaAQuienLeDebo());
                                                            
                                                            String identClase = $1.sval +"#"+ ambitoActual.substring(0, indiceLexClase);
                                                            
                                                            if((TablaDeSimbolos.obtenerSimbolo(identClase).getUso().equals("")) || (TablaDeSimbolos.obtenerSimbolo(identClase).getClaseAPosterior())){
                                                               if(!auxVarClases.equals("")){
                                                                    String claseHereda = nivelDeClaseCorrecto(auxVarClases); 
                                                                    if(claseHereda.equals("")){
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases);
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                        // TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);

                                                                        //creo todos los identificadores o metodos de la clase que heredo en esta clase
                                                                        // String[] claseHeredada = auxVarClases.split("#");
                                                                        // crearAtributoClaseHeredada(claseHeredada[0], $1.sval); //paso solo el nombre de la clase que heredo, no el ambito

                                                                        
                                                                        
                                                                    }else{
                                                                        TablaDeSimbolos.obtenerSimbolo(identClase).setHereda(auxVarClases); //agrego igual de la clase que hereda por si luego hay mas clases
                                                                        String err = "Linea: "+AnalizadorLexico.getLineaActual()+". Error Semantico: El nivel de herencia es superior a 3 por: " + claseHereda;
                                                                        erroresSemanticos.add(err);
                                                                    }
                                                                }else{
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setHereda("nadie");
                                                                    TablaDeSimbolos.obtenerSimbolo(identClase).setUso("clase");
                                                                    //TablaDeSimbolos.obtenerSimbolo(identClase).setClasePosterior(false);
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
                                                                String err = "Linea: " + AnalizadorLexico.getLineaActual()+". Error Semantico: Clase " + $1.sval+" redeclarada";
                                                                erroresSemanticos.add(err);
                                                            }

                                                            nombre_clase = "";
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
                                        //TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setClasePosterior(true);
                                        //TablaDeSimbolos.obtenerSimboloSinAmbito($1.sval).setUso("clase");
                                         System.out.println("SIMBOLO FINAL DEC POSTERIOR: " + TablaDeSimbolos.obtenerSimbolo($1.sval +"#"+ ambitoActual.substring(0, indiceLexClase)).ToString() + " LISTA DEBO: " +TablaDeSimbolos.obtenerSimbolo($1.sval +"#"+ ambitoActual.substring(0, indiceLexClase)).getListaAQuienLeDebo() );
                                         
                                    }
                  | encabezado_clase '{' cuerpo_clase error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() + ". Error sintactico.Falta } al final de la clase");}
                  | encabezado_clase cuerpo_clase '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta '{' al inicio de la clase");}
                  | encabezado_clase '{' '}' error {agregarError("Linea: " + AnalizadorLexico.getLineaActual() +". Error sintactico.Falta cuerpo de la clase");}
				  ;

encabezado_clase: CLASS ID {
                        $$ = $2;
                        nombre_clase = $2.sval;
                        String lexema = $2.sval +"#"+ ambitoActual;
                        
                        if(!auxiliar_clase.equals("")){
                            System.out.println("####################### ESTOY AL PRINCIPIO DE LA DECLARACION DE UNA CLASE, LA LISTA DE AUXILIAR_CLASE HASTA AHORA ES: " + auxiliar_lista_clase);
                            ContenedorClase nuevoCont = new ContenedorClase(auxiliar_clase, auxiliar_lista_clase, auxiliar_profundidad);
                            pilaAuxiliarClases.push(nuevoCont);    
                            auxiliar_clase = "";
                            auxiliar_lista_clase.clear();
                            auxiliar_profundidad = 0;   
                        } 

                        auxiliar_clase = lexema; // auxiliar_clase = c2#global
                        if(TablaDeSimbolos.obtenerSimbolo(lexema).getId() == -1)
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
                                                    if(!$1.sval.equals("")){
                                                        for(String s : lista_identificadores){
                                                            if(!TablaDeSimbolos.existeSimboloAmbitoActual(s+"#"+ambitoActual, "nulo")){
                                                                TablaDeSimbolos.setTipo($1.sval, s);
                                                                TablaDeSimbolos.modificarLexema(s, s+"#"+ambitoActual);
                                                                Simbolo simbolo = TablaDeSimbolos.obtenerSimboloSinAmbito(s + "#"+ambitoActual);
                                                                simbolo.setUso("identificador");
                                                                TablaDeSimbolos.borrarSimbolo(s);
                                                                simbolo.setEsObjetoClase(true);

                                                                // primero creo los atributos con el objt adelante
                                                                Simbolo simboloTipo = TablaDeSimbolos.obtenerSimbolo($1.sval+"#"+ambitoActual);
                                                                ArrayList<String> atributosClase = simboloTipo.getAtributosTotalesClase();
                                                                ArrayList<String> lexemasListaObjt = new ArrayList<>();
                                                                for(String lexemaAtributo : atributosClase){
                                                                   lexemasListaObjt.add(CrearCopiaVariable(lexemaAtributo, s, ambitoActual)); // s es el principio del lexema, el nombre del objeto
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
                                                                //doy error por re declaracion del identificador
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
                      | sentencias_ejecucion_funcion ','{ $$ = $1;
                                                           System.out.println("mando el subArbol para arribla: sentencias_ejecucion_funcion");
                                                        } 
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

sentencias_ejecucion_funcion: id_asig '(' expr_aritmetic ')' {
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
                                                                            $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,(ArbolSintactico) $3);
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                                            erroresSemanticos.add(err);
                                                                            $$ = (ArbolSintactico) nodoError;
                                                                        }
                                                                    }else{
                                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lista_identificadores.get(0) +" no fue declarada";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = (ArbolSintactico) nodoError;
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
                                                                            $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,(ArbolSintactico) $3);
                                                                        }else{
                                                                            String err = "Linea: " + AnalizadorLexico.getLineaActual() + ". Error Semantico: El tipo del parametro es incorrecto";
                                                                            erroresSemanticos.add(err);
                                                                            $$ = (ArbolSintactico) nodoError;
                                                                        }
                                                                    }else{
                                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lista_identificadores.get(0) +" no fue declarada";
                                                                        erroresSemanticos.add(err);
                                                                        $$ = (ArbolSintactico) nodoError;
                                                                    }

                                                                }
                                                                lista_identificadores.clear();
                                                                
                                                            }
                            | id_asig '(' ')' {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una INVOCACION A FUNCION SIN PARAMETRO");
                                                String lexemaIdentificadorFun = "";
                                                String idAnterior = "";
                                                String nombreObjtClase = "";
                                                
                                                if(lista_identificadores.size() == 1){
                                                    lexemaIdentificadorFun = lista_identificadores.get(0)+"#"+ambitoActual;
                                                    Simbolo simboloFun = TablaDeSimbolos.obtenerSimbolo(lexemaIdentificadorFun);
                                                    if(simboloFun.getId() != -1 & simboloFun.getParametro() == null){
                                                        simboloFun.setUsada(true);
                                                        NodoHoja id_func = new NodoHoja(simboloFun.getLexema());
                                                        $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,null);
                                                    }else{
                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lista_identificadores.get(0) +" no fue declarada";
                                                        erroresSemanticos.add(err);
                                                        $$ = (ArbolSintactico) nodoError;
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
                                                        $$ = (ArbolSintactico) new NodoComun("Ejecucion_func",id_func,null);
                                                    }else{
                                                        String err = "Linea "+AnalizadorLexico.getLineaActual()+". Error Semantico: La funcion " + lista_identificadores.get(0) +" no fue declarada";
                                                        erroresSemanticos.add(err);
                                                        $$ = (ArbolSintactico) nodoError;
                                                    }
                                                }
                                                lista_identificadores.clear();
                                                
                                                }
                            ;

                                   
                                               


sentencia_asignacion: id_asig '=' valor_asignacion {System.out.println("Linea: " + AnalizadorLexico.getLineaActual() + ". Se reconocio una ASIGNACION");
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
                            $$ = (ArbolSintactico) hoja;
                        }else{
                            String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+auxIdentificador+ " no declarada";
                            erroresSemanticos.add(err);
                            $$ = (ArbolSintactico) nodoError;
                        }
                        auxDerAsig.clear();
                        if(auxTipoAsig.equals("")){
                            //Esto me sirve para resolver la comparacion del parametro de una funcion
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
      | id_asig_der {System.out.println("Linea: "+ AnalizadorLexico.getLineaActual() + ". Se reconocio un IDENTIFICADOR ");
            String lexema = "";
            // for(String s : auxDerAsig){
              
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
                $$ = (ArbolSintactico) hoja;
              }else{
                String err = "Linea "+AnalizadorLexico.getLineaActual() + ". Error Semantico: Variable "+auxIdentificador+ " no declarada";
                erroresSemanticos.add(err);
                $$ = (ArbolSintactico) nodoError;
              }
              auxDerAsig.clear();
              if(auxTipoAsig.equals("")){
                //Esto me sirve para resolver la comparacion del parametro de una funcion
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