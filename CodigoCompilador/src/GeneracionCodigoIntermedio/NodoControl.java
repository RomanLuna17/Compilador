package GeneracionCodigoIntermedio;

import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;

public class NodoControl extends ArbolSintactico {
	
	private String valorAssembler = "";
	
	public NodoControl(String lex,ArbolSintactico nodo) {
        super(lex);
        setIzq(nodo);
        valorAssembler = (lex); //seteo el valor del nodo
    	
    }

	@Override
	public String getValorAssembler() {
		// TODO Auto-generated method stub
		return valorAssembler;
	}
	
	@Override
	public void recorrerArbol(String identado) {
		// TODO Auto-generated method stub
		System.out.print(identado+"Lexema: " +super.getLex()+ "\n");
        identado += "    ";
        super.getIzq().recorrerArbol(identado);
	}

	

	@Override
	public String getAssembler() {
		// TODO Auto-generated method stub
		String salida = "";
		//System.out.println("Nodo Control. Lexema: '" + getLex()+"'. Hijo: '" + getIzq().getLex()+"'");
		switch(this.getLex()) {
			case "Programa":
				ArbolSintactico arbIzq = super.getIzq();
				salida += arbIzq.getAssembler();
				break;
			case "Condicion":
				//System.out.println("ENTRE EN CONDICION");
				salida += getIzq().getAssembler();
				break;
			case "then":
				//System.out.println("ENTRE EN THEN");
				salida += getIzq().getAssembler();
				break;
			case "else":
				//System.out.println("ENTRE EN ELSE");
				salida+="JMP "+ArbolSintactico.apilarLabel()+"\n";
				String aux = ArbolSintactico.desapilarLabel();
				if(ArbolSintactico.pilaLabels.isEmpty()) {
					salida+= aux + ": \n";
				}else {
					salida+= ArbolSintactico.desapilarLabel() + ": \n";
					ArbolSintactico.pilaLabels.push(aux);
				}
				salida += getIzq().getAssembler();
				break;
			case "cuerpo_while":
				salida += this.getIzq().getAssembler();
				String auxlabel = ArbolSintactico.desapilarLabel();
				salida += "JMP " + ArbolSintactico.desapilarLabel() +" \n";
				ArbolSintactico.pilaLabels.push(auxlabel);
				break;
			case "condicion_while":
				//label
				salida += ArbolSintactico.apilarLabel() +": \n";
				salida += this.getIzq().getAssembler();
				break;
			case "PRINT":
				//Como en este caso se que solo voy a tener un hijo izquierdo, hago la operacion aca
				Simbolo simbolo = TablaDeSimbolos.obtenerSimbolo(this.getIzq().getLex());
				//System.out.println("SIMBOLO: " + simbolo.ToString());
				if(simbolo.getUso().equals("cadena")) {
					salida+= "invoke MessageBox, NULL, addr $" + simbolo.getLexema().replace(" ", "$").replace(".","_")+", addr $" +simbolo.getLexema().replace(" ", "$").replace(".","_") +", MB_OK \n";
					//salida+= "invoke ExitProcess, 0 \n";
				}else if(simbolo.getUso().equals("identificador")){
					salida += "MOV EAX , $"+simbolo.getLexema().replace("#", "$")+" \n";
					salida += "invoke MessageBox, NULL, addr $" + simbolo.getLexema().replace("#", "$").replace(".","_")+", addr EAX , MB_OK \n";
				}
				break;
			case "TOF":
				if(this.getIzq().getLex().substring(0, getIzq().getLex().length()-3).equals("_ui")) {
					//es un entero de 16
					salida += "FILD $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$")+" \n";
					salida+= getIzq().getAssembler();
				}else {
					//es un entero de 32
					salida += "FILD $"+getIzq().getLex().replace("#", "$").replace(".","_").replace("+","$").replace("-","$") +" \n";
					salida += getIzq().getAssembler();
				}
				break;
		}
		//System.out.println("ASSEMBLER: ");
		//System.out.println(salida);
		return salida;
	}



	

}