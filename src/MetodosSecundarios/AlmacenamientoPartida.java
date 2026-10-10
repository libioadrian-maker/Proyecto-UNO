package MetodosSecundarios;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Excepciones.ReiniciarJuego;
import Excepciones.SalirDelJuego;
import Objetos.PartidaContexto;

/**
 * Clase que contiene metodos necesarios para controlar la serializacion de una
 * partida en un archivo .dat
 * 
 * @author Dani S y Libio
 */
public class AlmacenamientoPartida {

    // Rutas de los archivos guardados en el proyecto
    private static final Path RUTA_PARTIDA = Paths.get("src", "Archivos").resolve("partida_guardada.dat");
    private static final String NOMBRE_ARCHIVO_PARTIDA = RUTA_PARTIDA.toString();
    private static final File ARCHIVO_PARTIDA = new File(NOMBRE_ARCHIVO_PARTIDA);

    /**
     * Método que comprueba que el archivo partida sea un archivo y que exista
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando se quiera
     * @throws SalirDelJuego        para salir del juego cuando quieras
     */
    public static int partidaGuardada() throws InterruptedException, ReiniciarJuego, SalirDelJuego {
        if (ARCHIVO_PARTIDA.exists() && ARCHIVO_PARTIDA.isFile()) {
            pedirReanudarPartida();
            // Para terminar la partida
            return 5;
        } else if (!ARCHIVO_PARTIDA.exists()) {
            System.out.println("No hay partida guardada");
            // Para seguir con la partida
            return 0;
        } else {
            System.out.println("No es un archivo lo que se indica en la ruta");
            // Para seguir con la partida
            return 0;
        }
    }

    /**
     * Método que permite guardar la partida actual en un archivo en binario con el
     * respectivo contexto de la partida actual
     * 
     * @param contexto de la partida actual
     */
    public static void guardarPartida(PartidaContexto contexto) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(NOMBRE_ARCHIVO_PARTIDA))) {
            out.writeObject(contexto);
            System.out.println("Partida guardada correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    /**
     * Método que pide si quieres reanudar la partida anterior o no
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando se quiera
     * @throws SalirDelJuego        para salir del juego cuando quieras
     */
    private static void pedirReanudarPartida()
            throws InterruptedException, ReiniciarJuego, SalirDelJuego {
        boolean salir = false;
        Datos.saltoDeLineas();
        System.out.println("Se ha detectado una partida interrumpida.");
        while (!salir) {
            String respuesta = Datos.pedirCadena("¿Deseas reanudar la partida anterior? (S/N): ");

            switch (respuesta) {
                case "S":
                    UnoEngine.contextoPartida = cargarPartida();
                    UnoEngine.reanudarPartida();
                    salir = true;
                    break;
                case "N":
                    eliminarPartida();
                    salir = true;
                    break;

                default:
                    System.out.println("Tienes que poner \"S\" o \"N\", intentalo de nuevo");
                    break;
            }
        }
    }

    /**
     * Método que carga la partida guardada leyendo el archivo binario y pasandolo a
     * objeto
     * 
     * @param 'ninguno'
     * @return devuelve el contexto de la partida guardada
     */
    public static PartidaContexto cargarPartida() {
        if (!ARCHIVO_PARTIDA.exists()) {
            System.out.println("No existe una partida guardada");
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(NOMBRE_ARCHIVO_PARTIDA))) {
            Object obj = in.readObject();
            if (obj instanceof PartidaContexto contexto) {
                System.out.println("\nPartida cargada correctamente");
                return contexto;
            } else {
                System.out.println("El archivo de guardado no contiene una partida válida.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
            return null;
        }
    }

    /**
     * Método que elimina el archivo de la partida
     * 
     * @param 'ninguno'
     */
    public static void eliminarPartida() {
        if (ARCHIVO_PARTIDA.exists()) {
            try {
                Files.delete(RUTA_PARTIDA);
            } catch (IOException e) {
                System.out.println("No se encuentra el archivo de la partida");
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

}
