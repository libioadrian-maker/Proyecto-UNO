package MetodosSecundarios;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase que gestiona la persistencia del ranking de victorias 
 * Implementa las operaciones CRUD sobre la tabla perfil_jugador
 * utilizando el JDBC.
 *
 * @author DaniS y Libio
 */
public class GestorRankingBD {

    // Ruta del archivo de base de datos SQLite
    private static final Path RUTA_PARTIDA = Paths.get("src", "Archivos").resolve("ranking.db");
    private static final String NOMBRE_ARCHIVO_RANKING = "jdbc:sqlite:" + RUTA_PARTIDA.toString();

    /**
     * Crea la tabla perfil_jugador si no existe todavia. La tabla almacena un 
     * codigo autoincremental como clave primaria, el nombre unico del jugador 
     * y su contador de victorias
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void inicializarBD() {
        try {
            Class.forName("org.sqlite.JDBC");
            String tablaPerfilJugador = "CREATE TABLE IF NOT EXISTS perfil_jugador ("
                    + "codigo INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre VARCHAR(100) UNIQUE, "
                    + "victorias INTEGER"
                    + ");";

            try (Connection conexion = DriverManager.getConnection(NOMBRE_ARCHIVO_RANKING);
                    Statement declaracion = conexion.createStatement()) {
                declaracion.execute(tablaPerfilJugador);
                System.out.println("Base de datos de Ranking inicializada.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("No se encuentra el driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }

    /**
     * Registra una victoria para el jugador. Primero consulta la tabla
     * mediante un SELECT para comprobar si el jugador ya existe. Si existe,
     * ejecuta un UPDATE que incrementa su contador en uno. Si no existe,
     * ejecuta un INSERT que crea su perfil con una victoria.
     * Los valores se pasan de forma segura mediante PreparedStatement con
     * parametros de sustitucion (?) para prevenir inyecciones SQL.
     *
     * @param nombreJugador nombre del jugador ganador tal como aparece en la partida
     */
    public static void registrarVictoria(String nombreJugador) {
        String selectJugador = "SELECT victorias FROM perfil_jugador WHERE nombre = ?";
        String insertNuevoJugador = "INSERT INTO perfil_jugador (nombre, victorias) VALUES (?, 1)";
        String updateJugadorExistente = "UPDATE perfil_jugador SET victorias = victorias + 1 WHERE nombre = ?";
        boolean existe;

        try (Connection conn = DriverManager.getConnection(NOMBRE_ARCHIVO_RANKING)) {

            try (PreparedStatement pstmtSelect = conn.prepareStatement(selectJugador)) {
                pstmtSelect.setString(1, nombreJugador);
                try (ResultSet resultado = pstmtSelect.executeQuery()) {
                    existe = resultado.next();
                }
            }

            if (existe) {
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateJugadorExistente)) {
                    pstmtUpdate.setString(1, nombreJugador);
                    pstmtUpdate.executeUpdate();
                }
            } else {
                try (PreparedStatement pstmtInsert = conn.prepareStatement(insertNuevoJugador)) {
                    pstmtInsert.setString(1, nombreJugador);
                    pstmtInsert.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar victoria: " + e.getMessage());
        }
    }

    /**
     * Consulta la tabla perfil_jugador y muestra por consola los cinco jugadores
     * con mas victorias.
     * Utiliza Statement y ResultSet para ejecutar el SELECT y 
     * recorrer cada fila devuelta con el metodo next(), extrayendo el nombre con 
     * getString y las victorias con getInt
     */
    public static void mostrarTopJugadores() {
        String sql = "SELECT nombre, victorias FROM perfil_jugador ORDER BY victorias DESC LIMIT 5";

        try (Connection conn = DriverManager.getConnection(NOMBRE_ARCHIVO_RANKING);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- TOP MEJORES JUGADORES (GLOBAL) ---");
            while (rs.next()) {
                System.out.println("Jugador: " + rs.getString("nombre") +
                        " | Victorias: " + rs.getInt("victorias"));
            }
            System.out.println("--------------------------------------\n");

        } catch (SQLException e) {
            System.out.println("Error al consultar el ranking: " + e.getMessage());
        }
    }

    /**
     * Método que elimina de la base de datos el perfil del jugador indicado mediante
     * DELETE. Comprueba el numero de filas afectadas que devuelve para
     * informar si el borrado fue exitoso o si el jugador no existia en la tabla
     *
     * @param nombreJugador nombre del jugador cuyo perfil se desea eliminar
     */
    public static void eliminarPerfil(String nombreJugador) {
        String sql = "DELETE FROM perfil_jugador WHERE nombre = ?";

        try (Connection conn = DriverManager.getConnection(NOMBRE_ARCHIVO_RANKING);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreJugador);
            int afectadas = pstmt.executeUpdate();

            if (afectadas > 0) {
                System.out.println("Perfil eliminado con éxito.");
            } else {
                System.out.println("No se encontró al jugador.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar perfil: " + e.getMessage());
        }
    }

    /**
     * Modifica la estructura de la tabla perfil_jugador anadiendo la columna
     * racha_actual de tipo INTEGER, mediante ALTER TABLE.
     * Si la columna ya existe de una ejecucion anterior, SQLite lanza una excepcion
     */
    public static void añadirColumnaRacha() {
        String sql = "ALTER TABLE perfil_jugador ADD COLUMN racha_actual INTEGER DEFAULT 0";

        try (Connection conn = DriverManager.getConnection(NOMBRE_ARCHIVO_RANKING);
                Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabla alterada: Nueva columna 'racha_actual' añadida.");

        } catch (SQLException e) {
            System.out.println("Aviso al alterar la tabla (quizás la columna ya exista): " + e.getMessage());
        }
    }
}