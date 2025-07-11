import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerarData {
    public static void main(String[] args) {
        String nombreArchivo = "data.txt";
        int filas = 20;
        int columnas = 20;

        Random rand = new Random();

        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    int numero = rand.nextInt(1000)-300; 
                    writer.write(numero + (j < columnas - 1 ? "\t" : ""));
                }
                writer.write("\n");
            }
            System.out.println("Archivo '" + nombreArchivo + "' generado con éxito.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}