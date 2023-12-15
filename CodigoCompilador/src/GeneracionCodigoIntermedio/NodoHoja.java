package GeneracionCodigoIntermedio;

import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;

public class NodoHoja extends ArbolSintactico {
	
	private String valorAssembler ="";
	
	public NodoHoja(String lex) {
		super(lex);
		valorAssembler = (lex); //seteo el valor del nodo
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getValorAssembler() {
		// TODO Auto-generated method stub
		if(getLex().equals("RETURN")) {
			return "ret \n";
		}
		return valorAssembler;
	}
	
	@Override
	public void recorrerArbol(String s) {
		// TODO Auto-generated method stub
		System.out.print(s);
        System.out.print("Lexema Nodo Hoja: " + super.getLex()+ "\n");

	}

	@Override
	public String getAssembler() {
		// TODO Auto-generated method stub
		//System.out.println("Nodo Hoja: '" + getLex() + "'");
		return "";
	}


	

}
