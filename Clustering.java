import java.net.*;
import java.io.*;
import java.util.*;

// Archivo ejecutor de tareas del cluster, este archivo
// lo ejecuta cada computadora que forma parte del cluster de red
// El archivo lee las tareas asignadas a su IP desde el archivo Asignaciones.txt
// y las ejecuta en el orden que se encuentran.

// Desarrollado por:
// Argumedo Rosales Albert
// Castillejo Mendez Gabriel
// Carrillo Jordan Sandro

public class Clustering {

    static final int FILAS = 20;
    static final int COLUMNAS = 20;

    static int[][] matrizOriginal = new int[FILAS][COLUMNAS];
    static double[][] matrizTransformada = new double[FILAS][COLUMNAS];



    public static String getAddressIP() {
        // Obtiene la dirección IP local de la máquina
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if (localHost instanceof Inet4Address) {
                return localHost.getHostAddress();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    public static List<String> obtenerTareas(String ip) {
        // Lee el archivo Asignaciones.txt y obtiene las tareas asignadas a la IP proporcionada
        List<String> tareas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Asignaciones.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("=");
                if (partes[0].trim().equals(ip)) {
                    String[] lista = partes[1].split(",");
                    for (String t : lista) {
                        tareas.add(t.trim());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo las tareas.");
        }
        return tareas;
    }

    public static void ejecutarTarea(String tarea) {
        // Ejecuta la tarea correspondiente según el nombre de la tarea
        if (tarea.equals("LEER")) {
            tareaRead();
        } else if (tarea.equals("DEPURACION")) {
            tareaDepuracion();
        } else if (tarea.equals("TRANSFORMACION")) {
            tareaTransformacion();
        } else if (tarea.equals("MINERIA")) {
            tareaMineria();
        } else if (tarea.equals("VISUALIZACION")) {
            tareaVisualizacion();
        } else {
            System.out.println("Tarea no reconocida: " + tarea);
        }
    }

    public static void tareaRead() {
        System.out.println("Ejecutando tarea: LEER");
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String linea;
            int fila = 0;
            while ((linea = br.readLine()) != null && fila < FILAS) {
                String[] partes = linea.split("\t");
                if (partes.length != COLUMNAS) {
                    System.out.println("Error en formato en la fila " + fila);
                    continue;
                }
                for (int col = 0; col < COLUMNAS; col++) {
                    matrizOriginal[fila][col] = Integer.parseInt(partes[col].trim());
                }
                fila++;
            }
            System.out.println("Archivo leído exitosamente.");
            guardarMatrizLeida("data_leida.txt");

        } catch (IOException e) {
            System.out.println("Error leyendo data.txt: " + e.getMessage());
        }
    }


    public static void tareaDepuracion() {
        // Reemplaza los valores negativos en la matrizOriginal por ceros
        System.out.println("Ejecutando tarea: DEPURACION");

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (matrizOriginal[i][j] < 0) {
                    matrizOriginal[i][j] = 0;
                }
            }
        }

        System.out.println("Negativos reemplazados por ceros.");
        guardarMatrizDepurada("data_depurada.txt");
    }


    public static void guardarMatrizDepurada(String nombreArchivo) {
        // Guarda la matriz depurada en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    writer.write(matrizOriginal[i][j] + (j < 19 ? "\t" : ""));
                }
                writer.newLine();
            }
            System.out.println("Archivo '" + nombreArchivo + "' guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo depurado: " + e.getMessage());
        }
    }

    public static void guardarMatrizLeida(String nombreArchivo) {
        // Guarda la matriz leída en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    writer.write(matrizOriginal[i][j] + (j < 19 ? "\t" : ""));
                }
                writer.newLine();
            }
            System.out.println("Archivo '" + nombreArchivo + "' guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo leido: " + e.getMessage());
        }
    }

    public static void tareaTransformacion() {
        // Normaliza los datos de la matrizOriginal y los guarda en matrizTransformada
        System.out.println("Ejecutando tarea: TRANSFORMACION");


        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        try (BufferedReader br = new BufferedReader(new FileReader("./data_depurada.txt"))) {
            String linea;
            int fila = 0;
            while ((linea = br.readLine()) != null && fila < 20) {
                String[] partes = linea.split("\t");
                for (int col = 0; col < 20; col++) {
                    int valor = Integer.parseInt(partes[col].trim());
                    matrizOriginal[fila][col] = valor;

                    if (valor < min) min = valor;
                    if (valor > max) max = valor;
                }
                fila++;
            }
        } catch (IOException e) {
            System.out.println("Error leyendo data_depurada.txt: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data_transformada.txt"))) {
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    double normalizado = (max == min) ? 0.0 : (matrizOriginal[i][j] - min) / (double)(max - min);
                    matrizTransformada[i][j] = normalizado;

                    writer.write(String.format("%.6f", normalizado));
                    if (j < 19) writer.write("\t");
                }
                writer.newLine();
            }
            System.out.println("Archivo 'data_transformada.txt' guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo transformado: " + e.getMessage());
        }
    }

    public static void tareaVisualizacion()
    {
        // Imprime la matriz transformada en consola
        System.out.println("\n ========================================VISUALIZACION========================================");
        for(int i = 0; i < FILAS; i++)
        {
            System.out.print("| ");
            for(int j = 0; j < COLUMNAS; j++)
            {
                System.out.printf("%.4f  ", matrizTransformada[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("");
    }


    public static void tareaMineria() {
        System.out.println("Ejecutando tarea: MINERIA");

        // Validar que la matriz esté cargada
        boolean vacia = true;
        for (int i = 0; i < FILAS && vacia; i++) {
            for (int j = 0; j < COLUMNAS && vacia; j++) {
                if (matrizTransformada[i][j] != 0.0) {
                    vacia = false;
                }
            }
        }
        if (vacia) {
            System.out.println("La matriz transformada está vacía. Asegúrate de ejecutar TRANSFORMACION antes.");
            return;
        }

        // Calcular distancia entre algunos pares de filas
        for (int i = 0; i < Math.min(5, FILAS); i++) {
            for (int j = i + 1; j < Math.min(5, FILAS); j++) {
                double distancia = calcularDistancia(matrizTransformada[i], matrizTransformada[j]);
                System.out.printf("Distancia entre fila %d y fila %d: %.4f%n", i, j, distancia);
            }
        }

        System.out.println("Minería simple completada.");
    }

    public static double calcularDistancia(double[] a, double[] b) {
        double suma = 0.0;
        for (int i = 0; i < a.length; i++) {
            suma += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(suma);
    }


    public static void main(String[] args) {
        String ip = getAddressIP();
        System.out.println("IP detectada: " + ip);

        List<String> tareas = obtenerTareas(ip);

        //Array con tareas para pruebas, en el cluster se asignan las tareas de acuerdo a la IP
        //con la linea anterior
        //List<String> tareas = Arrays.asList("LEER", "DEPURACION", "TRANSFORMACION", "VISUALIZACION");

        if (tareas.isEmpty()) {
            System.out.println("No se encontraron tareas para esta IP.");
        } else {
            System.out.println("Tareas asignadas: " + tareas);
            for (String tarea : tareas) {
                ejecutarTarea(tarea);
            }
        }
    }
}