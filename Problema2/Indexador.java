import java.io.*;
import java.util.*;

public class Indexador {

    static List<String> data = new ArrayList<>();
    static List<List<Integer>> indices = new ArrayList<>();


    public static void generarData(String archivo, int cantidad) throws IOException {
        Random rand = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (int i = 0; i < cantidad; i++) {
                int num1 = 1000 + rand.nextInt(9000); // 1000–9999
                int num2 = rand.nextInt(100);         // 0–99
                char letra = (char) ('A' + rand.nextInt(26));
                writer.write(num1 + "\t" + num2 + "\t" + letra);
                writer.newLine();
            }
        }
    }

    public static void generarIndex(String archivo, int filas, int columnas, int maxValor) throws IOException {
        Random rand = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    int index = 1 + rand.nextInt(maxValor); // entre 1 y maxValor
                    writer.write(index + (j < columnas - 1 ? "\t" : ""));
                }
                writer.newLine();
            }
        }
    }





    public static void main(String[] args) throws IOException {
        generarData("DATA.txt",9);
        generarIndex("INDEX.txt",5,2,9);

        leerData("DATA.txt");
        leerIndices("INDEX.txt");

        System.out.println("=== Acceso aleatorio a los datos ===");

        for (int i = 0; i < indices.size(); i++) {
            List<Integer> fila = indices.get(i);
            System.out.print("Fila " + (i + 1) + " → ");
            for (int idx : fila) {
                if (idx >= 1 && idx <= data.size()) {
                    System.out.print("[" + data.get(idx - 1) + "] ");
                } else {
                    System.out.print("[??] ");
                }
            }
            System.out.println();
        }
    }

    static void leerData(String nombreArchivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                data.add(linea.trim());
            }
        }
    }

    static void leerIndices(String nombreArchivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.trim().split("\t");
                List<Integer> fila = new ArrayList<>();
                for (String p : partes) {
                    fila.add(Integer.parseInt(p.trim()));
                }
                indices.add(fila);
            }
        }
    }
}
