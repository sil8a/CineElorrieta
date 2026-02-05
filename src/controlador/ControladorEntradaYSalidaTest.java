package controlador;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ControladorEntradaYSalidaTest {
	private ControladorEntradaYSalida controlador;
    private final String CARPETA = "ticket";
    private final String ARCHIVO = "ticket/ticket_compra.txt";
	@Before
	public void setUp() throws Exception {
		controlador = new ControladorEntradaYSalida();
		File directorio = new File(CARPETA);
        if (!directorio.exists()) {
            directorio.mkdir();
        }
    }
	

	@After
	public void tearDown() throws Exception {
		File fichero = new File(ARCHIVO);
        if (fichero.exists()) {
            fichero.delete();}
	}

	@Test
	public void testImprimirTicket() {
	
		//arrange
		String contenidoPrueba = "*************"+
		"Entrada Cine\n"+ "Pelicula: Matrix\n"+"Precio: 19€"+"************";
		//act
		controlador.imprimirTicket(contenidoPrueba);
		//assert
		File ficheroGenerado = new File(ARCHIVO);
		assertTrue("El archivo debería existir después de imprimir", ficheroGenerado.exists());

		String contenidoLeido = leerFichero(ficheroGenerado); 
		assertEquals("El contenido del ticket debe coincidir", contenidoPrueba.trim(),
				contenidoLeido.trim()); } private String leerFichero(File fichero)
				{ try { return new String(java.nio.file.Files.readAllBytes(fichero.toPath())); } 
				catch (Exception e) { return ""; } }
		




	}


