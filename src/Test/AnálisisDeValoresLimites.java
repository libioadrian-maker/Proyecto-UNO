package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import Enumerados.Color;
import Excepciones.CartaLanzadaNoValida;
import Objetos.CartaNormal;

/**
 * AVL, analisis de valores limites, para mirar que los limites esten bien
 * establecidos
 * 
 * @author DaniS
 */
public class AnálisisDeValoresLimites {

    @Test
    public void testAVL_NumeroMinimoEs0() {
        CartaNormal carta = new CartaNormal(0, Color.ROJO);
        assertEquals(0, carta.getNumero());
    }

    @Test
    public void testAVL_NumeroMaximoEs9() {
        CartaNormal carta = new CartaNormal(9, Color.ROJO);
        assertEquals(9, carta.getNumero());
    }

    @Test
    public void testAVL_NumeroLimiteFronterizo1() {
        CartaNormal carta = new CartaNormal(1, Color.ROJO);
        assertEquals(1, carta.getNumero());
    }

    @Test
    public void testAVL_NumeroLimiteFronterizo8() {
        CartaNormal carta = new CartaNormal(8, Color.ROJO);
        assertEquals(8, carta.getNumero());
    }

    @Test
    public void testAVL_MesaNula_PrimeraprimeraJugada() throws CartaLanzadaNoValida {
        CartaNormal carta = new CartaNormal(5, Color.ROJO);
        assertTrue(carta.puedePonerseSobre(null));
    }

    @Test
    public void testAVL_MesaNulaConNumeroMínimo() throws CartaLanzadaNoValida {
        CartaNormal carta = new CartaNormal(0, Color.ROJO);
        assertTrue(carta.puedePonerseSobre(null));
    }

    @Test
    public void testAVL_MesaNulaConNumeroMaximo() throws CartaLanzadaNoValida {
        CartaNormal carta = new CartaNormal(9, Color.ROJO);
        assertTrue(carta.puedePonerseSobre(null));
    }
}
