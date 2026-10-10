package MetodosSecundarios;

import java.util.Scanner;

import Excepciones.ReiniciarJuego;

/**
 * Clase para todos los métodos o funcionalidades propias de la Entrada/Salida
 * 
 * @author DaniS y Libio
 */
public class Datos {
    // Scanner (Objeto) estático que se podrá utilizar en todos los métodos de la
    // clase
    private static Scanner s = new Scanner(System.in);
    public static int milisegundos = 1000;

    /**
     * Pide una cadena de caracteres y la devuelve
     * 
     * @param mensaje de petición de datos tipo String
     * @return Dato introducido por teclado tipo string
     * @throws ReiniciarJuego para reiniciar el juego cuando se quiera
     */
    public static String pedirCadena(String mensaje) throws ReiniciarJuego {
        String entrada;
        while (true) {
            System.out.print(mensaje);
            entrada = s.nextLine().trim();
            if (!entrada.isEmpty()) {
                break;
            }
            System.out.println("No se puede introducir un valor nulo");
        }

        // Para que el usuario vuelva a iniciar el juego cuando quiera
        solicitarReinicio(entrada);

        return entrada;
    }

    /**
     * Pide un entero y lo devuelve
     * 
     * @param mensaje de petición de datos tipo entero
     * @return Dato introducido por teclado tipo entero
     * @throws ReiniciarJuego para reiniciar el juego cuando se quiera
     */
    public static int pedirEntero(String mensaje) throws ReiniciarJuego {

        while (true) {
            System.out.print(mensaje);
            String entrada = s.nextLine().trim();

            // Para que el usuario vuelva a iniciar el juego cuando quiera
            solicitarReinicio(entrada);

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                entradaIncorrecta();
            }
        }
    }

    /**
     * Método que solo sirve para pulsar enter cuando lo pide por pantalla
     * 
     * @param 'ninguno'
     */
    public static void pulsaEnter() {
        System.out.println("\nPulsa enter para continuar");
        s.nextLine();
    }

    /**
     * Método que incluye la posibilidad de salir del juego
     * 
     * @param 'ninguno'
     * @return valor booleano que indica si quiere salir o no del juego
     * @throws ReiniciarJuego para reiniciar el juego cuando se quiera
     */
    public static boolean salirDelJuego() throws ReiniciarJuego {
        String entrada;
        System.out.println("Pulse enter para continuar, si quieres salir, escribe \"salir\": ");
        entrada = s.nextLine().trim();

        // Para que el usuario vuelva a iniciar el juego cuando quiera
        solicitarReinicio(entrada);

        return entrada.equalsIgnoreCase("salir");
    }

    /**
     * Solamente es un mensaje para entradas incorrectas
     * 
     * @param 'ninguno'
     */
    public static void entradaIncorrecta() {
        System.out.println("Mensaje no válido, introduce los valores sugeridos.");
    }

    /**
     * Salto de líneas para cuando se cambie de menu/salto de escena
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     */
    public static void saltoDeLineas() throws InterruptedException {
        Thread.sleep(milisegundos);
        for (int i = 0; i < 20; i++) {
            System.out.println("");
        }
    }

    /**
     * Método que lanza la excepcion reiniciar juego si se introduce terminar
     * 
     * @param entrada valor tipo String que representa la entrada de informacion
     * @throws ReiniciarJuego para reiniciar el juego cuando se quiera
     */
    public static void solicitarReinicio(String entrada) throws ReiniciarJuego {
        if (entrada.equalsIgnoreCase("reiniciar")) {
            throw new ReiniciarJuego("Regresando al menú principal...");
        }
    }

    /**
     * Método que agrupa el flujo final de la base de datos: Guarda, Muestra y permite Borrar.
     * * @param nombreGanador Nombre del jugador que acaba de ganar
     * @throws ReiniciarJuego 
     * @throws InterruptedException 
     */
    public static void gestionarRankingFinal(String nombreGanador) throws ReiniciarJuego, InterruptedException {
        Datos.saltoDeLineas();
        System.out.println("Guardando tu victoria en la Base de Datos SQLite...");
        
        // 1. Guarda o actualiza el perfil (INSERT / UPDATE)
        GestorRankingBD.registrarVictoria(nombreGanador); 
        
        // 2. Muestra los mejores jugadores (SELECT)
        GestorRankingBD.mostrarTopJugadores(); 

        // 3. Ofrece la opción de borrar el perfil (DELETE)
        boolean salir = false;
        while (!salir) {
            String respuesta = Datos.pedirCadena("¿Deseas eliminar tu perfil y victorias del Ranking Global? (S/N): ");

            if (respuesta.equalsIgnoreCase("S")) {
                GestorRankingBD.eliminarPerfil(nombreGanador);
                salir = true;
            } else if (respuesta.equalsIgnoreCase("N")) {
                System.out.println("¡Tu historial de victorias sigue a salvo!");
                salir = true;
            } else {
                System.out.println("Entrada incorrecta. Por favor, introduce 'S' o 'N'.");
            }
        }
    }
}
