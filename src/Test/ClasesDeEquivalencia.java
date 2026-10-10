package Test;

import static org.junit.Assert.*;
import org.junit.Test;

import Enumerados.Color;
import Objetos.CartaNormal;
import Excepciones.CartaLanzadaNoValida;

/**
 * Clases de equivalencia del proyecto, que consiste en dividir los datos de
 * entrada en grupos o conjuntos de valores que el sistema debe procesar de
 * forma similar
 * 
 * @author DaniS
 */
public class ClasesDeEquivalencia {

    // ============================================
    // CLASES DE EQUIVALENCIA
    // ============================================

    @Test
    public void testClaseEquivalencia_CartaNumeroValido0() {
        CartaNormal carta = new CartaNormal(0, Color.ROJO);
        assertEquals(0, carta.getNumero());
    }

    @Test
    public void testClaseEquivalencia_CartaNumeroValido5() {
        CartaNormal carta = new CartaNormal(5, Color.ROJO);
        assertEquals(5, carta.getNumero());
    }

    @Test
    public void testClaseEquivalencia_CartaNumeroValido9() {
        CartaNormal carta = new CartaNormal(9, Color.ROJO);
        assertEquals(9, carta.getNumero());
    }

    @Test
    public void testClaseEquivalencia_CartaColorRojo() {
        CartaNormal carta = new CartaNormal(5, Color.ROJO);
        assertEquals(Color.ROJO, carta.getColor());
    }

    @Test
    public void testClaseEquivalencia_CartaColorVerde() {
        CartaNormal carta = new CartaNormal(5, Color.VERDE);
        assertEquals(Color.VERDE, carta.getColor());
    }

    @Test
    public void testClaseEquivalencia_CartaColorAzul() {
        CartaNormal carta = new CartaNormal(5, Color.AZUL);
        assertEquals(Color.AZUL, carta.getColor());
    }

    @Test
    public void testClaseEquivalencia_CartaColorAmarillo() {
        CartaNormal carta = new CartaNormal(5, Color.AMARILLO);
        assertEquals(Color.AMARILLO, carta.getColor());
    }

    @Test
    public void testClaseEquivalencia_CartaColorNegro() {
        CartaNormal carta = new CartaNormal(5, Color.NEGRO);
        assertEquals(Color.NEGRO, carta.getColor());
    }

    @Test
    public void testClaseEquivalencia_PuedePonerseMismoColor() throws CartaLanzadaNoValida {
        CartaNormal cartaMesa = new CartaNormal(7, Color.ROJO);
        CartaNormal cartaJugador = new CartaNormal(3, Color.ROJO);
        assertTrue(cartaJugador.puedePonerseSobre(cartaMesa));
    }

    @Test
    public void testClaseEquivalencia_PuedePonerseMismoNumero() throws CartaLanzadaNoValida {
        CartaNormal cartaMesa = new CartaNormal(5, Color.ROJO);
        CartaNormal cartaJugador = new CartaNormal(5, Color.AZUL);
        assertTrue(cartaJugador.puedePonerseSobre(cartaMesa));
    }

    @Test
    public void testClaseEquivalencia_CartaNegraSobreCualquierColor() throws CartaLanzadaNoValida {
        CartaNormal cartaMesa = new CartaNormal(3, Color.AMARILLO);
        CartaNormal cartaNegra = new CartaNormal(7, Color.NEGRO);
        assertTrue(cartaNegra.puedePonerseSobre(cartaMesa));
    }

    @Test
    public void testClaseEquivalencia_CartaNoValidaLanzaExcepcion() {
        CartaNormal cartaMesa = new CartaNormal(2, Color.ROJO);
        CartaNormal cartaJugador = new CartaNormal(8, Color.AZUL);
        assertThrows(CartaLanzadaNoValida.class, () -> cartaJugador.puedePonerseSobre(cartaMesa));
    }
}
