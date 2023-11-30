package GeneracionCodigoIntermedio;

import Compilador.Simbolo;
import Compilador.TablaDeSimbolos;

public class NodoHoja extends ArbolSintactico {

	public NodoHoja(String lex) {
		super(lex);
		// TODO Auto-generated constructor stub
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
