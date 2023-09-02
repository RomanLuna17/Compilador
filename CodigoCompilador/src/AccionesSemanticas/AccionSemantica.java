package AccionesSemanticas;

import java.io.IOException;
import java.io.Reader;

public interface AccionSemantica {
	
	public int run(Reader lector, StringBuilder token_act) throws IOException;
	
}
