package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import Enumerados.Color;
import Excepciones.CartaLanzadaNoValida;
import Objetos.CartaNormal;

/**
 * Conjetura de errores, son basicamente la experiencia de una idea que puede fallar
 * 
 * @author DaniS
 */
public class ConjeturaDeErrores {
        @Test
    public void testConjetura_ColorIncorrectoNumeroIncorrecto() {
        CartaNormal cartaMesa = new CartaNormal(2, Color.ROJO);
        CartaNormal cartaJugador = new CartaNormal(7, Color.VERDE);
        assertThrows(CartaLanzadaNoValida.class, () -> cartaJugador.puedePonerseSobre(cartaMesa));
    }

    @Test
    public void testConjetura_Numero0ComparaCorrectamente() throws CartaLanzadaNoValida {
        CartaNormal cartaMesa = new CartaNormal(0, Color.ROJO);
        CartaNormal cartaJugador = new CartaNormal(0, Color.AZUL);
        assertTrue(cartaJugador.puedePonerseSobre(cartaMesa));
    }

    @Test
    public void testConjetura_SetNumeroActualizaValor() {
        CartaNormal carta = new CartaNormal(5, Color.ROJO);
        carta.setNumero(8);
        assertEquals(8, carta.getNumero());
    }

    @Test
    public void testConjetura_SetColorActualizaValor() {
        CartaNormal carta = new CartaNormal(5, Color.ROJO);
        carta.setColor(Color.AZUL);
        assertEquals(Color.AZUL, carta.getColor());
    }

    @Test
    public void testConjetura_EqualsMismoObjeto() {
        CartaNormal carta = new CartaNormal(5, Color.ROJO);
        assertEquals(carta, carta);
    }

    @Test
    public void testConjetura_EqualsDistintoNumero() {
        CartaNormal carta1 = new CartaNormal(5, Color.ROJO);
        CartaNormal carta2 = new CartaNormal(6, Color.ROJO);
        assertNotEquals(carta1, carta2);
    }

    @Test
    public void testConjetura_EqualsDistintoColor() {
        CartaNormal carta1 = new CartaNormal(5, Color.ROJO);
        CartaNormal carta2 = new CartaNormal(5, Color.AZUL);
        assertNotEquals(carta1, carta2);
    }

    @Test
    public void testConjetura_EqualsCartasIdenticas() {
        CartaNormal carta1 = new CartaNormal(5, Color.ROJO);
        CartaNormal carta2 = new CartaNormal(5, Color.ROJO);
        assertEquals(carta1, carta2);
    }

    @Test
    public void testConjetura_HashCodeConsistencia() {
        CartaNormal carta1 = new CartaNormal(5, Color.ROJO);
        CartaNormal carta2 = new CartaNormal(5, Color.ROJO);
        assertEquals(carta1.hashCode(), carta2.hashCode());
    }

    @Test
    public void testConjetura_ToStringNoNull() {
        CartaNormal carta = new CartaNormal(5, Color.ROJO);
        assertNotNull(carta.toString());
    }

    @Test
    public void testConjetura_ToStringContienelNumero() {
        CartaNormal carta = new CartaNormal(5, Color.ROJO);
        assertTrue(carta.toString().contains("5"));
    }

    @Test
    public void testConjetura_ComparaciónMismoNumero() {
        CartaNormal carta1 = new CartaNormal(5, Color.ROJO);
        CartaNormal carta2 = new CartaNormal(5, Color.AZUL);
        int comparacion = carta1.compareTo(carta2);
        assertTrue(comparacion != 0 || carta1.equals(carta2));
    }

    @Test
    public void testConjetura_NumeroNegativoFueraRango() {
        CartaNormal carta = new CartaNormal(-1, Color.ROJO);
        CartaNormal cartaMesa = new CartaNormal(5, Color.ROJO);
        assertThrows(CartaLanzadaNoValida.class, () -> carta.puedePonerseSobre(cartaMesa));
    }

    @Test
    public void testConjetura_NumeroMayorA9FueraRango() {
        CartaNormal carta = new CartaNormal(15, Color.ROJO);
        CartaNormal cartaMesa = new CartaNormal(5, Color.ROJO);
        assertThrows(CartaLanzadaNoValida.class, () -> carta.puedePonerseSobre(cartaMesa));
    }
}
