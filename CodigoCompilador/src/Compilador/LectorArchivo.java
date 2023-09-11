package Compilador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import AccionesSemanticas.*;
public class LectorArchivo {

// Este metodo se encarga de leer un txt y aparterir de las cantidad de filas y columnas dadas genera la matriz
	 public static int[][] readIntMatrixFile(String path, int rows, int columns) {
	        int[][] int_matrix = new int[rows][columns];

	        try {
	            File archivo = new File(path);
	            Scanner scanner = new Scanner(archivo);

	            for (int i = 0; i < rows; ++i) {
	                for (int j = 0; j < columns; ++j) {
	                    int_matrix[i][j] = Integer.parseInt(scanner.nextLine());
	                }
	            }

	            scanner.close();
	        } catch (FileNotFoundException excepcion) {
	            System.out.println("No se pudo leer el archivo " + path);  
	            excepcion.printStackTrace();
	        }

	        return int_matrix;
	    }

// Este metodo se encarga de leer un txt y aparterir de las cantidad de filas y columnas dadas genera la matriz de Acciones Semanticas
	    public static AccionSemantica[][] readActionMatrixFile(String path, int rows, int columns) {
	        AccionSemantica[][] action_matrix = new AccionSemantica[rows][columns];

	        try {
	            File archivo = new File(path);
	            Scanner scanner = new Scanner(archivo);

	            for (int i = 0; i < rows; ++i) {
	                for (int j = 0; j < columns; ++j) {
	                    action_matrix[i][j] = crearAccion(scanner.nextLine());
	                }
	            }

	            scanner.close();
	        } catch (FileNotFoundException excepcion) {
	            System.out.println("No se pudo leer el archivo " + path);
	            excepcion.printStackTrace();
	        }

	        return action_matrix;
	    }
	    
// Este metodo se encarga de leer un archivo y setear las palabras reservadas
	    public static Map<String, Integer> readMapFile(String path) {
	        Map<String, Integer> map = new HashMap<>();

	        try {
	            File archivo = new File(path);
	            Scanner scanner = new Scanner(archivo);

	            while (scanner.hasNext()) {
	                String palabra_reservada = scanner.next();
	                int identificador = scanner.nextInt();
	                map.put(palabra_reservada, identificador);
	            }

	            scanner.close();
	        } catch (FileNotFoundException excepcion) {
	            System.out.println("No se pudo leer el archivo " + path);
	            excepcion.printStackTrace();
	        }

	        return map;
	    }

	    public static void writeProgram(String file_name, String content) {
	        File file = new File(file_name);

	        try {
	            file.createNewFile();
	            FileWriter writer = new FileWriter(file_name);
	            writer.write(content);
	            writer.close();
	        } catch (IOException exc) {
	            exc.printStackTrace();
	        }
	    }

	    public static boolean endOfFile(Reader reader) throws IOException {
	        reader.mark(1);
	        int value = reader.read();
	        reader.reset();

	        return value < 0;
	    }

	    public static char getNextCharWithoutAdvancing(Reader reader) throws IOException {
	        reader.mark(1);
	        char next_character = (char) reader.read();
	        reader.reset();

	        return next_character;
	    }
	    
	    private static AccionSemantica crearAccion(String action_name) {
	        switch (action_name) {
	            case "AS0":
	                return new AS0();
	            case "AS1":
	                return new AS1();
	            case "AS2":
	                return new AS2();
	            case "ASE":
	                return new ASE();
	            case "AS3":
	                return new AS3();
	            case "AS4":
	                return new AS4();
	            case "AS5":
	                return new AS5();
	            case "AS6":
	                return new AS6();
	            case "AS7":
	                return new AS7();
	            case "AS8":
	                return new AS8();
	            case "AS9":
	                return new AS9();
	            default:
	                return null;
	        }
	    }
}
