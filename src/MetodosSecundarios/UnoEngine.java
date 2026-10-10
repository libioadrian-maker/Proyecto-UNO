package MetodosSecundarios;

import Excepciones.CartaLanzadaNoValida;
import Excepciones.ReiniciarJuego;
import Excepciones.SalirDelJuego;

import java.util.HashMap;
import java.util.List;

import Enumerados.Tipos;
import Objetos.Carta;
import Objetos.Jugador;
import Objetos.Tablero;
import Objetos.Turno;
import Objetos.PartidaContexto;

/**
 * Clase que gestiona el flujo del juego
 * 
 * @author DaniS y Libio
 */
public class UnoEngine {

    protected static PartidaContexto contextoPartida;
    private static boolean fin;
    private static boolean cartaValida;
    private static int opcionCarta;
    protected static Jugador jugador;

    /**
     * Da inicio a la partida del uno inicializando el contexto de esta y sacando y
     * validando la primera carta
     * 
     * @param nombresCargados         nombres de los jugadores
     * @param cantidadActualJugadores cantidad de jugadores
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reinciar el juego cuando se quiera
     * @throws SalirDelJuego        para salir del juego cuando quieras
     */
    public static void inicioPartida(List<String> nombresCargados, int cantidadActualJugadores)
            throws InterruptedException, ReiniciarJuego, SalirDelJuego {

        fin = false;

        contextoPartida = new PartidaContexto(new Tablero(), new Turno(), new HashMap<>(), cantidadActualJugadores,
                nombresCargados);

        Mecanicas.repartoInicial();

        contextoPartida.getTablero().dejar(contextoPartida.getTablero().tirarCarta());

        Mecanicas.aplicarEfectoPrimeraCarta();
        Datos.pulsaEnter();

        partida();
    }

    /**
     * Lógica principal de la partida en la que se discurre el fluje y una vez
     * termina imprime la pantalla final precedida de sus respectivas estadísticas y
     * la eliminación de la partida guardada
     * 
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reinciar el juego cuando se quiera
     * @throws SalirDelJuego        para salir del juego cuando quieras
     */
    public static void partida() throws InterruptedException, ReiniciarJuego, SalirDelJuego {
        flujoDeLaPartida();

        Pantallas.pantallaFinal();

        AlmacenamientoDatos.preguntarMostrarEstadisticas(jugador, contextoPartida.getJugadores(),
                contextoPartida.getControladorTurnos());

        if (fin) {
            Datos.gestionarRankingFinal(jugador.getNombre());
        }
        
        AlmacenamientoPartida.eliminarPartida();

        GestorRankingBD.cerrarConexion();

    }

    /**
     * Metodo que reproduce el flujo de la partida
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reinciar el juego cuando se quiera
     * @throws SalirDelJuego        para salir del juego cuando quieras
     */
    private static void flujoDeLaPartida() throws InterruptedException, ReiniciarJuego, SalirDelJuego {
        while (!fin) {
            // Escoge al jugador correspondiente, basado en el turno actual
            Mecanicas.ordenarBarajaJugadores();
            jugador = actual();

            // Imprime el tablero, con el turno, el jugador y las cartas
            verTablero();

            // Resolucion de la carta que quieres sacar
            accionSacarCarta();

            // Validación carta sacada
            cartaSacadaValida();

            Thread.sleep(Datos.milisegundos);
            System.out.println("\n  * " + contextoPartida.getTablero() + " *");
            Thread.sleep(Datos.milisegundos);

            if (Datos.salirDelJuego()) {
                AlmacenamientoPartida.guardarPartida(UnoEngine.contextoPartida);
                throw new SalirDelJuego();
            }

            // Si nadie ha ganado, pasamos al siguiente turno
            if (!fin)
                siguiente();
        }
    }

    /**
     * Método que muestra la interfaz gráfica del tablero excepto la de la accion
     * 
     * @param 'ninguno'
     * @throws InterruptedException Para los saltos de lineas
     */
    private static void verTablero() throws InterruptedException {
        Datos.saltoDeLineas();
        System.out.println("\n--- TURNO DE: " + jugador.getNombre() + " ---");
        System.out.println("  - " + contextoPartida.getControladorTurnos() + " -");
        System.out.println("Mesa: " + contextoPartida.getTablero().verCartaEnLaMesa());

        // Mostrar la mano del jugador actual
        for (int i = 0; i < jugador.getNumCartas(); i++) {
            System.out.print(i + ":" + jugador.getMano().obtener(i) + " ");
        }
        System.out.println(jugador.getNumCartas() + ":[ROBAR]");
    }

