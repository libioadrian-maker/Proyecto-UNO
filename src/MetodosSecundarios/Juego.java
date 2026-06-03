package MetodosSecundarios;

import java.util.ArrayList;

import Enumerados.ModoEjecucion;
import Excepciones.ReiniciarJuego;
import Excepciones.SalirDelJuego;
import Objetos.VentanaConfiguracion;

/**
 * Clase que establece la raiz del juego
 *
 * @author DaniS y Libio
 */
public class Juego {

    public static ArrayList<String> nombresCargados = new ArrayList<>();
    public static int cantidadActualJugadores;
    private static boolean salir;
    private static int opcion;

    /**
     * Inicio del juego donde se inicializan variables como los nombres, el modo y
     * la cantida de jugadores
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando se quiera
     */
    public static void iniciarJuego() throws InterruptedException, ReiniciarJuego {
        Datos.saltoDeLineas();

        GestorRankingBD.inicializarBD();

        // Configuración inicial por defecto si no se reanuda
        cantidadActualJugadores = 2;
        nombresCargados.clear();
        nombresCargados.add("Jugador1");
        nombresCargados.add("Jugador2");
        Menus.modoDeJuego = "Clásico";

        menuInicio();
    }

    /**
     * Menu de opciones que permite cambiar el modo de ejecucion y salir del juego
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando se quiera
     */
    public static void menuInicio() throws InterruptedException, ReiniciarJuego {
        opcion = 0;
        salir = false;
        while (!salir) {
            try {
                opcion = Menus.menuEjecucion();
                switch (opcion) {
                    case 1:
                        ejecutarSistemaCompleto(ModoEjecucion.NORMAL);
                        salir = true;
                        break;
                    case 2:
                        ejecutarSistemaCompleto(ModoEjecucion.DEVELOPER);
                        salir = true;
                        break;
                    default:
                        Datos.entradaIncorrecta();
                        Thread.sleep(Datos.milisegundos);
                        break;
                }
            } catch (SalirDelJuego e) {
                System.out.println(e.getMessage());
                salir = true;
            }
        }
    }

    /**
     * A partir del modo seleccionado especifica las funcionalidades del modo de
     * ejecucion seleccionado
     * 
     * @param modo el modo de ejecución (NORMAL o DEVELOPER)
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando se quiera
     */
    public static void ejecutarSistemaCompleto(ModoEjecucion modo) throws InterruptedException, ReiniciarJuego {
        if (modo == ModoEjecucion.NORMAL) {
            Pantallas.pantallaUNO();
        } else {
            Datos.milisegundos = 0;
            GestorRankingBD.añadirColumnaRacha(); // Añade la columna racha_actual si no existe
        }
        sistema();
    }

    /**
     * Método que ejecuta el sistema de juego completo con sus menús
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando se quiera
     */
    public static void sistema() throws InterruptedException, ReiniciarJuego {
        opcion = 0;
        try {
            while (true) {
                opcion = Menus.menuBienvenida();
                switch (opcion) {
                    case 1:
                        flujoDelSistema();
                        break;
                    case 2:
                        throw new SalirDelJuego();
                    default:
                        Datos.entradaIncorrecta();
                        break;
                }
            }
        } catch (ReiniciarJuego e) {
            reinicio();
            System.out.println("\n" + e.getMessage());
            Thread.sleep(Datos.milisegundos);
            Datos.pulsaEnter();
            Juego.iniciarJuego();
        } catch (SalirDelJuego e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que reproduce el flujo por el que circula el sistema
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando se quiera
     * @throws SalirDelJuego        para salir del juego cuando quieras
     */
    public static void flujoDelSistema() throws InterruptedException, ReiniciarJuego, SalirDelJuego {
        // Comprobación de partida guardada existente
        opcion = AlmacenamientoPartida.partidaGuardada();
        salir = false;
        while (!salir) {
            opcion = Menus.menuInicio();

            switch (opcion) {
                case 1: // Configurar modo de juego
                    Menus.menuModoDeJuego();
                    break;
                case 2: // Configurar nombres y cantidad de jugadores
                    VentanaConfiguracion ventanaConfiguracion = new VentanaConfiguracion();
                    ventanaConfiguracion.setVisible(true);
                    // Congela el terminal para concentrar el foco en la pantalla visible
                    while (ventanaConfiguracion.isVisible()) {
                        Thread.sleep(200);
                    }
                    break;
                case 3: // Mostrar instrucciones
                    AlmacenamientoDatos.pantallaReglas();
                    break;
                case 4: // Iniciar una partida
                    Datos.saltoDeLineas();
                    UnoEngine.inicioPartida(nombresCargados, cantidadActualJugadores);
                    break;
                case 5: // Salir del programa
                    salir = true;
                    break;
                default:
                    Datos.entradaIncorrecta();
                    break;
            }
        }
    }

    /**
     * Flujo de reinicio del sistema
     * 
     * @param 'ninguno'
     * @throws InterruptedException para los thread sleep
     * @throws ReiniciarJuego       para reiniciar el juego cuando se quiera
     */
    public static void reinicio() throws InterruptedException, ReiniciarJuego {
        System.out.println("\n\nReiniciando...");
        Thread.sleep(2000);
        Datos.saltoDeLineas();

        Menus.menuReinicio();

    }

    /**
     * Metodo que resetea el estado del juego al predeterminado cuando se reiniciar
     * el juego
     * 
     * @param 'ninguno'
     */
    public static void resetearEstado() {
        nombresCargados.clear();
        cantidadActualJugadores = 2;

        Menus.modoDeJuego = "Clásico";
        Menus.jugadores = "2";
        Menus.nombreJugador = "Invitado";

        Datos.milisegundos = 1000;

        System.out.println("\nEstado del juego restablecido correctamente.");
    }
}