    /**
     * Método que sirve para sacar la carta que quieres o para robar carta
     * 
     * @param 'ninguno'
     * @throws ReiniciarJuego       para reinciar el juego cuando se quiera
     * @throws InterruptedException para los thread sleep
     */
    private static void accionSacarCarta() throws ReiniciarJuego, InterruptedException {
        boolean salir = false;
        opcionCarta = -1;
        cartaValida = false;
        while (!salir) {
            opcionCarta = Datos.pedirEntero("Acción: ");
            if (opcionCarta == jugador.getNumCartas()) {
                // Opción Robar
                Mecanicas.robarCarta();
                salir = true;
            } else {
                try {
                    cartaValida = Mecanicas.cartaSacada(opcionCarta);
                    salir = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("La carta que quieres lanzar no esta dentro del limite de la baraja");
                } catch (NullPointerException e) {
                    System.out.println("No existe la carta seleccionada");
                    System.out.println(e.getLocalizedMessage());
                } catch (CartaLanzadaNoValida e) {
                    System.out.println(e.getMessage());
                    Mecanicas.cartaSacadaNoValida();
                    salir = true;
                }
            }
        }
    }

    /**
     * Método que funciona si la carta sacada es valida para realizar la accion de
     * sacarla
     * 
     * @param 'ninguno'
     * @throws ReiniciarJuego       para reinciar el juego cuando se quiera
     * @throws InterruptedException para los thread sleep
     */
    private static void cartaSacadaValida() throws InterruptedException, ReiniciarJuego {
        Carta cartaTirada;
        if (cartaValida) {
            cartaTirada = jugador.jugarCarta(opcionCarta);

            if (cartaTirada.getTipo() == Tipos.ESPECIAL) {
                Efectos.efectosCartasEspeciales(cartaTirada);
            }

            contextoPartida.getTablero().dejar(cartaTirada);
            System.out.println("La carta que has tirado es: " + cartaTirada);
            // Si el jugador se queda sin cartas el juego termina
            if (jugador.getNumCartas() == 0) {
                fin = true;
                Menus.nombreJugador = jugador.getNombre();
            }
        }
    }

    /**
     * Reanuda una partida previamente guardada restaurando el estado
     * desde el objeto contenedor contextoPartida
     * 
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando quiera
     * @throws @throws              SalirDelJuego para salir del juego cuando
     *                              quieras
     */
    public static void reanudarPartida() throws InterruptedException, ReiniciarJuego, SalirDelJuego {
        jugador = actual();
        int numeroCartasJugadorActual = jugador.getNumCartas();

        boolean jugadorSinCartas = false;
        if (numeroCartasJugadorActual == 0) {
            jugadorSinCartas = true;
            fin = true;
            Menus.nombreJugador = jugador.getNombre();
            partida();
        }
        if (!jugadorSinCartas) {
            if (contextoPartida.getJugadores() == null || contextoPartida.getTablero() == null) {
                System.out.println("Error: No se encontró información válida para reanudar la partida :(");
                Datos.pulsaEnter();
                Juego.iniciarJuego();
            } else {
                System.out.println("Restaurando parámetros de juego...");
                Thread.sleep(Datos.milisegundos);

                fin = false;

                System.out.println("Partida restaurada :)");
                Datos.pulsaEnter();

                partida();
            }
        }
    }

    // Metodos atajo
    // Sirven para reducir codigo en efectos y en mecanicas
    public static Jugador actual() {
        return contextoPartida.jugadorActual();
    }

    public static Tablero tablero() {
        return contextoPartida.getTablero();
    }

    public static Turno turnos() {
        return contextoPartida.getControladorTurnos();
    }

    public static int nJugadores() {
        return contextoPartida.getCantidadJugadores();
    }

    public static void siguiente() {
        contextoPartida.pasarSiguiente();
    }
}